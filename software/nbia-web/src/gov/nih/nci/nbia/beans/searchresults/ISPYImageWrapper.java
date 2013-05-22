/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchresults;

import java.util.List;

import gov.nih.nci.nbia.util.NameValueObj;
import gov.nih.nci.ncia.search.ImageSearchResult;

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
