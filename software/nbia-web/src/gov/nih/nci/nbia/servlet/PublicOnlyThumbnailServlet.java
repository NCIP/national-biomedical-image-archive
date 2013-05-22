/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.servlet;

import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.util.ThumbnailUtil;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is the cousin of the ThumbnailServer.... it is application wide
 * and allows access only to thumbnails that are public.
 */
public class PublicOnlyThumbnailServlet extends HttpServlet {

    
    public void service(HttpServletRequest request,
    		            HttpServletResponse response) throws ServletException, 
    		                                                 IOException {

    	//crap, if this takes too long.... can't put it in init()
    	//because authorizations can change.  maybe put on a timer - like every hour
    	//or so rebuild this and store it?
    	AuthorizationManager authorizationManager = null;
    	try {
    		authorizationManager = new AuthorizationManager();
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    		throw new RuntimeException();
    	}	
    	
    	// Get the file path ID from the request
        String imageId = request.getParameter("imageId");
        if(imageId==null) {
        	response.setStatus(400);
        	return;
        }
        int indx = imageId.indexOf('-');
		String locPart1 = imageId;
		String locPart2 = null;
		if (indx != -1) {
			locPart1 = imageId.substring(0, indx);
			locPart2 = imageId.substring(indx + 1);
		}

        File dicomFile = ThumbnailUtil.retrieveImageFile(authorizationManager,
        		locPart1);
        if(dicomFile==null) {
        	response.setStatus(400);
        	return;
        }
        File thumbnailFile = null;
        if (locPart2 != null) {
			thumbnailFile = ThumbnailUtil.deduceThumbnailFromDicomFile(
					dicomFile, locPart2);
		} else {
			thumbnailFile = ThumbnailUtil.deduceThumbnailFromDicomFile(dicomFile);
		}

        ThumbnailUtil.writeJpegFile(thumbnailFile, response);
    }

}
