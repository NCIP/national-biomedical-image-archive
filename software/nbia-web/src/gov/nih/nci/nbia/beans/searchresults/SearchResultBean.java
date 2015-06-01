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
import gov.nih.nci.nbia.dto.QcSearchResultDTO;
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
import java.util.Collections;
import java.util.Comparator;
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
		if (nodeTableWrappers == null ) {
			return new ArrayList<NodeTableWrapper>(1);
		} else {
			if (!oldSort.equals(sortColumnName) ||
	                oldAscending != ascending) {
	             sort();
	             oldSort = sortColumnName;
	             oldAscending = ascending;
	        }
			return nodeTableWrappers;
		}
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
		// set text to false so solr search does not interfere with anything else
		isTextResult=false;
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
		//Cedara is not supported since 6.0.  So it is always false.
		boolean showCedara = false;
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
		String queryNameText = "MAINbody:searchMenuForm:saveQueryView:queryName";

		// Check to see if the query is being edited, if so, this will be
		// performed
		// when the user wants to update the query
		if (swb.isEditingSavedQuery()) {
			queryNameText = "MAINbody:searchMenuForm:saveQueryView:newDataQuery";
			swb.setToggleQuery(false);
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
				swb.setToggleQuery(false);
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

	
	
	private boolean isTextResult=false;
	
    boolean isFirstTime = true;
    boolean isFirstTimeAdvanced = true;
    boolean isFirstTimeText = true;
    
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
    //for sorting
    private static final String collectionNameHeader = "Collection ID";
    private static final String patientHeader = "Subject ID";
    private static final String matchedStudyHeader = "Matched Studies";
    private static final String totalStudyHeader = "Total Studies";
    private static final String matchedSeriesHeader = "Matched Series";
    private static final String totalSeriesHeader = "Total Series";
    private static final String hitHeader = "Hit";

    private String sortColumnName= "Subject ID";
    private boolean ascending = true;
    // we only want to resort if the oder or column has changed.
    private String oldSort = sortColumnName;
    private boolean oldAscending = ascending;

   // ----------------
    /**
     * Gets the sortColumnName column.
     *
     * @return column to sortColumnName
     */
    public String getSortColumnName() {
        return sortColumnName;
    }

    /**
     * Sets the sortColumnName column
     *
     * @param sortColumnName column to sortColumnName
     */
    public void setSortColumnName(String sortColumnName) {
    	 oldSort = this.sortColumnName;
         this.sortColumnName = sortColumnName;

    }

    /**
     * Is the sortColumnName ascending.
     *
     * @return true if the ascending sortColumnName otherwise false.
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * Set sortColumnName type.
     *
     * @param ascending true for ascending sortColumnName, false for desending sortColumnName.
     */
    public void setAscending(boolean ascending) {
    	 oldAscending = this.ascending;
        this.ascending = ascending;
    }

    private void sort() {
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
            	PatientResultWrapper c1 = (PatientResultWrapper) o1;
            	PatientResultWrapper c2 = (PatientResultWrapper) o2;

                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(collectionNameHeader)) {
                		return compareObject(
                                c1.getPatient().getProject().compareTo(c2.getPatient().getProject()),
                                c2.getPatient().getProject().compareTo(c1.getPatient().getProject()));
                }
                else if (sortColumnName.equals(patientHeader)) {
                    return compareObject(c1.getPatient().getSubjectId().compareTo(c2.getPatient().getSubjectId()),
                        c2.getPatient().getSubjectId().compareTo(c1.getPatient().getSubjectId()));
                }
                else if (sortColumnName.equals(matchedStudyHeader)) {
                	return compareObject(c1.getStudyCounts().compareTo(c2.getStudyCounts()),
                		c2.getStudyCounts().compareTo(c1.getStudyCounts()));
                }
                else if (sortColumnName.equals(matchedSeriesHeader)) {
                	return compareObject(c1.getSeriesCounts().compareTo(c2.getSeriesCounts()),
                		c2.getSeriesCounts().compareTo(c1.getSeriesCounts()));
                }else if (sortColumnName.equals(totalStudyHeader)) {
                	return compareObject(c1.getPatient().getTotalNumberOfStudies().compareTo(c2.getPatient().getTotalNumberOfStudies()),
                		c2.getPatient().getTotalNumberOfStudies().compareTo(c1.getPatient().getTotalNumberOfStudies()));
                }else if (sortColumnName.equals(totalSeriesHeader)) {
                	return compareObject(c1.getPatient().getTotalNumberOfSeries().compareTo(c2.getPatient().getTotalNumberOfSeries()),
                		c2.getPatient().getTotalNumberOfSeries().compareTo(c1.getPatient().getTotalNumberOfSeries()));
               }else {
                	return 0;
                }
            }
        };
        Collections.sort(nodeTableWrappers.get(0).getPatients(), comparator);
    }

    private int compareObject(int result1, int result2){
    	return ascending ? result1 : result2;
    }

	public String getCollectionNameHeader() {
		return collectionNameHeader;
	}

	public String getPatientHeader() {
		return patientHeader;
	}

	public String getMatchedStudyHeader() {
		return matchedStudyHeader;
	}

	public String getTotalStudyHeader() {
		return totalStudyHeader;
	}

	public String getMatchedSeriesHeader() {
		return matchedSeriesHeader;
	}

	public String getTotalSeriesHeader() {
		return totalSeriesHeader;
	}

	public boolean isTextResult() {
		return isTextResult;
	}

	public void setTextResult(boolean isTextResult) {
		this.isTextResult = isTextResult;
	}


	public boolean isFirstTime() {
		return isFirstTime;
	}

	public void setFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	public boolean isFirstTimeAdvanced() {
		return isFirstTimeAdvanced;
	}

	public void setFirstTimeAdvanced(boolean isFirstTimeAdvanced) {
		this.isFirstTimeAdvanced = isFirstTimeAdvanced;
	}

	public boolean isFirstTimeText() {
		return isFirstTimeText;
	}

	public void setFirstTimeText(boolean isFirstTimeText) {
		this.isFirstTimeText = isFirstTimeText;
	}

	public String getHitHeader() {
		return hitHeader;
	}
	
}
