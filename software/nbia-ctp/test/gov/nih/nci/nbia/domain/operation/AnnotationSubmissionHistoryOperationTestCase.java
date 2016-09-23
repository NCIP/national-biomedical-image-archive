/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.internaldomain.SubmissionHistory;
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

public class AnnotationSubmissionHistoryOperationTestCase extends AbstractDbUnitTestForJunit4 {
	@Autowired
	AnnotationSubmissionHistoryOperationInterface asho;
	
	@Test
	public void testValidateMissingSeries() throws Exception {
	    try{	
	    	asho.setOperation("fakeStudy", "fakeSeries");
	    	asho.validate(null);
	    	Assert.fail();
	    }catch(Exception e){
	    	System.out.println("Test is successed and failed to validate.");
	    }
	}

	@Test
	public void testValidateHappyPath() throws Exception {
		
		    asho.setOperation("2.16.756.5.5.52.3078913146.1137515844.659581",
		    			      "2.16.756.5.5.14.3078913146.1137516344.276618");


		    SubmissionHistory submissionHistory = (SubmissionHistory)asho.validate(null);
		    Assert.assertEquals(submissionHistory.getPatientId(),"00032-056");
		    Assert.assertEquals(submissionHistory.getStudyInstanceUID(),"2.16.756.5.5.52.3078913146.1137515844.659581");
		    Assert.assertEquals(submissionHistory.getSeriesInstanceUID(),"2.16.756.5.5.14.3078913146.1137516344.276618");
		    Assert.assertEquals(submissionHistory.getSite(),"VCU");
		    Assert.assertEquals(submissionHistory.getProject(), "COMBIDEX-VCU");
		    Assert.assertEquals(submissionHistory.getOperationType(), 2);
		    Assert.assertNotNull(submissionHistory.getSubmissionDate());
		    Assert.assertNull(submissionHistory.getSOPInstanceUID());

	}
    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_353337344.xml";

}
