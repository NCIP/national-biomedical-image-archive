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




import gov.nih.nci.nbia.dao.WorkflowDAO;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.wadosupport.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.log4j.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WADOServlet extends HttpServlet
{
	private static Logger logger = Logger.getLogger(WADOServlet.class);
    


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		String user=null;
		String requestType=null;
		String series=null;
		String study=null;
		String image=null;
		try{
			user =(String)request.getSession().getAttribute("userId");
			requestType = request.getParameter("requestType");
			study = request.getParameter("studyUID");
			series = request.getParameter("seriesUID");
			image = request.getParameter("objectUID");
			WADOSupportDAO wadoDao = (WADOSupportDAO)SpringApplicationContext.getBean("WADOSupportDAO");
			WADOSupportDTO wdto = wadoDao.getWADOSupportDTO(study, series, image, user);
            if (wdto==null)
            {
            	throw new Exception("Image not found");
            }
			response.setContentType("application/dicom");   
			response.addHeader("Content-Disposition", "attachment; filename=" + image + ".dcm");
			
			OutputStream out = response.getOutputStream();   
						
			out.write(wdto.getImage());   
			System.out.println("write out the stream...");
			out.flush();   
			System.out.println("flush.....");
			out.close();  
			System.out.println("close....... the stream");
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
