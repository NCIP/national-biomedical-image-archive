/*
 * DICOM Objects filter
 */

package gov.nih.nci.nbia.dicomapi;

import java.util.Iterator;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
//import pt.ua.dicoogle.DebugManager;

/**
 *
 * @author Joaoffr  <joaoffr@ua.pt>
 * @author DavidP   <davidp@ua.pt>
 *
 * @version $Revision: 002 $ $Date: 2008-11-22 14:25:00  $
 * @since Nov 21, 2008
 *
 */
public class Filter {

     public static boolean applyFilter(DicomObject keyObj, DicomObject sourceObj)
     {
        DicomObject kObj = keyObj;
        DicomObject sObj = sourceObj;

        Iterator<DicomElement> itKeys = kObj.datasetIterator();

        DicomElement dcmElmt;
        int tag;

        //DebugManager.getInstance().debug("Appling a filter to DCMObj");

        for (; itKeys.hasNext();  ) {
            dcmElmt = (DicomElement) itKeys.next();

            if (dcmElmt.isEmpty()) {
                continue;
            }

            if (dcmElmt.hasDicomObjects()) {
                if (dcmElmt.getDicomObject() == null) continue;

                if (!applyFilter(dcmElmt.getDicomObject(), sObj)) {
                    return false;
                }
                continue;
            }

             tag = dcmElmt.tag();

             if ( sObj.get(tag)!= null &&
                  kObj.get(tag)!= null &&
                  !(byte2string(sObj.get(tag).getBytes()).trim().equals(byte2string(dcmElmt.getBytes()).trim()))
                 ) {
                    //System.out.println("-->" + byte2string(sObj.get(tag).getBytes()) );
                    return false;
             }
        }

        return true;
    }


     private static String byte2string(byte[] in) {
        String out = "";

        for (int i = 0; i < in.length; i++) {
            out += (char)in[i];
        }

        return out;
    }
}
