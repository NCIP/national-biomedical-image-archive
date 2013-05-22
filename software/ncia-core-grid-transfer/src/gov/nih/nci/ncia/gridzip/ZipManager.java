/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.ncia.gridzip;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author lethai
 *
 */
public class ZipManager {
	private static Logger logger = Logger.getLogger(ZipManager.class);
	
    private String destinationFileName; // hold the name of the zipfile
    private String destinationFilePath; // hold the path for the destinationFileName
    private List<ZippingDTO> zdto;
    
    public String getDestinationFileName() {
		return destinationFileName;
	}

	public void setDestinationFileName(String destinationFileName) {
		this.destinationFileName = destinationFileName;
	}

	public String getDestinationFilePath() {
		return destinationFilePath;
	}

	public void setDestinationFilePath(String destinationFilePath) {
		this.destinationFilePath = destinationFilePath;
	}

	public List<ZippingDTO> getZdto() {
		return zdto;
	}

	public void setZdto(List<ZippingDTO> zdto) {
		this.zdto = zdto;
	}
	
	/*
    
	@Override
	public void run() {
		try {
            zip();
        } catch (Exception e) {
            logger.error("Unable to complete zipping of file " + destinationFilePath  +
                destinationFileName, e);
        }
	}*/
	
	public void zip() throws Exception {
	    
		logger.info("Starting to zip: " + destinationFilePath +  destinationFileName);
        long startTime = System.currentTimeMillis();
        
        //This is the Zip Work Horse
        AbstractFileZipper zipit = FileZipperFactory.getInstance();

        // Initialize zipper
        try {
            zipit.startNewFile(destinationFilePath +  destinationFileName, 0);
            try{
            	logger.debug("Creating the zip");
        		
        		for(int i=0, n=zdto.size();i< n; i++){        			
        			String sopInstanceUid = zdto.get(i).getSopInstanceUidAsFileName();
        			String filePath  = zdto.get(i).getFilePath();
        			String internalFilePath =zdto.get(i).getProject() + File.separator + zdto.get(i).getPatientId() + 
            		File.separator + zdto.get(i).getStudyInstanceUid() + File.separator + zdto.get(i).getSeriesInstanceUid();
        			
        			logger.info("sop " + sopInstanceUid + " filePath: " + filePath + " internal path: " + internalFilePath);
        			this.zipFile(zipit, internalFilePath, filePath, sopInstanceUid);
        			
        		}
	       	    
            }catch(Throwable t){
            	logger.error("Error zipping....", t);
            	throw new Exception("Error zipping: "+ t.getMessage());
            }
            zipit.closeFile();
        } catch (Exception e) {
            logger.error("Destination file " + destinationFilePath + destinationFileName +
                " cannot be created ", e);
            throw e;
        }

        long endTime = System.currentTimeMillis();

        logger.info(" Total zipping time for file " +  destinationFilePath + destinationFileName + " was " + (endTime - startTime) + " ms.");

        
    }
	
	/**
	 * This private method was added to make it clearer where the actual addition of a file to the zip was
	 * happening. Not every file follows the convention of project/patient/study/series. Some go to the
	 * root.
	 * 
	 * @param zipper
	 * @param fileName
	 * @throws Exception
	 */
    private void zipFile(AbstractFileZipper zipper, String internalZipFileFolder,  String fileSystemPath, String fileName) throws Exception{
    	 
        logger.debug("zipFile() was called for:"+fileSystemPath);
        logger.debug("Adding to internal zip folder: "+internalZipFileFolder);
	    zipper.zip(internalZipFileFolder, fileSystemPath, fileName);
	   
    }

}
