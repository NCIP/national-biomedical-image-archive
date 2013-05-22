/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.18  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.lookup;

import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.UsAvailableSearchTerms;
import gov.nih.nci.ncia.search.NBIANode;

import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * This Interface will act as a support for the UI in attaining
 * specific information required for dropdowns and other selection
 * menus
 */
public interface LookupManager {

	/**
	 * Return all modalities that could be searched on.... in no particular order even though it is a List.
	 * Examples: CT, MR, DX, etc.
	 * 
	 * <p>This should be an aggregation of all values from getSearchableNodes
	 */
    public List<String> getModality();

    /**
     * Return all anatomic sites that could be searched on.... in no particular order
     * Examples: HEAD, CHEST, etc.  
	 * 
	 * <p>This should be an aggregation of all values from getSearchableNodes
     */
    public List<String> getAnatomicSite();
    
    /**
     * Return all ultrasound image type that could be searched on.... in no particular order
     * Examples: color doppler, 2D Imaging, etc.  
	 * 
	 * <p>This should be an aggregation of all values from getSearchableNodes
     */
    public List<String> getUsMultiModality();

    /**
     * Return all collections or projects that could be searched on.... in no particular order
     * Examples: IDRI, LIDC, etc. 
	 * 
	 * <p>This should be an aggregation of all values from getSearchableNodes
     */    
    public List<String> getSearchCollection();

    /**
     * Return all convolution kernels that could be searched on.... in no particular order
     * Examples: B31s, B41f, etc.
	 * 
	 * <p>This should be an aggregation of all values from getSearchableNodes 
     */    
    public List<String> getDICOMKernelType();

    /**
     * All the equipment that can be searched against.  This will include
     * all equipment aggregated from the getSearchableNodes method.
     * 
     * <p>manufacturer name->(model name->{versions}
	 * 
	 * <p>This should be an aggregation of all values from getSearchableNodes
     */
    public Map<String, Map<String, Set<String>>> getManufacturerModelSoftwareItems();
    
    
    /**
     * All the searchable nodes available from this system.
     * If a system is configured for only local search, then
     * this will have one entry for the local node, that will
     * reflect in value the other methods in this object.
     * 
     * If a system has remote nodes, this could have N entries,
     * one per remote node, plus the local node.
     */
    public Map<NBIANode, AvailableSearchTerms> getSearchableNodes();
    
    /**
     * All the searchable nodes available from this system.
     * If a system is configured for only local search, then
     * this will have one entry for the local node, that will
     * reflect in value the other methods in this object.
     * 
     * If a system has remote nodes, this could have N entries,
     * one per remote node, plus the local node.
     */
    public Map<NBIANode, UsAvailableSearchTerms> getSearchableNodesForUs();
}
