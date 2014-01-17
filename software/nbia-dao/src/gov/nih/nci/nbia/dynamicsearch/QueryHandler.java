/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

import gov.nih.nci.nbia.dto.QcSearchResultDTO;
import gov.nih.nci.nbia.lookup.StudyNumberMap;
import gov.nih.nci.nbia.textsupport.SolrAllDocumentMetaData;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.ncia.search.PatientSearchResult;

import java.util.Date;
import java.util.List;

//this needs a different name that doesnt collide with other names in system
//QueryGenerator?  dont have a strong opinion except not to call it what
//its being called
public interface QueryHandler {
	public void setStudyNumberMap(StudyNumberMap studyNumberMap);

	/**
	 * After calling methods to set the criteria.... call this
	 * to actually execute the query.  Use accessor methods to
	 * get the results of invoking this method.
	 *
	 * fail if setQueryCriteria hasnt been called?
	 */
	public void query()throws Exception;
	
	/**
	 * @param fromDate
	 * @param toDate
	 * @return Qc series dto
	 * @throws Exception on error
	 */
	public List<QcSearchResultDTO> querySeries(Date fromDate, Date toDate) throws Exception;

	/**
	 * Call this before executing the query method.  This
	 * determines the criteria to be used when going to the database.
	 *
	 * statementRelation should be an enumerated type or a constant
	 * instead of formless String (AND/OR)
	 *
	 * any preconditions on this method?  length of criteria?
	 */
	public void setQueryCriteria(List<DynamicSearchCriteria> criteria,
			                     String statementRelation,
			                     List<SiteData> aData,
			                     List<String> sGrooups) throws Exception;


	/**
	 * Call this before executing the query method.  This
	 * determines the criteria to be used when going to the database.
	 *
	 * statementRelation should be an enumerated type or a constant
	 * instead of formless String (AND/OR)
	 *
	 * any preconditions on this method?  length of criteria?
	 */
	public void setQueryCriteria(List<DynamicSearchCriteria> criteria,
			                     String statementRelation,
			                     List<SiteData> aData,
			                     List<String> sGrooups, String[] visibilityStatus) throws Exception;
	/**
	 * This is returning a Hibernate object, so this should
	 * be changed to a DTO, or this whole thing needs to be hidden
	 * behind a DAO object.
	 */
	public List<PatientSearchResult> getPatients();

	/**
	 * This method will return list of data that user can select them from GUI
	 * as permissible data.
	 *
	 * @param packageName
	 * @param dataSource
	 * @param field
	 * @throws Exception
	 */
	public List<String> getPermissibleData(String packageName,
			                              String dataSource,
			                              String field) throws Exception;

	/**
	 * This method will return list of data corresponding to patients found in Solr
	 * as permissible data.
	 *
	 * @param testCriteria
	 */
	public List<SolrAllDocumentMetaData> searchSolr(String textCriteria);

}
