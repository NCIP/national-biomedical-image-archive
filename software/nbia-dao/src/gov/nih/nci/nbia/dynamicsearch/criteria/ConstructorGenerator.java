/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch.criteria;

import java.lang.reflect.Constructor;

/*pkg*/ class ConstructorGenerator {

	public static Constructor getConstructor(String type) throws Exception
	{
		Class c = Class.forName(type);
		Constructor con = c.getConstructor(String.class);

		return con;
	}

}
