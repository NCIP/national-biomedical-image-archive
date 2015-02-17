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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class MarkupSaveServlet extends MarkupServlet {
    private static Logger logger = Logger.getLogger(MarkupSaveServlet.class);  
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        String lineSeparator = System.getProperty("line.separator");
        MarkupDelegate mdg = new MarkupDelegate();
        StringBuffer sbr = new StringBuffer();
//      used for debug
        String  qString =request.getQueryString();
        logger.info("!!!Markup Save Servlet:query string from Cedara IRW= "+qString);

        String uid = request.getParameter("uid");   // series instance UID  
        String usr = request.getParameter("~usr");   // user login name
       
        ServletInputStream istrm = request.getInputStream();
        
        BufferedReader rdr = new BufferedReader(new InputStreamReader(istrm));        
        String s;
        logger.info("start to save markup for series:"+ uid);        
        while((s = rdr.readLine()) != null)
        {
            sbr.append(s);
            sbr.append(lineSeparator);
        } 
        logger.info("end of save markup for series:"+ uid);
        if (isBlank(sbr.toString())) {
            error(response, 
                  HttpServletResponse.SC_BAD_REQUEST, 
                  "There is no Markup for series " + uid);
        }
        else {
            mdg.saveMarkup(uid, usr, sbr.toString());
        }
        istrm.close();
    } 
}

