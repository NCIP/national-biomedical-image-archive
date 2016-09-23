/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.searchresult.PatientSearchResult;

/**
 * A tuple of a node and actual patient results from a "top level" search against 
 * that node. 
 * 
 * This object is immutable, keep it that way.
 */
public class PatientSearchResults {
	public PatientSearchResults(Exception searchError) {
		this.results = null;
		this.searchError = searchError;
	}
	
	public PatientSearchResults(PatientSearchResult[] results) {
		this.results = results;
		this.searchError = null;
	}
	
	
	/**
	 * The actual results for the node.  This can be null if there was 
	 * a search error.
	 */
	public PatientSearchResult[] getResults() {
		return results;
	}
	
	/**
	 * If these results are invalid, this is an exception related to why its invalid.
	 * This will be null if there was no error.
	 */
	public Exception getSearchError() {
		return searchError;
	}
	
	
	//////////////////////////////////////PRIVATE/////////////////////////////////////////////
	
	private Exception searchError;
	
	private PatientSearchResult[] results;
}