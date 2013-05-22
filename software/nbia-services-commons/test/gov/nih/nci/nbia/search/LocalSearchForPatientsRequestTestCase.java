/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.ncia.criteria.ImageModalityCriteria;
import gov.nih.nci.ncia.search.NBIANode;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.PatientSearchResultImpl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocalSearchForPatientsRequest.class}) 
public class LocalSearchForPatientsRequestTestCase  {
	@Test
	public void testCall() throws Exception {
		PatientSearcher patientSearcherMock = createMock(PatientSearcher.class);

		NBIANode localNode = new NBIANode(true, "d1", "u1");
        DICOMQuery dicomQuery = new DICOMQuery();
		ImageModalityCriteria imageModalityCriteria = new ImageModalityCriteria();
		imageModalityCriteria.setImageModalityValue("CT");
		dicomQuery.setCriteria(imageModalityCriteria);

		expectNew(PatientSearcher.class).
	        andReturn(patientSearcherMock);		
		expect(patientSearcherMock.searchForPatients(dicomQuery)).
		    andReturn(constructPatientSearchResultList());
		
		replay(PatientSearcher.class, patientSearcherMock);
		
		//OUT
		LocalSearchForPatientsRequest localSearchForPatientsRequest =
			new LocalSearchForPatientsRequest(localNode, dicomQuery);

		PatientSearchResults patientSearchResults = localSearchForPatientsRequest.call();

		Assert.assertEquals(patientSearchResults.getNode(), localNode);
		Assert.assertTrue(patientSearchResults.getResults().length==4);
		
		verify(PatientSearcher.class, patientSearcherMock);
	}
	
	//////////////////////////////////////PRIVATE/////////////////////////////////////
	
	private List<PatientSearchResult> constructPatientSearchResultList() {
		List<PatientSearchResult> list = new ArrayList<PatientSearchResult>();
		for(int i=0;i<4;i++) {
			list.add(new PatientSearchResultImpl());
		}
		return list;
	}
}
