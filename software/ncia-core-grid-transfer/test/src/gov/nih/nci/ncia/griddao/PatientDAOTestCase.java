/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.griddao;

import java.util.Date;
import java.util.List;

import gov.nih.nci.ncia.AbstractDbUnitTestForJunit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml", "/applicationContext-hibernate-testContext.xml"})
public class PatientDAOTestCase extends AbstractDbUnitTestForJunit4{
    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_1044.xml";
    
    @Autowired
    private PatientDAOInterface patientDaoInterface;    
    
	@Test
	public void testTimepointStudyForPatient() throws Exception{
		
		List<Date> dates = patientDaoInterface.getTimepointStudyForPatient("1.3.6.1.4.1.9328.50.3.0022");
		String d1 = ((Date)dates.get(0)).toString();
		Assert.assertTrue(d1.equalsIgnoreCase("2000-01-01 00:00:00.0"));
		dates = patientDaoInterface.getTimepointStudyForPatient("1.3.6.1.4.1.9328.50.3.0023");
		String d2 = ((Date)dates.get(0)).toString();
		Assert.assertTrue(d2.equalsIgnoreCase("2000-01-01 00:00:00.0"));
	}
	
    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }
}
