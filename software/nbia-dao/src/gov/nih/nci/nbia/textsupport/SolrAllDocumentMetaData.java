package gov.nih.nci.nbia.textsupport;

public class SolrAllDocumentMetaData {
private String term;
private String foundValue;
private String patientId;
private String documentId;
public SolrAllDocumentMetaData(String termIn, String foundValueIn, String documentIdIn, String patientIdIn)
{
	term=termIn;
	foundValue=foundValueIn;
	documentId=documentIdIn;
	patientId=patientIdIn;
}
public String getTerm() {
	return term;
}
public void setTerm(String term) {
	this.term = term;
}
public String getFoundValue() {
	return foundValue;
}
public void setFoundValue(String foundValue) {
	this.foundValue = foundValue;
}

public String getDocumentId() {
	return documentId;
}
public void setDocumentId(String documentId) {
	this.documentId = documentId;
}
public String getPatientId() {
	return patientId;
}
public void setPatientId(String patientId) {
	this.patientId = patientId;
}
}
