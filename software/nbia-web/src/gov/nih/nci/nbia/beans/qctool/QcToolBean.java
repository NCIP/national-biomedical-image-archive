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
import gov.nih.nci.nbia.qctool.VisibilityStatus;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.DateValidator;
import gov.nih.nci.nbia.util.JsfUtil;
import gov.nih.nci.nbia.util.SelectItemComparator;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.verifysubmission.VerifySubmissionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * This bean captures the input controls for QC Tool
 * including the QC Status, Patient ID and collection/site choice.
 */
public class QcToolBean {


    public QcToolBean() {
    }

    private void setCollectionBox(boolean deleteRole) {
    	 // get Site information and Handle authorization
    	SecurityBean secure = BeanManager.getSecurityBean();
        AuthorizationManager am = secure.getAuthorizationManager();
        List<SiteData> authorizedSites;
        if (deleteRole){
        	 authorizedSites = am.getAuthorizedSites(RoleType.SUPER_CURATOR);
        }
        else {
        	authorizedSites = am.getAuthorizedSites(RoleType.MANAGE_VISIBILITY_STATUS);
        }
       collectionNames.clear();
       collectionItems.clear();
        for(int i=0; i<authorizedSites.size(); i++){
        	collectionNames.add(VerifySubmissionUtil.siteDataToString(authorizedSites.get(i)));
        	//The following line is for combo box only
        	collectionItems.add(new SelectItem(collectionNames.get(i)));
        }

       //The following line is for combo box only
        Collections.sort(collectionItems, new SelectItemComparator());
       // for select list box only
       // Collections.sort(collectionNames);
       // collectionItems = JsfUtil.getBooleanSelectItemsFromStrings(collectionNames);
    }


    public String getSelectedPatients() {
		return selectedPatients;
	}

    public void setSelectedPatients(String selectedPatients) {
		this.selectedPatients = selectedPatients;
	}


	public String[] getSelectedQcStatus() {
		return selectedQcStatus;
	}


	public void setSelectedQcStatus(String[] selectedQcStatus) {
		this.selectedQcStatus = selectedQcStatus;
	}

	/**
     * This is the list of project+sites the user can see.
     */
    public List<SelectItem> getAuthorizedProjectsSitesSelectItems() {
        return authorizedProjectsSitesSelectItems;
    }


    public void setAuthorizedProjectsSitesSelectItems(
			List<SelectItem> authorizedProjectsSitesSelectItems) {
		this.authorizedProjectsSitesSelectItems = authorizedProjectsSitesSelectItems;
	}

    public String getButtonLabel() {
		return buttonLabel;
	}

	public void setButtonLabel(String buttonLabel) {
		this.buttonLabel = buttonLabel;
	}

	/**
     * Gets the option items for QC Status.
     *
     * @return array of QC Status items
     */
    public SelectItem[] getQcStatusItems() {
    	SelectItem[] qcStatusItems = new SelectItem[11];
        //qcStatusItems[0] = new SelectItem("Not Yet Reviewed");
        //qcStatusItems[1] = new SelectItem("Visible");
        //qcStatusItems[2] = new SelectItem("Not Visible");
        //qcStatusItems[3] = new SelectItem("To Be Deleted");
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
        return qcStatusItems;
    }
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

    public String performDelete() {
    	superRole=true;
    	setCollectionBox(true);
    	String[] defaultCheckBoxLable = { "To be Deleted" };
    	buttonLabel="Delete";
        setSelectedQcStatus(defaultCheckBoxLable);
    	return "qcTool";
    }

    public String performQc() {
    	superRole=false;
    	setCollectionBox(false);
    	String[] defaultCheckBoxLable = { "Not Yet Reviewed"  };
    	buttonLabel="Update";
        setSelectedQcStatus(defaultCheckBoxLable);
        setFromDate(null);
        setToDate(null);
    	return "qcTool";
    }

	public boolean isSuperRole() {
		return superRole;
	}

	public void setSuperRole(boolean superRole) {
		this.superRole = superRole;
	}

	public String getSelectedCollectionSite() {
		return selectedCollectionSite;
	}

	public void setSelectedCollectionSite(String selectedCollectionSite) {
		this.selectedCollectionSite = selectedCollectionSite;
	}

	public List<String> getAuthCollectionList(){
		return 	collectionNames;
	}
	
	
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
	
	/**
     * This is to help workaround the timezone stuff in the calendar
     * that uses GMT and causes days to be off
     */
    public TimeZone getDefaultTimeZone() {
        return TimeZone.getDefault();
    }
    
    /**
     * Validate the date fields.  The dates should already
     * be validated individually by the calender component
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


	/////////////////////////////////////PRIVATE////////////////////////////////
    private List<SelectItem> authorizedProjectsSitesSelectItems = new ArrayList<SelectItem>();

    /**
     * Holds values for selection boxes
     */
    private List<SelectItem> collectionItems = new ArrayList<SelectItem>();
    private String selectedPatients = null;
    private String[] selectedQcStatus;
    private boolean superRole=false;
    private String buttonLabel="Update";
    private List<String> collectionNames = new ArrayList<String>();
    private String selectedCollectionSite;
    private Date fromDate;
    private Date toDate;


}


