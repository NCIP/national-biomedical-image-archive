/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.dto.CustomSeriesListAttributeDTO;
import gov.nih.nci.nbia.dto.CustomSeriesListDTO;

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
public class CustomSeriesListDAOTestCase extends AbstractDbUnitTestForJunit4 {
    @Test	
	public void testFindCustomSeriesListByUser() throws Exception{
		List<CustomSeriesListDTO> dtos = customSeriesListDAO.findCustomSeriesListByUser("lethai");
		System.out.println("size: " + dtos.size());
		Assert.assertEquals(dtos.size(), 9);
	}
    @Test	
	public void testFindCustomSeriesListByName() throws Exception{
		String customSeriesListName = "LIDC";
		CustomSeriesListDTO listDTO = customSeriesListDAO.findCustomSeriesListByName(customSeriesListName);
		
		Assert.assertEquals(listDTO.getName(),customSeriesListName);
	}
    @Test	
	public void testIsDuplicateName() throws Exception{
		String customSeriesListName = "LIDC";
		boolean duplicateName = customSeriesListDAO.isDuplicateName(customSeriesListName);
		
		Assert.assertTrue(duplicateName);
	}
    @Test	
	public void testUpdateList() throws Exception{		
		CustomSeriesListDTO editList = new CustomSeriesListDTO();
		editList.setName("Phantom List");
		editList.setHyperlink("http://www.yahoo.com");
		editList.setComment("JUnit update testing");
		editList.setId(1361281024);
		Boolean updatedSeries=false;
		customSeriesListDAO.update(editList, "lethai", updatedSeries);
		
		String customSeriesListName = "Phantom List";
		CustomSeriesListDTO listDTO = customSeriesListDAO.findCustomSeriesListByName(customSeriesListName);
		
		Assert.assertEquals(listDTO.getName(),customSeriesListName);		
	}
    @Test	
	public void testUpdateListWithSeries() throws Exception{
		CustomSeriesListDTO editList = new CustomSeriesListDTO();
		List<CustomSeriesListAttributeDTO> attributeDtos = new ArrayList<CustomSeriesListAttributeDTO>();
		CustomSeriesListAttributeDTO dto = new CustomSeriesListAttributeDTO();
		dto.setSeriesInstanceUid("1.2.840.113619.2.5.1762388187.1886.1039082614.131");
		dto.setParentId(1361379328);
		attributeDtos.add(dto);
		editList.setName("LIDC");
		editList.setHyperlink("http://www.yahoo.com");
		editList.setId(1361379328);
		editList.setSeriesInstanceUidsList(attributeDtos);
		Boolean updatedSeries=true;
		customSeriesListDAO.update(editList, "lethai", updatedSeries);
		/* retrieve to make sure it's updated successfully */
		CustomSeriesListDTO listDTO = customSeriesListDAO.findCustomSeriesListByName("LIDC");
		Assert.assertEquals(listDTO.getName(), "LIDC");
		Assert.assertEquals(listDTO.getHyperlink(), "http://www.yahoo.com");
		Assert.assertTrue(listDTO.getSeriesInstanceUIDs().size() == 1);
		
	}
    @Test	
	public void testInsertList() throws Exception{
		List<String> seriesInstanceUIDs = new ArrayList<String>();
		seriesInstanceUIDs.add("1.2.840.113619.2.5.1762388187.1886.1039082614.131");
		seriesInstanceUIDs.add("1.2.840.113619.2.5.1762388187.1886.1039082614.163");
		seriesInstanceUIDs.add("1.2.840.113619.2.5.1762388187.1886.1039082614.192");
		CustomSeriesListDTO customList = new CustomSeriesListDTO();
		customList.setName("JUnit Test List");
		customList.setHyperlink("http://www.yahoo.com");
		customList.setComment("testing");
		customList.setSeriesInstanceUIDs(seriesInstanceUIDs);
		customSeriesListDAO.insert(customList, "lethai");
		
		String customSeriesListName = "JUnit Test List";
		CustomSeriesListDTO listDTO = customSeriesListDAO.findCustomSeriesListByName(customSeriesListName);
		
		Assert.assertEquals(listDTO.getName(),customSeriesListName);
	}

	 //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }

	
    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/custom_series_list.xml";
    
    @Autowired
    private CustomSeriesListDAO customSeriesListDAO;
}
