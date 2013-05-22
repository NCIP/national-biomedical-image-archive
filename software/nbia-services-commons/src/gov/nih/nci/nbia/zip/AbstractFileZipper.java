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
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.5  2006/10/23 21:31:37  dietrich
* Defect 138 - Changed to make SOP instance UID the file name when images are downloaded
*
* Revision 1.4  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.zip;

import java.util.List;


public abstract class AbstractFileZipper {
    public abstract void startNewFile(String outputFileName, int sequenceNumber)
        throws Exception;

    public abstract void closeFile() throws Exception;

    public abstract void zip(String directory, String filePath, String fileName)
        throws Exception;

    public abstract long getFileSize();

    public abstract List<String> getListOfZipFiles(); 
}
