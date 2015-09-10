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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 *
 * @author asgar
 */
public class DcmImage extends HttpServlet {

    /*
     * Initialize the Logger.
     */
    private static Logger log = Logger.getLogger(DcmImage.class);

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String imageURL = "";
        System.out.println("In DcmImage");
        // Reads the parameters from the request object which is sent by user.
        String serverURL = request.getParameter("serverURL");
        String study = request.getParameter("study");
        String series = request.getParameter("series");
        String object = request.getParameter("object");
        String contentType = request.getParameter("contentType");
        String windowCenter = request.getParameter("windowCenter");
        String windowWidth = request.getParameter("windowWidth");
        String rows = request.getParameter("rows");
        String transferSyntax = request.getParameter("transferSyntax");
        String frameNo = request.getParameter("frameNumber");

        imageURL = serverURL + "?requestType=WADO&studyUID=" + study + "&seriesUID=" + series + "&objectUID=" + object+"&contentType=image/jpeg";
    /*
        if(contentType != null) {
            imageURL += "&contentType=" + contentType;
            response.setContentType(contentType);
        }

        if(windowCenter != null && windowCenter.length() > 0) {
            imageURL += "&windowCenter=" + windowCenter;
        }

        if(windowWidth != null && windowWidth.length() > 0) {
            imageURL += "&windowWidth=" + windowWidth;
        }

        if(rows != null && rows.length() > 0) {
            imageURL += "&rows=" + rows;
        }

        if(transferSyntax != null && transferSyntax.length()>0) {
            imageURL += "&transferSyntax=" + transferSyntax;
        }
       */
        if(frameNo != null && frameNo.length()>0) {
            imageURL += "&frameNumber=" + frameNo;
        }

        InputStream is = null;

        // Get the response OutputStream instance to write a image in the response.
        OutputStream os = response.getOutputStream();
    	HttpSession session = request.getSession();
    	String oviyamId = (String)session.getAttribute("oviyamId");
    	imageURL=imageURL.concat("&oviyamId="+oviyamId);
    	String wado2Url = (String)session.getAttribute("wadoUrl");
    	imageURL=imageURL.concat("&wadoUrl="+wado2Url);

        try {
            // Initialize the URL for the requested image.
        	System.out.println("wado call:"+imageURL);
            URL imgURL = new URL(imageURL);
            // Open the InputStream of the URL.
            if(object!=null && object.length()>0) {
                is = imgURL.openStream();
                // Initialize the byte array to read and hold the bytes from the imgURL stream.
                byte[] buffer = new byte[4096];
                int bytes_read;

                //Write the image bytes into the response's output stream.
                while((bytes_read=is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytes_read);
                }

                os.flush();
            }
        } catch(Exception e) {
            log.error("Unable to read and write the image from " + serverURL, e);
        } finally {
            // Close the output stream.
            if(os != null) {
                try {
                    os.close();
                } catch(Exception ignore) { }
            }

            //Close the input stream.
            if(is != null) {
                try {
                    is.close();
                } catch(Exception ignore) { }
            }
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}