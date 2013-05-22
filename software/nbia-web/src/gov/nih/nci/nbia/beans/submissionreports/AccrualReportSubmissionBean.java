/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: VerifySubmissionBean.java 8078 2009-04-20 16:35:07Z lethai $
*
* $Log: not supported by cvs2svn $
* Revision 1.8  2007/06/25 17:08:49  lethai
* verify submission enhancement
*
* Revision 1.7  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.beans.submissionreports;

import gov.nih.nci.nbia.verifysubmission.AccrualReport;
import gov.nih.nci.nbia.verifysubmission.AccrualReportGenerator;
import gov.nih.nci.nbia.verifysubmission.VerifySubmissionUtil;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

/**
 * This bean is used to generate the accrual report and store
 * it for viewing in the UI.
 *
 * <p>This bean relies upon VerifySubmissionBean to manage the
 * selection criteria for the report (date range, collection//site).
 */
public class AccrualReportSubmissionBean {

    /**
     * This is the overall accrual report for the total
     * time frame (as opposed to the per day report).
     */
    public AccrualReport getAccrualReport() {
        return accrualReport;
    }

    /**
     * This is the start range of the report - but this is for the results
     * as opposed to the input controls.  This has to be distinct because
     * results can be showing while user is tweak range controls - causing
     * results to be updated by ajax and show incorrect value
     */
    public String getDateFromString() {
        return VerifySubmissionUtil.dateFormat(accrualReport.getFrom());
    }

    /**
     * This is the end range of the report - but this is for the results
     * as opposed to the input controls.  This has to be distinct because
     * results can be showing while user is tweak range controls - causing
     * results to be updated by ajax and show incorrect value
     */
    public String getDateToString() {
        return VerifySubmissionUtil.dateFormat(accrualReport.getTo());
    }

    /**
     * This is the collcetion+site of the report - but this is for the results
     * as opposed to the input controls.  This has to be distinct because
     * results can be showing while user is tweak range controls - causing
     * results to be updated by ajax and show incorrect value
     */
    public String getSelectedCollectionSite() {
    	return accrualReport.getCollectionSite();
    }

    /**
     * This action is called when the submit button is clicked.
     */
    public String submit() throws Exception {
    	if(submissionReportsBean.validateDates()!=null) {
    		return null;
    	}
    	Date startDate = submissionReportsBean.getDateFrom();
    	Date endDate = submissionReportsBean.getDateTo();
    	String collectionSite = submissionReportsBean.getSelectedCollectionSite();
    	String projectName = VerifySubmissionUtil.getProject(collectionSite);
    	String siteName = VerifySubmissionUtil.getSite(collectionSite);

    	accrualReport = reportGenerator.generateReport(startDate,
    			                                       endDate,
    			                                       projectName,
    			                                       siteName);


    	Map<Date,Integer> days = accrualReport.getAccrualDays();
    	initDailyReportMap(days);

        return "displayAccrualResults";
    }

    /**
     * Date -> Accrual Report per day mapping
     *
     * <p>The String is a day in the format mm-dd-yyyy
     */
    public Set<Map.Entry<String, AccrualReportWrapper>> getDailyReportDetails() {
    	if(dailyReportMap!=null) {
    		return dailyReportMap.entrySet();
    	}
    	else {
    		return null;
    	}
    }


    /**
     * Action to drill down into the details for a day.  Clicking
     * on the day fires this off
     */
    public void dayDetailsClicked(ActionEvent actionEvent) throws Exception {
    	UIComponent comp = actionEvent.getComponent();
    	UICommand link = (UICommand)comp;

    	Object value = link.getAttributes().get("day");
    	String date = (String)value;

    	AccrualReportWrapper existingDailyReport = dailyReportMap.get(date);
    	if(existingDailyReport.isReportEmpty()) {
    		AccrualReportWrapper dailyAccrualReport = constructDailyAccrualReport(date,
    				                                                              existingDailyReport.getSubmissionCount());
    		dailyAccrualReport.setExpanded(true);
    		dailyReportMap.put(date, dailyAccrualReport);
    	}
    	else {
    		existingDailyReport.setExpanded(!existingDailyReport.isExpanded());
    	}
    }

    /**
     * Method to wire in the other bean that contains the input controls/values
     */
	public void setSubmissionReportsBean(SubmissionReportsBean submissionReportsBean) {
		this.submissionReportsBean = submissionReportsBean;
	}

	/**
	 * This wrapper wraps the accrual report for a given day.  It includes
	 * presentation aspects like whether it is expanded/collapsed.
	 */
    public static class AccrualReportWrapper {
    	public AccrualReportWrapper(AccrualReport accrualReport,
    			                    int submissionCount) {
    		this.accrualReport = accrualReport;
    		this.submissionCount = submissionCount;

    	}
    	public boolean isReportEmpty() {
    		return accrualReport==null;
    	}

    	public int getSubmissionCount() {
    		return submissionCount;
    	}

    	public AccrualReport getAccrualReport() {
    		return accrualReport;
    	}

    	public boolean isExpanded() {
    		return expanded;
    	}

    	public void setExpanded(boolean b){
    		this.expanded = b;
    	}
    	private boolean expanded = false;
    	private AccrualReport accrualReport;
    	private int submissionCount;
    }


    ////////////////////////////PRIVATE///////////////////////////////////////

    /**
     * The String key is a date of the accepted format
     */
    private Map<String, AccrualReportWrapper> dailyReportMap;

    private SubmissionReportsBean submissionReportsBean;

    private AccrualReport accrualReport;

	private AccrualReportGenerator reportGenerator = new AccrualReportGenerator();

    private AccrualReportWrapper constructDailyAccrualReport(String dayString,
    		                                                 int submissionCount) throws Exception {
    	Date day = VerifySubmissionUtil.dateParse(dayString);
    	String collectionSite = submissionReportsBean.getSelectedCollectionSite();
    	String projectName = VerifySubmissionUtil.getProject(collectionSite);
    	String siteName = VerifySubmissionUtil.getSite(collectionSite);

    	AccrualReport dailyReport = reportGenerator.generateReportByDay(day,
    			                                                        projectName,
    			                                                        siteName);
    	return new AccrualReportWrapper(dailyReport,
    			                        submissionCount);
    }

    private void initDailyReportMap(Map<Date, Integer> days) {
    	dailyReportMap = new LinkedHashMap<String, AccrualReportWrapper>(days.size());
        for(Map.Entry<Date,Integer> day : days.entrySet()) {
        	dailyReportMap.put(VerifySubmissionUtil.dateFormat(day.getKey()),
        			           new AccrualReportWrapper(null, day.getValue()));
        }

    }
}
