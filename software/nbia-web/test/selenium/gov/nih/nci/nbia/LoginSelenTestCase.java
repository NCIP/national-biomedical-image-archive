package gov.nih.nci.nbia;


public class LoginSelenTestCase extends AbstractSelenTestCaseImpl {

	public void testSanity() throws Exception {
		login();

		navigateToSearchPage();
		//assert something?
	}
}
