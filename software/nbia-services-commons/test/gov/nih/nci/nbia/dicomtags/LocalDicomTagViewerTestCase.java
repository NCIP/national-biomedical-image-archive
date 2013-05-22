/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dicomtags;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.nbia.dao.ImageDAO;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.PublicData;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultImpl;

import java.io.File;
import java.net.URL;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"gov.nih.nci.nbia.security.NCIASecurityManager$RoleType"})
@PrepareForTest({LocalDicomTagViewer.class,  
                 SpringApplicationContext.class}) 
public class LocalDicomTagViewerTestCase  {

	@Test
	public void testViewDicomHeaderById()  throws Exception {
		
		URL testDicomUrl = this.getClass().getClassLoader().getResource("test.dcm");
		FileUtils.copyURLToFile(testDicomUrl, new File("/tmp/test.dcm"));
		
        expect(SpringApplicationContext.getBean("imageDAO")).
            andReturn(imageDAOMock);		
        expect(imageDAOMock.findImagePath(591166)).
            andReturn("/tmp/test.dcm");				
		
        replayMock();
        
		LocalDicomTagViewer localDicomTagViewer = new LocalDicomTagViewer();		
		List<DicomTagDTO> tags = localDicomTagViewer.viewDicomHeader(591166);
		Assert.assertTrue(tags.size()==110);
				
		new File("/tmp/test.dcm").delete();		
	}

	@Test
	public void testViewDicomHeader()  throws Exception {

		ImageSearchResultImpl imageSearchResultImpl = new ImageSearchResultImpl();
		imageSearchResultImpl.setId(591166);
		URL testDicomUrl = this.getClass().getClassLoader().getResource("test.dcm");
		FileUtils.copyURLToFile(testDicomUrl, new File("/tmp/test.dcm"));
		
        expect(SpringApplicationContext.getBean("imageDAO")).
            andReturn(imageDAOMock);		
        expect(imageDAOMock.findImagePath(591166)).
            andReturn("/tmp/test.dcm");				
	
        replayMock();
    
		LocalDicomTagViewer localDicomTagViewer = new LocalDicomTagViewer();
		List<DicomTagDTO> tags = localDicomTagViewer.viewDicomHeader(imageSearchResultImpl);
		Assert.assertTrue(tags.size()==110);
		
		new File("/tmp/test.dcm").delete();
		
		tags = localDicomTagViewer.viewDicomHeader((ImageSearchResult)null);
		Assert.assertTrue(tags.size()==0);		
	}	
	
	@Test
	public void testViewDicomHeaderByIdPublicForPublicImage()  throws Exception {
		PublicData publicDataMock = createMock(PublicData.class);		
		
		URL testDicomUrl = this.getClass().getClassLoader().getResource("test.dcm");
		FileUtils.copyURLToFile(testDicomUrl, new File("/tmp/test.dcm"));
		
        expect(SpringApplicationContext.getBean("imageDAO")).
            andReturn(imageDAOMock);		
        expect(imageDAOMock.findImagePath(591166)).
            andReturn("/tmp/test.dcm");
        expectNew(PublicData.class).
            andReturn(publicDataMock);
        expect(publicDataMock.checkPublicImage(591166)).
            andReturn(Boolean.TRUE);
        publicDataMock.setAuthorizationManager(authorizationManagerMock);
        
        replayMock();
        replay(PublicData.class, publicDataMock);
        
		LocalDicomTagViewer localDicomTagViewer = new LocalDicomTagViewer();
		localDicomTagViewer.setPublicData(true);
		localDicomTagViewer.setAuthorizationManager(authorizationManagerMock);
		
		List<DicomTagDTO> tags = localDicomTagViewer.viewDicomHeader(591166);
		Assert.assertTrue(tags.size()==110);
		
		new File("/tmp/test.dcm").delete();	
		
		verify(PublicData.class, publicDataMock);
	}	
	
	@Test
	public void testViewDicomHeaderByIdPublicForPrivateImage()  throws Exception {
		PublicData publicDataMock = createMock(PublicData.class);		
		
        expectNew(PublicData.class).
            andReturn(publicDataMock);
        expect(publicDataMock.checkPublicImage(666)).
            andReturn(Boolean.FALSE);
        publicDataMock.setAuthorizationManager(authorizationManagerMock);

        replayMock();
        replay(PublicData.class, publicDataMock);
        
		LocalDicomTagViewer localDicomTagViewer = new LocalDicomTagViewer();
		localDicomTagViewer.setPublicData(true);
		localDicomTagViewer.setAuthorizationManager(authorizationManagerMock);
		
		
		List<DicomTagDTO> tags = localDicomTagViewer.viewDicomHeader(666);
		Assert.assertTrue(tags.size()==0);		
		
		verify(PublicData.class, publicDataMock);
	}		

    
    @Before
    public void setUp() throws Exception {
        mockStatic(SpringApplicationContext.class);
        authorizationManagerMock = createMock(AuthorizationManager.class);  
        imageDAOMock = createMock(ImageDAO.class);       
    }

    @After
    public void tearDown() throws Exception {    
    	verifyMock();
    }
    
    ////////////////////////////////////PRIVATE/////////////////////////////////
   
    private AuthorizationManager authorizationManagerMock;    
    
    private ImageDAO imageDAOMock;    

	private void replayMock() {
        replay(SpringApplicationContext.class);
        replay(authorizationManagerMock, AuthorizationManager.class);
        replay(imageDAOMock, ImageDAO.class);
	}
	
	
	private void verifyMock() {
		verify(SpringApplicationContext.class);
		verify(authorizationManagerMock, AuthorizationManager.class);
		verify(imageDAOMock, ImageDAO.class);
	}   
}
