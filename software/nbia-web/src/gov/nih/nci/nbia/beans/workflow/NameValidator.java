package gov.nih.nci.nbia.beans.workflow;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.component.UIInput;

public class NameValidator implements Validator {

	  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		  
		 String name = (String) value;
		
	    if ("".equals(name.trim())) {
	      FacesMessage message = new FacesMessage();
	      String messageStr = (String)component.getAttributes().get("message");
	      if (messageStr == null) {
	        messageStr = "Please enter an Name";
	      }
	      message.setDetail(messageStr);
	      message.setSummary(messageStr);
	      message.setSeverity(FacesMessage.SEVERITY_ERROR);
	      throw new ValidatorException(message);
	    }
	  }
	}