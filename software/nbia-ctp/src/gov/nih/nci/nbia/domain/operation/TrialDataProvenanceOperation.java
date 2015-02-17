/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.internaldomain.TrialDataProvenance;
import gov.nih.nci.nbia.util.DicomConstants;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.Map;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class TrialDataProvenanceOperation extends DomainOperation implements TrialDataProvenanceOperationInterface{
	
	public TrialDataProvenanceOperation() {
		
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Object validate(Map numbers) throws Exception {
		String temp;
	    String hql = "from TrialDataProvenance as tdp where ";
	
	    TrialDataProvenance tdp = (TrialDataProvenance)SpringApplicationContext.getBean("trialDataProvenance");
	
	    try {
		    if ((temp = (String) numbers.get(DicomConstants.PROJECT_NAME)) != null) {
		        hql += ("lower(tdp.project) = '" + temp.trim().toLowerCase() +
		        "' and ");
		        tdp.setProject(temp.trim());
		    } else {
		        hql += "tdp.project is null and ";
		    }
		
		    if ((temp = (String) numbers.get(DicomConstants.SITE_ID)) != null) {
		        hql += ("tdp.dpSiteId = '" + temp.trim() + "' and ");
		        tdp.setDpSiteId(temp.trim());
		    } else {
		        hql += "tdp.dpSiteId is null and ";
		    }
		
		    if ((temp = (String) numbers.get(DicomConstants.SITE_NAME)) != null) {
		        hql += ("lower(tdp.dpSiteName) = '" + temp.trim().toLowerCase() +
		        "' ");
		        tdp.setDpSiteName(temp.trim());
		    } else {
		        hql += "tdp.dpSiteName is null ";
		    }
		
		    List ret = getHibernateTemplate().find(hql);
		    if(ret != null  && ret.size() > 0) {
		    	if (ret.size() == 1) {
		    		tdp = (TrialDataProvenance) ret.get(0);
		    	}
		    	else if (ret.size() > 1)
		    	{
		    		throw new Exception("Trial_Data_Provenance table (" 
		    				+ (String)numbers.get(DicomConstants.PROJECT_NAME) + "/" 
		    				+ (String) numbers.get(DicomConstants.SITE_NAME) 
		    				+") has duplicate records, please contact Data Team to fix data, then upload data again");
		    	}
		    }
	    }catch(Exception e) {
	    	log.error("Exception in TrialDataProvenanceOperation " + e);
	    	throw new Exception("Exception in TrialDataProvenanceOperation: " + e);
	    }
	
	    return tdp;		
	}

}
