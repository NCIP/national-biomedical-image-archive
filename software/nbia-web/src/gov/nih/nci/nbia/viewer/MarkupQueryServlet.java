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

public class MarkupQueryServlet extends MarkupServlet {
    private static Logger logger = Logger.getLogger(MarkupQueryServlet.class);  
      
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        MarkupDelegate mdg = new MarkupDelegate();
//      used for debug
        String  qString =request.getQueryString();
        logger.info("!!!MarkupQuery/query string from Cedara IRW= "+qString);
        
        String uid = request.getParameter("uid");
        String usr1 = request.getParameter("~usr");
   
        response.setContentType("text/plain;charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("uid="+uid + "&");
        
        if (mdg.markupExist(uid))
        {
            logger.info("!!!!!!!Markup Exist");
            out.println("readURL="+"/ncia/MarkupRead"+ "&");
        }
       
        out.println("writeURL="+"/ncia/MarkupSave");
        out.println("user="+usr1+"&writable=y");        
//        out.println("other_user="+"lethai@mail.nih.gov"+"&readable=y");
//        out.println("other_user="+"panq@mail.nih.gov"+"&readable=y");
//        out.println("other_user="+"basuan@mail.nih.gov"+"&readable=y");
        out.println("other_user="+"*"+"&readable=y");
        out.close();
    }
}
