/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class PatientSearchResultImplTestCase extends TestCase {

	public void testSetters() {
		NBIANode node = new NBIANode(true, "foo1", "foo2");

		PatientSearchResultImpl p = new PatientSearchResultImpl();
		p.setProject("p1");
		p.setId(2);
		p.setTotalNumberOfSeries(44);
		p.setTotalNumberOfStudies(66);
		p.associateLocation(node);
		
		assertTrue(p.getProject().equals("p1"));
		assertTrue(p.getId()==2);
		assertTrue(p.getTotalNumberOfSeries()==44);
		assertTrue(p.getTotalNumberOfStudies()==66);
		assertTrue(p.associatedLocation().equals(node));

	}
	
	
	public void testSetStudyIdentifiersStudyIdentifiersArray() {
		PatientSearchResultImpl p = new PatientSearchResultImpl();
		
		StudyIdentifiers[] si = constructIdentifiers();
		p.setStudyIdentifiers(si);
		
		p.addSeriesForStudy(50, 45);
		
		
		assertTrue(p.getStudyIdentifiers().length==5);
		
		StudyIdentifiers moo = new StudyIdentifiers();
		moo.setStudyIdentifier(44);
		
		p.setStudyIdentifiers(2, moo);
		
		assertTrue(p.getStudyIdentifiers(2).getStudyIdentifier()==44);
	}
	
	public void testComputeListOfSeriesIds() {
		PatientSearchResultImpl p = new PatientSearchResultImpl();
		
		Set<Integer> seriesIds = p.computeListOfSeriesIds();
		assertTrue(seriesIds.size()==0);
		
		constructIdentifiers(p);
		
		seriesIds = p.computeListOfSeriesIds();
		assertTrue(seriesIds.size()==12);
		for(Integer i : seriesIds) {
			System.out.println("i:"+i);
		}			
		
		assertTrue(p.computeFilteredNumberOfSeries()==12);
		assertTrue(p.computeFilteredNumberOfStudies()==4);		
		
		p = new PatientSearchResultImpl();
		assertTrue(p.computeListOfSeriesIds().size()==0);
	}	
	
	public void testComputeListOfSeriesIds2() {
		PatientSearchResultImpl p = new PatientSearchResultImpl();
		p.setStudyIdentifiers(constructIdentifiers());
		
		Set<Integer> seriesIds = p.computeListOfSeriesIds();
		for(Integer i : seriesIds) {
			System.out.println("m:"+i);
		}
		assertTrue(seriesIds.size()==12);

	}		
	
	
	public void testStudySearchResultImplSort() {
		PatientSearchResultImpl patientSearchResultImpl1 = new PatientSearchResultImpl();
		patientSearchResultImpl1.setSubjectId("z1");

		PatientSearchResultImpl patientSearchResultImpl2 = new PatientSearchResultImpl();
		patientSearchResultImpl2.setSubjectId("m2");
		
		PatientSearchResultImpl patientSearchResultImpl3 = new PatientSearchResultImpl();
		patientSearchResultImpl3.setSubjectId("a3");
		
		PatientSearchResultImpl patientSearchResultImpl4 = new PatientSearchResultImpl();
		patientSearchResultImpl4.setSubjectId("z2");
		
		PatientSearchResultImpl patientSearchResultImpl5 = new PatientSearchResultImpl();
		patientSearchResultImpl5.setSubjectId("j1");
		
		List<PatientSearchResultImpl> list = new ArrayList<PatientSearchResultImpl>();
		list.add(patientSearchResultImpl1);
		list.add(patientSearchResultImpl2);
		list.add(patientSearchResultImpl3);
		list.add(patientSearchResultImpl4);
		list.add(patientSearchResultImpl5);
		
		Collections.sort(list);
	
		assertTrue(list.get(0).getSubjectId().equals("a3"));
		assertTrue(list.get(1).getSubjectId().equals("j1"));
		assertTrue(list.get(2).getSubjectId().equals("m2"));
		assertTrue(list.get(3).getSubjectId().equals("z1"));
		assertTrue(list.get(4).getSubjectId().equals("z2"));
	}
	
	private static void constructIdentifiers(PatientSearchResultImpl p) {
		p.addSeriesForStudy(1,1);
		p.addSeriesForStudy(1,2);
		p.addSeriesForStudy(1,3);
	
		p.addSeriesForStudy(2,4);
		p.addSeriesForStudy(2,5);
		p.addSeriesForStudy(2,6);		
		
		p.addSeriesForStudy(3,7);
		p.addSeriesForStudy(3,8);
		p.addSeriesForStudy(3,9);		
		
		p.addSeriesForStudy(4,10);
		p.addSeriesForStudy(4,11);
		p.addSeriesForStudy(4,14);
	}	
	
	private static StudyIdentifiers[] constructIdentifiers() {
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
