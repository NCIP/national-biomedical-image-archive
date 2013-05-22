/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.dto.AnnotationFileDTO;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class AnnotationDAOTestCase extends AbstractDbUnitTestForJunit4 {

    @Test
	public void testWithSeriesIdCriteria() throws Exception {

        List<Integer> seriesIdCriteria = new ArrayList<Integer>();
        seriesIdCriteria.add(6990);
        seriesIdCriteria.add(6991);

		List<AnnotationFileDTO> resultSets = annotationDAO.findAnnotationBySeriesPkId(seriesIdCriteria);
		System.out.println("resultSets:"+resultSets.size());
		Assert.assertTrue(resultSets.size()==2);
	}



    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }

    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_1044.xml";

    @Autowired
    private AnnotationDAO annotationDAO;
}
