/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchform;

import gov.nih.nci.ncia.criteria.RangeData;

import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;


public class RangeValidator {

	public void validateRange(FacesContext context,
			                  UIComponent component,
			                  Object value) {

	    String opLeft = retrieveInputComponentValue(component, "cmpLeftId");
	    String valLeft = retrieveInputComponentValue(component, "cmpLeftValue");
	    String opRight = retrieveInputComponentValue(component, "cmpRightId");
	    String valRight = retrieveInputComponentValue(component, "cmpRightValue");

	    String error = RangeData.validateRange(opLeft, valLeft, opRight, valRight);

        if (error != null) {
        	throw new ValidatorException(new FacesMessage(error));
        }
	}

	private String retrieveInputComponentValue(UIComponent anchor, String componentId) {
	    Map attributes = anchor.getAttributes();
	    String inputId = (String) attributes.get(componentId);
	    UIInput component = (UIInput) anchor.findComponent(inputId);

	    return (String)component.getLocalValue();
	}
}
