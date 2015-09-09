package gov.nih.nci.nbia.restUtil;

import java.util.List;
public class CollectionUtil {

	private long created;
	List<String> authorizedCollections;
	public long getCreated() {
		return created;
	}
	public void setCreated(long created) {
		this.created = created;
	}
	public List<String> getAuthorizedCollections() {
		return authorizedCollections;
	}
	public void setAuthorizedCollections(List<String> authorizedCollections) {
		this.authorizedCollections = authorizedCollections;
	}
	
	
}
