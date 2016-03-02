/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.util.JnlpArgumentsParser;
import gov.nih.nci.nbia.download.SeriesData;
import junit.framework.TestCase;
import java.util.List;

/**
 * @author lethai
 *
 */
public class JnlpArgumentsParserTestCase extends TestCase {

    /**
     * Test method for {@link gov.nih.nci.nbia.util.JnlpArgumentsParser#parse(java.lang.String[])}.
     */
    public void testParse() {
        String [] args = new String[3];

        args[0]="LIDC|1.3.6.1.4.1.9328.50.3.0022|1.3.6.1.4.1.9328.50.3.68|1.3.6.1.4.1.9328.50.3.69|true|123|6469782|2416|http://fake1|disp1|true";
        args[1]="LIDC|1.3.6.1.4.1.9328.50.3.0023|1.3.6.1.4.1.9328.50.3.194|1.3.6.1.4.1.9328.50.3.195|true|139|73113757|2887|http://fake2|disp2|true";
        args[2]="LIDC|1.3.6.1.4.1.9328.50.3.0023|1.3.6.1.4.1.9328.50.3.194|1.3.6.1.4.1.9328.50.3.195|true|139|73113757|2887|http://fake3|disp3|false";

        //JnlpArgumentsParser parser ;
        List<SeriesData> data = JnlpArgumentsParser.parse(args);
        assertEquals(data.size(), 3);
        
        assertEquals(data.get(1).getCollection(), "LIDC");
        assertEquals(data.get(1).getPatientId(), "1.3.6.1.4.1.9328.50.3.0023");
        assertEquals(data.get(1).getStudyInstanceUid(), "1.3.6.1.4.1.9328.50.3.194");
        assertEquals(data.get(1).getSeriesInstanceUid(), "1.3.6.1.4.1.9328.50.3.195");
        assertTrue(data.get(1).isHasAnnotation());       
        assertEquals(data.get(1).getNumberImages(), "139");      
        assertTrue(data.get(1).getImagesSize()==73113757);
        assertEquals(data.get(1).getUrl(), "http://fake2");
        assertEquals(data.get(1).getDisplayName(), "disp2");        
        assertTrue(data.get(1).isLocal());
    }
}
