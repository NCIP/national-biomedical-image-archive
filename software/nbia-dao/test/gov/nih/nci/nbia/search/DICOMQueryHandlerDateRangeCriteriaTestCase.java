/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.ncia.criteria.AuthorizationCriteria;
import gov.nih.nci.ncia.criteria.DateRangeCriteria;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.SiteData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})

public class DICOMQueryHandlerDateRangeCriteriaTestCase extends AbstractDbUnitTestForJunit4 {

    //must have both from and to otherwise no filtering
	@Test	
	public void testDateRangeFromAndTo() throws Exception {
		DateRangeCriteria dateRangeCriteria = new DateRangeCriteria();

		SimpleDateFormat sdf = NCIAConfig.getDateFormat();
		Date fromDate = sdf.parse("2006-11-19");
		dateRangeCriteria.setFromDate(fromDate);

		Date toDate = sdf.parse("2006-11-21");
		dateRangeCriteria.setToDate(toDate);

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(dateRangeCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue(resultSets.size()==2);

		////////////////////////////////////////////////////

		fromDate = sdf.parse("2006-11-16");
		dateRangeCriteria.setFromDate(fromDate);
		toDate = sdf.parse("2006-11-18");
		dateRangeCriteria.setToDate(toDate);
		dicomQuery.setCriteria(dateRangeCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue(resultSets.size()==3);

		////////////////////////////////////////////////////

		fromDate = sdf.parse("2006-11-16");
		dateRangeCriteria.setFromDate(fromDate);
		toDate = sdf.parse("2006-11-21");
		dateRangeCriteria.setToDate(toDate);
		dicomQuery.setCriteria(dateRangeCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue(resultSets.size()==5);
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
