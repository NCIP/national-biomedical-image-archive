package gov.nih.nci.nbia.textsupport;
import gov.nih.nci.ncia.search.PatientSearchResultImpl;
import gov.nih.nci.ncia.search.PatientSearchResult;
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
    this.associateLocation(input.associatedLocation());

}
public String getHit() {
	return hit;
}

public void setHit(String hit) {
	this.hit = hit;
}
}
