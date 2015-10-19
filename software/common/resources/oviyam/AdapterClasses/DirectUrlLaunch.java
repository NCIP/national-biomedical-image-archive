/* ***** BEGIN LICENSE BLOCK *****
* Version: MPL 1.1/GPL 2.0/LGPL 2.1
*
* The contents of this file are subject to the Mozilla Public License Version
* 1.1 (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
*
* Software distributed under the License is distributed on an "AS IS" basis,
* WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
* for the specific language governing rights and limitations under the
* License.
*
* The Original Code is part of Oviyam, an web viewer for DICOM(TM) images
* hosted at http://skshospital.net/pacs/webviewer/oviyam_0.6-src.zip
*
* The Initial Developer of the Original Code is
* Raster Images
* Portions created by the Initial Developer are Copyright (C) 2014
* the Initial Developer. All Rights Reserved.
*
* Contributor(s):
* Babu Hussain A
* Devishree V
* Meer Asgar Hussain B
* Prakash J
* Suresh V
*
* Alternatively, the contents of this file may be used under the terms of
* either the GNU General Public License Version 2 or later (the "GPL"), or
* the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
* in which case the provisions of the GPL or the LGPL are applicable instead
* of those above. If you wish to allow use of your version of this file only
* under the terms of either the GPL or the LGPL, and not to allow others to
* use your version of this file under the terms of the MPL, indicate your
* decision by deleting the provisions above and replace them with the notice
* and other provisions required by the GPL or the LGPL. If you do not delete
* the provisions above, a recipient may use your version of this file under
* the terms of any one of the MPL, the GPL or the LGPL.
*
* ***** END LICENSE BLOCK ***** */

package in.raster.oviyam.servlet;

import in.raster.oviyam.PatientInfo;
import in.raster.oviyam.xml.handler.LanguageHandler;
import in.raster.oviyam.xml.handler.ListenerHandler;
import in.raster.oviyam.xml.handler.ServerHandler;
import in.raster.oviyam.xml.handler.XMLFileHandler;
import in.raster.oviyam.xml.model.Server;

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class DirectUrlLaunch
 */

public class DirectUrlLaunch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DirectUrlLaunch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String patId = request.getParameter("patientID");
		String studyId = request.getParameter("studyUID");
		String serverName = request.getParameter("serverName");		
		String wadoUrl = request.getParameter("wadoUrl");	
		String seriesUid = request.getParameter("seriesUid");
		HttpSession session = request.getSession();
		String oviyamId = null;
		Object oviyamCheck=session.getAttribute("oviyamId");
		if (oviyamCheck!=null&&oviyamCheck.toString().length()>0)
		{
			System.out.println("existing id:"+oviyamCheck.toString());
			oviyamId=oviyamCheck.toString();
		} else {
			oviyamId=request.getParameter("oviyamId");
			System.out.println("new id:"+oviyamId);
			session.setAttribute("oviyamId", oviyamId);
		}
		if (seriesUid!=null&&seriesUid.length()>0)
		{
			session.setAttribute("seriesUidIn", seriesUid);
		}
			
		System.out.println("In DirectURL patID:"+patId+"-studyUID:"+studyId+"-seriesId"+seriesUid+"-serverName:"+serverName
				+"-oviyamId:"+oviyamId+"-wadoUrl:"+wadoUrl);

		String forwardUrl = "";	

		forwardUrl = "/viewer.html?"; 
		forwardUrl += "patientID=" + patId;
        forwardUrl += "&studyUID=" + studyId;
		forwardUrl += "&serverName=" + serverName;
        forwardUrl += "&oviyamId=" + oviyamId;
		forwardUrl += "&wadoUrl=" + wadoUrl;
		
		
		session.setAttribute("wadoUrl", wadoUrl);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardUrl);
	    dispatcher.forward(request,response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	 public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        doGet(request, response);
	    }

}