package gov.nih.nci.nbia.dynamicsearch.criteria;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class EqCriteriaFactory implements CriteriaFactory {

	public Criterion constructCriteria(String fieldName, String value, String fieldType)
	throws Exception
	{
		if (fieldType.equalsIgnoreCase("java.lang.String"))
		{
			if (value.equalsIgnoreCase("Not Populated (NULL)"))
			{
				return Restrictions.isNull(fieldName);
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