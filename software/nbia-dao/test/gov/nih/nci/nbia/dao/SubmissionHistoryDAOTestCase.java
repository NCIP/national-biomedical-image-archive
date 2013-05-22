/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.dto.DayCountDTO;
import gov.nih.nci.nbia.dto.SeriesSubmissionCountDTO;
import gov.nih.nci.nbia.dto.SubmissionCountsDTO;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class SubmissionHistoryDAOTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testFindSeriesSubmissionCountInSingleDay() throws Exception {
        Calendar end = new GregorianCalendar();
        end.set(2006,7,4);
        Calendar start = new GregorianCalendar();
        start.set(2006,7,4);
        System.out.println(start.getTime());
        List<SeriesSubmissionCountDTO> seriesSubmissinCountDtos =
		    submissionHistoryDAO.findSeriesSubmissionCountInTimeFrame(start.getTime(),
			    	                                                  end.getTime(),
				                                                      "p1",
				                                                      "s1",
				                                                      0);

        Assert.assertEquals(seriesSubmissinCountDtos.size(),2);

        SeriesSubmissionCountDTO dto = seriesSubmissinCountDtos.get(0);
        SeriesSubmissionCountDTO dto1 = seriesSubmissinCountDtos.get(1);

        if(!dto.getSeriesInstanceUid().equals("3.1")) {
        	dto = seriesSubmissinCountDtos.get(1);
            dto1 = seriesSubmissinCountDtos.get(0);
        }

        Assert.assertEquals(dto.getPatientId(),"1.1");
        Assert.assertEquals(dto.getStudyInstanceUid(),"2.1");
        Assert.assertEquals(dto.getSeriesInstanceUid(),"3.1");
        Assert.assertEquals(dto.getSubmissionCount(),2);

        Assert.assertEquals(dto1.getPatientId(),"1.1");
        Assert.assertEquals(dto1.getStudyInstanceUid(),"2.2");
        Assert.assertEquals(dto1.getSeriesInstanceUid(),"3.2");
        Assert.assertEquals(dto1.getSubmissionCount(),2);
	}

	@Test	
	public void testFindSeriesSubmissionCountInTimeFrame() throws Exception {
        Calendar end = new GregorianCalendar();
        end.set(2006,7,5);
        Calendar start = new GregorianCalendar();
        start.set(2006,7,4);
        System.out.println(start.getTime());
        List<SeriesSubmissionCountDTO> seriesSubmissinCountDtos =
		    submissionHistoryDAO.findSeriesSubmissionCountInTimeFrame(start.getTime(),
			    	                                                  end.getTime(),
				                                                      "p1",
				                                                      "s1",
				                                                      0);

        Assert.assertEquals(seriesSubmissinCountDtos.size(),3);
	}

	@Test	
	public void testFindImageCounts() throws Exception {
        Calendar end = new GregorianCalendar();
        end.set(2007,7,5);
        Calendar start = new GregorianCalendar();
        start.set(2007,7,4);

		SubmissionCountsDTO submissionCountsDTO =
			submissionHistoryDAO.findImageCounts(start.getTime(),
                                                 end.getTime(),
                                                 "p2",
                                                 "s2");

		Assert.assertEquals(submissionCountsDTO.getPatientCount(), 5);
		Assert.assertEquals(submissionCountsDTO.getStudyCount(), 5);
		Assert.assertEquals(submissionCountsDTO.getSeriesCount(), 5);
		Assert.assertEquals(submissionCountsDTO.getSubmissionCount(), 5);

	}

	@Test	
	public void testFindAnnotationCounts() throws Exception {
        Calendar end = new GregorianCalendar();
        end.set(2007,7,5);
        Calendar start = new GregorianCalendar();
        start.set(2007,7,4);

		SubmissionCountsDTO submissionCountsDTO =
			submissionHistoryDAO.findAnnotationCounts(start.getTime(),
                                                 end.getTime(),
                                                 "p4",
                                                 "s4");

		Assert.assertEquals(submissionCountsDTO.getPatientCount(), 1);
		Assert.assertEquals(submissionCountsDTO.getStudyCount(), 1);
		Assert.assertEquals(submissionCountsDTO.getSeriesCount(), 2);
		Assert.assertEquals(submissionCountsDTO.getSubmissionCount(), 2);

	}

	@Test
	public void testFindCorrectedCounts() throws Exception {
        Calendar end = new GregorianCalendar();
        end.set(2007,7,5);
        Calendar start = new GregorianCalendar();
        start.set(2007,7,4);

		SubmissionCountsDTO submissionCountsDTO =
			submissionHistoryDAO.findCorrectedCounts(start.getTime(),
                                                 end.getTime(),
                                                 "p2",
                                                 "s2");

		Assert.assertEquals(submissionCountsDTO.getPatientCount(), 1);
		Assert.assertEquals(submissionCountsDTO.getStudyCount(), 1);
		Assert.assertEquals(submissionCountsDTO.getSeriesCount(), 1);
		Assert.assertEquals(submissionCountsDTO.getSubmissionCount(), 1);

	}

	@Test	
	public void testFindImageSubmissionCountInTimeFrame() throws Exception {
        Calendar end = new GregorianCalendar();
        end.set(2007,7,5);
        Calendar start = new GregorianCalendar();
        start.set(2006,7,4);

		long imageCnt =
			submissionHistoryDAO.findImageSubmissionCountInTimeFrame(start.getTime(),
                                                 end.getTime(),
                                                 "p2",
                                                 "s2");

		Assert.assertEquals(imageCnt, 5);
	}

	@Test	
	public void testFindAnnotationSubmissionCountInTimeFrame() throws Exception {
        Calendar end = new GregorianCalendar();
        end.set(2007,7,5);
        Calendar start = new GregorianCalendar();
        start.set(2006,7,4);

		long imageCnt =
			submissionHistoryDAO.findAnnotationSubmissionCountInTimeFrame(start.getTime(),
                                                 end.getTime(),
                                                 "p4",
                                                 "s4");

		Assert.assertEquals(imageCnt, 2);
	}

	@Test	
	public void testFindSubmissionDatesInTimeFrame() throws Exception {
        Calendar end = new GregorianCalendar();
        end.set(2007,7,5);
        Calendar start = new GregorianCalendar();
        start.set(2006,7,4);

		List<DayCountDTO> days =
			submissionHistoryDAO.findSubmissionDatesInTimeFrame(start.getTime(),
                                                                end.getTime(),
                                                                "p1",
                                                                "s1",
                                                                new Integer[] {0,1});

		Assert.assertEquals(days.size(),2);

		DayCountDTO d1 = days.get(0);
		//assertEquals(VerifySubmissionUtil.dateFormat(d1.getDay()),("08/04/2006"));
		Assert.assertEquals(d1.getSubmissionCount(), 4);

		d1 = days.get(1);
		//assertEquals(VerifySubmissionUtil.dateFormat(d1.getDay()),("08/05/2006"));
		Assert.assertEquals(d1.getSubmissionCount(), 1);
	}

	@Test	
	public void testFindNewPatientCountInTimeFrame() throws Exception {

        Calendar start = new GregorianCalendar();
        start.set(2006,7,5);

        int newPatientCount = submissionHistoryDAO.findNewPatientCountInTimeFrame(start.getTime(),
        		                                                start.getTime(),
                                                                "p1",
                                                                "s1");
        Assert.assertEquals(newPatientCount, 0);

        start = new GregorianCalendar();
        start.set(2006,7,4);

        newPatientCount = submissionHistoryDAO.findNewPatientCountInTimeFrame(start.getTime(),
        		start.getTime(),
                "p1",
                "s1");
        Assert.assertEquals(newPatientCount, 1);
	}

	@Test	
	public void testFindUpdatedSeriesCountInTimeFrame() throws Exception {

        Calendar start = new GregorianCalendar();
        start.set(2007,10,5);

        Calendar end = new GregorianCalendar();
        end.set(2007,10,6);

        int updSeriesCount = submissionHistoryDAO.findUpdatedSeriesCountInTimeFrame(start.getTime(),
        		                                                end.getTime(),
                                                                "p2",
                                                                "s2");
        Assert.assertEquals(updSeriesCount, 1);


        start.set(2007,10,4);

        updSeriesCount = submissionHistoryDAO.findUpdatedSeriesCountInTimeFrame(start.getTime(),
        		                                                start.getTime(),
                                                                "p2",
                                                                "s2");
        Assert.assertEquals(updSeriesCount, 0);



	}

	@Test	
	public void testFindNewSeriesCountInTimeFrame() throws Exception {
        Calendar start = new GregorianCalendar();
        start.set(2007,10,5);

        Calendar end = new GregorianCalendar();
        end.set(2007,10,6);

        int newSeriesCount = submissionHistoryDAO.findNewSeriesCountInTimeFrame(start.getTime(),
        		                                                end.getTime(),
                                                                "p2",
                                                                "s2");
        Assert.assertEquals(newSeriesCount, 0);


        start.set(2007,10,4);

        newSeriesCount = submissionHistoryDAO.findNewSeriesCountInTimeFrame(start.getTime(),
        		                                                start.getTime(),
                                                                "p2",
                                                                "s2");
        Assert.assertEquals(newSeriesCount, 1);
	}

	
	@Test	
	public void testFindUpdatedStudyCountInTimeFrame() throws Exception {

        Calendar start = new GregorianCalendar();
        start.set(2007,10,5);

        Calendar end = new GregorianCalendar();
        end.set(2007,10,6);

        int updStudyCount = submissionHistoryDAO.findUpdatedStudyCountInTimeFrame(start.getTime(),
        		                                                end.getTime(),
                                                                "p2",
                                                                "s2");
        Assert.assertEquals(updStudyCount, 1);


        start.set(2007,10,4);

        updStudyCount = submissionHistoryDAO.findUpdatedStudyCountInTimeFrame(start.getTime(),
        		                                                start.getTime(),
                                                                "p2",
                                                                "s2");
        Assert.assertEquals(updStudyCount, 0);
	}

	@Test
	public void testFindNewStudyCountInTimeFrame() throws Exception {
        Calendar start = new GregorianCalendar();
        start.set(2007,10,5);

        Calendar end = new GregorianCalendar();
        end.set(2007,10,6);

        int newStudyCount = submissionHistoryDAO.findNewStudyCountInTimeFrame(start.getTime(),
        		                                                end.getTime(),
                                                                "p2",
                                                                "s2");
        Assert.assertEquals(newStudyCount, 0);


        start.set(2007,10,4);

        newStudyCount = submissionHistoryDAO.findNewStudyCountInTimeFrame(start.getTime(),
        		                                                start.getTime(),
                                                                "p2",
                                                                "s2");
        Assert.assertEquals(newStudyCount, 1);
	}

	@Test
	public void testFindUpdatedPatientCountInTimeFrame() throws Exception {

        Calendar start = new GregorianCalendar();
        start.set(2007,10,5);

        Calendar end = new GregorianCalendar();
        end.set(2007,10,6);

        int updPatientCount = submissionHistoryDAO.findUpdatedPatientCountInTimeFrame(start.getTime(),
        		                                                end.getTime(),
                                                                "p2",
                                                                "s2");
        Assert.assertEquals(updPatientCount, 1);


        start.set(2007,10,4);

        updPatientCount = submissionHistoryDAO.findUpdatedPatientCountInTimeFrame(start.getTime(),
        		                                                start.getTime(),
                                                                "p2",
                                                                "s2");
        Assert.assertEquals(updPatientCount, 0);
	}
    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/submission_history.xml";

    @Autowired
	private SubmissionHistoryDAO submissionHistoryDAO;
}
