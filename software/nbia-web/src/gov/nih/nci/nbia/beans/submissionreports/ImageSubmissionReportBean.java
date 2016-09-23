/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.submissionreports;

import gov.nih.nci.nbia.verifysubmission.ImageSubmissionReport;
import gov.nih.nci.nbia.verifysubmission.ImageSubmissionReportGenerator;
import gov.nih.nci.nbia.verifysubmission.PatientDetails;
import gov.nih.nci.nbia.verifysubmission.VerifySubmissionUtil;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

/**
 * This bean is used to generate the annotation submission report and store
 * it for viewing in the UI.
 *
 * <p>This bean relies upon VerifySubmissionBean to manage the
 * selection criteria for the report (date range, collection//site).
 */
public class ImageSubmissionReportBean {

    /**
     * This is the overall annotation submission report for the total
     * time frame (as opposed to the per day report).
     */
    public ImageSubmissionReport getImageSubmissionReport() {
        return imageSubmissionReport;
    }

    /**
     * This is the start range of the report - but this is for the results
     * as opposed to the input controls.  This has to be distinct because
     * results can be showing while user is tweak range controls - causing
     * results to be updated by ajax and show incorrect value
     */
    public String getDateFromString() {
        return VerifySubmissionUtil.dateFormat(imageSubmissionReport.getFrom());
    }

    /**
     * This is the end range of the report - but this is for the results
     * as opposed to the input controls.  This has to be distinct because
     * results can be showing while user is tweak range controls - causing
     * results to be updated by ajax and show incorrect value
     */
    public String getDateToString() {
        return VerifySubmissionUtil.dateFormat(imageSubmissionReport.getTo());
    }

    /**
     * This is the collcetion+site of the report - but this is for the results
     * as opposed to the input controls.  This has to be distinct because
     * results can be showing while user is tweak range controls - causing
     * results to be updated by ajax and show incorrect value
     */
    public String getSelectedCollectionSite() {
    	return imageSubmissionReport.getCollectionSite();
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

        imageSubmissionReport = reportGenerator.generateReport(startDate,
                                                               endDate,
                                                               projectName,
                                                               siteName);


        initDailyReportMap(imageSubmissionReport.getSubmissionDays());
        initDailyCorrectedReportMap(imageSubmissionReport.getCorrectedSubmissionDays());

        return "displayImageSubmissionReport";
    }


    /**
     * Date -> (New) Image Submission Report per day mapping
     *
     * <p>The String is a day in the format mm-dd-yyyy
     */
    public Set<Map.Entry<String, DailyReportWrapper>> getDailyReportDetails() {
        if(dailyReportMap!=null) {
            return dailyReportMap.entrySet();
        }
        else {
            return null;
        }
    }

    /**
     * Date -> (Cprrected) Image Submission Report per day mapping
     *
     * <p>The String is a day in the format mm-dd-yyyy
     */
    public Set<Map.Entry<String, DailyReportWrapper>> getDailyCorrectedReportDetails() {
        if(dailyCorrectedReportMap!=null) {
            return dailyCorrectedReportMap.entrySet();
        }
        else {
            return null;
        }
    }

    /**
     * Action to drill down into the details for a day in the corrected tab.  Clicking
     * on the day fires this off
     */
    public void correctedDayDetailsClicked(ActionEvent actionEvent) throws Exception {
        UIComponent comp = actionEvent.getComponent();
        UICommand link = (UICommand)comp;

        Object value = link.getAttributes().get("day");
        String date = (String)value;

        DailyReportUtil.selectDateDetails(dailyCorrectedReportMap, date, correctedDailyReportFactory);
    }

    /**
     * Action to drill down into the details for a day in the "new" tab.  Clicking
     * on the day fires this off
     */
    public void dayDetailsClicked(ActionEvent actionEvent) throws Exception {
        UIComponent comp = actionEvent.getComponent();
        UICommand link = (UICommand)comp;

        Object value = link.getAttributes().get("day");
        String date = (String)value;

        DailyReportUtil.selectDateDetails(dailyReportMap, date, dailyReportFactory);
    }

    /**
     * Method to wire in the other bean that contains the input controls/values
     */
    public void setSubmissionReportsBean(SubmissionReportsBean submissionReportsBean) {
        this.submissionReportsBean = submissionReportsBean;
    }


    ////////////////////////////PRIVATE///////////////////////////////////////

    /**
     * The String key is a date of the accepted format
     */
    private Map<String, DailyReportWrapper> dailyReportMap;

    private Map<String, DailyReportWrapper> dailyCorrectedReportMap;

    private SubmissionReportsBean submissionReportsBean;

    private ImageSubmissionReport imageSubmissionReport;

    private ImageSubmissionReportGenerator reportGenerator = new ImageSubmissionReportGenerator();

    private DailyReportUtil.DailyReportWrapperFactory dailyReportFactory = new DailyReportFactory();

    private DailyReportUtil.DailyReportWrapperFactory correctedDailyReportFactory = new CorrectedDailyReportFactory();


    private void initDailyReportMap(SortedMap<Date, Integer> days) {
        dailyReportMap = new LinkedHashMap<String, DailyReportWrapper>(days.size());
        for(Map.Entry<Date,Integer> day : days.entrySet()) {
            dailyReportMap.put(VerifySubmissionUtil.dateFormat(day.getKey()),
                               new DailyReportWrapper(null, day.getValue()));
        }
    }

    private void initDailyCorrectedReportMap(SortedMap<Date, Integer> days) {
        dailyCorrectedReportMap = new LinkedHashMap<String, DailyReportWrapper>(days.size());
        for(Map.Entry<Date,Integer> day : days.entrySet()) {
            dailyCorrectedReportMap.put(VerifySubmissionUtil.dateFormat(day.getKey()),
                                        new DailyReportWrapper(null, day.getValue()));
        }
    }

    private class CorrectedDailyReportFactory implements DailyReportUtil.DailyReportWrapperFactory {
        public DailyReportWrapper create(String dayString, int submissionCount) throws Exception {
            Date day = VerifySubmissionUtil.dateParse(dayString);
            String collectionSite = submissionReportsBean.getSelectedCollectionSite();
            String projectName = VerifySubmissionUtil.getProject(collectionSite);
            String siteName = VerifySubmissionUtil.getSite(collectionSite);

            List<PatientDetails> dailyReport = reportGenerator.generateCorrectedReportByDay(day,
                                                                                            projectName,
                                                                                            siteName);
            return new DailyReportWrapper(DailyReportUtil.convert(dailyReport),
                                          submissionCount);
        }
    }
    private class DailyReportFactory implements DailyReportUtil.DailyReportWrapperFactory {
        public DailyReportWrapper create(String dayString, int submissionCount) throws Exception {

            Date day = VerifySubmissionUtil.dateParse(dayString);
            String collectionSite = submissionReportsBean.getSelectedCollectionSite();
            String projectName = VerifySubmissionUtil.getProject(collectionSite);
            String siteName = VerifySubmissionUtil.getSite(collectionSite);


            List<PatientDetails> dailyReport = reportGenerator.generateAffectedReportByDay(day,
                                                                                           projectName,
                                                                                           siteName);
            return new DailyReportWrapper(DailyReportUtil.convert(dailyReport),
                                          submissionCount);
        }
    }
}
