/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.gridsearch;

import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.nbia.search.ThumbnailURLResolver;
import gov.nih.nci.cagrid.ncia.service.NCIACoreServiceConfiguration;
import java.net.URI;

/**
 * A thumbnail resolver used by the grid service.... to point
 * to thumbnails served up by a webapp associated with this grid service.
 */
public class GridThumbnailUrlResolver implements ThumbnailURLResolver {

	/**
	 * {@inheritDoc}
	 */
	public String resolveThumbnailUrl(ImageDTO imageDTO) {
		try {
			NCIACoreServiceConfiguration nciaCoreServiceConfiguration = NCIACoreServiceConfiguration.getConfiguration();

			URI uri = new URI(nciaCoreServiceConfiguration.getThumbnailUrl()+
					          "?imageId="+
					          imageDTO.getSopInstanceUid());
			return uri.toString();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
}