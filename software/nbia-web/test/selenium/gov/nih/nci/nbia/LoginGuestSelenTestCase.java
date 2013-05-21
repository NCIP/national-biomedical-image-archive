package gov.nih.nci.nbia;

public class LoginGuestSelenTestCase extends AbstractSelenTestCaseImpl {

	public void testSanity() throws Exception {
		loginAsGuest();

		selectModalitySearchCriteria("CT");

		submitSearch();

		//put a simple incidental test here if we can get controlled test data worked out
	}
}
