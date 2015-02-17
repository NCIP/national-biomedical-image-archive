/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: ACRINPreProcessUtil.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
* Revision 1.5  2006/09/28 19:29:00  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.util.preprocessor;

import gov.nih.nci.ncia.util.DcmUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;


public class ACRINPreProcessUtil implements IPreProcessor {
    final static Logger logger = Logger.getLogger(ACRINPreProcessUtil.class);
    ArrayList<File> unProcessedFilesQueue = new ArrayList<File>();

    public String preprocess(String dir) {
        // TODO Auto-generated method stub
        String logMessage = null;

        process(dir);

        logMessage = "<p>ACRINPREPREPROCESS completes all files under the directory <p>" +
            dir;

        if (!unProcessedFilesQueue.isEmpty()) {
            logMessage = "<p>The following file(s) is failed to be processed (Please check the log for the detail):";

            Iterator<File> iter = unProcessedFilesQueue.iterator();

            while (iter.hasNext()) {
                logMessage += ("<p>" + iter.next());
            }
        }

        if (unProcessedFilesQueue != null) {
            unProcessedFilesQueue.clear();
        }

        return logMessage;
    }

    public void process(String filename) {
        File tempFile = new File(filename);

        if (tempFile.isDirectory()) {
            File[] filelist = tempFile.listFiles();

            for (int i = 0; i < filelist.length; i++) {
                process(filelist[i].getAbsolutePath());
            }
        }

        if (tempFile.getName().endsWith(".dcm")) {
            try {
                DcmUtil.eraseDICOMHeader(tempFile);
            } catch (IOException e) {
                logger.error("Unexpected Exception: " + e.getMessage());
                unProcessedFilesQueue.add(tempFile);
            }
        }
    }
}
