/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.customqueryprocessor.cql1;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.sdkquery4.processor.SDK4QueryProcessor;


public class NBIACql1QueryProcessor extends SDK4QueryProcessor {
	/**
	 * This method modifies the CQLQuery to apply the filtering 
	 **/
	public CQLQueryResults processQuery(CQLQuery cqlQuery) throws MalformedQueryException, 
	                                                              QueryProcessingException {
		throw new RuntimeException("not impl yet.  port from ncia-core-grid-transfer");
	}
}
