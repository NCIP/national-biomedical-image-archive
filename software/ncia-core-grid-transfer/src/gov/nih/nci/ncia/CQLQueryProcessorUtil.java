/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.service.ServiceConfigUtil;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.ncia.domain.Image;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class CQLQueryProcessorUtil {


	/**
	 * This method used to be ImageFilesProcessor.getSOPInstanceUIDList and it
	 * used to call the grid service from the grid service...
	 *
	 * <p>This method computes a string 'sop1' OR 'sop2' OR ... 'sopN' based upon
	 * the result of the specified CQL query.  The sop strings will come from
	 * the Images that are deemed relevant for the query.
	 */
	public static List<String> computeSopInstanceUidOrClauseFromQuery(CQLQuery cqlQuery) throws Exception{
		//StringBuffer sbSOPInstanceUIDList = null;
		List<String> sbSOPInstanceUIDList = new ArrayList<String>();

		CQLQueryProcessor cqlQueryProcessor = CQLQueryProcessorUtil.getCqlQueryProcessorInstance();

		GridUtil.dumpCQLQuery(cqlQuery);

		CQLQueryResults results = cqlQueryProcessor.processQuery(cqlQuery);
		System.out.println("computeSopInstanceUidOrClauseFromQuery results from processQuery: " + results );
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results,
            NCIACoreServiceClient.class.getResourceAsStream("client-config.wsdd"));
//		sbSOPInstanceUIDList = new StringBuffer();
//		sbSOPInstanceUIDList.append('\'');

		System.out.println("CQLQueryResultsIterator iter: " + iter);

		while (iter.hasNext()) {
			Image image = (Image) iter.next();
			sbSOPInstanceUIDList.add(image.getSopInstanceUID());
// 			if (iter.hasNext()) {
//				sbSOPInstanceUIDList.append("' OR gi.SOPInstanceUID = '");
//			}
		}
//		sbSOPInstanceUIDList.append('\'');

		return sbSOPInstanceUIDList;
	}

	public static CQLQueryProcessor getCqlQueryProcessorInstance() throws Exception {
        String qpClassName = ServiceConfigUtil.getCqlQueryProcessorClassName();

        Class cqlQueryProcessorClass = Class.forName(qpClassName);

	    CQLQueryProcessor queryProcessorInstance = (CQLQueryProcessor)cqlQueryProcessorClass.newInstance();

        String serverConfigLocation = ServiceConfigUtil.getConfigProperty("serverConfigLocation");
        InputStream configStream = new FileInputStream(serverConfigLocation);
        Properties configuredProperties = getCqlQueryProcessorConfig();
        Properties defaultProperties = queryProcessorInstance.getRequiredParameters();
        Properties unionProperties = new Properties();
        String key;
        String value;
        for(Enumeration defaultKeys = defaultProperties.keys(); defaultKeys.hasMoreElements(); unionProperties.setProperty(key, value))
        {
            key = (String)defaultKeys.nextElement();
            System.out.println("getCqlQueryProcessorInstance: key = " + key);
            value = null;
            if(configuredProperties.keySet().contains(key)) {
                value = configuredProperties.getProperty(key);
                System.out.println("getCqlQueryProcessorInstance: value = " + value);
            }
            else {
                value = defaultProperties.getProperty(key);
                System.out.println("getCqlQueryProcessorInstance: default value = " + key);
            }
        }

        queryProcessorInstance.initialize(unionProperties, configStream);
        return queryProcessorInstance;
    }

	private static Properties cqlQueryProcessorConfig = null;

	private static Properties getCqlQueryProcessorConfig() throws Exception {
		if (cqlQueryProcessorConfig == null) {
			try {
				cqlQueryProcessorConfig = ServiceConfigUtil.getQueryProcessorConfigurationParameters();
			} catch (Exception ex) {
				throw new Exception(
					"Error getting query processor configuration parameters: " + ex.getMessage(), ex);
			}
		}
		// clone the query processor config instance
		// (in case they get modified by the Query Processor implementation)
		Properties clone = new Properties();
		Enumeration keyEnumeration = cqlQueryProcessorConfig.keys();
		while (keyEnumeration.hasMoreElements()) {
			String key = (String) keyEnumeration.nextElement();
			String value = cqlQueryProcessorConfig.getProperty(key);
			clone.setProperty(key, value);
		}
		return clone;
	}
}
