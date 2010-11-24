/**
 *
 */
package gov.nih.nci.ncia;

import gov.nih.nci.ncia.gridzip.ZippingDTO;

import java.util.List;

/**
 * @author lethai
 *
 */
public class TimepointsTestCase extends AbstractDbTestCaseImpl {



	public void testGetImagesByNthStudyForPatient() throws Exception{

		List<ZippingDTO> zippingDtoList = Timepoints.getImagesByNthStudyTimePointForPatient("1.3.6.1.4.1.9328.50.3.0022",
				                                                                            1);

		assertEquals(zippingDtoList.size(),128);

	    zippingDtoList = Timepoints.getImagesByNthStudyTimePointForPatient("1.3.6.1.4.1.9328.50.3.0022",
                                                                           0);
        //assertNull(zippingDtoList);
	    assertEquals(zippingDtoList.size(),0);

	    zippingDtoList = Timepoints.getImagesByNthStudyTimePointForPatient("1.3.6.1.4.1.9328.50.3.0022",
                                                                           2);
        //assertNull(zippingDtoList);
	    assertEquals(zippingDtoList.size(),0);
	}


    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_1044.xml";
}
