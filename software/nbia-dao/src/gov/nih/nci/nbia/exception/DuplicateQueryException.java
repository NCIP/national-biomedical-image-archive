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
* Revision 1.4  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.exception;

public class DuplicateQueryException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String errorMessage;

    /**
     *
     */
    public DuplicateQueryException() {
        super();
        errorMessage = "Query Already Exists";
    }

    /**
     * @param message
     */
    public DuplicateQueryException(String message) {
        super(message);
        errorMessage = message;
    }

    /**
     * @param message
     * @param cause
     */
    public DuplicateQueryException(String message, Throwable cause) {
        super(message, cause);
        errorMessage = message;
    }

    /**
     * @param cause
     */
    public DuplicateQueryException(Throwable cause) {
        super(cause);
    }

    /**
     * @return Returns the errorMessage.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
