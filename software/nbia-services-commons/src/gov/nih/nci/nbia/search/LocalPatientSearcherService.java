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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

/**
 * This implementation is a "bootstrap" implementation that doesnt
 * support remote nodes....
 */
public class LocalPatientSearcherService extends AbstractPatientSearcherService  {
		
	/**
	 * {@inheritDoc}
	 */
	protected List<Future<PatientSearchResults>> 
        searchNodes(CompletionService<PatientSearchResults> completionService,
	                List<NBIANode> nodesToSearch,
	                DICOMQuery dicomQuery) {
		
		
		List<Future<PatientSearchResults>> futures =  
		    new ArrayList<Future<PatientSearchResults>>();
		
		//makes sense for this only to have one node
		for(NBIANode nodeToSearch : nodesToSearch) {
			if(nodeToSearch.isLocal()) {
				Future<PatientSearchResults> future =
					completionService.submit(new LocalSearchForPatientsRequest(nodeToSearch, 
					     	                                                   dicomQuery));
				futures.add(future);
			}
			else {
				//if you pass in remote nodes to this impl, the presentation above 
				//this wont work..... 
				System.out.println("Ignoring remote node:"+nodeToSearch.getDisplayName());
			}
		}
		return futures;
	}
}
