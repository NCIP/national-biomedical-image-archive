/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.util.Date;

public class DateValidator {

	/**
	 * Ensures that :
	 * 
	 * <ul>
	 * <li>both the from and to date are of the form MM/dd/yyyy</li>
	 * <li>both the from and to date are legit in terms of days per month including leap yr</li>
	 * <li>no dates are in the future</li>
	 * <li>the from date is before or the same as the to date</li>
	 * </ul>
	 * 
	 * A message is returned for a validation failure and null
	 * is returned if all is well.	
	 * 
	 * note: this used to live in VerifySubmissionBean.  broken out
	 * to support unit testing.
	 */
    public String validateDates(Date fromDate, Date toDate, boolean fromMandatory) {
                       
        if (fromDate == null && fromMandatory) {
            return MessageUtil.getString("fromDaterequired");
        }
        
        if(fromDate!=null) {
        	String result = validateFuture(fromDate, "From Date");
        	if(result!=null) {
        		return result;
        	}
        }
        
        if(toDate==null) {
        	return null;
        }
        else {
        	String result = validateFuture(toDate, TO_DATE);
        	if(result!=null) {
        		return result;
        	}
        	else {
        		result = validateComparative(fromDate, toDate);
        		return result;
        	}
        }
    }

    ////////////////////////////////////PRIVATE/////////////////////////////////////
    
    private static final String TO_DATE = "To date";
    
    private String validateFuture(Date date, String fieldName) {
        Date todayDate = new Date();
    	
    	int rs = date.compareTo(todayDate);

        if (rs > 0) {
            return MessageUtil.getString("noFutureDate", 
            		                     new Object[] { fieldName });
        }   
        return null;
    }
    
    private String validateComparative(Date fromDate, Date toDate) {
    	if(toDate!=null && fromDate==null) {
    		return MessageUtil.getString("fromDaterequired");
    	}
        int i = fromDate.compareTo(toDate);

        if (i > 0) {
            return MessageUtil.getString("toDateGreaterThanFromDate");
        }

        return null;
    }
   
}
