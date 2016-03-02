/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.zip;

import gov.nih.nci.nbia.dto.AnnotationFileDTO;
import gov.nih.nci.nbia.dto.DicomFileDTO;
import gov.nih.nci.nbia.dto.ImageFileDTO;
import gov.nih.nci.nbia.util.StringUtil;
import gov.nih.nci.nbia.searchresult.SeriesSearchResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.commons.io.FileUtils;


/**
 * This class is the entry point for creating zip files from the various files.  These "various" files can come
 * from either the existing file system or be generated dynamically by the application.  The manager,
 * contains convenience methods to access the status of the zipping operations.  Though it was orginally
 * written to accomodate only Beans from the UI, it has now been extended to all the ability to add other
 * files as well.
 *
 * It also extends the thread class so that it can be run in an asynchronous manner in a seperate thread.
 *
 */
public class ZipManager extends Thread {

    /**
     * Callback interface to get finer grained progress reports from the zipper.
     *
     */
    public interface ZipManagerListener {
        /**
         * notification that some incremenet of progress has occurred
         */
        public void progressUpdate();

        /**
         * notification tht zipping is complete.
         */
        public void completed();
    }

    public void addZipManagerListener(ZipManagerListener listener) {
        listeners.add(listener);
    }

    public void removeZipManagerListener(ZipManagerListener listener) {
        listeners.remove(listener);
    }


    /**
     * Compute the percentage of series that have
     * been processed.  Used by the progress bar
     */
    public Integer getPercentProcessed() {
        //System.out.println("bytesZippedSoFar:"+bytesZippedSoFar);
        //System.out.println("totalBytesToZip:"+totalBytesToZip);

        if (totalBytesToZip != 0) {
            return new Double((bytesZippedSoFar * 100) / totalBytesToZip).intValue();
        }
        else {
            return 0;
        }
    }


    /**
     * This methods sets the collection of series that should be zipped up
     * once zip is invoked.
     */
    public void setItems(Map<String, SeriesSearchResult> items) {
        this.basketItems = items;

    }

    
    /**
     * This methods returns the collection of series
     */
    public Map<String, SeriesSearchResult> getItems() {
        return this.basketItems;
    }    
    
    /**
     * This sets the zip file that should be created.
     */
    public void setTarget(File targetZipFile) {
        this.destinationFile = targetZipFile;
    }

    private long getTotalBytesToZip(Map<String, SeriesSearchResult> basketItem)
    {
    	long size = 0;
    	
        for (SeriesSearchResult bsib : basketItem.values()) {
            logger.debug("SeriesDTO.getExactSize()="+bsib.computeExactSize());
            logger.debug("SeriesDTO.getAnnotationsSize()="+bsib.getAnnotationsSize());
            // Total up the size of all files to zip (including annotations)
            size += bsib.computeExactSize()- bsib.getAnnotationsSize();
        }
        return size;
    }
    
    /**
     * Actually does the zipping of the files.
     *
     * <p>return list of zip files that are CREATED.  This is usually one
     * but if broken into multiple files...
     */
    public List<String> zip() throws Exception {

        logger.info("Starting to zip: " + destinationFile);
        long startTime = System.currentTimeMillis();

        AbstractFileZipper zipit = FileZipperFactory.getInstance();
        // Initialize zipper
        try {
            zipit.startNewFile(destinationFile.getAbsolutePath(), sequenceNumber);

            totalBytesToZip = getTotalBytesToZip(basketItems);
            
            List<SeriesSearchResult> seriesToZip = new ArrayList<SeriesSearchResult>(basketItems.values());


            // Run queries to get the image data for chunks of series IDs
            // Doing this in chunks prevents too much data from coming back
            // or too many (over 1000) IDs being added to an IN clause
            int pointer = 0;

            boolean atLeastOneSeriesSuccessfullyRetrieved = false;
           
            studyIdToSeriesCntMap = new HashMap<String, Integer>();
            
            List<AnnotationFileDTO> annotationFilePaths = new ArrayList<AnnotationFileDTO>();
            StringBuffer seriesNotRetrieved = new StringBuffer();
            while (pointer < seriesToZip.size()) {
                // The list of IDs to put in this query
                List<SeriesSearchResult> seriesChunk = chunk(seriesToZip, pointer);

                pointer += NUMBER_OF_SERIES_TO_RETRIEVE_AT_ONE_TIME;
                System.out.println("total series to be download  " + seriesChunk.size());
                for(SeriesSearchResult seriesSearchResult : seriesChunk) {
                    SeriesFileRetriever seriesFileRetriever = SeriesFileRetrieverFactory.getSeriesFileRetriever();

                    Integer seriesCnt = studyIdToSeriesCntMap.get(seriesSearchResult.getStudyInstanceUid());
                    if(seriesCnt==null) {
                    	seriesCnt = 0;
                    }
                    
                    try {
                    	DicomFileDTO dicomFile = null;
                        try {
                            dicomFile = seriesFileRetriever.retrieveImages(seriesSearchResult);
                        } catch(Exception e) {
                            System.out.println("retry since there was exception for series " + seriesSearchResult.getSeriesInstanceUid() + e.getMessage() );
                            //retry the series once
                            dicomFile = seriesFileRetriever.retrieveImages(seriesSearchResult);
                        }
                        List<ImageFileDTO> dicomFilePaths = dicomFile.getImageFileDTOList();
                        annotationFilePaths = dicomFile.getAnnotationDTOList();
                        
                        if (dicomFilePaths == null){
                        	throw new Exception("No image File found!");
                        }
                        
                        zipListOfImages(seriesSearchResult, 
                        		        dicomFilePaths, 
                        		        zipit,
                        		        seriesCnt);
                        atLeastOneSeriesSuccessfullyRetrieved = true;
                    }
                    catch(Exception ex) {
                    	System.out.println(ex.getMessage() + " exception no retry for series " + seriesSearchResult.getSeriesInstanceUid());
                    	ex.printStackTrace();
                        //keep on trucking...maybe other series will be ok
                        seriesNotRetrieved.append(seriesSearchResult.getSeriesInstanceUid()).append(System.getProperty("line.separator"));
                    }

                    if(!noAnnotation) {
                        try {
                           // List<AnnotationFileDTO> annotationFilePaths = seriesFileRetriever.retrieveAnnotations(seriesSearchResult);

                            zipAnnotations(seriesSearchResult, 
                            		       annotationFilePaths, 
                            		       zipit,
                            		       seriesCnt);
                        }
                        catch(Exception ex) {
                            ex.printStackTrace();
                            //keep on trucking...maybe other series will be ok
                        }
                    }
                    

                    studyIdToSeriesCntMap.put(seriesSearchResult.getStudyInstanceUid(), 
                    		                  seriesCnt+1);                    
                    seriesFileRetriever.cleanupResultsDirectory();

                }
            }

            if(!atLeastOneSeriesSuccessfullyRetrieved) {
                createErrorMessageFile(zipit,null);
            }
            if(!StringUtil.isEmptyTrim(seriesNotRetrieved.toString())) {
            	createErrorMessageFile(zipit, "There was some network error during the download for series listed below. Please try again later."
            			+ System.getProperty("line.separator") + seriesNotRetrieved.toString());
            }
            zipit.closeFile();
        }
        catch (Exception e) {
            logger.error("Destination file " + destinationFile + " cannot be created ", e);
            throw e;
        }

        long endTime = System.currentTimeMillis();

        System.out.println(" Total zipping time for file " + destinationFile + " ( " +
                bytesZippedSoFar + "/" + totalBytesToZip + "bytes) was " + (endTime - startTime) + " ms.");

        wrapThingsUp();

        return zipit.getListOfZipFiles();
    }



    /**
     * {@inheritDoc}
     *
     * <p>Start the zipping process.
     */
    public void run() {
        try {
            zip();
        } catch (Exception e) {
            logger.error("Unable to complete zipping of file " +
                destinationFile, e);
        }
    }

    /**
     * Setter for breakIntoMultipleFileIfLarge
     *
     * @param flag
     */
    public void setBreakIntoMultipleFileIfLarge(boolean flag) {
        breakIntoMultipleFileIfLarge = flag;
    }

    /**
     * @return Returns the noAnnotation.
     */
    public boolean isNoAnnotation() {
        return noAnnotation;
    }

    /**
     * @param noAnnotation The noAnnotation to set.
     */
    public void setNoAnnotation(boolean noAnnotation) {
        this.noAnnotation = noAnnotation;
    }

    ////////////////////////////////////////////PRIVATE//////////////////////////////////////////

    /**
     * Maximum size for a single zip file (4GB)
     * Only applies if breakIntoMultipleFileIfLarge is true
     */
    private static final long MAX_ZIP_FILE_SIZE = 4000000000L;
    private static final int NUMBER_OF_SERIES_TO_RETRIEVE_AT_ONE_TIME = 400;
    private static Logger logger = Logger.getLogger(ZipManager.class);

    /**
     * Total number of bytes to zip for all series
     **/
    private long totalBytesToZip = 0;

    /**
     * Total number of bytes zipped up to this point
     **/
    private long bytesZippedSoFar = 0;

    /**
     * Bytes zipped in the current zip file (there can be multiple zip files for large data sets)
     **/
    private long bytesZippedInCurrentZipFile = 0;

    /**
     * Name of first zip file created for the data
     **/
    private File destinationFile;

    /**
     *  For a large data set, this number keeps track of which zip file is
     * being zipped.  0 is the first file
     **/
    private int sequenceNumber = 0;

    /**
     * Set to true if resulting zip files should be broken up
     * so that they don't become too large
     */
    private boolean breakIntoMultipleFileIfLarge = true;

    /**
     * List of series items to be zipped
     **/
    private Map<String, SeriesSearchResult> basketItems;

    /**
     * Whether to include annotations files from the series to be zipped.
     */
    private boolean noAnnotation = true;

    /**
     * List of objects to notify of progress/completion
     **/
    private List<ZipManagerListener> listeners = new ArrayList<ZipManagerListener>();

    private Map<String, Integer> studyIdToSeriesCntMap;
    
    /**
     * Zips a list of images passed in as a list of BasketImageItemBean
     */
    private void zipListOfImages(SeriesSearchResult bsib,
                                 Collection<ImageFileDTO> imageList,
                                 AbstractFileZipper zipit,
                                 int seriesCnt)
        throws Exception {
    	int imageCnt = 0;
        for (ImageFileDTO image : imageList) {
           zipFile(zipit,
                   bsib.getProject(),
                   bsib.getPatientId(),
                   bsib.getStudyInstanceUid(),
                   StringUtil.displayAsSixDigitString(seriesCnt), //bsib.getSeriesInstanceUid(),
                   image.getFileURI(),
                   image.getSize(),
                   StringUtil.displayAsSixDigitString(imageCnt) + ".dcm");
           if ( (imageCnt+1) <= imageList.size()) {
        	   fireProgressEvent();
           }
           imageCnt += 1;
        }
    }

    
    /**
     * Zips a list of annotation files passed in as a list of strings
     */
    private void zipAnnotations(SeriesSearchResult bsib,
                                List<AnnotationFileDTO> annotationFilePaths,
                                AbstractFileZipper zipit,
                                int seriesCnt) throws Exception {
        if (annotationFilePaths != null) {
            for (AnnotationFileDTO annotationFile : annotationFilePaths) {
                totalBytesToZip +=annotationFile.getFileSize().longValue();
                zipFile(zipit,
                        bsib.getProject(),
                        bsib.getPatientId(),
                        bsib.getStudyInstanceUid(),
                        StringUtil.displayAsSixDigitString(seriesCnt), //bsib.getSeriesInstanceUid(),
                        annotationFile.getFilePath(),
                        annotationFile.getFileSize().longValue(),
                        null);
            }
        }
    }


    /**
     * Internal method that creates the internal path for the file based on the following:
     * project/patient/study/series.  Then adds the file to the zipped file.
     *
     */
    private void zipFile(AbstractFileZipper zipit,
                         String project,
                         String patientId,
                         String studyId,
                         String seriesId,
                         String fileSystemPath,
                         Long fileSize,
                         String fileName) throws Exception {
         // Build the path inside of the zip file based on values passed in
        String internalZipFileFolder = project + File.separator +
                                       patientId + File.separator +
                                       studyId + File.separator +
                                       seriesId;

        zipFile(zipit, internalZipFileFolder, fileSystemPath, fileName, fileSize);

    }

    /**
     * This private method was added to make it clearer where the actual addition of a file to the zip was
     * happening. Not every file follows the convention of project/patient/study/series. Some go to the
     * root.
     */
    private void zipFile(AbstractFileZipper zipper,
                         String internalZipFileFolder,
                         String fileSystemPath,
                         String fileName,
                         Long fileSize) throws Exception{
         // Possibly start a new zip file instead of
        // adding to the current one
        if (breakIntoMultipleFileIfLarge) {
            // Determine the file size
            // Only do this if there has been a sufficient amount
            // of data added to the zip file.
            long zipFileSize = 0;

            if (bytesZippedInCurrentZipFile > MAX_ZIP_FILE_SIZE) {
                zipFileSize = zipper.getFileSize();
                long sizeWithThisFileIncluded = zipFileSize + fileSize;
                // See if the zip file would go over the limit if the
                // current file is added
                if (sizeWithThisFileIncluded > MAX_ZIP_FILE_SIZE) {
                    // If so, then close the streams and finalize the zip file
                    zipper.closeFile();
                    // Start a new zip file, increasing the sequence number
                    zipper.startNewFile(destinationFile.getAbsolutePath(), ++sequenceNumber);
                    // Reset the counter
                    bytesZippedInCurrentZipFile = 0;
                }
            }
        }
        System.out.println("zipFile() was called for:"+fileSystemPath);
        System.out.println("Adding to internal zip folder: "+internalZipFileFolder);
        logger.debug("Which has a size of: "+fileSize);
        zipper.zip(internalZipFileFolder, fileSystemPath, fileName);
        bytesZippedSoFar += fileSize;
        bytesZippedInCurrentZipFile += fileSize;
    }



    private void fireCompletedEvent() {
        //worth putting in another thread?  slow listener
        //could slow zipping... but overhead could slow overall
        //system
        for(ZipManagerListener listener : listeners) {
            listener.completed();
        }
        //listeners.clear();
    }

    private void fireProgressEvent() {
        //worth putting in another thread?  slow listener
        //could slow zipping... but overhead could slow overall
        //system
        for(ZipManagerListener listener : listeners) {
            listener.progressUpdate();
        }
    }

    /**
     * If not sites respond to series retrieval.... then put a single file
     * into zip explaining the problem.  A zip file must have at least one file....
     */
    private static void createErrorMessageFile(AbstractFileZipper zipit, String msg) throws Exception {
        File messageFile = File.createTempFile("pre", "suffix");
        if (!StringUtil.isEmptyTrim(msg)) {
        	FileUtils.writeStringToFile(messageFile, msg);
        } else {
        	FileUtils.writeStringToFile(messageFile,
                    "No series could be retrieved.  It is possible that all remote nodes are down.  Please try again later.");
        	        	
        }

        zipit.zip("",
                  messageFile.getAbsolutePath(),
                  "README.TXT");
        messageFile.delete();
    }

    private void wrapThingsUp() {
        //actual image file size and series total can vary by a bit?
    	bytesZippedSoFar = totalBytesToZip;

        //one last update now that we are done
        fireCompletedEvent();
        listeners.clear();
    }

    private static List<SeriesSearchResult> chunk(List<SeriesSearchResult> seriesToZip, int pointer) {
        // The list of IDs to put in this query
        List<SeriesSearchResult> seriesChunk = new ArrayList<SeriesSearchResult>();

        for (int i = 0; i < NUMBER_OF_SERIES_TO_RETRIEVE_AT_ONE_TIME; i++) {
            if ((pointer + i) < seriesToZip.size()) {
                seriesChunk.add(seriesToZip.get(pointer + i));
            }
        }

        return seriesChunk;
    }
}