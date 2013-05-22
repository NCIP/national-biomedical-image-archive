/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.util.regex.Pattern;

/**
 * This is a regular expression bean used to validate different fields
 *
 * @author NCIA Team
 */

public class RegexUtil {

	// for alpha numeric values
    public static final String alphaNumeric = "[a-zA-Z0-9 ]+";

    // for alphabets
    public static final String alpha = "\\w+\\s?\\w*\\s?\\w*";

    // for numerics
    public static final String numeric = "[0-9]+";

    //public static final String phoneNumber = "\\^(\\()?([0-9]{3})(\\)|-)?([0-9]{3})(-)?([0-9]{4}|[0-9]{4})$";
    // for phone number
    //public static final String phoneNumber = "\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})";

    // fix for the International phone number just validate for 15 digits
    public static final String phoneNumber = "^(\\+)?[\\d\\(\\)\\-\\s]{6,22}$";

    // for e-mail
    public static final String email = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";

    // for validating dates
    public static final String date = "(0?[1-9]|1[012])[/](0?[1-9]|[12][0-9]|3[01])[/](19|20)\\d\\d";

    // Regular expression for validating the date for leap year etc to complex to understand so used java code instead
    //public static final String date ="^(?:(31)(\\D)(0?[13578]|1[02])\\2|(29|30)(\\D)(0?[13-9]|1[0-2])\\5|(0?[1-9]|1\\d|2[0-8])(\\D)(0?[1-9]|1[0-2])\\8)((?:1[6-9]|[2-9]\\d)?\\d{2})$|^(29)(\\D)(0?2)\\12((?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:16|[2468][048]|[3579][26])00)$";


    // for double values
    public static final String doubleVal = "[-+]?\\b\\d+(\\.\\d(\\d)?)\\b";


    public RegexUtil(){

    }

   public boolean cmpAlphaNumeric(String result){
    	boolean temp = false;
    	if(Pattern.matches(alphaNumeric, result)){
    		temp = true;
    	}

    	return temp;
    }

    public boolean cmpAlpha(String result){
    	boolean temp = false;
    	if(Pattern.matches(alpha, result)){
    		temp = true;
    	}
    	return temp;
    }

    public boolean cmpNumeric(String result){
    	boolean temp = false;
    	if(Pattern.matches(numeric, result)){
    		temp = true;
    	}
    	return temp;
    }

    public boolean cmpPhoneNumber(String result){
    	boolean temp = false;
    	if(Pattern.matches(phoneNumber, result)){
    		temp = true;
    	}
    	return temp;
    }

    public boolean cmpEmail(String result){
    	boolean temp = false;
    	if(Pattern.matches(email, result)){
    		temp = true;
    	}
    	return temp;
    }

    public boolean cmpDate(String result){
    	boolean temp = false;
    	if(Pattern.matches(date, result)){
    		temp = true;
    	}
    	return temp;
    }

    public boolean cmpDoubleVal(String result){
    	boolean temp = false;
    	if(Pattern.matches(doubleVal, result)){
    		temp = true;
    	}
    	return temp;
    }

    public boolean validDate(String aDateText) {

		int[] mMonthDays = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		boolean mResult = false;

		if (aDateText.length() == 10) {
			int mMonth = Integer.parseInt(aDateText.substring(0, 2));
			int mDay = Integer.parseInt(aDateText.substring(3, 5));
			int mYear = Integer.parseInt(aDateText.substring(6));

				if ((mMonth >= 1) && (mMonth <= 12) && (mYear > 1900) && (mYear < 2220)) {
					if (isLeapYear(mYear)) {
						mMonthDays[1] = 29;
					}

					if ((mDay >= 1) && (mDay <= mMonthDays[mMonth - 1])) {
						mResult = true;
					}
				}
		}

		return mResult;
	}

    /**
	 * 1) If a year is divisible by 4 it is a leap year if #2 does not apply. 2)
	 * If a year is divisible by 100 it is not a leap year unless #3 applies. 3)
	 * If a year is divisible by 400 it is a leap year.
	 *
	 */
    public boolean isLeapYear  ( int aYear )
    {
    	// divisible by 4
    	// divisible by 4 and not 100  (1900 is not a leap year)
    	// divisible by 4 and not 100 unless divisible by 400

    	return (aYear % 4 == 0) && ((aYear % 100 != 0) || (aYear % 400 == 0));
    }
}