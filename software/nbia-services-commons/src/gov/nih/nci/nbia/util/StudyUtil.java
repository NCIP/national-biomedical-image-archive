/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.ncia.search.StudySearchResult;
import gov.nih.nci.ncia.search.StudySearchResultImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StudyUtil {

	public static class StudySeriesPair {
        public SeriesSearchResult seriesDto;
        public StudySearchResult studyDto;
	}

	public static StudySeriesPair findFirstMatchingStudyForSeries(StudySearchResult[] studies,
		                                                          int seriesPkId) {

		for (StudySearchResult study : studies) {
			SeriesSearchResult[] seriesDtos = study.getSeriesList();

			for (SeriesSearchResult series : seriesDtos) {
				if (series.getId().equals(seriesPkId)) {
					StudySeriesPair studySeriesPair = new StudySeriesPair();
					studySeriesPair.seriesDto = series;
					studySeriesPair.studyDto = study;
					return studySeriesPair;
				}
			}
		}
		return null;
	}

	public static PatientSearchResult findLastMatchingPatientForSeries(Collection<PatientSearchResult> patients,
			                                                           int seriesPkId) {

		PatientSearchResult matchingPatient = null;
		for (PatientSearchResult patient : patients) {
			if (patient.computeListOfSeriesIds().contains(seriesPkId)) {
				//this.patient = patient;
				matchingPatient = patient;
			}
		}

		return matchingPatient;
	}

	public static List<StudySearchResultImpl> calculateOffsetValues(List<StudySearchResultImpl> studyResults) {
		List<StudySearchResultImpl> studyWithOffsetValues = new ArrayList<StudySearchResultImpl>();
		if(!studyResults.isEmpty()){
			StudySearchResultImpl sdto0 = studyResults.get(0);
			studyWithOffsetValues.add(sdto0);
		    //sdto0.setOffsetMonth(0);

			for(int i=1, n=studyResults.size(); i<n; i++){
				StudySearchResultImpl sdto = studyResults.get(i);
				String offsetValue = Util.calculateOffsetValue(sdto0.getDate(),
						                                       sdto.getDate());

				sdto.setOffSetDesc(offsetValue);
				studyWithOffsetValues.add(sdto);
			}
		}
		return studyWithOffsetValues;
	}
}
