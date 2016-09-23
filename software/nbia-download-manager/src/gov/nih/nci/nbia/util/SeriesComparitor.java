package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.download.SeriesData;

import java.util.Comparator;

public class SeriesComparitor implements Comparator<SeriesData>{
	public int compare(SeriesData s1, SeriesData s2) {
		if (s1==null||s2==null) return 0;
		if (s1.getPatientId().compareTo(s2.getPatientId())!=0) return s1.getPatientId().compareTo(s2.getPatientId());
		//same patient
		if (s1.getStudyInstanceUid().compareTo(s2.getStudyInstanceUid())!=0) return s1.getStudyInstanceUid().compareTo(s2.getStudyInstanceUid());
		//same study
		return s1.getSeriesInstanceUid().compareTo(s2.getSeriesInstanceUid());
	}
}
