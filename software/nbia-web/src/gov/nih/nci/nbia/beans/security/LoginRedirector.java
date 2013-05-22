/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.security;

import java.io.IOException;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletResponse;

/**
 * This encapsulates the logic that needs to run in the SecurityBean.login
 * to ensure that the requested resource is redirected to if there is one to redirect to
 * (set from the SecurityCheckFilter)
 */
class LoginRedirector {
	/**
	 * Look in the session for where the request was originally targeted
	 * before the security filter nuked it....  redirect to that resource.
	 */
    void afterLoginRedirect() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		//ServletContext servletContext = (ServletContext)externalContext.getContext();
		Map sessionMap = externalContext.getSessionMap();
		Map originalRequestMap = (Map)sessionMap.get("originalRequest");
		//System.out.println("originalRequest:"+originalRequestMap);

		//HttpServletResponse response = (HttpServletResponse)externalContext.getResponse();

		if(originalRequestMap!=null) {
			//should have leading slash
			try {
				//System.out.println("redirect:"+originalRequestMap.get("servletPath"));
				String redirectURL = "/ncia"+ originalRequestMap.get("servletPath");
				externalContext.redirect(redirectURL);
//				response.sendRedirect("/"+
//						              servletContext.getServletContextName()+
//						              originalRequestMap.get("servletPath"));
			}
			catch(IOException ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}
}
