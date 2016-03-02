/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: GeneralSeriesBuilder.java 6424 2008-09-23 20:27:05Z zhoujim $
*
* $Log: not supported by cvs2svn $
* Revision 1.17  2006/12/19 16:24:42  zhouro
* changed data type from Integer to Double
*
* Revision 1.16  2006/09/28 19:29:00  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Jul 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.ncia.tool.mirctest;

import gov.nih.nci.ncia.db.IDataAccess;
import gov.nih.nci.ncia.domain.GeneralEquipment;
import gov.nih.nci.ncia.domain.GeneralSeries;
import gov.nih.nci.ncia.domain.Patient;
import gov.nih.nci.ncia.domain.Study;
import gov.nih.nci.ncia.util.DicomConstants;

import java.util.Hashtable;
import java.util.List;
import org.apache.log4j.Logger;


/**
 * @author Rona Zhou
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class GeneralSeriesBuilder extends DataBuilder  {
    Logger log = Logger.getLogger(GeneralSeriesBuilder.class);

    public GeneralSeriesBuilder(IDataAccess ida) throws Exception{
    	super(ida);
    }

	public GeneralSeries buildSeries(Hashtable numbers, Patient patient,
			Study study, GeneralEquipment equip) throws Exception {
		String temp;
		String hql = "from GeneralSeries as series where ";
		GeneralSeries series = new GeneralSeries();

		hql += (" series.study.id = " + study.getId());
		hql += (" and series.generalEquipment.id = " + equip.getId());

		series.setStudy(study);
		series.setGeneralEquipment(equip);

		if ((temp = (String) numbers.get("83")) != null) {
			series.setSeriesInstanceUID(temp.trim());
			hql += (" and series.seriesInstanceUID = '" + temp.trim() + "' ");
		} else {
			hql += " and series.seriesInstanceUID is null ";
		}

		// System.out.println( "Series HQL: " + hql );
		series = (GeneralSeries) this.update(hql, series);

		if ((temp = (String) numbers.get("14")) != null) {
			series.setModality(temp.trim());
		}

		if ((temp = (String) numbers.get("93")) != null) {
			series.setLaterality(temp.trim());
		}

		if ((temp = (String) numbers.get("7")) != null) {
			series.setSeriesDate(this.stringToDate(temp.trim()));
		}

		if ((temp = (String) numbers.get("62")) != null) {
			series.setProtocolName(temp.trim());
		}

		if ((temp = (String) numbers.get("19")) != null) {
			series.setSeriesDesc(temp.trim());
		}

		if ((temp = (String) numbers.get("54")) != null) {
			series.setBodyPartExamined(temp.trim());
		}

		if ((temp = (String) numbers.get("41")) != null) {
			series.setTrialProtocolId(temp.trim());
		}

		if ((temp = (String) numbers.get("43")) != null) {
			series.setTrialProtocolName(temp.trim());
		}

		if ((temp = (String) numbers.get("46")) != null) {
			series.setTrialSiteName(temp.trim());
		}

		if ((temp = (String) numbers.get("6")) != null) {
			series.setStudyDate(this.stringToDate(temp.trim()));
		}

		if ((temp = (String) numbers.get("18")) != null) {
			series.setStudyDesc(temp.trim());
		}

		if ((temp = (String) numbers.get("21")) != null) {
			series.setAdmittingDiagnosesDesc(temp.trim());
		}

		if ((temp = (String) numbers.get("32")) != null) {
			series.setPatientAge(temp.trim());
		}

		if ((temp = (String) numbers.get("30")) != null) {
			series.setPatientSex(temp.trim());
		}

		if ((temp = (String) numbers.get("35")) != null) {
			series.setPatientWeight(Double.valueOf(temp.trim()));
		}

		if ((temp = (String) numbers.get("32")) != null) {
			series.setAgeGroup(temp.trim());
		}

		if ((temp = (String) numbers.get("85")) != null) {
			series.setSeriesNumber(Integer.valueOf(temp.trim()));
		}

		if ((temp = (String) numbers.get("94")) != null) {
			series.setSyncFrameOfRefUID(temp.trim());
		}

		if ((temp = (String) numbers.get("91")) != null) {
			series.setFrameOfReferenceUID(temp.trim());
		}

		// System.out.println( "patient id ******** " + patient.getId() );
		series.setPatientPkId(patient.getId());
		series.setPatientId(patient.getPatientId());

		return series;
	}

	public GeneralSeries retrieveGeneralSeriesFromDB(Integer generalSeriesPkId) throws Exception {
    	
        String hql = "from GeneralSeries as obj where ";

        if (generalSeriesPkId != null) {
            hql += (" obj.id = " + generalSeriesPkId + " ");
        }

        List retList = null;
        retList = (List) getIDataAccess().search(hql);

        GeneralSeries gs=null;
        if ((retList != null) && (retList.size() != 0)) {
        	gs = (GeneralSeries) retList.get(0);
        } else {
            System.out.println("cannot find the GeneralSeriesImpl");
        }
        return gs;
	}
	
    public Hashtable addGeneralSeries(Hashtable numbersInDb, GeneralSeries gs) {

    	//Hashtable numbersInDb=new Hashtable();
    	if (gs==null) {
    		System.out.println("GeneralImage is null");
    		return numbersInDb;
    	}
    	if (gs.getModality()!=null)	{
    		numbersInDb.put(DicomConstants.MODALITY, gs.getModality());
    	}
    	if (gs.getSeriesInstanceUID()!=null) {
    		numbersInDb.put(DicomConstants.SERIES_INSTANCE_UID, gs.getSeriesInstanceUID());
    	}
    	if (gs.getLaterality()!=null) {
    		numbersInDb.put(DicomConstants.LATERALITY, gs.getLaterality());
    	}
    	if (gs.getSeriesDate()!=null) {
    		numbersInDb.put(DicomConstants.SERIES_DATE, gs.getSeriesDate());
    	}
    	if (gs.getProtocolName()!=null) {
    		numbersInDb.put(DicomConstants.PROTOCOL_NAME, gs.getProtocolName());
    	}
    	if (gs.getSeriesDesc()!=null) {
    		numbersInDb.put(DicomConstants.SERIES_DESCRIPTION, gs.getSeriesDesc());
    	}
    	if (gs.getBodyPartExamined()!=null) {
    		numbersInDb.put(DicomConstants.BODY_PART_EXAMINED, gs.getBodyPartExamined());
    	}
    	if (gs.getTrialProtocolId()!=null) {
    		numbersInDb.put(DicomConstants.CLINICAL_TRIAL_PROTOCOL_ID, gs.getTrialProtocolId());
    	}
    	if (gs.getTrialProtocolName()!=null) {
    		numbersInDb.put(DicomConstants.CLINICAL_TRIAL_PROTOCOL_NAME, gs.getTrialProtocolName());
    	}
    	
    	// get those fields from study table
//    	if (gs.getStudyDate()!=null)
//    		numbersInDb.put(DicomConstants.STUDY_DATE, gs.getStudyDate());
//    	if (gs.getStudyDesc()!=null)
//    		numbersInDb.put(DicomConstants.STUDY_DESCRIPTION, gs.getStudyDesc());
//    	
    	if (gs.getAdmittingDiagnosesDesc()!=null) {
    		numbersInDb.put(DicomConstants.ADMITTING_DIAGNOSES_DESCRIPTION, gs.getAdmittingDiagnosesDesc());
    	}
    	if (gs.getPatientAge()!=null) {
    		numbersInDb.put(DicomConstants.PATIENT_AGE, gs.getPatientAge());
    	}
    	if (gs.getPatientSex()!=null) {
    		numbersInDb.put(DicomConstants.PATIENT_SEX, gs.getPatientSex());
    	}
    	if (gs.getPatientWeight()!=null) {
    		numbersInDb.put(DicomConstants.PATIENT_WEIGHT, gs.getPatientWeight());
    	}
    	if (gs.getAgeGroup()!=null) {
    		numbersInDb.put(DicomConstants.PATIENT_AGE, gs.getAgeGroup());
    	}
    	if (gs.getSeriesNumber()!=null) {
    		numbersInDb.put(DicomConstants.SERIES_ID, gs.getSeriesNumber());
    	}
    	if (gs.getSyncFrameOfRefUID()!=null) {
    		numbersInDb.put(DicomConstants.SYNCHRONIZATION_FRAME_OF_REF_UID, gs.getSyncFrameOfRefUID());
    	}
    	if (gs.getFrameOfReferenceUID()!=null) {
    		numbersInDb.put(DicomConstants.FRAME_OF_REFERENCE_UID, gs.getFrameOfReferenceUID());
    	}


		// many to one relation
    	if (gs.getGeneralEquipment()!=null) {
    		numbersInDb.put("GENERAL_EQUIPMENT_PK_ID", gs.getGeneralEquipment().getId());
    	}

		return numbersInDb;
    }
}
