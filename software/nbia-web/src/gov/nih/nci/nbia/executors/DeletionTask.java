package gov.nih.nci.nbia.executors;

import gov.nih.nci.nbia.deletion.DeletionDisplayObject;
import gov.nih.nci.nbia.deletion.ImageDeletionService;
import gov.nih.nci.nbia.deletion.ImageFileDeletionService;
import gov.nih.nci.nbia.mail.MailManager;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DeletionTask {
	private Logger log = Logger.getLogger(DeletionTask.class);
	private ImageDeletionService imageDeletionService;
	private ImageFileDeletionService imageFileDeletionService;
	public void deleteImages(ImageDeletionMessage message){
		
		log.info("start processing series deletion...");
		List<DeletionDisplayObject> deletionObjectLst = null;
		String initializedDeletionTime = null;


		try{
			String userName = message.getUserName();
			imageDeletionService = (ImageDeletionService)SpringApplicationContext.getBean("imageDeletionService");
			deletionObjectLst = imageDeletionService.getDeletionDisplayObject();
            initializedDeletionTime = getCurrentTime();

			Map<String, List<String>> files = imageDeletionService.removeSeries(userName);
			
			
			imageFileDeletionService = (ImageFileDeletionService)SpringApplicationContext.getBean("imageFileDeletionService");

			//remove all annotation files and dicom files, this must be out of
			//transaction
			imageFileDeletionService.removeImageFiles(files);

		}catch(Throwable e){
			e.printStackTrace();
            log.error("Error deleting file... " + e);
            return;
		}

		try{
			List<String> deletedSeries = new ArrayList<String>();
			for (DeletionDisplayObject obj: deletionObjectLst) {
				deletedSeries.add(obj.getSeriesUID());
			}
			MailManager.sendDeletionConfirmationMail(message.getEmailAddress(),
					message.getUserName(), deletedSeries, initializedDeletionTime);
		}catch(Exception ee){
			ee.printStackTrace();
			log.error("Fail to send an email to deletion requestor...");
		}
	}

	private String getCurrentTime(){
		String dateTime = "";
		Calendar now = Calendar.getInstance();

		dateTime = "" + (now.get(Calendar.MONTH) + 1)
        + "-"
        + now.get(Calendar.DATE)
        + "-"
        + now.get(Calendar.YEAR)
        + " "
        + now.get(Calendar.HOUR_OF_DAY)
        + ":"
        + now.get(Calendar.MINUTE)
        + ":"
        + now.get(Calendar.SECOND)
        + "."
        + now.get(Calendar.MILLISECOND);

		return dateTime;
	}
	

}
