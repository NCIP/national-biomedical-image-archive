/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * $Id$ $Log:
 * SearchWorkflowBean.java,v $ Revision 1.90 2006/09/27 20:46:27 panq Reformated
 * with Sun Java Code Style and added a header for holding CVS history.
 */
/*
 * Created on Jun 13, 2005
 */
package gov.nih.nci.nbia.beans.searchform;

import gov.nih.nci.ncia.criteria.ReconstructionDiameterCriteria;
import gov.nih.nci.nbia.query.DICOMQuery;

import org.apache.log4j.Logger;

/**
 * This is the Session scope bean that provides the search functionality on the
 * search page. It is maintained in the session in order to provide the
 * functionality of being able to go back and edit a search from the
 * breadcrumbs.
 *
 * @author shinohaa
 */
public class ReconstructionDiameterSearchBean extends RangeCriteriaSearchBean {

	public ReconstructionDiameterSearchBean() {
    	logger = Logger.getLogger(ReconstructionDiameterSearchBean.class);
        logger.debug("constructing AcquisitionMatrixSearchBean");
    }
    /**
     * Populates the DicomQuery
     *
     */
    protected void setQuery(DICOMQuery query) {
    	ReconstructionDiameterCriteria criteria = new ReconstructionDiameterCriteria(getCriteriaLeftCompare(), getCriteriaLeft(), getCriteriaRightCompare(), getCriteriaRight());
        query.setCriteria(criteria);
    }

}
