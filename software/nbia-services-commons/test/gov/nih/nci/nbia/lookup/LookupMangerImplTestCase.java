/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.lookup;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.dao.ImageDAO;
import gov.nih.nci.nbia.dto.EquipmentDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"gov.nih.nci.nbia.security.NCIASecurityManager$RoleType"})
@PrepareForTest({LookupManagerImpl.class,  
                 SpringApplicationContext.class}) 
public class LookupMangerImplTestCase {

	@Test    
	public void testModality() throws Exception {
		
		List<String> modalities = lookupMangerImpl.getModality();

		Assert.assertTrue(modalities.contains("PT"));
		Assert.assertTrue(modalities.contains("CT"));
		Assert.assertTrue(modalities.contains("MR"));
		Assert.assertTrue(modalities.size()==3);
	}	
	
	@Test    
	public void testUsMultiModalItems() throws Exception {
    	List<String> multiModalities = lookupMangerImpl.getUsMultiModality();    	
    	Assert.assertTrue(multiModalities.contains("2D Imaging"));
    	Assert.assertTrue(multiModalities.contains("Color Doppler"));
    	Assert.assertEquals(2, multiModalities.size());
    }	

	@Test 
	public void testAnatomicSite() throws Exception {
		List<String> anatomicSites = lookupMangerImpl.getAnatomicSite();

		Assert.assertTrue(anatomicSites.contains("ABDOMEN"));
		Assert.assertTrue(anatomicSites.contains("MRI BRAIN"));
		Assert.assertTrue(anatomicSites.contains("NOT SPECIFIED"));
		Assert.assertTrue(anatomicSites.size()==3);
	}
	
	@Test 
	public void testSearchCollection() throws Exception {
		List<String> searchCollection = lookupMangerImpl.getSearchCollection();

		Assert.assertTrue(searchCollection.contains("CT Colonography"));
		Assert.assertTrue(searchCollection.contains("Virtual Colonoscopy"));

		Assert.assertTrue(searchCollection.size()==2);
	}	
	

	@Test 
	public void testDicomKernelType() throws Exception {
		List<String> kernelTypes = lookupMangerImpl.getDICOMKernelType();
		Assert.assertTrue(kernelTypes.contains("STANDARD"));
		Assert.assertTrue(kernelTypes.contains("SOFT"));
		Assert.assertTrue(kernelTypes.contains("T20s"));
		Assert.assertTrue(kernelTypes.contains("B30f"));

		Assert.assertTrue(kernelTypes.size()==4);
	}
	


	@Test 
	public void testManufacturerModelSoftwareItems() throws Exception {
		Map<String, Map<String, Set<String>>> equipment = lookupMangerImpl.getManufacturerModelSoftwareItems();

		//all 4 series go to 1 piece of equipment
		Assert.assertTrue(equipment.size()==1);

		String manufacturerName = equipment.keySet().iterator().next();
		Assert.assertEquals(manufacturerName, "SIEMENS");

		Map<String, Set<String>> models = equipment.get(manufacturerName);
		String modelName = models.keySet().iterator().next();
		Assert.assertEquals(modelName, "Sensation 16");

		String version = models.get(modelName).iterator().next();
		Assert.assertEquals(version, "VA70C");
	}	
	
    @Before
	public void setUp() throws Exception {
	
        mockStatic(SpringApplicationContext.class);
        generalSeriesDAOMock = createMock(GeneralSeriesDAO.class);
        imageDAOMock = createMock(ImageDAO.class);
              
        expect(SpringApplicationContext.getBean("imageDAO")).
            andReturn(imageDAOMock).anyTimes();
        expect(SpringApplicationContext.getBean("generalSeriesDAO")).
            andReturn(generalSeriesDAOMock);
    
        setupModalities();
        setupUsMultiModalities();
        setupAnatomicSiteModalities();
        setupSearchCollections();
        setupKernelTypes();
        setupEquipment();
        
        replayMock();
        
		Collection<String> authorizedCollections = new ArrayList<String>();
		authorizedCollections.add("CT Colonography");
		authorizedCollections.add("Virtual Colonoscopy");        
		lookupMangerImpl = new LookupManagerImpl(authorizedCollections);        
	}

    @After
    public void tearDown() {
    	verifyMock();
    }

    ////////////////////////////////////PRIVATE/////////////////////////////////
    
    private GeneralSeriesDAO generalSeriesDAOMock;

    private ImageDAO imageDAOMock;
   
    private LookupManagerImpl lookupMangerImpl;

    private void setupUsMultiModalities() {
    	List<String> multiModalities = new ArrayList<String>();    	

    	multiModalities.add("0001");
    	multiModalities.add("0010"); 
    	
        expect(imageDAOMock.findAllImageType()).
            andReturn(multiModalities);          
    }
    
    
    private void setupModalities() {
    	List<String> modalityList = new ArrayList<String>();
        modalityList.add("CT");
        modalityList.add("PT");
        modalityList.add("MR");        
        expect(generalSeriesDAOMock.findDistinctModalitiesFromVisibleSeries()).
                andReturn(modalityList);   
        
    }
    
    private void setupAnatomicSiteModalities() {
    	List<String> anatomicSites = new ArrayList<String>();
        anatomicSites.add("ABDOMEN");
        anatomicSites.add("MRI BRAIN");
        anatomicSites.add("NOT SPECIFIED");        
        expect(generalSeriesDAOMock.findDistinctBodyPartsFromVisibleSeries()).
                andReturn(anatomicSites);   
        
    }
    
    private void setupSearchCollections() {
    	List<String> searchCollections = new ArrayList<String>();
    	searchCollections.add("CT Colonography");
    	searchCollections.add("Virtual Colonoscopy");
        expect(generalSeriesDAOMock.findProjectsOfVisibleSeries()).
                andReturn(searchCollections);   
        
    }
    
    private void setupKernelTypes() {
    	List<String> kernelTypes = new ArrayList<String>();
    	kernelTypes.add("STANDARD");
    	kernelTypes.add("SOFT");
    	kernelTypes.add("T20s");
    	kernelTypes.add("B30f");
   	
        expect(imageDAOMock.findDistinctConvolutionKernels()).
                andReturn(kernelTypes);   
        
    }    
    
    private void setupEquipment() {
   	
        Collection<EquipmentDTO> resultSetList = new ArrayList<EquipmentDTO>();
        EquipmentDTO dto0 = new EquipmentDTO("SIEMENS", "Sensation 16", "VA70C");
        resultSetList.add(dto0);

        expect(generalSeriesDAOMock.findEquipmentOfVisibleSeries()).
                andReturn(resultSetList);   
        
    }     
    
	private void replayMock() {
        replay(SpringApplicationContext.class);
        replay(ImageDAO.class, imageDAOMock);
        replay(GeneralSeriesDAO.class, generalSeriesDAOMock);    	        
        
	}
	
	private void verifyMock() {
		verify(SpringApplicationContext.class);
		verify(generalSeriesDAOMock, GeneralSeriesDAO.class);
		verify(imageDAOMock, ImageDAO.class); 	
		
	}   
}
