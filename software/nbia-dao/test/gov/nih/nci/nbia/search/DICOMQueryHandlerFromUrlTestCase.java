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
import gov.nih.nci.ncia.criteria.UrlParamCriteria;
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

public class DICOMQueryHandlerFromUrlTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testString() throws Exception {
		DICOMQuery dicomQuery = new DICOMQuery();

        UrlParamCriteria crit = new UrlParamCriteria();
        crit.setPatientId("1.3.6.1.4.1.9328.50.4.0112");
        crit.setStudyInstanceUid("1.3.6.1.4.1.9328.50.4.122840");
        crit.setSeriesInstanceUid("1.3.6.1.4.1.9328.50.4.122841");
        //crit handler doesnt use this????
        //crit.setImageSopInstanceUid(urlParams.getImage1ImageSopInstanceUid());

        dicomQuery.addUrlParamCriteria(crit);

        UrlParamCriteria crit2 = new UrlParamCriteria();
        crit2.setPatientId("SD VC-096M");
        crit2.setStudyInstanceUid("1.3.6.1.4.1.9328.50.6.40559");
        crit2.setSeriesInstanceUid("1.3.6.1.4.1.9328.50.6.40560");
        //crit2.setImageSopInstanceUid(urlParams.getImage2ImageSopInstanceUid());

        dicomQuery.addUrlParamCriteria(crit2);

		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
//		System.out.println("resultsSets:"+resultSets.size());
//
//		for(ResultSet resultSet : resultSets) {
//			PatientResultSet patientResultSet = (PatientResultSet)resultSet;
//			System.out.println("patientResultSet:"+patientResultSet.getPatientPkId());
//		}
		Assert.assertTrue(resultSets.size()==2);
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
