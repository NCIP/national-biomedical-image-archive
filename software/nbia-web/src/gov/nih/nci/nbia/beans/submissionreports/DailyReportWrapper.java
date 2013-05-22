/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.submissionreports;

import java.util.List;

/**
 * This wrapper wraps the annotation or image report for a given day.  It includes
 * presentation aspects like whether it is expanded/collapsed.
 */
public class DailyReportWrapper {
	public DailyReportWrapper(List<PatientDetailGroupWrapper> patientDetails,
			                  int submissionCount) {
		this.patientDetails = patientDetails;
		this.submissionCount = submissionCount;
	}

	public boolean isReportEmpty() {
		return patientDetails==null;
	}

	public int getSubmissionCount() {
		return submissionCount;
	}

	public List<PatientDetailGroupWrapper> getPatientDetails() {
		return patientDetails;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean b){
		this.expanded = b;
	}


	//////////////////////////////PRIVATE/////////////////////////////////
	private boolean expanded = false;

	private int submissionCount;

	private List<PatientDetailGroupWrapper> patientDetails;
}
