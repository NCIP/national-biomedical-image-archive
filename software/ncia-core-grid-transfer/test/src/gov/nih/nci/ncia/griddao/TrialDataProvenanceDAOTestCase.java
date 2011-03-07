/**
 *
 */
package gov.nih.nci.ncia.griddao;

import java.util.Collections;
import java.util.Map;

import gov.nih.nci.ncia.AbstractDbTestCaseImpl;
import gov.nih.nci.ncia.domain.TrialDataProvenance;

/**
 * @author lethai
 *
 */
public class TrialDataProvenanceDAOTestCase extends AbstractDbTestCaseImpl {


	public void testGetTDPByPatientId() throws Exception{
		TrialDataProvenanceDAO imageFilesProcessor = new TrialDataProvenanceDAO();
		
		Map<String, TrialDataProvenance> trialDataProvenances = imageFilesProcessor.getTDPByPatientId(Collections.singletonList("1.3.6.1.4.1.9328.50.3.0022"));
		
		TrialDataProvenance trialDataProvenance = trialDataProvenances.get("1.3.6.1.4.1.9328.50.3.0022");
		
		assertTrue(trialDataProvenance.getProject().equals("LIDC"));
		assertTrue(trialDataProvenance.getSiteName().equals("LIDC"));
	}


	public void testGetTDPByStudyInstanceUID() throws Exception{
		TrialDataProvenanceDAO imageFilesProcessor = new TrialDataProvenanceDAO();
		Map<String, TrialDataProvenance> trialDataProvenances = imageFilesProcessor.getTDPByStudyInstanceUID(Collections.singletonList("1.3.6.1.4.1.9328.50.3.68"));

		TrialDataProvenance trialDataProvenance = trialDataProvenances.get("1.3.6.1.4.1.9328.50.3.68");
		
		assertTrue(trialDataProvenance.getProject().equals("LIDC"));
		assertTrue(trialDataProvenance.getSiteName().equals("LIDC"));
	}


	public void testGetTDPBySeriesInstanceUID() throws Exception {
		TrialDataProvenanceDAO imageFilesProcessor = new TrialDataProvenanceDAO();
		Map<String, TrialDataProvenance> trialDataProvenances = imageFilesProcessor.getTDPBySeriesInstanceUID(Collections.singletonList("1.3.6.1.4.1.9328.50.3.69"));
		TrialDataProvenance trialDataProvenance = trialDataProvenances.get("1.3.6.1.4.1.9328.50.3.69");
		assertTrue(trialDataProvenance.getProject().equals("LIDC"));
		assertTrue(trialDataProvenance.getSiteName().equals("LIDC"));
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
}
