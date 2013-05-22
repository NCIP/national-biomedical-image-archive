/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.cagrid.ncia.client;

import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.ncia.util.SecureClientUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.globus.gsi.GlobusCredential;
import org.junit.Assert;
import org.junit.Test;

//this is geared toward DEV tier with ISPY collection
//would be better to control the population.... but we'll just go with
//this since ISPY isn't going to be submitted to any time soon as far as I know
public class PrivateQueriesWithCredTestCaseFunctional extends DataServiceTestCaseFunctional {
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

	////////////////////////////////////////PRIVATE////////////////////////////////////////////

	private void runCQLInDirectory(String relativeDirectoryName) throws Exception {
	    String gridUsername = System.getProperty("grid.user.name");
	    String gridPassword = System.getProperty("grid.password");

		GlobusCredential globusCred = SecureClientUtil.generateGlobusCredential(gridUsername,
				                                                                gridPassword,
				                                                                dorianURL,
				                                                                authUrl);
		System.out.println("identity:"+globusCred.getIdentity());
		System.out.println("gridServiceUrl:"+gridServiceUrl);

		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl, globusCred);
		client.setAnonymousPrefered(false);

		File[] cqlFiles = getCQLFiles(relativeDirectoryName);
		for(File cqlFile : cqlFiles) {
			System.out.println("CQL:"+cqlFile.getName());
			CQLQueryResults results = sendCQLQuery(client, cqlFile);
			if(results.getObjectResult()==null) {
				Assert.fail("nulll results");
			}
			else {
				Integer resultLen = cqlToCountMap.get(constructKey(relativeDirectoryName,cqlFile));
				Assert.assertTrue(results.getObjectResult().length == resultLen.intValue());
			}
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
		cqlToCountMap.put("test/resources/publicfilter/private/association/testISPY_PatientTDP.xml", 7);
		//series visibility afffects this one
		cqlToCountMap.put("test/resources/publicfilter/private/association/testISPY_SeriesEquipment.xml", 2);
		cqlToCountMap.put("test/resources/publicfilter/private/association/testISPY_SeriesStudy.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/association/testISPY_SeriesStudyPatient.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/association/testISPY_StudyPatient.xml", 3);

		cqlToCountMap.put("test/resources/publicfilter/private/attribute/testISPY_image.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/attribute/testISPY_patient.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/attribute/testISPY_project.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/attribute/testISPY_series.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/attribute/testISPY_site.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/attribute/testISPY_study.xml", 1);

		cqlToCountMap.put("test/resources/publicfilter/private/group/andAssocAssoc.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/group/andAttrAssoc.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/group/andAttrAttr.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/private/group/orAttrAssoc.xml", 3);
		cqlToCountMap.put("test/resources/publicfilter/private/group/orAttrAttr.xml", 2);
	}
}
