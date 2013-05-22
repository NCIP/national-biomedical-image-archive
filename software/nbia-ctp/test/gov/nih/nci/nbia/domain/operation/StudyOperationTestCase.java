/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.internaldomain.Patient;

import java.util.Hashtable;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })

public class StudyOperationTestCase  extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testValidateNoStudyInstanceUid() throws Exception {
		try {
			setUp();
			Hashtable<String, String> numbers = new Hashtable<String, String> ();

			so.validate(numbers);
			Assert.fail("without a study uid, the image shouldnt validate");
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

    @Autowired
	private Patient patient;
    protected void setUp() throws Exception {
    
		so.setPatient(patient);
    }

    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_353337344.xml";

    @Autowired
    private StudyOperationInterface so;
}
