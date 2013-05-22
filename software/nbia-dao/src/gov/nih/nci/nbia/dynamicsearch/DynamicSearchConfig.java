/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

import java.io.File;

/**
 * This encapsulates access to any configuration information
 * needed for dynamic search, like paths to config files, etc.
 * 
 * <p>As it stands, this object depends on JBoss deployment.
 * If trying to deploy somewhere else besides JBoss... turn this
 * into a interface/factory instead with a different instance for
 * the other container (or whatever)
 */
public class DynamicSearchConfig {

	public static File getDataSourceItemConfigFile() {
		String dynamicSearchPropertyPath = System.getProperty("jboss.server.data.dir");
		return new File(dynamicSearchPropertyPath,
				        "DataSourceItem.xml");
	}
	
	public static File getRelationshipConfigFile() {
		String dynamicSearchPropertyPath = System.getProperty("jboss.server.data.dir");
		return new File(dynamicSearchPropertyPath,
				        "relationship.xml");		
	}
	
	private DynamicSearchConfig() {
		
	}
}
