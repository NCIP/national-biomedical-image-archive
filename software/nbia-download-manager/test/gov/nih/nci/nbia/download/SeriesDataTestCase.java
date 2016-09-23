/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.download;

import gov.nih.nci.nbia.download.SeriesData;
import junit.framework.TestCase;

/**
 * @author lethai
 *
 */
public class SeriesDataTestCase extends TestCase {
    public void testAccessors() {
        SeriesData sd = new SeriesData();
        sd.setCollection("collection1");
        sd.setPatientId("patient1");
        sd.setStudyInstanceUid("study1");
        sd.setSeriesInstanceUid("series1");
        sd.setImagesSize(120000);
        sd.setAnnoSize(30);
        sd.setHasAnnotation(true);
        sd.setNumberImages("120");

        assertTrue(sd.getCollection().equals("collection1"));
        assertTrue(sd.getPatientId().equals("patient1"));
        assertTrue(sd.getStudyInstanceUid().equals("study1"));
        assertTrue(sd.getSeriesInstanceUid().equals("series1"));
        assertTrue(sd.getNumberImages().equals("120"));
        assertEquals(sd.getAnnoSize().intValue(), 30);
        assertEquals(sd.getImagesSize().intValue(), 120000);
        assertTrue(sd.isHasAnnotation()== true);
    }
}
