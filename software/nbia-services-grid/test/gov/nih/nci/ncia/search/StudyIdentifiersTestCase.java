/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;

import junit.framework.TestCase;

public class StudyIdentifiersTestCase extends TestCase {

	public void testStudyIdentifiers() {
		StudyIdentifiers studyIdentifiers = new StudyIdentifiers();
		studyIdentifiers.setStudyIdentifier(4);
		
		Integer[] seriesIds = new Integer[] {4,5,6};

		studyIdentifiers.setSeriesIdentifiers(seriesIds);
		studyIdentifiers.setSeriesIdentifiers(1, 999);
		
		assertTrue(studyIdentifiers.getStudyIdentifier()==4);

		assertTrue(studyIdentifiers.getSeriesIdentifiers().length==3);
		assertTrue(studyIdentifiers.getSeriesIdentifiers(0).equals(4));
		assertTrue(studyIdentifiers.getSeriesIdentifiers(1).equals(999));
		assertTrue(studyIdentifiers.getSeriesIdentifiers(2).equals(6));
	}

}
