/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.submissionreports;

import gov.nih.nci.nbia.verifysubmission.PatientDetails;
import gov.nih.nci.nbia.verifysubmission.SeriesDetails;
import gov.nih.nci.nbia.verifysubmission.StudyDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Random operations that can be reused across the different reports for
 * the daily report details.  So annotation and image reports both rely on this.
 *
 * <p>(package visibility on purpose)
 */
class DailyReportUtil {

    /*pkg*/ interface DailyReportWrapperFactory {
        public DailyReportWrapper create(String dateString, int submissionCount) throws Exception;
    }

    /**
     * Take a list of PatientDetails objects and turn each into a wrapper object
     * suitable for use when displaying the patient details on a page (with
     * the expandable table)
     *
     * <p>(package visibility on purpose)
     */
    static List<PatientDetailGroupWrapper> convert(List<PatientDetails> patientDetailsList) {

       	List<PatientDetailGroupWrapper> topLevelWrapperList = new ArrayList<PatientDetailGroupWrapper>();
    	for(PatientDetails patientDetails : patientDetailsList) {
    		PatientDetailGroupWrapper patientDetailWrapper =
    			new PatientDetailGroupWrapper(patientDetails.getPatientId(),
    					                      Integer.toString(patientDetails.getStudyCount()),
    					                      Integer.toString(patientDetails.getSeriesCount()),
    					                      Integer.toString(patientDetails.getSubmissionCount()),
    					                      topLevelWrapperList);

    		topLevelWrapperList.add(patientDetailWrapper);


    		for(StudyDetails studyDetails : patientDetails.getStudyDetails()) {
        		PatientDetailGroupWrapper studyWrapper = new PatientDetailGroupWrapper("",
        				                                                               studyDetails.getStudyInstanceUid(),
        				                                                               Integer.toString(studyDetails.getSeriesCount()),
        				                                                               Integer.toString(studyDetails.getSubmissionCount()),
        				                                                               topLevelWrapperList);


        		patientDetailWrapper.getChildren().add(studyWrapper);

        		for(SeriesDetails seriesDetails : studyDetails.getSeriesDetails()) {
            		PatientDetailGroupWrapper sWrapper = new PatientDetailGroupWrapper("",
            				                                                           "",
            				                                                           seriesDetails.getSeriesInstanceUid(),
            				                                                           Long.toString(seriesDetails.getSubmissionCount()),
            				                                                           topLevelWrapperList);

            		studyWrapper.getChildren().add(sWrapper);
        		}
    		}
    	}
    	return topLevelWrapperList;
    }

    /**
     * Utiltiy method used by image and annotation details presentation....
     * called when the details are clicked on.... collapse/expand the
     * details if we already have them in memory, otherwise delegate getting
     * the details to a factory, and then expand.
     *
     * <p>(package visibility on purpose)
     */
    static void selectDateDetails(Map<String,DailyReportWrapper> dailyReportMap,
    		                      String dayString,
    		                      DailyReportWrapperFactory reportFactory) throws Exception {
        DailyReportWrapper dailyReport = dailyReportMap.get(dayString);

        if(dailyReport.isReportEmpty()) {
            DailyReportWrapper dailyImageReport = reportFactory.create(dayString,
            		                                                   dailyReport.getSubmissionCount());
            dailyReportMap.put(dayString, dailyImageReport);
            dailyImageReport.setExpanded(true);
        }
        else {
        	dailyReport.setExpanded(!dailyReport.isExpanded());
        }
    }
}
