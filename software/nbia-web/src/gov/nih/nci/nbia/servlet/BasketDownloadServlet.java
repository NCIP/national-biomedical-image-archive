/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.servlet;

import gov.nih.nci.nbia.util.NCIAConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasketDownloadServlet extends HttpServlet {


    /**
     * This used to be in dataBasketBean.completeDownload.
     * Moved here to avoid Tomahawk filter which loads everything in memory
     **/
	public void doGet(HttpServletRequest request,
			          HttpServletResponse response) throws ServletException,
			                                               IOException {

		String fileName = (String)request.getSession().getAttribute("httpDownloadZipFileName");
		if(fileName==null) {
			response.sendError(500);
			return;
		}

		response.setContentType("application/zip");
        response.setHeader("Content-Disposition",
                           "attachment;filename=\"" + fileName + "\"");

        File f = new File(NCIAConfig.getZipLocation() + "/" + fileName);
        long fileLength = f.length();
        InputStream in = new BufferedInputStream(new FileInputStream(f));

        // Set header so that browser will know the file size
        response.addHeader("Content-Length", String.valueOf(fileLength));


        ServletOutputStream out = response.getOutputStream();

        byte[] buf = new byte[4 * 1024]; // 4K buffer
        int bytesRead;

        try {
            while ((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }

            out.flush();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                	in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                response.sendError(500);
            }
        }
	}
}
