/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.nbia.search.*;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.PatientSearchResultImpl;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.ncia.search.StudyIdentifiers;
import gov.nih.nci.ncia.search.StudySearchResult;
import junit.framework.TestCase;

public class RemoteDrillDownTestDriver extends TestCase {

	public void testStudyRetrieval() throws Exception {
		RemoteDrillDown remoteDrillDown = new RemoteDrillDown();

		PatientSearchResultImpl patientSearchResult = new PatientSearchResultImpl();
		patientSearchResult.associateLocation(createRemoteNode());

		patientSearchResult.setId(1);
		patientSearchResult.setTotalNumberOfSeries(12);
		patientSearchResult.setTotalNumberOfStudies(4);
		patientSearchResult.setProject("RIDER");
		patientSearchResult.setSubjectId("Fred");


		StudyIdentifiers[] si = RemoteTestUtil.constructIdentifiers();
		patientSearchResult.setStudyIdentifiers(si);

		StudySearchResult[] results = remoteDrillDown.retrieveStudyAndSeriesForPatient(patientSearchResult);
		System.out.println("len:"+results.length);
		for(StudySearchResult r : results) {
			System.out.println("r:"+r.getStudyInstanceUid());
			for(SeriesSearchResult s : r.getSeriesList()) {
				System.out.println("s:"+s);
			}
		}
	}

	private static RemoteNode createRemoteNode() throws Exception {
		ServiceMetadata serviceMetadata = new ServiceMetadata();
		ServiceMetadataHostingResearchCenter serviceMetadataHostingResearchCenter = new ServiceMetadataHostingResearchCenter();
		ResearchCenter researchCenter = new ResearchCenter();
		researchCenter.setDisplayName("foo");
		serviceMetadataHostingResearchCenter.setResearchCenter(researchCenter);
		serviceMetadata.setHostingResearchCenter(serviceMetadataHostingResearchCenter);

		URI uri = new URI("http://imaging-dev.nci.nih.gov/wsrf/services/cagrid/NCIACoreService");
		EndpointReferenceType endpointReferenceType = new EndpointReferenceType(uri);
		AvailableSearchTerms availableSearchTerms = new AvailableSearchTerms();
		RemoteNode remoteNode = new RemoteNode(serviceMetadata,
				                               endpointReferenceType,
				                               availableSearchTerms);
		return remoteNode;
	}
}
