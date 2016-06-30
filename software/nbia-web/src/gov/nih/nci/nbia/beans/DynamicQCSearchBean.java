/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans;

import gov.nih.nci.nbia.beans.qctool.QcToolSearchBean;
import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.dynamicsearch.QueryHandler;
import gov.nih.nci.nbia.factories.ApplicationFactory;
import gov.nih.nci.nbia.qctool.VisibilityStatus;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.DateValidator;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.SelectItemLabelComparator;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.verifysubmission.VerifySubmissionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.paneltabset.TabChangeEvent;

public class DynamicQCSearchBean extends DynamicSearchBean {
	private String[] selectedQcStatus;
	
  ////////////////////////////////////////////////////////////
	private String selectedQcBatchNum;
	private List<SelectItem> qcBatchNums = new ArrayList<SelectItem>();
    
    private List<SelectItem> qcSubmissionTypes = new ArrayList<SelectItem>();
    private String selectedQcSubmissionType; 
    
    private List<SelectItem> qcReleasedStatus = new ArrayList<SelectItem>();
    private String selectedQcReleasedStatus; 
	
  ////////////////////////////////////////////////////////////
	private Date fromDate;
	private Date toDate;
	private QcToolSearchBean qcToolSearchBean;
	private int tabIndex = 0;// as we want to put focus on first tab;

	public QcToolSearchBean getQcToolSearchBean() {
		return qcToolSearchBean;
	}
	public void setQcToolSearchBean(QcToolSearchBean qcToolSearchBean) {
		this.qcToolSearchBean = qcToolSearchBean;
	}
	
	public DynamicQCSearchBean() throws Exception {
		super();
		String[] defaultCheckBoxLable = { "Not Yet Reviewed"  };
		
		String defaultQcBatchNum = "  ";
    	String defaultQcSubmissionType = "  ";
    	String defaultQcReleasedStatus = "  ";
		
        setSelectedQcStatus(defaultCheckBoxLable);
       
        setSelectedQcBatchNum(defaultQcBatchNum);
        setSelectedQcSubmissionType(defaultQcSubmissionType);
        setSelectedQcReleasedStatus(defaultQcReleasedStatus);
        
        setFromDate(null);
        setToDate(null);
        
        setUpAdditionalQCFlags();
        
	}
	public String submitQCSearch() throws Exception {
		if(validateDates()!=null) {
			if (qcToolSearchBean.getQsrDTOList() != null) {
	        	qcToolSearchBean.getQsrDTOList().clear();
	        }
            return null;
        }
		String [] qcStatus = getSelectedQcStatus();
				
		// Create additional QC flag list		
		 String[] additionalQcFlagList = new String[3];
		 additionalQcFlagList[0] = getSelectedQcBatchNum();
		 additionalQcFlagList[1] = getSelectedQcSubmissionType();
		 additionalQcFlagList[2] = getSelectedQcReleasedStatus();
		 
		
		if (qcStatus == null || qcStatus.length==0){
	       MessageUtil.addErrorMessage("MAINbody:qcToolSearchCritForm:dslctQcStatus","qcTool_requiedField_Search");
	       if (qcToolSearchBean.getQsrDTOList() != null) {
	        	qcToolSearchBean.getQsrDTOList().clear();
	        }
	       return null;
	    }
		QueryHandler qh = (QueryHandler)SpringApplicationContext.getBean("queryHandler");		
		qh.setStudyNumberMap(ApplicationFactory.getInstance().getStudyNumberMap());
		
		qh.setQueryCriteria(getCriteria(), getRelation(), authorizedSiteData, seriesSecurityGroups, getSelectedQcStatus());
		
		qcToolSearchBean.setQsrDTOList(qh.querySeries(fromDate, toDate, additionalQcFlagList));
		setTabIndex(1);// set focus on dynamic tab
		return "qcToolMain";
	}
    /**
     * Validate the date fields.  The dates should already
     * be validated individually by the calendar component
     * by the time we are here.  This method is for validating
     * the relationships between the dates and submission time.
     */
    public String validateDates() {
    	if( fromDate == null && toDate == null ) {
    		return null;
    	}
        DateValidator dateValidator = new DateValidator();
        String result = dateValidator.validateDates(fromDate, toDate, true) ;

        if(result!=null) {
            FacesMessage facesMessage = new FacesMessage("Date Invalid: "+result);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("error", facesMessage);
        }
        return result;
    }

	/**
     * Gets the option items for QC Status.
     *
     * @return array of QC Status items
     */
    public SelectItem[] getQcStatusItems() {
    	SelectItem[] qcStatusItems = new SelectItem[12];
		qcStatusItems[0] = new SelectItem(VisibilityStatus.NOT_YET_REVIEWED.getText() );
        qcStatusItems[1] = new SelectItem(VisibilityStatus.VISIBLE.getText());
        qcStatusItems[2] = new SelectItem(VisibilityStatus.NOT_VISIBLE.getText());
        qcStatusItems[3] = new SelectItem(VisibilityStatus.TO_BE_DELETED.getText());
        qcStatusItems[4] = new SelectItem(VisibilityStatus.STAGE_1.getText());
        qcStatusItems[5] = new SelectItem(VisibilityStatus.STAGE_2.getText());
        qcStatusItems[6] = new SelectItem(VisibilityStatus.STAGE_3.getText());
        qcStatusItems[7] = new SelectItem(VisibilityStatus.STAGE_4.getText());
        qcStatusItems[8] = new SelectItem(VisibilityStatus.STAGE_5.getText());
        qcStatusItems[9] = new SelectItem(VisibilityStatus.STAGE_6.getText());
        qcStatusItems[10] = new SelectItem(VisibilityStatus.STAGE_7.getText());
        qcStatusItems[11] = new SelectItem(VisibilityStatus.DOWNLOADABLE.getText());
        
        return qcStatusItems;
    }
    

    /**
     * Setup the option items for various the additional QC flags:
     * 
     * BatchNum - Numeric 
     * SubmissionType - String - Complete = Yes or Ongoing = No
     * ReleasedStatus - String - Yes or No
     */
    
 public void  setUpAdditionalQCFlags(){
    	
    	qcBatchNums.clear();    	    	
    	
       int batchNumberTotal = NCIAConfig.getQCBatchNumberSelectSize();
    	
    	for(int i = 0; i <= batchNumberTotal; i++)
    	{
    		if(i == 0)
    			qcBatchNums.add(new SelectItem("  "));
    		else			
    		    qcBatchNums.add(new SelectItem(""+ i));
    	}
    	
    //---------------------------------------------    
    	
    	qcSubmissionTypes.clear();
    	qcSubmissionTypes.add(new SelectItem("  "));
    	qcSubmissionTypes.add(new SelectItem("NO"));
    	qcSubmissionTypes.add(new SelectItem("YES"));   	
    	     	    	        
 //---------------------------------------------    
    	
    	qcReleasedStatus.clear();
    	qcReleasedStatus.add(new SelectItem("  "));
    	qcReleasedStatus.add(new SelectItem("NO"));
    	qcReleasedStatus.add(new SelectItem("YES"));   	
    }
    
   //////////Begin Getters for Additional QC Flags ////////////////
   public List<SelectItem> getQcBatchNums() {
	   return qcBatchNums;
   }

   public List<SelectItem> getQcSubmissionTypes() {
	   return qcSubmissionTypes;
   }

   public List<SelectItem> getQcReleasedStatus() {
	   return qcReleasedStatus;
   }
    
    /**
     * This is to help workaround the timezone stuff in the calendar
     * that uses GMT and causes days to be off
     */
    public TimeZone getDefaultTimeZone() {
        return TimeZone.getDefault();
    }
    
	public String[] getSelectedQcStatus() {
		return selectedQcStatus;
	}
	
	public void setSelectedQcStatus(String[] selectedQcStatus) {
		this.selectedQcStatus = selectedQcStatus;
	}
	
	//============ Getters and Setters for additional QC flags ==============

		public String getSelectedQcBatchNum() {
			return selectedQcBatchNum;
		}

		public void setSelectedQcBatchNum(String selectedQcBatchNum) {
			this.selectedQcBatchNum = selectedQcBatchNum;
	   }
		
		public String getSelectedQcSubmissionType() {
			return selectedQcSubmissionType;
		}

		public void setSelectedQcSubmissionType(String selectedQcSubmissionType) {
			this.selectedQcSubmissionType = selectedQcSubmissionType;
	   }
		
		public String getSelectedQcReleasedStatus() {
			return selectedQcReleasedStatus;
		}

		public void setSelectedQcReleasedStatus(String selectedQcReleasedStatus) {
			this.selectedQcReleasedStatus = selectedQcReleasedStatus;
	   }
	/////////////////////////////////////////////////////////////////////////
	

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	 public void tabChangeListener(TabChangeEvent event) {
	    	qcToolSearchBean.setQsrDTOList(null);
	    }
	public int getTabIndex() {
		return tabIndex;
	}
	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}
	public void resetAction() {
		super.resetAction();
		String[] defaultCheckBoxLable = { "Not Yet Reviewed"  };
				
		String defaultQcBatchNum = "  ";
    	String defaultQcSubmissionType = "  ";		
    	String defaultQcReleasedStatus = "  ";
		
        setSelectedQcStatus(defaultCheckBoxLable);
        
        setSelectedQcBatchNum(defaultQcBatchNum);
        setSelectedQcSubmissionType(defaultQcSubmissionType); 
        setSelectedQcReleasedStatus(defaultQcReleasedStatus);
        
        setFromDate(null);
        setToDate(null);
        if (qcToolSearchBean.getQsrDTOList() != null) {
        	qcToolSearchBean.getQsrDTOList().clear();
        }
	}
	public void fieldItemChanged(ValueChangeEvent event) throws Exception {
		super.fieldItemChanged(event);
		String newFieldValue = (String)event.getNewValue();
		this.hasPermissibleData = checkPermissibleData(newFieldValue);
		if(hasPermissibleData && newValue.equals(initialDataGroup) && newFieldValue.equals(dataGroup.getDataSource().get(0).getSourceItem().get(0).getItemName())) {
			permissibleData = new ArrayList<SelectItem>();
			permissibleData.add(new SelectItem(defaultSelectValue, defaultSelectLabel));
			SecurityBean secure = BeanManager.getSecurityBean();
	        AuthorizationManager am = secure.getAuthorizationManager();
	        List<SiteData> authorizedSites = am.getAuthorizedSites(RoleType.MANAGE_VISIBILITY_STATUS);
	        for (SiteData st : authorizedSites) {
				permissibleData.add(new SelectItem(st.getCollection(),VerifySubmissionUtil.siteDataToString(st)));
			}
	        Comparator<SelectItem> dssic = new SelectItemLabelComparator();
			Collections.sort(permissibleData, dssic);
		}
	}

}
