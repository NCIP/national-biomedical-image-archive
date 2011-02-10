package gov.nih.nci.ncia.search;

public class ImageSearchResultExImpl extends ImageSearchResultImpl
                                     implements ImageSearchResultEx {
	
	public ImageSearchResultExImpl() {

	}
	
	public ImageSearchResultExImpl(ImageSearchResult isr) {
		this.setId(isr.getId());
		this.setInstanceNumber(isr.getInstanceNumber());
		this.setSeriesId(isr.getSeriesId());
		this.setSeriesInstanceUid(isr.getSeriesInstanceUid());
		this.setSize(isr.getSize());
		this.setSopInstanceUid(isr.getSopInstanceUid());
		this.setThumbnailURL(isr.getThumbnailURL());
		this.associateLocation(isr.associatedLocation());
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
