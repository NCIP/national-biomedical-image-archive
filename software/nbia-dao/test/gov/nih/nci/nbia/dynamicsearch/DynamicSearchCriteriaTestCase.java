/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

import junit.framework.TestCase;

public class DynamicSearchCriteriaTestCase extends TestCase {
	public void testField() {
		dynamicSearchCriteria.setField("test");
		String field = dynamicSearchCriteria.getField();
		assertEquals(field, "test");
	}

	public void testOperator() {
		Operator operator= new Operator();
		operator.setValue("foo");
		operator.setDescription("test_desc");
		
		dynamicSearchCriteria.setOperator(operator);
		Operator op = dynamicSearchCriteria.getOperator();
		assertEquals(op.getValue(), "foo");				
		assertEquals(op.getDescription(), "test_desc");						
	}

	public void testValue() {
		dynamicSearchCriteria.setValue("test");
		String value = dynamicSearchCriteria.getValue();
		assertEquals(value, "test");
	}

	
	public void testDataGroup() {
		dynamicSearchCriteria.setDataGroup("test");
		String dataGroup = dynamicSearchCriteria.getDataGroup();
		assertEquals(dataGroup, "test");
	}

	public void testLabel() {
		dynamicSearchCriteria.setLabel("test");
		String label = dynamicSearchCriteria.getLabel();
		assertEquals(label, "test");
	}

	//////////////////////////////////PROTECTED///////////////////////
	
	
	protected void setUp() throws Exception {
		super.setUp();
		dynamicSearchCriteria = new DynamicSearchCriteria();
	}
	
	private DynamicSearchCriteria dynamicSearchCriteria;

}
