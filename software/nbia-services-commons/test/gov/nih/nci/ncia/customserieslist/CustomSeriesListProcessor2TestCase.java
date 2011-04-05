/**
 * 
 */
package gov.nih.nci.ncia.customserieslist;

import gov.nih.nci.ncia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.ncia.dto.SeriesDTO;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * Test two test cases fo the CustomSeriesListProcessor but using different data set.
 * @author lethai
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class CustomSeriesListProcessor2TestCase extends AbstractDbUnitTestForJunit4 {

	
	/**
	 * Test method for {@link gov.nih.nci.ncia.customserieslist.CustomSeriesListProcessor#validate(java.util.List)}.
	 */
	@Test
	public void testValidate() {
		List<String> seriesUids = new ArrayList<String>();
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122841");
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122844");
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122847");
		List<String> noPermissionSeries = customSeriesListProcessor.validate(seriesUids);
		System.out.println("noPermissionSeries: " + noPermissionSeries.size());
		Assert.assertTrue(noPermissionSeries.size() == 0);
	}

	/**
	 * Test method for {@link gov.nih.nci.ncia.customserieslist.CustomSeriesListProcessor#isAnyPrivate(java.util.List)}.
	 */
	@Test
	public void testIsAnyPrivate() {
		//fail("Not yet implemented");
		List<String> seriesUids = new ArrayList<String>();
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122841");
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122844");
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122847");
		List<String> anyPrivate = customSeriesListProcessor.isAnyPrivate(seriesUids);
		System.out.println("anyPrivate: ===== " + anyPrivate.size());
		Assert.assertTrue(anyPrivate.size() == 0);
	}

	

	/**
	 * Test method for {@link gov.nih.nci.ncia.customserieslist.CustomSeriesListProcessor#getSeriesDTO(java.util.List)}.
	 */
	@Test
	public void testGetSeriesDTO() {
		List<String> seriesUids = new ArrayList<String>();
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122841");
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122844");
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122847");
		List<SeriesDTO> seriesDTO = customSeriesListProcessor.getSeriesDTO(seriesUids);
		System.out.println("anyPrivate: " + seriesDTO.size());
		for(int i=0; i<seriesDTO.size(); i++){
			System.out.println("series: " + seriesDTO.get(i).getSeriesId());
		}
		Assert.assertTrue(seriesDTO.size() ==3);
	}

	

	
//////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }
    
    @Before
	public void setUp() throws Exception {
		customSeriesListProcessor = new CustomSeriesListProcessor("kascice");
	}

	
////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/collections_testdata.xml";
    private CustomSeriesListProcessor customSeriesListProcessor;
}
