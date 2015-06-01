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
* Revision 1.2  2007/10/01 12:22:10  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/07 12:05:23  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.3  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.security;

import java.io.Serializable;


/**
 * This is a wrapper for the CSM's user object as it is only availible for
 * hibernate 2.1.7 and we are using Hibernate 3.0.  It contains the items
 * neccessary to store a Query.
 * @author shinohaa
 *
 */
public class NCIAUser implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6576648415661488931L;
    private Long userId;
    private String loginName;
    private String email;

    public NCIAUser() {
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return Returns the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
}
