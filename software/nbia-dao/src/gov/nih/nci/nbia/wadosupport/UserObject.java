package gov.nih.nci.nbia.wadosupport;
import gov.nih.nci.nbia.util.SiteData;

import java.util.*;
public class UserObject {
private List <SiteData>authorizedSites;
private Date lastUpdated;
public List<SiteData> getAuthorizedSites() {
	return authorizedSites;
}
public void setAuthorizedSites(List<SiteData> authorizedSites) {
	this.authorizedSites = authorizedSites;
}
public Date getLastUpdated() {
	return lastUpdated;
}
public void setLastUpdated(Date lastUpdated) {
	this.lastUpdated = lastUpdated;
}

}
