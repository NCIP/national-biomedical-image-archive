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
 * This object corrals all the searchable terms for a given node.
 * 
 * <P>This object should be considered immutable.... even though
 * it doesn't enforce that.
 * 
 * <P><b>WARNING!</b> This object is serialized so if you change it, you risk
 * breaking remote search and the grid interface
 */
public class AvailableSearchTerms implements Serializable {


    public AvailableSearchTerms() {
    }

    /**
     * Gets the collections value for this AvailableSearchTerms.
     */
    public String[] getCollections() {
        return collections;
    }


    /**
     * Sets the collections value for this AvailableSearchTerms.
     */
    public void setCollections(String[] collections) {
        this.collections = collections;
    }

    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */
    public String getCollections(int i) {
        return this.collections[i];
    }

    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */    
    public void setCollections(int i, String _value) {
        this.collections[i] = _value;
    }


    /**
     * Gets the convolutionKernels value for this AvailableSearchTerms.
     */
    public String[] getConvolutionKernels() {
        return convolutionKernels;
    }


    /**
     * Sets the convolutionKernels value for this AvailableSearchTerms.
     */
    public void setConvolutionKernels(String[] convolutionKernels) {
        this.convolutionKernels = convolutionKernels;
    }

    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */    
    public String getConvolutionKernels(int i) {
        return this.convolutionKernels[i];
    }

    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */    
    public void setConvolutionKernels(int i, String _value) {
        this.convolutionKernels[i] = _value;
    }    

    /**
     * Gets the modalities value for this AvailableSearchTerms.
     */
    public String[] getModalities() {
        return modalities;
    }


    /**
     * Sets the modalities value for this AvailableSearchTerms.
     */
    public void setModalities(String[] modalities) {
        this.modalities = modalities;        
    }

    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */    
    public String getModalities(int i) {
        return this.modalities[i];
    }

    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */    
    public void setModalities(int i, String _value) {
        this.modalities[i] = _value;
    }  
    
    /**
     * Gets the anatomicSites value for this AvailableSearchTerms.
     *
     * @return anatomicSites
     */
    public String[] getAnatomicSites() {
        return anatomicSites;
    }


    /**
     * Sets the anatomicSites value for this AvailableSearchTerms.
     */
    public void setAnatomicSites(String[] anatomicSites) {
        this.anatomicSites = anatomicSites;
    }
    

    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */    
    public String getAnatomicSites(int i) {
        return this.anatomicSites[i];
    }

    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */    
    public void setAnatomicSites(int i, String _value) {
        this.anatomicSites[i] = _value;
    }      

    /**
     * Gets the equipment value for this AvailableSearchTerms.
     */
    public Manufacturer[] getEquipment() {
        return equipment;
    }


    /**
     * Sets the equipment value for this AvailableSearchTerms.
     */
    public void setEquipment(Manufacturer[] equipment) {
        this.equipment = equipment;
    }
    
    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */    
    public void setEquipment(int index, Manufacturer line ) {
        this.equipment[index] = line;
    }


    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */    
    public Manufacturer getEquipment(int index ) {
        return this.equipment[index];
    }
    

    ///////////////////////////////////////PRIVATE////////////////////////////////////////
    
    private String[] collections;
    private String[] convolutionKernels;
    private String[] modalities;
    private String[] anatomicSites;
    private Manufacturer[] equipment;
}
