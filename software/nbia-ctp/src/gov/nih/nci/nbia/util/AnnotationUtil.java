/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AnnotationUtil {
	public static String getSeriesInstanceUID(Document document) {
        if (document == null) {
            return "";
        }

        Element root = document.getDocumentElement();
        String rootName = root.getTagName();

        String seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                rootName + "@series-uid");

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                rootName + "@SeriesInstanceUID");

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                rootName + "@SeriesInstanceUid");

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                rootName + "/series-uid");

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                rootName + "/SeriesInstanceUID");

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        Node child = root.getFirstChild();

        while ((child != null) && (child.getNodeType() != Node.ELEMENT_NODE)) {
            child = child.getNextSibling();
        }

        if (child == null) {
            return "";
        }

        String path = rootName + "/" + child.getNodeName() + "/series-uid";
        seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                path);

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        path = rootName + "/" + child.getNodeName() + "/SeriesInstanceUid";
        seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                path);

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        path = rootName + "/" + child.getNodeName() + "/SeriesInstanceUID";

        return XmlUtil.getValueViaPath(document.getDocumentElement(), path);
    }
	
	/**
	 * Find StudyInstance UID in the given document
	 * @param document
	 */
	public static String getStudyInstanceUID(Document document) {
        if (document == null) {
            return "";
        }

        Element root = document.getDocumentElement();
        String rootName = root.getTagName();

        String seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                rootName + "@study-uid");

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                rootName + "@StudyInstanceUID");

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                rootName + "@StudyInstanceUid");

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                rootName + "/study-uid");

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                rootName + "/StudyInstanceUID");

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        Node child = root.getFirstChild();

        while ((child != null) && (child.getNodeType() != Node.ELEMENT_NODE)) {
            child = child.getNextSibling();
        }

        if (child == null) {
            return "";
        }

        String path = rootName + "/" + child.getNodeName() + "/study-uid";
        seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                path);

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        path = rootName + "/" + child.getNodeName() + "/StudyInstanceUid";
        seriesInstanceUid = XmlUtil.getValueViaPath(document.getDocumentElement(),
                path);

        if (!seriesInstanceUid.equals("")) {
            return seriesInstanceUid;
        }

        path = rootName + "/" + child.getNodeName() + "/StudyInstanceUID";

        return XmlUtil.getValueViaPath(document.getDocumentElement(), path);
    }
}
