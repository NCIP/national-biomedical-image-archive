/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: VisibilityStatus.java 5726 2008-07-23 20:56:06Z zhoujim $
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/07 12:05:22  bauerd
* *** empty log message ***
*
* Revision 1.7  2007/06/25 19:09:31  panq
* Add and remove the visibility status.
*
* Revision 1.6  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.qctool;

import java.util.Properties;
import gov.nih.nci.nbia.util.*;


/**
 * This is an enum that holds the functionality for VisibilityStatus
 *
 *
 * @author NCIA Team
 */
public enum VisibilityStatus {
	
	NOT_YET_REVIEWED(0, "Not Yet Reviewed"),
    VISIBLE(1, "Visible"),
    NOT_VISIBLE(2, "Not Visible"),
    TO_BE_DELETED(3, "To Be Deleted"),
    DELETE(4, "Delete"),
	STAGE_1(5, getPropertyValue("qctool.visibility.stage1")),
	STAGE_2(6, getPropertyValue("qctool.visibility.stage2")),
	STAGE_3(7, getPropertyValue("qctool.visibility.stage3")),
	STAGE_4(8, getPropertyValue("qctool.visibility.stage4")),
	STAGE_5(9, getPropertyValue("qctool.visibility.stage5")),
	STAGE_6(10, getPropertyValue("qctool.visibility.stage6")),
	STAGE_7(11, getPropertyValue("qctool.visibility.stage7")),
	DOWNLOADABLE(12, "Downloadable");
	
	
	public static String getPropertyValue(String messageVariable){
		
		String retStr = NCIAConfig.getQCToolPropertyValue(messageVariable);
		
	//	System.out.println("=========== In VisibilityStatus:getPropertyValue(..) - messageVariable is: " + messageVariable + ", retStrValue is: " + retStr);
		
		return retStr;	
	}
	

    /**
     * Reads in an integer and creates the correct VisibilityStatus for that integer.
     *
     * @param status
     */
    public static VisibilityStatus statusFactory(Integer status) {
        if (status == null) {
            return NOT_YET_REVIEWED;
        } else if (status == 0) {
            return NOT_YET_REVIEWED;
        } else if (status == 1) {
            return VISIBLE;
        } else if (status == 2) {
            return NOT_VISIBLE;
        } else if (status == 3) {            
           return TO_BE_DELETED;
        } else if (status == 4) {
            return DELETE;
        } else if (status == 5) {
            return STAGE_1;
        } else if (status == 6) {
            return STAGE_2;
        } else if (status == 7) {
            return STAGE_3;
        } else if (status == 8) {
            return STAGE_4;
        } else if (status == 9) {
            return STAGE_5;
        } else if (status == 10) {
            return STAGE_6;
        } else if (status == 11) {
            return STAGE_7;
        } else if (status == 12) {
        	return DOWNLOADABLE;
        }
        return null;
    }

    /**
     * Reads in a string and creates the correct status for that string.
     *
     * @param status
     */
    public static VisibilityStatus stringStatusFactory(String status) {
        if (status.equals(NOT_YET_REVIEWED.getText())) {
            return NOT_YET_REVIEWED;
        } else if (status.equals(VISIBLE.getText())) {
            return VISIBLE;
        } else if (status.equals(NOT_VISIBLE.getText())) {
            return NOT_VISIBLE;
        } else if (status.equals(TO_BE_DELETED.getText())) {
            return TO_BE_DELETED;
        } else if (status.equals(DELETE.getText())) {
            return DELETE;
        } else if (status.equals(STAGE_1.getText())) {
            return STAGE_1;
        } else if (status.equals(STAGE_2.getText())) {
            return STAGE_2;
        } else if (status.equals(STAGE_3.getText())) {
            return STAGE_3;
        } else if (status.equals(STAGE_4.getText())) {
            return STAGE_4;
        } else if (status.equals(STAGE_5.getText())) {
            return STAGE_5;
        } else if (status.equals(STAGE_6.getText())) {
            return STAGE_6;
        } else if (status.equals(STAGE_7.getText())) {
            return STAGE_7;
        } else if(status.equals(DOWNLOADABLE.getText())) {
        	return DOWNLOADABLE;
        }
        return null;
    }

    private final Integer numberValue;
    private final String text;

    VisibilityStatus(Integer number, String text) {
        numberValue = number;
        this.text = text;
        
    }

    /**
     * Returns a string representation of the number
     * method comment
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String ret = numberValue.toString();
        return ret;
    }

    /**
     * Returns a textual representation of the VisibilityStatus.
     */
    public String getText() {
        return text;
    }

	public Integer getNumberValue() {
		return numberValue;
	}
}
