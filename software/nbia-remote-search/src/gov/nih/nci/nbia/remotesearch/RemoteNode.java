/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.UsAvailableSearchTerms;
import gov.nih.nci.ncia.search.NBIANode;

import java.util.Date;
import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * Represents a remote instance of NCIACoreService (that has been registered in the
 * index service).
 */
public class RemoteNode extends NBIANode {
	static final long serialVersionUID = 5025173765475901441L;
	/**
	 * Construct a remote node
	 *
	 * @param serviceMetadata This must NOT be null.  The display name will be pulled form this object.
	 * @param endpoint  This can be null, but it's pretty useless without it
	 * @param availableSearchTerms This can be null
	 */
	public RemoteNode(ServiceMetadata serviceMetadata,
			          EndpointReferenceType endpoint,
			          AvailableSearchTerms availableSearchTerms,
			          UsAvailableSearchTerms usAvailableSearchTerms) {
		super(false,
			  serviceMetadata.getHostingResearchCenter().getResearchCenter().getDisplayName(),
			  endpoint.getAddress().toString());
		this.serviceMetadata = serviceMetadata;
		this.endpointReferenceType = endpoint;
		this.availableSearchTerms = availableSearchTerms;
		this.usAvailableSearchTerms = usAvailableSearchTerms;
		creationTime = new Date();
	}
	
	public RemoteNode(ServiceMetadata serviceMetadata,
			EndpointReferenceType endpoint,
			AvailableSearchTerms availableSearchTerms) {
		super(false, serviceMetadata.getHostingResearchCenter()
				.getResearchCenter().getDisplayName(), endpoint.getAddress()
				.toString());
		this.serviceMetadata = serviceMetadata;
		this.endpointReferenceType = endpoint;
		this.availableSearchTerms = availableSearchTerms;
		creationTime = new Date();
	}

	/**
	 * This the caGrid service metadata for the NCIACoreService at a given node.
	 * This cannot be null.
	 */
	public ServiceMetadata getServiceMetadata() {
		return serviceMetadata;
	}


	/**
	 * This the "telephone number" needed to send requests to a given caGrid NCIACoreService.
     * This should not be null.
	 */
	public EndpointReferenceType getEndpointReferenceType() {
		return endpointReferenceType;
	}


	/**
	 * These are the search terms associated with this node.
	 * Could be null I guess.
	 */
	public AvailableSearchTerms getAvailableSearchTerms() {
		return availableSearchTerms;
	}
	
	/**
	 * These are the search terms associated with this node.
	 * Could be null I guess.
	 */
	public UsAvailableSearchTerms getUsAvailableSearchTerms() {
		return usAvailableSearchTerms;
	}

	/**
	 * The time at which the constructor of this object was called
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * This constructs an instance of a remote node from just a url and a display name.
	 * This is useful in unit testing and also for the download mgr app which needs to
	 * make a RemoteNode from strings passed through a JNLP.  Maybe not the ideal way to do this
	 * but should work ok.
	 */
	public static RemoteNode constructPartialRemoteNode(String displayName, String url) throws Exception {
		ServiceMetadata serviceMetadata = new ServiceMetadata();
		ServiceMetadataHostingResearchCenter serviceMetadataHostingResearchCenter = new ServiceMetadataHostingResearchCenter();
		ResearchCenter researchCenter = new ResearchCenter();
		researchCenter.setDisplayName(displayName);
		serviceMetadataHostingResearchCenter.setResearchCenter(researchCenter);
		serviceMetadata.setHostingResearchCenter(serviceMetadataHostingResearchCenter);

		EndpointReferenceType endpointReferenceType = new EndpointReferenceType();
		endpointReferenceType.setAddress(new AttributedURI(url));

		return new RemoteNode(serviceMetadata, endpointReferenceType, null, null);
	}

	/////////////////////////////////////////////////PRIVATE/////////////////////////////////

	private AvailableSearchTerms availableSearchTerms;
	private UsAvailableSearchTerms usAvailableSearchTerms;	

	private ServiceMetadata serviceMetadata;

	private Date creationTime;

	private EndpointReferenceType endpointReferenceType;
}
