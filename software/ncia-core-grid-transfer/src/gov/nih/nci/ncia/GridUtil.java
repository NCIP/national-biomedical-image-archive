/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia;

import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.ncia.domain.TrialDataProvenance;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.encoding.SerializationException;

public class GridUtil {
	public static void addAssociationToGroup(gov.nih.nci.cagrid.cqlquery.Group group,
			                                 Association newAssociation) {
		Association[] groupAssociations = group.getAssociation();

		if (groupAssociations != null) {
			groupAssociations = GridUtil.copyArrayAndAddElement(groupAssociations,
					                                            newAssociation);
		}
		else {
			groupAssociations = new Association[1];
			groupAssociations[0] = newAssociation;
		}
		group.setAssociation(groupAssociations);

	}

	public static Group newAndGroupWithAssociationAndAttribute(Association association,
	                                                           Attribute attribute) {
    	Attribute attributes[] = new Attribute[1];
		attributes[0] = attribute;

		Group andGroup = new Group();
		andGroup.setLogicRelation(LogicalOperator.AND);

		Association[]associations = new Association[1];
		associations[0] = association;

		andGroup.setAssociation(associations);
		andGroup.setAttribute(attributes);

		return andGroup;
	}
	public static Group newAndGroupWithSubgroup(Group subgroup) {
		gov.nih.nci.cagrid.cqlquery.Group andGroup = new gov.nih.nci.cagrid.cqlquery.Group();
		andGroup.setLogicRelation(LogicalOperator.AND);

		gov.nih.nci.cagrid.cqlquery.Group nestedGroup[] = new gov.nih.nci.cagrid.cqlquery.Group[1];
		nestedGroup[0] = subgroup;

		andGroup.setGroup(nestedGroup);
		return andGroup;
	}

	public static Association[] copyArrayAndAddElement(Association[] srcArray, Association newElement) {
		//Java 5 does not permit the construction of generic arrays.
		Association[] newArray = new Association[srcArray.length+1];
		for (int j = 0; j < srcArray.length; j++) {
			newArray[j] = srcArray[j];
		}
		newArray[srcArray.length] = newElement;
		return newArray;
	}

	public static Group newGroupWithAndedAssociations(Association a1,
			                                          Association a2) {
		Group newGroup = new Group();
		newGroup.setLogicRelation(LogicalOperator.AND);
		Association associationsToAnd[] = new Association[2];
		associationsToAnd[0] = a1;
		associationsToAnd[1] = a2;
		newGroup.setAssociation(associationsToAnd);
		return newGroup;
	}

	public static Attribute retrieveProjectAttribute(
			gov.nih.nci.cagrid.cqlquery.Group constraintGroup) {
		Attribute constraintGroupAttr = null;
		Attribute[] constraintGroupAttrs = constraintGroup.getAttribute();
		if (constraintGroupAttrs != null) {
			for (int k = 0; k < constraintGroupAttrs.length; k++) {
				constraintGroupAttr = constraintGroupAttrs[k];

				if (constraintGroupAttr.getName().equalsIgnoreCase("project")) {
					break;
				}
			}
		}
		return constraintGroupAttr;
	}

	public static boolean isFound(TrialDataProvenance tdp,
			                      List<TrialDataProvenance> tdpList){
		
		if( tdp == null ) {
			return false;
		}

        String queryProject = tdp.getProject();
		String querySiteName = tdp.getSiteName();

		logger.info("query project and site: " + queryProject + " " + querySiteName);
		logger.info("public list: size = " + tdpList.size());

		for(TrialDataProvenance t : tdpList){
			logger.info("public trialdataprovenance; project: " + t.getProject() + " site name: " + t.getSiteName());
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

	public static boolean isProjectFound(CQLQuery cqlQuery, List<TrialDataProvenance> tdpList){
		gov.nih.nci.cagrid.cqlquery.Group constraintGroup = cqlQuery.getTarget().getGroup();
		
		if( constraintGroup != null ) {
			Attribute constraintGroupAttr = retrieveProjectAttribute(constraintGroup);
			if (constraintGroupAttr != null) {
				String queryProject = constraintGroupAttr.getValue();
				System.out.println("Value " + constraintGroupAttr.getValue());
				for (TrialDataProvenance t : tdpList) {
					String project = t.getProject();

					if (project.equals(queryProject)) {
						return true;
					}
				}
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


	public static void dumpCQLQuery(CQLQuery cqlQuery) {
		try {
			logger.debug(ObjectSerializer.toString(cqlQuery,
					                               new QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery",
							                                 "CQLQuery")));
		} catch (SerializationException e) {
			logger.error(e);
		}
	}

	private static Logger logger = Logger.getLogger(GridUtil.class);

}
