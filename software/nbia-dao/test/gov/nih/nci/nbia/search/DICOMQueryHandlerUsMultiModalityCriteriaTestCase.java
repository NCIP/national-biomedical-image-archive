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
import gov.nih.nci.ncia.criteria.UsMultiModalityCriteria;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.SiteData;
import java.util.List;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class DICOMQueryHandlerUsMultiModalityCriteriaTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testFindUsMultiModality() throws Exception {
		DICOMQuery dicomQuery = new DICOMQuery();
		UsMultiModalityCriteria usMultiModalityCriteria = new UsMultiModalityCriteria();
		usMultiModalityCriteria.setUsMultiModalityValue("2D Imaging");
		dicomQuery.setCriteria(usMultiModalityCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		
		for(PatientStudySeriesTriple t : resultSets) {
			System.out.println("t:"+t.getPatientPkId());
		}
		
		Assert.assertSame(resultSets.size(),1);

	}
	

    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/ultrasound.xml";
    
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
