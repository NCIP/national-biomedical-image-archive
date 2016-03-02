/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchresults;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.util.UidDisplayUtil;
import gov.nih.nci.nbia.searchresult.APIURLHolder;
import gov.nih.nci.nbia.searchresult.ImageSearchResult;
import gov.nih.nci.nbia.searchresult.ImageSearchResultEx;
import gov.nih.nci.nbia.searchresult.ImageSearchResultExImpl;

public class ImageResultWrapper {


	public ImageResultWrapper(ImageSearchResult imageSearchResult) {
		ImageSearchResultExImpl isrei = new ImageSearchResultExImpl();
		isrei.setId(imageSearchResult.getId());
		isrei.setInstanceNumber(imageSearchResult.getInstanceNumber());
		isrei.setSeriesId(imageSearchResult.getSeriesId());
		isrei.setSeriesInstanceUid(imageSearchResult.getSeriesInstanceUid());
		isrei.setSize(imageSearchResult.getSize());
		isrei.setSopInstanceUid(imageSearchResult.getSopInstanceUid());
		isrei.setThumbnailURL(imageSearchResult.getThumbnailURL());
		isrei.setStudyInstanceUid(imageSearchResult.getStudyInstanceUid());
		this.imageSearchResultEx = isrei;
	}
	
	public ImageResultWrapper(ImageSearchResultEx imageSearchResultEx) {
		this.imageSearchResultEx = imageSearchResultEx;
	}
   
	public ImageSearchResult getImage() {
		return imageSearchResultEx;
	}
 
	public ImageSearchResultEx getImageEx() {
		return imageSearchResultEx;
	}

    public String getSeriesInstanceUid() {    	
    	return  UidDisplayUtil.getDisplayUid(imageSearchResultEx.getSeriesInstanceUid());
    }
    
    public String getSopInstanceUid() {    	
    	return UidDisplayUtil.getDisplayUid(imageSearchResultEx.getSopInstanceUid());    	
    }  
	
    public String getBasketKey() {
    	return imageSearchResultEx.getSeriesId().toString();
    }

    public String getLink()
    {
        SecurityBean secure = BeanManager.getSecurityBean();
        String userName = secure.getUsername();
    	String url = APIURLHolder.getUrl()+"/nbia-api/services/o/wado?contentType=application/dicom&objectUID="+
    	imageSearchResultEx.getSopInstanceUid()+"&oviyamId="+APIURLHolder.addUser(userName)+
		"&wadoUrl="+APIURLHolder.getWadoUrl();
    	return url;
    }
    /////////////////////////////////////////PRIVATE////////////////////////////////////
    
//    private ImageSearchResult imageSearchResult;
    private ImageSearchResultEx imageSearchResultEx;
}
