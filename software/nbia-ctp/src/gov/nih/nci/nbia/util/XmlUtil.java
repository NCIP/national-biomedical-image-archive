/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: XmlUtil.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
* Revision 1.3  2006/09/28 19:29:00  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Encapsulates static methods for working with XML objects.
 */
public class XmlUtil extends org.rsna.mircsite.util.XmlUtil {
    /*
     * isElementNodeExist is to test if a given node name has existed in current document
     * It traverses the whole tree and return true if it finds the given node name.
     * It returns false if it cannot fine the given node name
     *
     * Note: the given name node could locate at anywhere in the tree.
     *
     * @para        Node        -        root starting node
     *                         String        -        a given node name
     * @return        boolean        -        a boolean value indicating found or not.
     */
    public static boolean isElementNodeExist(Node root, String nodeString) {
        NodeList nlist = root.getChildNodes();

        for (int i = 0; i < nlist.getLength(); i++) {
            if (nlist.item(i) instanceof Element) {
                if (nlist.item(i).getNodeName().equalsIgnoreCase(nodeString)) {
                    return true;
                }

                if (nlist.item(i).hasChildNodes() &&
                    isElementNodeExist(nlist.item(i), nodeString)) {
                    return true;                  
                }
            }
        }

        return false;
    }

    /*
     * getElementNode is to get the node for a given node name
     * It traverses the document and returns a Element if it finds the given node name.
     * It returns null if not found
     *
     * Note: it returns the first one if it can find for a given node name.
     *
     * @para        Node        -        root starting node
     *                         String        -        a given node name
     * @return        Element        -        a Element for a given node name or null
     */
    public static Element getElementNode(Node root, String nodeString) {
        NodeList nlist = root.getChildNodes();

        for (int i = 0; i < nlist.getLength(); i++) {
            if (nlist.item(i) instanceof Element) {
                if (nlist.item(i).getNodeName().equalsIgnoreCase(nodeString)) {
                    return (Element) nlist.item(i);
                }

                if (nlist.item(i).hasChildNodes()) {
                    Element retNode = getElementNode(nlist.item(i), nodeString);

                    if (retNode != null) {
                        return retNode;
                    }
                }
            }
        }

        return null;
    }

    /*
     * getElementTextNodeValue is to get the value for a given node name.
     * It returns the value for the first TEXTNODE if it finds the given node name.
     *
     * @para        Node        -        root node
     *                         String        -        a given node name
     * @return        String        -        the value for the first TEXTNODE
     */
    public static String getElementTextNodeValue(Node root, String nodeString) {
        Element elem = getElementNode(root, nodeString);        
        if(elem == null) {
        	return null;
        }
        NodeList nlist = elem.getChildNodes();        

        if(nlist == null) {
            return null;
        }
        for (int i = 0; i < nlist.getLength(); i++) {
            if (nlist.item(i).getNodeType() == Node.TEXT_NODE) {
                return nlist.item(i).getNodeValue();
            }
        }

        return null;
    }
}
