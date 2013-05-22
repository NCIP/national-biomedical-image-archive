/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;
import gov.nih.nci.nbia.internaldomain.SubmissionHistory;
import gov.nih.nci.nbia.internaldomain.TrialDataProvenance;
import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.util.DicomConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

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

public class ImageSubmissionHistoryOperationTestCase extends AbstractDbUnitTestForJunit4 {
	@Autowired
	TrialDataProvenance tdp;
	@Autowired
	GeneralSeries series;
	@Autowired
	ImageSubmissionHistoryOperationInterface asho;
	
	
	@Test
	public void testNew() throws Exception {
			tdp.setProject("fakeProject");
			tdp.setDpSiteName("fakeSite");

			Patient patient = new Patient();
			patient.setPatientId("patientId");

			Study study = new Study();
			study.setStudyInstanceUID("st1");

			series.setSeriesInstanceUID("ser1");

		    asho.setProperties(false,tdp,patient,study,series);

		    Map numbers = new HashMap();
		    numbers.put(DicomConstants.SOP_INSTANCE_UID, "sop1");
		    numbers.put("current_timestamp", new Date());
		    
		    SubmissionHistory submissionHistory = (SubmissionHistory)asho.validate(numbers);
		    Assert.assertEquals(submissionHistory.getPatientId(),"patientId");
		    Assert.assertEquals(submissionHistory.getStudyInstanceUID(),"st1");
		    Assert.assertEquals(submissionHistory.getSeriesInstanceUID(),"ser1");
		    Assert.assertEquals(submissionHistory.getProject(),"fakeProject");
		    Assert.assertEquals(submissionHistory.getSite(), "fakeSite");
		    Assert.assertEquals(submissionHistory.getOperationType(), 0);
		    Assert.assertEquals(submissionHistory.getSOPInstanceUID(),"sop1");
		    Assert.assertNotNull(submissionHistory.getSubmissionDate());
	}

	@Test
	public void testReplacement() throws Exception {
			tdp.setProject("fakeProject");
			tdp.setDpSiteName("fakeSite");

			Patient patient = new Patient();
			patient.setPatientId("patientId");

			Study study = new Study();
			study.setStudyInstanceUID("st1");

			GeneralSeries series = new GeneralSeries();
			series.setSeriesInstanceUID("ser1");

		    asho.setProperties(true,tdp,patient,study,series);

		    Map numbers = new HashMap();
		    numbers.put(DicomConstants.SOP_INSTANCE_UID, "sop1");
		    numbers.put("current_timestamp", new Date());

		    SubmissionHistory submissionHistory = (SubmissionHistory)asho.validate(numbers);
		    Assert.assertEquals(submissionHistory.getPatientId(),"patientId");
		    Assert.assertEquals(submissionHistory.getStudyInstanceUID(),"st1");
		    Assert.assertEquals(submissionHistory.getSeriesInstanceUID(),"ser1");
		    Assert.assertEquals(submissionHistory.getProject(),"fakeProject");
		    Assert.assertEquals(submissionHistory.getSite(), "fakeSite");
		    Assert.assertEquals(submissionHistory.getOperationType(), 1);
		    Assert.assertEquals(submissionHistory.getSOPInstanceUID(),"sop1");

		    Assert.assertNotNull(submissionHistory.getSubmissionDate());
	}
	
	@Autowired
	Patient patient;
	@Autowired
	Study study;
	
	
	
	@Test
	public void testMissingSop() throws Exception {
	    try {

			tdp.setProject("fakeProject");
			tdp.setDpSiteName("fakeSite");

			patient.setPatientId("patientId");

			study.setStudyInstanceUID("st1");
			
			series.setSeriesInstanceUID("ser1");

		    
		    asho.setProperties(false,tdp,patient,study,series);

		    Map numbers = new HashMap();
		    SubmissionHistory submissionHistory = (SubmissionHistory)asho.validate(numbers);
		    Assert.fail("shuoldnt get here");
	    }
	    catch(Exception ex) {
	    	//want to be here
	    }
	}
    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }

    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_353337344.xml";


}
