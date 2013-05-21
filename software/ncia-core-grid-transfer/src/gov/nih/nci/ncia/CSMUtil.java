package gov.nih.nci.ncia;

import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElementPrivilegeContext;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;

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
}
