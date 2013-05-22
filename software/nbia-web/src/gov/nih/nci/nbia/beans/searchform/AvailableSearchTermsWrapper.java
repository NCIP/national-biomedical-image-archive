/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchform;

import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.UsAvailableSearchTerms;
import gov.nih.nci.ncia.search.Manufacturer;
import gov.nih.nci.ncia.search.NBIANode;

public class AvailableSearchTermsWrapper implements Comparable<AvailableSearchTermsWrapper>{

	private NBIANode node;
	private AvailableSearchTerms terms;
	private UsAvailableSearchTerms newTerms;	
	private boolean expandable = true;

	public boolean isExpandable() {
		return expandable;
	}

	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
	}

	public String getModalities() {
		return commaSeparatedList(terms.getModalities());
	}
	
	public String getUsMultiModalities() {
		if (newTerms != null){
		return commaSeparatedList(newTerms.getUsMultiModalities());
		}
		else {
			return "NA";
		}
	}
	 
	public String getAnatomicSites()
	{
		return commaSeparatedList(terms.getAnatomicSites());
	}

	public String getCollections()
	{
		return commaSeparatedList(terms.getCollections());
	}

	public String getConvolutionKernels()
	{

		return commaSeparatedList(terms.getConvolutionKernels());
	}

	public String getEquipment()
	{
		Manufacturer[] equipments = terms.getEquipment();
		StringBuilder value = new StringBuilder("");
		if (equipments != null)
		{
			for (Manufacturer s : equipments)
			{
				value.append(s.getName());
				value.append(COMMA_PLUS_SPACE);
			}
			if(equipments.length>=1) {
				return value.substring(0,value.lastIndexOf(","));
			}
		}
		return value.toString();
	}


	public NBIANode getNode() {
		return node;
	}
	public void setNode(NBIANode node) {
		this.node = node;
	}
	public AvailableSearchTerms getTerms() {
		return terms;
	}
	public void setTerms(AvailableSearchTerms terms) {
		this.terms = terms;
	}
	public UsAvailableSearchTerms getNewTerms() {
		return newTerms;
	}
	public void setNewTerms(UsAvailableSearchTerms newTerms) {
		this.newTerms = newTerms;
	}
	/**
	 * Order nodes by display name for convenience.
	 */
    public int compareTo(AvailableSearchTermsWrapper obj) {
    	return node.getDisplayName().compareTo(obj.node.getDisplayName());
    }

	private static final String COMMA_PLUS_SPACE = ",  ";

	private static String commaSeparatedList(String[] values) {
		StringBuilder string = new StringBuilder("");
		if (values != null)
		{
			for (String s : values)
			{
				string.append(s);
				string.append(COMMA_PLUS_SPACE);
			}
			if(values.length>=1) {
				return string.substring(0,string.lastIndexOf(","));
			}
		}
		return string.toString();
	}
}
