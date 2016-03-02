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
/*
 * Created on Aug 17, 2005
 *
 *
 *
 */
package gov.nih.nci.nbia.exception;


/**
 * @author Prashant Shah - NCICB/SAIC
 *
 *
 *
 */
public class PatientNotFoundException extends Exception {
    private String errorMessage;

    /**
     *
     */
    public PatientNotFoundException() {
        super();
        errorMessage = "Cannot find Patient in Search Results List";
    }

    /**
     * @param message
     */
    public PatientNotFoundException(String message) {
        super(message);
        errorMessage = message;
    }

    /**
     * @param message
     * @param cause
     */
    public PatientNotFoundException(String message, Throwable cause) {
        super(message, cause);
        errorMessage = message;
    }

    /**
     * @param cause
     */
    public PatientNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * @return Returns the errorMessage.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
