/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.viewer;

import gov.nih.nci.nbia.dao.ImageDAO;
import gov.nih.nci.nbia.dto.ImageSecurityDTO;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ImageServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(ImageServlet.class);

    public void init() {
    }


    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     */

    protected boolean hasAccess(String userName, String project, String siteName, String ssg)
    {
       try {
           AuthorizationManager am = new AuthorizationManager(userName);
           List<SiteData> siteList = am.getAuthorizedSites(RoleType.READ);
           List<String> ssgList = am.getAuthorizedSeriesSecurityGroups(RoleType.READ);

            for (SiteData siteData : siteList)
            {
                 if ((siteData.getCollection().equals(project) &&
                        siteData.getSiteName().equals(siteName))) {
                    if (!isBlank(ssg)) {
                        for (String authSsg : ssgList) {
                            if (authSsg.equals(ssg)) {
                                return true;
                            }
                        }
                    } else if (isBlank(ssg)) {
                        return true;
                    }
                }
            }
        }
        catch (Exception e) {
            return false;
        }

        return false;
    }

    protected String fileNameReMap(String fullName)
    {
    	String [] patterns = NCIAConfig.getImagePathPattern().split(",");
    	String [] headers=NCIAConfig.getMappedImagePathHead().split(",");
    	int pos = -1;
    	for (int i = 0; i < patterns.length; ++i) {
    		if (fullName.indexOf(patterns[i])!= -1) {
    			pos = i;
    			break;
    		}
    	}

    	if (pos==-1) {
    		return "NCIA File Mapping Error!";
    	}

        String [] parts=fullName.split(patterns[pos]);
        StringBuffer sb = new StringBuffer(headers[pos]);
        sb.append(parts[1]);
        logger.debug("mapped file name="+sb.toString().replaceAll("/", "\\\\"));

        return sb.toString().replaceAll("/", "\\\\");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
    	logger.debug("ImageServlet--" + request.getQueryString());

        String uid = request.getParameter("uid");
        String usr = request.getParameter("usr");

        ImageDAO imageDao = (ImageDAO)SpringApplicationContext.getBean("imageDAO");

        Collection<ImageSecurityDTO> results = imageDao.findImageSecurityBySeriesInstanceUID(uid);

        if ((results == null) || results.isEmpty()) {
            logger.info("No image found for request uid="+uid);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No image found for series:"+uid);
        }
        else {
        	ImageSecurityDTO firstDTO = results.iterator().next();
        	String project = firstDTO.getProject();
            String site = firstDTO.getSite();
            String ssg = firstDTO.getSsg();

            if (hasAccess(usr, project, site, ssg)) {
                //print out html
                response.setContentType("text/plain;charset=UTF-8");
                PrintWriter out = response.getWriter();

                for (ImageSecurityDTO gi : results)
                {
                    String fileName = gi.getFileName();
                    String mappedName = fileNameReMap(fileName);
                    out.println(mappedName);
                }
                out.close();
            }
            else {
                logger.info("No user found. The user ID in IGS request =" +usr);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "user "+usr+ " does not have access for series "+uid);
            }
         }

        //used for debug
        String  qString =request.getQueryString();
        logger.info("!!!query string from Cedara IGS= "+qString);

    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
       processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "Series file URI list";
    }

    private boolean isBlank(String value) {
        return ((value == null) || value.equals("") || value.equals("\n") ||
        (value.length() == 0));
    }
}
