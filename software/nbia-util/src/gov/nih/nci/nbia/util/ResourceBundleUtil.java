/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


public class ResourceBundleUtil {


    public static String getString(String bundle, String resourceId, Object[] params, Locale locale) {
        return getString(bundle,
        		         resourceId, 
        		         locale,
        		         getClassLoader(), 
        		         params);
    }
    
    public static String getString(String bundle, String resourceId, Object[] params) {
        return getString(bundle,
        		         resourceId, 
        		         new Locale("en"),
        		         getClassLoader(), 
        		         params);
    }


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

    public static String getString(String bundleName,
    		                       String resourceId,
                                   Locale locale, 
                                   ClassLoader loader, 
                                   Object[] params) {
        String resource = null;
        ResourceBundle bundle;

        bundle = ResourceBundle.getBundle(bundleName, locale, loader);

        if (bundle != null) {
            try {
                resource = bundle.getString(resourceId);
            } catch (MissingResourceException mre) {
                // Not a problem, but will debug
                logger.debug("No error message.", mre);
            }
        }

        if (resource == null) {
            return null;
        }

        if (params == null) {
            return resource;
        }

        MessageFormat formatter = new MessageFormat(resource, locale);

        return formatter.format(params);
    }    
    ///////////////////////////////////////////PRIVATE/////////////////////////////////
    /**
     * Logger for the class.
     */
    private static Logger logger = Logger.getLogger(ResourceBundleUtil.class);

    private static final String nciMessageBundle = "ncia_commons_messages";


    
    protected static ClassLoader getClassLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }

        return loader;
    }

}
