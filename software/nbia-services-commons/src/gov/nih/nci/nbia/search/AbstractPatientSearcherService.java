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

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Encapsulate features necessary for all (known) "search service" implementations
 * regardless of local v. remote or whatever
 */
public abstract class AbstractPatientSearcherService implements PatientSearcherService  {
		
	/**
	 * {@inheritDoc}
	 */	
	public PatientSearchCompletionService searchForPatients(List<NBIANode> nodesToSearch,
                                                            DICOMQuery dicomQuery) {

		if(executorService==null) {
			throw new RuntimeException("must call setExecutorService before invoking this");
		}
		
		if(existingFutures!=null) {
			shutdownExistingFutures();
		}
				
		CompletionService<PatientSearchResults> completionService = 
			 new ExecutorCompletionService<PatientSearchResults>(executorService);
		
		existingFutures = searchNodes(completionService, nodesToSearch, dicomQuery);
		
		PatientSearchCompletionService sizedCompletionService = 
			new PatientSearchCompletionService(completionService, 
				                               nodesToSearch);
		return sizedCompletionService;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void setExecutorService(ExecutorService executorService) {
		if(this.executorService!=null) {
			throw new RuntimeException("you are prolly doing something bad if you are re-setting this");
		}
		this.executorService  = executorService;
		
	}
	
	/////////////////////////////////////PROTECTED/////////////////////////////////////
	
	/**
	 * The meat of the method that could vary (remote v. local).
	 * Submits Callables to the completion service and returns the Futures
	 * for those submissions.
	 */
	protected abstract List<Future<PatientSearchResults>> 
	    searchNodes(CompletionService<PatientSearchResults> completionService,
		            List<NBIANode> nodesToSearch,
		            DICOMQuery dicomQuery);
			                    
	
	//////////////////////////////////////PRIVATE/////////////////////////////////////
	
	private ExecutorService executorService;	
	
	private List<Future<PatientSearchResults>> existingFutures;	
	
	/**
	 * Tell all existing futures to interrupt/shutdown.
	 */
	private void shutdownExistingFutures() {
		for(Future<PatientSearchResults> future : existingFutures) {
			if(future.isDone()) {
				future.cancel(true);
			}
		}
	}
}
