/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.searchresult;

public class ImageSearchResultExImpl extends ImageSearchResultImpl
                                     implements ImageSearchResultEx {
	
	public ImageSearchResultExImpl() {

	}
	
	public ImageSearchResultExImpl(ImageSearchResult isr) {
		this.setId(isr.getId());
		this.setInstanceNumber(isr.getInstanceNumber());
		this.setSeriesId(isr.getSeriesId());
		this.setSeriesInstanceUid(isr.getSeriesInstanceUid());
		this.setStudyInstanceUid(isr.getStudyInstanceUid());
		this.setSize(isr.getSize());
		this.setSopInstanceUid(isr.getSopInstanceUid());
		this.setThumbnailURL(isr.getThumbnailURL());
		this.setStudyInstanceUid(isr.getStudyInstanceUid());
		this.nvPair = null;
	}
	
	
	public NameValuesPairs getNameValuesPairs() {
		return nvPair;		
	}
	
	public void setNameValuesPairs(NameValuesPairs nvPair) {
		this.nvPair = nvPair;
	}
 
	////////////////////////////////////////////////////PRIVATE///////////////////////////////////////
	
	private NameValuesPairs nvPair;
}
