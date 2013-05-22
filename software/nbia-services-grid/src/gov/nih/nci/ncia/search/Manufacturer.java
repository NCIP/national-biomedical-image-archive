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
 * This object represents an equipment manufacturer.  It is the name of
 * manufacturer plus a collection of models by the manufacturer.
 * 
 * <P>This object should be considered immutable.... even though
 * it doesn't enforce that.
 * 
 * <P><b>WARNING!</b> This object is serialized so if you change it, you risk
 * breaking remote search and the grid interface
 */
public class Manufacturer implements Serializable {


    public Manufacturer() {
    }



    /**
     * Gets the name value for this Manufacturer.
     * 
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * Sets the name value for this Manufacturer.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Gets the models value for this Manufacturer.
     * 
     * @return models
     */
    public Model[] getModels() {
        return models;
    }


    /**
     * Sets the models value for this Manufacturer.
     * 
     * @param models
     */
    public void setModels(Model[] models) {
        this.models = models;
    }
    

    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */      
    public Model getModels(int i) {
        return this.models[i];
    }

    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */      
    public void setModels(int i, Model _value) {
        this.models[i] = _value;
    }
    
    ///////////////////////////////////////////////PRIVATE////////////////////////
    private String name;
    private Model[] models;    
}
