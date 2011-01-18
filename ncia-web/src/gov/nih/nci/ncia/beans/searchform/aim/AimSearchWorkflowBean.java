package gov.nih.nci.ncia.beans.searchform.aim;

import gov.nih.nci.ncia.dao.AimImgObsCharacteristicDAO;
import gov.nih.nci.ncia.dto.ImgObsCharacteristicDTO;
import gov.nih.nci.ncia.util.SpringApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

public class AimSearchWorkflowBean {
    public void loggedIn() throws Exception {
    	AimImgObsCharacteristicDAO dao = (AimImgObsCharacteristicDAO)SpringApplicationContext.getBean("aimImgObsCharacteristicDAO");

    	Collection<ImgObsCharacteristicDTO> dtos = dao.findAllCodeMeaningNamesAndValuePairs();

    	for(ImgObsCharacteristicDTO dto : dtos) {
    		codeMeaningNameItems.add(new SelectItem(Boolean.FALSE, dto.getCodeMeaningName()));
    		codeValuePairItems.add(new SelectItem(Boolean.FALSE, dto.getCodeSchemaDesignator()+"-"+dto.getCodeValue()));
    	}
    }

	///////////////////////////////// begin Img Observation code meanings //////////////////////////////

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
    {
    	quantifications = AimMock.createMockQuantifications();
    	for(Quantification q : quantifications) {
            quantificationItems.add(new SelectItem(q.getName(), q.getName()));
    	}
    }

    private Quantification matchQuanNameToQuan(String quanName) {
    	for(Quantification q : quantifications) {
            if(q.getName().equals(quanName)) {
            	return q;
            }
    	}
    	return null;
    }
}
