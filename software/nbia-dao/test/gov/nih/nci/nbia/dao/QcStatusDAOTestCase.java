/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.dto.QcSearchResultDTO;
import gov.nih.nci.nbia.dto.QcStatusHistoryDTO;

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
public class QcStatusDAOTestCase extends AbstractDbUnitTestForJunit4 {


	@Test
	public void testFindSeries() throws Exception {
		String[] qcStatus = {"Visible"};
		List<String> collectionSites = new ArrayList();
		collectionSites.add("LIDC//LIDC");
		String[] patients={"1.3.6.1.4.1.9328.50.3.0023"};
		List<QcSearchResultDTO> qsrDTOs = qcStatusDAO.findSeries(qcStatus,collectionSites, patients);

		Assert.assertTrue(qsrDTOs.get(0).getPatientId().equals("1.3.6.1.4.1.9328.50.3.0023"));
		Assert.assertTrue(qsrDTOs.get(0).getCollectionSite().equals("LIDC//LIDC"));
		Assert.assertTrue(qsrDTOs.get(0).getVisibility().equals("1"));
	}

	@Test	
	public void testUpdateQcStatus() throws Exception {
		List<String> seriesList = new ArrayList();
		seriesList.add("1.3.6.1.4.1.9328.50.3.195");
		List<String> statusList = new ArrayList();
		statusList.add("1");
		String newStatus = "2";
		String userName = "Tester";
		String comment ="no comment";
		String[] newQcStatus = {"Not Visible"};
		List<String> collectionSites = new ArrayList();
		collectionSites.add("LIDC//LIDC");
		String[] patients={"1.3.6.1.4.1.9328.50.3.0023"};
		qcStatusDAO.updateQcStatus(seriesList, statusList, newStatus, userName, comment);
		List<QcSearchResultDTO> qsrDTOs = qcStatusDAO.findSeries(newQcStatus,collectionSites, patients);
		Assert.assertTrue(qsrDTOs.size()>0);
		statusList.remove(0);
		statusList.add("2");
		qcStatusDAO.updateQcStatus(seriesList, statusList, "1", userName, comment);
		List<QcStatusHistoryDTO> qshDTOs = qcStatusDAO.findQcStatusHistoryInfo(seriesList);
		Assert.assertTrue(qshDTOs.size()>0);
	}




    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/qcStatus_1044.xml";

    @Autowired
	private QcStatusDAO qcStatusDAO;


}
