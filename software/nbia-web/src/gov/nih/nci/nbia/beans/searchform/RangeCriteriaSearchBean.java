/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchform;

import gov.nih.nci.nbia.query.DICOMQuery;

import org.apache.log4j.Logger;

/**
 * This is the Session scope bean that provides the search functionality on the
 * search page. It is maintained in the session in order to provide the
 * functionality of being able to go back and edit a search from the
 * breadcrumbs.
 *
 * @author zhouro
 */
public abstract class RangeCriteriaSearchBean {

	protected static Logger logger = null;

	protected DICOMQuery query = null;

    //variables
    private String criteriaLeftCompare = "";
    private String criteriaLeft = "";
    private String criteriaRightCompare = "";
    private String criteriaRight = "";


    /**
     * Constructor. Populates drop downs and sets default values.
     */
    public RangeCriteriaSearchBean() {
    }


    /**
     * Populates the DicomQuery
     *
     * @return true if successful, false if there were validation errors
     */
    public boolean buildQuery(DICOMQuery query){
        if ((criteriaLeftCompare != null) && !criteriaLeftCompare.equals("") && !criteriaLeft.equals("")) {
            setQuery(query);
        }
        return true;
    }


    /**
     * Sets default values for all search fields.
     */
    public void setDefaultValues() {

        criteriaLeftCompare = "";
        criteriaLeft = "";

        criteriaRightCompare = "";
        criteriaRight = "";

    }

    protected abstract void setQuery(DICOMQuery query);



	/**
	 * @return Returns the criteriaLeftCompare.
	 */
	public String getCriteriaLeftCompare() {
		return criteriaLeftCompare;
	}

	/**
	 * @param criteriaLeftCompare The criteriaLeftCompare to set.
	 */
	public void setCriteriaLeftCompare(String criteriaLeftCompare) {
		this.criteriaLeftCompare = criteriaLeftCompare;
	}

	/**
	 * @return Returns the criteriaLeft.
	 */
	public String getCriteriaLeft() {
		return criteriaLeft;
	}

	/**
	 * @param criteriaLeft The criteriaLeft to set.
	 */
	public void setCriteriaLeft(String criteriaLeft) {
		this.criteriaLeft = criteriaLeft;
	}

	/**
	 * @return Returns the criteriaRight.
	 */
	public String getCriteriaRight() {
		return criteriaRight;
	}

	/**
	 * @param criteriaRight The criteriaRight to set.
	 */
	public void setCriteriaRight(String criteriaRight) {
		this.criteriaRight = criteriaRight;
	}

	/**
	 * @return Returns the criteriaRightCompare.
	 */
	public String getCriteriaRightCompare() {
		return criteriaRightCompare;
	}

	/**
	 * @param criteriaRightCompare The criteriaRightCompare to set.
	 */
	public void setCriteriaRightCompare(String criteriaRightCompare) {
		this.criteriaRightCompare = criteriaRightCompare;
	}
}
