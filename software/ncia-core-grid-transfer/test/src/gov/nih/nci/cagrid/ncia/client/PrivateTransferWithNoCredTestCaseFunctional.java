/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.cagrid.ncia.client;

import java.io.File;

import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;
import org.junit.Assert;
import org.junit.Test;

public class PrivateTransferWithNoCredTestCaseFunctional extends TransferServiceTestCaseFunctional {
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
		runCQLInDirectory("test/resources/publicfilter/private/association");
	}

	@Test	
	public void testAttributes() throws Exception {
		runCQLInDirectory("test/resources/publicfilter/private/attribute");
	}

	@Test	
	public void testGroups() throws Exception {
		runCQLInDirectory("test/resources/publicfilter/private/group");
	}

	@Test	
	public void testRetrieveDicomDataByPatientId() throws Exception {
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl, null);
		client.setAnonymousPrefered(true);

		TransferServiceContextReference tscr = client.retrieveDicomDataByPatientId("A2");

		int actualNumZipFiles = processTransfer(tscr, null);
		Assert.assertEquals(0,actualNumZipFiles);
	}

	@Test	
	public void testRetrieveDicomDataByStudyInstanceUid() throws Exception {
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl, null);
		client.setAnonymousPrefered(true);

		TransferServiceContextReference tscr = client.retrieveDicomDataByStudyUID("1.1.978.4966.292.1");

		int actualNumZipFiles = processTransfer(tscr, null);
		Assert.assertEquals(0,actualNumZipFiles);
	}

	@Test	
	public void testRetrieveDicomDataBySeriesInstanceUid() throws Exception {
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl, null);
		client.setAnonymousPrefered(true);

		TransferServiceContextReference tscr = client.retrieveDicomDataBySeriesUID("1.1.978.976.890.2");

		int actualNumZipFiles = processTransfer(tscr, null);
		Assert.assertEquals(0,actualNumZipFiles);
	}

	@Test
	public void testRetrieveDicomDataByStudyTimepoint() throws Exception {
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl, null);
		client.setAnonymousPrefered(true);

		TransferServiceContextReference tscr = client.retrieveDicomDataByNthStudyTimePointForPatient("A2", 1);

		int actualNumZipFiles = processTransfer(tscr, null);
		Assert.assertEquals(0,actualNumZipFiles);
	}


	private void runCQLInDirectory(String relativeDirectoryName) throws Exception {
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);

		File[] cqlFiles = getCQLFiles(relativeDirectoryName);
		for(File cqlFile : cqlFiles) {
			System.out.println("CQL:"+cqlFile.getName());
			int actualNumZipFiles = sendCQLForZipFile(client, cqlFile, null);
			Assert.assertEquals(actualNumZipFiles, 0);
		}
	}
}
