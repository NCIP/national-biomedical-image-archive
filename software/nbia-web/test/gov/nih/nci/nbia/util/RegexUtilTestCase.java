/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import junit.framework.TestCase;

public class RegexUtilTestCase extends TestCase {

	RegexUtil regexBean = null;

	public RegexUtilTestCase() {
        super();
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
    	regexBean = new RegexUtil();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public void testValidateDate() throws Exception {

    	boolean datevalidation = false;

    	datevalidation = regexBean.validDate("04/31/2007");

    	assertNotNull("datevalidation result not returned", datevalidation);
        assertFalse("datevalidation result", datevalidation);

        datevalidation = regexBean.validDate("04/30/2007");
        assertTrue("datevalidation result", datevalidation);


        datevalidation = regexBean.validDate("02/29/2007");
        assertFalse("datevalidation result", datevalidation);

        datevalidation = regexBean.validDate("02/29/2008");
        assertTrue("datevalidation result", datevalidation);

        datevalidation = regexBean.validDate("02/30/2000");
        assertFalse("datevalidation result", datevalidation);

        datevalidation = regexBean.validDate("02/29/2004");
        assertTrue("datevalidation result", datevalidation);

        datevalidation = regexBean.validDate("02/29/2007");
        assertFalse("datevalidation result", datevalidation);

        datevalidation = regexBean.validDate("02/29/2006");
        assertFalse("datevalidation result", datevalidation);

        datevalidation = regexBean.validDate("11/30/2004");
        assertTrue("datevalidation result", datevalidation);

    }

    public void testcmpDate(){

    	boolean datevalidation = false;

    	datevalidation = regexBean.cmpDate("04/3o/2007");
        assertFalse("datevalidation result", datevalidation);

        datevalidation = regexBean.cmpDate("05/30/2oo7");
        assertFalse("datevalidation result", datevalidation);

        datevalidation = regexBean.cmpDate("o6/30/2007");
        assertFalse("datevalidation result", datevalidation);

        datevalidation = regexBean.cmpDate("o7/31/2o07");
        assertFalse("datevalidation result", datevalidation);

        datevalidation = regexBean.cmpDate("o8/3o/2o07");
        assertFalse("datevalidation result", datevalidation);

        datevalidation = regexBean.cmpDate("o9/23/2oo7");
        assertFalse("datevalidation result", datevalidation);

        datevalidation = regexBean.cmpDate("06/30/3007");
        assertFalse("datevalidation result", datevalidation);

        datevalidation = regexBean.cmpDate("36/30/3007");
        assertFalse("datevalidation result", datevalidation);

        datevalidation = regexBean.cmpDate("06/40/3007");
        assertFalse("datevalidation result", datevalidation);
    }

    public void testcmpDoubleVal(){

    	boolean doublevalue = false;

    	doublevalue = regexBean.cmpDoubleVal("20.0");
        assertTrue("is Double Value", doublevalue);

    	doublevalue = regexBean.cmpDoubleVal("20");
    	assertFalse("is not Double Value", doublevalue);

    	doublevalue = regexBean.cmpDoubleVal("34ry4743");
    	assertFalse("is not Double Value", doublevalue);

    	doublevalue = regexBean.cmpDoubleVal("dfhdf");
    	assertFalse("is not Double Value", doublevalue);

    	doublevalue = regexBean.cmpDoubleVal("02/03/2006");
    	assertFalse("is not Double Value", doublevalue);

    }

    public void testcmpEmail(){

    	boolean verifyEmail = false;

    	verifyEmail = regexBean.cmpEmail("porankisv@mail.nih.gov");
        assertTrue("is Valid email", verifyEmail);

        verifyEmail = regexBean.cmpEmail("porankisv");
        assertFalse("is not a Valid email", verifyEmail);

        verifyEmail = regexBean.cmpEmail("34643");
        assertFalse("is not a Valid email", verifyEmail);

    }

    public void testcmpAlphaNumeric() {

    	boolean isAlphaNumeric = false;

    	isAlphaNumeric = regexBean.cmpAlphaNumeric("porankisv34543");
        assertTrue("is isAlphaNumeric", isAlphaNumeric);

    	isAlphaNumeric = regexBean.cmpAlphaNumeric("345@43");
    	assertFalse("is isAlphaNumeric", isAlphaNumeric);
    }
    public void testcmpAlpha(){

    	boolean isAlpha = false;

    	isAlpha = regexBean.cmpAlpha("porankisv");
        assertTrue("is isAlpha", isAlpha);

        isAlpha = regexBean.cmpAlpha("7867#89");
        assertFalse("is isAlpha", isAlpha);
    }

    public void testcmpNumeric(){
    	boolean isNumeric = false;

    	isNumeric = regexBean.cmpNumeric("7857898");
        assertTrue("is isNumeric", isNumeric);

        isNumeric = regexBean.cmpNumeric("786789hfkhkj");
        assertFalse("is isNumeric", isNumeric);

    }
    public void testcmpPhoneNumber(){
    	boolean isValidPhone = false;

    	isValidPhone = regexBean.cmpPhoneNumber("7857897969");
        assertTrue("is isNumeric", isValidPhone);

    	isValidPhone = regexBean.cmpPhoneNumber("7857");
        assertFalse("is isNumeric", isValidPhone);

        isValidPhone = regexBean.cmpPhoneNumber("785799");
        assertTrue("is isNumeric", isValidPhone);

        isValidPhone = regexBean.cmpPhoneNumber("785799999999222");
        assertTrue("is isNumeric", isValidPhone);

        isValidPhone = regexBean.cmpPhoneNumber("(91)(562) 4021700");
        assertTrue("is isNumeric", isValidPhone);

        isValidPhone = regexBean.cmpPhoneNumber("(852) 2956 1234");
        assertTrue("is isNumeric", isValidPhone);

        isValidPhone = regexBean.cmpPhoneNumber("+44 (20) 7486 5800");
        assertTrue("is isNumeric", isValidPhone);

        isValidPhone = regexBean.cmpPhoneNumber("+61 (0) 2 1234 5678");
        assertTrue("is isNumeric", isValidPhone);

    	isValidPhone = regexBean.cmpPhoneNumber("+61$(0)#2 1234 5678");
        assertFalse("is isNumeric", isValidPhone);

    	isValidPhone = regexBean.cmpPhoneNumber("123abc987");
        assertFalse("is isNumeric", isValidPhone);

    }

}
