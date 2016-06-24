package gov.nih.nci.nbia.restUtil;





import gov.nih.nci.nbia.util.SiteData;

import java.util.*;
public class AuthorizationUtil {
	

    private static Map<String, SiteUtil> userSites = new HashMap<String, SiteUtil>();

	public static List<SiteData> getUserSiteData(String user){
		long now = System.currentTimeMillis();
		SiteUtil util = userSites.get(user);
		if (util==null)
		{
			return null;
		}
		else
		{
			if (now < util.getCreated())
			{
				return util.getAuthorizedSites();
			}
			else
			{
				return null;
			}
		}
	}
	public static void setUserSites(String user, List<SiteData> authorizedSites){
		long now = System.currentTimeMillis() + 600000L;
		SiteUtil util = new SiteUtil();
        util.setCreated(now);
        util.setAuthorizedSites(authorizedSites);
        userSites.put(user, util);
	}
	
}
