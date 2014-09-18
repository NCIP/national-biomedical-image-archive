package gov.nih.nci.nbia.wadosupport;

public class WADOParameters {
private String requestType;
private String studyUID;
private String seriesUID;
private String objectUID;
private String contentType="application/dicom";
private String charset;
private String anonymize;
private String annotation;
private String rows;
private String columns;
private String region;
private String windowCenter;
private String windowWidth;
private String frameNumber;
private String imageQuality;
private String presentationUID;
private String presentationSeriesUID;
private String transferSyntax;
private String errorMessage;
public String getRequestType() {
	return requestType;
}
public void setRequestType(String requestType) {
	if (!requestType.equals("WADO")){
		addError("requestType not WADO");
	}
	this.requestType = requestType;
}
public String getStudyUID() {
	return studyUID;
}
public void setStudyUID(String studyUID) {
	this.studyUID = studyUID;
}
public String getSeriesUID() {
	return seriesUID;
}
public void setSeriesUID(String seriesUID) {
	this.seriesUID = seriesUID;
}
public String getObjectUID() {
	return objectUID;
}
public void setObjectUID(String objectUID) {
	this.objectUID = objectUID;
}
public String getContentType() {
	return contentType;
}
public void setContentType(String contentType) {
	if ((!contentType.equals("image/jpeg"))&&(!contentType.equals("application/dicom"))){
		addError("NBIA only supports image/jpeg or application/dicom");
	}
	this.contentType = contentType;
}
public String getCharset() {
	return charset;
}
public void setCharset(String charset) {
	addError("NBIA does not support charset parameter");
	this.charset = charset;
}
public String getAnonymize() {
	return anonymize;
}
public void setAnonymize(String anonymize) {
	//addError("NBIA does not support anonymize parameter, images already anonymized");
	this.anonymize = anonymize;
}
public String getAnnotation() {
	return annotation;
}
public void setAnnotation(String annotation) {
	addError("NBIA does not support annotation parameter");
	this.annotation = annotation;
}
public String getRows() {
	return rows;
}
public void setRows(String rows) {
	if (rows==null) return;
	if (contentType.equals("application/dicom"))
	{
		addError("rows not available for application/dicom");
	}
	if (!isValidPositive(rows))
	{
		addError("rows is not valid");
	}
	this.rows = rows;
}
public String getColumns() {
	return columns;
}
public void setColumns(String columns) {
	if (columns==null) return;
	if (contentType.equals("application/dicom"))
	{
		addError("columns not available for application/dicom");
	}
	if (!isValidPositive(columns))
	{
		addError("columns is not valid");
	}
	this.columns = columns;
}
public String getRegion() {
	return region;
}
public void setRegion(String region) {
	addError("region not supported by NBIA");
	this.region = region;
}
public String getWindowCenter() {
	return windowCenter;
}
public void setWindowCenter(String windowCenter) {
	if (windowCenter==null) return;
	if (contentType.equals("application/dicom"))
	{
		addError("windowCenter not available for application/dicom");
	}	
	if (!isValidPositive(windowCenter))
	{
		addError("windowCenter is not valid, needs to be a positive integer");
	}
	this.windowCenter = windowCenter;
}
public String getWindowWidth() {
	return windowWidth;
}
public void setWindowWidth(String windowWidth) {
	if (windowWidth==null) return;
	if (contentType.equals("application/dicom"))
	{
		addError("windowWidth not available for application/dicom");
	}
	if (!isNumeric(windowWidth))
	{
		addError("windowWidth is not valid, needs to be a positive integer");
	}
	this.windowWidth = windowWidth;
}
public String getImageQuality() {
	return imageQuality;
}
public void setImageQuality(String imageQuality) {
	if (imageQuality==null) return;
	if (contentType.equals("application/dicom"))
	{
		addError("imageQuality not available for application/dicom");
	}
	if (!isQualityValid(imageQuality))
	{
		addError("imageQuality is not valid, should be an integer between 1 and 100");
	}
	this.imageQuality = imageQuality;
}

public void setFrameNumber(String frameNumber) {
	if (frameNumber==null) return;
	if (contentType.equals("application/dicom"))
	{
		addError("imageQuality not available for application/dicom");
	}
	if (!isValidPositive(frameNumber))
	{
		addError("Frame Number is not valid, needs to be a positive integer");
	}
	this.frameNumber = frameNumber;
}
public String getPresentationUID() {
	return presentationUID;
}
public void setPresentationUID(String presentationUID) {

	addError("presentationUID not supported by NBIA");

	this.presentationUID = presentationUID;
}
public String getPresentationSeriesUID() {
	return presentationSeriesUID;
}
public void setPresentationSeriesUID(String presentationSeriesUID) {

		addError("presentationSeriesUID not supported by NBIA");

	this.presentationSeriesUID = presentationSeriesUID;
}
public String getTransferSyntax() {
	return transferSyntax;
}
public void setTransferSyntax(String transferSyntax) {

		// addError("transferSyntax not supported by NBIA");

	this.transferSyntax = transferSyntax;
}
public String getErrorMessage() {
	return errorMessage;
}
public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}

private void addError(String newError)
{
	if (errorMessage!=null)
	{
		errorMessage = errorMessage+"; ";
	} else
	{
		errorMessage="";
	}
	errorMessage = errorMessage+newError;	
		
}
public String validate()
{
	if (studyUID==null)
	{
		addError("Missing studyUID");
	}
	if (seriesUID==null)
	{
		addError("Missing seriesUID");
	}
	if (objectUID==null)
	{
		addError("Missing objectUID");
	}
	if (requestType==null)
	{
		addError("Missing requestType");
	}
	if ((windowWidth!=null&&windowCenter==null)||(windowWidth==null&&windowCenter!=null))
	{
		addError("windowWidth and windowCenter must both be set to be used");
	}
	return errorMessage;
}
private static boolean isNumeric(String str)  
{  
  try  
  {  
    double d = Double.parseDouble(str);  
  }  
  catch(NumberFormatException nfe)  
  {  
    return false;  
  }  
  return true;  
}
private static boolean isInt(String str)  
{  
  try  
  {  
    Integer d = Integer.parseInt(str);  
  }  
  catch(NumberFormatException nfe)  
  {  
    return false;  
  }  
  return true;  
}
private boolean isQualityValid(String str)  
{  
	int i;
  try  
  {  
    i = Integer.parseInt(str);  
  }  
  catch(NumberFormatException nfe)  
  {  
    return false;  
  }  
  if (i>=1&&i<=100)
  {
	  return true;  
  }
  return false;  
}
private static boolean isValidPositive(String str)  
{  
  try  
  {  
    Integer i = Integer.parseInt(str);  
    if (i<0)
    {
    	return false;
    }
  }  
  catch(NumberFormatException nfe)  
  {  
    return false;  
  }  
  return true;  
}
public float getQualityFloat()  
{  
	if (imageQuality==null)
	{
		return -1;
	}
    return Float.parseFloat(imageQuality)/100;  

}
public int getFrameNumberInt()  
{  
	if (frameNumber==null)
	{
		return 0;
	}
    return Integer.parseInt(frameNumber);  

}
public int getRowsInt()  
{  
	if (rows==null)
	{
		return -1;
	}
    return Integer.parseInt(rows);  

}
public int getColumnsInt()  
{  
	if (columns==null)
	{
		return -1;
	}
    return Integer.parseInt(columns);  

}
public int getWindowWidthInt()  
{  
	if (windowWidth==null)
	{
		return -1;
	}
    return Integer.parseInt(windowWidth);  

}
public int getWindowCenterInt()  
{  
	if (windowCenter==null)
	{
		return -1;
	}
    return Integer.parseInt(windowCenter);  

}
@Override
public String toString() {
	return "WADOParameters [requestType=" + requestType + ", studyUID="
			+ studyUID + ", seriesUID=" + seriesUID + ", objectUID="
			+ objectUID + ", contentType=" + contentType + ", charset="
			+ charset + ", anonymize=" + anonymize + ", annotation="
			+ annotation + ", rows=" + rows + ", columns=" + columns
			+ ", region=" + region + ", windowCenter=" + windowCenter
			+ ", windowWidth=" + windowWidth + ", imageQuality=" + imageQuality
			+ ", presentationUID=" + presentationUID
			+ ", presentationSeriesUID=" + presentationSeriesUID
			+ ", transferSyntax=" + transferSyntax + "]";
}

public static void main(String args[])
{
	WADOParameters wp=new WADOParameters();
	System.out.println(wp.isQualityValid("100"));
	System.out.println(wp.isQualityValid("50"));
	wp.setImageQuality("50");
	System.out.println(wp.getQualityFloat());
	System.out.println(wp.isQualityValid("T"));
}

}
