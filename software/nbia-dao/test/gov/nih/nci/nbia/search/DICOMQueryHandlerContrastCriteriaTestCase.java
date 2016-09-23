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
import gov.nih.nci.ncia.criteria.ContrastAgentCriteria;
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

public class DICOMQueryHandlerContrastCriteriaTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
    public void testUnenhancedConstrastAgentCriteria() throws Exception {
		ContrastAgentCriteria unenhancedCriteria = new ContrastAgentCriteria();
		unenhancedCriteria.setContrastAgentValue(ContrastAgentCriteria.UNENHANCED);

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(unenhancedCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("resultSets:"+resultSets.size());
		for(PatientStudySeriesTriple rs : resultSets) {
			PatientStudySeriesTriple prs = (PatientStudySeriesTriple)rs;
			System.out.println("foo:"+prs.getPatientPkId()+","+prs.getSeriesPkId()+","+prs.getStudyPkId());
		}
		//assert count for contrast - the contrast agent
		//is never loaded into a ImageResultSet so no need
		//to dig down - 14 series have it
		Assert.assertTrue(resultSets.size()==12);
	}
	@Test
	public void testEnhancedContrastAgentCriteria() throws Exception {
		ContrastAgentCriteria enhancedCriteria = new ContrastAgentCriteria();
		enhancedCriteria.setContrastAgentValue(ContrastAgentCriteria.ENHANCED);

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(enhancedCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("resultSets:"+resultSets.size());
		for(PatientStudySeriesTriple rs : resultSets) {
			PatientStudySeriesTriple prs = (PatientStudySeriesTriple)rs;
			System.out.println("foo:"+prs.getPatientPkId()+","+prs.getSeriesPkId()+","+prs.getStudyPkId());
		}
		//assert count for contrast - the contrast agent
		//is never loaded into a ImageResultSet so no need
		//to dig down - two objects since two series in dataset have enhanced
		Assert.assertTrue(resultSets.size()==2);



	}


    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/qalookupmgr_testdata.xml";
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
System.out.println("auth:"+authorizedCollections.size());
		AuthorizationCriteria authorizationCriteria = new AuthorizationCriteria();
		authorizationCriteria.setCollections(authorizedCollections);
		authorizationCriteria.setSites(authorizedSites);
		authorizationCriteria
				.setSeriesSecurityGroups(authorizedSeriesSecurityGroups);

		return authorizationCriteria;
	}

}
