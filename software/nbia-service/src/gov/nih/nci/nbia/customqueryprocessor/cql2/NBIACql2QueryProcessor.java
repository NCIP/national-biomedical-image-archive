/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.customqueryprocessor.cql2;

import org.cagrid.cql2.CQLAssociatedObject;
import org.cagrid.cql2.CQLAttribute;
import org.cagrid.cql2.CQLQuery;
import org.cagrid.cql2.CQLGroup;
import org.cagrid.cql2.CQLTargetObject;
import org.cagrid.cql2.GroupLogicalOperator;
import org.cagrid.cql2.results.CQLQueryResults;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.sdkquery4.processor2.SDK4CQL2QueryProcessor;
import gov.nih.nci.cagrid.introduce.servicetools.security.SecurityUtils;
import org.cagrid.cql.utilities.CQL2SerializationUtil;
import java.util.List;

import gov.nih.nci.ncia.domain.TrialDataProvenance;
import gov.nih.nci.nbia.util.CSMUtil;


public class NBIACql2QueryProcessor extends SDK4CQL2QueryProcessor {
	/**
	 * This method modifies the CQLQuery to apply the filtering
	 **/
	public CQLQueryResults processQuery(CQLQuery cqlQuery) throws QueryProcessingException {
		try {
			String userDN = SecurityUtils.getCallerIdentity();

			System.out.println("userDN:"+userDN);
			if(userDN==null) {
				return processQuery(cqlQuery,
						            new PublicTrialDataProvenance());
			}
			else {
				return processQuery(cqlQuery,
						            new AuthenticatedTrialDataProvenance(userDN));
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new QueryProcessingException(ex);
		}
	}

	///////////////////////////////////////////////////PRIVATE//////////////////////////////////////////////////////////

    private final static String IMAGE_DOMAIN = "gov.nih.nci.ncia.domain.Image";
    private final static String SERIES_DOMAIN = "gov.nih.nci.ncia.domain.Series";
    private final static String STUDY_DOMAIN = "gov.nih.nci.ncia.domain.Study";
    private final static String PATIENT_DOMAIN = "gov.nih.nci.ncia.domain.Patient";

	/**
	 * Process a CQL query according to the authorization level of the
	 * specified user i.e. through a secure grid connection with an authenticated user
	 */
	private CQLQueryResults processQuery(CQLQuery cqlQuery,
			                             TrialDataProvenanceFilter trialDataProvenanceFilter) throws Exception {


		List<TrialDataProvenance> authorizedTdpList = trialDataProvenanceFilter.getDataFilter();
		dumpList(authorizedTdpList);
 		if(authorizedTdpList.size() > 0){
 			CQLGroup tdpGroup = CQL2QueryUtil.convertTDPToCQL(authorizedTdpList);
 			cqlQuery = applyFilter(cqlQuery, tdpGroup);

 			System.out.println(CQL2SerializationUtil.serializeCql2Query(cqlQuery));
 		}
 		else {
 			return new CQLQueryResults();
 		}

		return super.processQuery(cqlQuery);
	}

	  /**
     * This method goes through the hierarchy chain of the object to apply the
     * right association and group for each one and modify it to include the
     * filter where clause. The modified CQLQuery is returned because calling
     * the super class processQuery method
     */
	private static CQLQuery applyFilter(CQLQuery cqlQuery,
                                       CQLGroup tdpGroup) {
    	CQLTargetObject cqlTarget = cqlQuery.getCQLTargetObject();


    	//AssociationPopulationSpecification - to pull in associated objects

    	CQLAssociatedObject tdpAssociation = new CQLAssociatedObject();
        tdpAssociation.setClassName("gov.nih.nci.ncia.domain.TrialDataProvenance");
        tdpAssociation.setEndName("dataProvenance");//used to be setRoleName
        tdpAssociation.setCQLGroup(tdpGroup);

        CQLAssociatedObject seriesAssociation = new CQLAssociatedObject();
        seriesAssociation.setClassName(SERIES_DOMAIN);
        seriesAssociation.setEndName("series");

        CQLAssociatedObject studyAssociation = new CQLAssociatedObject();
        studyAssociation.setClassName(STUDY_DOMAIN);
        studyAssociation.setEndName("study");

        CQLAssociatedObject patientAssociation = new CQLAssociatedObject();
        patientAssociation.setClassName(PATIENT_DOMAIN);
        patientAssociation.setEndName("patient");

        if (cqlTarget.getClassName().equalsIgnoreCase(IMAGE_DOMAIN)) {
            patientAssociation.setCQLAssociatedObject(tdpAssociation);
            studyAssociation.setCQLAssociatedObject(patientAssociation);
            seriesAssociation.setCQLAssociatedObject(studyAssociation);

            cqlTarget = mergeAssociationIntoQuery(cqlQuery, seriesAssociation);
        }
        else
        if (cqlTarget.getClassName().equalsIgnoreCase(SERIES_DOMAIN)) {
            patientAssociation.setCQLAssociatedObject(tdpAssociation);
            studyAssociation.setCQLAssociatedObject(patientAssociation);

            cqlTarget = mergeAssociationIntoQuery(cqlQuery, studyAssociation);
        }
        else
        if (cqlTarget.getClassName().equalsIgnoreCase(STUDY_DOMAIN)) {
            patientAssociation.setCQLAssociatedObject(tdpAssociation);

            cqlTarget = mergeAssociationIntoQuery(cqlQuery, patientAssociation);
        }
        else
        if (cqlTarget.getClassName().equalsIgnoreCase(PATIENT_DOMAIN)) {
            cqlTarget = mergeAssociationIntoQuery(cqlQuery, tdpAssociation);

        }
        else
        if (cqlTarget.getClassName().equalsIgnoreCase("gov.nih.nci.ncia.domain.Annotation")) {
            patientAssociation.setCQLAssociatedObject(tdpAssociation);
            studyAssociation.setCQLAssociatedObject(patientAssociation);
            seriesAssociation.setCQLAssociatedObject(studyAssociation);

            cqlTarget = mergeAssociationIntoQuery(cqlQuery, seriesAssociation);
        }
        else
        if (cqlTarget.getClassName().equalsIgnoreCase("gov.nih.nci.ncia.domain.TrialDataProvenance")) {
        	//cqlTarget.setCQLGroup(tdpGroup);
        }

        cqlQuery.setCQLTargetObject(cqlTarget);

        CQL2QueryUtil.dumpCQLQuery(cqlQuery);

        return cqlQuery;
    }


    private static boolean targetHasNoGroupButHasAssociation(CQLTargetObject cqlTarget) {
    	CQLAssociatedObject assoc = cqlTarget.getCQLAssociatedObject();
        CQLGroup group = cqlTarget.getCQLGroup();

        return group == null && assoc != null;
    }


    private static boolean targetHasAGroup(CQLTargetObject cqlTarget) {
        CQLGroup group = cqlTarget.getCQLGroup();
        return group != null;
    }


    private static boolean targetHasNoGroupNorAssociation(CQLTargetObject cqlTarget) {
    	CQLAssociatedObject assoc = cqlTarget.getCQLAssociatedObject();
        CQLGroup group = cqlTarget.getCQLGroup();

        return group == null && assoc == null;
    }


    /**
     * This method modify the input CQLQuery to include the where clause created
     * in the convertTDPToCQL method and return the modified target
     */
    private static CQLTargetObject mergeAssociationIntoQuery(CQLQuery cqlQuery,
                                                             CQLAssociatedObject association) {

        CQLTargetObject cqlTarget = cqlQuery.getCQLTargetObject();
        CQLAssociatedObject targetAssociation = cqlTarget.getCQLAssociatedObject();
        CQLGroup targetGroup = cqlTarget.getCQLGroup();

        if (targetHasNoGroupButHasAssociation(cqlTarget)) {
            CQLGroup newTargetGroup =
            	CQL2QueryUtil.newGroupWithAndedAssociations(targetAssociation, association);

            cqlTarget.setCQLAssociatedObject(null);
            cqlTarget.setCQLGroup(newTargetGroup);
        }
        else
        if (targetHasAGroup(cqlTarget)) {
            GroupLogicalOperator groupLogicRelation = targetGroup.getLogicalOperation();

            if (groupLogicRelation.getValue().equalsIgnoreCase("OR")) {
                CQLGroup newTargetGroup = CQL2QueryUtil.newAndGroupWithSubgroup(targetGroup);

                newTargetGroup.setCQLAssociatedObject(new CQLAssociatedObject[] {association});
                cqlTarget.setCQLGroup(newTargetGroup);
            }
            else {
            	CQL2QueryUtil.addAssociationToGroup(targetGroup, association);
                cqlTarget.setCQLGroup(targetGroup);
            }
        }
        else
        if (targetHasNoGroupNorAssociation(cqlTarget)) {
        	CQLAttribute attr = cqlTarget.getCQLAttribute();

            if (attr == null) {
                cqlTarget.setCQLAssociatedObject(association);
            }
            else {
                CQLGroup newTargetGroup =
                	CQL2QueryUtil.newAndGroupWithAssociationAndAttribute(association, attr);

                cqlTarget.setCQLGroup(newTargetGroup);
                cqlTarget.setCQLAttribute(null);
            }
        }
        return cqlTarget;
    }

	private static void dumpList(List<TrialDataProvenance> list) {
		System.out.println("Authorized TDP*************");
		for(TrialDataProvenance o : list) {
			System.out.println(o.getProject()+"//"+o.getSiteName());
		}
	}


	private static interface TrialDataProvenanceFilter {
		public List<TrialDataProvenance> getDataFilter() throws Exception;

	}

	private static class AuthenticatedTrialDataProvenance implements TrialDataProvenanceFilter {
		public AuthenticatedTrialDataProvenance(String theUsername) {
			this.username = theUsername;
		}

		public List<TrialDataProvenance> getDataFilter() throws Exception {

			return CSMUtil.getDataFilterByUserName(username);
		}

		private String username;
	}

	private static class PublicTrialDataProvenance implements TrialDataProvenanceFilter {

		public List<TrialDataProvenance> getDataFilter() throws Exception {
			String publicGroupName = "PUBLIC-GRID";// NBIAServiceConfiguration.getConfiguration().getPublicGroup();
			return CSMUtil.getDataFilterByGroupName(publicGroupName);
		}
	}
}
