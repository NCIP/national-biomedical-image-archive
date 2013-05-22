/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchresults;

import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.savedquery.SavedQueryBean;
import gov.nih.nci.nbia.beans.searchform.SearchWorkflowBean;
import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.exception.DuplicateQueryException;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.querystorage.QueryStorageManager;
import gov.nih.nci.nbia.search.LocalNode;
import gov.nih.nci.nbia.search.PatientSearchCompletionService;
import gov.nih.nci.nbia.search.PatientSearchResults;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.NCIAConstants;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.ncia.search.NBIANode;
import gov.nih.nci.ncia.search.PatientSearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * This is a session scope bean that holds all of the patient search results and allows
 * the drill down functionality.
 */
public class SearchResultBean {


	public SearchResultBean() {
	}

    /**
     * Drill down to view the studies for the specified patient.
     * This is called from NodeTableWrapper for a given node.
     */
	public void viewPatient(PatientResultWrapper wrapper) throws Exception {
		patient = wrapper.getPatient();
		
		studiesSearchResultBean.viewPatient(patient);			
	}

	
	/**
	 * Each element in this list in a result from a node.  For example,
	 * local node, remote node1, remote node2, etc.  So each
	 * element in the list matches up with a "patient results table".
	 */
	public List<NodeTableWrapper> getNodeTablesWrappers() {
		return nodeTableWrappers;
	}
		
	
	/**
	 * Return jsut the first set of results.... this is only used by
	 * ISPY.  Everything else should iterate over the node table wrappers.
	 */
	public List<PatientResultWrapper> getPatientResults() {
		return nodeTableWrappers.get(0).getPatients();		
	}

	
    /**
     * For an asynchronous query, this method is really "setting the results".
     * The completion service is used to retrieve results for display once
     * they are finally ready.
     */
    public void setPatientSearchResultsCompletionService(PatientSearchCompletionService results) {
    	//setup the tables
    	int numNodes = results.getNodesToSearch().size();
    	
    	nodeTableWrappers = new ArrayList<NodeTableWrapper>(numNodes);
    	List<NBIANode> nodes = results.getNodesToSearch();
    	for(NBIANode node : nodes) {
    		NodeTableWrapper wrapper = new NodeTableWrapper(node, this);
    		nodeTableWrappers.add(wrapper);
    	}
    	    	    	   
    	this.waitForSearchResults(results);
    }
    
    
	/**
	 * Sets the patient results. This is really an important method....
	 * it is called by the bean that initiates the search.... it is
	 * set here so the results can ultimately be displayed.
	 * 
	 * <P>This is only for synchronous searches (ispy and dynamic search)
	 */
	public void setPatientResults(List<PatientSearchResult> results) {
		if (results != null) {
			List<PatientResultWrapper> patientResultsWrapperList = new ArrayList<PatientResultWrapper>();
			for(PatientSearchResult result : results) {
				patientResultsWrapperList.add(new PatientResultWrapper(result));
			}

			nodeTableWrappers = new ArrayList<NodeTableWrapper>(1);
			NodeTableWrapper nodeTableWrapper = new NodeTableWrapper(LocalNode.getLocalNode(), this);
			PatientSearchResults psr = new PatientSearchResults(LocalNode.getLocalNode(),
					                                            results.toArray(new PatientSearchResult[]{}));
			nodeTableWrapper.setPatientSearchResults(psr);
			nodeTableWrappers.add(nodeTableWrapper);
		} 
		else {
			nodeTableWrappers = null;
		}
	}
	

	/**
	 * The currently selected patient as a result of executing
	 * viewPatient (drilling down on a patient to see its studies).
	 */
	public PatientSearchResult getPatient() {
		return patient;
	}

	
	/**
	 * After a patient is selected..... this tells whether the patient is
	 * on the local box.  Don't invoke this before viewPatient(xxx).
	 */
	public boolean isLocal() {
		boolean showCedara = patient.associatedLocation().isLocal();
		String installationSite = NCIAConfig.getInstallationSite();
		if (installationSite.equalsIgnoreCase(NCIAConstants.INSTALLATION_SITE) &&
				showCedara == true)
		{
			showCedara = true;
		}
		else
		{
			showCedara = false;
		}
	
		return showCedara;
	}

	
	/**
	 * The query from "classic search" that is responsible for the current
	 * results.
	 */
	public DICOMQuery getQuery() {
		return query;
	}

	
	/**
	 * Sets the query from "classic search" that is responsible for the current
	 * results.
	 */
	public void setQuery(DICOMQuery query) {
		this.query = query;
	}


	/**
	 * Saves the query with the desired name
	 */
	public String saveQuery() {
		logger.debug("query name is: " + queryName);

		SearchWorkflowBean swb = BeanManager.getSearchWorkflowBean();
		String queryNameText = "MAINbody:dataForm:saveQueryView:queryName";

		// Check to see if the query is being edited, if so, this will be
		// performed
		// when the user wants to update the query
		if (swb.isEditingSavedQuery()) {
			queryNameText = "MAINbody:dataForm:saveQueryView:newQueryName";

			if (updateQuery) {
				QueryStorageManager qManager = (QueryStorageManager)SpringApplicationContext.getBean("queryStorageManager");
				long newQueryId;

				try {
					newQueryId = qManager.updateQuery(query);
					query.setSavedQueryId(newQueryId);
					MessageUtil.addInfoMessage(queryNameText,
							                   "queryUpdated",
							                   new Object[] { query.getQueryName() });
					swb.setEditingSavedQuery(false);
					queryName = "";
				} catch (Exception e) {
					logger.error("Problem updating the query", e);

					MessageUtil.addErrorMessage(queryNameText,
							"queryErrorUpdating");
				}

				return null;
			}
		}

		// This will be performed when the user is either saving a new query
		// or saving a current query with a different name.
		if (!swb.isEditingSavedQuery() || !updateQuery) {
			if ((queryName == null) || queryName.equals("")) {
				MessageUtil.addErrorMessage(queryNameText, "queryBlankName");
			} else {
				SecurityBean secure = BeanManager.getSecurityBean();
				SavedQueryBean queryBean = BeanManager.getSavedQueryBean();
				String uName = secure.getUsername();
				String oldQueryName = query.getQueryName();
				query.setQueryName(queryName);
				query.setUserID(uName);

				QueryStorageManager qManager = (QueryStorageManager)SpringApplicationContext.getBean("queryStorageManager");
				long newQueryId;

				try {
					newQueryId = qManager.saveQuery(query);
					query.setSavedQueryId(newQueryId);

					queryBean.updateSavedQueryCount();
					MessageUtil.addInfoMessage(queryNameText, 
							                   "querySaved",
							                   new Object[] { query.getQueryName() });
					swb.setEditingSavedQuery(false);
					updateQuery = true;
				} catch (DuplicateQueryException dq) {
					MessageUtil.addErrorMessage(queryNameText, "queryExists");
					query.setQueryName(oldQueryName);
				} catch (Exception e) {
					logger.error("Problem storing the query", e);
					MessageUtil.addErrorMessage(queryNameText, "queryExists");
					query.setQueryName(oldQueryName);
				}
			}

			queryName = "";
		}

		return null;
	}


	public int getNumCriteria() {
		return query.getCriteriaList().size();
	}


	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getUpdateQuery() {
		return updateQuery.toString();
	}

	public void setUpdateQuery(String updateQuery) {
		this.updateQuery = Boolean.parseBoolean(updateQuery);
	}

	
	public Integer getResultsPerPage() {
		return resultsPerPage;
	}

	public void setResultsPerPage(Integer resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	//dep injection
	public StudiesSearchResultBean getStudySearchResultBean() {
		return studiesSearchResultBean;
	}


	public void setStudiesSearchResultBean(StudiesSearchResultBean studiesSearchResultBean) {
		this.studiesSearchResultBean = studiesSearchResultBean;
	}
	

	//////////////////////////////////////////PRIVATE//////////////////////////////////////////


    private Integer resultsPerPage=10;	

	private StudiesSearchResultBean studiesSearchResultBean;
	
	/**
	 * Logger for the class.
	 */
	private static Logger logger = Logger.getLogger(SearchResultBean.class);

	/**
	 * Holds the name of the query to be saved
	 */
	private String queryName;

	/**
	 * Holds the original query for display.
	 */
	private DICOMQuery query;


	/**
	 * Holds the currently selected patient, series and study.
	 */
	private PatientSearchResult patient;


	/**
	 * String of whether or not to update the query
	 */
	private Boolean updateQuery = true;


	/**
     * Each object in this collection represents the gui state for the search
     * results from a given node.  so two nodes search means two elements in this colleciton.
     */
	private List<NodeTableWrapper> nodeTableWrappers = new ArrayList<NodeTableWrapper>();
	
	
	private void addNodeResult(PatientSearchResults patientSearchResults) {

		NodeTableWrapper foundWrapper = null;
		
		for(NodeTableWrapper wrapperIter : nodeTableWrappers) {	
			if(wrapperIter.getNBIANode().getURL().equals(patientSearchResults.getNode().getURL())) {
				foundWrapper = wrapperIter;
				break;
			}
		}

		foundWrapper.setPatientSearchResults(patientSearchResults);
		
	}
    
	/**
	 * All the search requests are sent out in parallel.... wait for all of them
	 * to be done.  This method can be called as-is from a waiter thread with push
	 * ...that is if the push can work more reliably.
	 */
	private void waitForSearchResults(PatientSearchCompletionService completionService) {
		try {    
			System.out.println("#################"+completionService.getNodesToSearch().size());
			List<NBIANode> noResponseNode = new ArrayList<NBIANode>();
			for (NBIANode node : completionService.getNodesToSearch()) {
				noResponseNode.add(node);
			}
			for(int i=0;i<completionService.getNodesToSearch().size();i++) {
				try {
					//this is a blocking call
					Future<PatientSearchResults> future = completionService.getCompletionService().poll(NCIAConfig.getTimeoutInMin(), TimeUnit.MINUTES);
					//	this is a blocking call
					PatientSearchResults result = null;
					if (future != null) {
						result = future.get();
						System.out.println("got response from node "+ result.getNode().getDisplayName() +" so remove it");
						noResponseNode.remove(result.getNode()); // got response so remove it
						logResult(result);
						addNodeResult(result);
					} 
				} catch (CancellationException e) { 
	                e.printStackTrace(); 
	            } 

				//pushToBrowser();
			}
			if(noResponseNode !=null && !noResponseNode.isEmpty()) {
				//	check is there are any node from where, no response came with configurable minutes.
				for (NBIANode node : noResponseNode) {
					System.out.println("no response Node" +node.getDisplayName());
					Exception searchError = new Exception("no response from node");
					PatientSearchResults result = new PatientSearchResults(node, searchError);
					logResult(result);
					addNodeResult(result);
				}
			}			
			
			System.out.println("done waiting for results");
		}
//		catch(InterruptedException ie) {
//			System.out.println("interrupted the async result waiter");
//		}
		catch(Exception ex) {
			//shouldnt get here, the search result service should capture
			//any exceptions and results a search result that indicates
			//there was an error
			ex.printStackTrace();
		}		
	}
	
 
    private static void logResult(PatientSearchResults result) {
		if(result.getResults()!=null) {
			System.out.println("PatientSearchResults num results:"+result.getResults().length + " - Node " + result.getNode().getDisplayName());
		}   
		if(result.getSearchError()!=null) {
			System.out.println("PatientSearchResults error:"+result.getSearchError() + " - Node " + result.getNode().getDisplayName());
		}   
    }
}
