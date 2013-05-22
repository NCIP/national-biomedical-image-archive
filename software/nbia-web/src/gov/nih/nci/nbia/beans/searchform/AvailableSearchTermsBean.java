/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchform;

import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.lookup.LookupManager;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.UsAvailableSearchTerms;
import gov.nih.nci.ncia.search.NBIANode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AvailableSearchTermsBean {

	////////////////////// Public /////////////////////////////

	/**
	 * Return the available search terms for all NBIA nodes that the system recoginzes.
	 * This is cached per session.
	 */
	public List<AvailableSearchTermsWrapper> getNode() {

		if (wrapperList == null) {
			wrapperList = new ArrayList<AvailableSearchTermsWrapper>();
			searchableNodes = lookupManager.getSearchableNodes().entrySet();
			searchableNodesForUs = lookupManager.getSearchableNodesForUs().entrySet();
			for(Iterator<Map.Entry<NBIANode, AvailableSearchTerms>> iterator = searchableNodes.iterator();
			    iterator.hasNext() ; )
			{
			     Map.Entry<NBIANode, AvailableSearchTerms> entry =iterator.next();
			     NBIANode key = (NBIANode)entry.getKey();
			     AvailableSearchTerms value = (AvailableSearchTerms)entry.getValue();

			     AvailableSearchTermsWrapper wrapper = new AvailableSearchTermsWrapper();
			     wrapper.setNode(key);
			     wrapper.setTerms(value);

			     for(Iterator<Map.Entry<NBIANode, UsAvailableSearchTerms>> iteratorUs = searchableNodesForUs.iterator();
				    iteratorUs.hasNext() ; ){
			    	 Map.Entry<NBIANode, UsAvailableSearchTerms> usEntry =iteratorUs.next();
				     NBIANode usKey = (NBIANode)usEntry.getKey();
				     UsAvailableSearchTerms usValue = (UsAvailableSearchTerms)usEntry.getValue();
				     if(usKey.compareTo(key)==0) {
				    	 wrapper.setNewTerms(usValue);
				     }
				}

			     wrapperList.add(wrapper);
		    }
			
			Collections.sort(wrapperList);
			return wrapperList;
		}
		else {
			return wrapperList;
		}
	}

	//////////////////////////// Private /////////////////////
	private Set<Map.Entry<NBIANode, AvailableSearchTerms>> searchableNodes;
	private Set<Map.Entry<NBIANode, UsAvailableSearchTerms>> searchableNodesForUs;

	private List<AvailableSearchTermsWrapper> wrapperList;

	private LookupManager lookupManager = BeanManager.getSearchWorkflowBean().getLookupMgr();
}
