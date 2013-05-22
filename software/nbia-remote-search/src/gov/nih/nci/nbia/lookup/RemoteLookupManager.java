/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.lookup;

import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.nbia.remotesearch.RemoteNodes;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.UsAvailableSearchTerms;
import gov.nih.nci.ncia.search.Manufacturer;
import gov.nih.nci.ncia.search.Model;
import gov.nih.nci.ncia.search.NBIANode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * This implementation of lookup manager consults the RemoteNodes object in order
 * to return the lookup info for all known remote searchable nodes.  It ignores
 * the "local" node.
 *
 * <p>The iteration logic in here is highly redundant, but need a
 * "function pointer" or interface which is sort of a pain in the butt
 */
public class RemoteLookupManager implements LookupManager {

	/**
	 * {@inheritDoc}
	 */
    public Map<NBIANode, AvailableSearchTerms> getSearchableNodes() {
    	Map<NBIANode, AvailableSearchTerms> allNodes = new HashMap<NBIANode, AvailableSearchTerms>();

    	RemoteNodes remoteNodes = RemoteNodes.getInstance();

    	Collection<RemoteNode> nodes = remoteNodes.getRemoteNodes();
    	for(RemoteNode node : nodes) {
    		AvailableSearchTerms availableSearchTerms = node.getAvailableSearchTerms();
    		allNodes.put(node, availableSearchTerms);
    	}

    	return allNodes;
    }

	/**
	 * {@inheritDoc}
	 */
    public Map<NBIANode, UsAvailableSearchTerms> getSearchableNodesForUs() {
    	Map<NBIANode, UsAvailableSearchTerms> allNodes = new HashMap<NBIANode, UsAvailableSearchTerms>();

    	RemoteNodes remoteNodes = RemoteNodes.getInstance();

    	Collection<RemoteNode> nodes = remoteNodes.getRemoteNodes();
    	for(RemoteNode node : nodes) {
    		UsAvailableSearchTerms usAvailableSearchTerms = node.getUsAvailableSearchTerms();
    		if (usAvailableSearchTerms!=null){
    			allNodes.put(node, usAvailableSearchTerms);
    		}
    	}

    	return allNodes;
    }
	/**
	 * {@inheritDoc}
	 */
    public List<String> getModality() {
    	RemoteNodes remoteNodes = RemoteNodes.getInstance();

    	Set<String> allModalities = new HashSet<String>();

    	Collection<RemoteNode> nodes = remoteNodes.getRemoteNodes();
    	for(RemoteNode node : nodes) {
    		AvailableSearchTerms availableSearchTerms = node.getAvailableSearchTerms();

    		String[] modalities = availableSearchTerms.getModalities();
    		if(modalities!=null) {
    			allModalities.addAll(Arrays.asList(modalities));
    		}
    	}

    	return new ArrayList<String>(allModalities);
    }


	/**
	 * {@inheritDoc}
	 */
    public List<String> getAnatomicSite()  {
    	RemoteNodes remoteNodes = RemoteNodes.getInstance();

    	Set<String> allAnatomicSites = new HashSet<String>();

    	Collection<RemoteNode> nodes = remoteNodes.getRemoteNodes();
    	for(RemoteNode node : nodes) {
    		AvailableSearchTerms availableSearchTerms = node.getAvailableSearchTerms();

    		String[] anatomicSites = availableSearchTerms.getAnatomicSites();
    		if(anatomicSites!=null) {
    			allAnatomicSites.addAll(Arrays.asList(anatomicSites));
    		}
    	}

    	return new ArrayList<String>(allAnatomicSites);
    }

   	/**
	 * {@inheritDoc}
	 */
    public List<String> getUsMultiModality()  {
    	
	  	RemoteNodes remoteNodes = RemoteNodes.getInstance();

	   	Set<String> allUsMultiModalities = new HashSet<String>();

	   	Collection<RemoteNode> nodes = remoteNodes.getRemoteNodes();
	  	for (RemoteNode node : nodes) {
			UsAvailableSearchTerms availableSearchTerms = node
					.getUsAvailableSearchTerms();
			if (availableSearchTerms != null) {
				String[] usMultiModalities = availableSearchTerms
						.getUsMultiModalities();
				if (usMultiModalities != null) {
					allUsMultiModalities.addAll(Arrays
							.asList(usMultiModalities));
				}
			}
		}
     	return new ArrayList<String>(allUsMultiModalities);

     }


	/**
	 * {@inheritDoc}
	 */
    public List<String> getSearchCollection()  {
    	RemoteNodes remoteNodes = RemoteNodes.getInstance();

    	Set<String> allCollections = new HashSet<String>();

    	Collection<RemoteNode> nodes = remoteNodes.getRemoteNodes();
    	for(RemoteNode node : nodes) {
    		AvailableSearchTerms availableSearchTerms = node.getAvailableSearchTerms();

    		String[] collections = availableSearchTerms.getCollections();
    		if(collections!=null) {
    			allCollections.addAll(Arrays.asList(collections));
    		}
    	}

    	return new ArrayList<String>(allCollections);

    }


	/**
	 * {@inheritDoc}
	 */
    public List<String> getDICOMKernelType()  {
    	RemoteNodes remoteNodes = RemoteNodes.getInstance();

    	Set<String> allConvolutionKernels = new HashSet<String>();

    	Collection<RemoteNode> nodes = remoteNodes.getRemoteNodes();
    	for(RemoteNode node : nodes) {
    		AvailableSearchTerms availableSearchTerms = node.getAvailableSearchTerms();

    		String[] convolutionKernels = availableSearchTerms.getConvolutionKernels();
    		if(convolutionKernels!=null) {
    			allConvolutionKernels.addAll(Arrays.asList(convolutionKernels));
    		}
    	}

    	return new ArrayList<String>(allConvolutionKernels);

    }


	/**
	 * {@inheritDoc}
	 */
    public Map<String, Map<String, Set<String>>> getManufacturerModelSoftwareItems() {
    	RemoteNodes remoteNodes = RemoteNodes.getInstance();

    	Map<String, Map<String, Set<String>>> equipmentMap = new HashMap<String, Map<String, Set<String>>>();

    	Collection<RemoteNode> nodes = remoteNodes.getRemoteNodes();
    	for(RemoteNode node : nodes) {
    		AvailableSearchTerms availableSearchTerms = node.getAvailableSearchTerms();

    		//EquipmentUtil.convert
    		Manufacturer[] equipment = availableSearchTerms.getEquipment();
    		if(equipment==null) {
    			continue;
    		}

    		for(Manufacturer manufacturer : equipment) {

    		    Map<String, Set<String>> modelMap = equipmentMap.get(manufacturer.getName());
    		    if(modelMap==null) {
    		    	modelMap = new HashMap<String, Set<String>>();
    		    	equipmentMap.put(manufacturer.getName(), modelMap);
    		    }

    		    for(Model model : manufacturer.getModels()) {
    		    	Set<String> versions = modelMap.get(model.getName());
    		    	if(versions==null) {
    		    		versions = new HashSet<String>();
    		    		modelMap.put(model.getName(), versions);
    		    	}
    		    	versions.addAll(Arrays.asList(model.getVersions()));
    		    }
    		}
    	}

    	return equipmentMap;
    }
}
