/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

import java.util.HashMap;
import java.util.Map;

import gov.nih.nci.nbia.dynamicsearch.criteria.ContainsCriteriaFactory;
import gov.nih.nci.nbia.dynamicsearch.criteria.CriteriaFactory;
import gov.nih.nci.nbia.dynamicsearch.criteria.EndWithCriteriaFactory;
import gov.nih.nci.nbia.dynamicsearch.criteria.EqCriteriaFactory;
import gov.nih.nci.nbia.dynamicsearch.criteria.GeCriteriaFactory;
import gov.nih.nci.nbia.dynamicsearch.criteria.GtCriteriaFactory;
import gov.nih.nci.nbia.dynamicsearch.criteria.LeCriteriaFactory;
import gov.nih.nci.nbia.dynamicsearch.criteria.LtCriteriaFactory;
import gov.nih.nci.nbia.dynamicsearch.criteria.NeCriteriaFactory;
import gov.nih.nci.nbia.dynamicsearch.criteria.StartWithCriteriaFactory;

class LogicalOperatorCriteriaFactories {

    public static CriteriaFactory getCriteriaFactory(String logicalOperator) {
    	return critMap.get(logicalOperator);
    }
    
    private static Map<String, CriteriaFactory> critMap = new HashMap<String, CriteriaFactory>();

	private static final String[] logicOperator = 
	{">","<",">=","<=","=","!=","starts with","ends with","contains","equals"};
	
	private LogicalOperatorCriteriaFactories() {	
	}
	
    static {
		 critMap.put(logicOperator[0], new GtCriteriaFactory());
		 critMap.put(logicOperator[1], new LtCriteriaFactory());
		 critMap.put(logicOperator[2], new GeCriteriaFactory());
		 critMap.put(logicOperator[3], new LeCriteriaFactory());
		 critMap.put(logicOperator[4], new EqCriteriaFactory());
		 critMap.put(logicOperator[5], new NeCriteriaFactory());
		 critMap.put(logicOperator[6], new StartWithCriteriaFactory());
		 critMap.put(logicOperator[7], new EndWithCriteriaFactory());
		 critMap.put(logicOperator[8], new ContainsCriteriaFactory());
		 critMap.put(logicOperator[9], new EqCriteriaFactory());
	}
}