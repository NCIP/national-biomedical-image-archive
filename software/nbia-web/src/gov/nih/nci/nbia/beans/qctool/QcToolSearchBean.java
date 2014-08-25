/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.qctool;

import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.dao.QcStatusDAO;
import gov.nih.nci.nbia.dto.QcSearchResultDTO;
import gov.nih.nci.nbia.dto.QcStatusHistoryDTO;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

import com.icesoft.faces.async.render.SessionRenderer;

/**
 *
 * <p>This bean relies upon VerifySubmissionBean to manage the
 * selection criteria for the report (date range, collection//site).
 */
public class QcToolSearchBean {

	public UIData getDataTable() {
		return dataTable;
	}
	public void setDataTable(UIData dataTable) {
		this.dataTable = dataTable;
	}
	public QcToolSearchBean() {
		SessionRenderer.addCurrentSession(getSessionId());

	}
	private String getSessionId() {
		FacesContext fCtx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
		String sessionId =session.getId();
		return sessionId;
	}
	public void updateStatus() {
        // render the page
       	SessionRenderer.render(getSessionId());
    }
	public void pageNumberChangeListener(ValueChangeEvent event)
	{
		this.getDataTable().setFirst(0);


		/* To force the UI to show the updated values */
		if (event.getNewValue() != null ) {
			PhaseId phaseId = event.getPhaseId();

	        if (phaseId.equals(PhaseId.ANY_PHASE))
	        {
	            event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
	            event.queue();
	        }
	        else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES))
	        {
	        	String retValue = (String)event.getNewValue();

	        	setSelectedDispItemNum(retValue);
	        }
		}
	}

    /**
     * This is the overall QC Tool Search Result for the total
     * time frame (as opposed to the per day report).
     */
    public List<QcSearchResultDTO> getQsrDTOList() {
    	if (!oldSort.equals(sortColumnName) ||
                oldAscending != ascending){
             sort();
             oldSort = sortColumnName;
             oldAscending = ascending;
        }
        return qsrDTOList;
    }
    public void setQsrDTOList(List<QcSearchResultDTO> qsrDTOList) {
		this.qsrDTOList = qsrDTOList;
	}
	public List<QcStatusHistoryDTO>  getQshDTOList() {
        return qshDTOList;

    }

	/**
	 * @return the selectedDispItemNum
	 */
	public String getSelectedDispItemNum() {
		return selectedDispItemNum;
	}
	/**
	 * @param selectedDispItemNum the selectedDispItemNum to set
	 */
	public void setSelectedDispItemNum(String selectedDispItemNum) {
		this.selectedDispItemNum = selectedDispItemNum;
	}

	public Integer getResultsPerPage() {
		if( getSelectedDispItemNum() != null ) {
			return new Integer(getSelectedDispItemNum());
		}
		else {
			return 10;
		}
	}

	/**
     * This action is called when the submit button is clicked.
     */
    public String submit() throws Exception {
    	if(qcToolBean.validateDates()!=null) {
            return null;
        }
    	//for list box only
 //   	List<String> collectionSites = qcToolBean.getSelectedCollectionNames();
    	List<String> collectionSites = new ArrayList<String>();
    	collectionSites.add(qcToolBean.getSelectedCollectionSite());
        String [] qcStatus = {"To Be Deleted"};
        String patientIds = qcToolBean.getSelectedPatients();
        String [] patients = null;
        ifNotClickedSubmit = false;
        if (! qcToolBean.isSuperRole()) {
        	qcStatus = qcToolBean.getSelectedQcStatus();
        }
        if (qcStatus == null || qcStatus.length==0){
        	MessageUtil.addErrorMessage("MAINbody:qcToolSearchCritForm:slctQcStatus",
        			REQUIRED_FIELD);
        	return null;
        }

         if (qsrDTOList!=null) {
        	 qsrDTOList.clear();
        }

        if (collectionSites.size()==0) {
        	 collectionSites=qcToolBean.getAuthCollectionList();
        }
        if ((patientIds != null) &&  (patientIds.length() >=1)) {
        	patientIds=patientIds.replaceAll(" ","");
        	patients=patientIds.split(",");
        }

        QcStatusDAO qcStatusDAO = (QcStatusDAO)SpringApplicationContext.getBean("qcStatusDAO");
        qsrDTOList = qcStatusDAO.findSeries(qcStatus, collectionSites, patients, qcToolBean.getFromDate(), qcToolBean.getToDate(), getMaxRowsToShow() );

        // the big o
        try {
        SecurityBean secure = BeanManager.getSecurityBean();
        String userName = secure.getUsername();

			for (QcSearchResultDTO dto:qsrDTOList){
				System.out.println("-- adding user to QC DTO ---");
				dto.setUser(userName);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        	
        if (this.getDataTable() != null)
        {
        	this.getDataTable().setFirst(0);
        }

        return "qcToolMain";
    }

    public String  requestRpt(){
    	List<String> seriesList= new ArrayList<String>();
    	if (qsrDTOList !=null){
    	for (int i=0; i<qsrDTOList.size();++i){
    		if (qsrDTOList.get(i).isSelected()){
    			seriesList.add(qsrDTOList.get(i).getSeries());
    		}
    	}
    	}
    	 if (seriesList.size() == 0){
         	MessageUtil.addErrorMessage("MAINbody:qcToolForm:SlctRec",
         			ERRORMSG_RPT);
         	return null;
         }
    	QcStatusDAO qcStatusDAO = (QcStatusDAO)SpringApplicationContext.getBean("qcStatusDAO");
       	qshDTOList =qcStatusDAO.findQcStatusHistoryInfo(seriesList);
    	return "qcTooStatusRpt";
    }

    public void requestRpt(String seriesId){
    	List<String> seriesList= new ArrayList<String>();
    	seriesList.add(seriesId);
    	QcStatusDAO qcStatusDAO = (QcStatusDAO)SpringApplicationContext.getBean("qcStatusDAO");
       	qshDTOList =qcStatusDAO.findQcStatusHistoryInfo(seriesList);
    }


    public String  backToQcTool(){
    	 return "qcToolMain";
    }

    public QcToolBean getQcToolBean() {
		return qcToolBean;
	}

    /**
     * Method to wire in the other bean that contains the input controls/values
     */
    public void setQcToolBean(QcToolBean qcToolBean) {
        this.qcToolBean = qcToolBean;

    }
    /**
     * Gets the options for number of displaying items for QC Result.
     *
     * @return array of predefined numbers for displaying search result
     */
 /*   public SelectItem[] getDispItemNums() {
        SelectItem[] dispItemNums = new SelectItem[4];
        dispItemNums[0] = new SelectItem("10");
        dispItemNums[1] = new SelectItem("25");
        dispItemNums[2] = new SelectItem("50");
        dispItemNums[3] = new SelectItem("100");
        return dispItemNums;
    }
    */

   /**
	 * Any page that wants to force an update can call this notification
	 * hack method.... side effect will make IceFaces think that section of
	 * page has changed and will refresh.
	 */
	public int getNotificationHack() {
		return (notificationHack+=1);
	}

	public String getSelectedHRptDispItemNum() {
		return selectedHRptDispItemNum;
	}
	public void setSelectedHRptDispItemNum(String selectedHRptDispItemNum) {
		this.selectedHRptDispItemNum = selectedHRptDispItemNum;
	}

	public String getDateHeader() {
		return dateHeader;
	}
	public String getSiteHeader() {
		return siteHeader;
	}
	public String getPatientHeader() {
		return patientHeader;
	}
	public String getStudyHeader() {
		return studyHeader;
	}
	public String getSeriesHeader() {
		return seriesHeader;
	}
	public String getVisibilityHeader() {
		return visibilityHeader;
	}
    public String getModalityHeader() {
		return modalityHeader;
	}
	public String getSeriesDescHeader() {
		return seriesDescHeader;
	}
	public boolean isIfNotClickedSubmit() {
		return ifNotClickedSubmit;
	}
	public void setIfNotClickedSubmit(boolean ifNotClickedSubmit) {
		this.ifNotClickedSubmit = ifNotClickedSubmit;
	}


	////////////////////////////PRIVATE///////////////////////////////////////
    private static final String REQUIRED_FIELD = "qcTool_requiedField_Search";
    private static final String ERRORMSG_RPT="qcTool_requiedSeries";
    private QcToolBean qcToolBean;
    private String selectedDispItemNum="25";
    private String selectedHRptDispItemNum="25";
    private List<QcSearchResultDTO> qsrDTOList = null;
    private List<QcStatusHistoryDTO> qshDTOList;
    private static final String ALL = "all";
    private int notificationHack = 0;
    private UIData dataTable;

    private static final String dateHeader = "Submission date";
    private static final String siteHeader = "Collection//Site";
    private static final String patientHeader = "Patient";
    private static final String studyHeader = "Study";
    private static final String seriesHeader = "Series";
    private static final String visibilityHeader = "Visibility";
    private static final String modalityHeader = "Modality";
    private static final String seriesDescHeader = "Series Description";

    private String sortColumnName= "Creation Date";
    private boolean ascending = true;
    private boolean ifNotClickedSubmit = true;

    // we only want to resort if the oder or column has changed.
    private String oldSort = sortColumnName;
    private boolean oldAscending = ascending;

    private int maxRowsToShow = 0;

//    private boolean descending = true;
//    private boolean oldDescending = descending;
//    private String columnName = "Creation Date";
//    private String oldColumnName = columnName;


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

    /**
     *  Sorts the list of qc search result.
     */
    protected void sort() {
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
            	QcSearchResultDTO c1 = (QcSearchResultDTO) o1;
            	QcSearchResultDTO c2 = (QcSearchResultDTO) o2;

                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(dateHeader)) {
                	if (c1.getCreationDate() != null && c2.getCreationDate()!= null) {
                		return compareObject(
                                c1.getCreationDate().compareTo(c2.getCreationDate()),
                                c2.getCreationDate().compareTo(c1.getCreationDate()));
                	} else{
                		return compareObject(
                            c1.getDateTime().compareTo(c2.getDateTime()),
                            c2.getDateTime().compareTo(c1.getDateTime()));
                	}
                }
                else if (sortColumnName.equals(siteHeader)) {
                    return compareObject(c1.getCollectionSite().compareTo(c2.getCollectionSite()),
                            c2.getCollectionSite().compareTo(c1.getCollectionSite()));
                }
                else if (sortColumnName.equals(patientHeader)) {
                    return compareObject(c1.getPatientId().compareTo(c2.getPatientId()),
                        c2.getPatientId().compareTo(c1.getPatientId()));
                }
                else if (sortColumnName.equals(studyHeader)) {
                	return compareObject(c1.getStudy().compareTo(c2.getStudy()),
                		c2.getStudy().compareTo(c1.getStudy()));
                }
                else if (sortColumnName.equals(seriesHeader)) {
                	return compareObject(c1.getSeries().compareTo(c2.getSeries()),
                		c2.getSeries().compareTo(c1.getSeries()));
                }else if (sortColumnName.equals(visibilityHeader)) {
                	return compareObject(c1.getVisibilityStatus().compareTo(c2.getVisibilityStatus()),
                		c2.getVisibilityStatus().compareTo(c1.getVisibilityStatus()));
                }else if (sortColumnName.equals(modalityHeader)) {
                	return compareObject(c1.getModality().compareTo(c2.getModality()),
                		c2.getModality().compareTo(c1.getModality()));
               }else {
                	return 0;
                }
            }
        };
        Collections.sort(qsrDTOList, comparator);
    }

    private int compareObject(int result1, int result2){
    	return ascending ? result1 : result2;
    }

	/**
	 * selects all in the list.
	 */
	public String selectAll() {
		for(QcSearchResultDTO searchResult : qsrDTOList) {
			searchResult.setSelected(true);
		}
		return null;
	}

	/**
	 * selects all in the list.
	 */
	public String unselectAll() {
		for(QcSearchResultDTO searchResult : qsrDTOList) {
			searchResult.setSelected(false);
		}
		return null;
	}

	/**
	 * returns the max number of rows to show in search results
	 */
	public int getMaxRowsToShow() {
		if( maxRowsToShow == 0 ) {
			maxRowsToShow = QcUtil.getMaxNumberOfRowsToShow();
		}

		return maxRowsToShow;
	}

}
