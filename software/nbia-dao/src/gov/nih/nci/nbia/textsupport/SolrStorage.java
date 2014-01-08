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
  public static void  addPatientDocument(PatientDocument patientDocument)
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
	    String allFields=documentString(solrDoc);;
	    solrDoc.addField("text", allFields);
	    log.debug(solrDoc.toString());
	    server.add(solrDoc);
	    server.commit();
	    log.warn("Solr committed patient document for -"+patientDocument.getId());
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
	private static SolrInputDocument fillInTrials(SolrInputDocument document, PatientDocument patient)
	{
		if (patient.getTrialSite()==null) return document;
		TrialSiteDoc site = patient.getTrialSite();		
		document.addField("trialSiteId", site.getTrialSiteId());
		document.addField("trialSiteName", site.getTrialSiteName());
		document=fillInClinicalTrials(document, site);
		return document;
	}
	private static SolrInputDocument fillInClinicalTrials(SolrInputDocument document, TrialSiteDoc site)
	{
		if (site.getTrial()==null) return document;
		ClinicalTrialSubDoc trial=site.getTrial();
		document.addField("coordinatingCenter", trial.getCoordinatingCenter());
		document.addField("protocolName", trial.getProtocolName());
		document.addField("sponsorName", trial.getSponsorName());
		return document;
	}
	private static SolrInputDocument fillInTrialDP(SolrInputDocument document, PatientDocument patient)
	{
		if (patient.getDataProvenance()==null) return document;
		TrialDataProvenanceDoc trialDP = patient.getDataProvenance();		
		document.addField("dpSiteId", trialDP.getDpSiteId());
		document.addField("dpSiteName", trialDP.getDpSiteName());
		document.addField("project", trialDP.getProject());
		document.addField("collectionDescription", trialDP.getCollectionDescription());
		return document;
	}
	private static SolrInputDocument fillInStudies(SolrInputDocument document, PatientDocument patient)
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
			document=fillInSeries(document, study, studyIndentifier);
		}

		return document;
	}
	private static SolrInputDocument fillInSeries(SolrInputDocument document, StudyDoc study, String studyIndentifier)
	{
		
		if (study.getGeneralSeriesCollection()==null) return document;
		int i=0;
		for (GeneralSeriesSubDoc series : study.getGeneralSeriesCollection()){
			i++;
			String seriesIndentifier = studyIndentifier+"Series"+i+"-";
			document.addField(seriesIndentifier+"modality",series.getModality());
			document.addField(seriesIndentifier+"laterality",series.getLaterality());
			document.addField(seriesIndentifier+"protocolName",series.getProtocolName());
			document.addField(seriesIndentifier+"seriesDesc",series.getSeriesDesc());
			document.addField(seriesIndentifier+"bodyPartExamined",series.getBodyPartExamined());
			document.addField(seriesIndentifier+"trialProtocolId",series.getTrialProtocolId());
			document.addField(seriesIndentifier+"site",series.getSite());
			document.addField(seriesIndentifier+"studyDesc",series.getSeriesDesc());
			document.addField(seriesIndentifier+"admittingDiagnosesDesc",series.getAdmittingDiagnosesDesc());
			document.addField(seriesIndentifier+"patientSex",series.getPatientSex());
			document.addField(seriesIndentifier+"ageGroup",series.getAgeGroup());
			document.addField(seriesIndentifier+"patientId",series.getPatientId());
			document.addField(seriesIndentifier+"project",series.getProject());
			int x = 0;
			List <String> annotationFileContents=series.getAnnotationContents();
			if (annotationFileContents!=null)
			{
				for (String fileContent : annotationFileContents)
				{
					x++;
					document.addField(seriesIndentifier+"annotationFileContents-"+1, fileContent);
				}
			}

			document=fillInEquipment(document, series.getGeneralEquipment(), seriesIndentifier);
			document=fillInImages(document, series, seriesIndentifier);
	    
		}
		return document;
	}
	private static SolrInputDocument fillInEquipment(SolrInputDocument document, GeneralEquipmentSubDoc equipment, String seriesIndentifier)
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
	private static SolrInputDocument fillInImages(SolrInputDocument document, GeneralSeriesSubDoc series, String seriesIndentifier)
	{
		
		if (series.getGeneralImageCollection()==null) return document;
		int x=0;
		for (GeneralImageSubDoc image : series.getGeneralImageCollection()){
			x++;
			String imageIdentifier = seriesIndentifier+"Image"+x+"-";
			document.addField(imageIdentifier+"imageType",image.getImageType());
			document.addField(imageIdentifier+"lossyImageCompression",image.getLossyImageCompression());
			document.addField(imageIdentifier+"imageOrientationPatient",image.getImageOrientationPatient());
			document.addField(imageIdentifier+"imagePositionPatient",image.getImagePositionPatient());
			document.addField(imageIdentifier+"contrastBolusAgent",image.getContrastBolusAgent());
			document.addField(imageIdentifier+"contrastBolusRoute",image.getContrastBolusRoute());
			document.addField(imageIdentifier+"patientPosition",image.getPatientPosition());
			document.addField(imageIdentifier+"imageComments",image.getImageComments());
			document.addField(imageIdentifier+"annotation",image.getAnnotation());
			document.addField(imageIdentifier+"imageLaterality",image.getImageLaterality());
			document.addField(imageIdentifier+imageIdentifier+"patientId",image.getPatientId());
			document.addField(imageIdentifier+"project",image.getProject());
			document.addField(imageIdentifier+"usFrameNum",image.getUsFrameNum());
			document.addField(imageIdentifier+"usColorDataPresent",image.getUsColorDataPresent());
			document.addField(imageIdentifier+"usMultiModality",image.getUsMultiModality());
			document.addField(imageIdentifier+"imageTypeValue3",image.getImageTypeValue3());
			document.addField(imageIdentifier+"scanningSequence",image.getScanningSequence());
			document.addField(imageIdentifier+"sequenceVariant",image.getSequenceVariant());
			document.addField(imageIdentifier+"sequenceName",image.getSequenceName());
			document.addField(imageIdentifier+"imagedNucleus",image.getImagedNucleus());
			document.addField(imageIdentifier+"scanOptions",image.getScanOptions());
			document.addField(imageIdentifier+"convolutionKernel",image.getConvolutionKernel());
			document.addField(imageIdentifier+"anatomicRegionSeq",image.getAnatomicRegionSeq());
			if (image.getTagInfo()!=null)
			{
				for (DicomTagDTO tag : image.getTagInfo())
				{
					if (tag.getData()!=null&&(tag.getData().length()>2))
					{
					   String elementName=imageIdentifier+"dicomTag-"+tag.getElement()+"-"+tag.getName();
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
					     document.addField(elementName,tag.getData());
 					}
				}
            }

		}
		return document;
     }
	  private static String documentString(SolrInputDocument solrDoc)
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