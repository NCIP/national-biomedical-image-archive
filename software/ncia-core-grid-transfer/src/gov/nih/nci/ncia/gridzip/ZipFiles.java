/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.gridzip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;


/**
 * @author lethai 
 */
public class ZipFiles extends AbstractFileZipper {
    public static final int BUFFER = 16384;
    public static final int NO_COMPRESSION = 0; 
    public static final int DEFAULT_COMPRESSION = 1;
    public static final int MID_COMPRESSION = 4;
    public static final int MAX_COMPRESSION = 9;
    public static final String FILE = "File ";
    private static Logger logger = Logger.getLogger(ZipFiles.class);

    // Stream to read data being zipped
    private BufferedInputStream bis;

    // Stream to write to the zip file
    private ZipOutputStream zos;

    // Buffer
    private byte[] data;

    // List of file names that have been zipped
    // There can be several if a very large amount of data is zipped
    private List<String> fileList = new ArrayList<String>();

    // Zip file being created at this point in time
    private String outputFileName = null;

    /**
     *
     */
    public ZipFiles() {
        bis = null;
        zos = null;
    }

    // Set output Zipped file in init() method
    public void startNewFile(String fileName, int sequenceNumber)
        throws Exception {
        try {
            if (sequenceNumber == 0) {
                // This is the first zip file for this data. 
                // Just use the name passed in
                this.outputFileName = fileName;
            } else {
                // This is not the first zip file for this data
                // Append a number (e.g. _1 _2) to the file name
                int dotIndex = fileName.lastIndexOf('.');
                String beforeDot = fileName.substring(0, dotIndex);
                outputFileName = beforeDot + "_" + sequenceNumber + ".zip";
            }

            // Add file to the list to be returned
            fileList.add(outputFileName);

            logger.debug("output file " + outputFileName);

            FileOutputStream dest = new FileOutputStream(outputFileName);
            logger.debug("dest file " + dest);
            this.zos = new ZipOutputStream(new BufferedOutputStream(dest));

            // Set Default Compression level
            zos.setLevel(ZipFiles.DEFAULT_COMPRESSION);
            zos.setMethod(ZipOutputStream.DEFLATED);
            this.data = new byte[ZipFiles.BUFFER];
        } catch (FileNotFoundException e) {
            logger.error(FILE + outputFileName + " not found !!", e);
            throw new Exception(FILE + outputFileName + " not found !!", e);
        }
    }

    /**
     * Returns the file size of the file being zipped
     */
    public long getFileSize() {
        return new File(outputFileName).length();
    }

    /**
     * Adds files to the zip archive.
     *
     * @param directoryInZip - directory location inside of the zip where file will be included in the zip
     * @param filePath - location of the file to be added to the zip
     * @param fileName - name to be used for the file in the zip
     */
    public void zip(String directoryInZip, String filePath, String fileName)
        throws Exception {
        File thisFile = new File(filePath );

        if (thisFile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(thisFile);

                bis = new BufferedInputStream(fis);

                // If a new file name is not passed in, use the name of the file
                if((fileName == null) || fileName.equals("")){
                    fileName = thisFile.getName();
                }
                
                ZipEntry entry = new ZipEntry(directoryInZip + File.separator +
                       fileName );
                zos.putNextEntry(entry);

                int count;
                data = new byte[ZipFiles.BUFFER];
               
                while ((count = bis.read(data, 0, data.length)) > 0) {
                    zos.write(data, 0, count);
                }                
                closeInputStream(bis);
            } catch (FileNotFoundException e) {
                logger.error(FILE + filePath + " not found !!", e);
                throw new Exception("FileNotFoundException: " + e);
            } catch (IOException e) {
                if (e.getMessage().startsWith("duplicate entry:")){
                    logger.error("!!!!Find duplicated files = "+filePath + "|"+ fileName);
                    closeInputStream(bis);
                }
                else  { 
                    logger.error("!!!!zipping error when zip File : " + filePath + "|"+ fileName);
                    logger.error("Could not write to Zip File ", e);
                    throw new Exception("!!!!zipping error when zip File : " , e);
                }
            }finally{
            	closeInputStream(bis);
            }
        } else {
            // Log message if file does not exist
            logger.error(FILE + thisFile.getPath() + thisFile.getName() +
                " does not exist on file system");
            throw new Exception (FILE + thisFile.getPath() + thisFile.getName() +
                " does not exist on file system");
        }
        
    }

    public void closeFile() {
        try {
            zos.flush();
            zos.close();
            zos = null;
        } catch (IOException e) {
            logger.error("Error Flushing or closing Zipped file in Zipfiles destroy() method ",
                e);
        }
    }

    /**
     * Returns a list of zip files that have been created by this object
     * There can be more than one for very large data sets
     */
    public List<String> getListOfZipFiles() {
        return fileList;
    }
    
    private  void closeInputStream(BufferedInputStream bis) throws IOException{
    	if(bis != null){
        	bis.close();
        	bis = null;
        }
    }
}
