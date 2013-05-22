/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia;

import gov.nih.nci.nbia.dao.AnnotationDAO;
import gov.nih.nci.nbia.dao.DownloadDataDAO;
import gov.nih.nci.nbia.dao.ImageDAO2;
import gov.nih.nci.nbia.dto.AnnotationDTO;
import gov.nih.nci.nbia.dto.ImageDTO2;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.NCIASecurityManager;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.List;


/**
 * @author lethai
 *
 */
public class DownloadProcessor {
    public List<ImageDTO2> process(String seriesInstanceUid, String sopUids){
        ImageDAO2 imageDAO = (ImageDAO2)SpringApplicationContext.getBean("imageDAO2");
        List<ImageDTO2> imageResults = imageDAO.findImagesBySeriesUid(seriesInstanceUid, sopUids);
        return imageResults;
    }

    public List<AnnotationDTO> process(String seriesInstanceUid){
        List<AnnotationDTO> annoResults=null;

	    AnnotationDAO annotationDAO = (AnnotationDAO)SpringApplicationContext.getBean("annotationDAO2");
        annoResults = annotationDAO.findAnnotationBySeriesUid(seriesInstanceUid);

        return annoResults;
    }

    public void recordDownload(String seriesInstanceUid, String loginName, Long size) throws Exception{

        DownloadDataDAO downloadDAO = (DownloadDataDAO)SpringApplicationContext.getBean("downloadDataDAO");
        downloadDAO.record(seriesInstanceUid, loginName, "DM", size);
    }

    /**
     * check to see if user has authorized to see data. Return true if yes, otherwise, no
     * @param userId
     * @param project
     * @param siteName
     * @param ssg
     */
    public boolean hasAccess(String userId,
                             String password,
                             String project,
                             String siteName,
                             String ssg) {
        try {
           if(!checkCredentials(userId, password)) {
               return false;
           }
           AuthorizationManager am = new AuthorizationManager(userId);
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

        } catch (Exception e) {
			e.printStackTrace();
            return false;
        }

        return false;
    }

    private static boolean isGuestEnabled() {
        String guestAccount  = NCIAConfig.getEnabledGuestAccount();
        if(guestAccount.equalsIgnoreCase("yes")){
            return true;
        }else{
            return false;
        }
    }

    private static boolean checkCredentials(String userId,
                                            String password) throws Exception {
        boolean guestLogin = false;
        if(isGuestEnabled() && userId.equals(NCIAConfig.getGuestUsername())) {
             guestLogin = true;
        }
        NCIASecurityManager mgr = (NCIASecurityManager)SpringApplicationContext.getBean("nciaSecurityManager");
        if(!guestLogin && !mgr.login(userId, password)) {
             return false;
        }
        return true;
    }

    private boolean isBlank(String value) {
        return ((value == null) || value.equals("") || value.equals("\n") ||
        (value.length() == 0));
    }
}