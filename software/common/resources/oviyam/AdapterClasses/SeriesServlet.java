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

import in.raster.oviyam.SeriesInfo;
import in.raster.oviyam.model.SeriesModel;
import in.raster.oviyam.util.SeriesComparator;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author asgar
 */
public class SeriesServlet extends HttpServlet {
   
    //Initialize the Logger.
    private static Logger log = Logger.getLogger(SeriesServlet.class);

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String patID = request.getParameter("patientID");
        String studyUID = request.getParameter("studyUID");
        String dcmURL = request.getParameter("dcmURL");
        HttpSession session = request.getSession();
		String oviyamId = (String)session.getAttribute("oviyamId");
		String wadoUrl = (String)session.getAttribute("wadoUrl");
		Object seriesOUid = session.getAttribute("seriesUidIn");
		String seriesUid = null;
		if (seriesOUid!=null){
			seriesUid=seriesOUid.toString();
		}
		System.out.println("The latest build");
		System.out.println("In seriesinfoservlet patID:"+patID+"-studyUID:"+studyUID+
				"-seriesUid:"+seriesUid+"-serverName:"+dcmURL
				+"-oviyamId:"+oviyamId+"-wadoUrl:"+wadoUrl);
        SeriesInfo series = new SeriesInfo();
        series.callFindWithQuery(patID, studyUID, dcmURL, oviyamId, wadoUrl, seriesUid);
        ArrayList<SeriesModel> seriesList = series.getSeriesList();
        Collections.sort(seriesList, new SeriesComparator());

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj = null;

        try {
            for(int i=0; i<seriesList.size(); i++) {
                SeriesModel sm = (SeriesModel) seriesList.get(i);
                jsonObj = new JSONObject();
                jsonObj.put("seriesUID", sm.getSeriesIUID());
                jsonObj.put("seriesNumber", sm.getSeriesNumber());
                jsonObj.put("modality", sm.getModality());
                jsonObj.put("seriesDesc", sm.getSeriesDescription());
                jsonObj.put("bodyPart", sm.getBodyPartExamined());
                jsonObj.put("totalInstances", sm.getNumberOfInstances());
                jsonObj.put("patientId", patID);
                jsonObj.put("studyUID", studyUID);

                jsonArray.put(jsonObj);
            }

            PrintWriter out = response.getWriter();
            out.print(jsonArray);
            out.close();
        } catch (Exception ex) {
            log.error(ex);
        }

    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    }

}