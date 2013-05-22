/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.griddao;

import gov.nih.nci.ncia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.ncia.domain.Image;
import gov.nih.nci.ncia.gridzip.ZippingDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml", "/applicationContext-hibernate-testContext.xml"})
public class ImageDAOTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testGetImagesFiles() throws Exception{
		List<String> sopInstanceUIDList = new ArrayList<String>(); 
		sopInstanceUIDList.add("1.3.6.1.4.1.9328.50.3.170");
		sopInstanceUIDList.add("1.3.6.1.4.1.9328.50.3.171");
		sopInstanceUIDList.add("1.3.6.1.4.1.9328.50.3.172");
		sopInstanceUIDList.add("1.3.6.1.4.1.9328.50.3.173");

		Map<String, String> sopToFilePathMap = imageDAO.getImagesFiles(sopInstanceUIDList);
		System.out.println("Size:"+sopToFilePathMap.size());
//		for(String k : sopToFilePathMap.keySet()) {
//			System.out.println(k+"="+sopToFilePathMap.get(k));
//		}

		Assert.assertTrue(sopToFilePathMap.size()==4);

		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.170.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57684.dcm"));
		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.171.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57685.dcm"));
		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.172.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57690.dcm"));
		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.173.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57691.dcm"));

	}
	
	@Test
	public void testGetImagesFilesEmpty() throws Exception {
		List<String> sopInstanceUIDList = new ArrayList<String>();
		sopInstanceUIDList.add("fred");

		Map<String, String> sopToFilePathMap = imageDAO.getImagesFiles(sopInstanceUIDList);

		Assert.assertTrue(sopToFilePathMap.size()==0);
	}

	@Test
	public void testGetImagesFilesByPatientId() throws Exception {
		Map<String, String> sopToFilePathMap = imageDAO.getImagesFilesByPatientId("1.3.6.1.4.1.9328.50.3.0022");

		System.out.println("Size:"+sopToFilePathMap.size());
		Assert.assertTrue(sopToFilePathMap.size()==129);

		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.170.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57684.dcm"));
		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.171.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57685.dcm"));
		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.172.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57690.dcm"));
		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.173.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57691.dcm"));

	}

	@Test	
	public void testGetImagesFilesByStudyInstanceUID() throws Exception{
		Map<String, String> sopToFilePathMap = imageDAO.getImagesFilesByStudyInstanceUID("1.3.6.1.4.1.9328.50.3.68");

		System.out.println("Size:"+sopToFilePathMap.size());
		Assert.assertTrue(sopToFilePathMap.size()==129);

		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.170.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57684.dcm"));
		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.171.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57685.dcm"));
		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.172.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57690.dcm"));
		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.173.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57691.dcm"));

	}

	@Test	
	public void testGetImagesFilesBySeriesInstanceUID() throws Exception{
		Map<String, String> sopToFilePathMap = imageDAO.getImagesFilesBySeriesInstanceUID("1.3.6.1.4.1.9328.50.3.195");

		System.out.println("Size:"+sopToFilePathMap.size());
		System.out.println("sopToFIlePathMap.size() ======= "  + sopToFilePathMap.size());
		Assert.assertTrue(sopToFilePathMap.size()==140);

		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.330.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-58000.dcm"));
		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.331.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-58001.dcm"));
		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.332.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-58002.dcm"));
		Assert.assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.333.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-58003.dcm"));
	}

	@Test
	public void testGetImageFilesByPatientIds() throws Exception {

		List<String> patientIdList = new ArrayList<String>();
		patientIdList.add("1.3.6.1.4.1.9328.50.3.0022");
		patientIdList.add("1.3.6.1.4.1.9328.50.3.0023");
		List<ZippingDTO> zippingDtoList = imageDAO.getImageFilesByPatientIds(patientIdList);

		System.out.println("fffff:"+zippingDtoList.size());
		Assert.assertTrue(zippingDtoList.size()==(140+129));
	}


	@Test
	public void testGetImageFilesByStudiesUids() throws Exception {

		List<String> studyIdList = new ArrayList<String>();
		studyIdList.add("1.3.6.1.4.1.9328.50.3.68");
		studyIdList.add("1.3.6.1.4.1.9328.50.3.194");
		List<ZippingDTO> zippingDtoList = imageDAO.getImageFilesByStudiesUids(studyIdList);
		System.out.println("fffff1:"+zippingDtoList.size());

		Assert.assertTrue(zippingDtoList.size()==(140+129));
	}

	@Test
	public void testGetImageFilesBySeriesUids() throws Exception{

		List<String> seriesIdList = new ArrayList<String>();
		seriesIdList.add("1.3.6.1.4.1.9328.50.3.69");
		seriesIdList.add("1.3.6.1.4.1.9328.50.3.195");
		List<ZippingDTO> zippingDtoList = imageDAO.getImageFilesBySeriesUids(seriesIdList);
		System.out.println("fffff2:"+zippingDtoList.size());

		Assert.assertTrue(zippingDtoList.size()==(140+129));
	}

	@Test
	public void testGetRepresentativeImageBySeries() throws Exception{
		Image image = imageDAO.getRepresentativeImageBySeries("1.3.6.1.4.1.9328.50.3.69");
		Assert.assertTrue(image != null);
		Assert.assertTrue(image.getSopInstanceUID() != null);
	}

    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_1044.xml";
    
    @Autowired
    private ImageDAOInterface imageDAO;    
}
