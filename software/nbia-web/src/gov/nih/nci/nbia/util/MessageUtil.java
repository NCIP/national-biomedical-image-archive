/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: MessageUtil.java 5106 2008-06-20 15:54:45Z kascice $
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/09/27 23:30:39  bauerd
* This is the checked in seperation of dependent classes betweeen the grid and the commons module...
*
* Revision 1.3  2007/08/21 19:43:02  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/07 12:05:11  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.6  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.util;

import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;


/**
 * This will provide utility classes in order to easily lookup JSF messages.
 * It is taken from Core JSF book, Geary & Horstmann
 *
 * @author shinohaa
 *
 */
public class MessageUtil extends ResourceBundleUtil {

    public static String getString(String resourceId) {
        return getString(nciMessageBundle, resourceId, null);
    }

    public static String getString(String resourceId, Object[] params) {
        return getString(nciMessageBundle,
        		         resourceId,
        		         new Locale("en"),
        		         getClassLoader(),
        		         params);
    }

    public static FacesMessage getMessage(String resourceId) {
        return getMessage(resourceId, EMPTY_ARRAY);
    }

    public static FacesMessage getMessage(String resourceId, Object[] params) {
        FacesContext context = FacesContext.getCurrentInstance();
        Locale locale = getLocale(context);
        String summary = getString(nciMessageBundle, resourceId, params, locale);

        if (summary == null) {
            summary = resourceId;
        }

        /*String detail = getString(appBundle, resourceId + "_detail", locale,
                loader, params);*/
        String detail = getString(nciMessageBundle, resourceId, params, locale);

        return new FacesMessage(summary, detail);
    }



    public static void addErrorMessage(String fieldId, String resourceId,
        Object[] params) {
        addMessage(fieldId, FacesMessage.SEVERITY_ERROR, resourceId, params);
    }

    public static void addErrorMessage(String fieldId, String resourceId) {
        addErrorMessage(fieldId, resourceId, EMPTY_ARRAY);
    }

    public static void addInfoMessage(String fieldId, String resourceId,
        Object[] params) {
        addMessage(fieldId, FacesMessage.SEVERITY_INFO, resourceId, params);
    }

    public static void addInfoMessage(String fieldId, String resourceId) {
        addInfoMessage(fieldId, resourceId, EMPTY_ARRAY);
    }


    ////////////////////////////////PRIVATE/////////////////////////////////////////////

    private static final Object[] EMPTY_ARRAY = new Object[] {  };
    private static final String nciMessageBundle = "ncia_messages";

    private static void addMessage(String fieldId,
    		                       FacesMessage.Severity severity,
    		                       String resourceId,
    		                       Object[] params) {
        FacesMessage msg = getMessage(resourceId, params);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(fieldId, msg);
    }


    private static Locale getLocale(FacesContext context) {
        Locale locale = null;
        UIViewRoot viewRoot = context.getViewRoot();

        if (viewRoot != null) {
            locale = viewRoot.getLocale();
        }

        if (locale == null) {
            locale = Locale.getDefault();
        }

        return locale;
    }
}
