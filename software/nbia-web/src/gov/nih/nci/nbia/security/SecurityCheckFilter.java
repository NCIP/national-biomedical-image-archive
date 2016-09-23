/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.security;


import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.util.NCIAConfig;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import gov.nih.nci.nbia.searchresult.APIURLHolder;

/**
 * Taken from sun software forums
 *
 */
public class SecurityCheckFilter implements Filter {
    private final static String FILTER_APPLIED = "_security_filter_applied";

    final static Logger logger = Logger.getLogger(SecurityCheckFilter.class);

    public SecurityCheckFilter() { // called once. no method arguments allowed
        // here!
    }

    public void init(FilterConfig conf) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request,
    		             ServletResponse response,
    		             FilterChain chain) throws IOException, ServletException {

        HttpServletRequest hreq = (HttpServletRequest) request;
        HttpSession session = hreq.getSession();

        logger.info("!!! doing SecurityCheckFilter");
        // added to know the current address
        // and current user for Oviyam
        	try {
				APIURLHolder.setUrl(NCIAConfig.getImageServerUrl());
			} catch (Exception e) {
				e.printStackTrace();
			}

        String checkforloginpage = hreq.getServletPath();

        String referer = (String)session.getAttribute("previous");
        session.setAttribute("previous", checkforloginpage);

        logger.info("session:"+session.getId());
    	logger.info("request:"+hreq.getRequestURL());
    	logger.info("checkforloginpage:"+checkforloginpage);

        // dont filter login.jsp because otherwise an endless loop.

        // & only filter .jsp otherwise it will filter all images etc as well.
        if ((request.getAttribute(FILTER_APPLIED) == null) &&
            shouldApplyFilter(checkforloginpage)) {

            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);

            // If the session bean is not null get the session bean property
            // username.
            boolean loggedIn = false;

            if (((SecurityBean) session.getAttribute("securityBean")) != null) {
                loggedIn = ((SecurityBean) session.getAttribute("securityBean")).getLoggedIn();
            }
            logger.info("loggedIn:"+loggedIn);
            if (!loggedIn) {
            	session.setAttribute("originalRequest", saveRequestInfo(hreq));
            	HttpServletResponse httpServletResponse = (HttpServletResponse)response;
            	httpServletResponse.sendRedirect("/ncia/login.jsf");
                return;
            }

            if (loggedIn && referer.endsWith("login.jsf") && checkforloginpage.endsWith("home.jsf"))
            {
				// Change the session ID for fixing sessionID fixation problem
				HashMap<String, Object> attributes = new HashMap<String, Object>();
				// copy/save all attributes
				Enumeration<String> enames = session.getAttributeNames();
				while (enames.hasMoreElements()) {
					String name = enames.nextElement();
					if (!name.equals("JSESSIONID")) {
						attributes.put(name, session.getAttribute(name));
					}
				}

				// invalidate the session
				session.invalidate();

				// create a new session
				session = hreq.getSession(true);
				// "restore" the session values
				for (Map.Entry<String, Object> et : attributes.entrySet()) {
					session.setAttribute(et.getKey(), et.getValue());
				}
			}
		}

        // deliver request to next filter
        chain.doFilter(request, response);
    }

    public void refresh() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, context
         .getViewRoot().getViewId());
        context.setViewRoot(viewRoot);
        context.renderResponse(); //Optional
      }

    private static Map<String, Object> saveRequestInfo(HttpServletRequest httpServletRequest) {
    	Map<String, Object>  originalRequestMap = new HashMap<String, Object> ();
    	originalRequestMap.put("servletPath", httpServletRequest.getServletPath());
    	originalRequestMap.put("requestURI", httpServletRequest.getRequestURI());
    	originalRequestMap.put("parameterMap", new HashMap(httpServletRequest.getParameterMap()));

    	return originalRequestMap;
    }
    /**
     * Based upon the path of the request, does security apply to the
     * path.  Mostly yes, but some pre-login screens shouldn't be secured.
     *
     * <p>
     * This isn't ideal as it ties compiled code to layout of web site.
     * Better approach would move these paths into a config file that
     * could be changed as layout changes if necessary.
     */
    private boolean shouldApplyFilter(String checkforloginpage) {
        return !checkforloginpage.endsWith("login.jsf")&&
               // These next pages are available prior to logging in,
               !checkforloginpage.endsWith("imageViewers.jsf") &&
               !checkforloginpage.endsWith("legalRules.jsf") &&
               !checkforloginpage.endsWith("welcome.jsf") &&
               !checkforloginpage.endsWith("registerMain.jsf") &&
               !checkforloginpage.endsWith("accountSupport.jsf") &&
               !checkforloginpage.contains("externalLinks.jsf") &&
              checkforloginpage.endsWith(".jsf");
    }
}
