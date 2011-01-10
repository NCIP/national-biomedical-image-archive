package gov.nih.nci.ncia.util;

import gov.nih.nci.ncia.dao.ImageDAO;
import gov.nih.nci.ncia.dto.ImageSecurityDTO;
import gov.nih.nci.ncia.security.AuthorizationManager;
import gov.nih.nci.ncia.security.NCIASecurityManager.RoleType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class ThumbnailUtil {
    /**
     * For a given DICOM file path, look on the file system to see if
     * the MIRC thumbnail exists.  If so, pass it back.  If not, look
     * for a CTP thumbnail and pass it back.
     */
	public static File deduceThumbnailFromDicomFile(File dicomFile) {
		String dicomFilePath = dicomFile.getAbsolutePath();
		assert dicomFilePath.endsWith(".dcm");

		String mircThumbnailFilePath = dicomFilePath.substring(0,
				                                               dicomFilePath.lastIndexOf(".dcm")) + "_base.jpeg";
		//this isn't necessarily the thumbanil string
		//512, 512, and -1 can be configured in CTP's config.xml
		String ctpThumbnailFilePath = mircThumbnailFilePath.substring(0,
                										mircThumbnailFilePath.indexOf("_base"))+
                										".dcm[512;512;-1].jpeg";
		//this isn't necessarily the thumbanil string
		//512, 512, and -1 can be configured in CTP's config.xml
		String ctpNewThumbnailFilePath = mircThumbnailFilePath.substring(0,
														mircThumbnailFilePath.indexOf("_base"))+
														".dcm[512;512;-1][0].jpeg";

        File mircThumbnailFile = new File(mircThumbnailFilePath);
        File ctpThumbnailFile = new File (ctpThumbnailFilePath);
        File ctpNewThumbnailFile = new File (ctpNewThumbnailFilePath);
        
        if (mircThumbnailFile.exists()) {
			return mircThumbnailFile;
		}
		else if (ctpThumbnailFile.exists())
		{
			return ctpThumbnailFile;
		}
		else {
			return ctpNewThumbnailFile;
		}

	}
    /**
     * For a given DICOM file path, look on the file system to see if
     * the MIRC thumbnail exists.  If so, pass it back.  If not, look
     * for a CTP thumbnail and pass it back.
     */
	public static File deduceThumbnailFromDicomFile(File dicomFile, String frameNum) {
		String dicomFilePath = dicomFile.getAbsolutePath();
		assert dicomFilePath.endsWith(".dcm");

		String mircThumbnailFilePath = dicomFilePath.substring(0,
				                                               dicomFilePath.lastIndexOf(".dcm")) + "_base.jpeg";

        File mircThumbnailFile = new File(mircThumbnailFilePath);
		if (!mircThumbnailFile.exists()) {
			//this isn't necessarily the thumbanil string
			//512, 512, and -1 can be configured in CTP's config.xml
			String ctpThumbnailFilePath = mircThumbnailFilePath.substring(0,
					                                                      mircThumbnailFilePath.indexOf("_base"))+
					                                                      ".dcm[512;512;-1]["+frameNum+"].jpeg";
			return new File(ctpThumbnailFilePath);
		}
		else {
			return mircThumbnailFile;
		}

	}
	/**
	 * Returns the local File for the specified sop instance uid.
	 *
	 * If the series is not visible, or the image is not part of
	 * a public series, null is returned.
	 */
    public static File retrieveImageFile(AuthorizationManager authorizationManager,
    		                             String imageSopInstanceUid) {
    	ImageDAO imageDAO = (ImageDAO)SpringApplicationContext.getBean("imageDAO");
    	ImageSecurityDTO imageSecurityDTO = imageDAO.findImageSecurity(imageSopInstanceUid);
    	if(imageSecurityDTO==null) {
    		return null;
    	}
    	if(!imageSecurityDTO.getSeriesVisibility()) {
    		System.out.println("not visible:....");
    		return null;
   		}

		System.out.println("dto"+imageSecurityDTO.getProject()+","+
			     imageSecurityDTO.getSite()+","+
			     imageSecurityDTO.getSsg());

    	if(hasAccess(authorizationManager,
    			     imageSecurityDTO.getProject(),
    			     imageSecurityDTO.getSite(),
    			     imageSecurityDTO.getSsg())) {
    		return new File(imageSecurityDTO.getFileName());
    	}
    	else {
    		System.out.println("not public:....");

    		return null;
    	}
    }


    /**
     * Take a JPEG file and stream the bytes to a HTTP response (and set
     * the content type as image/jpeg.
     */
    public static void writeJpegFile(File thumbnailFile,
    		                         HttpServletResponse response) throws IOException {
        OutputStream os = null;
        FileInputStream fin = null;
    	try {
            // actually write the image to the output stream
            response.setContentType("image/jpeg");
            os = response.getOutputStream();

            fin = new FileInputStream(thumbnailFile);
            IOUtils.copy(fin, os);
        }
    	catch (Exception e) {
            System.out.println("File: " + thumbnailFile + " not found on server");
            //ok to ignore this I guess.  broken images will show which is ok i guess.
        }
        finally {
            if (os != null) {
                os.flush();
            }

            if (fin != null) {
                fin.close();
            }
        }
    }

    //unify this with download servlet.  redundant.
    private static boolean hasAccess(AuthorizationManager am,
    		                         String project,
                                     String siteName,
                                     String ssg) {
        try {
           List<SiteData> siteList = am.getAuthorizedSites(RoleType.READ);
           List<String> ssgList = am.getAuthorizedSeriesSecurityGroups(RoleType.READ);

            for (SiteData siteData : siteList)
            {
                 if ((siteData.getCollection().equals(project) &&
                        siteData.getSiteName().equals(siteName))) {
                    if (!StringUtil.isEmpty(ssg)) {
                        for (String authSsg : ssgList) {
                            if (authSsg.equals(ssg)) {
                                System.out.println("returning true...");
                                return true;
                            }
                        }
                    }
                    else
                    if (StringUtil.isEmpty(ssg)) {
                        System.out.println("returning true");
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("exception: " + e.getMessage() + " returning false.");
            return false;
        }

        System.out.println("no exception reaching the end, returning... false");
        return false;
    }
}
