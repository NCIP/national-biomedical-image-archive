/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.ncia.criteria.AnatomicalSiteCriteria;
import gov.nih.nci.ncia.criteria.AuthorizationCriteria;
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
public class DICOMQueryHandlerAnatomicalSiteCriteriaTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testAnatomicalSiteHqlQuery() throws Exception {
        DICOMQuery dicomQuery = new DICOMQuery();
        AnatomicalSiteCriteria anatomicalSiteCriteria = new AnatomicalSiteCriteria();
        anatomicalSiteCriteria.setAnatomicalSiteValue("ABDOMEN");
		dicomQuery.setCriteria(anatomicalSiteCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("results:"+resultSets.size());
		Assert.assertTrue(resultSets.size()==2);

		////////////////
        anatomicalSiteCriteria = new AnatomicalSiteCriteria();
        anatomicalSiteCriteria.setAnatomicalSiteValue("garbage");
		dicomQuery.setCriteria(anatomicalSiteCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("results:"+resultSets.size());
		Assert.assertTrue(resultSets.size()==0);

		////////////////
        anatomicalSiteCriteria = new AnatomicalSiteCriteria();
        anatomicalSiteCriteria.setAnatomicalSiteValue("ABDOMEN");
        anatomicalSiteCriteria.setAnatomicalSiteValue("MRI BRAIN");
		dicomQuery.setCriteria(anatomicalSiteCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("results:"+resultSets.size());
		Assert.assertTrue(resultSets.size()==3);

	}

	@Test
	public void testAnatomicalSiteNotSpecifiedOnly() throws Exception {
        DICOMQuery dicomQuery = new DICOMQuery();
        AnatomicalSiteCriteria anatomicalSiteCriteria = new AnatomicalSiteCriteria();
        //not i18n friendly
        anatomicalSiteCriteria.setAnatomicalSiteValue("NOT SPECIFIED");
		dicomQuery.setCriteria(anatomicalSiteCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("not spec only results:"+resultSets.size());
		Assert.assertTrue(resultSets.size()==6);
	}

	@Test
	public void testAnatomicalSiteNotSpecifiedAndThenSome() throws Exception {
        DICOMQuery dicomQuery = new DICOMQuery();
        AnatomicalSiteCriteria anatomicalSiteCriteria = new AnatomicalSiteCriteria();
        //not i18n friendly
        anatomicalSiteCriteria.setAnatomicalSiteValue("NOT SPECIFIED");
        anatomicalSiteCriteria.setAnatomicalSiteValue("ABDOMEN");
		dicomQuery.setCriteria(anatomicalSiteCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("spec and then some results:"+resultSets.size());
		Assert.assertTrue(resultSets.size()==8);

	}

    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }

    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/collections_testdata.xml";

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
