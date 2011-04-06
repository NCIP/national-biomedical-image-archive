package gov.nih.nci.ncia.beans.searchresults;

import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.ncia.dto.ImageDTO;
import gov.nih.nci.ncia.lookup.ImageRegisterMap;
import gov.nih.nci.ncia.search.ThumbnailURLResolver;
import gov.nih.nci.ncia.util.JsfUtil;

/**
 * <p>This object dreams up a URL by which a given DICOM image's thumbnail can be viewed.
 * This code MUST line up with ThumbnailServer which serves up the URL.
 */
public class DefaultThumbnailURLResolver implements ThumbnailURLResolver {
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>This registers the DICOM file path in a registration map that the ThumbnailServer
	 * will use to return thumbnail files.
	 */
	public String resolveThumbnailUrl(ImageDTO imageDTO) {
		
		ImageRegisterMap imageRegisterMap = (ImageRegisterMap)JsfUtil.getSessionObject("imageRegisterMap");
		if(imageRegisterMap==null) {
			imageRegisterMap = new ImageRegisterMap();
			JsfUtil.putSessionObject("imageRegisterMap", imageRegisterMap);
		}
		int locationId = imageRegisterMap.registerFilePath(imageDTO.getFileURI());
		
	    String thumbnailUrl = NCIAConfig.getImageServerUrl()+"/ncia/thumbnailViewer?location="+locationId;
	    return thumbnailUrl;
	}
}	