/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.textsupport;


import java.io.Serializable;
	/**
	* Computed Tomography image	**/
public class MRImageSubDoc  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	/**
	* identifier	**/
	private Integer seriesPKId;
	/**
	* Retrieves the value of seriesPKId attribute
	* @return seriesPKId
	**/

	public Integer getSeriesPKId(){
		return seriesPKId;
	}

	/**
	* Sets the value of seriesPKId attribute
	**/

	public void setSeriesPKId(Integer seriesPKId){
		this.seriesPKId = seriesPKId;
	}
	
	/**
	* An associated gov.nih.nci.ncia.domain.GeneralSeries object
	**/

	private GeneralSeriesSubDoc generalSeries;
	/**
	* Retrieves the value of generalSeries attribute
	* @return generalSeries
	**/

	public GeneralSeriesSubDoc getGeneralSeries(){
		return generalSeries;
	}
	/**
	* Sets the value of generalSeries attribute
	**/

	public void setGeneralSeries(GeneralSeriesSubDoc generalSeries){
		this.generalSeries = generalSeries;
	}
		
	/**
	* MR image identification characteristics kept in value 3 of Image Type (0008,0008) **/
	private String imageTypeValue3;
	
	/**
	* Retrieves the value of imageTypeValue3 attribute
	* @return imageTypeValue3
	**/

	public String getImageTypeValue3(){
		return imageTypeValue3;
	}

	/**
	* Sets the value of imageTypeValue3 attribute
	**/

	public void setImageTypeValue3(String imageTypeValue3){
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
	* The period of time in msec between the beginning of a pulse sequence and the beginning of the succeeding (essentially identical) pulse sequence. **/
	private Double repetitionTime;
	/**
	* Retrieves the value of repetitionTime attribute
	* @return repetitionTime
	**/
	
	public Double getRepetitionTime(){
		return repetitionTime;
	}
	
	/**
	* Sets the value of repetitionTime attribute
	**/
	
	public void setRepetitionTime(Double repetitionTime){
		this.repetitionTime = repetitionTime;
	}	
	
	/**
	* Time in ms between the middle of the excitation pulse and the peak of the echo produced (kx=0). 
	* In the case of segmented k-space, the TE(eff) is the time between the middle of the excitation 
	* pulse to the peak of the echo that is used to cover the center of k-space (i.e.-kx=0, ky=0).  **/
	private Double echoTime;
	/**
	* Retrieves the value of echoTime attribute
	* @return echoTime
	**/
	
	public Double getEchoTime(){
		return echoTime;
	}
	
	/**
	* Sets the value of echoTime attribute
	**/
	
	public void setEchoTime(Double echoTime){
		this.echoTime = echoTime;
	}		
	
	/**
	* Time in msec after the middle of inverting RF pulse to middle of excitation pulse to detect 
	* the amount of longitudinal magnetization.  **/
	private Double inversionTime;
	/**
	* Retrieves the value of inversionTime attribute
	* @return inversionTime
	**/
	
	public Double getInversionTime(){
		return inversionTime;
	}
	
	/**
	* Sets the value of inversionTime attribute
	**/
	
	public void setInversionTime(Double inversionTime){
		this.inversionTime = inversionTime;
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
	* Nominal field strength of MR magnet, in Tesla. **/
	private Double magneticFieldStrength;
	/**
	* Retrieves the value of magneticFieldStrength attribute
	* @return magneticFieldStrength
	**/
	
	public Double getMagneticFieldStrength(){
		return magneticFieldStrength;
	}
	
	/**
	* Sets the value of magneticFieldStrength attribute
	**/
	
	public void setMagneticFieldStrength(Double magneticFieldStrength){
		this.magneticFieldStrength = magneticFieldStrength;
	}	
	
	/**
	* Calculated whole body Specific Absorption Rate in watts/kilogram.   **/
	private Double sar;
	/**
	* Retrieves the value of sar attribute
	* @return sar
	**/
	
	public Double getSar(){
		return sar;
	}
	
	/**
	* Sets the value of sar attribute
	**/
	
	public void setSar(Double sar){
		this.sar = sar;
	}		
	
	/**
	*The rate of change of the gradient coil magnetic flux density with time (T/s).   **/
	private Double dbDt;
	/**
	* Retrieves the value of dbDt attribute
	* @return dbDt
	**/
	
	public Double getDbDt(){
		return dbDt;
	}
	
	/**
	* Sets the value of dbDt attribute
	**/
	
	public void setDbDt(Double dbDt){
		this.dbDt = dbDt;
	}

	/**
	*Time, in msec, between peak of the R wave and the peak of the echo produced. 
	*In the case of segmented k-space, the TE(eff) is the time between the peak 
	*of the echo that is used to cover the center of k-space.   **/
	private Double triggerTime;
	/**
	* Retrieves the value of triggerTime attribute
	* @return triggerTime
	**/
	
	public Double getTriggerTime(){
		return triggerTime;
	}
	
	/**
	* Sets the value of triggerTime attribute
	**/
	
	public void setTriggerTime(Double triggerTime){
		this.triggerTime = triggerTime;
	}
	
	/**
	* One or more characters used to identify, name, or characterize the nature, properties, or contents of a thing.	**/
	private Integer id;
	/**
	* Retrieves the value of id attribute
	* @return id
	**/

	public Integer getId(){
		return id;
	}

	/**
	* Sets the value of id attribute
	**/

	public void setId(Integer id){
		this.id = id;
	}

	/** 
	 * Angio Image Indicator. Primary image for Angio processing.	**/
	private String angioFlag;
	/**
	* Retrieves the value of angioFlag attribute
	* @return angioFlag
	**/

	public String getAngioFlag(){
		return angioFlag;
	}

	/**
	* Sets the value of angioFlag attribute
	**/

	public void setAngioFlag(String angioFlag){
		this.angioFlag = angioFlag;
	}
	
	/**
	* An associated gov.nih.nci.ncia.domain.GeneralImage object
	**/
			
	private GeneralImageSubDoc generalImage;
	/**
	* Retrieves the value of generalImage attribute
	* @return generalImage
	**/
	
	public GeneralImageSubDoc getGeneralImage(){
		return generalImage;
	}
	/**
	* Sets the value of generalImage attribute
	**/

	public void setGeneralImage(GeneralImageSubDoc generalImage){
		this.generalImage = generalImage;
	}
			
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof MRImageSubDoc) 
		{
			MRImageSubDoc c =(MRImageSubDoc)obj; 			 
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
	
}