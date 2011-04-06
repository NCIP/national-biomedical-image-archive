package gov.nih.nci.nbia.search;

import gov.nih.nci.ncia.search.NBIANode;

import java.util.List;
import java.util.concurrent.CompletionService;

/**
 * This object bundles an actual completion service with the nodes
 * that were searched.
 * 
 * <p>This allows to to know what we are waiting for in total in terms
 * of results.
 */
public class PatientSearchCompletionService {
	public PatientSearchCompletionService(CompletionService<PatientSearchResults> completionService, 
			                              List<NBIANode> nodesToSearch) {
		this.completionService = completionService;
		this.nodesToSearch = nodesToSearch;
	}
	
	public CompletionService<PatientSearchResults> getCompletionService() {
		return completionService;
	}
	
	public List<NBIANode> getNodesToSearch() {
		return nodesToSearch;
	}
	
	////////////////////////////////////////PRIVATE/////////////////////////////////////
	private List<NBIANode> nodesToSearch;
	
	private CompletionService<PatientSearchResults> completionService;
}
