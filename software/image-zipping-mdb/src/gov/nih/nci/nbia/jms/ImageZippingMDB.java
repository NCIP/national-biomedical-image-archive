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
* Revision 1.7  2007/10/01 12:22:10  bauerd
* *** empty log message ***
*
* Revision 1.6  2007/08/29 19:11:19  bauerd
* *** empty log message ***
*
* Revision 1.5  2007/08/20 18:01:38  bauerd
* *** empty log message ***
*
* Revision 1.4  2007/08/16 19:35:11  bauerd
* *** empty log message ***
*
* Revision 1.3  2007/08/14 16:01:57  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/07 19:21:01  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.23  2006/12/20 22:24:26  panq
* Modified for download images from the grid.
*
* Revision 1.22  2006/11/01 16:57:38  dietrich
* Changed back to bean managed
*
* Revision 1.21  2006/10/16 17:03:44  panq
* Add a checkbox to allow user to choose download with or without annotation files.
*
* Revision 1.20  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.jms;

import gov.nih.nci.nbia.basket.DownloadRecorder;
import gov.nih.nci.nbia.mail.MailManager;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.zip.ZipManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionManagementType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Performs asynchronous zipping of image and annotation files
 *
 *
 */
@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)
@MessageDriven(name = "ImageZippingMDB", activationConfig = {
@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/imageQueue"),
@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class ImageZippingMDB implements MessageListener {
    private Logger log = Logger.getLogger(ImageZippingMDB.class);


    /**
     * This method gets called every time a message is pulled off
     * of the queue
     *
     * @param arg0 - the message to be processed
     */
    public void onMessage(Message arg0) {
        log.debug("ImageZippingMDB.onMessage");

        ImageZippingMessage izm = null;

        // List of paths to zip files generated
        // There can be more than one for very large data sets
        List<String> listOfZipFiles;


        try {
            ObjectMessage om = (ObjectMessage) arg0;
            izm = (ImageZippingMessage) om.getObject();

            // Prevents a file from being zipped twice.
            // If the file exists (even only a part of it), don't try to zip
            // again.   This handles a situation where the bean times out, gets put back
            // on the queue and then is run again
            // In the future, we could use izm.getZipHistoryId() and Hibernate to load up the download
            // history object and set a flag on there where zipping is succesfully completed
            if ((new File(izm.getZipFilename())).exists()) {
                return;
            }

            // start zipping and wait for it to finish
            ZipManager zipper = new ZipManager();
            zipper.setName(izm.getZipFilename());
            zipper.setItems(izm.getItems());
            zipper.setTarget(new File(izm.getZipFilename()));
            zipper.setNoAnnotation(!izm.isIncludeAnnotation());
            listOfZipFiles = zipper.zip();

            log.info("Finish zipping file " + izm.getZipFilename());
        } catch (Throwable e) {
        	e.printStackTrace();
            log.error("Error zipping file " + izm.getZipFilename(), e);

            // Return so that email doesn't get sent
            return;
        }


        try {
            // send an email to user
            log.info("FTP_DOWNLOAD: " + izm.getEmailAddress());

            MailManager.sendFTPMail(izm.getEmailAddress(),
            		                izm.getUserName(),
            		                listOfZipFiles,
            		                izm.getNodeName());


            log.info("Finish sending email ... ");
        } catch (Throwable e) {
            log.error("Error while sending email");
        } finally {
            // Commit the transaction so that it won't run over and over again
            try {
               // ctx.getUserTransaction().commit();

                DownloadRecorder downloadRecorder = new DownloadRecorder();
                downloadRecorder.recordDownload(izm.getItems(), izm.getUserName());
            }
            catch (Throwable ee) {
                log.error("While handling an exception in the MDB, could not commit the transaction",
                    ee);
            }
        }
    }
}