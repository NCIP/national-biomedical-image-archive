/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.cagrid.ncia.client;

import gov.nih.nci.cagrid.ncia.util.SecureClientUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;
import org.globus.gsi.GlobusCredential;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PublicTransferWithCredTestCaseFunctional extends TransferServiceTestCaseFunctional {
	String gridServiceUrl;
	{
		if(System.getProperty("grid.service.url")==null) {
			gridServiceUrl = "https://nciavgriddev5004.nci.nih.gov:8443/wsrf/services/cagrid/NCIACoreService";
		}
		else {
			gridServiceUrl = System.getProperty("grid.service.url");
		}		
	}

	String authUrl = "https://cagrid-auth-stage.nci.nih.gov:8443/wsrf/services/cagrid/AuthenticationService";
	String dorianURL = "https://cagrid-dorian-stage.nci.nih.gov:8443/wsrf/services/cagrid/Dorian";

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

		int actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(659,actualNumZipFiles);

		tscr = client.retrieveDicomDataByPatientIds(new String[]{"TCGA-06-0147"});
		actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(659,actualNumZipFiles);
	}

	@Test	
	public void testRetrieveDicomDataByStudyInstanceUid() throws Exception {

		TransferServiceContextReference tscr = client.retrieveDicomDataByStudyUID("1.3.6.1.4.1.9328.50.45.275881025454183713545354420382217269222");

		int actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(659,actualNumZipFiles);

		tscr = client.retrieveDicomDataByStudyUIDs(new String[]{"1.3.6.1.4.1.9328.50.45.275881025454183713545354420382217269222"});
		actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(551,actualNumZipFiles);

		//nimpy - series visibility treated differently by these two methods.  this is probably incorrect behavior.
	}

	@Test	
	public void testRetrieveDicomDataBySeriesInstanceUid() throws Exception {

		TransferServiceContextReference tscr = client.retrieveDicomDataBySeriesUID("1.3.6.1.4.1.9328.50.45.244740464952049551939432248480700607976");

		int actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(325,actualNumZipFiles);

		tscr = client.retrieveDicomDataBySeriesUIDs(new String[]{"1.3.6.1.4.1.9328.50.45.244740464952049551939432248480700607976"});
		actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(325,actualNumZipFiles);
	}

	@Test
	public void testRetrieveDicomDataByStudyTimepoint() throws Exception {

		TransferServiceContextReference tscr = client.retrieveDicomDataByNthStudyTimePointForPatient("TCGA-06-0147", 1);

		int actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(551,actualNumZipFiles);
	}

	///////////////////////////////////////////////////PROTECTED//////////////////////////////////////////////////////


	@Before
	protected void setUp()throws Exception{
	    String gridUsername = System.getProperty("grid.user.name");
	    String gridPassword = System.getProperty("grid.password");

		globusCred = SecureClientUtil.generateGlobusCredential(gridUsername,
                                                               gridPassword,
                                                               dorianURL,
                                                               authUrl);
		System.out.println("identity:"+globusCred.getIdentity());

		client = new NCIACoreServiceClient(gridServiceUrl, globusCred);
		client.setAnonymousPrefered(false);

	}

	///////////////////////////////////////////////////PRIVATE//////////////////////////////////////////////////////

	private NCIACoreServiceClient client;
	private GlobusCredential globusCred;


	private void runCQLInDirectory(String relativeDirectoryName) throws Exception {

		File[] cqlFiles = getCQLFiles(relativeDirectoryName);
		for(File cqlFile : cqlFiles) {
			System.out.println("CQL:"+cqlFile.getName());
			int actualNumZipFiles = sendCQLForZipFile(client, cqlFile, globusCred);
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
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_PatientTDP.xml", 551);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_SeriesStudy.xml", 551);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_SeriesStudyPatient.xml", 551);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_StudyPatient.xml", 551);

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
