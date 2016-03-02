/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.StudyNumberDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class StudyNumberDAOImpl extends AbstractDAO 
                                implements StudyNumberDAO {

	@Transactional(propagation=Propagation.REQUIRED)
	public Map<Integer, StudyNumberDTO> findAllStudyNumbers() throws DataAccessException {
		String hql =
	    	"select id, patientId, project, seriesNumber, studyNumber from StudyNumber";


        List<Object[]> resultSetList = (List<Object[]>)getHibernateTemplate().find(hql);

        Map<Integer, StudyNumberDTO> studyNumberMap = new HashMap<Integer, StudyNumberDTO>();


        for (Object[] result : resultSetList) {


            StudyNumberDTO studyNumber = new StudyNumberDTO((Integer) result[0],
                                                            (String) result[1],
                                                            (String) result[2],
                                                            (Integer) result[4],
                                                            (Integer) result[3]);
            studyNumberMap.put(studyNumber.getPatientPkId(), studyNumber);
        }

        return studyNumberMap;
	}
}
