/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.customqueryprocessor.cql2;

import gov.nih.nci.ncia.domain.TrialDataProvenance;
import gov.nih.nci.nbia.util.Util;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.cagrid.cql.utilities.CQL2SerializationUtil;
import org.cagrid.cql2.AttributeValue;
import org.cagrid.cql2.BinaryPredicate;
import org.cagrid.cql2.CQLAssociatedObject;
import org.cagrid.cql2.CQLAttribute;
import org.cagrid.cql2.CQLGroup;
import org.cagrid.cql2.CQLQuery;
import org.cagrid.cql2.GroupLogicalOperator;



public class CQL2QueryUtil {
	
	
	/**
     * This method converts the TrialDataProvenance list into CQL where clause
     */
    public static CQLGroup convertTDPToCQL(List<TrialDataProvenance> authorizedTdpList) {

        List<CQLGroup> projectAndSiteGroups = new ArrayList<CQLGroup>();

        for (TrialDataProvenance authorizedTdp : authorizedTdpList) {
            if(Util.isMangledTdp(authorizedTdp)) {
                System.err.println("Skipping:"+authorizedTdp.getProject()+","+authorizedTdp.getSiteName());
                continue;
            }
            
            CQLAttribute projectAtt = new CQLAttribute();
            projectAtt.setName("project");
            projectAtt.setBinaryPredicate(BinaryPredicate.EQUAL_TO);
            projectAtt.setAttributeValue(new AttributeValue(null,null,null,null,null,authorizedTdp.getProject(),null));
        
            CQLAttribute siteAtt = new CQLAttribute();
            siteAtt.setName("siteName");
            siteAtt.setBinaryPredicate(BinaryPredicate.EQUAL_TO);
            siteAtt.setAttributeValue(new AttributeValue(null,null,null,null,null,authorizedTdp.getSiteName(),null));
    
            CQLAttribute[] projectAndSiteAttributesArr = new CQLAttribute[2];
            projectAndSiteAttributesArr[0] = projectAtt;
            projectAndSiteAttributesArr[1] = siteAtt;
            
            CQLGroup projectAndSiteGroup = new CQLGroup();
            projectAndSiteGroup.setCQLAttribute(projectAndSiteAttributesArr);
            projectAndSiteGroup.setLogicalOperation(GroupLogicalOperator.AND);
            
            projectAndSiteGroups.add(projectAndSiteGroup);
        }
        
        CQLGroup oneOfTheProjectSitePairsGroup = new CQLGroup();
        oneOfTheProjectSitePairsGroup.setLogicalOperation(GroupLogicalOperator.OR);        
        oneOfTheProjectSitePairsGroup.setCQLGroup(projectAndSiteGroups.toArray(new CQLGroup[]{}));
        return oneOfTheProjectSitePairsGroup;
    }

	public static void dumpCQLQuery(CQLQuery cqlQuery) {
		try {
			CQL2SerializationUtil.serializeCql2Query(cqlQuery);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void addAssociationToGroup(CQLGroup group,
			                                 CQLAssociatedObject newAssociation) {
		CQLAssociatedObject[] groupAssociations = group.getCQLAssociatedObject();

		if (groupAssociations != null) {
			groupAssociations = copyArrayAndAddElement(groupAssociations, 
					                                            newAssociation);
		} 
		else {
			groupAssociations = new CQLAssociatedObject[1];
			groupAssociations[0] = newAssociation;
		}		
		group.setCQLAssociatedObject(groupAssociations);

	}	

	public static CQLGroup newAndGroupWithAssociationAndAttribute(CQLAssociatedObject association, 
	                                                              CQLAttribute attribute) {
		CQLAttribute attributes[] = new CQLAttribute[1];
		attributes[0] = attribute;
				
		CQLGroup andGroup = new CQLGroup();
		andGroup.setLogicalOperation(GroupLogicalOperator.AND);
				
		CQLAssociatedObject[]associations = new CQLAssociatedObject[1];
		associations[0] = association;	
		
		andGroup.setCQLAssociatedObject(associations);
		andGroup.setCQLAttribute(attributes);		
		
		return andGroup;
	}	
	
	
	public static CQLGroup newAndGroupWithSubgroup(CQLGroup subgroup) {
		CQLGroup andGroup = new CQLGroup();
		andGroup.setLogicalOperation(GroupLogicalOperator.AND);
		
		CQLGroup nestedGroup[] = new CQLGroup[1];
		nestedGroup[0] = subgroup;
				
		andGroup.setCQLGroup(nestedGroup);
		return andGroup;
	}
	
	public static CQLAssociatedObject[] copyArrayAndAddElement(CQLAssociatedObject[] srcArray, 
			                                                   CQLAssociatedObject newElement) {
		//Java 5 does not permit the construction of generic arrays. 
		CQLAssociatedObject[] newArray = new CQLAssociatedObject[srcArray.length+1];
		for (int j = 0; j < srcArray.length; j++) {
			newArray[j] = srcArray[j];
		}
		newArray[srcArray.length] = newElement;
		return newArray;
	}
	
	public static CQLGroup newGroupWithAndedAssociations(CQLAssociatedObject a1, 
			                                             CQLAssociatedObject a2) {
		CQLGroup newGroup = new CQLGroup();
		newGroup.setLogicalOperation(GroupLogicalOperator.AND);
		CQLAssociatedObject associationsToAnd[] = new CQLAssociatedObject[2];
		associationsToAnd[0] = a1;
		associationsToAnd[1] = a2;
		newGroup.setCQLAssociatedObject(associationsToAnd);	
		return newGroup;
	}
	
	private static Logger logger = Logger.getLogger(CQL2QueryUtil.class);

	


}
