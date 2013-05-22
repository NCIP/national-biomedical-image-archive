/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch;

import gov.nih.nci.ncia.search.StudyIdentifiers;

public class RemoteTestUtil {
	public static StudyIdentifiers[] constructIdentifiers() {
		StudyIdentifiers[] si = new StudyIdentifiers[4];
		
		StudyIdentifiers si0 = new StudyIdentifiers();
		si0.setStudyIdentifier(1);
		Integer[] series0 = new Integer[3];
		series0[0] = 1;		
		series0[1] = 2;
		series0[2] = 3;
		si0.setSeriesIdentifiers(series0);
		si[0] = si0;
		
		StudyIdentifiers si1 = new StudyIdentifiers();
		si1.setStudyIdentifier(2);
		Integer[] series1 = new Integer[3];
		series1[0] = 4;		
		series1[1] = 5;
		series1[2] = 6;
		si1.setSeriesIdentifiers(series1);
		si[1] = si1;		
		
		StudyIdentifiers si2 = new StudyIdentifiers();
		si2.setStudyIdentifier(3);
		Integer[] series2 = new Integer[3];
		series2[0] = 7;		
		series2[1] = 8;
		series2[2] = 9;
		si2.setSeriesIdentifiers(series2);
		si[2] = si2;
		
		StudyIdentifiers si3 = new StudyIdentifiers();
		si3.setStudyIdentifier(4);
		Integer[] series3 = new Integer[3];
		series3[0] = 10;		
		series3[1] = 11;
		series3[2] = 14;
		si3.setSeriesIdentifiers(series3);
		si[3] = si3;	
		return si;
	}
}
