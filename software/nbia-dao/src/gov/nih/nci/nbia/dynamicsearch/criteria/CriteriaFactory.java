/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch.criteria;

import org.hibernate.criterion.Criterion;

/**
 * Interface for a factory object that cranks out Hibernate
 * Criterion object.
 */
public interface CriteriaFactory {
    
	/**
	 * Construct a criteria.  The subclass will really define the nature
	 * of the criteria, but the field name and value are passed in here.
	 * The caller is responsible for the full field name (aliases and all that).
	 */
	public Criterion constructCriteria(String fieldName, 
    		                           String value,
    		                           String fieldType) throws Exception;
}
