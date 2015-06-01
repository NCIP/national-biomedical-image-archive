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

import in.raster.oviyam.xml.handler.QueryParamHandler;
import in.raster.oviyam.xml.handler.UserHandler;
import in.raster.oviyam.xml.model.Button;
import in.raster.oviyam.xml.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.json.JSONArray;

/**
 *
 * @author asgar
 */
public class UserConfiguration extends HttpServlet {

    private static Logger log = Logger.getLogger(UserConfiguration.class);

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
         PrintWriter out = null;
        try {
            String settings = request.getParameter("settings");
            String actionToDo = request.getParameter("todo");
            String settingsValue = request.getParameter("settingsValue");
            
            //Get user details
            /*InitialContext ctx = new InitialContext();
            Subject subject = (Subject) ctx.lookup("java:comp/env/security/subject");
            List<Principal> prinList = new ArrayList<Principal>(subject.getPrincipals());
            Principal p = prinList.get(0);
            String userName = p.getName();
            System.out.println("User name: " + userName);*/ //Works in JBoss but not in Tomcat
            
            //String userName = request.getUserPrincipal().getName();
            String userName = "NBIA";
            System.out.println("settings:"+settings+"-actionToDo:"+actionToDo+"settingsValue"+settingsValue);
            if(actionToDo.equalsIgnoreCase("ROLE")) {
            	String role = "";
            	if(request.isUserInRole("WebAdmin") || request.isUserInRole("admin")) {
            		role = "Admin";
                } else {
                	role = "Other";
                }
            	response.setContentType("text/html");
            	out = response.getWriter();
        		out.print(role);
        		out.close();
            } else {        

            //File tempDir = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
            UserHandler uh = new UserHandler();
            String str = null;
            /*   out = response.getWriter();
            User user = uh.findUserByName(userName);  */
            
            User user = null;
            if(user == null) {
                user = new User();
                user.setUserName(userName);
                Button btn = new Button();
                btn.setLabel("Today CT");
                btn.setDateCrit("t");
                btn.setModality("CT");
                btn.setAutoRefresh("0");
              //  uh.addNewUser(btn, userName);
            }

            if(user != null) {
                if (actionToDo.equalsIgnoreCase("READ")) {
                    if (settings.equals("theme")) {
                        str = user.getTheme();
                    } else if (settings.equals("sessTimeout")) {
                        str = user.getSessTimeout();
                    } else if(settings.equals("userName")) {
                        str = user.getUserName();
                        String sessTimeout = user.getSessTimeout();
                        if(sessTimeout != null) {
                            HttpSession session = request.getSession(false);
                            session.setMaxInactiveInterval(Integer.parseInt(sessTimeout));
                        }
                    } else if(settings.equals("viewerSlider")) {
                        str = user.getViewerSlider();
                    } else if(settings.equals("roles")) {
                        //Principal pTmp = prinList.get(1);
                        //str = pTmp.toString();
                        if(request.isUserInRole("WebAdmin") || request.isUserInRole("admin")) {
                            str = "Admin";
                        } else {
                            str = "Other";
                        }
                    } else if(settings.equals("buttons")) {
                        QueryParamHandler qph = new QueryParamHandler();
                        List<Button> butList = qph.getAllButtons(userName);
                        JSONArray jsonArray = new JSONArray(butList);
                        str = jsonArray.toString();
                    }
                    out = response.getWriter();
                    out.print(str);
                } else if (actionToDo.equalsIgnoreCase("UPDATE")) {
                    if (settings.equals("theme")) {
                        user.setTheme(settingsValue);
                    } else if (settings.equals("sessTimeout")) {
                        user.setSessTimeout(settingsValue);
                    } else if(settings.equals("viewerSlider")) {
                        user.setViewerSlider(settingsValue);
                    }
                    uh.updateUser(user);
                    out = response.getWriter();
                    out.println("Success");
                }
            }
            }
        } catch (Exception ex) {
            log.error("Exception occured in User Configuration servlet", ex);
            //out.println("Failure");
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}