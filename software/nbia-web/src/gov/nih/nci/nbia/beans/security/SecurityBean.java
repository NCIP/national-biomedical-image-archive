/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.security;

import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.dao.LoginHistoryDAO;
import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.nbia.lookup.RESTUtil;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.NCIASecurityManager;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.apache.log4j.Logger;

/**
 * Session scope bean that allows for security to be implemented.
 *
 * @author shinohaa
 *
 */
public class SecurityBean {
	private static Logger logger = Logger.getLogger(SecurityBean.class);
	/**
	 * Current users username and password.
	 */
	private String username = "";
	private String password = "";
	private String email = "";
	private boolean firstTimeLogin = false;
	private String newPassword1="";
	private String newPassword2="";
	private static final String LOGIN_FAIL = "loginFail";
	private DefaultOAuth2AccessToken token;
	private static final String PASSWORD_FIELD_JSF_ID = null;//"MAINbody:sideBarView:loginForm:pass";
	                                                    //MAINbody:sideBarViewAsGuest:loginFormGuestEnabled:loginGuestEnabled:pass

	/**
	 * Flag that holds whether or not a user is logged in.
	 */
	private boolean loggedIn = false;

	/**
	 * Authorization manager for this user
	 */
	private AuthorizationManager authMgr;

	/**
	 * Attempts to log a user into the website.
	 */
	public String login() throws Exception {
		return login(username, password);
	}

	/**
	 * Attempts to change the password.
	 */
	public void changePassword() {
		if (newPassword1.equals(newPassword2)) {
			try {
				NCIASecurityManager sm = (NCIASecurityManager)SpringApplicationContext.getBean("nciaSecurityManager");
				sm.modifyPasswordForNewUser(username, newPassword1);
				firstTimeLogin=false;
			}
			catch (Exception e) {
				firstTimeLogin = true;
				if (e.getMessage() != null)
					MessageUtil.addErrorMessage(PASSWORD_FIELD_JSF_ID, e.getMessage()+".");
				else e.printStackTrace();
			}
		}
		else {
			firstTimeLogin=true;
			MessageUtil.addErrorMessage(PASSWORD_FIELD_JSF_ID,
					                    "passwordsMismatch");
		}
		loggedIn = false;

	}

	/**
	 * AuthorizationManager is expensive to create.
	 * Small optimization to expose this so other dont have to.
	 * Wouldn't normally be keen on managed bean sharing stuffvoid
	 * but it's already rampant... so let's do it one more time! :)
	 */
	public AuthorizationManager getAuthorizationManager() {
		return this.authMgr;
	}

	/**
	 * Returns a list of collection names that the user is allowed to view.
	 */
	public List<String> getAuthorizedCollections() {
		if (authMgr != null) {
			return authMgr.getAuthorizedCollections();
		} else {
			return new ArrayList<String>();
		}
	}

	/**
	 * Returns a list of collection names that the user is allowed to view for a
	 * particular role.
	 */
	public List<String> getAuthorizedCollections(RoleType role) {

		if (authMgr != null) {
			return authMgr.getAuthorizedCollections(role);
		} else {
			return new ArrayList<String>();
		}
	}

	/**
	 * Returns a list of authorized series security groups for the specified
	 * role.
	 *
	 * @param role
	 */
	public List<String> getAuthorizedSeriesSecurityGroups(RoleType role) {
		if (authMgr != null) {
			return authMgr.getAuthorizedSeriesSecurityGroups(role);
		} else {
			return new ArrayList<String>();
		}
	}

	/**
	 * Handles authorization for sites and series security groups
	 *
	 * @param query
	 */
	public void authorizeSitesAndSSGs(DICOMQuery query) {
		authMgr.authorizeSitesAndSSGs(query);
	}

	/**
	 * Handles authorization for collections groups
	 *
	 * @param query
	 */
	public void authorizeCollections(DICOMQuery query) {
		if (!authMgr.authorizeCollections(query)) {
			// User is not authorized to view one or more of the collections, so
			// create a message
			MessageUtil.addInfoMessage(
					"MAINbody:searchMenuForm:unauthorizedCollections",
					"unallowedCollection");
		}
	}

	/**
	 * Uses CSM to validate whether or not a user has provided the correct
	 * credentials.
	 *
	 * @param uname
	 * @param pass
	 */
	public String login(String uname, String pass) throws Exception {
		username = uname;
		password = pass;
		boolean isInLDAP = false;
		boolean isInLocal = false;
		loggedIn = false;

		// Check for Special chars in the user name
		if(!StringUtil.removeIllegitCharacters(username).equalsIgnoreCase(username)){
			MessageUtil.addErrorMessage(PASSWORD_FIELD_JSF_ID,
					                    "securityLogin");
    		loginFailure = true;

			return LOGIN_FAIL;
    	}

		AnonymousLoginBean anonymousLoginBean = BeanManager.getAnonymousLoginBean();
		try {
			NCIASecurityManager sm = (NCIASecurityManager)SpringApplicationContext.getBean("nciaSecurityManager");
			if( anonymousLoginBean.getIsGuestEnabled() && username.equals(anonymousLoginBean.getGuestUserName())){
				//bypass authentication for guest user
				anonymousLoginBean.setGuestLoggedIn(true);
				token=RESTUtil.getToken(username, password);
				isInLDAP = true;
				logger.info("bypass authentication for guest user");
			}else{
				isInLDAP = sm.login(uname, pass);
				token=RESTUtil.getToken(username, password);
				System.out.println(token);
				logger.info("authentication registered user");
			}
			isInLocal = sm.isInLocalDB(username);
			
			if (isInLDAP && isInLocal) {
				loggedIn = true;
			}
			else {
				loggedIn = false;
				MessageUtil.addErrorMessage(PASSWORD_FIELD_JSF_ID,
						                    "noUserInNcia");
				loginFailure = true;

				return LOGIN_FAIL;
			}

			if (loggedIn) {
				email = sm.getUserEmail(uname);
			}
		}
		catch (gov.nih.nci.security.exceptions.CSFirstTimeLoginException ftle) {
			firstTimeLogin = true;
			loggedIn = false;
			loginFailure = true;
			MessageUtil.addErrorMessage(PASSWORD_FIELD_JSF_ID,
										"firstTimeLogin");
			return LOGIN_FAIL;
		}
		catch (Exception e) {
			logger.error("CSException: " + e);
			e.printStackTrace();
			// User has provided incorrect information
			MessageUtil.addErrorMessage(PASSWORD_FIELD_JSF_ID,
					                    "securityLogin");
			loggedIn = false;
			loginFailure = true;

			return LOGIN_FAIL;
		}

		if (loggedIn) {
			loginFailure = false;

			recordLogin();
			return setupAuthorization(uname);
		} else {
			loginFailure = true;
			return LOGIN_FAIL;
		}
	}

	private void recordLogin() throws Exception {
		/* the following remote nodes setup has been moved to the
		 * remote search link action.
		 */
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getRemoteAddr();
		System.out.println("recordLogin:"+ipAddress);
		// Record the login for reporting purposes, not for guest user
		AnonymousLoginBean anonymousLoginBean = BeanManager.getAnonymousLoginBean();
		if(!anonymousLoginBean.getGuestLoggedIn()){
			System.out.println("not guest");

			LoginHistoryDAO loginHistoryDAO = (LoginHistoryDAO)SpringApplicationContext.getBean("loginHistoryDAO");
			loginHistoryDAO.recordUserLogin(ipAddress);
		}
		else {
			System.out.println("guest so forget it");

		}
	}

	private String setupAuthorization(String uname) {

		// Get the security rights for the user and store them
		try {
			authMgr = new AuthorizationManager(uname);

			loginRedirector.afterLoginRedirect();
			BeanManager.getSearchWorkflowBean().loggedIn();
			return "home";

		} catch (Exception e) {
			logger.error("Exception in AuthorizationManager: " + e);
			e.printStackTrace();
			// User has provided incorrect information
			MessageUtil.addErrorMessage(PASSWORD_FIELD_JSF_ID,
					                    "securityLogin");

			loginFailure = true;

			return LOGIN_FAIL;
		}
	}

	private LoginRedirector loginRedirector = new LoginRedirector();

	/**
	 * Logs a user out by invalidating the session.
	 */
	public String logout() {
		AnonymousLoginBean anonymousLoginBean = BeanManager.getAnonymousLoginBean();
		anonymousLoginBean.logout();

		return "logout";
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String pass) {
		password = pass;
	}

	public boolean getLoggedIn() {
		return loggedIn;
	}

	/**
	 * Returns a flag of whether or not to display the Tools submenu
	 */
	public boolean getShowTools() {
		return getHasQaRole() || getHasFeedbackRole() || getChangePassword();
	}

	/**
	 * Returns true if the user has the QA Tool role
	 */
	public boolean getHasQaRole() {
		if (authMgr == null) {
			return false;
		}

		return authMgr.hasRole(RoleType.MANAGE_VISIBILITY_STATUS);
	}
	/**
	 * Returns true if the user has the Super curator role
	 */
	public boolean getHasSuperCuratorRole() {
		if (authMgr == null) {
			return false;
		}

		return authMgr.hasRole(RoleType.SUPER_CURATOR);
	}
	
	public boolean getHasAdminRole() {
		if (authMgr == null) {
			return false;
		}

		return authMgr.hasRole(RoleType.ADMIN);
	}	

	public boolean getHasDeletionRole() {
		if (authMgr == null) {
			return false;
		}

		return authMgr.hasRole(RoleType.DELETE_ADMIN);
	}

	public boolean getHasCollectionDescRole() {
		if (authMgr == null) {
			return false;
		}
		return authMgr.hasRole(RoleType.MANAGE_COLLECTION_DESCRIPTION);
	}

	/**
	 * Returns true if the user has the feedback to submitter role
	 */
	public boolean getHasFeedbackRole() {
		if (authMgr == null) {
			return false;
		}

		return authMgr.hasRole(RoleType.VIEW_SUBMISSION_REPORT);
	}

	/**
	 * Returns true - every user is able to change password
	 */
	public boolean getChangePassword() {
		AnonymousLoginBean anonymousLoginBean = BeanManager.getAnonymousLoginBean();
		return (loggedIn && getInstallationSite() && !anonymousLoginBean.getGuestLoggedIn());
	}

	private boolean loginFailure = false;

	public boolean getLoginFailure() {
		/*HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		String para = request.getParameter("action");

		if (para != null
				&& (para.equalsIgnoreCase("cancel") || para
						.equalsIgnoreCase("home"))) {
			return false;
		}*/
		return loginFailure;
	}

	public void setLoginFailure(boolean loginFailure) {
		this.loginFailure = loginFailure;
	}

	/**
	 * @return true if the installation site is ncicb, false otherwise
	 */
	public boolean getInstallationSite() {
		String installedSite = NCIAConfig.getInstallationSite();
		if (installedSite.equalsIgnoreCase("ncicb")) {
			return true;
		}
		else {
			return false;
		}
	}

	private String adminEmailAddress;

	/**
	 * @return application administrator email address
	 */

	public String getAdminEmailAddress() {
		adminEmailAddress = NCIAConfig.getAdminEmailAddress();
		return adminEmailAddress;

	}

	public String getUserNameForUI(){
		AnonymousLoginBean anonymousLoginBean = BeanManager.getAnonymousLoginBean();
		if(anonymousLoginBean.getGuestLoggedIn()){
			return "Guest";
		}
		return username;
	}

	public boolean getLoggedOutLink() {
		AnonymousLoginBean anonymousLoginBean = BeanManager.getAnonymousLoginBean();
		if(anonymousLoginBean.getGuestLoggedIn()){
			return false;
		}
		return loggedIn;
	}

	/**
	 * determine whether the user has logged in to the system with their registered id or guest user.
	 */
	public boolean getHasLoggedInAsRegisteredUser(){
		if(loggedIn){
			AnonymousLoginBean anonymousLoginBean = BeanManager.getAnonymousLoginBean();
			boolean isGuestEnabled = anonymousLoginBean.getIsGuestEnabled();
			if(!isGuestEnabled){
				return true;
			}else{
				if(!anonymousLoginBean.getGuestLoggedIn()){
					return true;
				}
			}
		}
		return false;
	}

	public boolean getFirstTimeLogin() {
		return firstTimeLogin;
	}

	public void setFirstTimeLogin(boolean firstTimeLogin) {
		this.firstTimeLogin = firstTimeLogin;
	}

	public String getNewPassword1() {
		return newPassword1;
	}

	public void setNewPassword1(String pass1) {
		newPassword1 = pass1;
	}

	public String getNewPassword2() {
		return newPassword2;
	}

	public void setNewPassword2(String pass2) {
		newPassword2 = pass2;
	}
	public String getTokenValue(){
		if (token!=null){
			if (token.isExpired())
			{
				token=RESTUtil.getToken(username, password);
			}
			return token.getValue();
		}
		else{
			return null;
		}
	}
}
