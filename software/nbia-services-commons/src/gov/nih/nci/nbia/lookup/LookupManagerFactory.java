/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.lookup;

import java.lang.reflect.Constructor;
import java.util.Collection;
import gov.nih.nci.nbia.util.*;
public class LookupManagerFactory {
	
	/**
	 * Construct a LookupManager based upon the lookupManager.className property
	 * and then cache the instance.
	 */
	public static LookupManager createLookupManager(Collection<String> authorizedCollections) {
		String lookupManagerClassName = NCIAConfig.getlookupManagerClassName();
		
		if(lookupManagerClassName==null) {
			throw new RuntimeException("lookupManager.className must be defined in properties");
		}
		else {
			try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Class clazz = Class.forName(lookupManagerClassName, false, loader);
				Constructor<LookupManager> constructor = clazz.getConstructor(new Class[]{Collection.class});
				LookupManager lookupManager =  constructor.newInstance(authorizedCollections);
				return lookupManager;
			}
			catch(Exception ex) {
				throw new RuntimeException(ex);
			}
		}			
	}
}
