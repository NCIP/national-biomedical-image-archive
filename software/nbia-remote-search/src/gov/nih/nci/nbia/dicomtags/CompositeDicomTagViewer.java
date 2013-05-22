/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dicomtags;

import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.ImageSearchResult;

import java.util.List;


/**
 * Implementation of DicomTagViewer that is smart enough to redirect
 * to a remote/local DicomTagViewer depending on the node associated
 * with the ImageSearchResult.
 */
public class CompositeDicomTagViewer implements DicomTagViewer {
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Depending on where the image is from, local/remote, direct
	 * to the proper tag viewer instance.
	 */
	public List<DicomTagDTO> viewDicomHeader(ImageSearchResult image) {
		if(image.associatedLocation().isLocal()) {
			return localDicomTagViewer.viewDicomHeader(image);
		}
		else {
			return remoteDicomTagViewer.viewDicomHeader(image);
		}
	}
	
	//////////////////////////////////PRIVATE///////////////////////////////
	
	private DicomTagViewer localDicomTagViewer = new LocalDicomTagViewer();
	
	private DicomTagViewer remoteDicomTagViewer = new RemoteDicomTagViewer();
}