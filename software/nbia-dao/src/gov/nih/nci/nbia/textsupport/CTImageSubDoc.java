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
public class CTImageSubDoc  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

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
	* Numeric value representing the ratio of the Table Feed per Rotation to the Total Collimation Width.	**/
	private Double CTPitchFactor;
	/**
	* Retreives the value of CTPitchFactor attribute
	* @return CTPitchFactor
	**/

	public Double getCTPitchFactor(){
		return CTPitchFactor;
	}

	/**
	* Sets the value of CTPitchFactor attribue
	**/

	public void setCTPitchFactor(Double CTPitchFactor){
		this.CTPitchFactor = CTPitchFactor;
	}
	
		/**
	* Millimeter value for the diameter of the region over which data was collected as specified in DICOM tag.	**/
	private Double dataCollectionDiameter;
	/**
	* Retreives the value of dataCollectionDiameter attribute
	* @return dataCollectionDiameter
	**/

	public Double getDataCollectionDiameter(){
		return dataCollectionDiameter;
	}

	/**
	* Sets the value of dataCollectionDiameter attribue
	**/

	public void setDataCollectionDiameter(Double dataCollectionDiameter){
		this.dataCollectionDiameter = dataCollectionDiameter;
	}
	
		/**
	* Current exposure of x-ray image in milliAmp seconds, as calculated from time and x-ray tube current, as specified in DICOM tag.	**/
	private Integer exposure;
	/**
	* Retreives the value of exposure attribute
	* @return exposure
	**/

	public Integer getExposure(){
		return exposure;
	}

	/**
	* Sets the value of exposure attribue
	**/

	public void setExposure(Integer exposure){
		this.exposure = exposure;
	}
	
		/**
	* Value for exposure expressed in microAmp seconds.	**/
	private Integer exposureInMicroAs;
	/**
	* Retreives the value of exposureInMicroAs attribute
	* @return exposureInMicroAs
	**/

	public Integer getExposureInMicroAs(){
		return exposureInMicroAs;
	}

	/**
	* Sets the value of exposureInMicroAs attribue
	**/

	public void setExposureInMicroAs(Integer exposureInMicroAs){
		this.exposureInMicroAs = exposureInMicroAs;
	}
	
		/**
	* Value for the time of x-ray exposure expressed as number of milliseconds.	**/
	private Integer exposureTime;
	/**
	* Retreives the value of exposureTime attribute
	* @return exposureTime
	**/

	public Integer getExposureTime(){
		return exposureTime;
	}

	/**
	* Sets the value of exposureTime attribue
	**/

	public void setExposureTime(Integer exposureTime){
		this.exposureTime = exposureTime;
	}
	
		/**
	* Numeric degree value for the nominal angle of tilt of the scanning gantry. This value is not intended for mathematical computations.
	**/
	private Double gantryDetectorTilt;
	/**
	* Retreives the value of gantryDetectorTilt attribute
	* @return gantryDetectorTilt
	**/

	public Double getGantryDetectorTilt(){
		return gantryDetectorTilt;
	}

	/**
	* Sets the value of gantryDetectorTilt attribue
	**/

	public void setGantryDetectorTilt(Double gantryDetectorTilt){
		this.gantryDetectorTilt = gantryDetectorTilt;
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
	* Numeric value to represent the Kilovoltage Peak (kVp) reading as recorded in a DICOM tag.	**/
	private Double KVP;
	/**
	* Retreives the value of KVP attribute
	* @return KVP
	**/

	public Double getKVP(){
		return KVP;
	}

	/**
	* Sets the value of KVP attribue
	**/

	public void setKVP(Double KVP){
		this.KVP = KVP;
	}
	
		/**
	* Numeric millimeter value for the diameter in millimeters of the region from within which data were used in creating the reconstruction an image. Data may exist outside this region and portions of the patient may exist outside this region.	**/
	private Double reconstructionDiameter;
	/**
	* Retreives the value of reconstructionDiameter attribute
	* @return reconstructionDiameter
	**/

	public Double getReconstructionDiameter(){
		return reconstructionDiameter;
	}

	/**
	* Sets the value of reconstructionDiameter attribue
	**/

	public void setReconstructionDiameter(Double reconstructionDiameter){
		this.reconstructionDiameter = reconstructionDiameter;
	}
	
		/**
	* Period of time required for a single complete turn of the source around the gantry orbit, expressed as a number of seconds.	**/
	private Double revolutionTime;
	/**
	* Retreives the value of revolutionTime attribute
	* @return revolutionTime
	**/

	public Double getRevolutionTime(){
		return revolutionTime;
	}

	/**
	* Sets the value of revolutionTime attribue
	**/

	public void setRevolutionTime(Double revolutionTime){
		this.revolutionTime = revolutionTime;
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
	
		/**
	* Width of a single row of acquired data expressed as a number of millimeters (mm).	**/
	private Double singleCollimationWidth;
	/**
	* Retreives the value of singleCollimationWidth attribute
	* @return singleCollimationWidth
	**/

	public Double getSingleCollimationWidth(){
		return singleCollimationWidth;
	}

	/**
	* Sets the value of singleCollimationWidth attribue
	**/

	public void setSingleCollimationWidth(Double singleCollimationWidth){
		this.singleCollimationWidth = singleCollimationWidth;
	}
	
		/**
	* Motion in millimeters of the table during a completion revolution of the source around the gantry orbit.	**/
	private Double tableFeedPerRotation;
	/**
	* Retreives the value of tableFeedPerRotation attribute
	* @return tableFeedPerRotation
	**/

	public Double getTableFeedPerRotation(){
		return tableFeedPerRotation;
	}

	/**
	* Sets the value of tableFeedPerRotation attribue
	**/

	public void setTableFeedPerRotation(Double tableFeedPerRotation){
		this.tableFeedPerRotation = tableFeedPerRotation;
	}
	
		/**
	* Numeric value to represent the distance in millimeters that the table moves in one second during the gathering of data that resulted in an image.	**/
	private Double tableSpeed;
	/**
	* Retreives the value of tableSpeed attribute
	* @return tableSpeed
	**/

	public Double getTableSpeed(){
		return tableSpeed;
	}

	/**
	* Sets the value of tableSpeed attribue
	**/

	public void setTableSpeed(Double tableSpeed){
		this.tableSpeed = tableSpeed;
	}
	
		/**
	* Width of the total collimation over the area of active x-ray detection, expressed as a number of millimeters (mm).	**/
	private Double totalCollimationWidth;
	/**
	* Retreives the value of totalCollimationWidth attribute
	* @return totalCollimationWidth
	**/

	public Double getTotalCollimationWidth(){
		return totalCollimationWidth;
	}

	/**
	* Sets the value of totalCollimationWidth attribue
	**/

	public void setTotalCollimationWidth(Double totalCollimationWidth){
		this.totalCollimationWidth = totalCollimationWidth;
	}
	
		/**
	* X-ray tube current expressed in MilliAmp(s) as specified in DICOM tag.	**/
	private Integer XRayTubeCurrent;
	/**
	* Retreives the value of XRayTubeCurrent attribute
	* @return XRayTubeCurrent
	**/

	public Integer getXRayTubeCurrent(){
		return XRayTubeCurrent;
	}

	/**
	* Sets the value of XRayTubeCurrent attribue
	**/

	public void setXRayTubeCurrent(Integer XRayTubeCurrent){
		this.XRayTubeCurrent = XRayTubeCurrent;
	}
	
	/**
	* An associated gov.nih.nci.ncia.domain.GeneralImage object
	**/
			
	private GeneralImageSubDoc generalImage;
	/**
	* Retreives the value of generalImage attribue
	* @return generalImage
	**/
	
	public GeneralImageSubDoc getGeneralImage(){
		return generalImage;
	}
	/**
	* Sets the value of generalImage attribue
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
		if(obj instanceof CTImageSubDoc) 
		{
			CTImageSubDoc c =(CTImageSubDoc)obj; 			 
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