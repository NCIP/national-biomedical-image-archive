/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.textsupport;


import gov.nih.nci.ncia.dto.DicomTagDTO;

import java.io.Serializable;
import java.util.List;
	/**
	* An in vivo image of a human being.  In cases where imaging modalities produce multiple slices (e.g. CT or MR scans), this represents one slice.	**/
public class GeneralImageSubDoc  implements Serializable
{
	
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

		/**
	* The date the acquisition of data that resulted in this image started	**/
	private java.util.Date acquisitionDate;
	/**
	* Retreives the value of acquisitionDate attribute
	* @return acquisitionDate
	**/

	public java.util.Date getAcquisitionDate(){
		return acquisitionDate;
	}

	/**
	* Sets the value of acquisitionDate attribue
	**/

	public void setAcquisitionDate(java.util.Date acquisitionDate){
		this.acquisitionDate = acquisitionDate;
	}

		/**
	* The date and time that the acquisition of data that resulted in this image started.	**/
	private String acquisitionDatetime;
	/**
	* Retreives the value of acquisitionDatetime attribute
	* @return acquisitionDatetime
	**/

	public String getAcquisitionDatetime(){
		return acquisitionDatetime;
	}

	/**
	* Sets the value of acquisitionDatetime attribue
	**/

	public void setAcquisitionDatetime(String acquisitionDatetime){
		this.acquisitionDatetime = acquisitionDatetime;
	}

		/**
	* 	**/
	private Double acquisitionMatrix;
	/**
	* Retreives the value of acquisitionMatrix attribute
	* @return acquisitionMatrix
	**/

	public Double getAcquisitionMatrix(){
		return acquisitionMatrix;
	}


	/**
	* Sets the value of acquisitionMatrix attribue
	**/

	public void setAcquisitionMatrix(Double acquisitionMatrix){
		this.acquisitionMatrix = acquisitionMatrix;
	}

	/**
	* 	**/
	private String md5Digest;
	/**
	* Retreives the value of MD5 Digest
	* @return mdDigest
	**/

	public String getMd5Digest(){
		return md5Digest;
	}


	/**
	* Sets the value of md5 digest
	**/

	public void setMd5Digest(String md5Digest){
		this.md5Digest = md5Digest;
	}

	/**
	* Number identifying the single continuous gathering of data over a period of time which resulted in an image, as specified in the DICOM tag.	**/
	private Integer acquisitionNumber;
	/**
	* Retreives the value of acquisitionNumber attribute
	* @return acquisitionNumber
	**/

	public Integer getAcquisitionNumber(){
		return acquisitionNumber;
	}

	/**
	* Sets the value of acquisitionNumber attribue
	**/

	public void setAcquisitionNumber(Integer acquisitionNumber){
		this.acquisitionNumber = acquisitionNumber;
	}

		/**
	* The time the acquisition of data that resulted in this image started	**/
	private String acquisitionTime;
	/**
	* Retreives the value of acquisitionTime attribute
	* @return acquisitionTime
	**/

	public String getAcquisitionTime(){
		return acquisitionTime;
	}

	/**
	* Sets the value of acquisitionTime attribue
	**/

	public void setAcquisitionTime(String acquisitionTime){
		this.acquisitionTime = acquisitionTime;
	}

		/**
	* Annotation	**/
	private String annotation;
	/**
	* Retreives the value of annotation attribute
	* @return annotation
	**/

	public String getAnnotation(){
		return annotation;
	}

	/**
	* Sets the value of annotation attribue
	**/

	public void setAnnotation(String annotation){
		this.annotation = annotation;
	}

		/**
	* Value to represent the number of columns in an image.	**/
	private Integer columns;
	/**
	* Retreives the value of columns attribute
	* @return columns
	**/

	public Integer getColumns(){
		return columns;
	}

	/**
	* Sets the value of columns attribue
	**/

	public void setColumns(Integer columns){
		this.columns = columns;
	}

		/**
	* The date the image pixel data creation started.	**/
	private java.util.Date contentDate;
	/**
	* Retreives the value of contentDate attribute
	* @return contentDate
	**/

	public java.util.Date getContentDate(){
		return contentDate;
	}

	/**
	* Sets the value of contentDate attribue
	**/

	public void setContentDate(java.util.Date contentDate){
		this.contentDate = contentDate;
	}

		/**
	* The time the image pixel data creation started.	**/
	private String contentTime;
	/**
	* Retreives the value of contentTime attribute
	* @return contentTime
	**/

	public String getContentTime(){
		return contentTime;
	}

	/**
	* Sets the value of contentTime attribue
	**/

	public void setContentTime(String contentTime){
		this.contentTime = contentTime;
	}

		/**
	* Text name of contrast/bolus agent given to increase visualization of structures and tissues during medical imaging.	**/
	private String contrastBolusAgent;
	/**
	* Retreives the value of contrastBolusAgent attribute
	* @return contrastBolusAgent
	**/

	public String getContrastBolusAgent(){
		return contrastBolusAgent;
	}

	/**
	* Sets the value of contrastBolusAgent attribue
	**/

	public void setContrastBolusAgent(String contrastBolusAgent){
		this.contrastBolusAgent = contrastBolusAgent;
	}

		/**
	* Text term to specify the administration route of a contrast/bolus agent.	**/
	private String contrastBolusRoute;
	/**
	* Retreives the value of contrastBolusRoute attribute
	* @return contrastBolusRoute
	**/

	public String getContrastBolusRoute(){
		return contrastBolusRoute;
	}

	/**
	* Sets the value of contrastBolusRoute attribue
	**/

	public void setContrastBolusRoute(String contrastBolusRoute){
		this.contrastBolusRoute = contrastBolusRoute;
	}

		/**
	* The date and time at which the image had its visibility status changed to Visible	**/
	private java.util.Date curationTimestamp;
	/**
	* Retreives the value of curationTimestamp attribute
	* @return curationTimestamp
	**/

	public java.util.Date getCurationTimestamp(){
		return curationTimestamp;
	}

	/**
	* Sets the value of curationTimestamp attribue
	**/

	public void setCurationTimestamp(java.util.Date curationTimestamp){
		this.curationTimestamp = curationTimestamp;
	}

		/**
	* The size (in bytes) of the image file	**/
	private Long dicomSize;
	/**
	* Retreives the value of dicomSize attribute
	* @return dicomSize
	**/

	public Long getDicomSize(){
		return dicomSize;
	}

	/**
	* Sets the value of dicomSize attribue
	**/

	public void setDicomSize(Long dicomSize){
		this.dicomSize = dicomSize;
	}

		/**
	* 	**/
	private Double dxDataCollectionDiameter;
	/**
	* Retreives the value of dxDataCollectionDiameter attribute
	* @return dxDataCollectionDiameter
	**/

	public Double getDxDataCollectionDiameter(){
		return dxDataCollectionDiameter;
	}

	/**
	* Sets the value of dxDataCollectionDiameter attribue
	**/

	public void setDxDataCollectionDiameter(Double dxDataCollectionDiameter){
		this.dxDataCollectionDiameter = dxDataCollectionDiameter;
	}

		/**
	* The location (full path and file name) of the image file on the file system.	**/
	private String filename;
	/**
	* Retreives the value of filename attribute
	* @return filename
	**/

	public String getFilename(){
		return filename;
	}

	/**
	* Sets the value of filename attribue
	**/

	public void setFilename(String filename){
		this.filename = filename;
	}

		/**
	* Millimeter value for the size of the focal spot as specified in DICOM tag.	**/
	private Double focalSpotSize;
	/**
	* Retreives the value of focalSpotSize attribute
	* @return focalSpotSize
	**/

	public Double getFocalSpotSize(){
		return focalSpotSize;
	}

	/**
	* Sets the value of focalSpotSize attribue
	**/

	public void setFocalSpotSize(Double focalSpotSize){
		this.focalSpotSize = focalSpotSize;
	}

		/**
	* One or more characters used to identify, name, or characterize the nature, properties, or contents of a thing.	**/
	private Integer id;
	/**
	* Retreives the value of id attribute
	* @return id
	**/

	public Integer getId(){
		return id;
	}

	/**
	* Sets the value of id attribue
	**/

	public void setId(Integer id){
		this.id = id;
	}

		/**
	* User-defined text comments about the image as defined in DICOM tag.	**/
	private String imageComments;
	/**
	* Retreives the value of imageComments attribute
	* @return imageComments
	**/

	public String getImageComments(){
		return imageComments;
	}

	/**
	* Sets the value of imageComments attribue
	**/

	public void setImageComments(String imageComments){
		this.imageComments = imageComments;
	}

		/**
	* Laterality of (paired) body part examined.	**/
	private String imageLaterality;
	/**
	* Retreives the value of imageLaterality attribute
	* @return imageLaterality
	**/

	public String getImageLaterality(){
		return imageLaterality;
	}

	/**
	* Sets the value of imageLaterality attribue
	**/

	public void setImageLaterality(String imageLaterality){
		this.imageLaterality = imageLaterality;
	}
	
	

		/**
	* Numeric value to specify the direction cosines of the first row and the first column with respect to the patient, as specified in the DICOM tag.	**/
	private String imageOrientationPatient;
	/**
	* Retreives the value of imageOrientationPatient attribute
	* @return imageOrientationPatient
	**/

	public String getImageOrientationPatient(){
		return imageOrientationPatient;
	}

	/**
	* Sets the value of imageOrientationPatient attribue
	**/

	public void setImageOrientationPatient(String imageOrientationPatient){
		this.imageOrientationPatient = imageOrientationPatient;
	}

		/**
	* The x, y, and z coordinates of the upper left hand corner (center of the first voxel transmitted) of the image, expressed as a number of millimeters.	**/
	private String imagePositionPatient;
	/**
	* Retreives the value of imagePositionPatient attribute
	* @return imagePositionPatient
	**/

	public String getImagePositionPatient(){
		return imagePositionPatient;
	}

	/**
	* Sets the value of imagePositionPatient attribue
	**/

	public void setImagePositionPatient(String imagePositionPatient){
		this.imagePositionPatient = imagePositionPatient;
	}

		/**
	* 	**/
	private java.util.Date imageReceivingTimestamp;
	/**
	* Retreives the value of imageReceivingTimestamp attribute
	* @return imageReceivingTimestamp
	**/

	public java.util.Date getImageReceivingTimestamp(){
		return imageReceivingTimestamp;
	}

	/**
	* Sets the value of imageReceivingTimestamp attribue
	**/

	public void setImageReceivingTimestamp(java.util.Date imageReceivingTimestamp){
		this.imageReceivingTimestamp = imageReceivingTimestamp;
	}

		/**
	* Text name to represent attributes important for image identification characteristics.	**/
	private String imageType;
	/**
	* Retreives the value of imageType attribute
	* @return imageType
	**/

	public String getImageType(){
		return imageType;
	}

	/**
	* Sets the value of imageType attribue
	**/

	public void setImageType(String imageType){
		this.imageType = imageType;
	}
	private String imageTypeValue3;
	
	
	
		/**
	* Numeric value to identify a single image instance.	**/
	private Integer instanceNumber;
	/**
	* Retreives the value of instanceNumber attribute
	* @return instanceNumber
	**/

	public Integer getInstanceNumber(){
		return instanceNumber;
	}

	/**
	* Sets the value of instanceNumber attribue
	**/

	public void setInstanceNumber(Integer instanceNumber){
		this.instanceNumber = instanceNumber;
	}

		/**
	* Code that designates whether or not an image has been compressed (at a point in its lifetime) with a lossy algorithm, and changes have been introduced into the pixel data.	**/
	private String lossyImageCompression;
	/**
	* Retreives the value of lossyImageCompression attribute
	* @return lossyImageCompression
	**/

	public String getLossyImageCompression(){
		return lossyImageCompression;
	}

	/**
	* Sets the value of lossyImageCompression attribue
	**/

	public void setLossyImageCompression(String lossyImageCompression){
		this.lossyImageCompression = lossyImageCompression;
	}

		/**
	* The unique number assigned to identify a patient/participant on a protocol.	**/
	private String patientId;
	/**
	* Retreives the value of patientId attribute
	* @return patientId
	**/

	public String getPatientId(){
		return patientId;
	}

	/**
	* Sets the value of patientId attribue
	**/

	public void setPatientId(String patientId){
		this.patientId = patientId;
	}

		/**
	* Identifier	**/
	private Integer patientPkId;
	/**
	* Retreives the value of patientPkId attribute
	* @return patientPkId
	**/

	public Integer getPatientPkId(){
		return patientPkId;
	}

	/**
	* Sets the value of patientPkId attribue
	**/

	public void setPatientPkId(Integer patientPkId){
		this.patientPkId = patientPkId;
	}

		/**
	* Coded value to describe if the patient is head first or feet first in the gantry, and prone or supine, according to the DICOM tag.	**/
	private String patientPosition;
	/**
	* Retreives the value of patientPosition attribute
	* @return patientPosition
	**/

	public String getPatientPosition(){
		return patientPosition;
	}

	/**
	* Sets the value of patientPosition attribue
	**/

	public void setPatientPosition(String patientPosition){
		this.patientPosition = patientPosition;
	}

		/**
	* Value to represent the physical distance between the center of each pixel, specified by a numeric pair - adjacent row spacing (delimiter), adjacent column spacing - expressed as a number in millimeters.	**/
	private Double pixelSpacing;
	/**
	* Retreives the value of pixelSpacing attribute
	* @return pixelSpacing
	**/

	public Double getPixelSpacing(){
		return pixelSpacing;
	}

	/**
	* Sets the value of pixelSpacing attribue
	**/

	public void setPixelSpacing(Double pixelSpacing){
		this.pixelSpacing = pixelSpacing;
	}

		/**
	* An name chosen by the image submitter to identify the collection that the image is a part of.	**/
	private String project;
	/**
	* Retreives the value of project attribute
	* @return project
	**/

	public String getProject(){
		return project;
	}

	/**
	* Sets the value of project attribue
	**/

	public void setProject(String project){
		this.project = project;
	}

		/**
	* Numeric value to represent the number of rows in an image.	**/
	private Integer rows;
	/**
	* Retreives the value of rows attribute
	* @return rows
	**/

	public Integer getRows(){
		return rows;
	}

	/**
	* Sets the value of rows attribue
	**/

	public void setRows(Integer rows){
		this.rows = rows;
	}

		/**
	* Unique identifer of a study series.	**/
	private String seriesInstanceUID;
	/**
	* Retreives the value of seriesInstanceUID attribute
	* @return seriesInstanceUID
	**/

	public String getSeriesInstanceUID(){
		return seriesInstanceUID;
	}

	/**
	* Sets the value of seriesInstanceUID attribue
	**/

	public void setSeriesInstanceUID(String seriesInstanceUID){
		this.seriesInstanceUID = seriesInstanceUID;
	}

		/**
	* identifier	**/
	private Integer seriesPKId;
	/**
	* Retreives the value of seriesPKId attribute
	* @return seriesPKId
	**/

	public Integer getSeriesPKId(){
		return seriesPKId;
	}

	/**
	* Sets the value of seriesPKId attribue
	**/

	public void setSeriesPKId(Integer seriesPKId){
		this.seriesPKId = seriesPKId;
	}

		/**
	* Number to represent relative position of exposure, expressed in millimeters.	**/
	private Double sliceLocation;
	/**
	* Retreives the value of sliceLocation attribute
	* @return sliceLocation
	**/

	public Double getSliceLocation(){
		return sliceLocation;
	}

	/**
	* Sets the value of sliceLocation attribue
	**/

	public void setSliceLocation(Double sliceLocation){
		this.sliceLocation = sliceLocation;
	}

		/**
	* Numeric value to represent the nominal slice thickness of an image, expressed in millimeters.	**/
	private Double sliceThickness;
	/**
	* Retreives the value of sliceThickness attribute
	* @return sliceThickness
	**/

	public Double getSliceThickness(){
		return sliceThickness;
	}

	/**
	* Sets the value of sliceThickness attribue
	**/

	public void setSliceThickness(Double sliceThickness){
		this.sliceThickness = sliceThickness;
	}

		/**
	* Unique identifier specific for a Service-Object Pair (SOP) class, as specified in the DICOM standard.	**/
	private String SOPClassUID;
	/**
	* Retreives the value of SOPClassUID attribute
	* @return SOPClassUID
	**/

	public String getSOPClassUID(){
		return SOPClassUID;
	}

	/**
	* Sets the value of SOPClassUID attribue
	**/

	public void setSOPClassUID(String SOPClassUID){
		this.SOPClassUID = SOPClassUID;
	}

		/**
	* Unique identifer for a Service-Object Pair (SOP) instance, as specified in a DICOM tag.	**/
	private String SOPInstanceUID;
	/**
	* Retreives the value of SOPInstanceUID attribute
	* @return SOPInstanceUID
	**/

	public String getSOPInstanceUID(){
		return SOPInstanceUID;
	}

	/**
	* Sets the value of SOPInstanceUID attribue
	**/

	public void setSOPInstanceUID(String SOPInstanceUID){
		this.SOPInstanceUID = SOPInstanceUID;
	}

		/**
	* Millimeter distance from energy source to subject (isocenter) as specified in DICOM tag.	**/
	private Double sourceSubjectDistance;
	/**
	* Retreives the value of sourceSubjectDistance attribute
	* @return sourceSubjectDistance
	**/

	public Double getSourceSubjectDistance(){
		return sourceSubjectDistance;
	}

	/**
	* Sets the value of sourceSubjectDistance attribue
	**/

	public void setSourceSubjectDistance(Double sourceSubjectDistance){
		this.sourceSubjectDistance = sourceSubjectDistance;
	}

		/**
	* Millimeter dIstance from the source to the detector as specified in DICOM tag.	**/
	private Double sourceToDetectorDistance;
	/**
	* Retreives the value of sourceToDetectorDistance attribute
	* @return sourceToDetectorDistance
	**/

	public Double getSourceToDetectorDistance(){
		return sourceToDetectorDistance;
	}

	/**
	* Sets the value of sourceToDetectorDistance attribue
	**/

	public void setSourceToDetectorDistance(Double sourceToDetectorDistance){
		this.sourceToDetectorDistance = sourceToDetectorDistance;
	}

		/**
	* Identifer used to locate the storage of a series of image files.	**/
	private String storageMediaFileSetUID;
	/**
	* Retreives the value of storageMediaFileSetUID attribute
	* @return storageMediaFileSetUID
	**/

	public String getStorageMediaFileSetUID(){
		return storageMediaFileSetUID;
	}

	/**
	* Sets the value of storageMediaFileSetUID attribue
	**/

	public void setStorageMediaFileSetUID(String storageMediaFileSetUID){
		this.storageMediaFileSetUID = storageMediaFileSetUID;
	}

		/**
	* Unique identifier for an occurrence of a medical imaging study.	**/
	private String studyInstanceUID;
	/**
	* Retreives the value of studyInstanceUID attribute
	* @return studyInstanceUID
	**/

	public String getStudyInstanceUID(){
		return studyInstanceUID;
	}

	/**
	* Sets the value of studyInstanceUID attribue
	**/

	public void setStudyInstanceUID(String studyInstanceUID){
		this.studyInstanceUID = studyInstanceUID;
	}

		/**
	* The date on which the image was submitted.	**/
	private java.util.Date submissionDate;
	/**
	* Retreives the value of submissionDate attribute
	* @return submissionDate
	**/

	public java.util.Date getSubmissionDate(){
		return submissionDate;
	}

	/**
	* Sets the value of submissionDate attribue
	**/

	public void setSubmissionDate(java.util.Date submissionDate){
		this.submissionDate = submissionDate;
	}

		/**
	* The URI of the image on the Medical Imaging Resource Center (MIRC) server	**/
	private String uri;
	/**
	* Retreives the value of uri attribute
	* @return uri
	**/

	public String getUri(){
		return uri;
	}

	/**
	* Sets the value of uri attribue
	**/

	public void setUri(String uri){
		this.uri = uri;
	}

	/**
	* An associated gov.nih.nci.ncia.domain.GeneralSeries object
	**/

	private GeneralSeriesSubDoc generalSeries;
	/**
	* Retreives the value of generalSeries attribue
	* @return generalSeries
	**/

	public GeneralSeriesSubDoc getGeneralSeries(){
		return generalSeries;
	}
	/**
	* Sets the value of generalSeries attribue
	**/

	public void setGeneralSeries(GeneralSeriesSubDoc generalSeries){
		this.generalSeries = generalSeries;
	}

	/**
	* An associated gov.nih.nci.ncia.domain.CTImage object
	**/

	private CTImageSubDoc ctimage;
	/**
	* Retreives the value of ctimage attribue
	* @return ctimage
	**/

	public CTImageSubDoc getCtimage(){
		return ctimage;
	}
	/**
	* Sets the value of ctimage attribue
	**/

	public void setCtimage(CTImageSubDoc ctimage){
		this.ctimage = ctimage;
	}
	private String usFrameNum;
		public String getUsFrameNum() {
			return usFrameNum;
		}

		public void setUsFrameNum(String usFrameNum) {
			this.usFrameNum = usFrameNum;
		}

	/**
	* Text to capture usColorDataPresent  as specified in a DICOM tag.	**/
	private String usColorDataPresent ;
	/**
	* Retreives the value of usColorDataPresent attribute
	* @return usColorDataPresent
	**/

	public String getUsColorDataPresent(){
		return usColorDataPresent;
	}

	/**
	* Sets the value of usColorDataPresent  attribue
	**/

	public void setUsColorDataPresent(String usColorDataPresent){
		this.usColorDataPresent = usColorDataPresent;
	}

	/** ultrasound multi modality **/
	private String usMultiModality;
	
	/**
	 * Retrieve the value of us multi modality
	 * @return usMultiModality
	 */
	public String getUsMultiModality(){
		return usMultiModality;
	}
	
	/**
	 * Set the value of us multi Modality
	 * @return void
	 */
	public void setUsMultiModality(String usMultiModality)
	{
		this.usMultiModality = usMultiModality;
	}

	/**
	* An associated gov.nih.nci.ncia.domain.TrialDataProvenance object
	**/

	private TrialDataProvenanceDoc dataProvenance;
	/**
	* Retreives the value of dataProvenance attribue
	* @return dataProvenance
	**/

	public TrialDataProvenanceDoc getDataProvenance(){
		return dataProvenance;
	}
	/**
	* Sets the value of dataProvenance attribue
	**/

	public void setDataProvenance(TrialDataProvenanceDoc dataProvenance){
		this.dataProvenance = dataProvenance;
	}

	/**
	* An associated gov.nih.nci.ncia.domain.Study object
	**/

	private StudyDoc study;
	/**
	* Retreives the value of study attribue
	* @return study
	**/

	public StudyDoc getStudy(){
		return study;
	}
	/**
	* Sets the value of study attribue
	**/

	public void setStudy(StudyDoc study){
		this.study = study;
	}

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof GeneralImageSubDoc)
		{
			GeneralImageSubDoc c =(GeneralImageSubDoc)obj;
			if(getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}

	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null)
			return getId().hashCode();
		return 0;
	}

	public String getImageTypeValue3() {
		return imageTypeValue3;
	}

	public void setImageTypeValue3(String imageTypeValue3) {
		this.imageTypeValue3 = imageTypeValue3;
	}
	/**
	* Description of the type of data taken. **/
	private String scanningSequence;
	
	/**
	* Retrieves the value of scanningSequence attribute
	* @return scanningSequence
	**/

	public String getScanningSequence(){
		return scanningSequence;
	}

	/**
	* Sets the value of scanningSequence attribute
	**/

	public void setScanningSequence(String scanningSequence){
		this.scanningSequence = scanningSequence;
	}
	/**
	* User defined name for the Scanning Sequence (0018,0020) and Sequence Variant (0018,0021) combination.  **/
	private String sequenceName;
	
	/**
	* Retrieves the value of sequenceName attribute
	* @return sequenceName
	**/

	public String getSequenceName(){
		return sequenceName;
	}

	
	/**
	* Sets the value of sequenceName attribute
	**/

	public void setSequenceName(String sequenceName){
		this.sequenceName = sequenceName;
	}
		
	/**
	* Variant of the Scanning Sequence. **/
	private String sequenceVariant;
	
	/**
	* Retrieves the value of sequenceVariant attribute
	* @return sequenceVariant
	**/

	public String getSequenceVariant(){
		return sequenceVariant;
	}

	/**
	* Sets the value of sequenceVariant attribute
	**/

	public void setSequenceVariant(String sequenceVariant){
		this.sequenceVariant = sequenceVariant;
	}	
	/**
	* Nucleus that is resonant at the imaging frequency. **/
	private String imagedNucleus;
	
	/**
	* Retrieves the value of imagedNucleus attribute
	* @return imagedNucleus
	**/

	public String getImagedNucleus(){
		return imagedNucleus;
	}

	/**
	* Sets the value of imagedNucleus attribute
	**/

	public void setImagedNucleus(String imagedNucleus){
		this.imagedNucleus = imagedNucleus;
	}
		
	/**
	* Text to capture the sequence that identifies the anatomic region of interest in an instance, as specified in a DICOM tag.	**/
	private String anatomicRegionSeq;
	/**
	* Retreives the value of anatomicRegionSeq attribute
	* @return anatomicRegionSeq
	**/

	public String getAnatomicRegionSeq(){
		return anatomicRegionSeq;
	}

	/**
	* Sets the value of anatomicRegionSeq attribue
	**/

	public void setAnatomicRegionSeq(String anatomicRegionSeq){
		this.anatomicRegionSeq = anatomicRegionSeq;
	}
	
	/**
	* Type of convolution kernel or algorithm used to reconstruct imaging data.	**/
	private String convolutionKernel;
	/**
	* Retreives the value of convolutionKernel attribute
	* @return convolutionKernel
	**/

	public String getConvolutionKernel(){
		return convolutionKernel;
	}

	/**
	* Sets the value of convolutionKernel attribue
	**/

	public void setConvolutionKernel(String convolutionKernel){
		this.convolutionKernel = convolutionKernel;
	}
	/**
	* Type of scan used in medical imaging as specified in DICOM tag.	**/
	private String scanOptions;
	/**
	* Retreives the value of scanOptions attribute
	* @return scanOptions
	**/

	public String getScanOptions(){
		return scanOptions;
	}

	/**
	* Sets the value of scanOptions attribue
	**/

	public void setScanOptions(String scanOptions){
		this.scanOptions = scanOptions;
	}
	private List<DicomTagDTO> tagInfo;
	public List<DicomTagDTO> getTagInfo() {
		return tagInfo;
	}

	public void setTagInfo(List<DicomTagDTO> tagInfo) {
		this.tagInfo = tagInfo;
	}
	
}