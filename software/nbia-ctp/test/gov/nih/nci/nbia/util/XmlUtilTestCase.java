/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.*;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.*;



public class XmlUtilTestCase extends TestCase {

	public void testIsElementNodeExist() throws Exception {
		Element rootElement = constructDocument();
		assertTrue(XmlUtil.isElementNodeExist(rootElement, "c1"));
		assertTrue(XmlUtil.isElementNodeExist(rootElement, "c2"));
		assertTrue(XmlUtil.isElementNodeExist(rootElement, "c3"));
		assertTrue(XmlUtil.isElementNodeExist(rootElement, "gc1_1"));
		assertTrue(XmlUtil.isElementNodeExist(rootElement, "gc1_2"));
		assertTrue(XmlUtil.isElementNodeExist(rootElement, "gc2_1"));

		assertFalse(XmlUtil.isElementNodeExist(rootElement, "notfoudn"));
	}

	public void testGetElementNode() throws Exception {
		Element rootElement = constructDocument();
		
		Element c1Element = XmlUtil.getElementNode(rootElement, "c1");
		assertTrue(c1Element.getNodeName().equals("c1"));
		
		Element c2Element = XmlUtil.getElementNode(rootElement, "c2");
		assertTrue(c2Element.getNodeName().equals("c2"));		
		
		Element c3Element = XmlUtil.getElementNode(rootElement, "c3");
		assertTrue(c3Element.getNodeName().equals("c3"));			

		Element gc1Element = XmlUtil.getElementNode(rootElement, "gc1_1");
		assertTrue(gc1Element.getNodeName().equals("gc1_1"));
		
		Element gc2Element = XmlUtil.getElementNode(rootElement, "gc1_2");
		assertTrue(gc2Element.getNodeName().equals("gc1_2"));		
		
		Element gc3Element = XmlUtil.getElementNode(rootElement, "gc2_1");
		assertTrue(gc3Element.getNodeName().equals("gc2_1"));			

		assertNull(XmlUtil.getElementNode(rootElement, "notfoudn"));
	}	


	public void testGetElementTextNodeValue() throws Exception  {
		Element rootElement = constructDocument();
		assertTrue(XmlUtil.getElementTextNodeValue(rootElement, "gc2_1").equals("uncle_freddie"));	
		
	}

	private static Element constructDocument() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		Element rootElement = doc.createElement("root");
		doc.appendChild(rootElement);	
		Element c1Element = doc.createElement("c1");
		Element c2Element = doc.createElement("c2");
		Element c3Element = doc.createElement("c3");
		Element gc1Element = doc.createElement("gc1_1");
		Element gc2Element = doc.createElement("gc1_2");
		Text textNode = doc.createTextNode("uncle_freddie");
		Element gc3Element = doc.createElement("gc2_1");	
		
		rootElement.appendChild(c1Element);
		rootElement.appendChild(c2Element);
		rootElement.appendChild(c3Element);

		c1Element.appendChild(gc1Element);
		c1Element.appendChild(gc2Element);
		c2Element.appendChild(gc3Element);

		gc3Element.appendChild(textNode);
		
		return rootElement;
	}
}
