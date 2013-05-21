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
