/**
 *
 */
package gov.nih.nci.ncia;

import gov.nih.nci.ncia.griddao.ImageDAO;
import gov.nih.nci.ncia.griddao.PatientDAO;
import gov.nih.nci.ncia.gridzip.ZippingDTO;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;


/**
 * This object figures out time point related information.  It is one layer
 * "above" a DAO... it's more like a transaction, because it queries multiple
 * DAO and merges the info together.
 *
 * <p>This came from a massive object called ImageFileProcessor which was split
 * into this object and several other "DAO" objects to isolate data access according
 * to similar pattern as rest of system.
 *
 * @author lethai
 */
public class Timepoints {

	public static List<ZippingDTO> getImagesByNthStudyTimePointForPatient(String patientId,
			                                                       int studyTimepoint) throws Exception{
		//firststep - find out the date for the the studyTimepoint
		PatientDAO patientDAO = new PatientDAO();
		List<Date> dateBucket = patientDAO.getTimepointStudyForPatient(patientId);


		Date dateForTimepoint = getDateForNthTimePoint(dateBucket, studyTimepoint);

		ImageDAO imageDAO = new ImageDAO();
		return imageDAO.getImagesByNthStudyTimePointForPatient(patientId,
                                                               dateForTimepoint);

	}

	/////////////////////////////////////PRIVATE///////////////////////////////////////////

	private static Logger logger = Logger.getLogger(Timepoints.class);

	private Timepoints() {
	}

	/* go through loop of dateTimepoint, find the value for the nthTimepoint*/
	private static Date getDateForNthTimePoint(List<Date> dateTimepoint,
			                                   int nthTimepoint){
		Date d= null;
		try{

			logger.info("before sorting....");
			for(int i=0; i<dateTimepoint.size(); i++){
				logger.info("i: " + i + " date: " + dateTimepoint.get(i));
			}
			Collections.sort(dateTimepoint);

			logger.info("after sorting....");
			for(int i=0; i<dateTimepoint.size(); i++){
				logger.info("i: " + i + " date: " + dateTimepoint.get(i));
			}
			if(nthTimepoint > 0 && nthTimepoint <= dateTimepoint.size()){
				d = dateTimepoint.get(nthTimepoint - 1);
			}
		}catch(Exception e){
			logger.error("Error getting Date the nth timepoint", e);

		}
		logger.info("date for " + nthTimepoint + " is " + d);
		return d;
	}
}
