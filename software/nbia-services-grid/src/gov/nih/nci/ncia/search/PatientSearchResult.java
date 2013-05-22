/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;

import java.util.Set;

/**
 * This is the output of PatientSearcher.
 * It is used for the first page of results in the classic search.
 * 
 * <p>This is the artist formerly known as PatientDTO... but elevated
 * to a "business logic" concept with no "data access" baggage/confusion.
 * 
 * <P><b>WARNING!</b> This object is serialized so if you change it, you risk
 * breaking remote search and the grid interface
 */
public interface PatientSearchResult extends Comparable<PatientSearchResult> {
	
	/**
	 * This should identify a patient uniquely at a given node.
	 * 
	 * <P>Likely implementation is a pkid from the database.
	 */
	public Integer getId();
	
	/**
	 * The DICOM "patient id" for this patient.
	 * Example: "1.3.4.x.y.z"
	 */
    public String getSubjectId();
    
    /**
     * The project or collection this patient is in.
     * For example: IDRI, LIDC, etc.
     */
    public String getProject();

    /**
     * The total number of series this patient has.
     * 
     * <p>Patient X with study y with series m,n,o would return 3.
     */
    public Integer getTotalNumberOfSeries();

    /**
     * The total number of studies this patient has.
     * 
     * <p>Patient X with study y and z would return 2.
     */    
    public Integer getTotalNumberOfStudies();
    

    /**
     * Returns the number of studies for this patient that contain at least one
     * visible series matching the criteria
     */    
    public Integer computeFilteredNumberOfStudies();
    
    /**
     * Returns the number of series for this patient that contain at least one
     * visible series matching the criteria
     */    
    public Integer computeFilteredNumberOfSeries();

    /**
     * This contains a map of study identifiers to the series 
     * identifiers in that study.
     * 
     * <p>I might have dropped this... but the DICOMQueryHandler
     * which is ungodly complex starts off by finding out this
     * triple (patient-id,study-id,series-id), so just roll with it.  
     * The alternative is to just find patients and then ask a 
     * "drill down manager" to get study/series as necessary, which 
     * may or may not be a better approach (maybe better latency,
     * but not necessarily throughput?).
     */
    public StudyIdentifiers[] getStudyIdentifiers();
	
    /**
     * Convenience method to operate on {@link #getStudyIdentifiers}
     * Returns a list of all series IDs for all studies of this patient.
     */
    public Set<Integer> computeListOfSeriesIds();
    
    /**
     * The node that this patient was found on.
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
