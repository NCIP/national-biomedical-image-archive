/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.ncia.domain.TrialDataProvenance;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElementPrivilegeContext;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CSMUtil {
	public static Set<ProtectionElementPrivilegeContext> findUserProtectionElementPrivilegeContext(String username) throws CSException {
		
		UserProvisioningManager upm = (UserProvisioningManager) SecurityServiceProvider.getAuthorizationManager("NCIA");	
		
		User userToSearch = new User();
		userToSearch.setLoginName(username);
		
		UserSearchCriteria userSearchCriteria = new UserSearchCriteria(userToSearch);
		List<User> foundUsers = upm.getObjects(userSearchCriteria);
		
		if (foundUsers.size() > 0) {
			Long userId = ((User) foundUsers.get(0)).getUserId();
			
			return upm.getProtectionElementPrivilegeContextForUser(userId.toString());
		} 
		else {
			throw new CSException("Could not find user name: "+ username);
		}		
	}
	
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
    
    
    ////////////////////////////////////PRIVATE/////////////////////////////////////////
    
    private final static String delimiter = "//";

    private final static String NCIA_PROJECT = "NCIA.PROJECT";
    private final static String NCIA_DP_SITE_NAME = "DP_SITE_NAME";
    
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
}
