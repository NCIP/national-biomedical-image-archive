package gov.nih.nci.ncia.search;

import gov.nih.nci.nbia.search.PatientSearchResults;
import junit.framework.TestCase;

public class PatientSearchResultsTestCase extends TestCase {

	public void testErrorResults() {
		Exception error = new Exception("foo");
		NBIANode node = new NBIANode(true, "display", "url");
		
		PatientSearchResults patientSearchResults = new PatientSearchResults(node, error);
		
		assertNull(patientSearchResults.getResults());
		assertEquals(patientSearchResults.getNode(), node);
		assertEquals(patientSearchResults.getSearchError(), error);
	}
	
	public void testResults() {
		PatientSearchResult[] results = new PatientSearchResult[3];
		results[0] = new PatientSearchResultImpl();
		results[1] = new PatientSearchResultImpl();
		results[2] = new PatientSearchResultImpl();
		
		NBIANode node = new NBIANode(true, "display", "url");
		
		PatientSearchResults patientSearchResults = new PatientSearchResults(node, results);
		
		assertNull(patientSearchResults.getSearchError());
		assertEquals(patientSearchResults.getNode(), node);
		assertTrue(patientSearchResults.getResults().length==3);
	}	

}
