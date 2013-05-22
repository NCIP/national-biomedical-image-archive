/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.lookup;

import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.UsAvailableSearchTerms;
import gov.nih.nci.ncia.search.NBIANode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This implementation of lookup manager aggregates the lookup info
 * from the local node plus all of the other remote nodes.
 */
public class CompositeLookupManager implements LookupManager {
	/**
	 * Constructor.
	 *
	 * @param authorizedCollections all collections to show for the local
	 *                              lookup manager.
	 */
	public CompositeLookupManager(Collection<String> authorizedCollections) {
		lookupManagers.add(new LookupManagerImpl(authorizedCollections));
		lookupManagers.add(new RemoteLookupManager());
	}


	/**
	 * {@inheritDoc}
	 */
    public Map<NBIANode, AvailableSearchTerms> getSearchableNodes() {

    	Map<NBIANode, AvailableSearchTerms> allNodes = new HashMap<NBIANode, AvailableSearchTerms>();

    	for(LookupManager lookupManager : lookupManagers) {
    		Map<NBIANode, AvailableSearchTerms> searchableNodes = lookupManager.getSearchableNodes();
    	    for(Map.Entry<NBIANode, AvailableSearchTerms> entry : searchableNodes.entrySet()) {
    	    	NBIANode node = entry.getKey();
    	    	allNodes.put(node, entry.getValue());
    	    }
    	}
    	return allNodes;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public Map<NBIANode, UsAvailableSearchTerms> getSearchableNodesForUs() {

    	Map<NBIANode, UsAvailableSearchTerms> allNodes = new HashMap<NBIANode, UsAvailableSearchTerms>();

    	for(LookupManager lookupManager : lookupManagers) {
    		Map<NBIANode, UsAvailableSearchTerms> searchableNodes = lookupManager.getSearchableNodesForUs();
    	    for(Map.Entry<NBIANode, UsAvailableSearchTerms> entry : searchableNodes.entrySet()) {
    	    	NBIANode node = entry.getKey();
    	    	allNodes.put(node, entry.getValue());
    	    }
    	}
    	return allNodes;
    }

	/**
	 * {@inheritDoc}
	 */
    public List<String> getModality() {

    	Set<String> allModalities = new HashSet<String>();

    	for(LookupManager lookupManager : lookupManagers) {
    		List<String> temp = lookupManager.getModality();
    	    allModalities.addAll(temp);
    	}

    	return new ArrayList<String>(allModalities);
    }


	/**
	 * {@inheritDoc}
	 */
    public List<String> getAnatomicSite() {

    	Set<String> allAnatomicSites = new HashSet<String>();

    	for(LookupManager lookupManager : lookupManagers) {
    	    allAnatomicSites.addAll(lookupManager.getAnatomicSite());
    	}

    	return new ArrayList<String>(allAnatomicSites);
    }

    /**
	 * {@inheritDoc}
	 */
	public List<String> getUsMultiModality() {
		Set<String> allUsMultiModalities = new HashSet<String>();

    	for(LookupManager lookupManager : lookupManagers) {
    		allUsMultiModalities.addAll(lookupManager.getUsMultiModality());
    	}

    	return new ArrayList<String>(allUsMultiModalities);
    }


	/**
	 * {@inheritDoc}
	 */
    public List<String> getSearchCollection()  {
    	Set<String> allCollections = new HashSet<String>();

    	for(LookupManager lookupManager : lookupManagers) {
    	    allCollections.addAll(lookupManager.getSearchCollection());
    	}

    	return new ArrayList<String>(allCollections);

    }


	/**
	 * {@inheritDoc}
	 */
    public List<String> getDICOMKernelType() {
    	Set<String> allModalities = new HashSet<String>();

    	for(LookupManager lookupManager : lookupManagers) {
    	    allModalities.addAll(lookupManager.getDICOMKernelType());
    	}

    	return new ArrayList<String>(allModalities);
    }


	/**
	 * {@inheritDoc}
	 */
    public Map<String, Map<String, Set<String>>> getManufacturerModelSoftwareItems() {
    	Map<String, Map<String, Set<String>>> allEquipmentMap = new HashMap<String, Map<String, Set<String>>>();

    	for(LookupManager lookupManager : lookupManagers) {
    		Map<String, Map<String, Set<String>>> equipmentMap = lookupManager.getManufacturerModelSoftwareItems();

    		for(Map.Entry<String, Map<String, Set<String>>> entry : equipmentMap.entrySet()) {
    			String manuName = entry.getKey();

    			Map<String, Set<String>> allModelMap = allEquipmentMap.get(manuName);
    			if(allModelMap==null) {
    				allEquipmentMap.put(manuName, entry.getValue());
    			}
    			else {
    				mergeModelMap(allModelMap, entry.getValue());
    			}
    		}
    	}

    	return allEquipmentMap;
    }


    ///////////////////////////////////////////////PRIVATE//////////////////////////////////////////

    /**
     * All the lookup managers to delegate to.  Typically just two: remote and local.
     */
    private Collection<LookupManager> lookupManagers = new ArrayList<LookupManager>();

    /**
     * For the equipment map, add the entries from the srcMap into the destMap
     * being careful to be additive instead of replacing.
     */
    private static void mergeModelMap(Map<String, Set<String>> destMap,
    		                          Map<String, Set<String>> srcMap) {
    	for(Map.Entry<String,Set<String>> entry : srcMap.entrySet()) {
    		String srcModelName = entry.getKey();
    		Set<String> srcVersions = entry.getValue();

    		Set<String> destVersions = destMap.get(srcModelName);
    		if(destVersions==null) {
    			destMap.put(srcModelName, srcVersions);
    		}
    		else {
    			destVersions.addAll(srcVersions);
    		}
    	}
    }
}
