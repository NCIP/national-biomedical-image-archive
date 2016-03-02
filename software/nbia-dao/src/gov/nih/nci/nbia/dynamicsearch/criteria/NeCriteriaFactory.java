/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch.criteria;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

public class NeCriteriaFactory implements CriteriaFactory {

	public SimpleExpression constructCriteria(String fieldName, String value, String fieldType)
	throws Exception
	{
		if (fieldType.equalsIgnoreCase("java.lang.String"))
		{
			if (value.equalsIgnoreCase("Not Populated (NULL)"))
			{
				value = "null";
			}
			return Restrictions.ne(fieldName, value).ignoreCase();
		}
		else
		{
			return Restrictions.ne(fieldName, ConstructorGenerator.getConstructor(fieldType).newInstance(value));
		}

	}


}
