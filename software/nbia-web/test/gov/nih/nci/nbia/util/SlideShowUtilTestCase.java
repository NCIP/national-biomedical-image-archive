/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultImpl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class SlideShowUtilTestCase extends TestCase {

	public void testGetImageSeriesJavascriptNoImage() {
		List<ImageSearchResult> thumbnailList = new ArrayList<ImageSearchResult>();
		String js = SlideShowUtil.getImageSeriesJavascript(thumbnailList);
		assertTrue(js.equals("[]"));
	}

	public void testGetImageSeriesJavascriptSingleImage() {
		ImageSearchResultImpl thumbnailImageDTO = new ImageSearchResultImpl();
		thumbnailImageDTO.setThumbnailURL("foo/ncia/thumbnailViewer?location=0");
		List<ImageSearchResult> thumbnailList = new ArrayList<ImageSearchResult>();
		thumbnailList.add(thumbnailImageDTO);
		String js = SlideShowUtil.getImageSeriesJavascript(thumbnailList);
		System.out.println("js:"+js);

		assertTrue(js.equals("[\'foo/ncia/thumbnailViewer?location=0\']"));
	}

	public void testGetImageSeriesJavascriptMutlipleImage() {
		ImageSearchResultImpl thumbnailImageDTO1 = new ImageSearchResultImpl();
		thumbnailImageDTO1.setThumbnailURL("foo1/ncia/thumbnailViewer?location=1");
		ImageSearchResultImpl thumbnailImageDTO2 = new ImageSearchResultImpl();
		thumbnailImageDTO2.setThumbnailURL("foo2/ncia/thumbnailViewer?location=2");

		List<ImageSearchResult> thumbnailList = new ArrayList<ImageSearchResult>();
		thumbnailList.add(thumbnailImageDTO1);
		thumbnailList.add(thumbnailImageDTO2);

		String js = SlideShowUtil.getImageSeriesJavascript(thumbnailList);
		System.out.println("js:"+js);
		assertTrue(js.equals("[\'foo1/ncia/thumbnailViewer?location=1\',\'foo2/ncia/thumbnailViewer?location=2\']"));

		ImageSearchResultImpl thumbnailImageDTO3 = new ImageSearchResultImpl();
		thumbnailImageDTO3.setThumbnailURL("foo3/ncia/thumbnailViewer?location=3");

		thumbnailList.add(thumbnailImageDTO3);
		js = SlideShowUtil.getImageSeriesJavascript(thumbnailList);
		System.out.println("js:"+js);
		assertTrue(js.equals("[\'foo1/ncia/thumbnailViewer?location=1\',\'foo2/ncia/thumbnailViewer?location=2\',\'foo3/ncia/thumbnailViewer?location=3\']"));

	}
}
