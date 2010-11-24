package gov.nih.nci.ncia.beans.searchresults;

import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.util.UidDisplayUtil;

public class ImageResultWrapper {


	public ImageResultWrapper(ImageSearchResult imageSearchResult) {
		this.imageSearchResult = imageSearchResult;
	}
    
	public ImageSearchResult getImage() {
		return imageSearchResult;
	}

    public String getSeriesInstanceUid() {    	
    	return  UidDisplayUtil.getDisplayUid(imageSearchResult.getSeriesInstanceUid());
    }
    
    public String getSopInstanceUid() {    	
    	return UidDisplayUtil.getDisplayUid(imageSearchResult.getSopInstanceUid());    	
    }  
	
    public String getBasketKey() {
    	return imageSearchResult.getSeriesId()+"||"+imageSearchResult.associatedLocation().getURL();
    }

    
    /////////////////////////////////////////PRIVATE////////////////////////////////////
    
    private ImageSearchResult imageSearchResult;
}
