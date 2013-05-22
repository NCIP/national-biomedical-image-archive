/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ImgObsCharacteristicDTO;
import gov.nih.nci.nbia.internaldomain.AimImagingObservationCharacteristic;

import java.util.ArrayList;
import java.util.Collection;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.springframework.dao.DataAccessException;

public class AimImgObsCharacteristicDAOImpl extends AbstractDAO
                                            implements AimImgObsCharacteristicDAO {

	public Collection<ImgObsCharacteristicDTO> findAllCodeMeaningNamesAndValuePairs() throws DataAccessException {

        DetachedCriteria criteria = DetachedCriteria.forClass(AimImagingObservationCharacteristic.class, "ct");

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("codeValue"));
        projectionList.add(Projections.property("codeMeaningName"));
        projectionList.add(Projections.property("codeSchemaDesignator"));

        criteria.setProjection(Projections.distinct(projectionList));

        Collection<ImgObsCharacteristicDTO> dtos = new ArrayList<ImgObsCharacteristicDTO>();

        Collection<Object[]> results = getHibernateTemplate().findByCriteria(criteria);
        for(Object[] result : results) {
        	ImgObsCharacteristicDTO dto = new ImgObsCharacteristicDTO((String)result[0],
        			                                                  (String)result[1],
        			                                                  (String)result[2]);
        	dtos.add(dto);
        }
        return dtos;
	}
}
