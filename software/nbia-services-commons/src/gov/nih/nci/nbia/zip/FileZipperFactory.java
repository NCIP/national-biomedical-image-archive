/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.zip;

/**
 * Gets an instance of a file zipper 
 */
public class FileZipperFactory {

    /**
     * Determines the type of zipper to use and returns it
     *
     * @throws Exception
     */
    public static AbstractFileZipper getInstance() {
        return new ZipFiles();
    }
}
