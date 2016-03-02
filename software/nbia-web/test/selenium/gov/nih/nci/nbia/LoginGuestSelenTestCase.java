/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia;

public class LoginGuestSelenTestCase extends AbstractSelenTestCaseImpl {

	public void testSanity() throws Exception {
		loginAsGuest();

		selectModalitySearchCriteria("CT");

		submitSearch();

		//put a simple incidental test here if we can get controlled test data worked out
	}
}
