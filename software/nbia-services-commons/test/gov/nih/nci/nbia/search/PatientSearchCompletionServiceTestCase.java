/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.ncia.search.NBIANode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import junit.framework.TestCase;

public class PatientSearchCompletionServiceTestCase extends TestCase {

	public void testPatientSearchCompletionService() throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		
		CompletionService<PatientSearchResults> completionService = 
			new ExecutorCompletionService<PatientSearchResults>(executorService);
		
        List<NBIANode> nodesToSearch = new ArrayList<NBIANode>();
        nodesToSearch.add(new NBIANode(true, "d1", "u1"));
        nodesToSearch.add(new NBIANode(false, "d2", "u2"));
        nodesToSearch.add(new NBIANode(true, "d3", "u3"));
        
		PatientSearchCompletionService patientSearchCompletionService
			= new PatientSearchCompletionService(completionService, 
					                             nodesToSearch);
		
		assertEquals(patientSearchCompletionService.getCompletionService(), completionService);
		assertTrue(patientSearchCompletionService.getNodesToSearch().size()==3);
	}
	
	
	public class FakeCallable implements Callable<PatientSearchResults> {
		public PatientSearchResults call() {
			return null;
		}
	}
}
