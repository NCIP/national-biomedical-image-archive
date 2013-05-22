/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;
import java.io.Serializable;
/**
 * This represents an instance of NBIA (that can be searched).
 * 
 * <P>This object is immutable.  Keep it that way.
 * 
 * <P>NOTE: This is NOT serialized.
 * 
 * <p>Note: this class has a natural ordering that is inconsistent with equals.
 */
public class NBIANode implements Comparable<NBIANode>, 
                                 Serializable {

	public NBIANode(boolean isLocal, String displayName, String url) {
		this.isLocal = isLocal;
		this.displayName = displayName;
		this.url = url;
	}

	/**
	 * A short name describing this node.  Doesn't have to
	 * be unique among all nodes in the system.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Is this node for the appserver we are searching FROM.
	 */
	public boolean isLocal() {
		return isLocal;
	}

	/**
	 * This should be a unique URL for this node.  If local, this
	 * doesn't need to actually point to anything.  Otherwise...
	 * should point to the grid service for the node.
	 */
	public String getURL() {
		return url;
	}
	
	/**
	 * Order nodes by display name for convenience.
	 */
    public int compareTo(NBIANode obj) {
    	return this.displayName.compareTo(obj.getDisplayName());
    }
    
    public boolean equals(Object obj){
    	return this.url.equals(((NBIANode)obj).url);
    }
    /**
	 * Required by check style.
	 */
    public int hashCode(){
    	return this.url.hashCode();
    }    
    public String toString() {
    	return getURL()+","+getDisplayName()+", local:"+isLocal();
    }
	
	///////////////////////////////////////PRIVATE////////////////////////////////

	private String displayName;

	private boolean isLocal;
	
	private String url;
}
