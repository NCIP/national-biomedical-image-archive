/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * $Id$ $Log:
 * SearchWorkflowBean.java,v $ Revision 1.90 2006/09/27 20:46:27 panq Reformated
 * with Sun Java Code Style and added a header for holding CVS history.
 */
/*
 * Created on Jun 13, 2005
 */
package gov.nih.nci.nbia.beans.searchform;

import gov.nih.nci.ncia.criteria.AnnotationOptionCriteria;
import gov.nih.nci.ncia.criteria.ColorModeOptionCriteria;
import gov.nih.nci.ncia.criteria.ContrastAgentCriteria;
import gov.nih.nci.ncia.criteria.NodeCriteria;
import gov.nih.nci.ncia.criteria.NumFrameOptionCriteria;
import gov.nih.nci.ncia.criteria.RangeData;
import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.DynamicSearchBean;
import gov.nih.nci.nbia.beans.DynamicSearchCriteriaBean;
import gov.nih.nci.nbia.beans.searchform.aim.AimSearchWorkflowBean;
import gov.nih.nci.nbia.beans.searchresults.SearchResultBean;
import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.customserieslist.FileGenerator;
import gov.nih.nci.nbia.dto.ModalityDescDTO;
import gov.nih.nci.nbia.dynamicsearch.DynamicSearchCriteria;
import gov.nih.nci.nbia.lookup.LookupManager;
import gov.nih.nci.nbia.lookup.LookupManagerFactory;
import gov.nih.nci.nbia.modalitydescription.ModalityDescProcessor;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.querystorage.QueryStorageManager;
import gov.nih.nci.nbia.search.LocalNode;
import gov.nih.nci.nbia.search.PatientSearchCompletionService;
import gov.nih.nci.nbia.search.PatientSearcher;
import gov.nih.nci.nbia.search.PatientSearcherService;
import gov.nih.nci.nbia.search.PatientSearcherServiceFactory;
import gov.nih.nci.nbia.util.DateValidator;
import gov.nih.nci.nbia.util.JsfUtil;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.util.StringUtil;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.UsAvailableSearchTerms;
import gov.nih.nci.ncia.search.NBIANode;
import gov.nih.nci.ncia.search.PatientSearchResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;

import com.icesoft.faces.context.ByteArrayResource;
import com.icesoft.faces.context.Resource;

/**
 * This is the Session scope bean that provides the search functionality on the
 * search page. It is maintained in the session in order to provide the
 * functionality of being able to go back and edit a search from the
 * breadcrumbs.
 *
 * @author shinohaa
 */
public class SearchWorkflowBean {



	/**
     * Constructor. Populates drop downs and sets default values.
     */
    public SearchWorkflowBean() {
        lookupBean = BeanManager.getSearchLookupBean();

        // initialize range criteria
        rcdSearchBean = new ReconstructionDiameterSearchBean();
        dxDcdSearchBean = new DxDataCollectionDiameterSearchBean();
        dcdSearchBean = new DataCollectionDiameterSearchBean();
        amSearchBean = new AcquisitionMatrixSearchBean();
    }

    /**
     * Called after the user is looged in.  This callback is important
     * because at this point we know what collections the user is authorized
     * to mess with.
     */
    public void loggedIn() throws Exception {
    	SecurityBean securityBean = BeanManager.getSecurityBean();
    	List<String> authorizedCollections = securityBean.getAuthorizedCollections();
    	lookupMgr = LookupManagerFactory.createLookupManager(authorizedCollections);

    	List<String> collectionNames = lookupMgr.getSearchCollection();
    	Collections.sort(collectionNames);
        collectionItems = JsfUtil.getBooleanSelectItemsFromStrings(collectionNames);

        populateModalityDescMap();

        List<String> modalities = lookupMgr.getModality();
    	Collections.sort(modalities);
    	modalityItems = JsfUtil.constructModalitySelectItems(modalities, modalityDescMap);

        List<String> anatomicSites = lookupMgr.getAnatomicSite();
        Collections.sort(anatomicSites);
        anatomicalSiteItems = JsfUtil.getBooleanSelectItemsFromStrings(anatomicSites);

        List<String> usMultiModalities = lookupMgr.getUsMultiModality();
        Collections.sort(usMultiModalities);
        usMultiModalityItems = JsfUtil.getBooleanSelectItemsFromStrings(usMultiModalities);

        List<String> kernels = lookupMgr.getDICOMKernelType();
        Collections.sort(kernels);
        kernelItems = JsfUtil.getBooleanSelectItemsFromStrings(kernels);

        searchableNodeMap = lookupMgr.getSearchableNodes();
        usSearchableNodeMap = lookupMgr.getSearchableNodesForUs();

        searchableNodes = mergeNodeList(searchableNodeMap.keySet(), usSearchableNodeMap.keySet());

        Collections.sort(searchableNodes);
        remoteNodeItems = JsfUtil.constructNodeSelectItems(searchableNodes);
        this.aimSearchWorkflowBean.loggedIn();

        setDefaultValues();
    }

    private ArrayList<NBIANode> mergeNodeList(Set<NBIANode> set1, Set<NBIANode> set2) {
    	ArrayList<NBIANode> nodeList = new ArrayList<NBIANode>(set1);
    	for (NBIANode aNode: set2){
    		if (!nodeList.contains(aNode)){
    			nodeList.add(aNode);
    		}
    	}
    	return nodeList;
    }



    public LookupManager getLookupMgr() {
		return lookupMgr;
	}

	public void setLookupMgr(LookupManager lookupMgr) {
		this.lookupMgr = lookupMgr;
	}

    public boolean getShowThickness() {
        return showThickness;
    }

    public void setShowThickness(boolean showThickness) {
        this.showThickness = showThickness;
    }

    //////////////////////////////////BEGIN REMOTE SEARCH ITEMS//////////////////////
    public int getNumNodes() {
    	return remoteNodeItems.size();
    }

    public List<SelectItem> getRemoteNodeItems() {
        return remoteNodeItems;
    }


    public String selectAllRemoteNodes() {
    	for(SelectItem selectItem : remoteNodeItems) {
    		selectItem.setValue(true);
    	}
    	return null;
    }

    public String unselectAllRemoteNodes() {
    	for(SelectItem selectItem : remoteNodeItems) {
    		selectItem.setValue(false);
    	}
    	return null;
    }


    //called by saved query
    public void selectRemoteNodes(Collection<String> remoteNodeUrls) {
    	for(String url : remoteNodeUrls) {
    		if(url.equals(LocalNode.getLocalNode().getURL())) {
    			selectLocalNode();
    		}
    		else {
	    		SelectItem item = JsfUtil.findSelectItemByDescription(remoteNodeItems, url);
	    		if(item!=null) {
	    			item.setValue(true);
	    		}
    		}
    	}
    }

    //called by saved query
    private void selectLocalNode() {
    	for(int i=0;i<searchableNodes.size();i++) {
    		NBIANode node = searchableNodes.get(i);

    		if(node.isLocal()) {
    			remoteNodeItems.get(i).setValue(true);
    		}
    	}
    }

    public List<String> getSelectedRemoteNodes() {
    	List<String> selectedRemoteNodes = new ArrayList<String>();
    	for(SelectItem item : remoteNodeItems) {
    		if(item.getValue().equals(true)) {
    			selectedRemoteNodes.add(item.getLabel());
    		}
    	}
    	return selectedRemoteNodes;
    }



    public List<NBIANode> getSelectedNodes() {
    	List<NBIANode> selectedRemoteNodes = new ArrayList<NBIANode>();
    	for(int i=0;i<remoteNodeItems.size();i++) {
    		if(remoteNodeItems.get(i).getValue().equals(true)) {
    			selectedRemoteNodes.add(this.searchableNodes.get(i));
    		}
    	}
    	return selectedRemoteNodes;
    }
    ///////////////////////////////////END REMOTE SEARCH ITEMS////////////////////

    //////////////////////////////////BEGIN COLLECTION ITEMS//////////////////////
    public List<SelectItem> getCollectionItems() {
        return collectionItems;
    }

    public String selectAllCollections() {
    	for(SelectItem selectItem : collectionItems) {
    		selectItem.setValue(true);
    	}
    	return null;
    }

    public String unselectAllCollections() {
    	for(SelectItem selectItem : collectionItems) {
    		selectItem.setValue(false);
    	}
    	return null;
    }

    //called by saved query
    public void selectCollectionNames(Collection<String> selectedCollectionNames) {
    	for(String collectionName : selectedCollectionNames) {
    		SelectItem item = JsfUtil.findSelectItemByLabel(collectionItems, collectionName);
    		if(item!=null) {
    			item.setValue(true);
    		}
    	}
    }

    public List<String> getSelectedCollectionNames() {
    	List<String> selectedCollectionNames = new ArrayList<String>();
    	for(SelectItem item : collectionItems) {
    		if(item.getValue().equals(true)) {
    			selectedCollectionNames.add(item.getLabel());
    		}
    	}
    	return selectedCollectionNames;
    }
    ///////////////////////////////////END COLLECTION ITEMS////////////////////

    //////////////////////////////////BEGIN MODALITY ITEMS//////////////////////
    public List<SelectItem> getModalityItems() {
        return modalityItems;
    }

    public String selectAllModalities() {
     	for(SelectItem selectItem : modalityItems) {
    		if(selectItem.getLabel().equalsIgnoreCase("US")){
    			usSearch=true;
    		}
    		selectItem.setValue(true);
    	}
    	return null;
    }

    public String unselectAllModalities() {
    	for(SelectItem selectItem : modalityItems) {
    		selectItem.setValue(false);
    	}
    	usSearch = false;
    	return null;
    }

    //called by saved query
    public void selectModalityNames(Collection<String> selectedModalityNames) {
    	for(String modalityName : selectedModalityNames) {
    		SelectItem item = JsfUtil.findSelectItemByLabel(modalityItems, modalityName);
    		if(item!=null) {
    			item.setValue(true);
    		}
    		if (modalityName.equalsIgnoreCase("US")){
    			usSearch=true;
    		}
    	}
    }

    public List<String> getSelectedModalityNames() {
    	List<String> selectedModalityNames = new ArrayList<String>();
    	for(SelectItem item : modalityItems) {
    		if(item.getValue().equals(true)) {
    			selectedModalityNames.add(item.getLabel());
    		}
    	}
    	return selectedModalityNames;
    }
    ///////////////////////////////////END MODALITY ITEMS////////////////////

    //////////////////////////////////BEGIN ANATOMICAL SITE ITEMS//////////////////////
    public List<SelectItem> getAnatomicalSiteItems() {
        return anatomicalSiteItems;
    }

    public String selectAllAnatomicalSites() {
    	for(SelectItem selectItem : anatomicalSiteItems) {
    		selectItem.setValue(true);
    	}
    	return null;
    }

    public String unselectAllAnatomicalSites() {
    	for(SelectItem selectItem : anatomicalSiteItems) {
    		selectItem.setValue(false);
    	}
    	return null;
    }


    //called by saved query
    public void selectAnatomicalSiteNames(Collection<String> selectedAnatomicalSiteNames) {
    	for(String anatomicalSiteName : selectedAnatomicalSiteNames) {
    		SelectItem item = JsfUtil.findSelectItemByLabel(anatomicalSiteItems, anatomicalSiteName);
    		if(item!=null) {
    			item.setValue(true);
    		}
    	}
    }

    public List<String> getSelectedAnatomicalSiteNames() {
    	List<String> selectedAnatomicalSiteNames = new ArrayList<String>();
    	for(SelectItem item : anatomicalSiteItems) {
    		if(item.getValue().equals(true)) {
    			selectedAnatomicalSiteNames.add(item.getLabel());
    		}
    	}
    	return selectedAnatomicalSiteNames;
    }

    ///////////////////////////////////END ANATOMICAL SITE ITEMS////////////////////

    //////////////////////////////////BEGIN Image Type ITEMS//////////////////////
    public List<SelectItem> getUsMultiModalityItems() {
        return usMultiModalityItems;
    }

    public String selectAllUsMultiModalityItems() {
    	for(SelectItem selectItem : usMultiModalityItems) {
    		selectItem.setValue(true);
    	}
    	return null;
    }

    public String unselectAllUsMultiModalityItems() {
    	for(SelectItem selectItem : usMultiModalityItems) {
    		selectItem.setValue(false);
    	}

    	return null;
    }


    //called by saved query
    public void selectUsMultiModalityNames(Collection<String> selectedUsMultiModalityNames) {
    	for(String usMultiModalityName : selectedUsMultiModalityNames) {
    		SelectItem item = JsfUtil.findSelectItemByLabel(usMultiModalityItems, usMultiModalityName);
    		if(item!=null) {
    			item.setValue(true);
    		}
    	}
    }

    public List<String> getSelectedUsMultiModalityNames() {
    	List<String> selectedUsMultiModalityNames = new ArrayList<String>();
    	for(SelectItem item : usMultiModalityItems) {
    		if(item.getValue().equals(true)) {
    			selectedUsMultiModalityNames.add(item.getLabel());
    		}
    	}
    	return selectedUsMultiModalityNames;
    }
    ///////////////////////////////////END ANATOMICAL SITE ITEMS////////////////////

    //////////////////////////////////BEGIN KERNEL ITEMS//////////////////////
    public List<SelectItem> getKernelItems() {
        return kernelItems;
    }

    public void setKernelItems(List<SelectItem> kernelItems) {
        this.kernelItems = kernelItems;
    }


    public String selectAllKernels() {
    	for(SelectItem selectItem : kernelItems) {
    		selectItem.setValue(true);
    	}
    	return null;
    }

    public String unselectAllKernels() {
    	for(SelectItem selectItem : kernelItems) {
    		selectItem.setValue(false);
    	}
    	return null;
    }


    //called by saved query
    public void selectKernelNames(Collection<String> selectedKernelNames) {
    	for(String kernelName : selectedKernelNames) {
    		SelectItem item = JsfUtil.findSelectItemByLabel(kernelItems, kernelName);
    		if(item!=null) {
    			item.setValue(true);
    		}
    	}
    }

    public List<String> getSelectedKernelNames() {
    	List<String> selectedKernelNames = new ArrayList<String>();
    	for(SelectItem item : kernelItems) {
    		if(item.getValue().equals(true)) {
    			selectedKernelNames.add(item.getLabel());
    		}
    	}
    	return selectedKernelNames;
    }
    ///////////////////////////////////END KERNEL ITEMS////////////////////

    public String getKvRightCompare() {
        return kvRightCompare;
    }

    public void setKvRightCompare(String kvRightCompare) {
        this.kvRightCompare = kvRightCompare;
    }

    public String getKvLeftCompare() {
        return kvLeftCompare;
    }

    public void setKvLeftCompare(String kvLeftCompare) {
        this.kvLeftCompare = kvLeftCompare;
    }

    public String getThicknessLeftCompare() {
        return thicknessLeftCompare;
    }

    public void setThicknessLeftCompare(String imageLeftCompare) {
        this.thicknessLeftCompare = imageLeftCompare;
    }

    public String getThicknessRightCompare() {
        return thicknessRightCompare;
    }

    public void setThicknessRightCompare(String imageRightCompare) {
        this.thicknessRightCompare = imageRightCompare;
    }

    public String getImageThicknessRight() {
        return imageThicknessRight;
    }

    public void setImageThicknessRight(String imgThicknessRight) {
        this.imageThicknessRight = imgThicknessRight;
    }

    public String getImageThicknessLeft() {
        return imageThicknessLeft;
    }

    public void setImageThicknessLeft(String imgThicknessLeft) {
        this.imageThicknessLeft = imgThicknessLeft;
    }

    public String getKvPeakLeft() {
        return kvPeakLeft;
    }

    public void setKvPeakLeft(String kvPeakLeft) {
        this.kvPeakLeft = kvPeakLeft;
    }

    public String getKvPeakRight() {
        return kvPeakRight;
    }

    public void setKvPeakRight(String kvPeakRight) {
        this.kvPeakRight = kvPeakRight;
    }

    public String getMonthCompare() {
        return monthCompare;
    }

    public void setMonthCompare(String monthCompare) {
        this.monthCompare = monthCompare;
    }

    public String getNumberMonths() {
        return numberMonths;
    }

    public void setNumberMonths(String numberMonths) {
        this.numberMonths = numberMonths;
    }

    public String getNumberStudies() {
        return numberStudies;
    }

    public void setNumberStudies(String numberStudies) {
        this.numberStudies = numberStudies;
    }


    public String getSeriesDescription() {
        return seriesDescription;
    }

    public void setSeriesDescription(String seriesDescription) {
        this.seriesDescription = seriesDescription;
    }

    public String getModalityAndedSearch() {
        return modalityAndedSearch;
    }

    public void setModalityAndedSearch(String modalityAndedSearch) {
        this.modalityAndedSearch = modalityAndedSearch;
    }

    public String[] getContrastAgents() {
        return contrastAgents;
    }

    public void setContrastAgents(String[] theContrastAgents) {
        this.contrastAgents = theContrastAgents;
    }


    public boolean getAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    public boolean getUsSearch() {
        return usSearch;
    }

    public void setUsSearch(boolean usSearch) {
        this.usSearch = usSearch;
    }

    public boolean isSimpleSearch() {
		return simpleSearch;
	}

	public void setSimpleSearch(boolean simpleSearch) {
		this.simpleSearch = simpleSearch;
	}



    public boolean isPatientCriteria() {
		return patientCriteria;
	}

	public void setPatientCriteria(boolean patientCriteria) {
		this.patientCriteria = patientCriteria;
	}



	public boolean isDateCriteria() {
		return dateCriteria;
	}

	public void setDateCriteria(boolean dateCriteria) {
		this.dateCriteria = dateCriteria;
	}

	/**
     * This action is performed when the search button is clicked.
     *
     * @throws Exception
     */
    public String submitSearch() throws Exception {
        String result = validateBeforeSearch();
    	SearchResultBean srb = BeanManager.getSearchResultBean();
    	logger.debug("***** setting false for simple search *********");
    	srb.setFirstTime(false);
        if(result!=null) {
            return null;
        }

    	// Attempt to build the query for submission
        if (!buildQuery()) {
            // If there is a validation error, stay on the
            // same page
        	srb = BeanManager.getSearchResultBean();
        	srb.setPatientResults(null);
            return null;
        }
        try {
            asynchronousQuery(query);
            if (this.resultPerPageOption != null) {
            	srb = BeanManager.getSearchResultBean();
            	srb.setResultsPerPage(new Integer(resultPerPageOption));
            }
        }
        catch (Exception e) {
            logger.error("Error submitting query", e);
            e.printStackTrace();
        }

        return "search";
    }


    /**
     * Called when the user pressed the Back to Simple Search or Back to
     * Advanced Search button on the curation data search page.
     *
     * @return - string used by JSF to determine where to go next
     */
    public String backToSimpleAdvancedSearch() {
        // Attempt to build a DicomQuery based on the criteria selected
        // on the simple or advanced search page.
        // This needs to be done so that the criteria list can be
        // displayed on the simple or advanced search page.
        if (!buildQuery()) {
            // If there is a validation error, stay on the
            // same page
            return null;
        }

        // Proceed to the search page
        return "search";
    }



    /**
     * This is for the saved query bean to bring the selection state of the Tree UI in sync
     * with the query/criteria that was just loaded.
     */
    public void updateTree() {
        updateManufacturerLevelOfTree();
        updatedModelLevelOfTree();
        updateVersionLevelOfTree();
    }

    /**
     * This is only called by ISPY.
     */
    public void synchronousLocalQuery(DICOMQuery query) throws Exception {
    	this.setQuery(query);
        SearchResultBean srb = BeanManager.getSearchResultBean();
        SecurityBean secure = BeanManager.getSecurityBean();

        // Handle authorization
        secure.authorizeCollections(query);
        secure.authorizeSitesAndSSGs(query);

        PatientSearcher resultMgr = new PatientSearcher();
        srb.setQuery(query);

        List<PatientSearchResult> patientList = resultMgr.searchForPatients(query);
        srb.setPatientResults(patientList);

        // Save the query automatically in query history if it is not from the
        // URL
        if (!query.isQueryFromUrl()) {
        	saveQueryToHistory();
        }
    }

    /**
     * This kicks off the patient search.  This is called externally by the
     * saved query/query history and internally by submitSearch.
     */
    public void asynchronousQuery(DICOMQuery query) throws Exception {
    	this.setQuery(query);
        SearchResultBean srb = BeanManager.getSearchResultBean();
        SecurityBean secure = BeanManager.getSecurityBean();

        // Handle authorization
        secure.authorizeCollections(query);
        secure.authorizeSitesAndSSGs(query);

        srb.setQuery(query);
        saveQueryToHistory();

        PatientSearcherService patientSeacherService =
        	PatientSearcherServiceFactory.getPatientSearcherService();

        NodeCriteria nodeCriteria = query.getNodeCriteria();
        List<NBIANode> selectedNodes = matchupNodeCriteria(nodeCriteria);

        PatientSearchCompletionService results = patientSeacherService.searchForPatients(selectedNodes,
        		                                                                         query);
        srb.setPatientSearchResultsCompletionService(results);
    }


    /**
     * Switch search from simple to advanced and back again, maintaining current
     * selected values.
     */
    public void switchSearch() {
        advanced = !advanced;
    }

    /**
     * Switch search from simple to US search and back again, maintaining current
     * selected values.
     */
    public void switchUsSearch() {
        usSearch = !usSearch;
    }

    /**
     * Creates a new simple search.
     *
     */
    public String newSimpleSearch() {
    	dynamicSearch = false;
        advanced = false;
        usSearch = false;
        freeTextSearch = false;
        simpleSearch = true;
        return newSearch();
    }

    public String externalSimpleSearch(String collectionName) {
    	dynamicSearch = false;
        advanced = false;
        usSearch = false;
        freeTextSearch = false;
        simpleSearch = true;
        return externalSearch(collectionName);
    }

    public String repopulateSearch() {
        advanced = false;

        return newSearch();
    }

    /**
     * Creates a new advanced search.
     */
    public String newAdvSearch() {
    	dynamicSearch = false;
        advanced = true;

        setDefaultValues();

        return SEARCH;
    }

    /**
     * Called when reset button is pressed, resets all fields.
     */
    public String reset() {
        setDefaultValues();

        return "searchReset";
    }


    //used by SavedQueryReconsturcotr
    public void addPatientItems(Collection<String> items) {
        if (items != null) {
        	patientInput = "";
            for (Object item : items) {
                String value = (String) item;
                if (value != null) {
                    patientInput=patientInput.concat(value + ',');
                }

            }
            patientInput= patientInput.substring(0, patientInput.length()-1);
        }
    }

    public void addModalityAndSearchItem(String s) {
    	modalityAndedSearch = s;
    }


    public void addManufacturerItems(Collection<String> items) {
        selectedManufacturers.addAll(items);
    }

    public List<String> getSelectedManufacturers() {
    	return this.selectedManufacturers;
    }

    public void addModelItems(Collection<String> items) {
        selectedModels.addAll(items);
    }

    public List<String> getSelectedModels() {
    	return this.selectedModels;
    }

    public DICOMQuery getQuery() {
        return query;
    }

    public void setQuery(DICOMQuery query) {
        this.query = query;
    }


    public String backToSearch() {
        if (dynamicSearch == true)
        {
        	return DYNAMIC_SEARCH;
        }
        if (freeTextSearch)
        {
        	return FREE_TEXT_SEARCH;
        }
        if (!showThickness) {
            defaultSliceThickness();
        }

        if (!StringUtil.isEmpty(query.getQueryName())) {
            editingSavedQuery = true;

            MessageUtil.addInfoMessage("MAINbody:searchForm:editQueryMessage",
            		                   "editingSavedQuery",
            		                   new Object[] { query.getQueryName() });
        }
       	return SEARCH;
    }


    public boolean isEditingSavedQuery() {
        return editingSavedQuery;
    }

    public void setEditingSavedQuery(boolean editingSavedQuery) {
        this.editingSavedQuery = editingSavedQuery;
    }


    public boolean isAnnotated() {
        return annotated;
    }

    public void setAnnotated(boolean annotated) {
        this.annotated = annotated;
    }


    public List<String> getSelectedSoftwareVersions() {
        return selectedSoftwareVersions;
    }

    public void setSelectedSoftwareVersions(List<String> selectedSoftwareVersions) {
        for (String s : selectedSoftwareVersions) {
            addSelectedSoftwareVersion(s);
        }
    }


    public String[] getAnnotationOptions() {
        return annotationOptions;
    }

    public void setAnnotationOptions(String[] theAnnotationOptions) {
        this.annotationOptions = theAnnotationOptions;
    }

    public String[] getNumFrameOptions() {
        return numFrameOptions;
    }

    public void setNumFrameOptions(String[] theNumFrameOptions) {
        this.numFrameOptions = theNumFrameOptions;
    }

    public String[] getColorModeOptions() {
        return colorModeOptions;
    }

    public void setColorModeOptions(String[] theColorModeOptions) {
        this.colorModeOptions = theColorModeOptions;
    }


	/**
	 * @return Returns the dxDcdSearchBean.
	 */
	public ReconstructionDiameterSearchBean getRcdSearchBean() {
		return this.rcdSearchBean;
	}


	/**
	 * @return Returns the dxDcdSearchBean.
	 */
	public DxDataCollectionDiameterSearchBean getDxDcdSearchBean() {
		return dxDcdSearchBean;
	}


	/**
	 * @return Returns the amSearchBean.
	 */
	public AcquisitionMatrixSearchBean getAmSearchBean() {
		return amSearchBean;
	}


	/**
	 * @return Returns the dcdSearchBean.
	 */
	public DataCollectionDiameterSearchBean getDcdSearchBean() {
		return dcdSearchBean;
	}

    /**
     * @return Returns the dateFrom.
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom The dateFrom to set.
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return Returns the dateTo.
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo The dateTo to set.
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getPatientInput() {
		return patientInput;
	}

	public void setPatientInput(String patientInput) {
		this.patientInput = patientInput;
	}




	public String getResultPerPageOption() {
		return resultPerPageOption;
	}

	public void setResultPerPageOption(String resultPerPageOption) {
		this.resultPerPageOption = resultPerPageOption;
	}


	public boolean isFromSavedQuery() {
		return fromSavedQuery;
	}

	public void setFromSavedQuery(boolean fromSavedQuery) {
		this.fromSavedQuery = fromSavedQuery;
	}


	public boolean isEditSavedQuery() {
		return editSavedQuery;
	}

	public void setEditSavedQuery(boolean editSavedQuery) {
		this.editSavedQuery = editSavedQuery;
	}

	public String newDynamicSearch()
	{
		dynamicSearch = true;
		simpleSearch = false;
		freeTextSearch = false;
		SearchResultBean srb = BeanManager.getSearchResultBean();
		srb.setPatientResults(null);
		DynamicSearchBean dySearchBean = BeanManager.getDynamicSearchBean();
		dySearchBean.resetAction();
		return DYNAMIC_SEARCH;
	}

	public boolean isFreeTextSearch() {
		return freeTextSearch;
	}

	public String newFreeTextSearch()
	{
		freeTextSearch = true;
		simpleSearch = false;
		dynamicSearch = false;
		SearchResultBean srb = BeanManager.getSearchResultBean();
		srb.setPatientResults(null);
		DynamicSearchBean dySarchBean = BeanManager.getDynamicSearchBean();
		dySarchBean.setTextValue("");
		return FREE_TEXT_SEARCH;
	}
	public boolean isDynamicSearch() {
		return dynamicSearch;
	}

	public void setDynamicSearch(boolean dynamicSearch) {
		this.dynamicSearch = dynamicSearch;
	}
	public void setFreeTextSearch(boolean freeTextSearch) {
		this.freeTextSearch = freeTextSearch;
	}
	public RangeValidator getRangeValidator() {
		return new RangeValidator();
	}


	/**
	 * Any page that wants to force an update can call this notification
	 * hack method.... side effect will make IceFaces think that section of
	 * page has changed and will refresh.
	 */
	public int getNotificationHack() {
		return (notificationHack+=1);
	}


	public AimSearchWorkflowBean getAimSearchWorkflowBean() {
		return aimSearchWorkflowBean;
	}

	public void setAimSearchWorkflowBean(AimSearchWorkflowBean aimSearchWorkflowBean) {
		this.aimSearchWorkflowBean = aimSearchWorkflowBean;
	}

	////////////////////////////////PRIVATE/////////////////////////////////////////

	private AimSearchWorkflowBean aimSearchWorkflowBean;

	private int notificationHack = 0;

	private LookupManager lookupMgr = null;

    /**
     * Logger for class.
     */
    private static Logger logger = Logger.getLogger(SearchWorkflowBean.class);

    /**
     * Parameters for the different search criteria.
     */
    private String thicknessLeftCompare = "";
    private String imageThicknessLeft = "";
    private String thicknessRightCompare = "";
    private String imageThicknessRight = "";
    private String numberStudies = "";
    private String monthCompare = "";
    private String numberMonths = "";
    private String seriesDescription = "";
    private String modalityAndedSearch = "any";
    private boolean toggleQuery = false;


	public boolean isToggleQuery() {
		return toggleQuery;
	}

	public void setToggleQuery(boolean toggleQuery) {
		this.toggleQuery = toggleQuery;
	}

	/**
     * The values (v. labels) of contrast agents set by the user (from checkboxes).
     * This array can be empty, or have 1 or 2 elements with the value
     * ContrastAgentCriteria.ENHANCED or ContrastAgentCriteria.UNENHANCED
     */
    private String[] contrastAgents;

    /**
     * The values (v. labels) of annotation options set by the user (from checkboxes).
     * This array can be empty, or have 1 or 2 elements with the value
     * AnnotationOptionCriteria.AnnotationOnly or AnnotationOptionCriteria.NoAnnotation
     */
    private String[] annotationOptions;
    private String[] numFrameOptions;
    private String[] colorModeOptions;

    /**
     * Parameter for the resultPerPage drop down.
     */
    private String resultPerPageOption = "";

    //kilovoltage
    private String kvLeftCompare = "";
    private String kvPeakLeft = "";
    private String kvRightCompare = "";
    private String kvPeakRight = "";



    /**
     * This query is built up based on user input... and then sent onto......some object
     * that will actually talk to the db
     */
    private DICOMQuery query;

    /**
     * Flag that holds whether or not this is an advanced search, will render
     * different components on page based upon this flag.
     */
    private boolean advanced = false;
    private boolean usSearch = false;

    private boolean dynamicSearch = false;
    private boolean freeTextSearch = false;
    private boolean simpleSearch = false;

    private boolean patientCriteria = false;
    private boolean dateCriteria = false;

	/**
     * Holds the values for manufacturer, model and software version in the tree
     */
    private List<String> selectedManufacturers = new ArrayList<String>();
    private List<String> selectedModels = new ArrayList<String>();
    private List<String> selectedSoftwareVersions = new ArrayList<String>();

    /**
     * Holds values for selection boxes
     */
    private List<SelectItem> collectionItems = new ArrayList<SelectItem>();
    private List<SelectItem> modalityItems = new ArrayList<SelectItem>();
    private List<SelectItem> anatomicalSiteItems = new ArrayList<SelectItem>();
    private List<SelectItem> usMultiModalityItems = new ArrayList<SelectItem>();
    private List<SelectItem> kernelItems = new ArrayList<SelectItem>();
    private List<SelectItem> remoteNodeItems = new ArrayList<SelectItem>();

    /**
     * Reference to lookup bean in order to populate dropdowns.
     */
    private SearchLookupBean lookupBean;

    /**
     * Flag of whether or not the image slice thickness is selected.
     */
    private boolean showThickness = false;


    private String patientInput;


    /**
     * Boolean to represent if a user is editing a saved query
     */
    private boolean editingSavedQuery;

    /**
     *
     */
    private boolean editSavedQuery=false;

    /**
     * Boolean to represent if the user wants to search annotated data
     */
    private boolean annotated;

    private ReconstructionDiameterSearchBean rcdSearchBean;
    private DataCollectionDiameterSearchBean dcdSearchBean;
    private DxDataCollectionDiameterSearchBean dxDcdSearchBean;
    private AcquisitionMatrixSearchBean amSearchBean;
    private Date dateFrom;
    private Date dateTo;

    private static final String SEARCH = "search";
    private static final String DYNAMIC_SEARCH = "dynamicSearch";
    private static final String FREE_TEXT_SEARCH = "freeTextSearch";
    private boolean fromSavedQuery= false;

    /**
     * Should this go into AvailableSearchTermsBeans?
     */
    private Map<NBIANode, AvailableSearchTerms> searchableNodeMap;
    private Map<NBIANode, UsAvailableSearchTerms> usSearchableNodeMap;


    /**
     * map holds modality description
     */
    private Map<String, String> modalityDescMap = new HashMap<String, String>();


    /**
     * Orderd List of NBIA nodes... parallel with the SelectItem list that
     * is actually rendered
     */
    private List<NBIANode> searchableNodes;

	/**
     * Sets default values for all search fields.
     */
    private void setDefaultValues() {
    	unselectAllRemoteNodes();

        if (!editingSavedQuery) {
            query = null;
        }

        showThickness = false;
        annotated = false;

        defaultSliceThickness();

        numberStudies = "";
        monthCompare = "";
        numberMonths = "";
        seriesDescription = "";
        modalityAndedSearch = "any";
        kvLeftCompare = "";
        patientInput = "";
        patientCriteria=false;
        dateCriteria=false;

        setDefaultKilovoltValues();
        if(resultPerPageOption == null || StringUtil.isEmpty(resultPerPageOption)){
        	resultPerPageOption = "10";
        }
        //and if its not null??? then what??? leave it????

        rcdSearchBean.setDefaultValues();
        dcdSearchBean.setDefaultValues();
        dxDcdSearchBean.setDefaultValues();
        amSearchBean.setDefaultValues();

        selectedManufacturers.clear();
        selectedModels.clear();
        selectedSoftwareVersions.clear();

        resetManufacturerTree();

        if (query != null) {
            query.setQueryFromUrl(false);
        }


        if(! isEditSavedQuery()) {
        	dateFrom = null;
        	dateTo = null;
        	numFrameOptions = new String[2];
            numFrameOptions[0] = NumFrameOptionCriteria.SingleFrameOnly;
            numFrameOptions[1] = NumFrameOptionCriteria.MultiFrame;
            colorModeOptions = new String[2];
            colorModeOptions[0] = ColorModeOptionCriteria.BMode;
            colorModeOptions[1] = ColorModeOptionCriteria.ColorMode;
            unselectAllUsMultiModalityItems();
        }

        contrastAgents = new String[2];
        contrastAgents[0] = ContrastAgentCriteria.ENHANCED;
        contrastAgents[1] = ContrastAgentCriteria.UNENHANCED;
        annotationOptions = new String[2];
        annotationOptions[0] = AnnotationOptionCriteria.AnnotationOnly;
        annotationOptions[1] = AnnotationOptionCriteria.NoAnnotation;


        unselectAllKernels();
        unselectAllCollections();
        unselectAllModalities();
        unselectAllAnatomicalSites();

        this.aimSearchWorkflowBean.setDefaultValues();
        SearchResultBean srb = BeanManager.getSearchResultBean();
    	srb.setPatientResults(null);
    }

    private void setDefaultKilovoltValues() {
    	kvLeftCompare = "";
        kvPeakLeft = "";
        kvRightCompare = "";
        kvPeakRight = "";
    }

    /**
     * Creates a new search by checking to make sure a user is logged in, then
     * setting up the default values.
     */
    private String newSearch() {
        logger.debug("calling new search action");

        SecurityBean secure = BeanManager.getSecurityBean();

        if (secure.getLoggedIn()) {
            SearchResultBean resultBean = BeanManager.getSearchResultBean();
            resultBean.setPatientResults(null);
            editingSavedQuery = false;
            setDefaultValues();
            return SEARCH;
        }
        else {
            MessageUtil.addErrorMessage("MAINbody:loginForm:pass", "securitySearch");
            secure.setLoginFailure(false);
            return "loginFail";
        }
    }

    private String externalSearch(String collectionName) {
        logger.debug("calling external search action");

        SecurityBean secure = BeanManager.getSecurityBean();

        if (secure.getLoggedIn()) {
            SearchResultBean resultBean = BeanManager.getSearchResultBean();
            resultBean.setPatientResults(null);
            editingSavedQuery = false;
            setDefaultValues();

        	List<String> collectionNames = lookupMgr.getSearchCollection();
        	//Collections.sort(collectionNames);
            collectionItems = JsfUtil.getBooleanSelectItemsFromStrings(collectionNames);
            SelectItem item = JsfUtil.findSelectItemByLabel(collectionItems, collectionName);
    		if(item!=null) {
    			item.setValue(true);
    		}

    		try {
    			submitSearch();

    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            return SEARCH;
        }
        else {
            MessageUtil.addErrorMessage("MAINbody:loginForm:pass", "securitySearch");
            secure.setLoginFailure(false);
            return "loginFail";
        }
    }


    private void addSelectedSoftwareVersion(String ver) {
    	if (!StringUtil.isEmpty(ver) && (!this.selectedSoftwareVersions.contains(ver))) {
            this.selectedSoftwareVersions.add(ver);
        }
    }

    private void addSelectedManufacturer(String manufacturer) {

        if (!StringUtil.isEmpty(manufacturer) && !this.selectedManufacturers.contains(manufacturer)) {
            this.selectedManufacturers.add(manufacturer);
        }

    }


    private void addSelectedModel(String model) {
    	if (!StringUtil.isEmpty(model) && (!this.selectedModels.contains(model))) {
    		this.selectedModels.add(model);
    	}
    }

    /**
     * Populates the DicomQuery
     *
     * @return true if successful, false if there were validation errors
     */
    private boolean buildQuery() {
    	setupTreeCriteria();

        // Construct a new DICOMQuery and set result type.
        DICOMQuery oldQuery = query;
        query = QueryBuilder.buildQuery(this, advanced);

        // This checks to see if this is an edit query, if so it keeps the name
        // and ID of the query.
        if (oldQuery != null) {
            query.setQueryName(oldQuery.getQueryName());

            query.setSavedQueryId(oldQuery.getSavedQueryId());
        }
        if(query.getCriteriaList().isEmpty()) {
        	return false;
        }
        return true;
    }


    private void setupTreeCriteria() {
        selectedManufacturers.clear();
        selectedModels.clear();
        selectedSoftwareVersions.clear();

        DefaultTreeModel manufacturerTree = lookupBean.getManufacturerTree();
        Enumeration manufacturers = ((DefaultMutableTreeNode)manufacturerTree.getRoot()).children();

        while (manufacturers.hasMoreElements()) {
        	DefaultMutableTreeNode currMan = (DefaultMutableTreeNode) manufacturers.nextElement();
        	EquipmentTreeUserObject manObj = (EquipmentTreeUserObject)currMan.getUserObject();

        	if(manObj.isSelected()) {
        		addSelectedManufacturer(manObj.getText());
        	}
        	else {
	            Enumeration currModels = currMan.children();

	            while (currModels.hasMoreElements()) {
	            	DefaultMutableTreeNode currModel = (DefaultMutableTreeNode) currModels.nextElement();
	            	EquipmentTreeUserObject modelObj = (EquipmentTreeUserObject)currModel.getUserObject();

	            	if(modelObj.isSelected()) {
	            		addSelectedModel(manObj.getText()+"||"+modelObj.getText());
	            	}
	            	else {
		                Enumeration currentVersions = currModel.children();

		                while (currentVersions.hasMoreElements()) {
		                	DefaultMutableTreeNode currVer = (DefaultMutableTreeNode) currentVersions.nextElement();
		                	EquipmentTreeUserObject verObj = (EquipmentTreeUserObject)currVer.getUserObject();

			            	if(verObj.isSelected()) {
			            		this.addSelectedSoftwareVersion(manObj.getText()+"||"+modelObj.getText()+"||"+verObj.getText());
			            	}
			            }
	            	}
	            }
        	}
        }
    }

	private void resetManufacturerTree() {
        DefaultTreeModel manufacturerTree = lookupBean.getManufacturerTree();
        Enumeration manufacturers = ((DefaultMutableTreeNode)manufacturerTree.getRoot()).children();
		EquipmentTreeUserObject parentObj = (EquipmentTreeUserObject)((DefaultMutableTreeNode)manufacturerTree.getRoot()).getUserObject();
		parentObj.setExpanded(false);
		parentObj.setSelected(false);

		while (manufacturers.hasMoreElements()) {
			DefaultMutableTreeNode currMan = (DefaultMutableTreeNode) manufacturers.nextElement();
			EquipmentTreeUserObject currObj = (EquipmentTreeUserObject)currMan.getUserObject();
			if (currObj.isSelected())
				currObj.setSelected(false);

			if (currObj.isExpanded())
				currObj.setExpanded(false);

			Enumeration currModels = currMan.children();
			while (currModels.hasMoreElements()) {
				DefaultMutableTreeNode currModel = (DefaultMutableTreeNode) currModels.nextElement();
				EquipmentTreeUserObject currModelObj = (EquipmentTreeUserObject)currModel.getUserObject();
				if (currModelObj.isSelected())
					currModelObj.setSelected(false);

				if (currModelObj.isExpanded())
					currModelObj.setExpanded(false);

				Enumeration currentVersions = currModel.children();
				while (currentVersions.hasMoreElements()) {
					DefaultMutableTreeNode currVer = (DefaultMutableTreeNode) currentVersions.nextElement();
					EquipmentTreeUserObject verObj = (EquipmentTreeUserObject)currVer.getUserObject();

					if (verObj.isSelected())
						verObj.setSelected(false);
				}
			}
		}
	}

    //saved query
    private void updateManufacturerLevelOfTree() {
        DefaultTreeModel manufacturerTree = lookupBean.getManufacturerTree();
        Enumeration manufacturers = ((DefaultMutableTreeNode)manufacturerTree.getRoot()).children();

    	for(String selectedManufacturer : this.selectedManufacturers) {
    		while (manufacturers.hasMoreElements()) {
    			DefaultMutableTreeNode currMan = (DefaultMutableTreeNode) manufacturers.nextElement();
    			EquipmentTreeUserObject manObj = (EquipmentTreeUserObject)currMan.getUserObject();

    			if(manObj.getText().equals(selectedManufacturer)) {
    				manObj.setSelected(true);

            		expandNode(currMan);

    			}
        	}
    	}
    }

    private void updatedModelLevelOfTree() {
        DefaultTreeModel manufacturerTree = lookupBean.getManufacturerTree();
        Enumeration manufacturers = ((DefaultMutableTreeNode)manufacturerTree.getRoot()).children();

    	for(String selectedModel : this.selectedModels) {
    		while (manufacturers.hasMoreElements()) {
    			DefaultMutableTreeNode currMan = (DefaultMutableTreeNode) manufacturers.nextElement();
	            Enumeration currModels = currMan.children();

	            while (currModels.hasMoreElements()) {
	            	DefaultMutableTreeNode currModel = (DefaultMutableTreeNode) currModels.nextElement();
	            	EquipmentTreeUserObject modelObj = (EquipmentTreeUserObject)currModel.getUserObject();

	            	if(StringUtil.lastToken(selectedModel, "||").equals(modelObj.getText())) {
	            		modelObj.setSelected(true);

	            		expandNode(currModel);
	        		}
	            }
    		}
    	}
    }

    private void updateVersionLevelOfTree() {
        DefaultTreeModel manufacturerTree = lookupBean.getManufacturerTree();
        Enumeration manufacturers = ((DefaultMutableTreeNode)manufacturerTree.getRoot()).children();

    	for(String selectedSoftwareVersion : this.selectedSoftwareVersions) {
    		while (manufacturers.hasMoreElements()) {
    			DefaultMutableTreeNode currMan = (DefaultMutableTreeNode) manufacturers.nextElement();
	            Enumeration currModels = currMan.children();

	            while (currModels.hasMoreElements()) {
	            	DefaultMutableTreeNode currModel = (DefaultMutableTreeNode) currModels.nextElement();
	                Enumeration currentVersions = currModel.children();

	                while (currentVersions.hasMoreElements()) {
	                	DefaultMutableTreeNode currVer = (DefaultMutableTreeNode) currentVersions.nextElement();
	                	EquipmentTreeUserObject verObj = (EquipmentTreeUserObject)currVer.getUserObject();

			           	if(StringUtil.lastToken(selectedSoftwareVersion,"||").equals(verObj.getText())) {
			           		verObj.setSelected(true);

		            		expandNode(currVer);
			            }
	            	}
	            }
        	}
        }
    }

    private static void expandNode(DefaultMutableTreeNode treeNode) {
    	DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)treeNode.getParent();
    	if(parentNode==null) {
    		return;
    	}
    	else {
    		EquipmentTreeUserObject parentObj = (EquipmentTreeUserObject)parentNode.getUserObject();
    		parentObj.setExpanded(true);
    		expandNode(parentNode);
    	}
    }


    /**
     * Setup the image slice thickness dropdowns to their default values.
     */
    private void defaultSliceThickness() {
        showThickness = false;

        thicknessLeftCompare = RangeData.GREATER_THAN;
        imageThicknessLeft = "0 mm";

        thicknessRightCompare = RangeData.LESS_THAN_EQUAL;
        imageThicknessRight = "5 mm";
    }

    /**
     * Validate the date fields
     *
     * Let the component do most of the work now.... just validate the cross
     * date constraints (order, future, etc.)
     */
    private String validateDates() {
        DateValidator dateValidator = new DateValidator();
        String result = dateValidator.validateDates(dateFrom, dateTo, false);
        if(result!=null) {
        	//throw new ValidatorException(new FacesMessage(message, message));
            FacesMessage facesMessage = new FacesMessage("Date Invalid: "+result);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("MAINbody:searchForm:dateValid", facesMessage);
            return result;
        }
        else {
        	return null;
        }
    }


    private String validateBeforeSearch() {
        String result = validateDates();
        if(result!=null) {
            return result;
        }

        return null;
    }



    private void saveQueryToHistory() throws Exception {
        SecurityBean secure = BeanManager.getSecurityBean();

        String uName = secure.getUsername();
        query.setUserID(uName);
        query.setExecuteTime(Calendar.getInstance().getTime());

        QueryStorageManager qManager = (QueryStorageManager)SpringApplicationContext.getBean("queryStorageManager");

        long queryId = qManager.addQueryToHistory(query);
        query.setQueryHistoryId(queryId);
    }

    /**
     * This is necessary to support saved queries (instead of just calling
     * getSelectedNodes).  Try to match up the (saved) URL with available
     * nodes.  If not found, just blow it off.
     **/
    private List<NBIANode> matchupNodeCriteria(NodeCriteria nodeCriteria) {

        List<NBIANode> matchedNodes = new ArrayList<NBIANode>();
        if(nodeCriteria==null) {
        	matchedNodes = new ArrayList<NBIANode>(1);
        	matchedNodes.add(LocalNode.getLocalNode());
        }
        else {
            List<String> nodeUrls = nodeCriteria.getRemoteNodes();

        	for(NBIANode searchableNode : searchableNodes) {
        		if(nodeUrls.contains(searchableNode.getURL())) {
        			matchedNodes.add(searchableNode);
        		}
        	}
        }
        return matchedNodes;
    }

    public void modalityChangeListener(ValueChangeEvent event) {

    	if(!editingSavedQuery)
    	{
    		setToggleQuery(true);
    	}


    	if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
    		event.setPhaseId(PhaseId.INVOKE_APPLICATION);
    		event.queue();
            return;
        }
		for (SelectItem selectItem : modalityItems) {
			System.out.println("modality changed:" + selectItem.getLabel() + " "+ selectItem.getValue());
			if (selectItem.getLabel().equals("US")) {
				if (selectItem.getValue().equals(Boolean.TRUE)) {
					System.out.println("modality changed:"
							+ selectItem.getLabel() + " "
							+ selectItem.getValue());
					usSearch = true;
				} else {
					usSearch = false;
			        unselectAllUsMultiModalityItems();
				}
			}
		}
		try {
			submitSearch();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public void patientSelectChangeListener(ValueChangeEvent event) {

    	if(!editingSavedQuery)
    	{
    		setToggleQuery(true);
    	}

    	if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
    		event.setPhaseId(PhaseId.INVOKE_APPLICATION);
    		event.queue();
            return;
        }
		try {
			logger.debug("patientSearch invoked");
			submitSearch();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public void baselineSelectChangeListener(ValueChangeEvent event) {

    	if(!editingSavedQuery)
    	{
    		setToggleQuery(true);
    	}

    	if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
    		event.setPhaseId(PhaseId.INVOKE_APPLICATION);
    		event.queue();
            return;
        }
		try {
			logger.debug("baselineSearch invoked");
			submitSearch();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public void dateSelectChangeListener(ValueChangeEvent event) {

    	if(!editingSavedQuery)
    	{
    		setToggleQuery(true);
    	}

    	if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
    		event.setPhaseId(PhaseId.INVOKE_APPLICATION);
    		event.queue();
            return;
        }
		try {
			logger.debug("dateSearch invoked");
			submitSearch();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    private void populateModalityDescMap() {
    	ModalityDescProcessor processor = new ModalityDescProcessor();
        List<ModalityDescDTO> dtoList = processor.findAllModalityDesc();

        modalityDescMap.clear();
        for( ModalityDescDTO dto : dtoList) {
        	modalityDescMap.put(dto.getModalityName(), dto.getDescription());
        }
    }

    /*
     * returns whether to show anatomic criteria or not
     */
    public boolean isShowAnatomicCriteria() {
    	String retValue = System.getProperty("show.anatomical.search.criteria");

    	if( retValue.equals("true")) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

   public void resultPerPageOptionChangeListener(ValueChangeEvent event) {
	   if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
   		event.setPhaseId(PhaseId.INVOKE_APPLICATION);
   		event.queue();
           return;
       }
	    String resultPerPageOptionNewValue = (String)event.getNewValue();
		System.out.println("resultPerPageOption new value" + resultPerPageOptionNewValue);
		this.resultPerPageOption = resultPerPageOptionNewValue;
		try {
			submitSearch();
		} catch (Exception e) {
			e.printStackTrace();
		}

   }

   public void modalityAndAnyOptionChangeListener(ValueChangeEvent event) {
	   if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
   		event.setPhaseId(PhaseId.INVOKE_APPLICATION);
   		event.queue();
           return;
       }
	    String modalityAndedSearch = (String)event.getNewValue();
		System.out.println("modalityAndedSearch new value" + modalityAndedSearch);
		this.modalityAndedSearch = modalityAndedSearch;
		try {
			submitSearch();
		} catch (Exception e) {
			e.printStackTrace();
		}

   }
}
