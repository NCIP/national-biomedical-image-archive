/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.2  2007/08/14 16:53:47  bauerd
* Removed the repopulate logic and cleaned up the class files
*
* Revision 1.1  2007/08/07 12:05:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.8  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.7  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.criteria;

import gov.nih.nci.nbia.util.ResourceBundleUtil;
import gov.nih.nci.nbia.util.StringUtil;
import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;


/**
 * Represents a set of operators and values used for specifying a range
 *
 * @author NCIA Team
 */
public class RangeData {
    private static Logger logger = Logger.getLogger(RangeData.class);

    // Constants for operators
    public final static String GREATER_THAN = ">";
    public final static String LESS_THAN = "<";
    public final static String EQUAL_TO = "=";
    public final static String GREATER_THAN_EQUAL = ">=";
    public final static String LESS_THAN_EQUAL = "<=";

    // Lists of operators to use on the left and right
    // sides of ranges for display purposes
    private static List<SelectItem> leftOperatorItems;
    private static List<SelectItem> rightOperatorItems;

    static {
        // Set left side operators
        leftOperatorItems = new ArrayList<SelectItem>();
        leftOperatorItems.add(new SelectItem(""));
        leftOperatorItems.add(new SelectItem(EQUAL_TO));
        leftOperatorItems.add(new SelectItem(GREATER_THAN));
        leftOperatorItems.add(new SelectItem(LESS_THAN));
        leftOperatorItems.add(new SelectItem(GREATER_THAN_EQUAL));
        leftOperatorItems.add(new SelectItem(LESS_THAN_EQUAL));

        // Set right side operators
        rightOperatorItems = new ArrayList<SelectItem>();
        rightOperatorItems.add(new SelectItem(""));
        rightOperatorItems.add(new SelectItem(LESS_THAN));
        rightOperatorItems.add(new SelectItem(LESS_THAN_EQUAL));
    }

    // The components of the range
    private String fromOperator;
    private Double fromValue;
    private String toOperator;
    private Double toValue;

    /**
     * Called from criteria classes.  Builds list of
     * strings to be saved.
     */
    protected List<String> getMultipleValues() {
        List<String> returnList = new ArrayList<String>();
        returnList.add(0, getFromOperator());
        returnList.add(1, String.valueOf(getFromValue()));

        if ((toOperator != null) && !toOperator.equals("")) {
            returnList.add(2, getToOperator());
            returnList.add(3, String.valueOf(getToValue()));
        }

        return returnList;
    }

    /**
     * Populates a QueryAttribute from the operators and values
     *
     * @param attr
     */
    public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
        String subAttributeName = attr.getSubAttributeName();

        if (subAttributeName.equals("1")) {
            setFromOperator(attr.getAttributeValue());
        } else if (subAttributeName.equals("2")) {
            setFromValue(attr.getAttributeValue());
        } else if (subAttributeName.equals("3")) {
            setToOperator(attr.getAttributeValue());
        } else if (subAttributeName.equals("4")) {
            setToValue(attr.getAttributeValue());
        } else {
            throw new RuntimeException("Unsupported sub attribute");
        }
    }

    /**
     * Returns a display string showing all operators and values
     */
    public String getDisplayValue() {
        if ((toOperator != null) && !toOperator.equals("")) {
            return fromOperator + " " + fromValue + " AND " + toOperator + " " +
            toValue;
        } else {
            return fromOperator + " " + fromValue;
        }
    }

    /**
     * This range is "empty" if:
     * 
     * <ul>
     * <li>neither operator is specified (regardless of value??)
     * <li>assuming both operators are not blank, then if either side
     * has an operator it must have a value (or be considered empty).
     * </ul>
     * 
     * If there's no from:/left side specified, but there's a to:/right side
     * specified, it's NOT empty which doesn't really jive with
     * {@link #validateRange}
     */
    public boolean isEmpty() {
        boolean fromOpBlank = StringUtil.isEmpty(fromOperator);
        boolean toOpBlank = StringUtil.isEmpty(toOperator);
        boolean fromValueBlank = (fromValue == null);
        boolean toValueBlank = (toValue == null);

        if (fromOpBlank && toOpBlank) {
            return true;
        }

        if (!fromOpBlank && fromValueBlank) {
            return true;
        }

        if (!toOpBlank && toValueBlank) {
            return true;
        }

        return false;
    }


    
    /**
     * This method validates a range of numeric values.  
     * 
     * <p>Each side of the range includes a relational operator (>,>=,<,<=,=) 
     * and the value. The value can include a decimal number with an optional 
     * space and then anything to the right of the space is ignored (like units: mm).
     *  
     * <p>Though vacuous, if neither side is specified, the range is considered
     * to be valid.  Not counting when nothing is specified, the left "side" 
     * is required, and the right side is optional.  When specifying
     * a side, both the operator and value must be specified.
     * 
     * <p>When the range is ok, null is returned.  Otherwise a cryptic string
     * is returned.... this string is a key to a resource bundle or
     * something?  no clue.
     * 
     * <p>(Apparently) bad things about this method that am afraid to change:
     * <ul>
     * <li>This is a ** validation ** method whose main point is to return
     * a _message_ if something is wrong, but if the value isn't a decimal number 
     * it throws an exception instead of returning a message.  
     * <li>The left value being 0 is considered an error.  What about negative
     * numbers!!!!?!?!!?!??!?
     * <li>when an operator is = and there is both a left and right side
     * this method considers that valid....
     * </ul>
     */
    public static String validateRange(String opLeft, 
    		                           String valLeft,
    		                           String opRight, 
    		                           String valRight) {
    	
        logger.debug("validation for range: " + opLeft + "|" + valLeft + "|" +
            opRight + "|" + valRight);

        if (allRangeComponentsAreEmpty(opLeft,
        		                       valLeft,
        		                       opRight,
        		                       valRight)) {
            return null;
        }

        // Make sure that the left operator and value are filled out
        if (leftSideHasEmptyComponent(opLeft, valLeft)) {
            return ResourceBundleUtil.getString("searchNoRange");
        }

        // Makes sure both right values are either selected or deselected
        if (rightSideHasOnlyOneComponent(opRight, valRight)) {
            return ResourceBundleUtil.getString("searchInvalidOperator");
        }

        // Look for a space in the value (e.g. '0 mm') and ignore anything after the space
        valLeft = stripUnits(valLeft);

        double leftVal = Double.parseDouble(valLeft);

        // Makes sure no values are selected on the right if you are using less
        // than
        if (opLeft.equals(RangeData.LESS_THAN) ||
            opLeft.equals(RangeData.LESS_THAN_EQUAL)) {
            if (!StringUtil.isEmpty(opRight)) {
                return ResourceBundleUtil.getString("searchInvalidRightOperator");
            }

            if (leftVal == 0) {
                return ResourceBundleUtil.getString("searchValueZero");
            }
        } 
        else {
            if (!StringUtil.isEmpty(valRight)) {
                valRight = stripUnits(valRight);

                double rightVal = Double.parseDouble(valRight);

                // Makes sure that the values of the range selected are valid
                if (rightVal <= leftVal) {
                    return ResourceBundleUtil.getString("searchInvalidRange");
                }
            }
        }

        // Return null if no errors found
        return null;
    }

    public static List<SelectItem> getLeftOperatorItems() {
        return leftOperatorItems;
    }

    public static List<SelectItem> getRightOperatorItems() {
        return rightOperatorItems;
    }

    public String getFromOperator() {
        return fromOperator;
    }

    public void setFromOperator(String fromOperator) {
        this.fromOperator = fromOperator;
    }

    public Double getFromValue() {
        return fromValue;
    }

    public void setToValue(String toValue) {
        if ((toValue != null) && !toValue.equals("")) {
            try {
                this.toValue = new Double(toValue);
            } catch (Exception e) {
                this.toValue = null;
            }
        }
    }

    public void setFromValue(String fromValue) {
        if ((fromValue != null) && !fromValue.equals("")) {
            try {
                this.fromValue = new Double(fromValue);
            } catch (Exception e) {
                this.fromValue = null;
            }
        }
    }

    public void setFromValue(Double fromValue) {
        this.fromValue = fromValue;
    }

    public String getToOperator() {
        return toOperator;
    }

    public void setToOperator(String toOperator) {
        this.toOperator = toOperator;
    }

    public Double getToValue() {
        return toValue;
    }

    public void setToValue(Double toValue) {
        this.toValue = toValue;
    }
    
    //////////////////////////////////PRIVATE/////////////////////////////////////
    
    private static boolean allRangeComponentsAreEmpty(String opLeft,
    		                                          String valLeft,
    		                                          String opRight,
    		                                          String valRight) {
        boolean fromOpBlank = StringUtil.isEmpty(opLeft);
        boolean toOpBlank = StringUtil.isEmpty(opRight);
        boolean fromValueBlank = StringUtil.isEmpty(valLeft);
        boolean toValueBlank = StringUtil.isEmpty(valRight);

        return (fromOpBlank && toOpBlank && fromValueBlank && toValueBlank);  	
    }
    
    private static boolean leftSideHasEmptyComponent(String opLeft, 
    		                                         String valLeft) {
        return StringUtil.isEmpty(opLeft) ||StringUtil.isEmpty(valLeft);	
    }
    
    private static boolean rightSideHasOnlyOneComponent(String opRight, 
    		                                            String valRight) {
    	return StringUtil.isEmpty(opRight) ^ StringUtil.isEmpty(valRight);
    }
    
    private static String stripUnits(String theValue) {
    	String value = theValue;
        int indexOfSpace = value.indexOf(' ');

        if (indexOfSpace > 0) {
        	value = value.substring(0, value.indexOf(' '));
        }   
        return value;
    }    
}
