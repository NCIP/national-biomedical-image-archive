/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

public class RemoteSiteDTO {

    private String name;
    private String url;

    private boolean accessible;
    private boolean fromGridNodeTable;
    private String description;
    public RemoteSiteDTO() { }

    public RemoteSiteDTO(String url, String name) {
        this.url = url;
        this.name = name;
    }

    /**
     * This bit is set when the remote site is considered "live"
     * or not.  When it's not live, it won't be used as part of
     * a remote search.
     */
    public boolean isAccessible() {
        return accessible;
    }
    public void setAccessible(boolean available) {
        this.accessible = available;
    }
    
    /**
     * This is the name of the remote site.  Although it's
     * not clear if/how this is enforced (in the grid_node table?)
     * this name should be unique among remote sites.
     */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFromGridNodeTable() {
		return fromGridNodeTable;
	}

	public void setFromGridNodeTable(boolean fromGridNodeTable) {
		this.fromGridNodeTable = fromGridNodeTable;
	}
	public void setDescription(String desc) {
		this.description = desc;
	}

	public String getDescription() {
		return this.description;
	}

	@Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(this.name == null && ((RemoteSiteDTO)obj).name == null) {
            return true;
        }
        if(this.name.equals(((RemoteSiteDTO)obj).name)) {
            return true;
        }
        return false;
    }
	
	public int hashCode() {
	    int hash = getName().hashCode();    
	   
	    return hash;
	  }
}
