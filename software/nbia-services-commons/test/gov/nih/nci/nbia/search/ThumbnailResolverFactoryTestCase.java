/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.dto.ImageDTO;
import junit.framework.TestCase;

public class ThumbnailResolverFactoryTestCase extends TestCase {

	public void testGetThumbnailURLResolverFailure() {
		//dont set sys property "thumbnailResolver.className"

		try {
			ThumbnailResolverFactory.getThumbnailURLResolver();
			fail("shouldnt get here");
		}
		catch(Exception ex) {
			//should get here
		}

	}

	public void testGetThumbnailURLResolver() {
		System.setProperty("thumbnailResolver.className",
				           "gov.nih.nci.nbia.search.ThumbnailResolverFactoryTestCase$FakeThumbnailURLResolver");

		ThumbnailURLResolver resolver = ThumbnailResolverFactory.getThumbnailURLResolver();
		ThumbnailURLResolver resolver2 = ThumbnailResolverFactory.getThumbnailURLResolver();

		assertEquals(resolver, resolver2);
		assertTrue(resolver instanceof FakeThumbnailURLResolver);
	}

	public static class FakeThumbnailURLResolver implements ThumbnailURLResolver {
		/**
		 * For a given local DICOM image, return a URL (String) to get to a thumbnail of it.
		 */
		public String resolveThumbnailUrl(ImageDTO imageDto) {
			return "foo";
		}
	}
}
