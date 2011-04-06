/**
 * 
 */
package gov.nih.nci.nbia.collectiondescription;

import gov.nih.nci.nbia.dto.CollectionDescDTO;
import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;

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
public class CollectionDescProcessorTestCase extends AbstractDbUnitTestForJunit4 {

	/**
	 * Test method for {@link gov.nih.nci.nbia.collectiondescription.CollectionDescProcessor#CollectionDescProcessor()}.
	 */
	@Test
	public void testCollectionDescProcessor() {
		CollectionDescProcessor processor = new CollectionDescProcessor();
		Assert.assertTrue(processor != null);
	}

	/**
	 * Test method for {@link gov.nih.nci.nbia.collectiondescription.CollectionDescProcessor#getCollectionNames()}.
	 */
	@Test	
	public void testGetCollectionNames() {
		List<String> collectionNames = processor.getCollectionNames();
		Assert.assertTrue(collectionNames.contains("LIDC"));
	}

	/**
	 * Test method for {@link gov.nih.nci.nbia.collectiondescription.CollectionDescProcessor#getCollectionDescByCollectionName(java.lang.String)}.
	 */
	@Test	
	public void testGetCollectionDescByCollectionName() {
		CollectionDescDTO dto = processor.getCollectionDescByCollectionName("RIDER Pilot");
		String description = "<p>This data collection was originally supported under supplemental funding for the  LIDC U01 project and focused on the collection of " +
		"longitudinal studies using  X-ray CT for monitoring the response to therapy. The data came primarily from  MDACC and several of the LIDC academic sites. " +
		"The data is not annotated. However  it contains a sub-set of 30 longitudinal cases that are annotated using the  RECIST criteria.</p>";

		Assert.assertTrue(dto.getCollectionName().equals("RIDER Pilot"));
		Assert.assertEquals(dto.getDescription(), description);
		Assert.assertTrue(dto.getUserName().equals("zengje"));
	}

	/**
	 * Test method for {@link gov.nih.nci.nbia.collectiondescription.CollectionDescProcessor#update(gov.nih.nci.nbia.dto.CollectionDescDTO)}.
	 */
	@Test	
	public void testUpdate() {
		CollectionDescDTO updateDto = new CollectionDescDTO();
		updateDto.setCollectionName("LIDC");
		updateDto.setId(1377992704);
		updateDto.setDescription("&lt;p&gt;LIDC&amp;nbsp; - Lung Imaging Database Consortium&lt;/p&gt; Testing");
		long update = processor.update(updateDto);
		Assert.assertTrue(update > 0L);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		processor = new CollectionDescProcessor();
	}

	@Override
	protected String getDataSetResourceSpec() {
		return TEST_DB_FLAT_FILE;
	}
//////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/collection_descriptions.xml";
    private CollectionDescProcessor processor;
}
