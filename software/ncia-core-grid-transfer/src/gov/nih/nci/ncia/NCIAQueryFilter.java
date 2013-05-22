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
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.ncia.domain.TrialDataProvenance;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElementPrivilegeContext;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * nimpy: The lifecycle of this object is not quite clear.  Need to be clearer
 * about what values should be cached in the singleton instance, and
 * what is/should be recomputed per method invocation
 *
 * @author lethai
 */
public class NCIAQueryFilter {
    //why list??? is there ordering?
    public static List<TrialDataProvenance> getDataFilterByUserName(String username) throws CSException {
        List<TrialDataProvenance> tdpList = new ArrayList<TrialDataProvenance>();

        Set<ProtectionElementPrivilegeContext> userProtectionElementPrivilegeContext =
            CSMUtil.findUserProtectionElementPrivilegeContext(username);

        Iterator<ProtectionElementPrivilegeContext> userProtectionElementPrivilegeContextIter =
            userProtectionElementPrivilegeContext.iterator();

        while (userProtectionElementPrivilegeContextIter.hasNext()) {
            ProtectionElementPrivilegeContext protectionElementPrivilegeContext =
                userProtectionElementPrivilegeContextIter.next();

            ProtectionElement pe = protectionElementPrivilegeContext.getProtectionElement();

            TrialDataProvenance trialDataProvenance = processProtectionElement(pe);

            if (trialDataProvenance != null){
                tdpList.add(trialDataProvenance);
            }
        }

        return tdpList;
    }

    /**
     * This method uses csmapi to retrieve PUBLIC-GRID group which contains all
     * the public collections and sites. Return list of TrialDataProvenance for
     * public project and site
     *
     * @return List<TrialDataProvenance>
     */
    public static List<TrialDataProvenance> getDataFilterByGroupName(String csmGroupName) throws Exception {
        List<TrialDataProvenance> tdp = new ArrayList<TrialDataProvenance>();

        UserProvisioningManager upm = (UserProvisioningManager) SecurityServiceProvider
                .getAuthorizationManager("NCIA");
        Group csmGroup = new Group();
        csmGroup.setGroupName(csmGroupName);

        System.out.println("Group name: " + csmGroupName);

        GroupSearchCriteria gsc = new GroupSearchCriteria(csmGroup);
        List<Group> pgrslt = upm.getObjects(gsc);
        Set peg;
        if (pgrslt.size() > 0) {
            Long publicGroupId = ((Group) pgrslt.get(0)).getGroupId();
            peg = upm
                    .getProtectionElementPrivilegeContextForGroup(publicGroupId
                            .toString());
        }
        else {
            throw new Exception("Couldnot find group name: "+ csmGroupName);
        }

        Iterator itrpeg = peg.iterator();

        while (itrpeg.hasNext()) {
            ProtectionElement pe = ((ProtectionElementPrivilegeContext) itrpeg
                    .next()).getProtectionElement();
            TrialDataProvenance tdptemp = processProtectionElement(pe);

            if (tdptemp != null){
                tdp.add(tdptemp);
            }
        }
        return tdp;
    }


    /**
     * This method converts the TrialDataProvenance list into CQL where clause
     */
    public static gov.nih.nci.cagrid.cqlquery.Group convertTDPToCQL(List<TrialDataProvenance> authorizedTdpList) {

        List<gov.nih.nci.cagrid.cqlquery.Group> projectAndSiteGroups =
            new ArrayList<gov.nih.nci.cagrid.cqlquery.Group>();

        for (TrialDataProvenance authorizedTdp : authorizedTdpList) {
            if(GridUtil.isMangledTdp(authorizedTdp)) {
                System.err.println("Skipping:"+authorizedTdp.getProject()+","+authorizedTdp.getSiteName());
                continue;
            }

            Attribute projectAtt = new Attribute();
            projectAtt.setName("project");
            projectAtt.setPredicate(Predicate.EQUAL_TO);
            projectAtt.setValue(authorizedTdp.getProject());

            Attribute siteAtt = new Attribute();
            siteAtt.setName("siteName");
            siteAtt.setPredicate(Predicate.EQUAL_TO);
            siteAtt.setValue(authorizedTdp.getSiteName());

            Attribute[] projectAndSiteAttributesArr = new Attribute[2];
            projectAndSiteAttributesArr[0] = projectAtt;
            projectAndSiteAttributesArr[1] = siteAtt;

            gov.nih.nci.cagrid.cqlquery.Group projectAndSiteGroup = new gov.nih.nci.cagrid.cqlquery.Group();
            projectAndSiteGroup.setAttribute(projectAndSiteAttributesArr);
            projectAndSiteGroup.setLogicRelation(LogicalOperator.AND);

            projectAndSiteGroups.add(projectAndSiteGroup);
        }

        gov.nih.nci.cagrid.cqlquery.Group oneOfTheProjectSitePairsGroup = new gov.nih.nci.cagrid.cqlquery.Group();
        oneOfTheProjectSitePairsGroup.setLogicRelation(LogicalOperator.OR);
        oneOfTheProjectSitePairsGroup.setGroup(projectAndSiteGroups.toArray(new gov.nih.nci.cagrid.cqlquery.Group[]{}));
        return oneOfTheProjectSitePairsGroup;
    }

    /**
     * This method goes through the hierarchy chain of the object to apply the
     * right association and group for each one and modify it to include the
     * filter where clause. The modified CQLQuery is returned because calling
     * the super class processQuery method
     */

    public static CQLQuery applyFilter(CQLQuery cqlQuery,
                                       gov.nih.nci.cagrid.cqlquery.Group tdpGroup) {
        gov.nih.nci.cagrid.cqlquery.Object cqlTarget = cqlQuery.getTarget();

        Association tdpAssociation = new Association();
        tdpAssociation.setName("gov.nih.nci.ncia.domain.TrialDataProvenance");
        tdpAssociation.setRoleName("dataProvenance");
        tdpAssociation.setGroup(tdpGroup);

        Association seriesAssociation = new Association();
        seriesAssociation.setName(SERIES_DOMAIN);
        seriesAssociation.setRoleName("series");

        Association studyAssociation = new Association();
        studyAssociation.setName(STUDY_DOMAIN);
        studyAssociation.setRoleName("study");

        Association patientAssociation = new Association();
        patientAssociation.setName(PATIENT_DOMAIN);
        patientAssociation.setRoleName("patient");

        if (cqlTarget.getName().equalsIgnoreCase(IMAGE_DOMAIN)) {
            patientAssociation.setAssociation(tdpAssociation);
            studyAssociation.setAssociation(patientAssociation);
            seriesAssociation.setAssociation(studyAssociation);

            cqlTarget = mergeAssociationIntoQuery(cqlQuery, seriesAssociation);
        }
        else
        if (cqlTarget.getName().equalsIgnoreCase(SERIES_DOMAIN)) {
            patientAssociation.setAssociation(tdpAssociation);
            studyAssociation.setAssociation(patientAssociation);

            cqlTarget = mergeAssociationIntoQuery(cqlQuery, studyAssociation);
        }
        else
        if (cqlTarget.getName().equalsIgnoreCase(STUDY_DOMAIN)) {
            patientAssociation.setAssociation(tdpAssociation);

            cqlTarget = mergeAssociationIntoQuery(cqlQuery, patientAssociation);
        }
        else
        if (cqlTarget.getName().equalsIgnoreCase(PATIENT_DOMAIN)) {
            cqlTarget = mergeAssociationIntoQuery(cqlQuery, tdpAssociation);

        }
        else
        if (cqlTarget.getName().equalsIgnoreCase("gov.nih.nci.ncia.domain.Annotation")) {
            patientAssociation.setAssociation(tdpAssociation);
            studyAssociation.setAssociation(patientAssociation);
            seriesAssociation.setAssociation(studyAssociation);

            cqlTarget = mergeAssociationIntoQuery(cqlQuery, seriesAssociation);
        }
        else
        if (cqlTarget.getName().equalsIgnoreCase("gov.nih.nci.ncia.domain.TrialDataProvenance") && cqlQuery.getTarget().getGroup() == null ) {
       		cqlTarget.setGroup(tdpGroup);
        }

        cqlQuery.setTarget(cqlTarget);

        GridUtil.dumpCQLQuery(cqlQuery);

        return cqlQuery;
    }

    ////////////////////////////////////PRIVATE/////////////////////////////////////////

    private final static String delimiter = "//";

    private final static String NCIA_PROJECT = "NCIA.PROJECT";
    private final static String NCIA_DP_SITE_NAME = "DP_SITE_NAME";
    private final static String IMAGE_DOMAIN = "gov.nih.nci.ncia.domain.Image";
    private final static String SERIES_DOMAIN = "gov.nih.nci.ncia.domain.Series";
    private final static String STUDY_DOMAIN = "gov.nih.nci.ncia.domain.Study";
    private final static String PATIENT_DOMAIN = "gov.nih.nci.ncia.domain.Patient";


    private NCIAQueryFilter() {
    }

    private static boolean targetHasNoGroupButHasAssociation(gov.nih.nci.cagrid.cqlquery.Object cqlTarget) {
        Association assoc = cqlTarget.getAssociation();
        gov.nih.nci.cagrid.cqlquery.Group group = cqlTarget.getGroup();

        return group == null && assoc != null;
    }


    private static boolean targetHasAGroup(gov.nih.nci.cagrid.cqlquery.Object cqlTarget) {
        gov.nih.nci.cagrid.cqlquery.Group group = cqlTarget.getGroup();
        return group != null;
    }


    private static boolean targetHasNoGroupNorAssociation(gov.nih.nci.cagrid.cqlquery.Object cqlTarget) {
        Association assoc = cqlTarget.getAssociation();
        gov.nih.nci.cagrid.cqlquery.Group group = cqlTarget.getGroup();

        return group == null && assoc == null;
    }


    private static TrialDataProvenance processProtectionElementWithDelimeter(ProtectionElement pe) {
        String att = pe.getAttribute();
        String pename = pe.getProtectionElementName();
        int pos = att.indexOf(delimiter);
        TrialDataProvenance tdptemp = null;

        final int NCIA_PREFIX_OFFSET = 5;
        String beforeDelimeterinAttribute = att.substring(0, pos);
        String afterDelimeterinAttribute = att.substring(pos + 2);
        int delimeterPositionInPeName = pename.indexOf(delimiter);

        if (beforeDelimeterinAttribute.equalsIgnoreCase(NCIA_PROJECT)) {
            String projectValue = pename.substring(NCIA_PREFIX_OFFSET, delimeterPositionInPeName).trim();
            if(projectValue==null) {
                throw new RuntimeException("null projectname:"+pename);
            }

            tdptemp = new TrialDataProvenance();
            tdptemp.setProject(projectValue);
        }
        else {
            throw new RuntimeException("attribute should start with ncia.project");
        }

        if (afterDelimeterinAttribute.equalsIgnoreCase(NCIA_DP_SITE_NAME)) {
            String siteValue = (pename.substring(delimeterPositionInPeName + 2)).trim();
            if(siteValue==null) {
                throw new RuntimeException("null siteValue:"+pename);
            }
            //when does this happen?
            if (tdptemp == null) {
                tdptemp = new TrialDataProvenance();
            }
            tdptemp.setSiteName(siteValue);
        }
        else {
            throw new RuntimeException("attribute should end with ncia.dp_site_name");
        }
        return tdptemp;
    }

    /**
     * Given a CSM Protection Element, interpret our screwy smoke signals in the PE
     * to construct a TrialDataProvenance object that would match up.
     */
    private static TrialDataProvenance processProtectionElement(ProtectionElement pe) {
        String att = pe.getAttribute();
        int pos = att.indexOf(delimiter);
        if (pos < 0) {
            return null;
        }
        else  {
            return processProtectionElementWithDelimeter(pe);
        }
    }


    /**
     * This method modify the input CQLQuery to include the where clause created
     * in the convertTDPToCQL method and return the modified target
     */
    private static gov.nih.nci.cagrid.cqlquery.Object mergeAssociationIntoQuery(CQLQuery cqlQuery,
                                                                                 Association association) {

        gov.nih.nci.cagrid.cqlquery.Object cqlTarget = cqlQuery.getTarget();
        Association targetAssociation = cqlTarget.getAssociation();
        gov.nih.nci.cagrid.cqlquery.Group targetGroup = cqlTarget.getGroup();

        if (targetHasNoGroupButHasAssociation(cqlTarget)) {
            gov.nih.nci.cagrid.cqlquery.Group newTargetGroup =
                GridUtil.newGroupWithAndedAssociations(targetAssociation, association);

            cqlTarget.setAssociation(null);
            cqlTarget.setGroup(newTargetGroup);
        }
        else
        if (targetHasAGroup(cqlTarget)) {
            LogicalOperator groupLogicRelation = targetGroup.getLogicRelation();

            if (groupLogicRelation.getValue().equalsIgnoreCase("OR")) {
                gov.nih.nci.cagrid.cqlquery.Group newTargetGroup = GridUtil.newAndGroupWithSubgroup(targetGroup);

                newTargetGroup.setAssociation(new Association[] {association});
                cqlTarget.setGroup(newTargetGroup);
            }
            else {
                GridUtil.addAssociationToGroup(targetGroup, association);
                cqlTarget.setGroup(targetGroup);
            }
        }
        else
        if (targetHasNoGroupNorAssociation(cqlTarget)) {
            Attribute attr = cqlTarget.getAttribute();

            if (attr == null) {
                cqlTarget.setAssociation(association);
            }
            else {
                gov.nih.nci.cagrid.cqlquery.Group newTargetGroup =
                    GridUtil.newAndGroupWithAssociationAndAttribute(association, attr);

                cqlTarget.setGroup(newTargetGroup);
                cqlTarget.setAttribute(null);
            }
        }
        return cqlTarget;
    }
}