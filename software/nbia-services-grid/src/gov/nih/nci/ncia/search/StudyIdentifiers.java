/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This object correlates a study id to a collection of series ids.
 * An array of these is in the patient search result to enable drill down.
 */
public class StudyIdentifiers {
	
	/**
	 * Returns the value of StudySearchResult.getId for the study.
	 */
	public int getStudyIdentifier() {
		return studyIdentifier;
	}

	
	/**
	 * The study to be correlated.
	 */
	public void setStudyIdentifier(int studyIdentifier) {
		this.studyIdentifier = studyIdentifier;
	}
	
	
	/**
	 * Returns the values of SeriesSearchResult.getId for the series in this study.
	 * Arrays are more friendly for web services...
	 */
    public Integer[] getSeriesIdentifiers() {
        return seriesIdentifiers.toArray(new Integer[]{});
    }

    
    public void setSeriesIdentifiers(Integer[] seriesIdentifiers) {
        this.seriesIdentifiers = new ArrayList<Integer>(Arrays.asList(seriesIdentifiers));
    }
    
    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */    
    public Integer getSeriesIdentifiers(int i) {
        return this.seriesIdentifiers.get(i);
    }

    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */    
    public void setSeriesIdentifiers(int i, Integer _value) {
        this.seriesIdentifiers.set(i, _value);
    }  
    
    
    /**
     * Add another series id for this study.
     */
    public void addSeriesIdentifier(int value) {
    	this.seriesIdentifiers.add(value);
    }
    
    /////////////////////////////////PRIVATE//////////////////////////////////
    
    private List<Integer> seriesIdentifiers = new ArrayList<Integer>();
    
    private int studyIdentifier;
}
