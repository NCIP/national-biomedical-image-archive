package gov.nih.nci.nbia.textsupport;
import gov.nih.nci.nbia.searchresult.PatientSearchResult;
public interface PatientTextSearchResult extends PatientSearchResult{
	public String getHit();
	public void setHit(String hit);
}
