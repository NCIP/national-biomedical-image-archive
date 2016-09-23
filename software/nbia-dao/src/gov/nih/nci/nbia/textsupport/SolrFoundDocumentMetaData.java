package gov.nih.nci.nbia.textsupport;

public class SolrFoundDocumentMetaData {
private String term;
private String fieldName;
private String fieldValue;
private int index;
private String patientId;
private int documentId;
public SolrFoundDocumentMetaData(String termIn, String fieldNameIn, String fieldValueIn, int indexIn, int documentIdIn, String patientIdIn)
{
	term=termIn;
	fieldName=fieldNameIn;
	fieldValue=fieldValueIn;
	index=indexIn;
	documentId=documentIdIn;
	patientId=patientIdIn;
}
public String getTerm() {
	return term;
}
public void setTerm(String term) {
	this.term = term;
}
public String getFieldName() {
	return fieldName;
}
public void setFieldName(String fieldName) {
	this.fieldName = fieldName;
}
public String getFieldValue() {
	return fieldValue;
}
public void setFieldValue(String fieldValue) {
	this.fieldValue = fieldValue;
}
public int getIndex() {
	return index;
}
public void setIndex(int index) {
	this.index = index;
}
@Override
public String toString() {
	return "SolrFoundDocumentMetaData [term=" + term + ", fieldName="
			+ fieldName + ", fieldValue=" + fieldValue + ", index=" + index
			+ ", patientId=" + patientId + "]";
}
public int getDocumentId() {
	return documentId;
}
public void setDocumentId(int documentId) {
	this.documentId = documentId;
}
public String getPatientId() {
	return patientId;
}
public void setPatientId(String patientId) {
	this.patientId = patientId;
}
}
