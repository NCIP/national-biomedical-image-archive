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

public class PrivateTransferWithCredTestCaseFunctional extends TransferServiceTestCaseFunctional {
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

	/*@Test public void testAssociations() throws Exception {
		runCQLInDirectory("test/resources/publicfilter/private/association");
	}*/

	@Test
	public void testAttributes() throws Exception {
		runCQLInDirectory("test/resources/publicfilter/private/attribute");
	}

	/*@Test public void testGroups() throws Exception {
		runCQLInDirectory("test/resources/publicfilter/private/group");
	}*/

	@Test
	public void testRetrieveDicomDataByPatientId() throws Exception {

		TransferServiceContextReference tscr = client.retrieveDicomDataByPatientId("A2");

		int actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(3,actualNumZipFiles);


		tscr = client.retrieveDicomDataByPatientIds(new String[]{"A2"});
		actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(3,actualNumZipFiles);
	}

	@Test
	public void testRetrieveDicomDataByStudyInstanceUid() throws Exception {


		TransferServiceContextReference tscr = client.retrieveDicomDataByStudyUID("1.1.978.4966.292.1");

		int actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(1,actualNumZipFiles);

		tscr = client.retrieveDicomDataByStudyUIDs(new String[]{"1.1.978.4966.292.1"});
		actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(1,actualNumZipFiles);
	}

	@Test	
	public void testRetrieveDicomDataBySeriesInstanceUid() throws Exception {

		TransferServiceContextReference tscr = client.retrieveDicomDataBySeriesUID("1.1.978.976.890.2");

		int actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(1,actualNumZipFiles);

		tscr = client.retrieveDicomDataBySeriesUIDs(new String[]{"1.1.978.976.890.2"});

		actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(1,actualNumZipFiles);
	}

	@Test
	public void testRetrieveDicomDataByStudyTimepoint() throws Exception {

		TransferServiceContextReference tscr = client.retrieveDicomDataByNthStudyTimePointForPatient("A2", 1);

		int actualNumZipFiles = processTransfer(tscr, globusCred);
		Assert.assertEquals(1,actualNumZipFiles);
	}

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

	////////////////////////////////////////////////PRIVATE//////////////////////////////////////////////////////

	private GlobusCredential globusCred;

	private NCIACoreServiceClient client;


	private void runCQLInDirectory(String relativeDirectoryName) throws Exception {

		System.out.println("identity:"+globusCred.getIdentity());

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
		cqlToCountMap.put("test/resources/publicfilter/private/association/testISPY_ImageSeries.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/association/testISPY_ImageSeriesStudy.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/association/testISPY_ImageSeriesStudyPatient.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/association/testISPY_PatientTDP.xml", 1000);
		//series visibility afffects this one
		cqlToCountMap.put("test/resources/publicfilter/private/association/testISPY_SeriesEquipment.xml", 2);
		cqlToCountMap.put("test/resources/publicfilter/private/association/testISPY_SeriesStudy.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/association/testISPY_SeriesStudyPatient.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/association/testISPY_StudyPatient.xml", 1);

		cqlToCountMap.put("test/resources/publicfilter/private/attribute/testISPY_image.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/attribute/testISPY_patient.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/attribute/testISPY_project.xml", 1000);
		cqlToCountMap.put("test/resources/publicfilter/private/attribute/testISPY_series.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/attribute/testISPY_site.xml", 2);
		cqlToCountMap.put("test/resources/publicfilter/private/attribute/testISPY_study.xml", 1);

		cqlToCountMap.put("test/resources/publicfilter/private/group/andAssocAssoc.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/group/andAttrAssoc.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/group/andAttrAttr.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/group/orAttrAssoc.xml", 2);
		cqlToCountMap.put("test/resources/publicfilter/private/group/orAttrAttr.xml", 46);
	}
}
