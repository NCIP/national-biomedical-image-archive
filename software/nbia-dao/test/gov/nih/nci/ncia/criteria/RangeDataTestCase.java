/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.criteria;

import junit.framework.TestCase;
import gov.nih.nci.nbia.util.ResourceBundleUtil;

public class RangeDataTestCase extends TestCase {

	public void testNull() throws Exception {
		String validationError = RangeData.validateRange(">", "1", null, null);
		assertNull(validationError);
	}
	public void testValidateRangeEmpty() throws Exception {
		String validationError = RangeData.validateRange("", "", "", "");
		assertNull(validationError);
	}
	
	public void testValidateRangeEmptyLeftComponent() throws Exception {
		String validationError = RangeData.validateRange("", "3", "", "");
		assertEquals(ResourceBundleUtil.getString("searchNoRange"), validationError);		
				
		validationError = RangeData.validateRange(">", "", "", "");
		assertEquals(ResourceBundleUtil.getString("searchNoRange"), validationError);		
		
		validationError = RangeData.validateRange(">", "1", "", "");
		assertNotSame(ResourceBundleUtil.getString("searchNoRange"), validationError);		
	}
	
	public void testValidateRangeRightComponentIsAllOrNothing() throws Exception {
		String validationError = RangeData.validateRange(">", "1", ">", "");
		assertEquals(ResourceBundleUtil.getString("searchInvalidOperator"), validationError);		
				
		validationError = RangeData.validateRange(">", "1", "", "3");
		assertEquals(ResourceBundleUtil.getString("searchInvalidOperator"), validationError);		
		
		validationError = RangeData.validateRange(">", "1", "<", "4");
		assertNotSame(ResourceBundleUtil.getString("searchInvalidOperator"), validationError);
		
		validationError = RangeData.validateRange(">", "1", "", "");
		assertNotSame(ResourceBundleUtil.getString("searchInvalidOperator"), validationError);		
	}	
	
	
	public void testValidateRangeNonDecimalNumber() throws Exception {
		try {
			RangeData.validateRange(">", "X", "<", "43");
			fail("X should have made it bail even though this is stupid");
		}
		catch(NumberFormatException nfe) {
			
		}
	}	
	
	
	public void testValidateRangeLeftSideOnly() throws Exception {
		String validationError = RangeData.validateRange("<", "24", "<", "12");
		assertEquals(ResourceBundleUtil.getString("searchInvalidRightOperator"), validationError);
		
		validationError = RangeData.validateRange("<=", "24", "<", "12");
		assertEquals(ResourceBundleUtil.getString("searchInvalidRightOperator"), validationError);
		
		validationError = RangeData.validateRange("<=", "0", "", "");
		assertEquals(ResourceBundleUtil.getString("searchValueZero"), validationError);	
		
		validationError = RangeData.validateRange("<=", "-1", "", "");
		assertNull(validationError);		
	}
	
	public void testValidateRangeLeftGreaterThanRight() throws Exception {
		String validationError = RangeData.validateRange(">", "24", "<", "12");
		assertEquals(ResourceBundleUtil.getString("searchInvalidRange"), validationError);		
	}
	
	public void testValidateRangeNoErrorsNoRightSide() throws Exception {
		String validationError = RangeData.validateRange(">", "12", "", "");
		assertNull(validationError);		
	}
	
	public void testValidateRangeNoErrors() throws Exception {
		String validationError = RangeData.validateRange(">", "12", "<", "24");
		assertNull(validationError);		
	}		
	
	public void testIsEmpty() throws Exception {
		RangeData rangeData = new RangeData();
		assertTrue(rangeData.isEmpty());
		
		rangeData = new RangeData();
		rangeData.setFromValue(1.0);
		rangeData.setToValue(2.0);
		assertTrue(rangeData.isEmpty());	
		
		rangeData = new RangeData();
		rangeData.setFromOperator(">");
		assertTrue(rangeData.isEmpty());
				
		rangeData = new RangeData();
		rangeData.setToOperator(">");
		assertTrue(rangeData.isEmpty());
		
		rangeData = new RangeData();
		rangeData.setFromValue(1.0);
		assertTrue(rangeData.isEmpty());		
		
		rangeData = new RangeData();
		rangeData.setToValue(1.0);
		assertTrue(rangeData.isEmpty());
		
		rangeData = new RangeData();
		rangeData.setToOperator("<");
		rangeData.setToValue(1.0);
		assertFalse(rangeData.isEmpty());	
		
		rangeData = new RangeData();
		rangeData.setFromOperator("<");
		rangeData.setFromValue(1.0);
		assertFalse(rangeData.isEmpty());	
		
		rangeData = new RangeData();
		rangeData.setToOperator("<");
		rangeData.setToValue(1.0);
		rangeData.setFromOperator("<");
		rangeData.setFromValue(1.0);
		assertFalse(rangeData.isEmpty());		
	}
}
