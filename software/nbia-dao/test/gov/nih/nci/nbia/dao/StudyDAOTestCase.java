/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.dto.StudyDTO;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class StudyDAOTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testHqlProcessNotFound() throws Exception {
		final int SERIES_PK_ID = -1;
		//hqlProcess will sit on ALL exceptions????????????
		List<StudyDTO> resultSets = studyDAO.findStudiesBySeriesId(createSeriesIds(new long[]{SERIES_PK_ID}));
		
		Assert.assertTrue("should have no results",
				    resultSets.size()==0);
	}

	@Test	
	public void testHqlProcessFound() throws Exception {
		final int SERIES_PK_ID = 6990;

		List<StudyDTO> resultSets = studyDAO.findStudiesBySeriesId(createSeriesIds(new long[]{SERIES_PK_ID}));
		Assert.assertTrue("series criteria should yield one result",
				    resultSets.size()==1);

		for(StudyDTO studyResultSet : resultSets) {

			Assert.assertEquals(studyResultSet.getId(),
					     (Integer)2094);

			Assert.assertEquals(studyResultSet.getDescription(),
                         null);
			
			//more assertions here
		}
	}

	@Test	
	public void testMultipleSeriesIdHqlProcessFound() throws Exception {
		final int SERIES_PK_ID_1 = 6990;
		final int SERIES_PK_ID_2 = 6991;


		List<StudyDTO> resultSets = studyDAO.findStudiesBySeriesId(createSeriesIds(new long[]{SERIES_PK_ID_1,SERIES_PK_ID_2}));
		Assert.assertTrue("series criteria should yield one result",
				    resultSets.size()==2);

		//just make sure two come back
	}

    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_1044.xml";

    @Autowired
	private StudyDAO studyDAO;


	private static List<Integer> createSeriesIds(long[] seriesPkIds) {
		List<Integer> seriesIds = new ArrayList<Integer>();
		for(long seriesPkId : seriesPkIds) {
			seriesIds.add((int)seriesPkId);
		}
		return seriesIds;
	}
}
