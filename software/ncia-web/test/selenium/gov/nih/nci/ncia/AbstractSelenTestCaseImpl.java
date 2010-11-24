package gov.nih.nci.ncia;

import com.thoughtworks.selenium.SeleneseTestCase;

public class AbstractSelenTestCaseImpl extends SeleneseTestCase {

	public void setUp() throws Exception {
		String seleniumBrowser = System.getProperty("selenium.browser");
		System.out.println("seleniumBrowser:"+seleniumBrowser);
		System.out.println("selnurl:"+System.getProperty("selenium.url"));
		if(seleniumBrowser==null) {
			seleniumBrowser = "*firefox";
		}
		setUp(System.getProperty("selenium.url"),
		      seleniumBrowser);

	}
}
