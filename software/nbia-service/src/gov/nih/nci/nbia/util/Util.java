/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.ncia.domain.TrialDataProvenance;

import java.util.List;

public class Util {
	public static boolean isFound(TrialDataProvenance tdp, 
			                      List<TrialDataProvenance> tdpList){

        String queryProject = tdp.getProject();
		String querySiteName = tdp.getSiteName();
		
		
		for(TrialDataProvenance t : tdpList){
			if(isMangledTdp(t)) {
				continue;
			}
			String site = t.getSiteName();
			String project = t.getProject();
			
			if(project.equals(queryProject) && site.equals(querySiteName)){

				return true;
			}
		}
		return false;
	}
	
	
	
	public static boolean isMangledTdp(TrialDataProvenance tdp) {
		return isEmpty(tdp.getProject()) ||
		       isEmpty(tdp.getSiteName());
	}
	
	public static boolean isEmpty(String s) {
		return s==null || s.length()==0;
	}
}
