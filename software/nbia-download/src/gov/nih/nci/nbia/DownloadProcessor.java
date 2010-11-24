package gov.nih.nci.nbia;

import gov.nih.nci.ncia.dao.AnnotationDAO;
import gov.nih.nci.nbia.dao.ImageDAO;
import gov.nih.nci.ncia.dto.AnnotationDTO;
import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.ncia.dao.DownloadDataDAO;
import gov.nih.nci.ncia.security.AuthorizationManager;
import gov.nih.nci.ncia.security.NCIASecurityManager;
import gov.nih.nci.ncia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.ncia.util.NCIAConfig;
import gov.nih.nci.ncia.util.SiteData;
import gov.nih.nci.ncia.util.SpringApplicationContext;
import java.util.List;


/**
 * @author lethai
 *
 */
public class DownloadProcessor {
    public List<ImageDTO> process(String seriesInstanceUid, String sopUids){
        ImageDAO imageDAO = (ImageDAO)SpringApplicationContext.getBean("imageDAO2");
        List<ImageDTO> imageResults = imageDAO.findImagesBySeriesUid(seriesInstanceUid, sopUids);
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
                                System.out.println("returning true...");
                                return true;
                            }
                        }
                    } else if (isBlank(ssg)) {
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