/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: FileZipperFactory.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.4  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.gridzip;



/**
 * Gets an instance of a file zipper according to settings in the config file
 *
 *
 */
public class FileZipperFactory {
	
    /**
     * Determines the type of zipper to use and returns it
     * 
     * @throws Exception
     */
    public static synchronized AbstractFileZipper getInstance()
        throws Exception {
        AbstractFileZipper zipper;
            zipper = new ZipFiles();

        return zipper;
    }
}
