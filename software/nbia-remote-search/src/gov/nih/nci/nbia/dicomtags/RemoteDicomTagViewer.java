package gov.nih.nci.nbia.dicomtags;

import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.ImageSearchResult;

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

		try {
			NCIACoreServiceClient nbiaServiceClient = new NCIACoreServiceClient(serviceAddress);
			return Arrays.asList(nbiaServiceClient.viewDicomHeader(imageSearchResult));
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
}
