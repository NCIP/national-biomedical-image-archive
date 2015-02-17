/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.lookup;

import gov.nih.nci.nbia.dao.StudyNumberDAO;
import gov.nih.nci.nbia.dto.StudyNumberDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.Map;

/**
 * Maps patient primary key to a StudyNumber object.
 *
 * <p>This is implicitly a singleton because it is an attribute of the ApplicationFactory.
 *
 * <p>The first time a search is done, this map is loaded up and used until the app goes down,
 * so any changes to the STUDY_SERIES_NUMBER view which underlies the StudyNumber toolkit object
 * won't be captured (is this on purpose?).  Further, this loads the whole StudyNumber/STUDY_SERIES_NUMBER
 * view into memory.....
 *
 */
public class StudyNumberMap {

    
    public StudyNumberMap()  {
        refreshStudiesForPatient();
    }


    /**
     * Holds a cached HashMap of all the domain objects that have a patient and
     * the total number of studies and series. Will update the cache if the
     * patient is not found the first time, throws an exception the second time.
     */
    public StudyNumberDTO getStudiesForPatient(Integer patientPkId) throws Exception {
        //Force to retrieve total studies/Series numbers If call refresh here. 
    	//refreshStudiesForPatient();
       
    	StudyNumberDTO temp = studyNumberMap.get(patientPkId);
        if (temp == null) {
            refreshStudiesForPatient();
            temp = studyNumberMap.get(patientPkId);

            if (temp == null) {
            	throw new Exception("Patient PK ID does not exist.");
            }
        }

        return temp;
    }

    public Map<Integer, StudyNumberDTO> getStudyNumberMap() {
        return studyNumberMap;
    }
    

    ////////////////////////////////////////PRIVATE/////////////////////////////////////
    
    private Map<Integer, StudyNumberDTO> studyNumberMap = null;
    private void refreshStudiesForPatient() {

    	StudyNumberDAO studyNumberDAO = (StudyNumberDAO)SpringApplicationContext.getBean("studyNumberDAO");
        studyNumberMap = studyNumberDAO.findAllStudyNumbers();
    }    
}
