/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/07 12:05:11  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.3  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.textsupport;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmElement;
import org.dcm4che.data.DcmObject;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.DcmParser;
import org.dcm4che.data.DcmParserFactory;
import org.dcm4che.data.FileFormat;
import org.dcm4che.data.SpecificCharacterSet;
import org.dcm4che.dict.DictionaryFactory;
import org.dcm4che.dict.TagDictionary;
import org.dcm4che.dict.Tags;
import org.dcm4che.dict.VRs;
import org.rsna.mircsite.util.FileObject;

import gov.nih.nci.nbia.dto.DicomTagDTO;

import org.apache.commons.io.IOUtils;

/**
 * This is an extension of the DicomObject in MIRC.  In mirc, it simply iterates
 * over all the dicom fields and writes out an HTML table.  This has been overriden so
 * that it returns DTOs so the UI can display it how it wants to.
 *
 *
 * @author NCIA Team
 */
public class NCIADicomTextObject{

    
    public static List<DicomTagDTO> getTagElements(File file) throws Exception {
        DcmParserFactory pFact = DcmParserFactory.getInstance();
        DcmObjectFactory oFact = DcmObjectFactory.getInstance();
        BufferedInputStream in = null;
        List<DicomTagDTO> allElements = new ArrayList<DicomTagDTO>();
        try {
            in = new BufferedInputStream(new FileInputStream(file));

            DcmParser parser = pFact.newDcmParser(in);
            FileFormat fileFormat = parser.detectFileFormat();

            if (fileFormat == null) {
                throw new IOException("Unrecognized file format: " + file);
            }

            Dataset dataset = oFact.newDataset();
            parser.setDcmHandler(dataset.getDcmHandler());
            //Parse the file, but don't get the pixels in order to save heap space
            parser.parseDcmFile(fileFormat, Tags.PixelData);
            //See if this is a real image.
            boolean isImage = (parser.getReadTag() == Tags.PixelData);
            //Get the charset in case we need it for manifest processing.

            in.close();

            SpecificCharacterSet cs = dataset.getSpecificCharacterSet(); 
            allElements.addAll(walkDataset(dataset.getFileMetaInfo(), cs,""));
            allElements.addAll(walkDataset(dataset, cs,""));
            parser=null;
         
        } 
        catch (Exception exception) {
            if (in != null) {
                in.close();
            }

            throw exception;
        }
        return allElements;
    }


    /**
     * Walks over all the dicom tags and pulls out the values.
     *
     * @param dataset
     * @param cs
     */
    private static List<DicomTagDTO> walkDataset(DcmObject dataset, SpecificCharacterSet cs, String prefix) {
        List<DicomTagDTO> tagList = new ArrayList<DicomTagDTO>();
        //int maxLength = 80;
        DcmElement el;
        String tagString;
        String tagName;
        String vrString;
        String valueString;
        DictionaryFactory dFact = DictionaryFactory.getInstance();
        TagDictionary tagDictionary = dFact.getDefaultTagDictionary();
        //String valueLength;
        int vr;

        if (dataset == null) {
            return null;
        }

        for (Iterator it = dataset.iterator(); it.hasNext();) { 
            el = (DcmElement) it.next();
            int tag = el.tag();
            tagString = checkForNull(Tags.toString(tag));
            try {
                tagName = checkForNull(tagDictionary.lookup(tag).name);
            } catch (Exception e) {
                tagName = "";
            }
            vr = el.vr();
            vrString = VRs.toString(vr);
            if (vrString.equals("")) {
                vrString = "[" + Integer.toHexString(vr) + "]";
             }
             el.vm();
             valueString = getElementValueString(cs, el);
             DicomTagDTO dtoTag = new DicomTagDTO(prefix+tagString, tagName, valueString);
             tagList.add(dtoTag);
             if (vrString.toLowerCase().startsWith("sq")) {//It's a sequence; get the tags
            	DicomTagDTO enddtoTag = new DicomTagDTO(prefix+tagString, "End "+tagName, valueString); //end tag
            	int i = 0;
				Dataset ds;
				while ((ds=el.getItem(i++)) != null) {
					tagList.addAll(walkDataset(ds, cs, prefix+">"));
				}
				tagList.add(enddtoTag); // add end tag
			}
        }

        return tagList;
    }

    //Handle null element values (e.g. missing elements).
    private static String checkForNull(String s) {
        if (s != null) {
            return s;
        }

        return "null";
    }

    //Make a displayable text value for an element, handling
    //cases where the element is multivalued and where the element value
    //is too long to be reasonably displayed.
    private static String getElementValueString(SpecificCharacterSet cs, DcmElement el) {
        int tag = el.tag();

        if ((tag & 0xffff0000) >= 0x60000000) {
            return "...";
        }

        String valueString;
        String[] s;

        try {
            s = el.getStrings(cs);
        } catch (Exception e) {
            s = null;
        }

        if (s == null) {
            valueString = null;
        } else {
            valueString = "";

            for (int i = 0; i < s.length; i++) {
                valueString += s[i];

                if (i != (s.length - 1)) {
                    valueString += "\\";
                }
            }
        }

        return valueString;
    }
}
