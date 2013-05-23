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

public class Register_User_TestCase extends SeleneseTestCase {
	public void testRegister_User_TestCase() throws Exception {
		selenium.open("/ncia/");
		selenium.click("link=CLICKING HERE");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Register");
		selenium.select("MAINbody:registerForm:_id86", "label=Dr.");
		selenium.type("MAINbody:registerForm:firstName", "Durga");
		selenium.type("MAINbody:registerForm:lastName", "Addepalli");
		selenium.type("MAINbody:registerForm:email", "ydmm_k@yahoo.co.in");
		selenium.type("MAINbody:registerForm:address", "2115 E Jefferson St");
		selenium.type("MAINbody:registerForm:city", "Rockville");
		selenium.select("MAINbody:registerForm:state", "label=MD");
		selenium.type("MAINbody:registerForm:postal", "20852");
		selenium.type("MAINbody:registerForm:pass1", "nciaqa");
		selenium.type("MAINbody:registerForm:pass2", "nciaqa");
		selenium.click("MAINbody:registerForm:_id166");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"document.forms['MAINbody:homeLoggedIn:navigationForm']['MAINbody:homeLoggedIn:navigationForm:_idcl'].value='MAINbody:homeLoggedIn:navigationForm:_id78'; document.forms['MAINbody:homeLoggedIn:navigationForm'].submit(); return false;\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("MAINbody:searchForm:_id311");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=LOGOUT");
		selenium.waitForPageToLoad("30000");
		checkForVerificationErrors();
	}
}
