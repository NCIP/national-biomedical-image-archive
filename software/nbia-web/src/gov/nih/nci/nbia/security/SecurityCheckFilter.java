package gov.nih.nci.nbia.security;


import gov.nih.nci.nbia.beans.security.SecurityBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

        String checkforloginpage = hreq.getServletPath();

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
            	//RequestDispatcher rd = request.getRequestDispatcher("/login.jsf");
            	//rd.forward(request, response);
            	HttpServletResponse httpServletResponse = (HttpServletResponse)response;
            	httpServletResponse.sendRedirect("/ncia/login.jsf");
                return;
            }
        }

        // deliver request to next filter
        chain.doFilter(request, response);
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
               checkforloginpage.endsWith(".jsf");
    }
}
