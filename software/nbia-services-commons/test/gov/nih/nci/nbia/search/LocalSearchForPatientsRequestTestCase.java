package gov.nih.nci.nbia.search;

import gov.nih.nci.ncia.criteria.AuthorizationCriteria;
import gov.nih.nci.ncia.criteria.ImageModalityCriteria;
import gov.nih.nci.ncia.search.NBIANode;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.search.LocalSearchForPatientsRequest;
import gov.nih.nci.nbia.search.PatientSearchResults;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class LocalSearchForPatientsRequestTestCase extends AbstractDbUnitTestForJunit4 {
	@Test
	public void testCall() throws Exception {
		NBIANode localNode = new NBIANode(true, "d1", "u1");

        DICOMQuery dicomQuery = new DICOMQuery();

		ImageModalityCriteria imageModalityCriteria = new ImageModalityCriteria();
		imageModalityCriteria.setImageModalityValue("CT");
		dicomQuery.setCriteria(imageModalityCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		LocalSearchForPatientsRequest localSearchForPatientsRequest =
			new LocalSearchForPatientsRequest(localNode, dicomQuery);

		PatientSearchResults patientSearchResults = localSearchForPatientsRequest.call();

		Assert.assertEquals(patientSearchResults.getNode(), localNode);
		Assert.assertTrue(patientSearchResults.getResults().length==4);
	}

    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/collections_testdata.xml";

	private static AuthorizationCriteria createAuthorizationCriteria()
			throws Exception {
		AuthorizationManager am = new AuthorizationManager("kascice");
		List<SiteData> authorizedSites = am.getAuthorizedSites(RoleType.READ);
		List<String> authorizedCollections = am
				.getAuthorizedCollections(RoleType.READ);
		List<String> authorizedSeriesSecurityGroups = am
				.getAuthorizedSeriesSecurityGroups(RoleType.READ);

		AuthorizationCriteria authorizationCriteria = new AuthorizationCriteria();
		authorizationCriteria.setCollections(authorizedCollections);
		authorizationCriteria.setSites(authorizedSites);
		authorizationCriteria
				.setSeriesSecurityGroups(authorizedSeriesSecurityGroups);

		return authorizationCriteria;
	}
}
