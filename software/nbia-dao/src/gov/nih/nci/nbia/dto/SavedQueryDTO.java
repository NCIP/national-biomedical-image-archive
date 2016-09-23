/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: SavedQueryDTO.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
* Revision 1.3  2007/09/27 23:30:39  bauerd
* This is the checked in seperation of dependent classes betweeen the grid and the commons module...
*
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.12  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.dto;

import gov.nih.nci.ncia.criteria.Criteria;
import gov.nih.nci.nbia.security.NCIAUser;

import java.util.ArrayList;


/**
 * This object will hold the information neccessary to display a saved query on
 * the front end.
 *
 * @author shinohaa
 *
 */
public class SavedQueryDTO extends AbstractStoredQueryDTO {
    private boolean newResults = false;
    private boolean delete;
    private NCIAUser user;

    public SavedQueryDTO() {
        criteriaList = new ArrayList<Criteria>();
    }

    public boolean isNewResults() {
        return newResults;
    }

    public void setNewResults(boolean newResults) {
        this.newResults = newResults;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    @Override
    public String getQueryName() {
        return queryName;
    }

	public NCIAUser getUser() {
		return user;
	}

	public void setUser(NCIAUser user) {
		this.user = user;
    }
}
