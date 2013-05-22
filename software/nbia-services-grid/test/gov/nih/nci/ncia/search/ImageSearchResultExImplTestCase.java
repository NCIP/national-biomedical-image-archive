/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;

import junit.framework.TestCase;

public class ImageSearchResultExImplTestCase extends TestCase {

	public void testDefault() {
		NameValuesPairs nvp1 = new NameValuesPairs();
		nvp1.setName("foo");
		String[] inVals1 = new String[] {"foo1", "foo2", "foo3"};		
		nvp1.setValues(inVals1);		
			
		
		ImageSearchResultExImpl out = new ImageSearchResultExImpl();
		out.setNameValuesPairs(nvp1);
		assertEquals(nvp1, out.getNameValuesPairs());
	}

	public void testPopulation() {
		ImageSearchResultImpl isr = new ImageSearchResultImpl();
		isr.setId(1);
		isr.setInstanceNumber(2);
		isr.setSeriesId(3);
		isr.setSeriesInstanceUid("4");
		isr.setSize(5L);
		isr.setSopInstanceUid("6");
		isr.setThumbnailURL("http://foo");
		isr.associateLocation(new NBIANode(true, "disp1", "foo2"));
		
		ImageSearchResultExImpl out = new ImageSearchResultExImpl(isr);
		assertEquals(out.getId(), isr.getId());
		assertEquals(out.getInstanceNumber(), isr.getInstanceNumber());
		assertEquals(out.getSeriesId(), isr.getSeriesId());
		assertEquals(out.getSeriesInstanceUid(), isr.getSeriesInstanceUid());
		assertEquals(out.getSize(), isr.getSize());
		assertEquals(out.getSopInstanceUid(), isr.getSopInstanceUid());
		assertEquals(out.getThumbnailURL(), isr.getThumbnailURL());
		assertEquals(out.associatedLocation(), isr.associatedLocation());
	}
	

}
