/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.nbia.customserieslist;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.nbia.dao.CustomSeriesListDAO;
import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.dto.CustomSeriesDTO;
import gov.nih.nci.nbia.dto.CustomSeriesListAttributeDTO;
import gov.nih.nci.nbia.dto.CustomSeriesListDTO;
import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author lethai
 *
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"gov.nih.nci.nbia.security.NCIASecurityManager$RoleType"})
@PrepareForTest({CustomSeriesListProcessor.class,  
                 SpringApplicationContext.class}) 
public class CustomSeriesListProcessorTestCase {

	/**
	 * This is a really stupid test, but I think this object is ill-defined.
	 */
	@Test	
	public void testIsDuplicateName() throws Exception {
    
        expect(customSeriesListDAOMock.isDuplicateName("LIDC")).
            andReturn(true); 
                   
        replayMock();
        
        //OUT
        CustomSeriesListProcessor customSeriesListProcessor = new CustomSeriesListProcessor("lethai");
        boolean duplicateName = customSeriesListProcessor.isDuplicateName("LIDC");
		Assert.assertTrue(duplicateName);
	}



	@Test	
	public void testSearchByName() {
		CustomSeriesListDTO testDTO = new CustomSeriesListDTO();
		testDTO.setName("LIDC");
		
        expect(customSeriesListDAOMock.findCustomSeriesListByName("LIDC")).
            andReturn(testDTO); 
               
        replayMock();
    
        //OUT
        CustomSeriesListProcessor customSeriesListProcessor = new CustomSeriesListProcessor("lethai");
        CustomSeriesListDTO actualDTO = customSeriesListProcessor.searchByName("LIDC");
        Assert.assertEquals(actualDTO.getName(), "LIDC");		
	}


	@Test	
	public void testGetCustomListByUser() {
		List<CustomSeriesListDTO> testDTOs = new ArrayList<CustomSeriesListDTO>();
		testDTOs.add(new CustomSeriesListDTO());
		testDTOs.add(new CustomSeriesListDTO());
		testDTOs.add(new CustomSeriesListDTO());

		
        expect(customSeriesListDAOMock.findCustomSeriesListByUser("LIDC")).
            andReturn(testDTOs); 
               
        replayMock();
    
        //OUT
        CustomSeriesListProcessor customSeriesListProcessor = new CustomSeriesListProcessor("lethai");
        List<CustomSeriesListDTO> dtos= customSeriesListProcessor.getCustomListByUser("LIDC");
		Assert.assertEquals(dtos.size(), 3);		
	}


	@Test	
	public void testCreate() {
		CustomSeriesListDTO customList = new CustomSeriesListDTO();
		
        expect(customSeriesListDAOMock.insert(customList, "lethai")).
            andReturn(1L); 		
		
        replayMock();
        
        CustomSeriesListProcessor customSeriesListProcessor = new CustomSeriesListProcessor("lethai");		
		long result = customSeriesListProcessor.create(customList);
		
		Assert.assertEquals(result,1);
	}


	@Test	
	public void testGetCustomseriesListAttributesById() {
		
		List<CustomSeriesListAttributeDTO> list = new ArrayList<CustomSeriesListAttributeDTO>();
		list.add(new CustomSeriesListAttributeDTO());
		list.add(new CustomSeriesListAttributeDTO());
		list.add(new CustomSeriesListAttributeDTO());

        expect(customSeriesListDAOMock.findCustomSeriesListAttribute(666)).
            andReturn(list); 
        
        replayMock();

        
        CustomSeriesListProcessor customSeriesListProcessor = new CustomSeriesListProcessor("lethai");		
		List<CustomSeriesListAttributeDTO> returnedList = customSeriesListProcessor.getCustomseriesListAttributesById(666);
		Assert.assertEquals(returnedList.size(), 3);	
	}


	@Test	
	public void testUpdate() {
		CustomSeriesListDTO editList = new CustomSeriesListDTO();
		String customSeriesListName = "Phantom List";
		editList.setName(customSeriesListName);
		editList.setHyperlink("http://www.yahoo.com");
		editList.setComment("JUnit update testing");
		editList.setId(1361281024);
		Boolean updatedSeries=false;
		
        expect(customSeriesListDAOMock.update(editList, "lethai", updatedSeries)).
            andReturn(2L); 		
		
        replayMock();

        CustomSeriesListProcessor customSeriesListProcessor = new CustomSeriesListProcessor("lethai");		
        long result = customSeriesListProcessor.update(editList, updatedSeries);
        
        Assert.assertEquals(result, 2L);      
	}

	@Test
	public void testValidate() {
		List<String> seriesUids = new ArrayList<String>();
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122841");
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122844");
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122847");
		
		List<SeriesDTO> seriesDTOsFound = new ArrayList<SeriesDTO>();
		SeriesDTO dto0 = new SeriesDTO();
		dto0.setSeriesUID("1.3.6.1.4.1.9328.50.4.122841");
		seriesDTOsFound.add(dto0);
		
        expect(generalSeriesDAOMock.findSeriesBySeriesInstanceUID(seriesUids, null,  null)).
            andReturn(seriesDTOsFound);
        
        replayMock();

        CustomSeriesListProcessor customSeriesListProcessor = new CustomSeriesListProcessor("lethai");		
		List<String> noPermissionSeries = customSeriesListProcessor.validate(seriesUids);
		Assert.assertTrue(noPermissionSeries.contains("1.3.6.1.4.1.9328.50.4.122844"));
		Assert.assertTrue(noPermissionSeries.contains("1.3.6.1.4.1.9328.50.4.122847"));
		Assert.assertEquals(noPermissionSeries.size(), 2);
	}


	@Test
	public void testIsAnyPrivate() {
		//fail("Not yet implemented");
		List<String> seriesUids = new ArrayList<String>();
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122841");
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122844");
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122847");
		
		
		List<CustomSeriesDTO> seriesDTOsFound = new ArrayList<CustomSeriesDTO>();
		CustomSeriesDTO dto0 = new CustomSeriesDTO();
		dto0.setSeriesUID("1.3.6.1.4.1.9328.50.4.122841");
		seriesDTOsFound.add(dto0);
		
        expect(customSeriesListDAOMock.findSeriesForPublicCollection(seriesUids, null)).
            andReturn(seriesDTOsFound);
        
        replayMock();

        CustomSeriesListProcessor customSeriesListProcessor = new CustomSeriesListProcessor("lethai");	
		List<String> anyPrivate = customSeriesListProcessor.isAnyPrivate(seriesUids);

		Assert.assertTrue(anyPrivate.contains("1.3.6.1.4.1.9328.50.4.122844"));
		Assert.assertTrue(anyPrivate.contains("1.3.6.1.4.1.9328.50.4.122847"));
		Assert.assertEquals(anyPrivate.size(), 2);
	}

	


	@Test
	public void testGetSeriesDTO() {
		List<String> seriesUids = new ArrayList<String>();
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122841");
		seriesUids.add("1.3.6.1.4.1.9328.50.4.122844");
		
		
		List<SeriesDTO> seriesDTOsFound = new ArrayList<SeriesDTO>();
		SeriesDTO dto0 = new SeriesDTO();
		dto0.setSeriesUID("1.3.6.1.4.1.9328.50.4.122841");
		seriesDTOsFound.add(dto0);
		SeriesDTO dto1 = new SeriesDTO();
		dto1.setSeriesUID("1.3.6.1.4.1.9328.50.4.122844");
		seriesDTOsFound.add(dto1);		
		
        expect(generalSeriesDAOMock.findSeriesBySeriesInstanceUID(seriesUids, null,  null)).
            andReturn(seriesDTOsFound);		

        replayMock();

        CustomSeriesListProcessor customSeriesListProcessor = new CustomSeriesListProcessor("lethai");			
		List<SeriesDTO> seriesDTOs = customSeriesListProcessor.getSeriesDTO(seriesUids);
		Assert.assertTrue(findBySeriesInstanceUid(seriesDTOs,"1.3.6.1.4.1.9328.50.4.122841"));
		Assert.assertTrue(findBySeriesInstanceUid(seriesDTOs,"1.3.6.1.4.1.9328.50.4.122844"));
		Assert.assertEquals(seriesDTOs.size(), 2);
	}	
 
	
    @Before
    public void setUp() throws Exception {
        mockStatic(SpringApplicationContext.class);
        customSeriesListDAOMock = createMock(CustomSeriesListDAO.class);
        generalSeriesDAOMock = createMock(GeneralSeriesDAO.class);
        authorizationManagerMock = createMock(AuthorizationManager.class);  
        
        expectNew(AuthorizationManager.class).
            andReturn(authorizationManagerMock);        

        expect(SpringApplicationContext.getBean("generalSeriesDAO")).
            andReturn(generalSeriesDAOMock);
        expect(SpringApplicationContext.getBean("customSeriesListDAO")).
            andReturn(customSeriesListDAOMock); 
        expect(authorizationManagerMock.getAuthorizedSeriesSecurityGroups()).            
            andReturn(null).
            anyTimes();
        expect(authorizationManagerMock.getAuthorizedSites()).
            andReturn(null).
            anyTimes();
        
        expectNew(AuthorizationManager.class, "lethai").
            andReturn(authorizationManagerMock);        
    }

    @After
    public void tearDown() throws Exception {    
    	verifyMock();
    }
    
    ////////////////////////////////////PRIVATE/////////////////////////////////
   
    private CustomSeriesListDAO customSeriesListDAOMock;
    private GeneralSeriesDAO generalSeriesDAOMock;
    private AuthorizationManager authorizationManagerMock;    
    
	private void replayMock() {
        replay(customSeriesListDAOMock);
        replay(SpringApplicationContext.class);
        replay(authorizationManagerMock, AuthorizationManager.class);
        replay(generalSeriesDAOMock, GeneralSeriesDAO.class); 		
	}
	
	private void verifyMock() {
		verify(customSeriesListDAOMock);
		verify(SpringApplicationContext.class);
		verify(authorizationManagerMock, AuthorizationManager.class);
		verify(generalSeriesDAOMock, GeneralSeriesDAO.class); 	
	}   
	
	private static boolean findBySeriesInstanceUid(List<SeriesDTO> dtos, String seriesInstanceUid) {
		for(SeriesDTO dto : dtos) {
			if(dto.getSeriesUID().equals(seriesInstanceUid)) {
				return true;
			}
		}
		return false;
	}
}
