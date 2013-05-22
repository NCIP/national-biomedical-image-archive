/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;


public abstract class PropertyLoader
{
    /**
     * Looks up a resource named 'name' in the classpath. The resource must map
     * to a file with .properties extention. The name is assumed to be absolute
     * and can use either "/" or "." for package segment separation with an
     * optional leading "/" and optional ".properties" suffix. Thus, the
     * following names refer to the same resource:
     * <pre>
     * some.pkg.Resource
     * some.pkg.Resource.properties
     * some/pkg/Resource
     * some/pkg/Resource.properties
     * /some/pkg/Resource
     * /some/pkg/Resource.properties
     * </pre>
     *
     * @param theName classpath resource name [may not be null]
     * @param loader classloader through which to load the resource [null
     * is equivalent to the application loader]
     *
     * @return resource converted to java.util.Properties [may be null if the
     * resource was not found and THROW_ON_LOAD_FAILURE is false]
     * @throws IllegalArgumentException if the resource was not found and
     * THROW_ON_LOAD_FAILURE is true
     */
    public static Properties loadProperties (String theName, ClassLoader loader)
    {
    	String name = preprocessNameArgument(theName);
    	
        Properties result = null;
        
        InputStream in = null;
        try
        {
            if (loader == null) {
            	loader = ClassLoader.getSystemClassLoader ();
            }
            
            if (LOAD_AS_RESOURCE_BUNDLE)
            {    
                name = name.replace ('/', '.');

                // Throws MissingResourceException on lookup failures:
                final ResourceBundle rb = ResourceBundle.getBundle (name,
                    Locale.getDefault (), loader);
                
                result = new Properties ();
                for (Enumeration<String> keys = rb.getKeys (); keys.hasMoreElements ();)
                {
                    final String key = (String) keys.nextElement ();
                    final String value = rb.getString (key);
                    
                    result.put (key, value);
                }
            }
            else
            {
                name = name.replace ('.', '/');
                
                if (! name.endsWith (SUFFIX)) {
                    name = name.concat (SUFFIX);
                }
                
                // Returns null on lookup failures:
                in = loader.getResourceAsStream (name);
                if (in != null)
                {
                    result = new Properties ();
                    result.load (in); // Can throw IOException
                }
            }
        }
        catch (Exception e)
        {
            result = null;
        }
        finally
        {
        	closeInputStream(in);
        }
        
        checkResultStatus(result, name);
        return result;
    }
    
    /**
     * A convenience overload of {@link #loadProperties(String, ClassLoader)}
     * that uses the current thread's context classloader.
     */
    public static Properties loadProperties (final String name)
    {
        return loadProperties (name,
            Thread.currentThread ().getContextClassLoader ());
    }
        
    private static final boolean THROW_ON_LOAD_FAILURE = true;
    private static final boolean LOAD_AS_RESOURCE_BUNDLE = false;
    private static final String SUFFIX = ".properties";

    /* ---------------------------------------------- */

	private static void closeInputStream(InputStream in) {
        if (in != null) {
        	try { 
        		in.close (); 
        	} 
        	catch (Throwable ignore) {
        		ignore.printStackTrace();
       		}
        }				
	}
	
	private static String preprocessNameArgument(String name) {
		String processedName = name;
		if (name == null) {
            throw new IllegalArgumentException ("null input: name");
        }
        if (name.startsWith ("/")) {
        	processedName = name.substring (1);
        }
        if (name.endsWith (SUFFIX)) {
        	processedName = name.substring (0, name.length () - SUFFIX.length ());
        }		
        return processedName;	
	}
	
	private static void checkResultStatus(Object result, String name) {
        if (THROW_ON_LOAD_FAILURE && (result == null))
        {
            throw new IllegalArgumentException ("could not load [" + name + "]"+
                " as " + (LOAD_AS_RESOURCE_BUNDLE
                ? "a resource bundle"
                : "a classloader resource"));
        }		
	}

} // End of class