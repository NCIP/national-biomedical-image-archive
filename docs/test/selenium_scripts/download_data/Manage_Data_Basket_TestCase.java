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

public class Manage_Data_Basket extends SeleneseTestCase {
	public void testManage_Data_Basket() throws Exception {
		selenium.open("/ncia/");
		selenium.click("link=CLICKING HERE");
		selenium.waitForPageToLoad("30000");
		selenium.click("MAINbody:loginForm:_id127");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"document.forms['MAINbody:homeLoggedIn:navigationForm']['MAINbody:homeLoggedIn:navigationForm:_idcl'].value='MAINbody:homeLoggedIn:navigationForm:_id78'; document.forms['MAINbody:homeLoggedIn:navigationForm'].submit(); return false;\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("MAINbody:searchForm:_id311");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"document.forms['MAINbody:dataForm']['MAINbody:dataForm:_idcl'].value='MAINbody:dataForm:resultTable:3:_id132'; document.forms['MAINbody:dataForm'].submit(); return false;\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"document.forms['MAINbody:dataForm']['MAINbody:dataForm:_idcl'].value='MAINbody:dataForm:studyTable:0:seriesTable:1:_id107'; document.forms['MAINbody:dataForm'].submit(); return false;\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("MAINbody:imageForm:imageTable:2:_id127");
		selenium.click("MAINbody:imageForm:imageTable:1:_id127");
		selenium.click("MAINbody:imageForm:_id117");
		selenium.waitForPageToLoad("30000");
		selenium.click("MAINbody:imageForm:_id72");
		selenium.click("MAINbody:dataForm:resultTable:6:_id176");
		selenium.click("MAINbody:dataForm:_id123");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick=\"document.forms['MAINbody:dataForm']['MAINbody:dataForm:_idcl'].value='MAINbody:dataForm:resultTable:8:_id132'; document.forms['MAINbody:dataForm'].submit(); return false;\"]");
		selenium.waitForPageToLoad("30000");
		selenium.click("MAINbody:dataForm:studyTable:0:seriesTable:1:_id157");
		selenium.click("MAINbody:dataForm:studyTable:0:seriesTable:1:_id157");
		selenium.click("MAINbody:dataForm:studyTable:1:seriesTable:1:_id157");
		selenium.click("MAINbody:dataForm:studyTable:0:_id84");
		selenium.click("MAINbody:dataForm:studyTable:0:_id83");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=View Contents");
		selenium.click("checkAllBox");
		selenium.click("MAINbody:basketForm:_id107:0:_id132");
		selenium.click("MAINbody:basketForm:_id107:1:_id132");
		selenium.click("MAINbody:basketForm:_id107:2:_id132");
		selenium.click("MAINbody:basketForm:_id107:3:_id132");
		selenium.click("MAINbody:basketForm:_id107:4:_id132");
		selenium.click("MAINbody:basketForm:_id105");
		selenium.waitForPageToLoad("30000");
		selenium.click("MAINbody:basketForm:_id153");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=LOGOUT");
		selenium.waitForPageToLoad("30000");
		checkForVerificationErrors();
	}
}
