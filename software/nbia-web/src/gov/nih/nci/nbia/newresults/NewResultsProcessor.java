/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.2  2007/08/10 20:09:54  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:06:53  bauerd
* Initial Check in of reorganized components
*
* Revision 1.11  2006/12/18 01:16:03  dietrich
* Grid enhancement
*
* Revision 1.10  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.9  2006/11/10 13:59:04  shinohaa
* grid prototype
*
* Revision 1.8  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.newresults;

import gov.nih.nci.ncia.criteria.Criteria;
import gov.nih.nci.ncia.criteria.CurationStatusDateCriteria;
import gov.nih.nci.ncia.criteria.NodeCriteria;
import gov.nih.nci.ncia.criteria.PersistentCriteria;
import gov.nih.nci.nbia.dto.SavedQueryDTO;
import gov.nih.nci.nbia.lookup.LookupManager;
import gov.nih.nci.nbia.lookup.LookupManagerFactory;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.querystorage.QueryStorageManager;
import gov.nih.nci.nbia.search.LocalNode;
import gov.nih.nci.nbia.search.PatientSearchCompletionService;
import gov.nih.nci.nbia.search.PatientSearchResults;
import gov.nih.nci.nbia.search.PatientSearcherService;
import gov.nih.nci.nbia.search.PatientSearcherServiceFactory;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.util.SavedQueryReconstructor;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.ncia.search.NBIANode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import org.quartz.Job;
import org.quartz.JobExecutionContext;


public class NewResultsProcessor implements Job {

    public void execute(JobExecutionContext jec) {
        QueryStorageManager qsm = (QueryStorageManager)SpringApplicationContext.getBean("queryStorageManager");

        List<SavedQueryDTO> activeQueries = null;

        try {
            activeQueries = qsm.getListOfActiveNoNew();
        } catch (Exception e) {
        	System.out.println("Problem with new data results processor");
        	e.printStackTrace();
        }

        if (activeQueries != null) {

            for (SavedQueryDTO dto : activeQueries) {
                List<Criteria> kriteria = dto.getCriteriaList();
                DICOMQuery newQuery =  new DICOMQuery();

                if (!dto.getQueryName().equals("")) {
                    newQuery.setQueryName(dto.getQueryName());
                }

                newQuery.setSavedQueryId(dto.getId());

                // Only repopulates search fields for persistent criteria
                // because
                // other
                // criteria have dependent values.
                for (Criteria kritItem : kriteria) {
                    if (kritItem instanceof PersistentCriteria) {
                        PersistentCriteria persistCrit = (PersistentCriteria) kritItem;
                        SavedQueryReconstructor.repopulatePersistantCriteria(persistCrit, null, newQuery);
                    }
                }

                // Setup date criteria here for new results
                CurationStatusDateCriteria csd = new CurationStatusDateCriteria();
                csd.setCurationStatusDate(dto.getExecutionTime());
                newQuery.setCriteria(csd);

                try {
                    // Add authorization criteria here
                    AuthorizationManager am = new AuthorizationManager(dto.getUserId());
                    am.authorizeCollections(newQuery);
                    am.authorizeSitesAndSSGs(newQuery);

                    LookupManager lookupMgr = LookupManagerFactory.createLookupManager(am.getAuthorizedCollections());
                    boolean newResults = doResultsExist(newQuery,
                    		                            lookupMgr.getSearchableNodes().keySet());

                    if (newResults) {
                        qsm.addNewResultsForQuery(newQuery.getSavedQueryId());
                    }
                }
                catch (Exception e) {
                	System.out.println("Problem re-executing query");
                	e.printStackTrace();
                }
            }
        }
    }


    private static boolean doResultsExist(DICOMQuery newQuery,
    		                              Set<NBIANode> searchableNodes) {

        PatientSearcherService patientSeacherService =
        	PatientSearcherServiceFactory.getPatientSearcherService();

        List<NBIANode> selectedNodes = new ArrayList<NBIANode>();

        NodeCriteria nodeCriteria = newQuery.getNodeCriteria();

        if(nodeCriteria!=null) {
	        List<String> nodeUrls = nodeCriteria.getRemoteNodes();
	    	for(NBIANode searchableNode : searchableNodes) {
	    		if(nodeUrls.contains(searchableNode.getURL())) {
	    			selectedNodes.add(searchableNode);
	    		}
	    	}
        }

        if(selectedNodes.isEmpty()) {
        	selectedNodes.add(LocalNode.getLocalNode());
        }

        PatientSearchCompletionService results = patientSeacherService.searchForPatients(selectedNodes,
        		                                                                         newQuery);

		try {
			for(int i=0;i<results.getNodesToSearch().size();i++) {
				//this is a blocking call
				Future<PatientSearchResults> future = results.getCompletionService().take();
				//this is a blocking call

				PatientSearchResults result = future.get();
				//result.getResults will be null if there was an error in issuing query
				if(result.getResults()!=null && result.getResults().length>0) {
					return true;
				}
			}
		}
		catch(Exception ex) {
			//shouldnt get here, the search result service should capture
			//any exceptions and results a search result that indicates
			//there was an error
			ex.printStackTrace();
		}
		return false;
    }
}
