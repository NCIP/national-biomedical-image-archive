/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.griddao;

import gov.nih.nci.ncia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.ncia.domain.TrialDataProvenance;

import java.util.Collections;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml", "/applicationContext-hibernate-testContext.xml"})
public class TrialDataProvenanceDAOTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testGetTDPByPatientId() throws Exception{		
		Map<String, TrialDataProvenance> trialDataProvenances = trialDataProvenanceDAO.getTDPByPatientId(Collections.singletonList("1.3.6.1.4.1.9328.50.3.0022"));
		
		TrialDataProvenance trialDataProvenance = trialDataProvenances.get("1.3.6.1.4.1.9328.50.3.0022");
		
		Assert.assertTrue(trialDataProvenance.getProject().equals("LIDC"));
		Assert.assertTrue(trialDataProvenance.getSiteName().equals("LIDC"));
	}

	@Test
	public void testGetTDPByStudyInstanceUID() throws Exception{
		Map<String, TrialDataProvenance> trialDataProvenances = trialDataProvenanceDAO.getTDPByStudyInstanceUID(Collections.singletonList("1.3.6.1.4.1.9328.50.3.68"));

		TrialDataProvenance trialDataProvenance = trialDataProvenances.get("1.3.6.1.4.1.9328.50.3.68");
		
		Assert.assertTrue(trialDataProvenance.getProject().equals("LIDC"));
		Assert.assertTrue(trialDataProvenance.getSiteName().equals("LIDC"));
	}

	@Test
	public void testGetTDPBySeriesInstanceUID() throws Exception {
		Map<String, TrialDataProvenance> trialDataProvenances = trialDataProvenanceDAO.getTDPBySeriesInstanceUID(Collections.singletonList("1.3.6.1.4.1.9328.50.3.69"));
		TrialDataProvenance trialDataProvenance = trialDataProvenances.get("1.3.6.1.4.1.9328.50.3.69");
		Assert.assertTrue(trialDataProvenance.getProject().equals("LIDC"));
		Assert.assertTrue(trialDataProvenance.getSiteName().equals("LIDC"));
	}




//	public void testGetTDPByListIds() {
//		TrialDataProvenanceDAO imageFilesProcessor = new TrialDataProvenanceDAO(
//		        System.getProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS),
//		        System.getProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL),
//		        System.getProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME),
//		        System.getProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD));
//
//		List<String> ids = new ArrayList<String>();
//
//		Map<String, TrialDataProvenance> xxxx = imageFilesProcessor.getTDPByListIds(ids,
//				                                                                   "PATIENT_ID");
//
//		System.out.println("Size:"+sopToFilePathMap.size());
//		assertTrue(sopToFilePathMap.size()==139);
//
//		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.330.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-58000.dcm"));
//		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.331.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-58001.dcm"));
//		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.332.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-58002.dcm"));
//		assertTrue(sopToFilePathMap.get("1.3.6.1.4.1.9328.50.3.333.dcm").equals("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-58003.dcm"));
//	}


    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_1044.xml";
    
    @Autowired
    private TrialDataProvenanceDAOInterface trialDataProvenanceDAO;      
}
