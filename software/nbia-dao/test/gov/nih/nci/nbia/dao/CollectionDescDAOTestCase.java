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
package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.dto.CollectionDescDTO;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * @author lethai
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class CollectionDescDAOTestCase extends AbstractDbUnitTestForJunit4 {

	

	/**
	 * Test method for {@link gov.nih.nci.ncia.dao.CollectionDescDAO#findCollectionNames()}.
	 */
    @Test	
	public void testFindCollectionNames() {
		List<String> findCollectionNames = collectionDescDAO.findCollectionNames();
		System.out.println("findCollectionNames : " + findCollectionNames + " size: " + findCollectionNames.size() );
		Assert.assertTrue(findCollectionNames.size() == 18);
	}

	/**
	 * Test method for {@link gov.nih.nci.ncia.dao.CollectionDescDAO#findCollectionDescByCollectionName(java.lang.String)}.
	 */
    @Test    
	public void testFindCollectionDescByCollectionName() {
		CollectionDescDTO collectionDescByCollectionName = collectionDescDAO.findCollectionDescByCollectionName("RIDER Pilot");
		String collectionName = collectionDescByCollectionName.getCollectionName();
		String retrievedDescription = collectionDescByCollectionName.getDescription();
		String description = "<p>This data collection was originally supported under supplemental funding for the  LIDC U01 project and focused on the collection of " +
				"longitudinal studies using  X-ray CT for monitoring the response to therapy. The data came primarily from  MDACC and several of the LIDC academic sites. " +
				"The data is not annotated. However  it contains a sub-set of 30 longitudinal cases that are annotated using the  RECIST criteria.</p>";
		
		System.out.println("name: " + collectionName + " desc: " + retrievedDescription);
		Assert.assertEquals(collectionName, "RIDER Pilot");
		Assert.assertEquals(retrievedDescription, description);
	}

//    @Test    
//	public void testUpdateAndInsert() {
//		//Testing update
//		CollectionDescDTO updateDto = new CollectionDescDTO();
//		updateDto.setCollectionName("LIDC");
//		updateDto.setId(1377992704);
//		updateDto.setDescription("&lt;p&gt;LIDC&amp;nbsp; - Lung Imaging Database Consortium&lt;/p&gt; Testing");
//		collectionDescDAO.update(updateDto);
//		
//		CollectionDescDTO collectionDescByCollectionName = collectionDescDAO.findCollectionDescByCollectionName("LIDC");
//		String collectionName = collectionDescByCollectionName.getCollectionName();
//		String retrievedDescription = collectionDescByCollectionName.getDescription();
//		
//		Assert.assertEquals(collectionName, "LIDC");
//		Assert.assertEquals(retrievedDescription, "&lt;p&gt;LIDC&amp;nbsp; - Lung Imaging Database Consortium&lt;/p&gt; Testing");
//		
//		//Testing insert
//		CollectionDescDTO insertDto = new CollectionDescDTO();
//		insertDto.setCollectionName("JUnit Test");
//		
//		insertDto.setDescription("Testing");
//		collectionDescDAO.insert(insertDto);
//		
//		collectionDescByCollectionName = collectionDescDAO.findCollectionDescByCollectionName("JUnit Test");
//		collectionName = collectionDescByCollectionName.getCollectionName();
//		retrievedDescription = collectionDescByCollectionName.getDescription();
//		
//		Assert.assertEquals(collectionName, "JUnit Test");
//		Assert.assertEquals(retrievedDescription, "Testing");
//	}

		
	 //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }
	
	
////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/collection_descriptions.xml";
    
    @Autowired
    private CollectionDescDAO collectionDescDAO;

}
