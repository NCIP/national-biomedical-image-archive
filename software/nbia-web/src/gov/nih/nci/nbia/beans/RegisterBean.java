/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.3  2008/02/20 20:53:32  lethai
* Task 2267 - Change to use LDAP
*
* Revision 1.2  2007/12/17 21:23:43  lethai
* changes for tracker id 8161 and 11009
*
* Revision 1.1  2007/08/05 21:06:52  bauerd
* Initial Check in of reorganized components
*
* Revision 1.26  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.beans;

import gov.nih.nci.nbia.ispy.UrlParams;
import gov.nih.nci.nbia.mail.MailManager;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.RegexUtil;
import gov.nih.nci.nbia.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;


/**
 * This bean holds all of the information and methods that perform the
 * register function.
 * @author shinohaa
 *
 */
public class RegisterBean {
    /**
     * Logger for the class.
     */
    private static Logger logger = Logger.getLogger(RegisterBean.class);
    private static final String REGISTER_REQUIRED_RESOURCE_ID = "registerRequired";

    /**
     * Information for all of the inputs on the page.
     */
    private String firstName = "";
    private String lastName = "";
    private String middleInitial = "";
    private String email = "";
    private String phone = "";
    private String fax = "";
    private String organization = "";
    private String address = "";
    private String city = "";
    private String postalCode = "";
    private String loginEmail = "";
    private String password1 = "";
    private String password2 = "";
    private String title = "";
    private String state = "";
    private String country = "";
    private String reasonForRegistration;
    private boolean registerToUsersList;
    /**
     * List of titles.
     */
    private List<SelectItem> titleItems;

    /**
     * List of all states.
     */
    private List<SelectItem> stateItems;

    /**
     * List of all countries.
     */
    private List<SelectItem> countryItems;

    /**
     * Components bound to the password fields.
     */
    private UIInput pass1;
    private UIInput pass2;

    /**
     * Constructor.  Initializes all lists with the SelectItems for the
     * drop down boxes.
     *
     */
    public RegisterBean() {
        titleItems = new ArrayList<SelectItem>();
        stateItems = new ArrayList<SelectItem>();
        countryItems = new ArrayList<SelectItem>();

        titleItems.add(new SelectItem(""));
        titleItems.add(new SelectItem("Dr."));
        titleItems.add(new SelectItem("Mr."));
        titleItems.add(new SelectItem("Mrs."));
        titleItems.add(new SelectItem("Miss"));
        titleItems.add(new SelectItem("Ms."));
    }

    private static boolean atLeastOneIsEmpty(String email,
    		                                 String phone,
    		                                 String firstName,
    		                                 String lastName,
    		                                 String organization) {
    	return StringUtil.isEmptyTrim(email) ||
               StringUtil.isEmptyTrim(phone) ||
               StringUtil.isEmptyTrim(firstName) ||
               StringUtil.isEmptyTrim(lastName) ||
               StringUtil.isEmptyTrim(organization);
    }

    private static boolean validateRequiredFieldsPresent(String email,
    		                               String phone,
    		                               String firstName,
    		                               String lastName,
    		                               String organization) {

    	if(atLeastOneIsEmpty(email,phone,firstName,lastName,organization)){

            if(StringUtil.isEmptyTrim(email)){
                MessageUtil.addErrorMessage("MAINbody:registerForm:email",
                                            REGISTER_REQUIRED_RESOURCE_ID);
            }

            if(StringUtil.isEmptyTrim(phone)){
                MessageUtil.addErrorMessage("MAINbody:registerForm:phone",
                                            REGISTER_REQUIRED_RESOURCE_ID);
            }

            if(StringUtil.isEmptyTrim(firstName)){
                MessageUtil.addErrorMessage("MAINbody:registerForm:firstName",
                                            REGISTER_REQUIRED_RESOURCE_ID);
            }

            if(StringUtil.isEmptyTrim(lastName)){
                MessageUtil.addErrorMessage("MAINbody:registerForm:lastName",
                                            REGISTER_REQUIRED_RESOURCE_ID);
            }

            if(StringUtil.isEmptyTrim(organization)){
                MessageUtil.addErrorMessage("MAINbody:registerForm:org",
                                            REGISTER_REQUIRED_RESOURCE_ID);
            }

            return true;

        }
        else {
        	return false;
        }
    }

    private static boolean validateFieldFormat(String email,
                                               String phone,
                                               String firstName,
                                               String lastName,
                                               String organization,
                                               String fax) {
    	boolean bValidationProblem = false;

    	bValidationProblem |= validateEmail(email);

        bValidationProblem |= validatePhone(phone);

        bValidationProblem |= validateFirstName(firstName);

        bValidationProblem |= validateLastName(lastName);

        bValidationProblem |= validateOrganization(organization);

        bValidationProblem |= validateFax(fax);

        return bValidationProblem;
    }

    private static boolean validateEmail(String email) {
    	RegexUtil regex = new RegexUtil();

        if(!StringUtil.isEmptyTrim(email) && ! regex.cmpEmail(email)){
            MessageUtil.addErrorMessage("MAINbody:registerForm:email",
                                        "registerInvalidEmail");
            return true;
        }
        else {
        	return false;
        }
    }
    private static boolean validateFax(String fax) {
    	RegexUtil regex = new RegexUtil();

        if(!StringUtil.isEmptyTrim(fax) &&
           !regex.cmpPhoneNumber(fax)){
            MessageUtil.addErrorMessage("MAINbody:registerForm:fax",
                                        "registerInvalidPhone");
            return true;
        }
        else {
        	return false;
        }
    }
    private static boolean validateOrganization(String organization) {
        if(organization!= null && !StringUtil.removeIllegitCharacters(organization).equalsIgnoreCase(organization)){
            MessageUtil.addErrorMessage("MAINbody:registerForm:org",
                                        "registerInvalidOrg");
            return true;
        }
        else {
        	return false;
        }
    }
    private static boolean validatePhone(String phone) {
    	RegexUtil regex = new RegexUtil();

        if (!StringUtil.isEmptyTrim(phone) && !regex.cmpPhoneNumber(phone)) {
            MessageUtil.addErrorMessage("MAINbody:registerForm:phone",
                                        "registerInvalidPhone");

            return true;
        }
        else {
        	return false;
        }

    }
    private static boolean validateLastName(String lastName) {
        if(lastName != null && !StringUtil.removeIllegitCharacters(lastName).equalsIgnoreCase(lastName)){
            MessageUtil.addErrorMessage("MAINbody:registerForm:lastName",
                                        "registerInvalidLastName");
            return true;
        }
        else {
        	return false;
        }
    }
    private static boolean validateFirstName(String firstName) {
        if(firstName!= null && !StringUtil.removeIllegitCharacters(firstName).equalsIgnoreCase(firstName)){
            MessageUtil.addErrorMessage("MAINbody:registerForm:firstName",
                                        "registerInvalidFirstName");
            return true;
        }
        else {
        	return false;
        }
    }
    /**
     * Called when the register button is pressed.
     */
    public String register() {
        try {

            boolean bValidationProblem = false;

            bValidationProblem = validateRequiredFieldsPresent(email, phone, firstName, lastName, organization);

            bValidationProblem |= validateFieldFormat(email, phone, firstName, lastName, organization, fax);

            if(bValidationProblem) {
                return null;
            }
            // construct email to send to ncicb mail box
            // content include: first name, last name, email address, phone, organization, title, fax
            // the sender will be the user email address so that ncicb mailbox will automatically generate email to send to
            // the user.
            MailManager.sendRegistrationEmail(firstName, 
                                              lastName, 
                                              email, 
                                              phone, 
                                              organization, 
                                              title, 
                                              fax,
                                              reasonForRegistration);
            
            if(this.isRegisterToUsersList()) {
            	MailManager.sendUsersListRegistration(email, firstName+" "+lastName);
            }
            
        } 
        catch (Exception e) {
            logger.error("Problem registering user ", e);
            throw new RuntimeException(e);
        }

        UrlParams urlParams = (UrlParams) FacesContext.getCurrentInstance()
                                                      .getExternalContext()
                                                      .getSessionMap()
                                                      .get("UrlParams");

        if ((urlParams != null) &&
                urlParams.getSource().equalsIgnoreCase("ISPY")) {
            return "referencedImages";
        }
        else {
            return "registerConfirm";
        }
    }


    /**
     * Validates phone number fields
     * @param fc
     * @param uic
     * @param val
     */
    public void validatePhone(FacesContext fc, UIComponent uic, Object val) {
        String phone = (String) val;

        // Validate the phone number using a regular expression (found online) that
        // handles most known phone number formats
        if (!phone.matches(
                    "^((\\+\\d{1,3}(-| )?\\(?\\d\\)?(-| )?\\d{1,3})|(\\(?\\d{2,3}\\)?))(-| )?(\\d{3,4})(-| )?(\\d{4})(( x| ext)\\d{1,5}){0,1}$")) {
            ((UIInput) uic).setValid(false);
            throw new ValidatorException(MessageUtil.getMessage("registerInvalidPhone"));
        }
    }

    /**
     * Validates whether email is in format of address@domain.subdomain
     * @param fc
     * @param uic
     * @param val
     */
    public void validateEmail(FacesContext fc, UIComponent uic, Object val) {
        String email = (String) val;

        if (!email.matches(".+@.+\\.[a-z]+")) {
            ((UIInput) uic).setValid(false);
            throw new ValidatorException(MessageUtil.getMessage(
                    "registerInvalidEmail"));
        }

    }

    /**
     * Compares the two passwords and makes sure they are the same.
     * @param fc
     * @param uic
     * @param val
     */
    public void validatePass(FacesContext fc, UIComponent uic, Object val) {
        String passOne = (String) pass1.getValue();
        String passTwo = (String) val;

        if (!passOne.equals(passTwo)) {
            throw new ValidatorException(MessageUtil.getMessage(
                    "registerPassNotMatch"));
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List getStateItems() {
        return stateItems;
    }

    public void setStateItems(List<SelectItem> stateItems) {
        this.stateItems = stateItems;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List getTitleItems() {
        return titleItems;
    }

    public void setTitleItems(List<SelectItem> titleItems) {
        this.titleItems = titleItems;
    }

    public List getCountryItems() {
        return countryItems;
    }

    public void setCountryItems(List<SelectItem> countryItems) {
        this.countryItems = countryItems;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public UIInput getPass1() {
        return pass1;
    }

    public void setPass1(UIInput pass1) {
        this.pass1 = pass1;
    }

    public UIInput getPass2() {
        return pass2;
    }

    public void setPass2(UIInput pass2) {
        this.pass2 = pass2;
    }

	public String getReasonForRegistration() {
		return reasonForRegistration;
	}

	public void setReasonForRegistration(String reasonForRegistration) {
		if(reasonForRegistration.length() > 4000) {
			reasonForRegistration = reasonForRegistration.substring(0, 4000);
		}
		this.reasonForRegistration = reasonForRegistration;
	}

	public boolean isRegisterToUsersList() {
		return registerToUsersList;
	}

	public void setRegisterToUsersList(boolean registerToUsersList) {
		this.registerToUsersList = registerToUsersList;
	}
}
