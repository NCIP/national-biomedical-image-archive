/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import junit.framework.TestCase;
import gov.nih.nci.nbia.searchresult.PatientSearchResult;
import gov.nih.nci.nbia.searchresult.PatientSearchResultImpl;

public class PatientSearchResultsTestCase extends TestCase {

	public void testErrorResults() {
		Exception error = new Exception("foo");
		
		PatientSearchResults patientSearchResults = new PatientSearchResults(node, error);
		
		assertNull(patientSearchResults.getResults());
		assertEquals(patientSearchResults.getSearchError(), error);
	}
	
	public void testResults() {
		PatientSearchResult[] results = new PatientSearchResult[3];
		results[0] = new PatientSearchResultImpl();
		results[1] = new PatientSearchResultImpl();
		results[2] = new PatientSearchResultImpl();
		
		PatientSearchResults patientSearchResults = new PatientSearchResults(node, results);
		
		assertNull(patientSearchResults.getSearchError());
		assertTrue(patientSearchResults.getResults().length==3);
	}	

}
