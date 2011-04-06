package gov.nih.nci.ncia.lookup;

import gov.nih.nci.nbia.lookup.LookupManagerImpl;
import gov.nih.nci.ncia.AbstractDbUnitTestForJunit4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class LookupMangerImplTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
    public void testLookupInit() throws Exception {
    	_testModality();
    	_testAnatomicSite();
    	_testDicomKernelType();
    	_testSearchCollection();
    	_testManufacturerModelSoftwareItems();
    	_testUsMultiModalItems();
    }


    //////////////////////////////PROTECTED/////////////////////////////////
    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }

    @Before
	public void setUp() throws Exception {
		Collection<String> authorizedCollections = new ArrayList<String>();
		authorizedCollections.add("CT Colonography");
		authorizedCollections.add("Virtual Colonoscopy");

		lookupMangerImpl = new LookupManagerImpl(authorizedCollections);
	}


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/collections_testdata.xml";

    private LookupManagerImpl lookupMangerImpl;

    private void _testUsMultiModalItems() throws Exception {
    	List<String> multiModalities = lookupMangerImpl.getUsMultiModality();    	

    	Assert.assertTrue(multiModalities.contains("2D Imaging"));
    	Assert.assertTrue(multiModalities.contains("Color Doppler"));
    	Assert.assertEquals(2, multiModalities.size());
    }
    
    
	private void _testModality() throws Exception {
		List<String> modalities = lookupMangerImpl.getModality();

		Assert.assertTrue(modalities.contains("PT"));
		Assert.assertTrue(modalities.contains("CT"));
		Assert.assertTrue(modalities.contains("MR"));
		Assert.assertTrue(modalities.size()==3);
	}

	private void _testAnatomicSite() throws Exception {
		List<String> anatomicSites = lookupMangerImpl.getAnatomicSite();

		for(String a : anatomicSites) {
			System.out.println("anatomy:"+a);
		}
		Assert.assertTrue(anatomicSites.contains("ABDOMEN"));
		Assert.assertTrue(anatomicSites.contains("MRI BRAIN"));
		//Assert.assertTrue(anatomicSites.contains(""));
		Assert.assertTrue(anatomicSites.contains("NOT SPECIFIED"));
		Assert.assertTrue(anatomicSites.size()==3);
	}

	private void _testDicomKernelType() throws Exception {
		List<String> kernelTypes = lookupMangerImpl.getDICOMKernelType();

		Assert.assertTrue(kernelTypes.contains("STANDARD"));
		Assert.assertTrue(kernelTypes.contains("SOFT"));
		Assert.assertTrue(kernelTypes.contains("T20s"));
		Assert.assertTrue(kernelTypes.contains("B30f"));

		Assert.assertTrue(kernelTypes.size()==4);
	}

	private void _testSearchCollection() throws Exception {
		List<String> searchCollection = lookupMangerImpl.getSearchCollection();

		Assert.assertTrue(searchCollection.contains("CT Colonography"));
		Assert.assertTrue(searchCollection.contains("Virtual Colonoscopy"));

		Assert.assertTrue(searchCollection.size()==2);
	}

	private void _testManufacturerModelSoftwareItems() throws Exception {
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
}
