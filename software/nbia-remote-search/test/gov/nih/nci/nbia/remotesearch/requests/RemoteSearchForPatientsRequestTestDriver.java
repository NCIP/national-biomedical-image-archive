/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch.requests;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.nbia.search.*;
import gov.nih.nci.ncia.criteria.ImageModalityCriteria;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.PatientSearchResult;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

public class RemoteSearchForPatientsRequestTestDriver extends TestCase {

	public void testCall() throws Exception {
		DICOMQuery query = createQuery();

		RemoteSearchForPatientsRequest request = new RemoteSearchForPatientsRequest(createRemoteNode(), query);

		PatientSearchResults results = request.call();
		System.out.println("results:"+results);

		PatientSearchResult[] r = results.getResults();
		System.out.println("r:"+r);
		for(PatientSearchResult x : r) {
			System.out.println("xr:"+x.getSubjectId());

		}

	}

	private static RemoteNode createRemoteNode() throws Exception {
		ServiceMetadata serviceMetadata = new ServiceMetadata();
		ServiceMetadataHostingResearchCenter serviceMetadataHostingResearchCenter = new ServiceMetadataHostingResearchCenter();
		ResearchCenter researchCenter = new ResearchCenter();
		researchCenter.setDisplayName("foo");
		serviceMetadataHostingResearchCenter.setResearchCenter(researchCenter);
		serviceMetadata.setHostingResearchCenter(serviceMetadataHostingResearchCenter);

		URI uri = new URI("http://localhost:21080/wsrf/services/cagrid/NCIACoreService");
		EndpointReferenceType endpointReferenceType = new EndpointReferenceType(uri);
		AvailableSearchTerms availableSearchTerms = new AvailableSearchTerms();
		RemoteNode remoteNode = new RemoteNode(serviceMetadata,
				                               endpointReferenceType,
				                               availableSearchTerms);
		return remoteNode;
	}
	private static DICOMQuery createQuery() {
		ImageModalityCriteria imageModalityCrit = new ImageModalityCriteria();
		List<String> imageModVal = new ArrayList<String>();
		imageModVal.add("CT");
		imageModalityCrit.setImageModalityObjects(imageModVal);

		// Build Query
		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setQueryName("Query 1");
		dicomQuery.setUserID("kascice");

		dicomQuery.setCriteria(imageModalityCrit);
		return dicomQuery;
	}

}
