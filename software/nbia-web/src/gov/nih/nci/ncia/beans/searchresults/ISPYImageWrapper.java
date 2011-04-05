package gov.nih.nci.ncia.beans.searchresults;

import java.util.List;

import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.util.NameValueObj;

public class ISPYImageWrapper {

	public ISPYImageWrapper(ImageSearchResult imageDTO) {
		this.imageDTO = imageDTO;
	}
	
	public ImageSearchResult getImage() {
		return imageDTO;
	}
	
    public String getLabel() {
        return label;
    }
  
    public void setLabel(String label) {
        this.label = label;
    }
  
    /**
     * @return Returns the paramNameValues.
     */
    public List<NameValueObj> getParamNameValues() {
        return paramNameValues;
    }

    //not db - ispy    
    /**
     * @param paramNameValues The paramNameValues to set.
     */
    public void setParamNameValues(List<NameValueObj> paramNameValues) {
        this.paramNameValues = paramNameValues;
    }	
    
    private List<NameValueObj> paramNameValues;
    
    private String label;
    
    private ImageSearchResult imageDTO;

}
