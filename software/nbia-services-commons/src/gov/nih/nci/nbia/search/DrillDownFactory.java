/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;
import gov.nih.nci.nbia.util.NCIAConfig;
public class DrillDownFactory {

	public static DrillDown getDrillDown() {
		if(instance==null) {
			instance = createDrillDown();
		}
		return instance;
	}

	/////////////////////////////////////PRIVATE////////////////////////////////////

	private static DrillDown instance;


	private static DrillDown createDrillDown() {
		String drillDownClassName = NCIAConfig.getDrilldownClassName();

		if(drillDownClassName==null) {
			throw new RuntimeException("drilldown.className must be defined in system properties");
		}
		else {
			try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Class clazz = Class.forName(drillDownClassName, false, loader);
				DrillDown drillDown =  (DrillDown)clazz.newInstance();

				return drillDown;
			}
			catch(Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}
