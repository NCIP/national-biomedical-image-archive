/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch.criteria;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class EqCriteriaFactory implements CriteriaFactory {

	public Criterion constructCriteria(String fieldName, String value, String fieldType)
	throws Exception
	{
		if (fieldType.equalsIgnoreCase("java.lang.String"))
		{
			if (value.equalsIgnoreCase("Not Populated (NULL)") || value.equalsIgnoreCase("NULL"))
			{
				return Restrictions.isNull(fieldName);
			}
			if ((fieldName.indexOf("SOPInstanceUID")>1)||
				(fieldName.indexOf("acquisitionDatetime")>1)||
				(fieldName.indexOf("SOPClassUID")>1))
			{
				return Restrictions.eq(fieldName, value);
			}
			return Restrictions.eq(fieldName, value).ignoreCase();
		}
		else
		{
			return Restrictions.eq(fieldName, 
					               ConstructorGenerator.getConstructor(fieldType).newInstance(value));
		}	
	}
}