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
public class PublicQueriesWithNoCredTestCaseFunctional extends DataServiceTestCaseFunctional {
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

	////////////////////////////////////////PRIVATE////////////////////////////////////////////

	private void runCQLInDirectory(String relativeDirectoryName) throws Exception {
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);
		client.setAnonymousPrefered(true);

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
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_ImageSeries.xml", 325);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_ImageSeriesStudy.xml", 551);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_ImageSeriesStudyPatient.xml", 551);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_PatientTDP.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_SeriesStudy.xml", 5);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_SeriesStudyPatient.xml", 5);
		cqlToCountMap.put("test/resources/publicfilter/public/association/testTCGA_StudyPatient.xml", 1);

		cqlToCountMap.put("test/resources/publicfilter/public/attribute/testTCGA_image.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/public/attribute/testTCGA_patient.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/public/attribute/testTCGA_project.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/public/attribute/testTCGA_series.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/public/attribute/testTCGA_site.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/public/attribute/testTCGA_study.xml", 1);

		cqlToCountMap.put("test/resources/publicfilter/public/group/andAssocAssoc.xml", 5);
		cqlToCountMap.put("test/resources/publicfilter/public/group/andAttrAssoc.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/public/group/andAttrAttr.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/public/group/orAttrAssoc.xml", 1);
		cqlToCountMap.put("test/resources/publicfilter/public/group/orAttrAttr.xml", 2);
	}
}
