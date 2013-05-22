/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

import java.util.Map;

/**
 * 
 * @author zhoujim
 *
 */
public class DataFieldTypeMap {
	private static Map<String, String> typeTable = DataFieldParser.getItemType();
	//Since Visibility is not showing up on the GUI, just manual add this in hashMap.
	static {
		typeTable.put("visibility", "java.lang.String");
	}
	
	public static String getDataFieldType(String field)
	{
		String type = typeTable.get(field.trim());
		return type;
	}
	private DataFieldTypeMap() {
		
	}
}
