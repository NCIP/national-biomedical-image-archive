/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

import gov.nih.nci.nbia.dynamicsearch.criteria.CriteriaFactory;
import junit.framework.TestCase;

public class LogicalOperatorCriteriaFactoriesTestCase extends TestCase {

	public void testGetCriteriaFactory() {
		CriteriaFactory gbg = LogicalOperatorCriteriaFactories.getCriteriaFactory("garbage");
		assertNull(gbg);
		
		CriteriaFactory cf = LogicalOperatorCriteriaFactories.getCriteriaFactory(">");
		assertNotNull(cf);

		cf = LogicalOperatorCriteriaFactories.getCriteriaFactory("<");
		assertNotNull(cf);
		
		cf = LogicalOperatorCriteriaFactories.getCriteriaFactory(">=");
		assertNotNull(cf);
		
		cf = LogicalOperatorCriteriaFactories.getCriteriaFactory("<=");
		assertNotNull(cf);
		
		cf = LogicalOperatorCriteriaFactories.getCriteriaFactory("=");
		assertNotNull(cf);
		
		cf = LogicalOperatorCriteriaFactories.getCriteriaFactory("!=");
		assertNotNull(cf);
		
		cf = LogicalOperatorCriteriaFactories.getCriteriaFactory("starts with");
		assertNotNull(cf);
		
		cf = LogicalOperatorCriteriaFactories.getCriteriaFactory("ends with");
		assertNotNull(cf);
		
		cf = LogicalOperatorCriteriaFactories.getCriteriaFactory("contains");
		assertNotNull(cf);		
	}

	
}
