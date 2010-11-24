package gov.nih.nci.ncia.search;

import gov.nih.nci.ncia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.ncia.dto.ImageDTO;
import gov.nih.nci.ncia.security.AuthorizationManager;
import gov.nih.nci.ncia.util.NCIAConfig;

import java.text.SimpleDateFormat;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class LocalDrillDownTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testRetrieveImagesForSeriesSeriesSearchResultNotPublic() {
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setPatientPublic(false);
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());
		
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.setId(102236166);
		seriesSearchResult.setSeriesInstanceUid("1.3.6.1.4.1.9328.50.6.40560");
		ImageSearchResult[] results = localDrillDown.retrieveImagesForSeries(seriesSearchResult);

		Assert.assertTrue(results.length==429);
		boolean found = false;
		for(ImageSearchResult result : results) {
			if(result.getInstanceNumber()==1) {
				found = true;
				
				Assert.assertTrue(result.getId()==102107844);
				Assert.assertTrue(result.getSeriesInstanceUid().equals("1.3.6.1.4.1.9328.50.6.40560"));
				Assert.assertTrue(result.getSopInstanceUid().equals("1.3.6.1.4.1.9328.50.6.40563"));
				Assert.assertTrue(result.getSeriesId()==102236166);
				Assert.assertTrue(result.getSize()==526116);
				Assert.assertTrue(result.getThumbnailURL().equals("foo"));
			}		
		}
		Assert.assertTrue(found);
	}
	@Test
	public void testRetrieveImagesForSeriesSeriesSearchResultPublic() throws Exception {
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setPatientPublic(true);
		localDrillDown.setAuthorizationManager(new AuthorizationManager());
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());
		
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.setId(102236166);
		seriesSearchResult.setSeriesInstanceUid("1.3.6.1.4.1.9328.50.6.40560");
		ImageSearchResult[] results = localDrillDown.retrieveImagesForSeries(seriesSearchResult);

		Assert.assertTrue(results.length==429);

		//	something that returns 0		
		seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.setId(666);
		seriesSearchResult.setSeriesInstanceUid("666");
		results = localDrillDown.retrieveImagesForSeries(seriesSearchResult);
		Assert.assertTrue(results.length==0);		
	}
	
	@Test
	public void testRetrieveStudiesAndSeriesNotPublic() {
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setPatientPublic(false);
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());
		
		PatientSearchResultImpl patientSearchResult = new PatientSearchResultImpl();
		patientSearchResult.setId(8454144);		
		patientSearchResult.addSeriesForStudy(8486912, 8519680);
		patientSearchResult.addSeriesForStudy(8486912, 8716288);
		
		StudySearchResult[] results = localDrillDown.retrieveStudyAndSeriesForPatient(patientSearchResult);

		Assert.assertTrue(results.length==1);
		Assert.assertTrue(results[0].getId()==8486912);
		Assert.assertTrue(results[0].getStudyInstanceUid().equals("1.3.6.1.4.1.9328.50.99.1"));
		Assert.assertTrue(results[0].getDescription().equals("Anonymized"));
		
		//offset desc looks like presentation state jammed in.  maybe get rid of
		//when have more time luxury
		Assert.assertNull(results[0].getOffSetDesc());
		
        SimpleDateFormat sdf = NCIAConfig.getDateFormat();	
		String dateStr = sdf.format(results[0].getDate());	
		Assert.assertTrue(dateStr.equals("2001-01-01"));
		
		Assert.assertTrue(results[0].getSeriesList().length==2);
	}
	
	@Test
	public void testRetrieveStudiesAndSeriesPublic() throws Exception {
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setPatientPublic(true);
		localDrillDown.setAuthorizationManager(new AuthorizationManager());
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());
		
		PatientSearchResultImpl patientSearchResult = new PatientSearchResultImpl();
		patientSearchResult.setId(8454144);		
		patientSearchResult.addSeriesForStudy(8486912, 8519680);
		patientSearchResult.addSeriesForStudy(8486912, 8716288);
		
		StudySearchResult[] results = localDrillDown.retrieveStudyAndSeriesForPatient(patientSearchResult);

		Assert.assertTrue(results.length==1);
		Assert.assertTrue(results[0].getId()==8486912);
		Assert.assertTrue(results[0].getStudyInstanceUid().equals("1.3.6.1.4.1.9328.50.99.1"));
		Assert.assertTrue(results[0].getDescription().equals("Anonymized"));
		
		//offset desc looks like presentation state jammed in.  maybe get rid of
		//when have more time luxury
		Assert.assertNull(results[0].getOffSetDesc());
		
        SimpleDateFormat sdf = NCIAConfig.getDateFormat();	
		String dateStr = sdf.format(results[0].getDate());	
		Assert.assertTrue(dateStr.equals("2001-01-01"));
		
		Assert.assertTrue(results[0].getSeriesList().length==2);
		
		patientSearchResult = new PatientSearchResultImpl();
		patientSearchResult.setId(666);		
		
		results = localDrillDown.retrieveStudyAndSeriesForPatient(patientSearchResult);
		Assert.assertTrue(results.length==0);
	}
	@Test
	public void testRetrieveImagesForSeriesId() throws Exception {
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());

		ImageSearchResult[] results = localDrillDown.retrieveImagesForSeries(8716288);
		Assert.assertTrue(results.length==4481-4069+1);
		
		boolean found = false;
		for(ImageSearchResult result : results) {
			if(result.getInstanceNumber()==1) {
				found = true;
				
				Assert.assertTrue(result.getId()==8650783);
				Assert.assertTrue(result.getSeriesInstanceUid().equals("1.3.6.1.4.1.9328.50.99.456"));
				Assert.assertTrue(result.getSopInstanceUid().equals("1.3.6.1.4.1.9328.50.99.458"));
				Assert.assertTrue(result.getSeriesId()==8716288);
				Assert.assertTrue(result.getSize()==526130.000000);
				Assert.assertTrue(result.getThumbnailURL().equals("foo"));
			}		
		}
		Assert.assertTrue(found);		
		
	}
	
	@Test
	public void testRetrieveImagesForSeriesBySeriesInstanceUid() throws Exception {
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());

		ImageSearchResult[] results = localDrillDown.retrieveImagesForSeries("1.3.6.1.4.1.9328.50.99.456");
		Assert.assertTrue(results.length==4481-4069+1);
		
		boolean found = false;
		for(ImageSearchResult result : results) {
			if(result.getInstanceNumber()==1) {
				found = true;
				
				Assert.assertTrue(result.getId()==8650783);
				Assert.assertTrue(result.getSeriesInstanceUid().equals("1.3.6.1.4.1.9328.50.99.456"));
				Assert.assertTrue(result.getSopInstanceUid().equals("1.3.6.1.4.1.9328.50.99.458"));
				Assert.assertTrue(result.getSeriesId()==8716288);
				Assert.assertTrue(result.getSize()==526130.000000);
				Assert.assertTrue(result.getThumbnailURL().equals("foo"));
			}		
		}
		Assert.assertTrue(found);		
		
	}
	
	@Test
	public void testRetrieveSeriesByInstanceUid() {
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());

		SeriesSearchResult result = localDrillDown.retrieveSeries("1.3.6.1.4.1.9328.50.99.456");
		Assert.assertNotNull(result);
		
		Assert.assertTrue(result.getId()==8716288);
		Assert.assertTrue(result.getSeriesInstanceUid().equals("1.3.6.1.4.1.9328.50.99.456"));
		Assert.assertNull(result.getDescription());
		Assert.assertNull(result.getManufacturer());
		Assert.assertTrue(result.getModality().equals("CT"));	
		
		Assert.assertTrue(result.getPatientId().equals("WRAMC VC-001M"));		
		Assert.assertTrue(result.getProject().equals("Virtual Colonoscopy"));		
		Assert.assertTrue(result.getSeriesNumber().equals("2"));		
		Assert.assertTrue(result.getStudyInstanceUid().equals("1.3.6.1.4.1.9328.50.99.1"));		
		Assert.assertTrue(result.getStudyId()==8486912);		
	}
	
	@Test
	public void testRetrieveSeriesById() {
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());

		SeriesSearchResult result = localDrillDown.retrieveSeries(8716288);
		Assert.assertNotNull(result);
		
		Assert.assertTrue(result.getId()==8716288);
		Assert.assertTrue(result.getSeriesInstanceUid().equals("1.3.6.1.4.1.9328.50.99.456"));
		Assert.assertNull(result.getDescription());
		Assert.assertNull(result.getManufacturer());
		Assert.assertTrue(result.getModality().equals("CT"));	
		
		Assert.assertTrue(result.getPatientId().equals("WRAMC VC-001M"));		
		Assert.assertTrue(result.getProject().equals("Virtual Colonoscopy"));
		Assert.assertTrue(result.getSeriesNumber().equals("2"));		
		Assert.assertTrue(result.getStudyInstanceUid().equals("1.3.6.1.4.1.9328.50.99.1"));		
		Assert.assertTrue(result.getStudyId()==8486912);		
	}	
	
	
	public static class FakeThumbnailURLResolver implements ThumbnailURLResolver {
		/**
		 * For a given local DICOM image, return a URL (String) to get to a thumbnail of it.	 
		 */	
		public String resolveThumbnailUrl(ImageDTO imageDto) {
			return "foo";
		}
	}	
	
    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }
	
    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/collections_testdata.xml";
}
