/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.submissionreports;

/**
 * This object is basically a row in the patient details
 * table.  The decomposition of this object and PatientDetailGroupWrapper
 * may be somewhat dubious.  Just about all the code cares about
 * the "group" wrapper....
 */
public class PatientDetailWrapper  {


	public PatientDetailWrapper(String patient,
			                    String study,
			                    String series,
			                    String img) {
		super();
		this.patient = patient;
		this.study = study;
		this.series = series;
		this.img = img;
	}

	/**
	 * This is always a patient id
	 */
	public String getPatient() {
		return patient;
	}

	/**
	 * This is either a study id or a count of studies for the patient
	 */
	public String getStudy() {
		return study;
	}

	/**
	 * This is either a series id or a count of series for the patient
	 */
	public String getSeries() {
		return series;
	}

	/**
	 * This is a count of images or submissions (if annotation)
	 */
	public String getImg() {
		return img;
	}

	/////////////////////////////////PRIVATE////////////////////////////

	private String patient;
	private String study;
	private String series;
	private String img;

}