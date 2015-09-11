package in.raster.oviyam.threed;

import in.raster.oviyam.ImageInfo;
import in.raster.oviyam.model.InstanceModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import org.jboss.system.server.ServerConfigLocator;

/**
 *
 * @author asgar
 */
@SuppressWarnings("serial")
public class CopyDicomFiles extends HttpServlet {

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String patID = request.getParameter("patientId");
        String studyUID = request.getParameter("studyUID");
        String seriesUID = request.getParameter("seriesUID");
        String dcmURL = request.getParameter("dcmURL");
        String wadoURL = request.getParameter("serverURL");
    	HttpSession session = request.getSession();
    	String oviyamId = (String)session.getAttribute("oviyamId");
    	String wadoLookupUrl = (String)session.getAttribute("wadoUrl");
        ImageInfo imageInfo = new ImageInfo();
		System.out.println("In CopyDicomFiles patID:"+patID+"-studyUID:"+studyUID+"-serverURL:"+dcmURL
				+"-oviyamId:"+oviyamId+"-wadoLookupUrl:"+wadoLookupUrl);
        imageInfo.callFindWithQuery(patID, studyUID, seriesUID, null, dcmURL, oviyamId, wadoLookupUrl);
        ArrayList<InstanceModel> instanceList = imageInfo.getInstancesList();

        //log.info("Instance count: " + instanceList.size());
        String dirPath = "";

        try {
            dirPath = ServerConfigLocator.locate().getServerHomeDir() + File.separator;
            dirPath += "DicomFiles" + File.separator + seriesUID;
            File tmpFile = new File(dirPath);
            if (!tmpFile.exists()) {
                tmpFile.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        wadoURL += "/wado?requestType=WADO&contentType=application/dicom&studyUID=" + studyUID + "&seriesUID=" + seriesUID;

        InputStream is = null;
        DicomInputStream dis;
        URL url;

        try {
            for (int i = 0; i < instanceList.size(); i++) {
                InstanceModel im = instanceList.get(i);

                String objectUID = im.getSopIUID();
                String UrlTmp = wadoURL + "&objectUID=" + objectUID + "&transferSyntax=1.2.840.10008.1.2.1";

                url = new URL(UrlTmp);
                is = url.openStream();

                dis = new DicomInputStream(is);
                DicomObject dcmObj = dis.readDicomObject();
                DicomElement imgType = dcmObj.get(Tag.ImageType);
                String image_type = "";
                if (imgType != null) {
                    image_type = new String(imgType.getBytes());
                    String[] imgTypes = image_type.split("\\\\");
                    if (imgTypes.length >= 3) {
                        image_type = imgTypes[2];
                    }
                }

                is.close();
                dis.close();

                if (image_type.equals("LOCALIZER")) {
                    continue;
                }

                String fileName = dirPath + File.separator + objectUID;
                FileOutputStream fos = new FileOutputStream(fileName);
                try {
                    url = new URL(UrlTmp);
                    is = url.openStream();
                    byte[] buffer = new byte[4096];
                    int bytes_read;

                    while ((bytes_read = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytes_read);
                    }

                } catch (Exception e) {
                    //log.error("Error while reading DICOM attributes " + e);
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (Exception ignore) {
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Exception ignore) {
                        }
                    }
                }

            }

        } catch (Exception ex) {
            //log.error(ex);
            ex.printStackTrace();
        }
        PrintWriter pw = response.getWriter();
        pw.write(dirPath);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
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
