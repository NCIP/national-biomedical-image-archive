/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.wadosupport;

import gov.nih.nci.nbia.dao.AbstractDAO;
import gov.nih.nci.nbia.dicomapi.DICOMParameters;
import gov.nih.nci.nbia.dicomapi.DICOMSupportDTO;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.util.*;
import gov.nih.nci.nbia.security.*;
import gov.nih.nci.nbia.util.*;

public class WADOSupportDAOImpl extends AbstractDAO
                               implements WADOSupportDAO
{
	static Logger log = Logger.getLogger(WADOSupportDAOImpl.class);
    private static HashMap <String, UserObject>userTable;
    private final static String WADO_QUERY="select distinct gs.project, gs.site, dicom_file_uri from general_image gi, general_series gs" +
    		" where gs.study_instance_uid = :study and gs.series_instance_uid = :series and gi.sop_instance_uid = :image " +
    		"  and gs.general_series_pk_id = gi.general_series_pk_id";

    private final static String WADO_OVIYAM_QUERY="select distinct gs.project, gs.site, dicom_file_uri from general_image gi, general_series gs" +
	" where gi.sop_instance_uid = :image " +
	"  and gs.general_series_pk_id = gi.general_series_pk_id";
    
    private final static String DICOM_QUERY="select distinct gs.project, gs.site, dicom_file_uri," +
    		" p.patient_name, p.patient_id, gs.modality, " +
    		" s.study_date, s.study_time,  gi.acquisition_number," +
    		" s.study_id, s.study_desc, gs.series_number, " +
    		" gs.series_instance_uid, gs.series_desc, p.patient_birth_date, " +
    		" gs.modality, s.study_instance_uid, gi.sop_instance_uid " +
    		" from general_image gi, general_series gs, patient p, study s " +
	" where gs.general_series_pk_id = gi.general_series_pk_id"+
	" and gs.patient_pk_id=p.patient_pk_id"+
	" and gs.study_pk_id=s.study_pk_id";
    
public WADOSupportDAOImpl()
{
	if (userTable==null){
		userTable=new HashMap <String, UserObject>();
	}
}
public WADOSupportDTO getWADOSupportDTO(String study, String series, String image)
{
	String user =  NCIAConfig.getGuestUsername();
	return getWADOSupportDTO(study, series, image, user );
}
	
@Transactional(propagation=Propagation.REQUIRED)
public WADOSupportDTO getWADOSupportDTO(String study, String series, String image, String user )
{
	WADOSupportDTO returnValue = new WADOSupportDTO();
	log.info("Study-"+study+" series-"+series+" image-"+image);
	try {
		List <Object[]>images= this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(WADO_QUERY)
		  .setParameter("study", study)
		  .setParameter("series", series)
		  .setParameter("image", image).list();
		if (images.size()==0) {
			   log.info("image not found");
			   return null; //nothing to do
		}
		List<SiteData> authorizedSites;
		UserObject uo = userTable.get(user);
		if (uo!=null)
		{
			authorizedSites = uo.getAuthorizedSites();
			if (authorizedSites==null)
			{
				   AuthorizationManager manager = new AuthorizationManager(user);
				   authorizedSites = manager.getAuthorizedSites();
				   uo.setAuthorizedSites(authorizedSites);
			}
		} else
		{
		   AuthorizationManager manager = new AuthorizationManager(user);
		   authorizedSites = manager.getAuthorizedSites();
		   uo = new UserObject();
		   uo.setAuthorizedSites(authorizedSites);
		   userTable.put(user, uo);
		}
		returnValue.setCollection((String)images.get(0)[0]);
		returnValue.setSite((String)images.get(0)[1]);
		boolean isAuthorized = false;
		for (SiteData siteData : authorizedSites)
		{
			if (siteData.getCollection().equals(returnValue.getCollection()))
			{
				if (siteData.getSiteName().equals(returnValue.getSite()))
				{
					isAuthorized = true;
					break;
				}
			}
		}
		if (!isAuthorized)
		{
			System.out.println("User: "+user+" not authorized");
			return null; //not authorized
		}
		String filePath = (String)images.get(0)[2];
		File imageFile = new File(filePath);
		if (!imageFile.exists())
		{
			log.error("File " + filePath + " does not exist");
			return null;
		}
		returnValue.setImage(FileUtils.readFileToByteArray(imageFile));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	
	return returnValue;
}
@Transactional(propagation=Propagation.REQUIRED)
public WADOSupportDTO getOviyamWADOSupportDTO(String image, String contentType, String user)
{
	WADOSupportDTO returnValue = new WADOSupportDTO();
	log.info("Oviyam wado image-"+image);
	try {
		List <Object[]>images= this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(WADO_OVIYAM_QUERY)
		  .setParameter("image", image).list();
		if (images.size()==0) {
			   log.info("image not found");
			   return null; //nothing to do
		}
		List<SiteData> authorizedSites;
		UserObject uo = userTable.get(user);
		if (uo!=null)
		{
			authorizedSites = uo.getAuthorizedSites();
			if (authorizedSites==null)
			{
				   AuthorizationManager manager = new AuthorizationManager(user);
				   authorizedSites = manager.getAuthorizedSites();
				   uo.setAuthorizedSites(authorizedSites);
			}
		} else
		{
		   AuthorizationManager manager = new AuthorizationManager(user);
		   authorizedSites = manager.getAuthorizedSites();
		   uo = new UserObject();
		   uo.setAuthorizedSites(authorizedSites);
		   userTable.put(user, uo);
		}
		returnValue.setCollection((String)images.get(0)[0]);
		returnValue.setSite((String)images.get(0)[1]);
		boolean isAuthorized = false;
		for (SiteData siteData : authorizedSites)
		{
			if (siteData.getCollection().equals(returnValue.getCollection()))
			{
				if (siteData.getSiteName().equals(returnValue.getSite()))
				{
					isAuthorized = true;
					break;
				}
			}
		}
		if (!isAuthorized)
		{
			System.out.println("User: "+user+" not authorized");
			return null; //not authorized
		}
		String filePath = (String)images.get(0)[2];
		File imageFile = new File(filePath);
		if (!imageFile.exists())
		{
			log.error("File " + filePath + " does not exist");
			return null;
		}
		if (contentType.equals(("application/dicom")))
		{
		    returnValue.setImage(FileUtils.readFileToByteArray(imageFile));
		} else
		{
			JPEGResult result = DCMUtils.getJPGFromFile(imageFile, new WADOParameters());
			if (result.getErrors()!=null)
			{
				returnValue.setErrors(result.getErrors());
				return returnValue;
			}
			returnValue.setImage(result.getImages());
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	return returnValue;
}


@Transactional(propagation=Propagation.REQUIRED)
public WADOSupportDTO getWADOSupportDTO(WADOParameters params, String user)
{
	WADOSupportDTO returnValue = new WADOSupportDTO();
	log.info("Study-"+params.getStudyUID()+" series-"+params.getSeriesUID()+" image-"+params.getObjectUID());
	try {
		List <Object[]>images= this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(WADO_QUERY)
		  .setParameter("study", params.getStudyUID())
		  .setParameter("series", params.getSeriesUID())
		  .setParameter("image", params.getObjectUID()).list();
		if (images.size()==0) {
			   log.error("image not found");
			   returnValue.setErrors("image not found");
			   return returnValue; 
		}
		List<SiteData> authorizedSites;
		UserObject uo = userTable.get(user);
		if (uo!=null)
		{
			authorizedSites = uo.getAuthorizedSites();
			if (authorizedSites==null)
			{
				   AuthorizationManager manager = new AuthorizationManager(user);
				   authorizedSites = manager.getAuthorizedSites();
				   uo.setAuthorizedSites(authorizedSites);
			}
		} else
		{
		   System.out.println("the user is " + user);
		   AuthorizationManager manager = new AuthorizationManager(user);
		   authorizedSites = manager.getAuthorizedSites();
		   uo = new UserObject();
		   uo.setAuthorizedSites(authorizedSites);
		   userTable.put(user, uo);
		}
		returnValue.setCollection((String)images.get(0)[0]);
		returnValue.setSite((String)images.get(0)[1]);
		boolean isAuthorized = false;
		for (SiteData siteData : authorizedSites)
		{
			if (siteData.getCollection().equals(returnValue.getCollection()))
			{
				if (siteData.getSiteName().equals(returnValue.getSite()))
				{
					isAuthorized = true;
					break;
				}
			}
		}
		if (!isAuthorized)
		{
			System.out.println("User: "+user+" not authorized");
			return null; //not authorized
		}
		String filePath = (String)images.get(0)[2];
		File imageFile = new File(filePath);
		if (!imageFile.exists())
		{
			log.error("File " + filePath + " does not exist");
			returnValue.setErrors("File does not exist");
			return returnValue; 
		}
		if (params.getContentType().equals(("application/dicom")))
		{
		    returnValue.setImage(FileUtils.readFileToByteArray(imageFile));
		} else
		{
			JPEGResult result = DCMUtils.getJPGFromFile(imageFile, params);
			if (result.getErrors()!=null)
			{
				returnValue.setErrors(result.getErrors());
				return returnValue;
			}
			returnValue.setImage(result.getImages());
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		returnValue.setErrors("unable to process request");
		return returnValue; 
	}
	
	return returnValue;
}


@Transactional(propagation=Propagation.REQUIRED)
public List <DICOMSupportDTO> getDICOMSupportDTO(DICOMParameters params, List<String> extraFields)
{
	List <DICOMSupportDTO>returnValues = new ArrayList<DICOMSupportDTO>();

	try {
		String user =  NCIAConfig.getGuestUsername();
		
	
		String queryString = DICOM_QUERY;
		List <String>parameterList = new ArrayList<String>();
		if (params.getPatientName()!=null)
		{
			queryString=queryString+" and p.patient_name like ?";
			parameterList.add(params.getPatientName());
			log.info("added patient name to query : "+params.getPatientName());
		} 
		if (params.getPatientID()!=null)
		{
			queryString=queryString+" and p.patient_id like ?";
			parameterList.add(params.getPatientID());
			log.info("added patient id to query : "+params.getPatientID());
		}
		if (params.getStudyInstanceUID()!=null)
		{
			queryString=queryString+" and gs.study_instance_uid like ?";
			parameterList.add(params.getStudyInstanceUID());
			log.info("added study instance uid to query : "+params.getStudyInstanceUID());
		}
		if (params.getSeriesInstanceUID()!=null)
		{
			queryString=queryString+" and gs.series_instance_uid like ?";
			parameterList.add(params.getSeriesInstanceUID());
			log.info("added series instance uid to query : "+params.getSeriesInstanceUID());
		}
		if (params.getStudyDescription()!=null)
		{
			queryString=queryString+" and s.study_description like ?";
			parameterList.add(params.getStudyDescription());
			log.info("added study description to query : "+params.getStudyDescription());
		}
		SQLQuery query= this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		int p=0;
		for (String param:parameterList)
		{
			query.setString(p, param.trim());
			p++;
		}
		List <Object[]>images=query.list();
		if (images.size()==0) {
			   log.error("images not found");
			  // returnValue.setErrors("images not found");
			   return returnValues;
		}
		List<SiteData> authorizedSites;
		UserObject uo = userTable.get(user);
		if (uo!=null)
		{
			authorizedSites = uo.getAuthorizedSites();
			if (authorizedSites==null)
			{
				   AuthorizationManager manager = new AuthorizationManager(user);
				   authorizedSites = manager.getAuthorizedSites();
				   uo.setAuthorizedSites(authorizedSites);
			}
		} else
		{
		   System.out.println("the user is " + user);
		   AuthorizationManager manager = new AuthorizationManager(user);
		   authorizedSites = manager.getAuthorizedSites();
		   uo = new UserObject();
		   uo.setAuthorizedSites(authorizedSites);
		   userTable.put(user, uo);
		}
		for (int i=0; i<images.size(); i++){
		   String collection=(String)images.get(0)[0];
		   String site=(String)images.get(0)[1];
		   boolean isAuthorized = false;
		   for (SiteData siteData : authorizedSites)
		   {
		   	   if (siteData.getCollection().equals(collection))
			   {
				   if (siteData.getSiteName().equals(site))
				   {
					   isAuthorized = true;
					   break;
				   }
			   }
		   }
		   if (!isAuthorized)
		   {
			System.out.println("User: "+user+" not authorized");
			continue; //not authorized
		   }
		   String filePath = (String)images.get(0)[2];
		   String patientName = (String)images.get(0)[3];
		   String patientID = (String)images.get(0)[4];
		   String modality = (String)images.get(0)[5];
		   String studyDate = null;
		   try {
			studyDate = ((Date) images.get(0)[6]).toString();
		   } catch (Exception e) {
			// its null
		   }
		   String studyTime = null;
		   try {
			studyTime = ((java.math.BigDecimal) images.get(0)[7]).toString();
		   } catch (Exception e) {
			// its null
		   }
		   String accessionNumber = null;
		   try {
			accessionNumber = ((java.math.BigDecimal) images.get(0)[8])
					.toString();
	 	    } catch (Exception e) {
			// its null
		    }
		   String studyID = (String)images.get(0)[9];
		   String studyDescription = (String)images.get(0)[10];
		   String seriesNumber = ((Object)images.get(0)[11]).toString();
		   String seriesInstanceUID = (String)images.get(0)[12];
		   String seriesDescription = (String)images.get(0)[13];
		   String referringPhysicianName = "not implemented";
		   String patientBirthDate = null;
		   try {
			patientBirthDate = ((Date)images.get(0)[14]).toString();
		   } catch (Exception e) {
			// its null
		   }
		   String modalitiesInStudy = (String)images.get(0)[15];
		   String studyInstanceUID = (String)images.get(0)[16];
		   String sOPInstanceUID = (String)images.get(0)[17];
		   File imageFile = new File(filePath);
		   if (!imageFile.exists())
		   {
			   log.error("File " + filePath + " does not exist");
			   // returnValue.setErrors("File does not exist");
			   // return returnValue; 
		   } else
		   {
			   DICOMSupportDTO returnItem = new DICOMSupportDTO();
			   returnItem.setFilePath(imageFile.getPath());
			   returnItem.setFileName(imageFile.getName());
			   returnItem.setFileSize(new Long(imageFile.length()).toString());
			   Hashtable <String, String>extraFieldMap = new Hashtable<String, String>();
			   extraFieldMap.put("PatientName", stringForNull(patientName));
			   extraFieldMap.put("PatientID", stringForNull(patientID));
			   extraFieldMap.put("Modality", stringForNull(modality));
			   extraFieldMap.put("StudyDate", stringForNull(studyDate));
			   extraFieldMap.put("StudyTime", stringForNull(studyTime));
			   extraFieldMap.put("AccessionNumber", stringForNull(accessionNumber));
			   extraFieldMap.put("StudyID", stringForNull(studyID));
			   extraFieldMap.put("StudyDescription", stringForNull(studyDescription));
			   extraFieldMap.put("SeriesNumber", stringForNull(seriesNumber));
			   extraFieldMap.put("SeriesInstanceUID", stringForNull(seriesInstanceUID));
			   extraFieldMap.put("SeriesDescription", stringForNull(seriesDescription));
			   extraFieldMap.put("ReferringPhysicianName", stringForNull(referringPhysicianName));
			   extraFieldMap.put("PatientBirthDate", stringForNull(patientBirthDate));
			   extraFieldMap.put("ModalitiesInStudy", stringForNull(modalitiesInStudy));
			   extraFieldMap.put("StudyInstanceUID", stringForNull(studyInstanceUID));
			   extraFieldMap.put("SOPInstanceUID", stringForNull(sOPInstanceUID));
			   returnItem.setFieldMap(extraFieldMap);
			   returnValues.add(returnItem);
			   log.info("added dicom dto "+returnItem.toString());
		   }
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		//returnValue.setErrors("unable to process request");
		return returnValues; 
	}
	
	return returnValues;
}
private class ParameterList
{
	public ParameterList(String paramIn, String paramNameIn)
	{
		//log.info("new parameter "+paramIn+":"+paramNameIn);
		this.param=paramIn;
		this.paramName=paramNameIn;
	}
	public String param;
	public String paramName;
}
private String stringForNull(String input)
{
	if (input==null){
		return "";
	} else {
		return input;
	}
}
}
