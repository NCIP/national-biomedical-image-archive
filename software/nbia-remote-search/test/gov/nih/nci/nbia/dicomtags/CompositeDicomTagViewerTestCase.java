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
import gov.nih.nci.nbia.dicomtags.LocalDicomTagViewer;
import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.ImageSearchResult;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CompositeDicomTagViewer.class)
public class CompositeDicomTagViewerTestCase {

	@Test
	public void testViewDicomHeaderRemote() throws Exception {
		ImageSearchResult imageSearchResult = DicomTagUtil.constructImageSearchResultRemote();
		DicomTagDTO[] dicomDtos = DicomTagUtil.constructMockReturnValue();

		//create mocks
		RemoteDicomTagViewer remoteDicomTagViewerMock = createMock(RemoteDicomTagViewer.class);
		LocalDicomTagViewer localDicomTagViewerMock = createMock(LocalDicomTagViewer.class);
		
	    expectNew(RemoteDicomTagViewer.class).andReturn(remoteDicomTagViewerMock);               
	    expect(remoteDicomTagViewerMock.viewDicomHeader(imageSearchResult)).
	        andReturn(Arrays.asList(dicomDtos));
							
	    expectNew(LocalDicomTagViewer.class).andReturn(localDicomTagViewerMock);               

		//replay the mock               
    	replay(remoteDicomTagViewerMock, RemoteDicomTagViewer.class);                
    	replay(localDicomTagViewerMock, LocalDicomTagViewer.class); 
    	
    	//verify the OUT
	    CompositeDicomTagViewer compositeDicomTagViewer = new CompositeDicomTagViewer();           

    	List<DicomTagDTO> dicomTagDtoList = compositeDicomTagViewer.viewDicomHeader(imageSearchResult);
    	assertTrue(dicomTagDtoList.size()==4);
    	
    	//verify the mock
    	verify(remoteDicomTagViewerMock, RemoteDicomTagViewer.class);		
    	verify(localDicomTagViewerMock, LocalDicomTagViewer.class);		
    }
	
	
	@Test
	public void testViewDicomHeaderLocal() throws Exception {
		ImageSearchResult imageSearchResult = DicomTagUtil.constructImageSearchResultLocal();
		DicomTagDTO[] dicomDtos = DicomTagUtil.constructMockReturnValue();

		//create mocks
		RemoteDicomTagViewer remoteDicomTagViewerMock = createMock(RemoteDicomTagViewer.class);
		LocalDicomTagViewer localDicomTagViewerMock = createMock(LocalDicomTagViewer.class);
	
	    expectNew(RemoteDicomTagViewer.class).andReturn(remoteDicomTagViewerMock);               

	    expectNew(LocalDicomTagViewer.class).andReturn(localDicomTagViewerMock);               
	    expect(localDicomTagViewerMock.viewDicomHeader(imageSearchResult)).
	        andReturn(Arrays.asList(dicomDtos));
							
		//replay the mock
    	replay(remoteDicomTagViewerMock, LocalDicomTagViewer.class);                
    	replay(localDicomTagViewerMock, RemoteDicomTagViewer.class);                

    	//verify the OUT
	    CompositeDicomTagViewer compositeDicomTagViewer = new CompositeDicomTagViewer();           

    	List<DicomTagDTO> dicomTagDtoList = compositeDicomTagViewer.viewDicomHeader(imageSearchResult);
    	assertTrue(dicomTagDtoList.size()==4);
    	
    	//verify the mock
    	verify(remoteDicomTagViewerMock, RemoteDicomTagViewer.class);
    	verify(localDicomTagViewerMock, LocalDicomTagViewer.class);		    	
    }		
}
