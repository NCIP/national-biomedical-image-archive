package gov.nih.nci.ncia;


public class LoginSelenTestCase extends AbstractSelenTestCaseImpl {

	public void testSanity() throws Exception {
		selenium.open("/ncia/");
		selenium.type("MAINbody:sideBarView:loginForm:uName2", "nciadevtest");
		selenium.type("MAINbody:sideBarView:loginForm:pass2", "saicT3@m17");
		selenium.click("MAINbody:sideBarView:loginForm:loginButton2");

		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:navigationForm:searchLink')",
				                  "30000");
		selenium.click("MAINbody:navigationForm:searchLink");
		//assert something?
	}
}
