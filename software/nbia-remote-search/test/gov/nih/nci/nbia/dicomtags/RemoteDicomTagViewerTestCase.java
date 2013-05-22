/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dicomtags;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.ImageSearchResult;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RemoteDicomTagViewer.class)
public class RemoteDicomTagViewerTestCase {

	@Test
	public void testViewDicomHeader() throws Exception {
		ImageSearchResult imageSearchResult = DicomTagUtil.constructImageSearchResultRemote();

		//create mock
		String serviceAddress = "http://fakeAddress";
		NCIACoreServiceClient nbiaServiceClientMock = createMock(NCIACoreServiceClient.class);

		//create object under test
		RemoteDicomTagViewer remoteDicomTagViewer = new RemoteDicomTagViewer();

		//set expectations for mock
		expectNew(NCIACoreServiceClient.class, serviceAddress).
		    andReturn(nbiaServiceClientMock);
		expect(nbiaServiceClientMock.viewDicomHeader(imageSearchResult)).
		    andReturn(DicomTagUtil.constructMockReturnValue());

		//replay the mock
    	replay(nbiaServiceClientMock, NCIACoreServiceClient.class);

    	//verify the OUT
    	List<DicomTagDTO> dicomTagDtoList = remoteDicomTagViewer.viewDicomHeader(imageSearchResult);
    	assertTrue(dicomTagDtoList.size()==4);

    	//verify the mock
    	verify(nbiaServiceClientMock, NCIACoreServiceClient.class);
	}
}
