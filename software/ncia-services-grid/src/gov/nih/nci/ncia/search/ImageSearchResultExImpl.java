package gov.nih.nci.ncia.search;

public class ImageSearchResultExImpl extends ImageSearchResultImpl
                                     implements ImageSearchResultEx {
	
	public ImageSearchResultExImpl() {

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
