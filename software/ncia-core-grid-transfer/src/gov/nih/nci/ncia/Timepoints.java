/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 *
 */
package gov.nih.nci.ncia;

import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.ncia.griddao.ImageDAOInterface;
import gov.nih.nci.ncia.griddao.PatientDAOInterface;
import gov.nih.nci.ncia.gridzip.ZippingDTO;

import java.util.Collections;
import java.util.Date;
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
		PatientDAOInterface patientDAO = (PatientDAOInterface)SpringApplicationContext.getBean("patientDaoInterface");
		List<Date> dateBucket = patientDAO.getTimepointStudyForPatient(patientId);


		Date dateForTimepoint = getDateForNthTimePoint(dateBucket, studyTimepoint);
		
		ImageDAOInterface imageDao = (ImageDAOInterface)SpringApplicationContext.getBean("imageDaoInterface");
		return imageDao.getImagesByNthStudyTimePointForPatient(patientId, dateForTimepoint);

	}

	/////////////////////////////////////PRIVATE///////////////////////////////////////////

	private static Logger logger = Logger.getLogger(Timepoints.class);

	private Timepoints() {
	}

	/* go through loop of dateTimepoint, find the value for the nthTimepoint*/
	private static Date getDateForNthTimePoint(List<Date> dateTimepoint,
			                                   int nthTimepoint){
		Date d = null;
		try{
			Collections.sort(dateTimepoint);

			if(nthTimepoint > 0 && nthTimepoint <= dateTimepoint.size()){
				d = dateTimepoint.get(nthTimepoint - 1);
			}
		}catch(Exception e){
			logger.error("Error getting Date the nth timepoint", e);
		}
		return d;
	}
}
