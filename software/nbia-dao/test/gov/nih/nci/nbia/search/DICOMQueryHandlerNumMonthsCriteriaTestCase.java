/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.ncia.criteria.AuthorizationCriteria;
import gov.nih.nci.ncia.criteria.NumOfMonthsCriteria;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.SiteData;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})

public class DICOMQueryHandlerNumMonthsCriteriaTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testNumOfMonthsEqualCriteria() throws Exception {
		DICOMQuery dicomQuery = new DICOMQuery();
		NumOfMonthsCriteria numOfMonthsCriteria = new NumOfMonthsCriteria("=", "9");
		dicomQuery.setCriteria(numOfMonthsCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		//one patient with 15 series
		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue(resultSets.size() == 15);

		numOfMonthsCriteria = new NumOfMonthsCriteria("=", "14");
		dicomQuery.setCriteria(numOfMonthsCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		//one patient with 15 series
		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue(resultSets.size() == 15);

		numOfMonthsCriteria = new NumOfMonthsCriteria("=", "12");
		dicomQuery.setCriteria(numOfMonthsCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		//one patient with 21 series
		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue(resultSets.size() == 21);
	}
	@Test
	public void testNumOfMonthsLtCriteria() throws Exception {
		DICOMQuery dicomQuery = new DICOMQuery();
		NumOfMonthsCriteria numOfMonthsCriteria = new NumOfMonthsCriteria("<", "10");
		dicomQuery.setCriteria(numOfMonthsCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		//one patient with 15 series
		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue(resultSets.size() == 15);
	}

	public void testNumOfMonthsGtCriteria() throws Exception {
		DICOMQuery dicomQuery = new DICOMQuery();
		NumOfMonthsCriteria numOfMonthsCriteria = new NumOfMonthsCriteria(">", "10");
		dicomQuery.setCriteria(numOfMonthsCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue(resultSets.size() == 36);
	}

	public void testNumOfMonthsGteCriteria() throws Exception {
		DICOMQuery dicomQuery = new DICOMQuery();
		NumOfMonthsCriteria numOfMonthsCriteria = new NumOfMonthsCriteria(">=", "12");
		dicomQuery.setCriteria(numOfMonthsCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue(resultSets.size() == 36);
	}

	public void testNumOfMonthsLteCriteria() throws Exception {
		DICOMQuery dicomQuery = new DICOMQuery();
		NumOfMonthsCriteria numOfMonthsCriteria = new NumOfMonthsCriteria("<=", "12");
		dicomQuery.setCriteria(numOfMonthsCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		//unfortunate symmetry
		Assert.assertTrue(resultSets.size() == 36);
	}

    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/min_studies_testdata.xml";
    @Autowired
    private DICOMQueryHandler dicomQueryHandler;

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
