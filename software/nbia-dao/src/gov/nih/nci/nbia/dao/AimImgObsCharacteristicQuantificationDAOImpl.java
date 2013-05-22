/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ImgObsCharacteristicQuantificationDTO;
import gov.nih.nci.nbia.internaldomain.AimQuantification;
import gov.nih.nci.nbia.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

public class AimImgObsCharacteristicQuantificationDAOImpl extends AbstractDAO
                                                          implements AimImgObsCharacteristicQuantificationDAO {

	public Collection<String> findAllQuantificationNames() throws DataAccessException {

        DetachedCriteria criteria = DetachedCriteria.forClass(AimQuantification.class, "q");

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("name"));

        criteria.setProjection(Projections.distinct(projectionList));

        Collection<String> names = new ArrayList<String>();

        Collection<String> results = getHibernateTemplate().findByCriteria(criteria);
        for(String result : results) {
        	names.add(result);
        }
        return names;
	}

	public Collection<String> findAllValuesByName(String quantificationName) throws DataAccessException {

        DetachedCriteria criteria = DetachedCriteria.forClass(AimQuantification.class, "q");

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("value"));

        criteria.setProjection(Projections.distinct(projectionList));
        criteria.add(Restrictions.eq("name", quantificationName));

        Collection<String> values = new ArrayList<String>();

        Collection<String> results = getHibernateTemplate().findByCriteria(criteria);
        for(String result : results) {
        	values.add(result);
        }
        return values;
	}

    public Collection<ImgObsCharacteristicQuantificationDTO> findAllQuantifications() throws DataAccessException {

        Collection<ImgObsCharacteristicQuantificationDTO> dtos = new ArrayList<ImgObsCharacteristicQuantificationDTO>();

        Collection<String> names = findAllQuantificationNames();

        for(String name: names) {
			Collection<String> possibleValues = findAllValuesByName(name);

			ImgObsCharacteristicQuantificationDTO dto = new ImgObsCharacteristicQuantificationDTO(name,
			                                                                                      "Scale", //shortarm this for prototype
			                                                                                      new TreeSet<String>(Util.removeNullElement(possibleValues)));
			dtos.add(dto);
		}

        return dtos;
	}

}
