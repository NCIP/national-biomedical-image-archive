/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 *
 */
package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ImageDTO2;
import gov.nih.nci.nbia.util.Util;
import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;

import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lethai
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class ImageDAO2TestCase extends AbstractDbUnitTestForJunit4 {
    /**
     * Test method for {@link gov.nih.nci.nbia.dao.ImageDAO2#findImagesBySeriesUid(java.lang.String, java.lang.String)}.
     */
	@Test
    public void testFindImagesBySeriesUid() {

        List<ImageDTO2> dtos =
            imageDAO.findImagesBySeriesUid("1.3.6.1.4.1.9328.50.3.195", "");

        Assert.assertTrue(dtos.size()==139);
    }

	/**
     * Test method for {@link gov.nih.nci.nbia.dao.ImageDAO2#findImagesBySeriesUid(java.lang.String, java.lang.String)}.
     */
	@Test
    public void testFindImagesBySeriesUidForMultiFrame() {

        List<ImageDTO2> dtos =
            imageDAO.findImagesBySeriesUid("1.3.6.1.4.1.9328.50.3.195", "");

        Assert.assertTrue(dtos.get(0).getFrameNum()==43);
    }



	@BeforeClass
	public static void foo() throws Exception {
		//Util.loadSystemPropertiesFromPropertiesResource("ncia.properites");
System.out.println("foo:"+Util.class.getClassLoader().getResource("ncia.properites"));
	}
     //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
        return TEST_DB_FLAT_FILE;
    }

    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_1044.xml";

    @Autowired
    private ImageDAO2 imageDAO;
}