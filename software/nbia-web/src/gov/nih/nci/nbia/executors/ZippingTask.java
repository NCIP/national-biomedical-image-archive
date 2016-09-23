package gov.nih.nci.nbia.executors;

import gov.nih.nci.nbia.basket.DownloadRecorder;
import gov.nih.nci.nbia.deletion.DeletionDisplayObject;
import gov.nih.nci.nbia.deletion.ImageDeletionService;
import gov.nih.nci.nbia.deletion.ImageFileDeletionService;
import gov.nih.nci.nbia.mail.MailManager;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.zip.ZipManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ZippingTask {
	private Logger log = Logger.getLogger(ZippingTask.class);

    // List of paths to zip files generated
    // There can be more than one for very large data sets
    List<String> listOfZipFiles;

	public void zipImages(ImageZippingMessage message){
		
		log.info("start Zipping");

		try{
            ZipManager zipper = new ZipManager();
            zipper.setName(message.getZipFilename());
            zipper.setItems(message.getItems());
            zipper.setTarget(new File(message.getZipFilename()));
            zipper.setNoAnnotation(!message.isIncludeAnnotation());
            listOfZipFiles = zipper.zip();

            log.info("Finish zipping file " + message.getZipFilename());
        } catch (Throwable e) {
        	e.printStackTrace();
            log.error("Error zipping file " + message.getZipFilename(), e);

            // Return so that email doesn't get sent
            return;
        }


        try {
            // send an email to user
            log.info("FTP_DOWNLOAD: " + message.getEmailAddress());

            MailManager.sendFTPMail(message.getEmailAddress(),
            						message.getUserName(),
            		                listOfZipFiles,
            		                message.getNodeName());


            log.info("Finish sending email ... ");
        } catch (Throwable e) {
            log.error("Error while sending email");
        } finally {
            // Commit the transaction so that it won't run over and over again
            try {
               // ctx.getUserTransaction().commit();

                DownloadRecorder downloadRecorder = new DownloadRecorder();
                downloadRecorder.recordDownload(message.getItems(), message.getUserName());
            }
            catch (Throwable ee) {
                log.error("While handling an exception in the task, could not commit the transaction",
                    ee);
            }
        }
    }
}
