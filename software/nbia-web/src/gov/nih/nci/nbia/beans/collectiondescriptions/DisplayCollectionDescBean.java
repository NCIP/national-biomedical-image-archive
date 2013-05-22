/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.collectiondescriptions;

import gov.nih.nci.nbia.dao.CollectionDescDAO;
import gov.nih.nci.nbia.dto.CollectionDescDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

/**
 * Display the collection description on the search page
 * @author lethai
 *
 */
public class DisplayCollectionDescBean {
	private static Logger logger = Logger.getLogger(DisplayCollectionDescBean.class);
	
	private String collectionDescription;
	private String selectedCollectionLabel;
	/**
     * Whether the popup is visible or not.  If the popup is modal... it should be only one?
     */
    private boolean collectionDescriptionPopupRendered = false;

	
	public String toggleCollectionDescriptionPopup() {
    	collectionDescriptionPopupRendered = false;
    	return null;
    }
	
	public boolean getCollectionDescriptionPopupRendered() {
    	return this.collectionDescriptionPopupRendered;
    }
	
	public String getCollectionDescription(){
    	return collectionDescription;
    }
    
    public void setCollectionDescription(String collectionDescription ){
    	this.collectionDescription = collectionDescription;
    }
    
    
    public String getSelectedCollectionLabel() {
		return selectedCollectionLabel;
	}

	public void setSelectedCollectionLabel(String selectedCollectionLabel) {
		this.selectedCollectionLabel = selectedCollectionLabel;
	}

	public void collectionDescriptionAction(ActionEvent event){
    	SelectItem selectItem = (SelectItem)event.getComponent().getAttributes().get("collectionName");
    	selectedCollectionLabel = selectItem.getLabel();
    	logger.debug("selectedCollectionLabel : " + selectedCollectionLabel);
		CollectionDescDTO collectionDescByCollectionName = dao.findCollectionDescByCollectionName(selectedCollectionLabel);
		System.out.println("collectionDescByCollectionName: " + collectionDescByCollectionName);
		if(collectionDescByCollectionName != null){
		    collectionDescription = collectionDescByCollectionName.getDescription();
		}else{
			collectionDescription = "";
		}
		collectionDescriptionPopupRendered = true;
		logger.debug("collectionDescription: " + collectionDescription);
    }
	
	private CollectionDescDAO dao = (CollectionDescDAO)SpringApplicationContext.getBean("collectionDescDAO");
}