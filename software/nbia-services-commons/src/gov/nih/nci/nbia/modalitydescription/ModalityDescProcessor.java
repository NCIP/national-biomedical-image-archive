/**
 * 
 */
package gov.nih.nci.nbia.modalitydescription;

import gov.nih.nci.nbia.dao.ModalityDescDAO;
import gov.nih.nci.nbia.dto.ModalityDescDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;

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
