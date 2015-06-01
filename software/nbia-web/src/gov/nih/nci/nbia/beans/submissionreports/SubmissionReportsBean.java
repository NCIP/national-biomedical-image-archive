/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.submissionreports;

import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.DateValidator;
import gov.nih.nci.nbia.util.SelectItemComparator;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.verifysubmission.VerifySubmissionUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * This bean captures the input controls for submission reports
 * including the date range and collection/site choice.
 */
public class SubmissionReportsBean {


    public SubmissionReportsBean() {
        // get Site information and Handle authorization
        SecurityBean secure = BeanManager.getSecurityBean();
        AuthorizationManager am = secure.getAuthorizationManager();
        List<SiteData> authorizedSites = am.getAuthorizedSites(RoleType.VIEW_SUBMISSION_REPORT);

        for(int i=0; i<authorizedSites.size(); i++){
            authorizedProjectsSitesSelectItems.add(new SelectItem(VerifySubmissionUtil.siteDataToString(authorizedSites.get(i))));
        }

        Collections.sort(authorizedProjectsSitesSelectItems,
                         new SelectItemComparator());

        if(authorizedProjectsSitesSelectItems.size()> 0){
            selectedCollectionSite = (String)authorizedProjectsSitesSelectItems.get(0).getValue();
        }
        else {
            selectedCollectionSite = null;
        }

        //initialize date value
        initializeDates();
    }

    /**
     * Just a convenience method for the display.  Don't do
     * any thing with this except show it.  Instead stick to
     * using Date objects
     */
    public String getDateToString() {
        return VerifySubmissionUtil.dateFormat(dateTo);
    }

    /**
     * Just a convenience method for the display.  Don't do
     * any thing with this except show it.  Instead stick to
     * using Date objects
     */
    public String getDateFromString() {
        return VerifySubmissionUtil.dateFormat(dateFrom);
    }

    /**
     * This property is for the user's selection of a start to the report date range
     */
    public Date getDateFrom() {
        return dateFrom;
    }


    /**
     * This property is for the user's selection of a start to the report date range
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }


    /**
     * This property is for the user's selection of an end to the report date range
     */
    public Date getDateTo() {
        return dateTo;
    }


    /**
     * This property is for the user's selection of an end to the report date range
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
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
        DateValidator dateValidator = new DateValidator();
        String result = dateValidator.validateDates(dateFrom, dateTo, true) ;

        if(result!=null) {
            FacesMessage facesMessage = new FacesMessage("Date Invalid: "+result);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("whocares", facesMessage);
        }
        return result;
    }

    /**
     * This is the list of project+sites the user can see.  Show them
     * in a drop down or something.
     */
    public List<SelectItem> getAuthorizedProjectsSitesSelectItems() {
        return authorizedProjectsSitesSelectItems;
    }


    /**
     * This property is for the user's selection of a collection+site
     */
    public void setSelectedCollectionSite(String selectedCollectionSite) {
        this.selectedCollectionSite = selectedCollectionSite;
    }


    /**
     * This property is for the user's selection of a collection+site
     */
    public String getSelectedCollectionSite() {
        return this.selectedCollectionSite;
    }

    /////////////////////////////////////PRIVATE////////////////////////////////

    private Date dateFrom;

    private Date dateTo;

    private List<SelectItem> authorizedProjectsSitesSelectItems = new ArrayList<SelectItem>();

    private String selectedCollectionSite;

    private void initializeDates(){
        dateTo = new Date();
        Calendar today = new GregorianCalendar();
        Calendar threeMonthsAgo = (Calendar)today.clone();
        threeMonthsAgo.add(Calendar.MONTH, -3);

        dateFrom = threeMonthsAgo.getTime();
    }
}
