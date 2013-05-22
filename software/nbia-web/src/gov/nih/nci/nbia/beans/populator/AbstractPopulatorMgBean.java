/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.populator;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

public abstract class AbstractPopulatorMgBean {

	/**
	 * This is the main entry point... blah blah
	 */
	public final void populate() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

		Map sessionMap = externalContext.getSessionMap();
		Map originalRequestMap = (Map)sessionMap.get("originalRequest");
		try {
			if(originalRequestMap!=null) {
				Map parameterMap = (Map)originalRequestMap.get("parameterMap");

				logger.debug("Processing originalRequestMap with N entries:"+parameterMap.size());

				for(Object key : parameterMap.keySet()) {
					Object value = parameterMap.get(key);

					try {
						BeanUtils.setProperty(this, (String)key, value);

						logger.debug("Processing originalRequestMap, set Property:"+key+"="+value);
					}
					catch(Exception ex) {
						logger.error("couldnt set:"+key+", did you define it as a property on the backing bean?");
						ex.printStackTrace();
					}
				}

			}
			//else had authorization and came right here
			else {
				logger.debug("no original request in populate");
			}

			populateImpl();
			resetProperties();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally	{
			HttpServletRequest request = (HttpServletRequest)externalContext.getRequest();
			request.getSession().removeAttribute("originalRequest");
		}
	}

	/**
	 * Any backing bean that is to be invoked as part of
	 * populating a page, or initializing anything for a
	 * "bookmarkable" page needs to implement this.
	 *
	 * <p>Any request parameters to the request will exist
	 * as mapped bean properties at this point, presuming
	 * that the properties are defined in the subclass.
	 */
	protected abstract void populateImpl() throws Exception;

	/////////////////////////////////////PRIVATE////////////////////////////////

	private static Logger logger = Logger.getLogger(AbstractPopulatorMgBean.class);

	/**
	 * Any properties in the bean are set to null.  there
	 * shouldnt be any properties that arent used as population
	 * parameters.....
	 */
	private void resetProperties() throws Exception {
		Map propertiesMap = BeanUtils.describe(this);
		for(Object key : propertiesMap.keySet()) {
			BeanUtils.setProperty(this, (String)key, null);
		}
	}
}
