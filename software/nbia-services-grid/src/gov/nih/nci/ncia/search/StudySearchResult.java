/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;

import java.util.Date;

/**
 * Represents a study search result (drill down from a patient search result)
 * 
 * <P>Lines up with a StudyDTO, but with extra stuff not found in db.
 */
public interface StudySearchResult extends Comparable<StudySearchResult> {
	/**
	 * This should identify a study uniquely at a given node.
	 * 
	 * <P>Likely implementation is a pkid from the database.
	 */
	public Integer getId();
	
	
	/**
	 * The DICOM study instance uid for this study.
	 * Example: "1.3.4.x.y.z"
	 */	
    public String getStudyInstanceUid();

    
    /**
     * The date for the study which is the day all the series happened on?
     * What about series on different day? (DICOM question).
     */
    public Date getDate();

    
    /**
     * The study description from the DICOM tags.
     */
    public String getDescription();

    
    /**
     * All the series in the study ordered by series number.
     * 
     * <P>Null series numbers come first in ordering (I think).
     */
    public SeriesSearchResult[] getSeriesList();
   
    
    /**
     * A string that describes the time elapsed between this study
     * and the first study of a given patient. (Like 2 months, 3 days, etc.)
     * 
     * <P>This would be better if we store the value, and let the 
     * presentation figure out the proper string based upon the value.
     */
	public String getOffSetDesc();
	
	    
    /**
     * The node that this study was found on.
     * 
	 * <p>This is intentionally not a property to avoid serialization.
     */
	public NBIANode associatedLocation();
	
	
	/**
	 * Associate a node with this result.  This should only be called once
	 * by a result generator.
	 * 
	 * <p>This is intentionally not a property to avoid serialization.
	 */
	public void associateLocation(NBIANode nbiaNode);
}
