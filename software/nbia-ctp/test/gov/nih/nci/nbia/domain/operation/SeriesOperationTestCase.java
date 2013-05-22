/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.internaldomain.GeneralEquipment;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;
import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;

import java.util.Hashtable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })

public class SeriesOperationTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testValidateNoSopInstanceUid() throws Exception {
		try {
			setUp();
			Hashtable<String, String> numbers = new Hashtable<String, String> ();

			so.validate(numbers);
			Assert.fail("without a series uid, the image shouldnt validate");
		}
		catch(Exception ex) {
			//want to be here
			Assert.assertTrue(true);
		}
	}
    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }

    protected void setUp() throws Exception {

		so = new SeriesOperation();
		so.setEquip(new GeneralEquipment());
		so.setPatient(new Patient());
		so.setStudy(new Study());
    }

    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_353337344.xml";

    @Autowired
    private SeriesOperationInterface so;
}
