/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.cagrid.ncia.client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PublicTransferWithNoCredTestCaseFunctional extends TransferServiceTestCaseFunctional {
	String gridServiceUrl;
	{
		if(System.getProperty("grid.service.url")==null) {
			gridServiceUrl = "https://nciavgriddev5004.nci.nih.gov:8443/wsrf/services/cagrid/NCIACoreService";
		}
		else {
			gridServiceUrl = System.getProperty("grid.service.url");
		}		
	}

	@Test
	public void testAssociations() throws Exception {
		runCQLInDirectory("test/resources/publicfilter/public/association");
	}

	@Test	
	public void testAttributes() throws Exception {
		runCQLInDirectory("test/resources/publicfilter/public/attribute");
	}

	@Test	
	public void testGroups() throws Exception {
		runCQLInDirectory("test/resources/publicfilter/public/group");
	}

	@Test	
	public void testRetrieveDicomDataByPatientId() throws Exception {

		TransferServiceContextReference tscr = client.retrieveDicomDataByPatientId("TCGA-06-0147");

		int actualNumZipFiles = processTransfer(tscr, null);
		Assert.assertEquals(0,actualNumZipFiles);
	}

	@Test	
	public void testRetrieveDicomDataByStudyInstanceUid() throws Exception {

		TransferServiceContextReference tscr = client.retrieveDicomDataByStudyUID("1.3.6.1.4.1.9328.50.45.275881025454183713545354420382217269222");

		int actualNumZipFiles = processTransfer(tscr, null);
		Assert.assertEquals(0,actualNumZipFiles);
	}

	@Test	
	public void testRetrieveDicomDataBySeriesInstanceUid() throws Exception {

		TransferServiceContextReference tscr = client.retrieveDicomDataBySeriesUID("1.3.6.1.4.1.9328.50.45.244740464952049551939432248480700607976");

		int actualNumZipFiles = processTransfer(tscr, null);
		Assert.assertEquals(0,actualNumZipFiles);
	}

	@Test
	public void testRetrieveDicomDataByStudyTimepoint() throws Exception {

		TransferServiceContextReference tscr = client.retrieveDicomDataByNthStudyTimePointForPatient("TCGA-06-0147", 1);

		int actualNumZipFiles = processTransfer(tscr, null);
		Assert.assertEquals(0,actualNumZipFiles);
	}


	@Before
	protected void setUp()throws Exception{
		client = new NCIACoreServiceClient(gridServiceUrl);
		client.setAnonymousPrefered(true);
	}

	private NCIACoreServiceClient client;


	private void runCQLInDirectory(String relativeDirectoryName) throws Exception {

		File[] cqlFiles = getCQLFiles(relativeDirectoryName);
		for(File cqlFile : cqlFiles) {
			System.out.println("CQL:"+cqlFile.getName());
			int actualNumZipFiles = sendCQLForZipFile(client, cqlFile, null);
			Integer expectedNumZipFiles = cqlToCountMap.get(constructKey(relativeDirectoryName,cqlFile));

			Assert.assertEquals(expectedNumZipFiles.intValue(),actualNumZipFiles);
		}
	}

	private static String constructKey(String relativeDirectoryName, File cqlFile) {
		return relativeDirectoryName + "/" + cqlFile.getName();
	}

	private static Map<String, Integer> cqlToCountMap = new HashMap<String, Integer>();

	{
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_ImageSeries.xml", 325);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_ImageSeriesStudy.xml", 551);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_ImageSeriesStudyPatient.xml", 551);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_PatientTDP.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_SeriesStudy.xml", 5);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_SeriesStudyPatient.xml", 5);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_StudyPatient.xml", 1);

		cqlToCountMap.put("test/resources/publicfilter/public/attribute/testTCGA_image.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/public/attribute/testTCGA_patient.xml", 551);
		cqlToCountMap.put("test/resources/publicfilter/public/attribute/testTCGA_project.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/public/attribute/testTCGA_series.xml", 325);
		cqlToCountMap.put("test/resources/publicfilter/public/attribute/testTCGA_site.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/public/attribute/testTCGA_study.xml", 551);

		cqlToCountMap.put("test/resources/publicfilter/public/group/andAssocAssoc.xml", 551);
		cqlToCountMap.put("test/resources/publicfilter/public/group/andAttrAssoc.xml", 551);
		cqlToCountMap.put("test/resources/publicfilter/public/group/andAttrAttr.xml", 325);
		cqlToCountMap.put("test/resources/publicfilter/public/group/orAttrAssoc.xml", 551);
		cqlToCountMap.put("test/resources/publicfilter/public/group/orAttrAttr.xml", 343);
	}
}
