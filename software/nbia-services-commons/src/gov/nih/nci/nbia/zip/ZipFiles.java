/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.14  2007/07/06 20:00:16  panq
* Modified to handle duplicate files.
*
* Revision 1.13  2007/01/31 15:26:37  panq
* Upgraded the level of log from info to error for missing file log.
*
* Revision 1.12  2006/10/23 21:31:37  dietrich
* Defect 138 - Changed to make SOP instance UID the file name when images are downloaded
*
* Revision 1.11  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.zip;

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
 * @author Prashant Shah - NCICB/SAIC
 *
 *
 *
 */
public class ZipFiles extends AbstractFileZipper {
    private static final int BUFFER = 204800;

    private static final int DEFAULT_COMPRESSION = 1;

    private static final String FILE = "File ";
    private static Logger logger = Logger.getLogger(ZipFiles.class);

    // Stream to read data being zipped
    private BufferedInputStream origin;

    // Stream to write to the zip file
    private ZipOutputStream out;

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
        origin = null;
        out = null;
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
            this.out = new ZipOutputStream(new BufferedOutputStream(dest));

            // Set Default Compression level
            out.setLevel(ZipFiles.DEFAULT_COMPRESSION);
            out.setMethod(ZipOutputStream.DEFLATED);
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
        File thisFile = new File(filePath);

        if (thisFile.exists()) {
            try {
                FileInputStream fi = new FileInputStream(thisFile);

                origin = new BufferedInputStream(fi, BUFFER);

                // If a new file name is not passed in, use the name of the file
                if((fileName == null) || fileName.equals("")){
                    fileName = thisFile.getName();
                }

                ZipEntry entry = new ZipEntry(directoryInZip + File.separator +
                       fileName );
                out.putNextEntry(entry);

                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }

                origin.close();
            } catch (FileNotFoundException e) {
                logger.error(FILE + filePath + " not found !!", e);
            } catch (IOException e) {
                if (e.getMessage().startsWith("duplicate entry:")){
                    logger.error("!!!!Find duplicated files = "+filePath + "|"+ fileName);
                }
                else  {
                    logger.error("!!!!zipping error when zip File : " + filePath + "|"+ fileName);
                    logger.error("Could not write to Zip File ", e);
                    throw new Exception("Could not write to Zip File ", e);
                }
            }
        } else {
            // Log message if file does not exist
            logger.error(FILE + thisFile.getName() +
                " does not exist on file system");
        }
    }

    public void closeFile() {
        try {
            out.flush();
            out.close();
            out = null;
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
}
