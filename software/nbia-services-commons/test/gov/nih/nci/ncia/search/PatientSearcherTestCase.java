package gov.nih.nci.ncia.search;

import gov.nih.nci.nbia.criteria.AuthorizationCriteria;
import gov.nih.nci.nbia.criteria.ImageModalityCriteria;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.ncia.AbstractDbUnitTestForJunit4;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class PatientSearcherTestCase extends AbstractDbUnitTestForJunit4 {

	/**
	 * Just test reduction of triples to patients... do the
	 * heavy lifting of all criteria permutations in the DICOM Query Handler
	 * tests
	 */
	@Test
	public void testSearchForPatients() throws Exception{
        DICOMQuery dicomQuery = new DICOMQuery();

		ImageModalityCriteria imageModalityCriteria = new ImageModalityCriteria();
		imageModalityCriteria.setImageModalityValue("CT");
		dicomQuery.setCriteria(imageModalityCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());


        List<PatientSearchResult> results = patientSearcher.searchForPatients(dicomQuery);
        System.out.println("results:"+results);
        //3 patients have CT series (out of 6 triples)
        Assert.assertTrue(results.size()==4);
	}


    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }

    @Before
	public void setUp() throws Exception {

		patientSearcher = new PatientSearcher();
	}


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/collections_testdata.xml";

    private PatientSearcher patientSearcher;


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
