/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.servlet;

import gov.nih.nci.nbia.lookup.ImageRegisterMap;
import gov.nih.nci.nbia.util.ThumbnailUtil;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class ThumbnailServer extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Get the file path ID from the request
		String location = request.getParameter("location");
		int indx = location.indexOf('-');
		String locPart1 = location;
		String locPart2 = null;
		if (indx != -1) {
			locPart1 = location.substring(0, indx);
			locPart2 = location.substring(indx + 1);
		}
		HttpSession session = request.getSession();
		logger.debug("ThumbnailServer has been called");
		// Lookup the actual file path from the security bean using the ID
		String filePath;
		if (indx != -1) {
			filePath = lookupFilePath(locPart1, session);
		} else {
			filePath = lookupFilePath(location, session);
		}
		if (filePath == null) {
			logger.error("Registered ID " + location + " was not registered");
			response.setStatus(400);
			return;
		}
		File thumbnailFile;
		if (locPart2 != null) {
			thumbnailFile = ThumbnailUtil.deduceThumbnailFromDicomFile(
					new File(filePath), locPart2);
		} else {
			thumbnailFile = ThumbnailUtil
					.deduceThumbnailFromDicomFile(new File(filePath));
		}
		ThumbnailUtil.writeJpegFile(thumbnailFile, response);
	}

    
    ///////////////////////////////////////PRIVATE////////////////////////////////////
    
    private static Logger logger = Logger.getLogger(ThumbnailServer.class);

    private static String lookupFilePath(String location, HttpSession session) {
        String filePath = null;
    	
        Integer registeredId;

        try {
            registeredId = Integer.parseInt(location);
            ImageRegisterMap registerMap = (ImageRegisterMap)session.getAttribute("imageRegisterMap");
            filePath = registerMap.lookupRegisteredPath(registeredId);

        } catch (Exception e) {
        	logger.error(e.getClass()+" thrown trying to get the filePath for registeredId: "+location);
        	logger.error("This exception stack trace only shown when debug logging is enabled");
        	logger.debug(e);
        	// Do nothing here.  The resulting null filePath will cause a message in the log below
        }
          	
        return filePath;
    }
}
