/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

import gov.nih.nci.nbia.xmlobject.Element;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

public class TableRelationshipsTestCase extends TestCase {

	public void testSortTableName() throws Exception {
		TableRelationships ro = new TableRelationships();

		List<DynamicSearchCriteria> myList = new ArrayList<DynamicSearchCriteria>();

		DynamicSearchCriteria dsc = new DynamicSearchCriteria();
		dsc.setDataGroup("GeneralImage");
		dsc.setField("visibility");
		dsc.setValue("1");
		myList.add(dsc);

		dsc = new DynamicSearchCriteria();
		dsc.setDataGroup("TrialDataProvenance");
		dsc.setField("project");
		dsc.setValue("Jim");
		myList.add(dsc);

		dsc = new DynamicSearchCriteria();
		dsc.setDataGroup("Study");
		dsc.setField("studyInstanceUID");
		dsc.setValue("1234");
		myList.add(dsc);

		dsc = new DynamicSearchCriteria();
		dsc.setDataGroup("GeneralImage");
		dsc.setField("whateven");
		dsc.setValue("4321");
		myList.add(dsc);

		for (DynamicSearchCriteria c : myList)
		{
			System.out.println(" === before == " + c.getDataGroup());
		}

		List<DynamicSearchCriteria> endList = ro.sortTableName(myList);
		for (DynamicSearchCriteria c : endList)
		{
			System.out.println(" === end == " + c.getDataGroup());
		}

		assertTrue(endList.get(0).getDataGroup().equals("TrialDataProvenance"));
		assertTrue(endList.get(1).getDataGroup().equals("Study"));
		assertTrue(endList.get(2).getDataGroup().equals("GeneralImage"));
		assertTrue(endList.get(3).getDataGroup().equals("GeneralImage"));

	}

	public void testGetRelationTree() throws Exception {

		TableRelationships ro = new TableRelationships();

		List<Element> elementList = ro.getRelationTree();

		//better assertion possible?
		assertTrue(elementList.size()==6);
	}

	protected void setUp() throws Exception {
		URL url = TableRelationships.class.getClassLoader().getResource("relationship.xml");
		File file = new File(url.toURI());
		System.setProperty("jboss.server.data.dir", file.getParent());
	}
}
