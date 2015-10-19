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
public class DicomRetrieve extends HttpServlet {

    /**
     *
     */
    private static Logger log = Logger.getLogger(DicomRetrieve.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String wadoUrl=request.getParameter("wadourl");
	String modifiedWadoUrl=wadoUrl.replaceAll("_", "&");
	String imageURL=modifiedWadoUrl.concat("&contentType=image/jpeg");
	HttpSession session = request.getSession();
	String oviyamId = (String)session.getAttribute("oviyamId");
	imageURL=imageURL.concat("&oviyamId="+oviyamId);
	String wado2Url = (String)session.getAttribute("wadoUrl");
	imageURL=imageURL.concat("&wadoUrl="+wado2Url);
	System.out.println("in DicomRetrieve"+imageURL);
	InputStream resultInStream = null;
	OutputStream out = response.getOutputStream();
	try {
            int bytes_read;
	    URL imageUrl = new URL(imageURL);
	    resultInStream = imageUrl.openStream();
	    byte[] buffer = new byte[4096];
	    while ((bytes_read = resultInStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytes_read);
            }
            out.flush();
        } catch (Exception e) {
            log.error("Error while streaming DICOM image ", e);
	} finally {
            out.close();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}