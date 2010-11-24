/**
 *
 */
package gov.nih.nci.ncia.griddao;

import gov.nih.nci.ncia.AbstractDbTestCaseImpl;
import gov.nih.nci.ncia.domain.Image;
import gov.nih.nci.ncia.gridzip.ZippingDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lethai
 *
 */
public class ImageDAOTestCase extends AbstractDbTestCaseImpl {


	public void testGetImagesFiles() throws Exception{
		String sopInstanceUIDList = "'1.3.6.1.4.1.9328.50.3.170' OR "+
		                            "SOP_INSTANCE_UID = '1.3.6.1.4.1.9328.50.3.171' OR "+
			                        "SOP_INSTANCE_UID = '1.3.6.1.4.1.9328.50.3.172' OR "+
			                        "SOP_INSTANCE_UID = '1.3.6.1.4.1.9328.50.3.173'";

		ImageDAO imageFilesProcessor = new ImageDAO();
		Map<String, String> sopToFilePathMap = imageFilesProcessor.getImagesFiles(new StringBuffer(sopInstanceUIDList));
		System.out.println("Size:"+sopToFilePathMap.size());
//		for(String k : sopToFilePathMap.keySet()) {
//			System.out.println(k+"="+sopToFilePathMap.get(k));
//		}

		assertTrue(sopToFilePathMap.size()==4);

		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.170.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57684.dcm"));
		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.171.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57685.dcm"));
		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.172.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57690.dcm"));
		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.173.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57691.dcm"));

	}

	public void testGetImagesFilesEmpty() throws Exception {
		String sopInstanceUIDList = "'fred'";

		ImageDAO imageFilesProcessor = new ImageDAO();
		Map<String, String> sopToFilePathMap = imageFilesProcessor.getImagesFiles(new StringBuffer(sopInstanceUIDList));

		assertTrue(sopToFilePathMap.size()==0);
	}

	public void testGetImagesFilesByPatientId() throws Exception {
		ImageDAO imageFilesProcessor = new ImageDAO();
		Map<String, String> sopToFilePathMap = imageFilesProcessor.getImagesFilesByPatientId("1.3.6.1.4.1.9328.50.3.0022");

		System.out.println("Size:"+sopToFilePathMap.size());
		assertTrue(sopToFilePathMap.size()==128);

		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.170.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57684.dcm"));
		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.171.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57685.dcm"));
		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.172.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57690.dcm"));
		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.173.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57691.dcm"));

	}

	public void testGetImagesFilesByStudyInstanceUID() throws Exception{
		ImageDAO imageFilesProcessor = new ImageDAO();
		Map<String, String> sopToFilePathMap = imageFilesProcessor.getImagesFilesByStudyInstanceUID("1.3.6.1.4.1.9328.50.3.68");

		System.out.println("Size:"+sopToFilePathMap.size());
		assertTrue(sopToFilePathMap.size()==128);

		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.170.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57684.dcm"));
		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.171.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57685.dcm"));
		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.172.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57690.dcm"));
		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.173.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-57691.dcm"));

	}

	public void testGetImagesFilesBySeriesInstanceUID() throws Exception{
		ImageDAO imageFilesProcessor = new ImageDAO();
		Map<String, String> sopToFilePathMap = imageFilesProcessor.getImagesFilesBySeriesInstanceUID("1.3.6.1.4.1.9328.50.3.195");

		System.out.println("Size:"+sopToFilePathMap.size());
		assertTrue(sopToFilePathMap.size()==139);

		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.330.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-58000.dcm"));
		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.331.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-58001.dcm"));
		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.332.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-58002.dcm"));
		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.333.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-58003.dcm"));
	}


	public void testGetImageFilesByPatientIds() throws Exception {
		ImageDAO imageFilesProcessor = new ImageDAO();

		List<String> patientIdList = new ArrayList<String>();
		patientIdList.add("1.3.6.1.4.1.9328.50.3.0022");
		patientIdList.add("1.3.6.1.4.1.9328.50.3.0023");
		List<ZippingDTO> zippingDtoList = imageFilesProcessor.getImageFilesByPatientIds(patientIdList);

		System.out.println("fffff:"+zippingDtoList.size());
		assertTrue(zippingDtoList.size()==(139+128));
	}



	public void testGetImageFilesByStudiesUids() throws Exception {
		ImageDAO imageFilesProcessor = new ImageDAO();

		List<String> studyIdList = new ArrayList<String>();
		studyIdList.add("1.3.6.1.4.1.9328.50.3.68");
		studyIdList.add("1.3.6.1.4.1.9328.50.3.194");
		List<ZippingDTO> zippingDtoList = imageFilesProcessor.getImageFilesByStudiesUids(studyIdList);
		System.out.println("fffff1:"+zippingDtoList.size());

		assertTrue(zippingDtoList.size()==(139+128));
	}

	public void testGetImageFilesBySeriesUids() throws Exception{
		ImageDAO imageFilesProcessor = new ImageDAO();

		List<String> seriesIdList = new ArrayList<String>();
		seriesIdList.add("1.3.6.1.4.1.9328.50.3.69");
		seriesIdList.add("1.3.6.1.4.1.9328.50.3.195");
		List<ZippingDTO> zippingDtoList = imageFilesProcessor.getImageFilesBySeriesUids(seriesIdList);
		System.out.println("fffff2:"+zippingDtoList.size());

		assertTrue(zippingDtoList.size()==(139+128));
	}


	public void testGetRepresentativeImageBySeries() throws Exception{
		ImageDAO imageFilesProcessor = new ImageDAO();

		Image image = imageFilesProcessor.getRepresentativeImageBySeries("1.3.6.1.4.1.9328.50.3.69");
		assertTrue(image != null);
		assertTrue(image.getSopInstanceUID() != null);
	}

    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_1044.xml";
}
