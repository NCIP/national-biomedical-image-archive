package gov.nih.nci.nbia.restUtil;

import gov.nih.nci.nbia.util.SiteData;

import java.util.List;
public class SiteUtil {
	private List<SiteData> authorizedSiteData;
	private long created;
	public long getCreated() {
		return created;
	}
	public void setCreated(long created) {
		this.created = created;
	}
	public List<SiteData> getAuthorizedSites() {
		return authorizedSiteData;
	}
	public void setAuthorizedSites(List<SiteData> authorizedSites) {
		this.authorizedSiteData = authorizedSites;
	}
	
	
}
