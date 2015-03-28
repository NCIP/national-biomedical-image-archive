/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.jms;

import gov.nih.nci.nbia.deletion.DeletionDisplayObject;
import gov.nih.nci.nbia.deletion.ImageDeletionService;
import gov.nih.nci.nbia.deletion.ImageFileDeletionService;
import gov.nih.nci.nbia.mail.MailManager;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@javax.ejb.TransactionManagement(TransactionManagementType.BEAN)
@MessageDriven(name = "ImageDeletionMDB", activationConfig = {
@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/deletionQueue"),
@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class ImageDeletionMDB implements MessageListener {

	private Logger log = Logger.getLogger(ImageDeletionMDB.class);

	private ImageDeletionService imageDeletionService;
	private ImageFileDeletionService imageFileDeletionService;

	public void onMessage(Message arg0) {
		log.info("start processing series deletion...");
		ImageDeletionMessage izm = null;
		List<DeletionDisplayObject> deletionObjectLst = null;
		String initializedDeletionTime = null;

		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext-hibernate.xml");
		new SpringApplicationContext().setApplicationContext(appContext);
		imageDeletionService = (ImageDeletionService)appContext.getBean("imageDeletionService");
		imageFileDeletionService = (ImageFileDeletionService)appContext.getBean("imageFileDeletionService");

		try{
			ObjectMessage om = (ObjectMessage) arg0;
			izm = (ImageDeletionMessage)om.getObject();
			String userName = izm.getUserName();
			deletionObjectLst = imageDeletionService.getDeletionDisplayObject();
            initializedDeletionTime = getCurrentTime();

			Map<String, List<String>> files = imageDeletionService.removeSeries(userName);

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
			MailManager.sendDeletionConfirmationMail(izm.getEmailAddress(),
						izm.getUserName(), deletedSeries, initializedDeletionTime);
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
