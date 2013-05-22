/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

import gov.nih.nci.nbia.xmlobject.DataGroup;
import gov.nih.nci.nbia.xmlobject.DataSource;
import gov.nih.nci.nbia.xmlobject.SourceItem;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

public class DataFieldParserTestCase extends TestCase {

	public void testGetDataGroup() throws Exception {

		DataGroup dg = DataFieldParser.getDataGroup();

		//test for getting all Data Source
		List<DataSource> dataSourceList = dg.getDataSource();
		for (DataSource ds : dataSourceList)
		{
			System.out.println(".." + ds.getSourceName());
			System.out.println(".." + ds.getSourcePackageName());
		}
		System.out.println("================================");

		assertTrue(dataSourceList.get(0).getSourceName().equals("TrialDataProvenance"));
		assertTrue(dataSourceList.get(1).getSourceName().equals("Patient"));
		assertTrue(dataSourceList.get(2).getSourceName().equals("Study"));
		assertTrue(dataSourceList.get(3).getSourceName().equals("GeneralSeries"));
		assertTrue(dataSourceList.get(4).getSourceName().equals("GeneralImage"));
	}

	public void testGetSourceItem() throws Exception {

		//test for getting data source items
		Map<String, List<SourceItem>> sources = DataFieldParser.getSourceItem();
		for (String sourceName : sources.keySet())
		{
			System.out.println(sourceName + ":");
			List<SourceItem> items = (List<SourceItem>)sources.get(sourceName);
			for (SourceItem item : items)
			{
				System.out.println("...." +item.getItemLabel());
			}

			if(sourceName.equals("TrialDataProvenance")) {
				assertTrue(items.get(0).getItemName().equals("project"));
			}
			if(sourceName.equals("Patient")) {
				assertTrue(items.get(0).getItemName().equals("ethnicGroup"));
			}
			if(sourceName.equals("Study")) {
				assertTrue(items.get(0).getItemName().equals("admittingDiagnosesCodeSeq"));
			}
			if(sourceName.equals("GeneralSeries")) {
				assertTrue(items.get(0).getItemName().equals("bodyPartExamined"));
			}
			if(sourceName.equals("GeneralImage")) {
				assertTrue(items.get(0).getItemName().equals("acquisitionDate"));
			}
		}
	}

	public void testGetItemType() throws Exception {

		//test for getting data source items
		Map<String, String> items = DataFieldParser.getItemType();
		for (String itemName : items.keySet())
		{
			//just pick few
			if(itemName.equals("KVP")) {
				assertTrue(items.get(itemName).equals("java.lang.Double"));
			}
			if(itemName.equals("gantryDetectorTilt")) {
				assertTrue(items.get(itemName).equals("java.lang.Double"));
			}
			if(itemName.equals("exposureInMicroAs")) {
				assertTrue(items.get(itemName).equals("java.lang.Integer"));
			}
			if(itemName.equals("anatomicRegionSeq")) {
				assertTrue(items.get(itemName).equals("java.lang.String"));
			}

		}
	}

	public void testGetDataFieldSourceItem() throws Exception {

		//test for getting data source items
		Map<String, SourceItem> items = DataFieldParser.getDataFieldSourceItem();
		for (String itemName : items.keySet())
		{
			//just pick few
			if(itemName.equals("SOPClassUID")) {
				SourceItem si = items.get(itemName);
				assertTrue(si.getItemType().equals("java.lang.String"));
				assertTrue(si.getItemName().equals("SOPClassUID"));
				assertTrue(si.getItemLabel().equals("SOP Class UID"));
				assertNull(si.getActualSource());
				assertNull(si.getItemPrimaryValue());
			}
		}
		assertNull(items.get("fake_key"));
	}

	public void testGetItemLabel() throws Exception {

		//test for getting data source items
		Map<String, String> items = DataFieldParser.getItemLabel();
		for (String itemName : items.keySet())
		{
			//just pick few
			if(itemName.equals("contentDate")) {
				assertTrue(items.get(itemName).equals("Content Date"));
			}
			if(itemName.equals("SOPClassUID")) {
				assertTrue(items.get(itemName).equals("SOP Class UID"));
			}
		}
	}

	public void testGetItemActualSource() throws Exception {

		//test for getting data source items
		Map<String, String> items = DataFieldParser.getItemActualSource();
		for (String itemName : items.keySet())
		{
			//just pick few
			if(itemName.equals("KVP")) {
				assertTrue(items.get(itemName).equals("CTImage"));
			}
			if(itemName.equals("gantryDetectorTilt")) {
				assertTrue(items.get(itemName).equals("CTImage"));
			}
			if(itemName.equals("imageComments")) {
				assertTrue(items.get(itemName).equals(""));
			}
		}
	}

	public void testGetPackageNames() throws Exception {

		//test for getting data source items
		Map<String, String> items = DataFieldParser.getPackageNames();
		for (String packageName : items.values())
		{
			assertEquals(packageName, "gov.nih.nci.nbia.internaldomain");
		}
	}

	protected void setUp() throws Exception {
		URL url = TableRelationships.class.getClassLoader().getResource("relationship.xml");
		File file = new File(url.toURI());
		System.setProperty("jboss.server.data.dir", file.getParent());
	}
}
