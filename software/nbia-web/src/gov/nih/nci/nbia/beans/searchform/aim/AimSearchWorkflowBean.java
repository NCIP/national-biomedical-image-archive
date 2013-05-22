/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchform.aim;

import gov.nih.nci.nbia.beans.searchform.aim.Quantification.TYPE;
import gov.nih.nci.nbia.dao.AimImgObsCharacteristicDAO;
import gov.nih.nci.nbia.dao.AimImgObsCharacteristicQuantificationDAO;
import gov.nih.nci.nbia.dto.ImgObsCharacteristicDTO;
import gov.nih.nci.nbia.dto.ImgObsCharacteristicQuantificationDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

public class AimSearchWorkflowBean {
    public void loggedIn() throws Exception {
        populateCharacteristics();
        populateQuantifications();
    }

    public boolean isAimVisible() {
    	return quantificationItems.size()> 0 ||
    	       codeMeaningNameItems.size()> 0 ||
    	       codeValuePairItems.size()>0;
    	
    }
    
    public void setDefaultValues() {
    	unselectAllCodeMeanings();   
    	unselectAllCodeValuePairs();
    	this.addedQuantifications.clear();
    }
	
	///////////////////////////////// begin Img Observation code meanings //////////////////////////////

    public void selectCodeMeaningNames(Collection<String> savedCodeMeaningNames) {
    	for(SelectItem selectItem : codeMeaningNameItems) {
    		boolean selected = savedCodeMeaningNames.contains(selectItem.getLabel());
    		selectItem.setValue(selected);
    	}
    }
    
    public List<SelectItem> getImagingObservationCharacteristicCodeMeaningItems() {
        return codeMeaningNameItems;
    }

    public String selectAllCodeMeanings() {
    	for(SelectItem selectItem : codeMeaningNameItems) {
    		selectItem.setValue(true);
    	}
    	return null;
    }

    public String unselectAllCodeMeanings() {
    	for(SelectItem selectItem : codeMeaningNameItems) {
    		selectItem.setValue(false);
    	}
    	return null;
    }

    public List<String> getSelectedCodeMeaningNames() {
    	List<String> selectedCodeMeaningNames = new ArrayList<String>();
    	for(SelectItem item : codeMeaningNameItems) {
    		if(item.getValue().equals(true)) {
    			selectedCodeMeaningNames.add(item.getLabel());
    		}
    	}
    	return selectedCodeMeaningNames;
    }
    ///////////////////////////////// end Img Observation code meanings //////////////////////////////
    
	///////////////////////////////// begin Img Observation code value pairs  //////////////////////////////
    
    public void selectCodeValuePairs(Collection<String> savedCodeValuePairs) {
    	   
    	for(SelectItem selectItem : codeValuePairItems) {
    		boolean selected = savedCodeValuePairs.contains(selectItem.getLabel());
    		selectItem.setValue(selected);
    	}
    }
    
    public List<SelectItem> getImagingObservationCharacteristicCodeValuePairItems() {
        return codeValuePairItems;
    }

    public String selectAllCodeValuePairs() {
    	for(SelectItem selectItem : codeValuePairItems) {
    		selectItem.setValue(true);
    	}
    	return null;
    }

    public String unselectAllCodeValuePairs() {
    	for(SelectItem selectItem : codeValuePairItems) {
    		selectItem.setValue(false);
    	}
    	return null;
    }

    public List<String> getSelectedCodeValuePairNames() {
    	List<String> selectedCodeValuePairNames = new ArrayList<String>();
    	for(SelectItem item : codeValuePairItems) {
    		if(item.getValue().equals(true)) {
    			selectedCodeValuePairNames.add(item.getLabel());
    		}
    	}
    	return selectedCodeValuePairNames;
    }
    ///////////////////////////////// end Img Observation code value pairs //////////////////////////////

  
    
    public void selectQuantifications(Collection<String> quantificationPairs) {
    	this.addedQuantifications.clear();
    	
    	for(String pair : quantificationPairs) {
    		Quantification q = matchQuanNameToQuan(parseName(pair));
    		if(q!=null) {
    			q.setValue(parseValue(pair));
    			addedQuantifications.add(q);
    		}
    		//else ok saved when different data in system possibly
    	}
    }
    
    public List<SelectItem> getCharacteristicQuantificationItems() {
        return quantificationItems;
    }


    public void addCharacteristicQuantification() {
    	addedQuantifications.add(selectedQuantification);
    }

    public List<Quantification> getQuantifications() {
    	return new ArrayList<Quantification>(addedQuantifications);
    }

	public String getSelectedQuantificationName() {
		return selectedQuantificationName;
	}

	public void setSelectedQuantificationName(String quantification) {
		this.selectedQuantificationName = quantification;
		this.selectedQuantification = matchQuanNameToQuan(quantification);
	}

	public void removeQuantificationItem (ActionEvent event) {
		String quantificationName = (String)event.getComponent().getAttributes().get("quantificationName");
		
		System.out.println("quantificationName:"+quantificationName);
		
		Quantification quantificationToRemove = matchQuanNameToQuan(quantificationName);
		if(quantificationToRemove!=null) {
            boolean found = addedQuantifications.remove(quantificationToRemove);
            if(!found) {
            	System.out.println("Something not cool going on.  received quantificationName not in list0");
            }
		}
		else {
			System.out.println("Something not cool going on.  received quantificationName not in list1");
		}
	}    	
    ////////////////////////////////////////PRIVATE//////////////////////////////////////

	private Set<Quantification> addedQuantifications = new HashSet<Quantification>();

	private Set<Quantification> quantifications;

    private String selectedQuantificationName;

    private Quantification selectedQuantification;

    private List<SelectItem> codeMeaningNameItems = new ArrayList<SelectItem>();
    /*{
    	Set<String> codeMeaningNames = AimMock.createMockCodeMeaningNames();
    	for(String codeMeaningName : codeMeaningNames) {
    		codeMeaningNameItems.add(new SelectItem(Boolean.FALSE, codeMeaningName));
    	}
    }*/

    private List<SelectItem> codeValuePairItems = new ArrayList<SelectItem>();
    /*{
    	Set<String> codeValuePairs = AimMock.createMockCodeValuePairs();
    	for(String codeValuePair : codeValuePairs) {
    		codeValuePairItems.add(new SelectItem(Boolean.FALSE, codeValuePair));
    	}
    }*/

    private List<SelectItem> quantificationItems = new ArrayList<SelectItem>();
    /*{
    	quantifications = AimMock.createMockQuantifications();
    	for(Quantification q : quantifications) {
            quantificationItems.add(new SelectItem(q.getName(), q.getName()));
    	}
    }*/

    private Quantification matchQuanNameToQuan(String quanName) {
    	for(Quantification q : quantifications) {
            if(q.getName().equals(quanName)) {
            	return q;
            }
    	}
    	return null;
    }


    private void populateCharacteristics() {
    	AimImgObsCharacteristicDAO dao = (AimImgObsCharacteristicDAO)SpringApplicationContext.getBean("aimImgObsCharacteristicDAO");

    	Collection<ImgObsCharacteristicDTO> dtos = dao.findAllCodeMeaningNamesAndValuePairs();

    	for(ImgObsCharacteristicDTO dto : dtos) {
    		codeMeaningNameItems.add(new SelectItem(Boolean.FALSE, dto.getCodeMeaningName()));
    		codeValuePairItems.add(new SelectItem(Boolean.FALSE, dto.getCodeSchemaDesignator()+"-"+dto.getCodeValue()));
    	}
    }


    private void populateQuantifications() {
		quantifications = new HashSet<Quantification>();

    	AimImgObsCharacteristicQuantificationDAO dao = (AimImgObsCharacteristicQuantificationDAO)SpringApplicationContext.getBean("aimImgObsCharacteristicQuantificationDAO");

    	Collection<ImgObsCharacteristicQuantificationDTO> dtos = dao.findAllQuantifications();

    	for(ImgObsCharacteristicQuantificationDTO dto : dtos) {
			quantificationItems.add(new SelectItem(dto.getName(), dto.getName()));

			Quantification newQ = new Quantification();
			newQ.setName(dto.getName());
			newQ.setType(TYPE.SCALE); //shortarm

		    List<SelectItem> possibleValues = new ArrayList<SelectItem>();
		    for(String possibleValue : dto.getPossibleValues()) {
		        possibleValues.add(new SelectItem(possibleValue));
		    }
            newQ.setPossibleValues(possibleValues);

            quantifications.add(newQ);
    	}
    }
    
    private static String parseName(String s) {
    	return s.substring(0, s.indexOf('='));
    }
    private static String parseValue(String s) {
    	return s.substring(s.indexOf('=')+1);
    }     

}
