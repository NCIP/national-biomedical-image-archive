package gov.nih.nci.cagrid.ncia.client;

import java.io.File;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;

public class PrivateQueriesWithNoCredTestCaseFunctional  extends DataServiceTestCaseFunctional {
	String gridServiceUrl;
	{
		if(System.getProperty("grid.service.url")==null) {
			gridServiceUrl = "https://nciavgriddev5004.nci.nih.gov:8443/wsrf/services/cagrid/NCIACoreService";
		}
		else {
			gridServiceUrl = System.getProperty("grid.service.url");
		}		
	}

	public void testAssociations() throws Exception {
		runCQLInDirectory("test/resources/publicfilter/private/association");
	}

	public void testAttributes() throws Exception {
		runCQLInDirectory("test/resources/publicfilter/private/attribute");
	}

	public void testGroups() throws Exception {
		runCQLInDirectory("test/resources/publicfilter/private/group");
	}

	private void runCQLInDirectory(String relativeDirectoryName) throws Exception {
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);

		File[] cqlFiles = getCQLFiles(relativeDirectoryName);
		for(File cqlFile : cqlFiles) {
			System.out.println("CQL:"+cqlFile.getName());
			CQLQueryResults results = sendCQLQuery(client, cqlFile);
			if(results.getObjectResult()!=null) {
				if(results.getObjectResult().length>0) {
					fail("was able to access a private item with no credentials");
				}
			}
		}

	}
}
