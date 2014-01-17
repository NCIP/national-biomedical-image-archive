package gov.nih.nci.nbia.textsupport;
import gov.nih.nci.ncia.dto.DicomTagDTO;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.*;
import org.apache.solr.client.solrj.embedded.*;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.*;
import gov.nih.nci.nbia.dao.*;
import gov.nih.nci.nbia.dto.*;
import gov.nih.nci.nbia.util.SpringApplicationContext;

public class SolrStorage {
  static Logger log = Logger.getLogger(SolrStorage.class);
  private List<SolrInputDocument>seriesDocs=new ArrayList<SolrInputDocument>();
  private List<SolrInputDocument>imageDocs=new ArrayList<SolrInputDocument>();
  public void  addPatientDocument(PatientDocument patientDocument)
  {
	  log.warn("Solr asked to store patient document for -"+patientDocument.getId());
	  SolrServerInterface serverAccess = (SolrServerInterface)SpringApplicationContext.getBean("solrServer");
	  SolrServer server = serverAccess.GetServer();
	  try {
	    SolrInputDocument solrDoc = new SolrInputDocument();
	    solrDoc.addField( "id", patientDocument.getId());
	    solrDoc.addField( "patientId", patientDocument.getPatientId());
	    solrDoc.addField("ethnicGroup", patientDocument.getEthnicGroup());
	    solrDoc.addField("patientBirthDate", patientDocument.getPatientBirthDate());
	    solrDoc.addField("patientName", patientDocument.getPatientName());
	    solrDoc.addField("patientSex", patientDocument.getPatientSex());
	    if (patientDocument.getTrialSite() != null){
	       solrDoc=fillInTrials(solrDoc, patientDocument);
	    }
	    if (patientDocument.getDataProvenance() != null){
		       solrDoc=fillInTrialDP(solrDoc, patientDocument);
		}
	    solrDoc=fillInStudies(solrDoc, patientDocument);
	    String allFields=documentString(solrDoc);
	    System.out.println("**** Text of patient document is "+allFields.length() + " characters long");
	    // in the end it was not feasible to retrieve all the information from solr
	    solrDoc.addField("docType","patient");
	    server.add(solrDoc);
	    for (SolrInputDocument seriesDoc : seriesDocs){
	    	String seriesFields=documentString(seriesDoc);
	    	System.out.println("**** Text of series document is "+seriesFields.length() + " characters long");
	    	seriesDoc.addField("text", seriesFields);
	    	seriesDoc.addField("docType","series");
	    	server.add(seriesDoc);
	    }
	    for (SolrInputDocument imageDoc : imageDocs){
	    	String imageFields=documentString(imageDoc);
	    	System.out.println("**** Text of image document is "+imageFields.length() + " characters long");
	    	imageDoc.addField("text", imageFields);
	    	imageDoc.addField("docType","image");
	    	server.add(imageDoc);
	    }
	    log.warn("Solr has stored documents for -"+patientDocument.getId());
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
	private SolrInputDocument fillInTrials(SolrInputDocument document, PatientDocument patient)
	{
		if (patient.getTrialSite()==null) return document;
		TrialSiteDoc site = patient.getTrialSite();		
		document.addField("trialSiteId", site.getTrialSiteId());
		document.addField("trialSiteName", site.getTrialSiteName());
		document=fillInClinicalTrials(document, site);
		return document;
	}
	private SolrInputDocument fillInClinicalTrials(SolrInputDocument document, TrialSiteDoc site)
	{
		if (site.getTrial()==null) return document;
		ClinicalTrialSubDoc trial=site.getTrial();
		document.addField("coordinatingCenter", trial.getCoordinatingCenter());
		document.addField("protocolName", trial.getProtocolName());
		document.addField("sponsorName", trial.getSponsorName());
		return document;
	}
	private SolrInputDocument fillInTrialDP(SolrInputDocument document, PatientDocument patient)
	{
		if (patient.getDataProvenance()==null) return document;
		TrialDataProvenanceDoc trialDP = patient.getDataProvenance();		
		document.addField("dpSiteId", trialDP.getDpSiteId());
		document.addField("dpSiteName", trialDP.getDpSiteName());
		document.addField("project", trialDP.getProject());
		document.addField("collectionDescription", trialDP.getCollectionDescription());
		return document;
	}
	private SolrInputDocument fillInStudies(SolrInputDocument document, PatientDocument patient)
	{
		if (patient.getStudyCollection()==null) return document;
		int i=0;
		for (StudyDoc study : patient.getStudyCollection()){
			i++;
			String studyIndentifier = "Study"+i+"-";
			document.addField(studyIndentifier+"admittingDiagnosesCodeSeq", study.getAdmittingDiagnosesCodeSeq());
			document.addField(studyIndentifier+"admittingDiagnosesDesc",study.getAdmittingDiagnosesDesc());
			document.addField(studyIndentifier+"studyDate", study.getStudyDate());
			document.addField(studyIndentifier+"studyDesc", study.getStudyDesc());
			document.addField(studyIndentifier+"studyId", study.getStudyId());
			document.addField(studyIndentifier+"studyTime", study.getStudyTime());
			document.addField(studyIndentifier+"timePointDesc", study.getTimePointDesc());
			document.addField(studyIndentifier+"timePointId", study.getTimePointId());
			document.addField(studyIndentifier+"ageGroup", study.getAgeGroup());
			document.addField(studyIndentifier+"occupation", study.getOccupation());
			fillInSeries(document, study, studyIndentifier);
		}

		return document;
	}
	private void fillInSeries(SolrInputDocument document, StudyDoc study, String studyIndentifier)
	{
		
		if (study.getGeneralSeriesCollection()==null) return;
		int i=0;
		for (GeneralSeriesSubDoc series : study.getGeneralSeriesCollection()){
			i++;
			SolrInputDocument seriesDoc = new SolrInputDocument();
			String orginalId = document.getField("id").toString();
			String seriesIndentifier = "Patient-"+orginalId+"-"+studyIndentifier+"Series"+i;
			seriesDoc.addField("id",seriesIndentifier);
			seriesDoc.addField("modality",series.getModality());
			seriesDoc.addField("laterality",series.getLaterality());
			seriesDoc.addField("protocolName",series.getProtocolName());
			seriesDoc.addField("seriesDesc",series.getSeriesDesc());
			seriesDoc.addField("bodyPartExamined",series.getBodyPartExamined());
			seriesDoc.addField("trialProtocolId",series.getTrialProtocolId());
			seriesDoc.addField("site",series.getSite());
			seriesDoc.addField("studyDesc",series.getSeriesDesc());
			seriesDoc.addField("admittingDiagnosesDesc",series.getAdmittingDiagnosesDesc());
			seriesDoc.addField("patientSex",series.getPatientSex());
			seriesDoc.addField("ageGroup",series.getAgeGroup());
			seriesDoc.addField("patientId",series.getPatientId());
			seriesDoc.addField("project",series.getProject());
			int x = 0;
			List <String> annotationFileContents=series.getAnnotationContents();
			if (annotationFileContents!=null)
			{
				for (String fileContent : annotationFileContents)
				{
					x++;
					seriesDoc.addField("annotationFileContents-"+1, fileContent);
				}
			}

			seriesDoc=fillInEquipment(seriesDoc, series.getGeneralEquipment(), "Series"+i+"-");
			fillInImages(seriesDoc, series, "Series"+i+"-");
			seriesDocs.add(seriesDoc);
		}

	}
	private SolrInputDocument fillInEquipment(SolrInputDocument document, GeneralEquipmentSubDoc equipment, String seriesIndentifier)
	{
		if (equipment==null) return document;
		String equipmentIdentifier=seriesIndentifier+"Equipment-";
		document.addField(equipmentIdentifier+"id", equipment.getId());
		document.addField(equipmentIdentifier+"deviceSerialNumber",equipment.getDeviceSerialNumber());
		document.addField(equipmentIdentifier+"manufacturer",equipment.getManufacturer());
		document.addField(equipmentIdentifier+"institutionName",equipment.getInstitutionName());
		document.addField(equipmentIdentifier+"institutionAddress",equipment.getInstitutionName());
		document.addField(equipmentIdentifier+"manufacturerModelName",equipment.getManufacturerModelName());
		document.addField(equipmentIdentifier+"softwareVersions",equipment.getSoftwareVersions());
		document.addField(equipmentIdentifier+"stationName",equipment.getStationName());
		return document;
	}
	private void fillInImages(SolrInputDocument document, GeneralSeriesSubDoc series, String seriesIndentifier)
	{
		
		if (series.getGeneralImageCollection()==null) return;
		int x=0;
		for (GeneralImageSubDoc image : series.getGeneralImageCollection()){
			x++;
			
			SolrInputDocument imageDoc = new SolrInputDocument();
			String orginalId = document.getField("id").toString();
			String imageIdentifier = "Patient-"+orginalId+"-"+seriesIndentifier+"Image"+x;
			imageDoc.addField("id",imageIdentifier);
			imageDoc.addField("imageType",image.getImageType());
			imageDoc.addField("lossyImageCompression",image.getLossyImageCompression());
			imageDoc.addField("imageOrientationPatient",image.getImageOrientationPatient());
			imageDoc.addField("imagePositionPatient",image.getImagePositionPatient());
			imageDoc.addField("contrastBolusAgent",image.getContrastBolusAgent());
			imageDoc.addField("contrastBolusRoute",image.getContrastBolusRoute());
			imageDoc.addField("patientPosition",image.getPatientPosition());
			imageDoc.addField("imageComments",image.getImageComments());
			imageDoc.addField("annotation",image.getAnnotation());
			imageDoc.addField("imageLaterality",image.getImageLaterality());
			imageDoc.addField("patientId",image.getPatientId());
			imageDoc.addField("project",image.getProject());
			imageDoc.addField("usFrameNum",image.getUsFrameNum());
			imageDoc.addField("usColorDataPresent",image.getUsColorDataPresent());
			imageDoc.addField("usMultiModality",image.getUsMultiModality());
			imageDoc.addField("imageTypeValue3",image.getImageTypeValue3());
			imageDoc.addField("scanningSequence",image.getScanningSequence());
			imageDoc.addField("sequenceVariant",image.getSequenceVariant());
			imageDoc.addField("sequenceName",image.getSequenceName());
			imageDoc.addField("imagedNucleus",image.getImagedNucleus());
			imageDoc.addField("scanOptions",image.getScanOptions());
			imageDoc.addField("convolutionKernel",image.getConvolutionKernel());
			imageDoc.addField("anatomicRegionSeq",image.getAnatomicRegionSeq());
			if (image.getTagInfo()!=null)
			{
				for (DicomTagDTO tag : image.getTagInfo())
				{
					if (tag.getData()!=null&&(tag.getData().length()>2))
					{
					   String elementName="dicomTag-"+tag.getElement()+"-"+tag.getName();
					   String orginalName=elementName;
					   //log.debug(elementName + " - " + tag.getData());
					   if (document.get(elementName)!=null) // tag has multiple values
					   {
					     for (int i=0; i<100000; i++) // that would be a real lot of values
					     {
					    	 String newElementName=elementName+"-"+i;
					    	 //log.debug(newElementName + " - " + tag.getData());
					    	 if (document.get(newElementName)==null)
					    	 {
					    		 elementName=newElementName;
					    		 break;
					    	 }
					      }
					   }
						 //log.debug("added-"+elementName+"-" + tag.getData());
					   imageDoc.addField(elementName,tag.getData());
 					}
				}
            }
			imageDocs.add(imageDoc);
		}

     }
	  private String documentString(SolrInputDocument solrDoc)
	  {
		  StringBuilder returnValue = new StringBuilder("");
		  for (String field  : solrDoc.getFieldNames()){
			  if (solrDoc.getField(field).getValue()!=null)
			  returnValue.append(solrDoc.getField(field).getValue()).append("\n");
		  }
		  if (solrDoc.getChildDocuments()!=null)
		  {
		     for (SolrInputDocument doc: solrDoc.getChildDocuments())
		     {
			  returnValue.append(documentString(doc)).append("\n");;
		     }
		  }
		  return returnValue.toString();

	  }

}