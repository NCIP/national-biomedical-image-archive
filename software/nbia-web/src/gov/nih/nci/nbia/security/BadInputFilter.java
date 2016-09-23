/*
 * Took some code written by by Jason Brittain for Tomcat: The Definitive Guide, 2nd Edition
 * and modified to scan and block the bad inputs specified in web.xml
 * -- Q. Pan
 */
package gov.nih.nci.nbia.security;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BadInputFilter implements Filter
  {
    /**
     * The comma-delimited set of <code>deny</code> expressions.
     */
    protected String deny = null;

    /**
     * The set of <code>deny</code> regular expressions we will evaluate.
     */
    protected Pattern denies[] = new Pattern[0];

    /**
     * The ServletContext under which this Filter runs. Used for logging.
     */
    protected ServletContext servletContext;

    // ------------------------------------------------- Constructors

    /**
     * Construct a new instance of this class with default property values.
     */
    public BadInputFilter()
      {}

    /**
     * Return a comma-delimited set of the <code>deny</code> expressions
     * configured for this Filter, if any; otherwise, return <code>null</code>.
     */
    public String getDeny()
      {
        return (this.deny);
      }


    /**
     * Set the comma-delimited set of the <code>deny</code> expressions
     * configured for this Filter, if any.
     *
     * @param deny The new set of deny expressions
     */
    public void setDeny(String deny)
      {
        this.deny = deny;
        denies = precalculate(deny);
        servletContext.log("BadInputFilter: deny = " + deny);
      }


    // ----------------------------------------------- Public Methods

    /**
     * {@inheritDoc}
     */
    public void init(FilterConfig filterConfig) throws ServletException
      {
        servletContext = filterConfig.getServletContext();

        // Parse the Filter's init parameters.
        setDeny(filterConfig.getInitParameter("deny"));
        servletContext.log(toString() + " initialized.");
      }


    /**
     * Sanitizes request parameters before bad user input gets into the web
     * application.
     *
     * @param request The servlet request to be processed
     * @param response The servlet response to be created
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain filterChain) throws IOException, ServletException
      {
        // Skip filtering for non-HTTP requests and responses.
        if (!(request instanceof HttpServletRequest)
            || !(response instanceof HttpServletResponse))
          {
            filterChain.doFilter(request, response);
            return;
          }

        // Only let requests through based on the allows and denies.
        if (processAllowsAndDenies(request, response))
          {
            filterChain.doFilter(request, response);
          }
      }


    /**
     * Stops requests that contain forbidden string patterns in parameter names
     * and parameter values.
     *
     * @param request The servlet request to be processed
     * @param response The servlet response to be created
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     *
     * @return false if the request is forbidden, true otherwise.
     */
    @SuppressWarnings("rawtypes")
    public boolean processAllowsAndDenies(ServletRequest request, ServletResponse response)
        throws IOException, ServletException
      {
//        Map paramMap = request.getParameterMap();

        //only check the parameter "rand" as a patch for iceface vulnerabilities
        String value = request.getParameter("rand");
        if (value!=null&&value.length()>0)
        {
           if (!checkAllowsAndDenies(value, response))
           {                       	
             return false;
           }
        
           //also check the parameter "rand" is a numerical number or not
           if (!isNumeric(value.trim()))
           {
        	   HttpServletResponse hres = (HttpServletResponse) response;
               hres.sendError(HttpServletResponse.SC_FORBIDDEN);
               return false;
           }        
        }
        
        //If it is decided to check all params and header, uncommon the code below.
        
//        // Loop through the list of parameters.
//        Iterator y = paramMap.keySet().iterator();
//        while (y.hasNext())
//          {
//            String name = (String) y.next();
//            String[] values = request.getParameterValues(name);
//
//            // See if the name contains a forbidden pattern.
//            if (!checkAllowsAndDenies(name, response))
//              {           	
//                return false;
//              }
//
//            // Check the parameter's values for the pattern.
//            if (values != null)
//              {
//                for (int i = 0; i < values.length; i++)
//                  {
//                    String value = values[i];
//                    if (!checkAllowsAndDenies(value, response))
//                      {                         	
//                        return false;
//                      }
//                  }
//              }
//          }
//        
//        //check request header
//		Enumeration headerNames = ((HttpServletRequest)request).getHeaderNames();
//		while (headerNames.hasMoreElements()) {
//			String key = (String) headerNames.nextElement();
//			String headerValue = ((HttpServletRequest)request).getHeader(key);
//            if (!checkAllowsAndDenies(headerValue, response))
//            {
//              return false;
//            }
//		}

        // No parameter caused a deny. The request should continue.
        
        return true;
      }


    /**
     * Perform the filtering that has been configured for this Filter, matching
     * against the specified request property. If the request is allowed to
     * proceed, this method returns true. Otherwise, this method sends a
     * Forbidden error response page, and returns false.
     * <br>
     * <br>
     *
     * This method borrows heavily from RequestFilterValve.process().
     *
     * @param property The request property on which to filter
     * @param response The servlet response to be processed
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     *
     * @return true if the request is still allowed to proceed.
     */
    public boolean checkAllowsAndDenies(String property, ServletResponse response)
        throws IOException, ServletException
      {
        // If there were no denies and no allows, process the request.
        if (denies.length == 0)
          {
            return true;
          }

        // Check the deny patterns, if any
        for (int i = 0; i < denies.length; i++)
          {
            Matcher m = denies[i].matcher(property);
            if (m.find())
              {
                if (response instanceof HttpServletResponse)
                  {
                    HttpServletResponse hres = (HttpServletResponse) response;
                    hres.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                  }
              }
          }

        // Allow if denies specified but nothing found so far
        if (denies.length > 0)
          {
            return true;
          }

        // Otherwise, deny the request.
        if (response instanceof HttpServletResponse)
          {
            HttpServletResponse hres = (HttpServletResponse) response;
            hres.sendError(HttpServletResponse.SC_FORBIDDEN);
          }
        return false;
      }


    /**
     * Return a text representation of this object.
     */
    @Override
    public String toString()
      {
        return "BadInputFilter";
      }


    /**
     * {@inheritDoc}
     */
    public void destroy()
      {

      }


    // -------------------------------------------- Protected Methods

    /**
     * Return an array of regular expression objects initialized from the
     * specified argument, which must be <code>null</code> or a comma-delimited
     * list of regular expression patterns.
     *
     * @param list The comma-separated list of patterns
     *
     * @exception IllegalArgumentException if one of the patterns has invalid syntax
     */
    protected Pattern[] precalculate(String list)
      {
        if (list == null)
            return (new Pattern[0]);

        list = list.trim();
        if (list.length() < 1)
            return (new Pattern[0]);

        list += ",";

        ArrayList<Pattern> reList = new ArrayList<Pattern>();
        while (list.length() > 0)
          {
            int comma = list.indexOf(',');

            if (comma < 0)
                break;

            String pattern = list.substring(0, comma).trim();
            try
              {
                reList.add(Pattern.compile(pattern));
              }
            catch (PatternSyntaxException e)
              {
                IllegalArgumentException iae = new IllegalArgumentException(
                    "Syntax error in request filter pattern" + pattern);
                iae.initCause(e);
                throw iae;
              }
            list = list.substring(comma + 1);
          }

        Pattern reArray[] = new Pattern[reList.size()];
        return ((Pattern[]) reList.toArray(reArray));
      }

    public static boolean isNumeric(String str)
    {	
        DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
        char localeMinusSign = currentLocaleSymbols.getMinusSign();

        if ( !Character.isDigit( str.charAt( 0 ) ) && str.charAt( 0 ) != localeMinusSign ) return false;

        boolean isDecimalSeparatorFound = false;
        char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

        for ( char c : str.substring( 1 ).toCharArray() )
        {   
            if ( !Character.isDigit( c ) )
            {
                if ( c == localeDecimalSeparator && !isDecimalSeparatorFound )
                {
                    isDecimalSeparatorFound = true;
                    continue;
                }
                return false;
            }
        }
        return true;    	
    }
  }
