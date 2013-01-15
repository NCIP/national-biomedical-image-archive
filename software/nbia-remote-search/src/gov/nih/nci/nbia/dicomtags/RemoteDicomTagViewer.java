package gov.nih.nci.nbia.dicomtags;

import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultImpl;

import java.util.Arrays;
import java.util.List;

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
		String remoteNodeVersion = remoteNode.getServiceMetadata().getServiceDescription().getService().getVersion();
		System.out.println("remoteNode Version" + remoteNodeVersion);
		try {
			NCIACoreServiceClient nbiaServiceClient = new NCIACoreServiceClient(serviceAddress);
			// TODO: KEEP ONLY "else" code when all nodes (remote and local) moves to NBIA 5.1 then below code is not needed. 
			RemoteNode location = RemoteNode.constructPartialRemoteNode(remoteNode.getDisplayName(),remoteNode.getURL());
			imageSearchResult.associateLocation(location);
			if (remoteNodeVersion != null && remoteNodeVersion.equals("1.3")) {
				ImageSearchResultImpl imageSearchImpl = new ImageSearchResultImpl();
				imageSearchImpl.setId(imageSearchResult.getId());
				imageSearchImpl.setInstanceNumber(imageSearchResult.getInstanceNumber());
				imageSearchImpl.setSeriesId(imageSearchResult.getSeriesId());
				imageSearchImpl.setSeriesInstanceUid(imageSearchResult.getSeriesInstanceUid());
				imageSearchImpl.setSize(imageSearchResult.getSize());
				imageSearchImpl.setSopInstanceUid(imageSearchResult.getSopInstanceUid());
				imageSearchImpl.setThumbnailURL(imageSearchResult.getThumbnailURL());
				return Arrays.asList(nbiaServiceClient.viewDicomHeader(imageSearchImpl));	
			} else {
				return Arrays.asList(nbiaServiceClient.viewDicomHeader(imageSearchResult));
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
}
