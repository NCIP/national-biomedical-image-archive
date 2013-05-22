/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.ncia.criteria.AnnotationOptionCriteria;
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
public class DICOMQueryHandlerAnnotationCriteriaTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testNoConditionCriteria() throws Exception {
		AnnotationOptionCriteria annotationOptionCriteria = new AnnotationOptionCriteria();
		annotationOptionCriteria.setAnnotationOptionValue(AnnotationOptionCriteria.NoCondition);

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(annotationOptionCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("resultSets:"+resultSets.size());
		//assert count for contrast - the contrast agent
		//is never loaded into a ImageResultSet so no need
		//to dig down
		Assert.assertTrue(resultSets.size()==14);
	}
	@Test
	public void testNoAnnotationCriteria() throws Exception {
		AnnotationOptionCriteria annotationOptionCriteria = new AnnotationOptionCriteria();
		annotationOptionCriteria.setAnnotationOptionValue(AnnotationOptionCriteria.NoAnnotation);

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(annotationOptionCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("resultSets:"+resultSets.size());
		//assert count for contrast - the contrast agent
		//is never loaded into a ImageResultSet so no need
		//to dig down
		Assert.assertTrue(resultSets.size()==12);
	}
	@Test
	public void testAnnotationOnlyCriteria() throws Exception {
		AnnotationOptionCriteria annotationOptionCriteria = new AnnotationOptionCriteria();
		annotationOptionCriteria.setAnnotationOptionValue(AnnotationOptionCriteria.AnnotationOnly);

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(annotationOptionCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("resultSets:"+resultSets.size());
		for(PatientStudySeriesTriple prs : resultSets) {
			System.out.println("foo:"+prs.getPatientPkId()+","+prs.getSeriesPkId()+","+prs.getStudyPkId());
		}
		//assert count for contrast - the contrast agent
		//is never loaded into a ImageResultSet so no need
		//to dig down
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

		AuthorizationCriteria authorizationCriteria = new AuthorizationCriteria();
		authorizationCriteria.setCollections(authorizedCollections);
		authorizationCriteria.setSites(authorizedSites);
		authorizationCriteria
				.setSeriesSecurityGroups(authorizedSeriesSecurityGroups);

		return authorizationCriteria;
	}

}
