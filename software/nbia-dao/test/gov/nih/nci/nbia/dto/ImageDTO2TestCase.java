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
package gov.nih.nci.nbia.dto;

import junit.framework.TestCase;

/**
 * @author lethai
 *
 */
public class ImageDTO2TestCase extends TestCase {
	public void testAccessors() {

		String SOPInstanceUID="1.2.3.4.5.6";
		String fileName="1.2.3.4.5.6.7.dcm";
		Long dicomSize = new Long(514);
		String project="RIDER";
		String site="RIDER";
		String ssg="test";
		int frameNum = 0;

        ImageDTO2 imageDTO = new ImageDTO2(SOPInstanceUID, fileName, dicomSize, project, site, ssg, 0);

        assertTrue(imageDTO.getSOPInstanceUID().equals("1.2.3.4.5.6"));
        assertTrue(imageDTO.getFileName().equals("1.2.3.4.5.6.7.dcm"));
        assertTrue(imageDTO.getDicomSize() ==514L);
        assertTrue(imageDTO.getProject().equals("RIDER"));
        assertTrue(imageDTO.getSite().equals("RIDER"));
        assertTrue(imageDTO.getSsg().equals("test"));
        assertTrue(imageDTO.getFrameNum()==0);
    }
}