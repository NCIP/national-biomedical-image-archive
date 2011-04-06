/**
 * 
 */
package gov.nih.nci.nbia.customserieslist;

import gov.nih.nci.nbia.dto.CustomSeriesListAttributeDTO;
import gov.nih.nci.nbia.dto.CustomSeriesListDTO;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lethai
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class CustomSeriesListProcessorTestCase extends AbstractDbUnitTestForJunit4 {

	/**
	 * Test method for {@link gov.nih.nci.nbia.customserieslist.CustomSeriesListProcessor#CustomSeriesListProcessor(java.lang.String, gov.nih.nci.nbia.security.AuthorizationManager)}.
	 */
	@Test
	public void testCustomSeriesListProcessor() throws Exception {
		AuthorizationManager am = new AuthorizationManager("lethai");
		System.out.println(am.getAuthorizedCollections());
		System.out.println(am.getAuthorizedSites());
		customSeriesListProcessor = new CustomSeriesListProcessor("lethai", am);
		Assert.assertTrue(customSeriesListProcessor != null);
	}

	/**
	 * Test method for {@link gov.nih.nci.nbia.customserieslist.CustomSeriesListProcessor#validate(java.util.List)}.
	 */
	@Test	
	public void testValidate() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link gov.nih.nci.nbia.customserieslist.CustomSeriesListProcessor#isAnyPrivate(java.util.List)}.
	 */
	@Test	
	public void testIsAnyPrivate() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link gov.nih.nci.nbia.customserieslist.CustomSeriesListProcessor#isDuplicateName(java.lang.String)}.
	 */
	@Test	
	public void testIsDuplicateName() {
		boolean duplicateName = customSeriesListProcessor.isDuplicateName("LIDC");
		Assert.assertTrue(duplicateName);
	}

	/**
	 * Test method for {@link gov.nih.nci.nbia.customserieslist.CustomSeriesListProcessor#searchByName(java.lang.String)}.
	 */
	@Test	
	public void testSearchByName() {
		CustomSeriesListDTO dto = customSeriesListProcessor.searchByName("LIDC");
		Assert.assertEquals(dto.getName(), "LIDC");
	}

	/**
	 * Test method for {@link gov.nih.nci.nbia.customserieslist.CustomSeriesListProcessor#getSeriesDTO(java.util.List)}.
	 */
	@Test	
	public void testGetSeriesDTO() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link gov.nih.nci.nbia.customserieslist.CustomSeriesListProcessor#getCustomListByUser(java.lang.String)}.
	 */
	@Test	
	public void testGetCustomListByUser() {
		List<CustomSeriesListDTO> dtos = customSeriesListProcessor.getCustomListByUser("lethai");
		System.out.println("size: " + dtos.size());
		Assert.assertEquals(dtos.size(), 9);
	}

	/**
	 * Test method for {@link gov.nih.nci.nbia.customserieslist.CustomSeriesListProcessor#create(gov.nih.nci.nbia.dto.CustomSeriesListDTO)}.
	 */
	@Test	
	public void testCreate() {
		List<String> seriesInstanceUIDs = new ArrayList<String>();
		seriesInstanceUIDs.add("1.2.840.113619.2.5.1762388187.1886.1039082614.131");
		seriesInstanceUIDs.add("1.2.840.113619.2.5.1762388187.1886.1039082614.163");
		seriesInstanceUIDs.add("1.2.840.113619.2.5.1762388187.1886.1039082614.192");
		CustomSeriesListDTO customList = new CustomSeriesListDTO();
		customList.setName("JUnit Test List");
		customList.setHyperlink("http://www.yahoo.com");
		customList.setComment("testing");
		customList.setSeriesInstanceUIDs(seriesInstanceUIDs);
		customList.setUserName("lethai");
		customSeriesListProcessor.create(customList);
		
		String customSeriesListName = "JUnit Test List";
		CustomSeriesListDTO listDTO = customSeriesListProcessor.searchByName(customSeriesListName);
		
		Assert.assertEquals(listDTO.getName(),customSeriesListName);
	}

	/**
	 * Test method for {@link gov.nih.nci.nbia.customserieslist.CustomSeriesListProcessor#getCustomseriesListAttributesById(java.lang.Integer)}.
	 */
	@Test	
	public void testGetCustomseriesListAttributesById() {
		List<CustomSeriesListAttributeDTO> returnedList = customSeriesListProcessor.getCustomseriesListAttributesById(1378287616);
		Assert.assertEquals(returnedList.size(), 51);
	}

	/**
	 * Test method for {@link gov.nih.nci.nbia.customserieslist.CustomSeriesListProcessor#update(gov.nih.nci.nbia.dto.CustomSeriesListDTO, java.lang.Boolean)}.
	 */
	@Test	
	public void testUpdate() {
		CustomSeriesListDTO editList = new CustomSeriesListDTO();
		String customSeriesListName = "Phantom List";
		editList.setName(customSeriesListName);
		editList.setHyperlink("http://www.yahoo.com");
		editList.setComment("JUnit update testing");
		editList.setId(1361281024);
		Boolean updatedSeries=false;
		customSeriesListProcessor.update(editList, updatedSeries);
		
		CustomSeriesListDTO dto = customSeriesListProcessor.searchByName(customSeriesListName);
		Assert.assertEquals(dto.getName(), customSeriesListName);
		Assert.assertEquals(dto.getComment(), "JUnit update testing");
	}
//////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }
	
    @Before
    public void setUp() throws Exception {
		customSeriesListProcessor = new CustomSeriesListProcessor("lethai");
	}

	
////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/custom_series_list.xml";
    private CustomSeriesListProcessor customSeriesListProcessor;
}
