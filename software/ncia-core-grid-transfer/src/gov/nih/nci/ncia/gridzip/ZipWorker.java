/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.gridzip;

import gov.nih.nci.ivi.utils.ZipEntryOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class ZipWorker implements Runnable {
	public ZipWorker(OutputStream outputStream,
			         Map<String, String> fileNames) {
		this.outputStream = outputStream;
		this.fileNames = fileNames;
	}

	public void run() {
        // now write to the output stream. for this test, use a zip
        // stream.
        // this is really to deal with the fact that we don't have a
        // good way to delimit the files.

        ZipEntryOutputStream zeos = null;
        BufferedInputStream dicomIn = null;
        ZipOutputStream zos = new ZipOutputStream(
                new BufferedOutputStream(outputStream));

        Set<String> keyset = fileNames.keySet();
        Iterator<String> iter = keyset.iterator();
        long start = System.currentTimeMillis();
        while (iter.hasNext()) {
            String sop =  iter.next();
             //System.out.println("sop: " + sop);
            String filePath = fileNames.get(sop);
             //System.out.println("filePath: " + filePath);
            try {
                zeos = new ZipEntryOutputStream(zos, sop,
                        ZipEntry.DEFLATED, -1);
                dicomIn = new BufferedInputStream(
                        new FileInputStream(filePath));

                byte[] data = new byte[BUFFER];
                int bytesRead = 0;
                while ((bytesRead = (dicomIn.read(data, 0, data.length))) > 0) {
                    zeos.write(data, 0, bytesRead);
                }
                
            } catch (IOException e1) {
                System.err.println("ERROR writing to zip entry "
                        + e1.getMessage());
                logger.error("Error writing to zip entry: ", e1);
                //throw new RuntimeException(e1);
            } finally {
                try {
                	dicomIn.close();
                    zeos.flush();
                    zeos.close();                   
                } catch (IOException e) {                    
                    logger.error("Error closing to zip entry: ", e);
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            zos.flush();
            zos.close();
        } catch (IOException e) {
            logger.error("Error closing to zip output stream : ", e);
            throw new RuntimeException(e);
        }

        long end = System.currentTimeMillis();
        logger.info("Total time to read and transfer "
                + fileNames.size() + " files: " + (end - start)
                + MILLI_SECONDS);
    }

	private OutputStream outputStream;
	private Map<String, String> fileNames;
    private static final int BUFFER = 16384;
    private static String MILLI_SECONDS = " milli seconds.";
    private static Logger logger = Logger.getLogger(ZipWorker.class);

}