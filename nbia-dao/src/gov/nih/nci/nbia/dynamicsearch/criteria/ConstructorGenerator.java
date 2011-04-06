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
