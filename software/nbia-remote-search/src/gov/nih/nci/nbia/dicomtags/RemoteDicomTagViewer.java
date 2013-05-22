/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dicomtags;

import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultImpl;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import org.apache.axis.types.URI.MalformedURIException;

/**
 * Implementation of DicomTagViewer that can retrieve the tags
 * remotely through the grid interface.
 */
public class RemoteDicomTagViewer implements DicomTagViewer {
	/**
	 * {@inheritDoc}
	 *
	 * <p>Invokes the grid service for the node associated with the search result
	 * to ask it for the dicom tags
	 */
	public List<DicomTagDTO> viewDicomHeader(ImageSearchResult imageSearchResult) {
		RemoteNode remoteNode = (RemoteNode)imageSearchResult.associatedLocation();
		String serviceAddress = remoteNode.getEndpointReferenceType().getAddress().toString();
		NCIACoreServiceClient nbiaServiceClient;
		try {
			nbiaServiceClient = new NCIACoreServiceClient(serviceAddress);
			// TODO: KEEP ONLY "else" code when all nodes (remote and local) moves to NBIA 5.1 then below code is not needed. 
			RemoteNode location = RemoteNode.constructPartialRemoteNode(remoteNode.getDisplayName(),remoteNode.getURL());
			imageSearchResult.associateLocation(location);
			if ( remoteNode.getServiceMetadata().getServiceDescription() != null && remoteNode.getServiceMetadata().getServiceDescription().getService().getVersion() != null 
					&& remoteNode.getServiceMetadata().getServiceDescription().getService().getVersion().equals("1.3")) {
				return getDicomHeaderForV13(imageSearchResult, serviceAddress);	
			} else {
				return Arrays.asList(nbiaServiceClient.viewDicomHeader(imageSearchResult));
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			try {
				//TODO: remove the try once all remote nodes moves to NBIA 5.1
				// this exception will come mostly when querying remote node(version 1.3)
				return getDicomHeaderForV13(imageSearchResult, serviceAddress);
			} catch (MalformedURIException e) {
				e.printStackTrace();
				throw new RuntimeException(ex);
			} catch (RemoteException e) {
				e.printStackTrace();
				throw new RuntimeException(ex);
			}
			
		}
	}

	private List<DicomTagDTO> getDicomHeaderForV13(ImageSearchResult imageSearchResult,
			String serviceAddress) throws MalformedURIException,
			RemoteException {
		NCIACoreServiceClient nbiaServiceClient;
		nbiaServiceClient = new NCIACoreServiceClient(serviceAddress);
		ImageSearchResultImpl imageSearchImpl = new ImageSearchResultImpl();
		imageSearchImpl.setId(imageSearchResult.getId());
		imageSearchImpl.setInstanceNumber(imageSearchResult.getInstanceNumber());
		imageSearchImpl.setSeriesId(imageSearchResult.getSeriesId());
		imageSearchImpl.setSeriesInstanceUid(imageSearchResult.getSeriesInstanceUid());
		imageSearchImpl.setSize(imageSearchResult.getSize());
		imageSearchImpl.setSopInstanceUid(imageSearchResult.getSopInstanceUid());
		imageSearchImpl.setThumbnailURL(imageSearchResult.getThumbnailURL());
		return Arrays.asList(nbiaServiceClient.viewDicomHeader(imageSearchImpl));
	}
}
