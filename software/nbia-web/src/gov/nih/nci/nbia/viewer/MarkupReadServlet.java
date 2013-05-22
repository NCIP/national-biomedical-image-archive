/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
*/

package gov.nih.nci.nbia.viewer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class MarkupReadServlet extends MarkupServlet {
    private static Logger logger = Logger.getLogger(MarkupReadServlet.class);  

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        MarkupDelegate mdg = new MarkupDelegate();
//      used for debug
        String  qString =request.getQueryString();
        logger.info("!!!MarkupRead:query string from Cedara IRW= "+qString);
        
        String uid = request.getParameter("uid"); // series instance UID 
       
        response.setContentType("text/xml; charset=UTF-8");     
        PrintWriter out = response.getWriter();

        logger.info("start read markup data for series:"+ uid);

        String data = mdg.getMarkups(uid);
        if (data != null) {
            out.println(data); 
        }
        else {
            error(response, HttpServletResponse.SC_NOT_FOUND, "Markups for series " + uid + " were not found");  
        }
        logger.info("end of read markup data for series:"+uid);            
        out.close();
    }
}
