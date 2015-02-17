package gov.nih.nci.nbia.textsupport;
import gov.nih.nci.nbia.dao.AbstractDAO;
import gov.nih.nci.nbia.dto.AnnotationFileDTO;
import gov.nih.nci.nbia.internaldomain.*;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import org.hibernate.SessionFactory;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import org.apache.commons.io.*;

import gov.nih.nci.nbia.internaldomain.Study;
import gov.nih.nci.nbia.qctool.VisibilityStatus;
import org.apache.log4j.Logger;

public class PatientAccessDAOImpl extends AbstractDAO
    implements PatientAccessDAO{
    private SessionFactory sessionFactory;
    static Logger log = Logger.getLogger(PatientAccessDAOImpl.class);
	TextSupportDAO support = (TextSupportDAO)SpringApplicationContext.getBean("textSupportDAO");

    // Seriously unfortunate that for some reason hibernate would not keep a single transaction across methods even
    // with the propagation required set, so this patient document method is huge in order to maintain the single
    // transaction.
    @Transactional(propagation=Propagation.REQUIRED)
	public PatientDocument getPatientDocument(String patientId)
	{
		PatientDocument returnValue = new PatientDocument();
		Patient patient = (Patient)SpringApplicationContext.getBean("patient");
		List rs = getHibernateTemplate().getSessionFactory().getCurrentSession()
	    .createQuery("from Patient as patient where patient.patientId =?")
	    .setParameter(0, patientId)
	    .list();
		if (rs.size()==0) return null;
		patient=(Patient)rs.get(0);
		returnValue.setId(patient.getId());
		returnValue.setPatientId(patient.getPatientId());
		returnValue.setPatientName(patient.getPatientName());
		returnValue.setEthnicGroup(patient.getEthnicGroup());
		returnValue.setPatientBirthDate(patient.getPatientBirthDate());
		returnValue.setPatientSex(patient.getPatientSex());
		
		//returnValue=fillInTrialDataProvenance(returnValue, patient.getDataProvenance());
		try {
			TrialDataProvenance trialDP = patient.getDataProvenance();
			if (trialDP!=null)
			{
			   TrialDataProvenanceDoc trialDPDoc = new TrialDataProvenanceDoc();
			   trialDPDoc.setDpSiteId(trialDP.getDpSiteId());
			   trialDPDoc.setDpSiteName(trialDP.getDpSiteName());
			   trialDPDoc.setProject(trialDP.getProject());
			   TextSupportDAO support = (TextSupportDAO)SpringApplicationContext.getBean("textSupportDAO");
			   List  rs2 = support.getCollectionDesc(trialDPDoc.getProject());
			   if (rs2.size()!=0) 
			   {
				for (Object result : rs2)
				  {
					  String collectionDesc = result.toString();
				      trialDPDoc.setCollectionDescription(collectionDesc);
				  }
			   }
			   returnValue.setDataProvenance(trialDPDoc);
			}

			int dicomFileCount = 0;
			if (patient.getStudyCollection()!=null)
			{
					Collection<StudyDoc> studyDocs= new ArrayList<StudyDoc>();
					for (Study study : patient.getStudyCollection()){
						StudyDoc studyDoc = new StudyDoc();
						studyDoc.setId(study.getId());
						studyDoc.setAdmittingDiagnosesCodeSeq(study.getAdmittingDiagnosesCodeSeq());
						studyDoc.setAdmittingDiagnosesDesc(study.getAdmittingDiagnosesDesc());
						studyDoc.setStudyDate(study.getStudyDate());
						studyDoc.setStudyDesc(study.getStudyDesc());
						studyDoc.setStudyId(study.getStudyId());
						studyDoc.setStudyTime(study.getStudyTime());
						studyDoc.setTimePointDesc(study.getTimePointDesc());
						studyDoc.setTimePointId(study.getTimePointId());
						studyDoc.setAgeGroup(study.getAgeGroup());
						studyDoc.setOccupation(study.getOccupation());
						if (patient.getTrialSite()!=null)
						{
							TrialSite site=patient.getTrialSite();
							TrialSiteDoc trialSiteDoc = new TrialSiteDoc();
							trialSiteDoc.setTrialSiteId(site.getTrialSiteId());
							trialSiteDoc.setTrialSiteName(site.getTrialSiteName());
							//trialSiteDoc=fillInClinicalTrials(trialSiteDoc, site);
							if (site.getTrial()!=null)
							{
								ClinicalTrial trial=site.getTrial();
								ClinicalTrialSubDoc clinicalTrialDoc = new ClinicalTrialSubDoc();
								clinicalTrialDoc.setCoordinatingCenter(trial.getCoordinatingCenter());
								clinicalTrialDoc.setProtocolName(trial.getProtocolName());
								clinicalTrialDoc.setSponsorName(trial.getSponsorName());
								trialSiteDoc.setTrial(clinicalTrialDoc);
							}
							
							returnValue.setTrialSite(trialSiteDoc);
						}
						log.info("series size-" + study.getGeneralSeriesCollection().size());
						if (study.getGeneralSeriesCollection()!=null)
						{
						  Collection<GeneralSeriesSubDoc> seriesDocs= new ArrayList<GeneralSeriesSubDoc>();
						  for (GeneralSeries series : study.getGeneralSeriesCollection()){
							GeneralSeriesSubDoc seriesDoc = new GeneralSeriesSubDoc();
							seriesDoc.setId(series.getId());
							seriesDoc.setModality(series.getModality());
							seriesDoc.setLaterality(series.getLaterality());
							seriesDoc.setProtocolName(series.getProtocolName());
							seriesDoc.setSeriesDesc(series.getSeriesDesc());
							seriesDoc.setBodyPartExamined(series.getBodyPartExamined());
							seriesDoc.setTrialProtocolId(series.getTrialProtocolId());
							seriesDoc.setProtocolName(series.getProtocolName());
					        seriesDoc.setSite(series.getSite());
					        seriesDoc.setStudyDesc(series.getSeriesDesc());
					        seriesDoc.setAdmittingDiagnosesDesc(series.getAdmittingDiagnosesDesc());
					        seriesDoc.setPatientSex(series.getPatientSex());
					        seriesDoc.setAgeGroup(series.getAgeGroup());
					        seriesDoc.setPatientId(series.getPatientId());
					        seriesDoc.setProject(series.getProject());
					        seriesDoc.setSite(series.getSite());
					        List<String> annotationFileConents = getFileAnnotationContents(seriesDoc.getId());
					        seriesDoc.setAnnotationContents(annotationFileConents);
					       // seriesDoc = fillInImages(seriesDoc, series);
					        
					        if (series.getGeneralImageCollection()!=null)
					    	{
					    	    Collection<GeneralImageSubDoc> imageDocs= new ArrayList<GeneralImageSubDoc>();
					    		for (GeneralImage image : series.getGeneralImageCollection()){
					    			GeneralImageSubDoc imageDoc = new GeneralImageSubDoc();
					    			imageDoc.setId(image.getId());
					    			imageDoc.setImageType(image.getImageType());
					    			imageDoc.setLossyImageCompression(image.getLossyImageCompression());
					    			imageDoc.setImageOrientationPatient(image.getImageOrientationPatient());
					                imageDoc.setImagePositionPatient(image.getImagePositionPatient());
					    			imageDoc.setContrastBolusAgent(image.getContrastBolusAgent());
					    			imageDoc.setContrastBolusRoute(image.getContrastBolusRoute());
					    			imageDoc.setPatientPosition(image.getPatientPosition());
					    			imageDoc.setImageComments(image.getImageComments());
					    			imageDoc.setAnnotation(image.getAnnotation());
					    			imageDoc.setImageLaterality(image.getImageLaterality());
					    			imageDoc.setPatientId(image.getPatientId());
					    			imageDoc.setProject(image.getProject());
					    			imageDoc.setUsFrameNum(image.getUsFrameNum());
					    			imageDoc.setUsColorDataPresent(image.getUsColorDataPresent());
					    			imageDoc.setUsMultiModality(image.getUsMultiModality());
					                if (image.getMrImage()!=null)
					                {
					                	MRImage mrImage = image.getMrImage();
					                	imageDoc.setImageTypeValue3(mrImage.getImageTypeValue3());
					                	imageDoc.setScanningSequence(mrImage.getScanningSequence());
					                	imageDoc.setSequenceVariant(mrImage.getSequenceVariant());
					                	imageDoc.setSequenceName(mrImage.getSequenceName());
					                    imageDoc.setImagedNucleus(mrImage.getImagedNucleus());
					                }
					                if (image.getCtimage()!=null)
					                {
					                	CTImage ctImage = image.getCtimage();
					                	imageDoc.setScanOptions(ctImage.getScanOptions());
					                	imageDoc.setConvolutionKernel(ctImage.getConvolutionKernel());
					                	imageDoc.setAnatomicRegionSeq(ctImage.getAnatomicRegionSeq());
					                }
					                imageDoc.setFilename(image.getFilename());
					    			imageDocs.add(imageDoc);
					    		    
					    		}
					    		seriesDoc.setGeneralImageCollection(imageDocs);
					    	}       
					        //seriesDoc = fillInEquipment(seriesDoc, series.getGeneralEquipment());
					        GeneralEquipment equipment = series.getGeneralEquipment();
					        if (equipment!=null)
					    	{
					    		GeneralEquipmentSubDoc equipmentDoc= new GeneralEquipmentSubDoc();
					            equipmentDoc.setDeviceSerialNumber(equipment.getDeviceSerialNumber());
					            equipmentDoc.setManufacturer(equipment.getManufacturer());
					            equipmentDoc.setInstitutionName(equipment.getInstitutionName());
					            equipmentDoc.setInstitutionAddress(equipment.getInstitutionName());
					            equipmentDoc.setManufacturerModelName(equipment.getManufacturerModelName());
					            equipmentDoc.setSoftwareVersions(equipment.getSoftwareVersions());
					            equipmentDoc.setStationName(equipment.getStationName());
					            seriesDoc.setGeneralEquipment(equipmentDoc);
					    	}
							seriesDocs.add(seriesDoc);
						  }
						  studyDoc.setGeneralSeriesCollection(seriesDocs);
						}
						studyDocs.add(studyDoc);
					}
					returnValue.setStudyCollection(studyDocs);
			}
			
			///returnValue=fillInTrials(returnValue, patient);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return returnValue;
		
	}
    @Transactional(propagation=Propagation.REQUIRED)
	private PatientDocument fillInStudies(PatientDocument document, Patient patient)
	{
		Collection<StudyDoc> studyDocs= new ArrayList<StudyDoc>();
		for (Study study : patient.getStudyCollection()){
			StudyDoc studyDoc = new StudyDoc();
			studyDoc.setId(study.getId());
			studyDoc.setAdmittingDiagnosesCodeSeq(study.getAdmittingDiagnosesCodeSeq());
			studyDoc.setAdmittingDiagnosesDesc(study.getAdmittingDiagnosesDesc());
			studyDoc.setStudyDate(study.getStudyDate());
			studyDoc.setStudyDesc(study.getStudyDesc());
			studyDoc.setStudyId(study.getStudyId());
			studyDoc.setStudyTime(study.getStudyTime());
			studyDoc.setTimePointDesc(study.getTimePointDesc());
			studyDoc.setTimePointId(study.getTimePointId());
			studyDoc.setAgeGroup(study.getAgeGroup());
			studyDoc.setOccupation(study.getOccupation());
			log.info("series size-" + study.getGeneralSeriesCollection().size());
			studyDoc=fillInSeries(studyDoc, study);
			studyDocs.add(studyDoc);
		    
		}
		document.setStudyCollection(studyDocs);
		return document;
	}
    @Transactional(propagation=Propagation.REQUIRED)
	private StudyDoc fillInSeries(StudyDoc document, Study study)
	{
		if (study.getGeneralSeriesCollection()==null) return document;
		Collection<GeneralSeriesSubDoc> seriesDocs= new ArrayList<GeneralSeriesSubDoc>();
		for (GeneralSeries series : study.getGeneralSeriesCollection()){
			GeneralSeriesSubDoc seriesDoc = new GeneralSeriesSubDoc();
			if (series.getVisibility().equals(VisibilityStatus.VISIBLE))
			{
			  log.info("*** series "+ series.getId()+" visible adding to index *****");
			  seriesDoc.setId(series.getId());
			  seriesDoc.setModality(series.getModality());
			  seriesDoc.setLaterality(series.getLaterality());
			  seriesDoc.setProtocolName(series.getProtocolName());
			  seriesDoc.setSeriesDesc(series.getSeriesDesc());
			  seriesDoc.setBodyPartExamined(series.getBodyPartExamined());
			  seriesDoc.setTrialProtocolId(series.getTrialProtocolId());
			  seriesDoc.setProtocolName(series.getProtocolName());
              seriesDoc.setSite(series.getSite());
              seriesDoc.setStudyDesc(series.getSeriesDesc());
              seriesDoc.setAdmittingDiagnosesDesc(series.getAdmittingDiagnosesDesc());
              seriesDoc.setPatientSex(series.getPatientSex());
              seriesDoc.setAgeGroup(series.getAgeGroup());
              seriesDoc.setPatientId(series.getPatientId());
              seriesDoc.setProject(series.getProject());
              seriesDoc.setSite(series.getSite());
              List<String> annotationFileConents = getFileAnnotationContents(seriesDoc.getId());
              seriesDoc.setAnnotationContents(annotationFileConents);
              seriesDoc = fillInImages(seriesDoc, series);
              seriesDoc = fillInEquipment(seriesDoc, series.getGeneralEquipment());
			  seriesDocs.add(seriesDoc);
			} else
			{
			  log.info("*** series "+ series.getId()+" not visible *****");
			}
		    
		}
		document.setGeneralSeriesCollection(seriesDocs);
		return document;
	}
    @Transactional(propagation=Propagation.REQUIRED)
	private GeneralSeriesSubDoc fillInEquipment(GeneralSeriesSubDoc document, GeneralEquipment equipment)
	{
		if (equipment==null) return document;
		GeneralEquipmentSubDoc equipmentDoc= new GeneralEquipmentSubDoc();
        equipmentDoc.setDeviceSerialNumber(equipment.getDeviceSerialNumber());
        equipmentDoc.setManufacturer(equipment.getManufacturer());
        equipmentDoc.setInstitutionName(equipment.getInstitutionName());
        equipmentDoc.setInstitutionAddress(equipment.getInstitutionName());
        equipmentDoc.setManufacturerModelName(equipment.getManufacturerModelName());
        equipmentDoc.setSoftwareVersions(equipment.getSoftwareVersions());
        equipmentDoc.setStationName(equipment.getStationName());
        document.setGeneralEquipment(equipmentDoc);
		return document;
	}
    @Transactional(propagation=Propagation.REQUIRED)
	private GeneralSeriesSubDoc fillInImages(GeneralSeriesSubDoc document, GeneralSeries series)
	{
		if (series.getGeneralImageCollection()==null) return document;
		Collection<GeneralImageSubDoc> imageDocs= new ArrayList<GeneralImageSubDoc>();
		for (GeneralImage image : series.getGeneralImageCollection()){
			GeneralImageSubDoc imageDoc = new GeneralImageSubDoc();
			imageDoc.setId(image.getId());
			imageDoc.setImageType(image.getImageType());
			imageDoc.setLossyImageCompression(image.getLossyImageCompression());
			imageDoc.setImageOrientationPatient(image.getImageOrientationPatient());
            imageDoc.setImagePositionPatient(image.getImagePositionPatient());
			imageDoc.setContrastBolusAgent(image.getContrastBolusAgent());
			imageDoc.setContrastBolusRoute(image.getContrastBolusRoute());
			imageDoc.setPatientPosition(image.getPatientPosition());
			imageDoc.setImageComments(image.getImageComments());
			imageDoc.setAnnotation(image.getAnnotation());
			imageDoc.setImageLaterality(image.getImageLaterality());
			imageDoc.setPatientId(image.getPatientId());
			imageDoc.setProject(image.getProject());
			imageDoc.setUsFrameNum(image.getUsFrameNum());
			imageDoc.setUsColorDataPresent(image.getUsColorDataPresent());
			imageDoc.setUsMultiModality(image.getUsMultiModality());
            if (image.getMrImage()!=null)
            {
            	MRImage mrImage = image.getMrImage();
            	imageDoc.setImageTypeValue3(mrImage.getImageTypeValue3());
            	imageDoc.setScanningSequence(mrImage.getScanningSequence());
            	imageDoc.setSequenceVariant(mrImage.getSequenceVariant());
            	imageDoc.setSequenceName(mrImage.getSequenceName());
                imageDoc.setImagedNucleus(mrImage.getImagedNucleus());
            }
            if (image.getCtimage()!=null)
            {
            	CTImage ctImage = image.getCtimage();
            	imageDoc.setScanOptions(ctImage.getScanOptions());
            	imageDoc.setConvolutionKernel(ctImage.getConvolutionKernel());
            	imageDoc.setAnatomicRegionSeq(ctImage.getAnatomicRegionSeq());
            }
            imageDoc.setFilename(image.getFilename());
			imageDocs.add(imageDoc);
		    
		}
		document.setGeneralImageCollection(imageDocs);
		return document;
	}
    @Transactional(propagation=Propagation.REQUIRED)
	private PatientDocument fillInTrials(PatientDocument document, Patient patient)
	{
		if (patient.getTrialSite()==null) return document;
		TrialSite site=patient.getTrialSite();
		TrialSiteDoc trialSiteDoc = new TrialSiteDoc();
		trialSiteDoc.setTrialSiteId(site.getTrialSiteId());
		trialSiteDoc.setTrialSiteName(site.getTrialSiteName());
		trialSiteDoc=fillInClinicalTrials(trialSiteDoc, site);
		document.setTrialSite(trialSiteDoc);
		return document;
	}
    @Transactional(propagation=Propagation.REQUIRED)
	private TrialSiteDoc fillInClinicalTrials(TrialSiteDoc document, TrialSite site)
	{
		if (site.getTrial()==null) return document;
		ClinicalTrial trial=site.getTrial();
		ClinicalTrialSubDoc clinicalTrialDoc = new ClinicalTrialSubDoc();
		clinicalTrialDoc.setCoordinatingCenter(trial.getCoordinatingCenter());
		clinicalTrialDoc.setProtocolName(trial.getProtocolName());
		clinicalTrialDoc.setSponsorName(trial.getSponsorName());
		document.setTrial(clinicalTrialDoc);
		return document;
	}
    @Transactional(propagation=Propagation.REQUIRED)
	private PatientDocument fillInTrialDataProvenance(PatientDocument document, TrialDataProvenance trialDP)
	{
		if (trialDP==null) return document;
		TrialDataProvenanceDoc trialDPDoc = new TrialDataProvenanceDoc();
		trialDPDoc.setDpSiteId(trialDP.getDpSiteId());
		trialDPDoc.setDpSiteName(trialDP.getDpSiteName());
		trialDPDoc.setProject(trialDP.getProject());
		TextSupportDAO support = (TextSupportDAO)SpringApplicationContext.getBean("textSupportDAO");
		List  rs = support.getCollectionDesc(trialDPDoc.getProject());
		   if (rs.size()!=0) 
		   {
			for (Object result : rs)
			  {
				  String collectionDesc = result.toString();
			      trialDPDoc.setCollectionDescription(collectionDesc);
			  }
		   }
		document.setDataProvenance(trialDPDoc);
		return document;
	}
    @Transactional(propagation=Propagation.REQUIRED)
	  private List<String> getFileAnnotationContents(Integer seriesPK)
	  {
		  List<String>returnValue=new ArrayList<String>();
		  try {
		    TextSupportDAO support = (TextSupportDAO)SpringApplicationContext.getBean("textSupportDAO");
		    List<Object> rs = support.getAnnotationFiles(seriesPK);
		    if (rs==null) return returnValue;
		  
			for (Object result : rs)
			  {
				    String filePath = result.toString();
				    String text = "";
				    File fileTest = new File(filePath);
				    log.info("found annotation file");
				    if (fileTest.exists())
				    {	
				      FileInputStream inputStream = new FileInputStream(filePath);
				      try {
				    	  text = IOUtils.toString(inputStream);
				    	  // make sure UTF-8
				    	  byte[] b = text.getBytes("UTF-8");
				    	  text = new String(b, "UTF-8");
				    	  //log.info("********* annotation text ****************");
				    	  //log.info(text);
				      } catch (Exception e) {
				    	//e.printStackTrace();
				    	log.warn("unable to convert "+filePath+" to UTF-8");
				      }
				    
				      finally {
				        try {
							inputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				      }
				      returnValue.add(text);
				    } else
				    {
				    	log.error("*** annotation file "+filePath+" does not exist *******");
				    }
			  }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  
		  return returnValue;
	  }
	
}
