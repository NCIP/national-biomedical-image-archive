/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.searchresult;

import java.io.Serializable;


/**
 * This object represents an equipment model.  It is the name of
 * model plus a collection of versions of the model.
 * 
 * <P>This object should be considered immutable.... even though
 * it doesn't enforce that.
 * 
 * <P><b>WARNING!</b> This object is serialized so if you change it, you risk
 * breaking remote search and the grid interface
 */
public class Model implements Serializable {


    public Model() {
    }


    /**
     * Gets the name value for this Model.
     * 
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * Sets the name value for this Model.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Gets the versions value for this Model.
     * 
     * @return versions
     */
    public String[] getVersions() {
        return versions;
    }


    /**
     * Sets the versions value for this Model.
     * 
     * @param versions
     */
    public void setVersions(String[] versions) {
        this.versions = versions;
    }
    
    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */      
    public String getVersions(int i) {
        return this.versions[i];
    }

    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */      
    public void setVersions(int i, String _value) {
        this.versions[i] = _value;
    }    
    
    ////////////////////////////////////////PRIVATE////////////////////////////////////
    private String name;
    private String[] versions;    
}
