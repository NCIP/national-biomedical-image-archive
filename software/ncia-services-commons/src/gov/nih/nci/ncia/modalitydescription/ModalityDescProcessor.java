/**
 * 
 */
package gov.nih.nci.ncia.modalitydescription;

import gov.nih.nci.ncia.dao.ModalityDescDAO;
import gov.nih.nci.ncia.dto.ModalityDescDTO;
import gov.nih.nci.ncia.util.SpringApplicationContext;

import java.util.List;

/**
 * process request send from the UI
 * @author prakasht
 *
 */
public class ModalityDescProcessor {
	private ModalityDescDAO modalityDescDAO;
	public ModalityDescProcessor(){
		modalityDescDAO = (ModalityDescDAO)SpringApplicationContext.getBean("modalityDescDAO");
	}
	
	public List<ModalityDescDTO> findAllModalityDesc(){
		return modalityDescDAO.findAllModalityDesc();		
	}
	
}
