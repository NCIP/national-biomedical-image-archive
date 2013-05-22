/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

/**
 * This class is utility class for Query Builder. The purpose of this class is to figure out
 * what the relationship among NBIA database tables. By giving a two or more tables names,
 * this class will analyze NBIA database, and return appropriate HQL statement for join
 * relation.
 */
import gov.nih.nci.nbia.xmlobject.Element;
import gov.nih.nci.nbia.xmlobject.Relation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * This object doesn't appear to have anything to do with operators per se
 * it has the order list of table relationships....
 */
public class TableRelationships {
	//private static Logger logger = Logger.getLogger(RelationalOperator.class);
	private static List<Element> relationTree;
	//consider to move this array into XML file.

	/**
	 * Static block to parse relational xml which contains all relationship among predefined
	 * tables. This parser is only needed to execute once for performance aspect.
	 */
	public TableRelationships() throws Exception
	{
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("relation", Relation.class);
		xstream.alias("element", Element.class);
		xstream.addImplicitCollection(Relation.class,
				                      "element",
				                      Element.class);

		//pass this in instead?
		File relationshipConfigFile = DynamicSearchConfig.getRelationshipConfigFile();

		InputStream in = new FileInputStream(relationshipConfigFile);
		Relation o = (Relation)xstream.fromXML(new InputStreamReader(in));
		relationTree = o.getElement();
		in.close();
	}

	/**
	 *
	 * sorts criteria by ordering of "source" attribute in this
	 * object's "relationTree"?
	 */
	public List<DynamicSearchCriteria> sortTableName(List<DynamicSearchCriteria> search)
		throws Exception
	{
		//assert search != null

		List<DynamicSearchCriteria> sortedList = new ArrayList<DynamicSearchCriteria>();

		//Sort given table names
		for (int i =0; i <relationTree.size(); i++)
		{
			for (int k = 0; k < search.size(); k++)
			{
				if (relationTree.get(i).getSource().equalsIgnoreCase(search.get(k).getDataGroup()))
				{
					sortedList.add(search.get(k));
				}
			}
		}

		return sortedList;
	}

	//this is not a tree.  its linear
	public List<Element> getRelationTree()
	{
		return Collections.unmodifiableList(relationTree);
	}
}
