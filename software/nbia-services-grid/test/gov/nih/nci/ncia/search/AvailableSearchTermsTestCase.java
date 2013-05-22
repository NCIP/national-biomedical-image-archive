/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;

import junit.framework.TestCase;

public class AvailableSearchTermsTestCase extends TestCase {

	public void testAvailableSearchTerms() {
		AvailableSearchTerms availableSearchTerms = new AvailableSearchTerms();
		
		testAnatomy(availableSearchTerms);
		testCollections(availableSearchTerms);
		testKernels(availableSearchTerms);
		testModalities(availableSearchTerms);
		testEquipment(availableSearchTerms);
	}

	private void testAnatomy(AvailableSearchTerms availableSearchTerms) {
		String[] anatomicSites = new String[] {"a1","a2","a3"};

		availableSearchTerms.setAnatomicSites(anatomicSites);
		availableSearchTerms.setAnatomicSites(1, "a4");
		
		assertTrue(availableSearchTerms.getAnatomicSites().length==3);
		assertTrue(availableSearchTerms.getAnatomicSites(0).equals("a1"));
		assertTrue(availableSearchTerms.getAnatomicSites(1).equals("a4"));
		assertTrue(availableSearchTerms.getAnatomicSites(2).equals("a3"));
	}
	
	private void testCollections(AvailableSearchTerms availableSearchTerms) {
		String[] anatomicSites = new String[] {"c1","c2","c3","c5"};

		availableSearchTerms.setCollections(anatomicSites);
		availableSearchTerms.setCollections(2, "c4");
		
		assertTrue(availableSearchTerms.getCollections().length==4);
		assertTrue(availableSearchTerms.getCollections(0).equals("c1"));
		assertTrue(availableSearchTerms.getCollections(1).equals("c2"));
		assertTrue(availableSearchTerms.getCollections(2).equals("c4"));
		assertTrue(availableSearchTerms.getCollections(3).equals("c5"));
		
	}	
	
	private void testKernels(AvailableSearchTerms availableSearchTerms) {
		String[] anatomicSites = new String[] {"c1","c2","c3","c5"};

		availableSearchTerms.setConvolutionKernels(anatomicSites);
		availableSearchTerms.setConvolutionKernels(2, "c4");
		
		assertTrue(availableSearchTerms.getConvolutionKernels().length==4);
		assertTrue(availableSearchTerms.getConvolutionKernels(0).equals("c1"));
		assertTrue(availableSearchTerms.getConvolutionKernels(1).equals("c2"));
		assertTrue(availableSearchTerms.getConvolutionKernels(2).equals("c4"));
		assertTrue(availableSearchTerms.getConvolutionKernels(3).equals("c5"));
	}	
	
	
	private void testModalities(AvailableSearchTerms availableSearchTerms) {
		String[] anatomicSites = new String[] {"c1","c2","c3","c5"};

		availableSearchTerms.setModalities(anatomicSites);
		availableSearchTerms.setModalities(2, "c4");
		
		assertTrue(availableSearchTerms.getModalities().length==4);
		assertTrue(availableSearchTerms.getModalities(0).equals("c1"));
		assertTrue(availableSearchTerms.getModalities(1).equals("c2"));
		assertTrue(availableSearchTerms.getModalities(2).equals("c4"));
		assertTrue(availableSearchTerms.getModalities(3).equals("c5"));
	}	
	
	private void testEquipment(AvailableSearchTerms availableSearchTerms) {
		Manufacturer[] manus = new Manufacturer[3];
		manus[0] = new Manufacturer();
		manus[1] = new Manufacturer();
		manus[1].setName("foo1");
		manus[2] = new Manufacturer();

		availableSearchTerms.setEquipment(manus);
		
		Manufacturer m2 = new Manufacturer();
		m2.setName("foo2");
		availableSearchTerms.setEquipment(2, m2);
		
		assertTrue(availableSearchTerms.getEquipment().length==3);
		assertTrue(availableSearchTerms.getEquipment(2).getName().equals("foo2"));
	}	
}
