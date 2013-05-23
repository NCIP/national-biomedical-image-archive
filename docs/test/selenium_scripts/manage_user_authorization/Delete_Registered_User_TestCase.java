/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package com.example.tests;

import com.thoughtworks.selenium.*;
import java.util.regex.Pattern;

public class Delete_Registered_User_TestCase extends SeleneseTestCase {
	public void testDelete_Registered_User_TestCase() throws Exception {
		selenium.open("/upt/");
		selenium.type("loginId", "addepald@mail.nih.gov");
		selenium.type("applicationContextName", "NCIA");
		selenium.click("//input[@value='Login']");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=USER");
		selenium.click("//a[contains(text(),'Select an\n									Existing User')]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@value='Search']");
		selenium.waitForPageToLoad("30000");
		selenium.click("document.UserForm.userId[644]");
		selenium.click("//input[@value='View Details']");
		selenium.waitForPageToLoad("30000");
		selenium.click("//button[@onclick=\"setAndSubmit('delete');\"]");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.getConfirmation().matches("^Are you sure you want to delete the record[\\s\\S]$"));
		selenium.click("link=LOG OUT");
		checkForVerificationErrors();
	}
}
