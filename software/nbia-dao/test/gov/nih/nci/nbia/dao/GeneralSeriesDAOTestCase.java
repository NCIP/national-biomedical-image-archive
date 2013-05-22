/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.dto.EquipmentDTO;
import gov.nih.nci.nbia.dto.SeriesDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class GeneralSeriesDAOTestCase extends AbstractDbUnitTestForJunit4 {
    @Test	
	public void testFindEquipmentOfVisibleSeries() throws Exception {
		Collection<EquipmentDTO> equipmentList = generalSeriesDAO.findEquipmentOfVisibleSeries();

		Assert.assertTrue(equipmentList.size()==1);

		for(EquipmentDTO dto : equipmentList) {
			if(dto.getManufacturer().equals("SIEMENS")) {
				Assert.assertEquals(dto.getModel(),"Sensation 16");
				Assert.assertEquals(dto.getVersion(),"VA70C");
			}
			else {
				Assert.fail("bad equipemnt");
			}
		}
	}

    @Test	
	public void testWithSeriesIdCriteria() throws Exception {

        List<Integer> seriesIdCriteria = new ArrayList<Integer>();
        seriesIdCriteria.add(3146);
        seriesIdCriteria.add(3147);
        seriesIdCriteria.add(3148);
        seriesIdCriteria.add(3149);

		List<SeriesDTO> resultSets = generalSeriesDAO.findSeriesBySeriesPkId(seriesIdCriteria);
		Assert.assertTrue(resultSets.size()==4);

		for(SeriesDTO seriesIter : resultSets) {
			if(seriesIter.getSeriesUID().equals("666")) {
				Assert.assertTrue(seriesIter.getAnnotationsSize().equals(5150+5150));
			}
		}
	}


    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }

    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/collections_testdata.xml";

    @Autowired
	private GeneralSeriesDAO generalSeriesDAO;

}
