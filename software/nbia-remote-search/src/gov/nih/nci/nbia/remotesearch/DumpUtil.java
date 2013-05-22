/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch;

import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.Manufacturer;
import gov.nih.nci.ncia.search.Model;

class DumpUtil {
	public static void dumpArray(String label, Object[] arr) {
		if(arr==null) {
			System.out.println(label+" is null");
		}
		else {
			System.out.println(label+":"+arr.length);

		}
	}
	
	public static void debug(AvailableSearchTerms availableSearchTerms) {
		if(availableSearchTerms==null) {
			System.out.println("availableSearchTerms0:null");
			return;
		}
		System.out.println("availableSearchTerms0:"+availableSearchTerms);
		
		dumpArray("availableSearchTerms1",availableSearchTerms.getModalities());
		dumpArray("availableSearchTerms2",availableSearchTerms.getAnatomicSites());
		dumpArray("availableSearchTerms3",availableSearchTerms.getCollections());
		dumpArray("availableSearchTerms4",availableSearchTerms.getConvolutionKernels());
		dumpArray("availableSearchTerms5",availableSearchTerms.getEquipment());

		Manufacturer[] m1 = availableSearchTerms.getEquipment();
		if(m1==null) {
			return;
		}
		for(Manufacturer m : m1) {
			System.out.println(m.getName());
			for(Model z : m.getModels()) {
				System.out.println("="+z.getName());
				for(String s : z.getVersions()) {
					System.out.println("=="+s);
				}
			}
		}		
	}
}
