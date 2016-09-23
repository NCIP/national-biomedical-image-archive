/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.searchresult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
 * 
 * <p>Note: this class has a natural ordering that is inconsistent with equals.
 */
public class PatientSearchResultImpl implements PatientSearchResult {


    public PatientSearchResultImpl() {
    }

    /**
     * Set the DICOM subject/patient id.
     */
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    

    /**
     * {@inheritDoc}
     */
    public String getSubjectId() {
        return subjectId;
    }


    /**
     * Set the project/collection for this patient.
     */
    public void setProject(String project) {
        this.project = project;
    }

    
    /**
     * {@inheritDoc}
     */
    public String getProject() {
        return project;
    }


    /**
     * Set the (pk) id for this patient.     
     */
    public void setId(Integer i) {
        id = i;
    }

    
    /**
     * {@inheritDoc}
     */
    public Integer getId() {
        return id;
    }
    
    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */     
    public StudyIdentifiers getStudyIdentifiers(int i) {
        return this.arr.get(i);
    }

    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */      
    public void setStudyIdentifiers(int i, StudyIdentifiers _value) {
        this.arr.set(i, _value);
    }

    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */      
    public void setStudyIdentifiers(StudyIdentifiers[] value) {
    	arr = new ArrayList<StudyIdentifiers>(Arrays.asList(value));
    }

    /**
     * {@inheritDoc}
     */
    public StudyIdentifiers[] getStudyIdentifiers() {
    	return arr.toArray(new StudyIdentifiers[]{});
    }

    /**
     * Set the toal number of studies in this patient.
     */
    public void setTotalNumberOfStudies(Integer numberStudies) {
        this.totalNumberOfStudies = numberStudies;
    }


    /**
     * Set the toal number of series in this patient.
     */    
    public void setTotalNumberOfSeries(Integer seriesNumber) {
        this.totalNumberOfSeries = seriesNumber;
    }

    
    /**
     * {@inheritDoc}
     */
    public Integer getTotalNumberOfSeries() {
        return totalNumberOfSeries;
    }

    
    /**
     * {@inheritDoc}
     */
    public Integer getTotalNumberOfStudies() {
        return totalNumberOfStudies;
    }

    
    /**
     * {@inheritDoc}
     */
    public Integer computeFilteredNumberOfStudies() {
        return arr.size();
    }

    
    /**
     * Adds a series to the list of series for this patient.
     *
     * <p>nimpy - think about simplifying this
     * 
     * @param studyId - the ID of the study that the patient belongs to
     * @param seriesId - the ID of the seriess
     */
    public void addSeriesForStudy(Integer studyId, Integer seriesId) {
    	StudyIdentifiers found = null;
    	for(StudyIdentifiers studyIdentifiers : arr) {
    		if(studyIdentifiers.getStudyIdentifier()==studyId.intValue()) {
    			found = studyIdentifiers;
    			break;
    		}
    	}

    	if(found==null) {
    		Integer[] seriesIds = new Integer[1];
    		seriesIds[0] = seriesId;

    		StudyIdentifiers newStudyIdentifiers = new StudyIdentifiers();
    		newStudyIdentifiers.setStudyIdentifier(studyId);
    		newStudyIdentifiers.setSeriesIdentifiers(seriesIds);

    		arr.add(newStudyIdentifiers);
    	}
    	else {
    		found.addSeriesIdentifier(seriesId);
    	}
    }

    
    /**
     * {@inheritDoc}
     */
    public Integer computeFilteredNumberOfSeries() {
        int seriesCount = 0;
        // Add up the series for each study
        for (StudyIdentifiers studyIdentifiers : arr) {
            seriesCount += studyIdentifiers.getSeriesIdentifiers().length;
        }

        return seriesCount;
    }

    
    /**
     * {@inheritDoc}
     */
    public Set<Integer> computeListOfSeriesIds() {
        Set<Integer> returnList = new HashSet<Integer>();
        
        // Create a list of all series for all studies
        for (StudyIdentifiers studyIdentifiers : arr) {
            returnList.addAll(Arrays.asList(studyIdentifiers.getSeriesIdentifiers()));
        }

        return returnList;
    }


    /**
     * {@inheritDoc}
     */
    public int compareTo(PatientSearchResult o) {
        // Patients shoiuld be ordered according to the subject ID
        return this.getSubjectId().compareTo(o.getSubjectId());
    }


	///////////////////////////////////////////PRIVATE//////////////////////////////////////

    private String subjectId;
    private String project;
    private Integer id;
    private List<StudyIdentifiers> arr = new ArrayList<StudyIdentifiers>();

    private Integer totalNumberOfStudies;
    private Integer totalNumberOfSeries;
}
