/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchform;

import gov.nih.nci.ncia.criteria.DataCollectionDiameterCriteria;
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
public class DataCollectionDiameterSearchBean extends RangeCriteriaSearchBean {

	public DataCollectionDiameterSearchBean() {
    	logger = Logger.getLogger(DataCollectionDiameterSearchBean.class);
        logger.debug("constructing DataCollectionDiameterSearchBean");
    }
    /**
     * Populates the DicomQuery
     *
     */
    protected void setQuery(DICOMQuery query) {
    	DataCollectionDiameterCriteria criteria = new DataCollectionDiameterCriteria(getCriteriaLeftCompare(), getCriteriaLeft(), getCriteriaRightCompare(), getCriteriaRight());
        query.setCriteria(criteria);
    }
}
