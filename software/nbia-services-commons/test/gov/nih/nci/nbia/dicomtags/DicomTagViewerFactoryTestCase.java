/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dicomtags;

import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.ImageSearchResult;

import java.util.List;

import junit.framework.TestCase;

public class DicomTagViewerFactoryTestCase extends TestCase {
	public void testGetDicomTagViewerFailure() {
		//dont set sys property "drilldown.className"

		try {
			DicomTagViewerFactory.getDicomTagViewer();
			fail("shouldnt get here");
		}
		catch(Exception ex) {
			//should get here
		}

	}

	public void testGetDrillDown() {
		System.setProperty("dicomTagViewer.className",
				           "gov.nih.nci.nbia.dicomtags.DicomTagViewerFactoryTestCase$FakeDicomTagViewer");

		DicomTagViewer dd1 = DicomTagViewerFactory.getDicomTagViewer();
		DicomTagViewer dd2 = DicomTagViewerFactory.getDicomTagViewer();

		assertEquals(dd1, dd2);
		assertTrue(dd1 instanceof DicomTagViewer);
	}

	public static class FakeDicomTagViewer implements DicomTagViewer {
		public List<DicomTagDTO> viewDicomHeader(ImageSearchResult image) {
			return null;
		}

	}
}
