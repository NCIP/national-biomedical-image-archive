/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: LIDCPreProcessUtil.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
* Revision 1.3  2007/10/15 13:54:10  lethai
* modified to work for both regular series and CT-CXR pair series
*
* Revision 1.2  2007/10/01 17:37:03  lethai
* corrected problem with processing CT-CXR pair
*
* Revision 1.1  2007/09/24 15:01:47  lethai
* new package for preprocessor
*
* Revision 1.9  2007/05/23 18:47:47  mccrimmons
* make the path in the main test external
*
* Revision 1.8  2007/05/21 19:37:59  mccrimmons
* changed tag name to CXRSeriesInstanceUid from CXRSeriesInstanceUID
*
* Revision 1.7  2007/05/21 15:07:20  mccrimmons
* added processing check for CXRSeriesInstanceUID as the instance UID tag name in the XML file. This request was made by John Freymann at my desk 5.18.2007
*
* Revision 1.6  2006/09/28 19:29:00  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.util.preprocessor;

import gov.nih.nci.ncia.util.DcmUtil;
import gov.nih.nci.ncia.util.XmlUtil;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dcm4che.data.Dataset;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class LIDCPreProcessUtil implements IPreProcessor {
    final static Logger logger = Logger.getLogger(LIDCPreProcessUtil.class);
    Hashtable<String, String> seriesStudyMap = new Hashtable<String, String>();
    ArrayList<File> unProcessedFilesQueue = new ArrayList<File>();
    private static final String SERIES_INSTANCE_UID_ELEMENT = "SeriesInstanceUID";
    
    public boolean isXMLFileQueueEmpty() {
        return this.unProcessedFilesQueue.isEmpty();
    }

    public ArrayList<File> getXmlFileQueue() {
        return this.unProcessedFilesQueue;
    }

    public String preprocess(String filename) {
        String logMessage = null;

        try {
            process(filename);

            if (!unProcessedFilesQueue.isEmpty()) {
                File[] tempFileList = unProcessedFilesQueue.toArray(new File[0]);

                for (int i = 0; i < tempFileList.length; i++) {
                    processXML(tempFileList[i]);
                }
            }

            logMessage = "<p>LIDCXMLPREPROCESS completes all files under the directory <p>" +
                filename;

            if (!unProcessedFilesQueue.isEmpty()) {
                logMessage += "<p>The following xml file(s) cannot find its studyInstanceUID:";
                logger.warn(
                    "The following xml file(s) cannot find its studyInstanceUID: \n");

                String tempFilename;
                Iterator<File> iter = unProcessedFilesQueue.iterator();

                while (iter.hasNext()) {
                    tempFilename = iter.next().getAbsolutePath();
                    logMessage += ("<p>" + tempFilename);
                    logger.warn(tempFilename + "\n");
                }
            }

            if (unProcessedFilesQueue != null) {
                unProcessedFilesQueue.clear();
            }
        } catch (Exception e) {
            logger.error("Exception in run in LIDCPreProcess: " + e.toString());
            logMessage += ("<p>Exception in run in LIDCPreProcess: " +
            e.toString());
            e.printStackTrace();
        }

        return logMessage;
    }

    public void process(String filename) throws Exception {
        String studyInstanceUID;
        String seriesInstanceUID;
        File tempFile = new File(filename);
        //System.out.println("filename: " + filename);

        if (tempFile.isDirectory()) {
            File[] filelist = tempFile.listFiles();

            for (int i = 0; i < filelist.length; i++) {
                process(filelist[i].getAbsolutePath());
            }
        }

        if (tempFile.getName().endsWith(".dcm")) {
            // dicom file:
            // get studyInstanceUID and seriesInstanceUID from it
            Dataset set = DcmUtil.parse(tempFile, 0x5FFFffff);
            int index = Integer.parseInt("0020000E", 16);
            seriesInstanceUID = set.getString(index);
            //System.out.println("process dicome file seriesInstanceUID: " + seriesInstanceUID);
            index = Integer.parseInt("0020000D", 16);
            studyInstanceUID = set.getString(index);
            //System.out.println("processs dicom file studyInstanceUID: "+ studyInstanceUID);

            // load seriesInstanceUID and studyInstanceUID into seriesStudyMap
            this.seriesStudyMap.put(seriesInstanceUID, studyInstanceUID);
        } else if (tempFile.getName().endsWith(".xml")) {
            // xml markup file:
            processXML(tempFile);
        }
    }

    public void processXML(File xmlFile) throws Exception {
        Document root = XmlUtil.getDocument(xmlFile);

        boolean processed=false;
        // check if studyInstanceUID is available
        List<String> series = getSeriesInstanceUIDFromXML(root);
        for(int i=0; i<series.size(); i++) {
            String seriesInstanceUID = series.get(i);
            String studyInstanceUID = this.seriesStudyMap.get(seriesInstanceUID);
            //System.out.println("processXML studyinstanceuid: " + studyInstanceUID);
            //System.out.println("processXML seriesInstanceUID: " + seriesInstanceUID);
    
            if (this.seriesStudyMap.containsKey(seriesInstanceUID)) {
                // if yes, recreate xml with studyInstanceUID
                String filepath=xmlFile.getAbsolutePath();
                int pos = filepath.lastIndexOf('.');
                String newfilename="";
                if(pos > 0) {
                    newfilename = filepath.substring(0, pos) + "_processed" +filepath.substring(pos);
                }
                //System.out.println("new file name: " + newfilename);
                //PrintWriter pw = new PrintWriter(newfilename);
                if (!XmlUtil.isElementNodeExist(root, "StudyInstanceUID")) {
                    String xmlString = createXmlFileWithStudyInstanceUID(studyInstanceUID,
                            root);
                    //System.out.println("xmlString: " + xmlString);
                    
                    PrintWriter pw = new PrintWriter(newfilename);
                    pw.print(xmlString);
                    pw.close();
                    processed=true;
                    
                }
                if (!XmlUtil.isElementNodeExist(root, SERIES_INSTANCE_UID_ELEMENT)) {
                    String xmlString = createXmlFileWithSeriesInstanceUID(seriesInstanceUID, root);
                    //System.out.println(xmlString);
                    PrintWriter pw = new PrintWriter(newfilename);
                    pw.print(xmlString);
                    pw.close();
                    processed=true;
                    
                }
                //pw.close();
                if (unProcessedFilesQueue.contains(xmlFile)) {
                    unProcessedFilesQueue.remove(xmlFile);
                }
            } else {
                // if not, put into queue
                if (!this.unProcessedFilesQueue.contains(xmlFile) && !processed) {
                    this.unProcessedFilesQueue.add(xmlFile);
                }
            }
        }
    }

    public String createXmlFileWithStudyInstanceUID(String studyInstanceUID,
        Document root) {
        //		Element elem = XmlUtil.getElementViaPath( root, "LidcReadMessage/ResponseHeader" );
        Element elem = XmlUtil.getElementNode(root, "ResponseHeader");
        Element studyElem = root.createElement("StudyInstanceUID");
        studyElem.appendChild(root.createTextNode(studyInstanceUID));
        elem.appendChild(studyElem);

        return XmlUtil.toString(root);
    }
    public String createXmlFileWithSeriesInstanceUID(String seriesInstanceUID,
        Document root) {
        //      Element elem = XmlUtil.getElementViaPath( root, "LidcReadMessage/ResponseHeader" );
        Element elem = XmlUtil.getElementNode(root, "ResponseHeader");
        Element seriesElem = root.createElement(SERIES_INSTANCE_UID_ELEMENT);
        seriesElem.appendChild(root.createTextNode(seriesInstanceUID));
        elem.appendChild(seriesElem);

        return XmlUtil.toString(root);
    }

    /*
     * getSeriesInstanceUIDFromXML method is to fetch the seriesInstanceUID from xml markup file
     * @param        Node        -        node in xml markup file
     * @return        String        -        seriesInstanceUID
     */
    private List<String> getSeriesInstanceUIDFromXML(Node node)
        throws Exception {
        //String seriesInstanceUID = XmlUtil.getValueViaPath( node, "LidcReadMessage/ResponseHeader/SeriesInstanceUID" );
    	
    	// Modified by Scott M 5.21.2007 based on adhoc request from John Freymann for CXRSeriesInstanceUID:
    	String seriesInstanceUID = null;
        List<String> series = new ArrayList<String>();
        
       String cxr= XmlUtil.getElementTextNodeValue(node, "CXRSeriesInstanceUid");
    	//System.out.println("cxr = " + cxr);
    	if(cxr != null){
    		seriesInstanceUID = XmlUtil.getElementTextNodeValue(node,
            "CXRSeriesInstanceUid");
            series.add(seriesInstanceUID);
            
    	}
        String ct = XmlUtil.getElementTextNodeValue(node, "CTSeriesInstanceUid");
       // System.out.println("ct = " + ct);
    	if(ct != null){    		
    		seriesInstanceUID = XmlUtil.getElementTextNodeValue(node,
            "CTSeriesInstanceUID");
            series.add(seriesInstanceUID);
    	}   
        String reg =XmlUtil.getElementTextNodeValue(node, SERIES_INSTANCE_UID_ELEMENT);
        //System.out.println("reg = " + reg);
        if(reg != null) {
            seriesInstanceUID = XmlUtil.getElementTextNodeValue(node,
            		                                            SERIES_INSTANCE_UID_ELEMENT );
            series.add(seriesInstanceUID);
        }

        if (series.size() == 0) {
            throw new Exception("No <SeriesInstanceUID> is found ");
        }

        return series;
    }
    
    public static void main(String[] args){
    	System.out.println("Begin test");
    	LIDCPreProcessUtil lppu = new LIDCPreProcessUtil();
    	lppu.preprocess(args[0]);
    	System.out.println("End test");    	    	
    }
}
