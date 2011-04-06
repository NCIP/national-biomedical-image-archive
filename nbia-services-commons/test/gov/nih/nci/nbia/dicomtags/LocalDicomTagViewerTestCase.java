package gov.nih.nci.nbia.dicomtags;

import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultImpl;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class LocalDicomTagViewerTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testViewDicomHeaderById()  throws Exception {
		LocalDicomTagViewer localDicomTagViewer = new LocalDicomTagViewer();
		
		URL testDicomUrl = this.getClass().getClassLoader().getResource("test.dcm");
		FileUtils.copyURLToFile(testDicomUrl, new File("/tmp/test.dcm"));
		
		List<DicomTagDTO> tags = localDicomTagViewer.viewDicomHeader(591166);
		System.out.println("size:"+tags.size());
		Assert.assertTrue(tags.size()==110);
		
		new File("/tmp/test.dcm").delete();
	}

	@Test
	public void testViewDicomHeader()  throws Exception {
		LocalDicomTagViewer localDicomTagViewer = new LocalDicomTagViewer();

		ImageSearchResultImpl imageSearchResultImpl = new ImageSearchResultImpl();
		imageSearchResultImpl.setId(591166);
		URL testDicomUrl = this.getClass().getClassLoader().getResource("test.dcm");
		FileUtils.copyURLToFile(testDicomUrl, new File("/tmp/test.dcm"));
		
		List<DicomTagDTO> tags = localDicomTagViewer.viewDicomHeader(imageSearchResultImpl);
		Assert.assertTrue(tags.size()==110);
		
		new File("/tmp/test.dcm").delete();
		
		tags = localDicomTagViewer.viewDicomHeader((ImageSearchResult)null);
		Assert.assertTrue(tags.size()==0);		
	}	
	
	@Test
	public void testViewDicomHeaderByIdPublic()  throws Exception {
		LocalDicomTagViewer localDicomTagViewer = new LocalDicomTagViewer();
		localDicomTagViewer.setPublicData(true);
		localDicomTagViewer.setAuthorizationManager(new AuthorizationManager());
		
		URL testDicomUrl = this.getClass().getClassLoader().getResource("test.dcm");
		FileUtils.copyURLToFile(testDicomUrl, new File("/tmp/test.dcm"));
		
		List<DicomTagDTO> tags = localDicomTagViewer.viewDicomHeader(591166);
		System.out.println("size:"+tags.size());
		Assert.assertTrue(tags.size()==110);
		
		new File("/tmp/test.dcm").delete();
		
		tags = localDicomTagViewer.viewDicomHeader(666);
		System.out.println("size:"+tags.size());
		Assert.assertTrue(tags.size()==0);		
	}	
	/////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }
    

    ///////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/collections_testdata.xml";
}
