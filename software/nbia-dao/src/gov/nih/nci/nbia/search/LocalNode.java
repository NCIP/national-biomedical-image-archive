/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.ncia.search.NBIANode;

/**
 * This is a factory to retrieve an NBIANode that represents the
 * local node.  The display name and URL are going to be populated
 * from the system properties (a la NCIAConfig).
 */
public class LocalNode {
	public static NBIANode getLocalNode() {
		return localNode;
	}
	
	private static NBIANode localNode = new NBIANode(true, 
			                                         NCIAConfig.getLocalNodeName(),
			                                         NCIAConfig.getLocalGridURI());

}
