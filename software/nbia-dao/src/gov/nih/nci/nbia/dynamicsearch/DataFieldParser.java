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
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
/**
 * This parser will parse xml file to retrieve searchable data fields for each target, 
 * such as Trial Data Provenance, Patient, Study, Series, and Image (including
 * CT Image). 
 * @author zhoujim
 *
 */
public class DataFieldParser {
	private static Logger logger = Logger.getLogger(DataFieldParser.class);
	private static DataGroup dg;
	/**
	 * This method provides a dataGroup list for user to select data source on the GUI
	 */
	public static DataGroup getDataGroup()
	{ 
		try
		{
			if (dg == null)
			{
				XStream xstream = new XStream(new DomDriver());
				xstream.alias("dataGroup", DataGroup.class);
				xstream.alias("dataSource", DataSource.class);
				xstream.alias("sourceItem", SourceItem.class);
				xstream.addImplicitCollection(DataGroup.class,"dataSource", DataSource.class);
				xstream.addImplicitCollection(DataSource.class,"sourceItem", SourceItem.class);
		    	 ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		    	 InputStream input = classLoader.getResourceAsStream("DataSourceItem.xml");
				dg = (DataGroup)xstream.fromXML(new InputStreamReader(input));
				
				input.close();
			}
		}
		catch(Exception e)
		{
			logger.error("Cannot open DataSourceItem file....");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return dg;
	}

	/**
	 * This method provides a HashMap for users to select data item for each data
	 * group.
	 */
	public static Map<String, List<SourceItem>> getSourceItem()
	{
		Map<String, List<SourceItem>> sourceItems = new HashMap<String, List<SourceItem>>();
		List<SourceItem> sortedItemList = null;
		
		DynamicSearchSourceItemComparator dssic = new DynamicSearchSourceItemComparator();
		
		for (DataSource dataSource : getDataGroup().getDataSource())
		{
			sortedItemList = new ArrayList<SourceItem>();
			for (SourceItem item : dataSource.getSourceItem())
			{
				sortedItemList.add(item);
			}
			Collections.sort(sortedItemList, dssic);
			sourceItems.put(dataSource.getSourceName(), sortedItemList);
		}
		return sourceItems;
	}
	
	/**
	 * This method provides HashMap for users to select each data source item
	 * type.
	 * @return ItemTypeTable
	 */
	public static Map<String, String> getItemType()
	{
		Map<String, String> itemTypeTable = new HashMap<String, String>();
		for (DataSource  ds : getDataGroup().getDataSource())
		{
			for (SourceItem si : ds.getSourceItem())
			{
				itemTypeTable.put(si.getItemName().trim(), si.getItemType().trim());
			}
		}
		return itemTypeTable;
	}
	
	public static Map<String, String> getItemActualSource()
	{
		Map<String, String> itemActualSource = new HashMap<String, String>();
		for (DataSource  ds : getDataGroup().getDataSource())
		{
			for (SourceItem si : ds.getSourceItem())
			{
				itemActualSource.put(si.getItemName().trim(), si.getActualSource()==null? "" : si.getActualSource());
			}
		}
		return itemActualSource;
	}
	
	public static Map<String, SourceItem>getDataFieldSourceItem()
	{
		Map<String, SourceItem> dataFieldSourceItem = new HashMap<String, SourceItem>();
		for (DataSource  ds : getDataGroup().getDataSource())
		{
			for (SourceItem si : ds.getSourceItem())
			{
				dataFieldSourceItem.put(si.getItemName().trim(), si);
			}
		}
		return dataFieldSourceItem;
		
	}
	
	public static Map<String, String> getItemLabel()
	{
		Map<String, String> itemLabelTable = new HashMap<String, String>();
		for (DataSource  ds : getDataGroup().getDataSource())
		{
			for (SourceItem si : ds.getSourceItem())
			{
				itemLabelTable.put(si.getItemName().trim(), si.getItemLabel().trim());
			}
		}
		
		return itemLabelTable;
	}
	
	public static Map<String, String> getPackageNames()
	{
		Map<String, String> packageNames = new HashMap<String, String>();
		for (DataSource ds : getDataGroup().getDataSource())
		{
			packageNames.put(ds.getSourceName(), ds.getSourcePackageName());
		}
		
		return packageNames;
	}
}
