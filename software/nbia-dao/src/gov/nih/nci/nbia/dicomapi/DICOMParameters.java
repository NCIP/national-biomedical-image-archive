package gov.nih.nci.nbia.dicomapi;

public class DICOMParameters {
private String patientID;
private String institutionName;
private String accessionNumber;
private String studyDate;
private String patientAge;
private String patientName;
private String studyID;
private String studyInstanceUID;
private String studyDescription;
private String seriesInstanceUID;
public static String starRemove(String input)
{
	if (input==null) return null;
	return input.replaceAll("\\*", "%");
}

public String getPatientID() {
	return patientID;
}
public void setPatientID(String patientID) {
	this.patientID = patientID;
}
public String getInstitutionName() {
	return institutionName;
}
public void setInstitutionName(String institutionName) {
	this.institutionName = institutionName;
}
public String getAccessionNumber() {
	return accessionNumber;
}
public void setAccessionNumber(String accessionNumber) {
	this.accessionNumber = accessionNumber;
}
public String getStudyDate() {
	return studyDate;
}
public void setStudyDate(String studyDate) {
	this.studyDate = studyDate;
}
public String getPatientAge() {
	return patientAge;
}
public void setPatientAge(String patientAge) {
	this.patientAge = patientAge;
}
public String getStudyID() {
	return starRemove(studyID);
}
public void setStudyID(String studyID) {
	this.studyID = studyID;
}
public String getStudyInstanceUID() {
	return starRemove(studyInstanceUID);
}
public void setStudyInstanceUID(String studyInstanceUID) {
	this.studyInstanceUID = studyInstanceUID;
}
public String getStudyDescription() {
	return starRemove(studyDescription);
}
public void setStudyDescription(String studyDescription) {
	this.studyDescription = studyDescription;
}
public String getPatientName() {
	return starRemove(patientName);
}
public void setPatientName(String patientName) {
	this.patientName = patientName;
}


public String getSeriesInstanceUID() {
	return starRemove(seriesInstanceUID);
}

public void setSeriesInstanceUID(String seriesInstanceUID) {
	this.seriesInstanceUID = seriesInstanceUID;
}



@Override
public String toString() {
	return "DICOMParameters [patientID=" + patientID + ", institutionName="
			+ institutionName + ", accessionNumber=" + accessionNumber
			+ ", studyDate=" + studyDate + ", patientAge=" + patientAge
			+ ", patientName=" + patientName + ", studyID=" + studyID
			+ ", studyInstanceUID=" + studyInstanceUID + ", studyDescription="
			+ studyDescription + ", seriesInstanceUID=" + seriesInstanceUID
			+ "]";
}

public boolean valid()
{
	if (patientName==null&&patientID==null&&studyInstanceUID==null&&studyDescription==null)
	{
		return false;
	}
	return true;
}

public static void main(String[] args)
{
	System.out.println(starRemove("he%llo*"));
}

}
