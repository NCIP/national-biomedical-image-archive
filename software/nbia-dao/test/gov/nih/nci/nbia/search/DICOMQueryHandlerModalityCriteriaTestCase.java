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
import gov.nih.nci.ncia.criteria.ImageModalityCriteria;
import gov.nih.nci.ncia.criteria.ModalityAndedSearchCriteria;
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

public class DICOMQueryHandlerModalityCriteriaTestCase extends AbstractDbUnitTestForJunit4 {
	@Test
	public void testAllModalityHqlQuery() throws Exception {
        DICOMQuery dicomQuery = new DICOMQuery();
		ModalityAndedSearchCriteria modalityAndedSearchCriteria = new ModalityAndedSearchCriteria();
        modalityAndedSearchCriteria.setModalityAndedSearchValue("all");
		dicomQuery.setCriteria(modalityAndedSearchCriteria);

		ImageModalityCriteria imageModalityCriteria = new ImageModalityCriteria();
		imageModalityCriteria.setImageModalityValue("MR");
		imageModalityCriteria.setImageModalityValue("PT");
		imageModalityCriteria.setImageModalityValue("CT");
		dicomQuery.setCriteria(imageModalityCriteria);

        dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple>  resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("Results4:"+resultSets.size());

		Assert.assertTrue(resultSets.size()==4);
	}
	@Test
	public void testAnyModalityHqlQuery() throws Exception {
        DICOMQuery dicomQuery = new DICOMQuery();

		ImageModalityCriteria imageModalityCriteria = new ImageModalityCriteria();
		imageModalityCriteria.setImageModalityValue("CT");
		dicomQuery.setCriteria(imageModalityCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("Results0:"+resultSets.size());
		Assert.assertTrue(resultSets.size()==7);
		/////////////////
		imageModalityCriteria = new ImageModalityCriteria();
		imageModalityCriteria.setImageModalityValue("PT");
		dicomQuery.setCriteria(imageModalityCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("Results1:"+resultSets.size());

		Assert.assertTrue(resultSets.size()==1);
		/////////////////
		imageModalityCriteria = new ImageModalityCriteria();
		imageModalityCriteria.setImageModalityValue("CT");
		imageModalityCriteria.setImageModalityValue("PT");
		dicomQuery.setCriteria(imageModalityCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("Results2:"+resultSets.size());

		Assert.assertTrue(resultSets.size()==8);

		/////////////////
		imageModalityCriteria = new ImageModalityCriteria();
		imageModalityCriteria.setImageModalityValue("garbage");
		dicomQuery.setCriteria(imageModalityCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		System.out.println("Results3:"+resultSets.size());

		Assert.assertTrue(resultSets.size()==0);
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
