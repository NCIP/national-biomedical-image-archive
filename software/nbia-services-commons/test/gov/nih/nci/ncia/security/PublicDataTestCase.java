package gov.nih.nci.ncia.security;

import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.ncia.AbstractDbUnitTestForJunit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class PublicDataTestCase extends AbstractDbUnitTestForJunit4 {

	private int nonpublic_patientPkID = 198;
	private int nonpublic_seriesPkId = 3146;
	private int nonpublic_imagePkId = 591060;
	
	private int public_patientPkId = 8454144;
	private int public_seriesPkId = 8519680;
	private int public_imagePkId = 8650859;
	
	@Test
	public void testCheckPublicData() throws Exception
	{
		AuthorizationManager am = new AuthorizationManager();
		
		PublicData pd = new PublicData();
		pd.setAuthorizationManager(am);
		
		boolean isPatientPublic = pd.checkPublicPatient(new Integer(nonpublic_patientPkID));
		boolean isSeriesPublic = pd.checkPublicSeries(new Integer(nonpublic_seriesPkId));
		boolean isImagePublic = pd.checkPublicImage(new Integer(nonpublic_imagePkId));
		
		Assert.assertFalse(isPatientPublic);
		Assert.assertFalse(isSeriesPublic);
		Assert.assertFalse(isImagePublic);
		
		isPatientPublic = pd.checkPublicPatient(new Integer(public_patientPkId));
		isSeriesPublic = pd.checkPublicSeries(new Integer(public_seriesPkId));
		isImagePublic = pd.checkPublicImage(new Integer(public_imagePkId));
		
		Assert.assertTrue(isPatientPublic);
		Assert.assertTrue(isSeriesPublic);
		Assert.assertTrue(isImagePublic);
	}
	
	////////////////////// private //////////////////////////////////////////////
	 private static final String TEST_DB_FLAT_FILE = "dbunitscripts/PublicTestData.xml";

	 @Override
	protected String getDataSetResourceSpec() {
	    return TEST_DB_FLAT_FILE;
	}

}
