/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.nbia.servlet;




import gov.nih.nci.ncia.search.APIURLHolder;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
public class WADOServlet extends HttpServlet
{
	private static Logger logger = Logger.getLogger(WADOServlet.class);
    

/**
 * Originally this was for in process wado calls, but with the secure calls now in Jersey all it does is look
 * up the user for from an oviyam id.
 * 
 */
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		String user=null;
		try{
			    
				String key = request.getParameter("oviyamId");
				logger.info("Geting user for "+key);
				String oUser=APIURLHolder.getUser(key);
				if (oUser==null)
				{
					oUser="NOT FOUND";
				} 
				logger.info("Found user "+oUser);
			    response.setContentType("text/html");
			    PrintWriter out = response.getWriter();  
			    out.println(oUser);
				out.flush();   
				out.close();  
				return;
		}
           catch(Exception e){
			e.printStackTrace();
			System.out.println( "WADOServlet Error: " + e.getMessage() );
			response.sendError( HttpServletResponse.SC_NOT_FOUND );
		}
		
		
	}

	public void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
		doGet(req,res);
	}



}
