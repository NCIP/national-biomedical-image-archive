package gov.nih.nci.ncia;


public class LoginSelenTestCase extends AbstractSelenTestCaseImpl {

	public void testSanity() throws Exception {
		login();

		navigateToSearchPage();
		//assert something?
	}
}
