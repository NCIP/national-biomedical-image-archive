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
* Revision 1.1  2007/08/07 12:05:23  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.5  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.security;

import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.Role;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a protected element subclass that has two discriminating features:
 *
 * <ul>
 *   <li>It is spawned from a PE, but maps the PE description as the "table name".
 *       Not sure about why this is worth doing...
 *   <li>The role for the PE is associated here.... instead of using CSM API throughout app?
 *   <li>this used to extend PE - but it really isn't a PE - it didn't initialize
 *       itself to act as a PE except in a restricted way, so it's not really a subtype.
 *       further, invoking any PE super class methods would of course lead to
 *       exceptions and such as nothing is initialized.
 * </ul>
 *
 */
public class TableProtectionElement {
	private String tableName;
    private String attributeName;
    private String attributeValue;
    private Set<String> roles;

    public TableProtectionElement(ProtectionElement pe) {
        this.tableName = pe.getProtectionElementDescription();
        this.attributeName = pe.getAttribute();
        this.attributeValue = pe.getProtectionElementName();
        this.roles = new HashSet<String>();
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public String getTableName() {
        return tableName;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public void addAllRoles(Set<Role> theRoles) {
        for (Role currRole : theRoles) {
            addRole(currRole.getName());
        }
    }

    public boolean hasRole(RoleType role) {
        return roles.contains(role.toString());
    }
}
