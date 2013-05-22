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
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Performs asynchronous zipping of image and annotation files
 *
 *
 */
public class ImageZippingMDB implements MessageDrivenBean, MessageListener {
    private Logger log = Logger.getLogger(ImageZippingMDB.class);

    // Queue values
    private QueueConnection conn;
    private QueueSession session;

    // The container's context for this bean
    private MessageDrivenContext ctx;

    /**
     * Constructor
     *
     */
    public ImageZippingMDB() {
        log.debug("ImageZippingMDB constructor");
    }

    /**
     * Called by the container to set the context
     *
     * @param ctx - the context for this bean
     */
    public void setMessageDrivenContext(MessageDrivenContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Called by the container when the EJB is created
     *
     */
    public void ejbCreate() {
        try {
            setup();
            log.debug("finish ejbCreate");
        } catch (Exception e) {
            throw new EJBException("Failed to init ImageZippingMDB");
        }

        //initialize spring so that nbia-dao stuff will work correctly.
        //not sure if there is an extra-special way to do this by
        //configuration or convention but this works
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext-hibernate.xml");
        new SpringApplicationContext().setApplicationContext(appContext);
    }

    /**
     * Called by the container when the EJB is removed
     */
    public void ejbRemove() {
        log.debug("ImageZippingMDB.ejbRemove");

        try {
            if (session != null) {
                session.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (JMSException e) {
            log.debug("Unexception exception: " + e.getMessage());
            e.printStackTrace();
        }


    }

    /**
     * Creates a connection to the queue
     *
     *
     * @throws JMSException
     * @throws NamingException
     * @throws IOException
     */
    private void setup() throws JMSException, NamingException, IOException {
        log.debug("ImageZippingMDB.setup");

        InitialContext context = new InitialContext();

        QueueConnectionFactory qcf = (QueueConnectionFactory) context.lookup(
                "java:comp/env/jms/QCF");
        conn = qcf.createQueueConnection();
        session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
        conn.start();
    }

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

            // Because a lot of data can be zipped, the EJB transaction timeout must be
            // set appropriately
            // To set the timeout ourselves, bean managed transactions must be used
            //  (see ejb-jar.xml)
//            log.debug("Setting MDB timeout to " +
//                NCIAConfig.getImageZippingMDBTimeout() + " seconds ");

            //ctx.getUserTransaction().setTransactionTimeout(NCIAConfig.getImageZippingMDBTimeout());

            // Begin the transaction
            //ctx.getUserTransaction().begin();



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

            // Mark download history as failed?
            // Commit the transaction so that it won't run over and over again
//            try {
//                ctx.getUserTransaction().commit();
//            }
//            catch (Throwable ee) {
//                log.error("While handling an exception in the MDB, could not commit the transaction",
//                    ee);
//            }

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
