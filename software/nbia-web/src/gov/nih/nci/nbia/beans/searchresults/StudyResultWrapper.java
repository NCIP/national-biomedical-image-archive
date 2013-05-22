/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchresults;

import gov.nih.nci.nbia.util.UidDisplayUtil;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.ncia.search.StudySearchResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudyResultWrapper {
	public StudyResultWrapper(StudySearchResult study) {
		this.study = study;
		
		seriesResults = new ArrayList<SeriesResultWrapper>();
		SeriesSearchResult[] seriesList = study.getSeriesList();
		for(SeriesSearchResult series : seriesList) {
			seriesResults.add(new SeriesResultWrapper(series));
		}
	}
	
	public String getLocation() {
		return study.associatedLocation().getDisplayName();
	}
	
	public boolean isLocal() {
		return study.associatedLocation().isLocal();
	}
	
	public String getDateString() {
		Date date = study.getDate();
	    if (date == null) {
	        return "";
	    }
	    SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy");
	    return sdf.format(date);
	}
	
	public String getStudyId() {
        return UidDisplayUtil.getDisplayUid(study.getStudyInstanceUid());
	}
	
	
	public StudySearchResult getStudy() {
		return study;
	}
	
	public List<SeriesResultWrapper> getSeriesResults() {
		return seriesResults;
	}
	
	
	private List<SeriesResultWrapper> seriesResults;
	
	
	private StudySearchResult study; 
}