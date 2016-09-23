/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion;

import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;
import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class SeriesDeletionTestCase  extends AbstractDbUnitTestForJunit4 {
   // private static final String TEST_DB_FLAT_FILE = "dbunitscripts/collections_deletionTestdata.xml";
    @Autowired
    private ImageDeletionService imageDeletionsvr;
    @Autowired
    private DeletionCheckingTestCaseSupport deletionCheck;
    
    @Test
    public void testDeleteSeries() throws Exception
    {
    	imageDeletionsvr.removeSeries(null);
 
    	Integer id = new Integer(8519680);
    	GeneralSeries series = deletionCheck.getSeries(id);
   		Assert.assertNull(series);

   		id = new Integer(8486912);
    	Study study = deletionCheck.getStudy(id);
    	Assert.assertNull(study);

    	id = new Integer(8454144);
    	Patient patient = deletionCheck.getPatient(id);
    	Assert.assertNull(patient);
    }
    
	protected String getDataSetResourceSpec() {
		return TEST_DB_FLAT_FILE;
	}


    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/collections_deletionTestdata.xml";	
}
