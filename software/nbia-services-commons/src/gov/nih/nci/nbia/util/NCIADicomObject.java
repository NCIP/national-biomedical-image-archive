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
package gov.nih.nci.nbia.util;

import gov.nih.nci.ncia.dto.DicomTagDTO;

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
import org.apache.commons.io.IOUtils;

/**
 * This is an extension of the DicomObject in MIRC.  In mirc, it simply iterates
 * over all the dicom fields and writes out an HTML table.  This has been overriden so
 * that it returns DTOs so the UI can display it how it wants to.
 *
 *
 * @author NCIA Team
 */
public class NCIADicomObject extends FileObject {
    private static final DcmParserFactory pFact = DcmParserFactory.getInstance();
    private static final DcmObjectFactory oFact = DcmObjectFactory.getInstance();
    private static final DictionaryFactory dFact = DictionaryFactory.getInstance();
    private static final TagDictionary tagDictionary = dFact.getDefaultTagDictionary();
    private Dataset dataset = null;
    private SpecificCharacterSet scharset = null;
    private String nullValue = "null";
    
    public static boolean isDicom(InputStream inputStream){
		DcmParser parser = pFact.newDcmParser(inputStream);
		 
		FileFormat fileFormat = null;
		try {
			fileFormat = parser.detectFileFormat();
		}
		catch(Throwable t) {
			fileFormat = null;
		}
		if (fileFormat == null){
			return false;
		}else{
			return true;
		}
		
    }
    
    public static String loadSOPInstanceUID(File dicomFile) throws Exception {
    	InputStream inputStream = new BufferedInputStream(new FileInputStream(dicomFile));
			
    	try {
			DcmParser parser = pFact.newDcmParser(inputStream);
				 
			FileFormat fileFormat = null;
			try {
				fileFormat = parser.detectFileFormat();
			}
			catch(Throwable t) {
				fileFormat = null;
			}
	        if (fileFormat == null) {
	        	return null;
	        }
		 
		    Dataset dataset = oFact.newDataset();
		    parser.setDcmHandler(dataset.getDcmHandler());
		    parser.parseDcmFile(fileFormat, Tags.PixelData);
		    
		    return dataset.getString(Tags.SOPInstanceUID);
    	}
    	finally {
    		IOUtils.closeQuietly(inputStream);
    	}
    }
    
    
    public NCIADicomObject(File file) throws Exception {
        super(file);

        BufferedInputStream in = null;

        try {
            in = new BufferedInputStream(new FileInputStream(file));

            DcmParser parser = pFact.newDcmParser(in);
            FileFormat fileFormat = parser.detectFileFormat();

            if (fileFormat == null) {
                throw new IOException("Unrecognized file format: " + file);
            }

            dataset = oFact.newDataset();
            parser.setDcmHandler(dataset.getDcmHandler());
            //Parse the file, but don't get the pixels in order to save heap space
            parser.parseDcmFile(fileFormat, Tags.PixelData);
            //See if this is a real image.
            boolean isImage = (parser.getReadTag() == Tags.PixelData);
            System.out.println("isImage:"+isImage);
            //Get the charset in case we need it for manifest processing.
            scharset = dataset.getSpecificCharacterSet(); 
            System.out.println("==============SpecificCharacterSet: ");
            System.out.println(scharset );
            System.out.println(" ==========================");
            in.close();
        } 
        catch (Exception exception) {
            if (in != null) {
                in.close();
            }

            throw exception;
        }
    }

    /**
     *
     */
    public List<DicomTagDTO> getTagElements() {
        List<DicomTagDTO> allElements = new ArrayList<DicomTagDTO>();
        SpecificCharacterSet cs = dataset.getSpecificCharacterSet(); 
        allElements.addAll(walkDataset(dataset.getFileMetaInfo(), cs,""));
        allElements.addAll(walkDataset(dataset, cs,""));

        return allElements;
    }

    /**
     * Walks over all the dicom tags and pulls out the values.
     *
     * @param dataset
     * @param cs
     */
    private List<DicomTagDTO> walkDataset(DcmObject dataset, SpecificCharacterSet cs, String prefix) {
        List<DicomTagDTO> tagList = new ArrayList<DicomTagDTO>();
        //int maxLength = 80;
        DcmElement el;
        String tagString;
        String tagName;
        String vrString;
        String valueString;
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
    private String checkForNull(String s) {
        if (s != null) {
            return s;
        }

        return nullValue;
    }

    //Make a displayable text value for an element, handling
    //cases where the element is multivalued and where the element value
    //is too long to be reasonably displayed.
    private String getElementValueString(SpecificCharacterSet cs, DcmElement el) {
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
