/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.querystorage;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.dto.QueryHistoryDTO;
import gov.nih.nci.nbia.dto.SavedQueryDTO;
import gov.nih.nci.nbia.security.AuthorizationManager;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class QueryStorageManagerTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testGetSavedQueryCount() {

		int i = 0;

		try {

			// get Site information and Handle authorization
			AuthorizationManager am = new AuthorizationManager(userName);
			i = qsm.getSavedQueryCount(userName);

		} catch (Exception e) {
			// System.out.println("Exception: " + e.toString());
			e.printStackTrace();
			Assert.fail("Exception: " + e.toString());
		}

		Assert.assertTrue("getSavedQueryCount returned expected number of results: "
				+ i, i >= 1);

	}

	public void testRetrieveQueryHistory() {

		List<QueryHistoryDTO> queryHistoryRecords = new ArrayList<QueryHistoryDTO>();
		try {
			queryHistoryRecords = qsm.retrieveQueryHistory(userName);
		} catch (Exception e) {
			// System.out.println("Exception: " + e.toString());
			e.printStackTrace();
			Assert.fail("Exception: " + e.toString());
		}

		Assert.assertNotNull("Returned null lisit of queryHistoryRecords", queryHistoryRecords);
		Assert.assertTrue("retrieveQueryHistory returned expected number of queryHistoryRecords: " + queryHistoryRecords.size(), queryHistoryRecords.size() >= 1);

	}

	@Test	
	public void testRetrieveSavedQueries() {
		List<SavedQueryDTO> savedQueries = new ArrayList<SavedQueryDTO>();
		try {
			savedQueries = qsm.retrieveSavedQueries(userName);
		} catch (Exception e) {
			// System.out.println("Exception: " + e.toString());
			e.printStackTrace();
			Assert.fail("Exception: " + e.toString());
		}
		Assert.assertNotNull("Returned null lisit of retrieveSavedQueries", savedQueries);
		Assert.assertTrue("retrieveSavedQueries returned expected number of SavedQueries: " + savedQueries.size(), savedQueries.size() >= 1);
	}

	@Test
	public void testgetListOfActiveNoNew() {
		List<SavedQueryDTO> savedQueries = new ArrayList<SavedQueryDTO>();

		try {
			savedQueries = qsm.getListOfActiveNoNew();
		} catch (Exception e) {
			// System.out.println("Exception: " + e.toString());
			e.printStackTrace();
			Assert.fail("Exception: " + e.toString());
		}
		Assert.assertNotNull("Returned null lisit of getListOfActiveNoNew", savedQueries);
		Assert.assertTrue("getListOfActiveNoNew returned expected number of SavedQueries: " + savedQueries.size(), savedQueries.size() >= 1);

	}

    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
        return TEST_DB_FLAT_FILE;
    }

    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/saved_query.xml";

    @Autowired
	private QueryStorageManager qsm;
	private String userName = "porankisv";
}
