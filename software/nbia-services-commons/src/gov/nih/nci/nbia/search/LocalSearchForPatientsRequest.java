/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.ncia.search.NBIANode;
import gov.nih.nci.ncia.search.PatientSearchResult;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * The Callable that encapsualte doing a "local" database search for patients.
 * (As opposed to going out remote).
 */
public class LocalSearchForPatientsRequest implements Callable<PatientSearchResults> {

	public LocalSearchForPatientsRequest(NBIANode localNode, DICOMQuery dicomQuery) {
		//assert localNode.isLocal()
		this.dicomQuery = dicomQuery;
		this.localNode = localNode;
	}
		
	public PatientSearchResults call() throws Exception {			

		PatientSearcher patientSearcher = new PatientSearcher();			
		List<PatientSearchResult> resultList = patientSearcher.searchForPatients(dicomQuery);
			
		PatientSearchResults patientSearchResults = new PatientSearchResults(localNode, 
				                                                             resultList.toArray(new PatientSearchResult[]{}));
		return patientSearchResults;
	}
	
	////////////////////////////////////////PRIVATE//////////////////////////////////
	
	private DICOMQuery dicomQuery;
	
	private NBIANode localNode;
}
