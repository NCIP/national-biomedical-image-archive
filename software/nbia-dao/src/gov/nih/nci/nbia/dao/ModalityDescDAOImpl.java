/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ModalityDescDTO;
import gov.nih.nci.nbia.internaldomain.ModalityDesc;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * handle query for Editing Modality description feature
 * @author prakasht
 *
 */
public class ModalityDescDAOImpl extends AbstractDAO implements ModalityDescDAO{

	@Transactional(propagation=Propagation.REQUIRED)
	public ModalityDescDTO findModalityDescByModalityName(String modalityName) throws DataAccessException{
		ModalityDescDTO dto = new ModalityDescDTO();
		dto.setModalityName(modalityName);
  
        DetachedCriteria criteria = DetachedCriteria.forClass(ModalityDesc.class);
        criteria.add(Restrictions.eq("modalityName", modalityName));
        List<ModalityDesc> modalityDescList = getHibernateTemplate().findByCriteria(criteria);

		if(modalityDescList != null && modalityDescList.size() == 1)	{
			ModalityDesc c = modalityDescList.get(0);
			dto.setDescription(c.getDescription());
			dto.setId(c.getId());
			//calling this from save() causes duplicate object in session
			//might be better to rework this so the find can just return
			//that object and save update that instead of re-creating
			getHibernateTemplate().evict(c);
		}
		return dto;	
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<ModalityDescDTO> findAllModalityDesc() throws DataAccessException{
        List<ModalityDesc> modalityDescList = getHibernateTemplate().loadAll(ModalityDesc.class);
        
        List<ModalityDescDTO> modalityDescDTOList = new ArrayList<ModalityDescDTO>();
        for( ModalityDesc modalityDesc: modalityDescList) {
        	ModalityDescDTO dto = new ModalityDescDTO();
			dto.setId(modalityDesc.getId());
			dto.setModalityName(modalityDesc.getModalityName());
        	dto.setDescription(modalityDesc.getDescription());
			
			modalityDescDTOList.add(dto);
        }

		return modalityDescDTOList;	
	}
}
