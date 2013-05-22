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
package gov.nih.nci.ncia;

import gov.nih.nci.ncia.gridzip.ZippingDTO;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml", "/applicationContext-hibernate-testContext.xml"})
public class TimepointsTestCase extends AbstractDbUnitTestForJunit4 {


	@Test
	public void testGetImagesByNthStudyForPatient() throws Exception{

		List<ZippingDTO> zippingDtoList = Timepoints.getImagesByNthStudyTimePointForPatient("1.3.6.1.4.1.9328.50.3.0022",
				                                                                            1);

		Assert.assertEquals(zippingDtoList.size(),129);
		
		boolean found = false;
		for(ZippingDTO z : zippingDtoList){
			if (z.getFilePath().equalsIgnoreCase("/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.68/https-76348.xml"))
			{
				found = true;
			}
		}
		
		Assert.assertTrue(found);
		

	    zippingDtoList = Timepoints.getImagesByNthStudyTimePointForPatient("1.3.6.1.4.1.9328.50.3.0022",
                                                                           0);
        //assertNull(zippingDtoList);
	    Assert.assertEquals(zippingDtoList.size(),0);

	    zippingDtoList = Timepoints.getImagesByNthStudyTimePointForPatient("1.3.6.1.4.1.9328.50.3.0022",
                                                                           2);
        //assertNull(zippingDtoList);
	    Assert.assertEquals(zippingDtoList.size(),0);
	}


    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_1044.xml";
}
