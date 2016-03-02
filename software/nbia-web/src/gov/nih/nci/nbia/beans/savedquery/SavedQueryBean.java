/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.savedquery;
import gov.nih.nci.ncia.criteria.Criteria;
import gov.nih.nci.ncia.criteria.CurationStatusDateCriteria;
import gov.nih.nci.ncia.criteria.PersistentCriteria;
import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.searchform.SearchWorkflowBean;
import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.datamodel.SortSavedQueryModel;
import gov.nih.nci.nbia.dto.AbstractStoredQueryDTO;
import gov.nih.nci.nbia.dto.QueryHistoryDTO;
import gov.nih.nci.nbia.dto.SavedQueryDTO;
import gov.nih.nci.nbia.newresults.NewResultsFlagUpdater;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.querystorage.QueryStorageManager;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.SavedQueryReconstructor;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIData;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.apache.log4j.Logger;


/**
 * This session scope bean will contain all of the user's saved queries.
 *
 * @author shinohaa
 *
 */
public class SavedQueryBean {
	private static final String QUERY_STORAGE_MANAGER = "queryStorageManager";
    /**
     * Logger for the class.
     */
    private static Logger logger = Logger.getLogger(SavedQueryBean.class);

    /**
     * All of the saved queries for the particular user.
     */
    private DataModel savedQueries;

    /**
     * Holds the count of saved queries if the queries have not been initialized
     */
    private Integer savedQueryCount;

    /**
     * The query history for a user.
     */
    private ListDataModel queryHistory;

    /**
     * Flag to tell the UI if the user is looking at the history or saved
     * queries.
     */
    private boolean historyMode;

	/**
	 * Flag to tell the UI if it is invoked as an admin tool.
	 */
    private boolean adminMode = false;

    /**
     * Binding to the saved query table
     */
    private UIData queryData;

    /**
     * Binding to the query history table.
     */
    private UIData queryHistoryData;

    /**
     * new results updater
     *
     */
    private NewResultsFlagUpdater flagUpdater;

    public SavedQueryBean() {
    }

    public DataModel getSavedQueries() {
        return savedQueries;
    }

    /**
     * Puts the saved queries into a wrapper that allows for sorting the queries
     *
     * @param savedQueries
     */
    public void setSavedQueries(List savedQueries) {
        this.savedQueries = new SortSavedQueryModel(new ListDataModel(
                    savedQueries));
    }

    /**
     * Returns the number of saved queries for a user.
     */
    public int getSavedQueryCount() {
        // If the saved queries have not been loaded yet, get the count.
        if (savedQueryCount == null) {
            updateSavedQueryCount();
        } else if (savedQueries != null) {
            // Otherwise return the count
            savedQueryCount = savedQueries.getRowCount();
        }

        return savedQueryCount;
    }

    /**
     * This will update the number of saved queries.  Used so that the count is
     * not retrieved on every page.
     *
     */
    public void updateSavedQueryCount() {
        // If the saved queries have not been loaded yet, get the count.
        if (savedQueries == null) {
            SecurityBean secure = BeanManager.getSecurityBean();

            try {
            	QueryStorageManager qsm = (QueryStorageManager)SpringApplicationContext.getBean(QUERY_STORAGE_MANAGER);
                savedQueryCount = qsm.getSavedQueryCount(secure.getUsername());
            } catch (Exception e) {
                logger.error("could not get saved query count", e);
                savedQueryCount = 0;
            }
        } else {
            loadSavedQueries();
        }
    }

    /**
     * Action to perform when a user wants to view the saved queries. It calls
     * the QueryStorageManager to load the saved queries after checking to see
     * the user is logged in.
     */
    public String viewSavedQueries() {
        historyMode = false;
        adminMode = false;

        SecurityBean secure = BeanManager.getSecurityBean();

        if (secure.getLoggedIn()) {
            loadSavedQueries();

            return "displaySavedQueries";
        } else {
            MessageUtil.addErrorMessage("MAINbody:loginForm:pass",
                "securitySavedQuery");

            return "loginFail";
        }
    }

    /**
     * Action to perform when a user wants to view the query history. It calls
     * the QueryStorageManager to load the query history after checking to see
     * the user is logged in.
     */
    public String viewQueryHistory() {
        historyMode = true;
        adminMode=false;

        SecurityBean secure = BeanManager.getSecurityBean();

        if (secure.getLoggedIn()) {
            loadQueryHistory();

            return "displaySavedQueries";
        } else {
            MessageUtil.addErrorMessage("MAINbody:loginForm:pass",
                "securitySavedQuery");

            return "loginFail";
        }
    }
    /**
     * Action to perform when an admin wants to manage the saved query history. It calls
     * the QueryStorageManager to load the saved query list
     */
    public String manageSavedQueries() {
        adminMode = true;
        historyMode = false;

        SecurityBean secure = BeanManager.getSecurityBean();

        if (secure.getLoggedIn() && secure.getHasSuperCuratorRole()) {
            loadAllSavedQueries();

            return "displaySavedQueries";
        } else {
            MessageUtil.addErrorMessage("MAINbody:loginForm:pass",
                "securitySavedQuery");

            return "loginFail";
        }
    }

    /**
     * Reloads the saved queries. Is public so that when a query is stored, the
     * count can be updated from another bean.
     *
     */
    private void loadSavedQueries() {
        SecurityBean secure = BeanManager.getSecurityBean();
        String uName = secure.getUsername();
        QueryStorageManager qManager = (QueryStorageManager)SpringApplicationContext.getBean(QUERY_STORAGE_MANAGER);

        try {
            setSavedQueries(qManager.retrieveSavedQueries(uName));
            Collections.sort((List) savedQueries.getWrappedData(),
                new SavedQueryComparator());
        } catch (Exception e) {
            logger.error("Problem storing the query", e);

            // TODO: Add Error message to JSF page
        }
    }

        /**
	     * Reloads all saved queries. Is public so that when a query is stored, the
	     * count can be updated from another bean.
	     *
	     */
	    private void loadAllSavedQueries() {
	        QueryStorageManager qManager = (QueryStorageManager)SpringApplicationContext.getBean(QUERY_STORAGE_MANAGER);

	        try {
	            setSavedQueries(qManager.retrieveAllSavedQueries());
	            Collections.sort((List) savedQueries.getWrappedData(),
	                new SavedQueryComparator());
	        } catch (Exception e) {
	            logger.error("Problem storing the query", e);

	            // TODO: Add Error message to JSF page
	        }
	    }


    /**
     * Reloads the query history.
     *
     */
    private void loadQueryHistory() {
        SecurityBean secure = BeanManager.getSecurityBean();
        String uName = secure.getUsername();
        QueryStorageManager qManager = (QueryStorageManager)SpringApplicationContext.getBean(QUERY_STORAGE_MANAGER);

        try {
            setQueryHistory(qManager.retrieveQueryHistory(uName));
            Collections.sort((List) queryHistory.getWrappedData(),
                new QueryHistoryComparator());
        } catch (Exception e) {
            logger.error("Problem storing the query", e);

            // TODO: Add Error message to JSF page
        }
    }

    /**
     * Switches the view between saved queries and query history.
     */
    public String switchView() {
        historyMode = !historyMode;

        if (historyMode) {
            loadQueryHistory();
        } else {
            loadSavedQueries();
        }

        return null;
    }

    public UIData getQueryData() {
        return queryData;
    }

    public void setQueryData(UIData queryData) {
        this.queryData = queryData;
    }

    public int getCurrRow() {
        return savedQueries.getRowIndex();
    }

    /**
     * Toggles whether or not to show the criteria for a seleced query.
     */
    public String showCriteria() {
        AbstractStoredQueryDTO dto = (AbstractStoredQueryDTO) queryData.getRowData();
        dto.setShowCriteria(!dto.isShowCriteria());

        return null;
    }

    /**
     * Toggles whether or not to show the criteria for a seleced query.
     */
    public String showHistoryCriteria() {
        AbstractStoredQueryDTO dto = (AbstractStoredQueryDTO) queryHistoryData.getRowData();
        dto.setShowCriteria(!dto.isShowCriteria());

        return null;
    }

    /**
     * Action to be performed when requbmitting a query. It repopulates the
     * search screen with the criteria values and then performs the search.
     */
    public String resubmitQuery() {
        AbstractStoredQueryDTO dto = null;

        if (historyMode) {
            dto = (AbstractStoredQueryDTO) queryHistoryData.getRowData();
        } else {
            dto = (AbstractStoredQueryDTO) queryData.getRowData();
        }

        DICOMQuery newQuery = repopulateSearch(dto);

        if (!dto.getQueryName().equals(MessageUtil.getString("noNameProvided"))) {
            updateNewResultsFlag(newQuery, dto.getExecutionTime());
        }

        SearchWorkflowBean swb = BeanManager.getSearchWorkflowBean();
        swb.setToggleQuery(false);
   /*     try {
            swb.asynchronousQuery(repopulateSearch(dto));
        } catch (Exception e) {
            e.printStackTrace();
        }
	*/
        MessageUtil.addInfoMessage("MAINbody:searchMenuForm:saveQueryView:queryName",
            "resubmitQuery", new Object[] { swb.getQuery().getQueryName() });

        return "search";
    }

    /**
     * Performs the action to edit a query. Repopulates the search values from
     * the criteria and then takes the user to the search page.
     *
     * @throws Exception
     */
    public String editQuery() throws Exception {
        SavedQueryDTO dto = null;

        dto = (SavedQueryDTO) queryData.getRowData();

        SearchWorkflowBean swb = BeanManager.getSearchWorkflowBean();
        swb.setToggleQuery(false);

        DICOMQuery newQuery = repopulateSearch(dto);
        swb.setEditingSavedQuery(true);

       	updateNewResultsFlag(newQuery, dto.getExecutionTime());

        MessageUtil.addInfoMessage("MAINbody:searchMenuForm:saveQueryView:editQueryMessage",
            "editingSavedQuery", new Object[] { swb.getQuery().getQueryName() });

        return "search";
    }

    /**
     * This performs the heavy lifting for repopulation of the search page.
     *
     * @param dto
     */
    private DICOMQuery repopulateSearch(AbstractStoredQueryDTO dto) {
        List<Criteria> kriteria = dto.getCriteriaList();
        DICOMQuery newQuery =  new DICOMQuery();

        if (!dto.getQueryName().equals("")) {
            newQuery.setQueryName(dto.getQueryName());
        }

        if (dto instanceof SavedQueryDTO) {
            newQuery.setSavedQueryId(dto.getId());
        } else if (dto instanceof QueryHistoryDTO) {
            newQuery.setSavedQueryId(((QueryHistoryDTO) dto).getSavedQueryId());
        }

        SearchWorkflowBean swb = (SearchWorkflowBean) BeanManager.getSearchWorkflowBean();
        swb.setEditSavedQuery(true);
        swb.newSimpleSearch();


        // Only repopulates search fields for persistent criteria because other
        // criteria have dependent values.
        for (Criteria kritItem : kriteria) {
             if (kritItem instanceof PersistentCriteria) {
                PersistentCriteria persistCrit = (PersistentCriteria) kritItem;
                SavedQueryReconstructor.repopulatePersistantCriteria(persistCrit, swb, newQuery);
            }
        }
        swb.setQuery(newQuery);
        swb.updateTree();
        try{
        	swb.submitSearch();
        } catch (Exception e) {
            logger.error("Error submitting query", e);
            e.printStackTrace();
        }
        return newQuery;
    }

    private Date updateNewResultsFlag(DICOMQuery newQuery, Date startDate) {
        // Put the flag updater in the session so that the user has access to
        // the
        // new data throughout their session.
        if (flagUpdater == null) {
            flagUpdater = new NewResultsFlagUpdater();
        }

        Date lastExecDate = flagUpdater.getLastExecuteTimeBeforeThisSession(newQuery.getSavedQueryId());

        if (lastExecDate == null) {
            flagUpdater.addSavedQuery(newQuery.getSavedQueryId(), startDate);
        } else {
            startDate = lastExecDate;
        }

        return startDate;
    }

    /**
     * Performs a search that only brings back new data for a particular query.
     */
    public String searchNewData() {
        SavedQueryDTO dto = null;
        dto = (SavedQueryDTO) queryData.getRowData();

        DICOMQuery newQuery = repopulateSearch(dto);
        CurationStatusDateCriteria csd = new CurationStatusDateCriteria();
        Date startDate = dto.getExecutionTime();

        startDate = updateNewResultsFlag(newQuery, startDate);


        csd.setCurationStatusDate(startDate);
        newQuery.setCriteria(csd);

        // Perform search
        SearchWorkflowBean swb = BeanManager.getSearchWorkflowBean();

        try {
            swb.asynchronousQuery(newQuery);
        } catch (Exception e) {
            logger.error("Error resubmitting query", e);
        }

        MessageUtil.addInfoMessage("MAINbody:searchMenuForm:saveQueryView:newDataQuery",
            "newDataQuery",
            new Object[] { swb.getQuery().getQueryName(), startDate });

        return "submitSearch";
    }

    /**
     * Deletes the selected saved queries.
     *
     */
    public void deleteQueries() {
        boolean queryDeleted = false;
        List<SavedQueryDTO> temp = new ArrayList<SavedQueryDTO>();

        for (SavedQueryDTO dto : (List<SavedQueryDTO>) savedQueries.getWrappedData()) {
            if (dto.isDelete()) {
                temp.add(dto);
                queryDeleted = true;
            }
        }

        QueryStorageManager man = (QueryStorageManager)SpringApplicationContext.getBean(QUERY_STORAGE_MANAGER);

        try {
            man.deleteQueries(temp);
        } catch (Exception e) {
            logger.error("Problem deleting queries", e);

            MessageUtil.addErrorMessage("MAINbody:queryForm:queryMessage",
                "errorQueryDelete");
        }

        // Put messages on the page to inform the user of what has occoured.
        if (queryDeleted) {
            MessageUtil.addErrorMessage("MAINbody:searchMenuForm:saveQueryView:queryMessage",
                "queriesDeleted");
        } else {
            MessageUtil.addErrorMessage("MAINbody:searchMenuForm:saveQueryView:queryMessage","noQueriesDeleted");
        }
		if (adminMode)
			loadAllSavedQueries();
	    else
        	loadSavedQueries();
    }

    public boolean isHistoryMode() {
        return historyMode;
    }

    public void setHistoryMode(boolean historyMode) {
        this.historyMode = historyMode;
    }

	public boolean isAdminMode() {
		return adminMode;
	}

	public void setAdminMode(boolean adminMode) {
		this.adminMode = adminMode;
    }

    public ListDataModel getQueryHistory() {
        return queryHistory;
    }

    public void setQueryHistory(List queryHistory) {
        this.queryHistory = new ListDataModel(queryHistory);
    }

    public UIData getQueryHistoryData() {
        return queryHistoryData;
    }

    public void setQueryHistoryData(UIData queryHistoryData) {
        this.queryHistoryData = queryHistoryData;
    }

    public int getQueryHistoryCount() {
        if (queryHistory == null) {
            return 0;
        }

        return queryHistory.getRowCount();
    }
}
