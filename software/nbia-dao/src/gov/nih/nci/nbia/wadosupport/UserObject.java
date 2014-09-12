package gov.nih.nci.nbia.wadosupport;
import gov.nih.nci.nbia.util.SiteData;

import java.util.*;
public class UserObject {
private List <SiteData>authorizedSites;
private long lastUpdated;
public List<SiteData> getAuthorizedSites() {
	if ((lastUpdated+3600000)<System.currentTimeMillis()){
       return null;
	}
	return authorizedSites;
}
public void setAuthorizedSites(List<SiteData> authorizedSites) {
	this.authorizedSites = authorizedSites;
	lastUpdated = System.currentTimeMillis();
}


}
