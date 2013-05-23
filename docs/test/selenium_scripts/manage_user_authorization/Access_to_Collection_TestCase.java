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

public class Access_to_Collection_TestCase extends SeleneseTestCase {
	public void testAccess_to_Collection_TestCase() throws Exception {
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
		selenium.click("document.UserForm.userId[13]");
		selenium.click("//input[@value='View Details']");
		selenium.click("//input[@value='Assign PG & Roles']");
		selenium.waitForPageToLoad("30000");
		selenium.select("availableProtectionGroupIds", "label=NCIA.ISPY");
		selenium.click("//input[@value='Assign']");
		selenium.addSelection("availableRoleIds", "label=NCIA.READ");
		selenium.click("//input[@value='Assign' and @type='button' and @onclick='selSwitchRole(this);']");
		selenium.click("//input[@value='Update Association']");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=USER");
		selenium.click("//a[contains(text(),'Select an\n									Existing User')]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@value='Search']");
		selenium.waitForPageToLoad("30000");
		selenium.click("document.UserForm.userId[14]");
		selenium.click("//input[@value='View Details']");
		selenium.click("//input[@value='Assign PG & Roles']");
		selenium.waitForPageToLoad("30000");
		selenium.select("availableProtectionGroupIds", "label=NCIA.IDRICT");
		selenium.click("//input[@value='Assign']");
		selenium.addSelection("availableRoleIds", "label=NCIA.CURATE");
		selenium.click("//input[@value='Assign' and @type='button' and @onclick='selSwitchRole(this);']");
		selenium.removeSelection("availableRoleIds", "label=NCIA.ADMIN");
		selenium.addSelection("availableRoleIds", "label=NCIA.MANAGE_VISIBILITY_STATUS");
		selenium.click("//input[@value='Assign' and @type='button' and @onclick='selSwitchRole(this);']");
		selenium.click("//input[@value='Update Association']");
		selenium.waitForPageToLoad("30000");
		checkForVerificationErrors();
	}
}
