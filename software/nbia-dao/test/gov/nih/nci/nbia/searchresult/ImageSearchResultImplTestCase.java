/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.searchresult;

import junit.framework.TestCase;
import java.util.*;

public class ImageSearchResultImplTestCase extends TestCase {

	public void testImageSearchResultImpl() {
		ImageSearchResultImpl imageSearchResultImpl = new ImageSearchResultImpl();
		imageSearchResultImpl.setId(1);
		imageSearchResultImpl.setInstanceNumber(null);
		imageSearchResultImpl.setSeriesId(2);
		imageSearchResultImpl.setSeriesInstanceUid("series1");
		imageSearchResultImpl.setSopInstanceUid("sop1");
		imageSearchResultImpl.setThumbnailURL("thumb1");
		imageSearchResultImpl.setSize(new Long(4));
		
		assertTrue(imageSearchResultImpl.getId()==1);
		assertTrue(imageSearchResultImpl.getInstanceNumber()==null);
		assertTrue(imageSearchResultImpl.getSeriesId()==2);
		assertTrue(imageSearchResultImpl.getSize()==4);
		assertTrue(imageSearchResultImpl.getSeriesInstanceUid().equals("series1"));
		assertTrue(imageSearchResultImpl.getSopInstanceUid().equals("sop1"));
		assertTrue(imageSearchResultImpl.getThumbnailURL().equals("thumb1"));
	}
	
	public void testImageSearchResultImplSort() {
		ImageSearchResultImpl imageSearchResultImpl1 = new ImageSearchResultImpl();
		imageSearchResultImpl1.setInstanceNumber(null);

		ImageSearchResultImpl imageSearchResultImpl2 = new ImageSearchResultImpl();
		imageSearchResultImpl2.setInstanceNumber(3);
		
		ImageSearchResultImpl imageSearchResultImpl3 = new ImageSearchResultImpl();
		imageSearchResultImpl3.setInstanceNumber(6);
		
		ImageSearchResultImpl imageSearchResultImpl4 = new ImageSearchResultImpl();
		imageSearchResultImpl4.setInstanceNumber(2);
		
		ImageSearchResultImpl imageSearchResultImpl5 = new ImageSearchResultImpl();
		imageSearchResultImpl5.setInstanceNumber(null);
		
		List<ImageSearchResultImpl> list = new ArrayList<ImageSearchResultImpl>();
		list.add(imageSearchResultImpl1);
		list.add(imageSearchResultImpl2);
		list.add(imageSearchResultImpl3);
		list.add(imageSearchResultImpl4);
		list.add(imageSearchResultImpl5);
		
		Collections.sort(list);
	
		
		assertTrue(list.get(0).getInstanceNumber()==null);
		assertTrue(list.get(1).getInstanceNumber()==null);
		assertTrue(list.get(2).getInstanceNumber()==2);
		assertTrue(list.get(3).getInstanceNumber()==3);
		assertTrue(list.get(4).getInstanceNumber()==6);
	}	

}
