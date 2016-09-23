package gov.nih.nci.nbia.textsupport;
import gov.nih.nci.nbia.searchresult.PatientSearchResultImpl;
import gov.nih.nci.nbia.searchresult.PatientSearchResult;
import gov.nih.nci.nbia.textsupport.SolrFoundDocumentMetaData;
public class PatientTextSearchResultImpl extends PatientSearchResultImpl implements PatientTextSearchResult{
private String hit;
public PatientTextSearchResultImpl(PatientSearchResult input)
{
	this.setProject(input.getProject());
	this.setId(input.getId());
	this.setStudyIdentifiers(input.getStudyIdentifiers());
	this.setSubjectId(input.getSubjectId());
	this.setTotalNumberOfSeries(input.getTotalNumberOfSeries());
	this.setTotalNumberOfStudies(input.getTotalNumberOfStudies());
}

public PatientTextSearchResultImpl()
{

}

public String getHit() {
	return hit;
}

public void setHit(String hit) {
	this.hit = hit;
}
}
