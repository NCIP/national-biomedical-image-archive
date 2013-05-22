/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.gridsearch;

import gov.nih.nci.ncia.criteria.Criteria;
import gov.nih.nci.ncia.criteria.PersistentCriteria;
import gov.nih.nci.nbia.internaldomain.QueryHistoryAttribute;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.search.PatientSearcher;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.SearchCriteriaDTO;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PatientSearchUtil {
	public static List<PatientSearchResult> searchForPatients(SearchCriteriaDTO[] dtos) throws Exception {
		DICOMQuery query = new DICOMQuery();
		Set<Criteria> criteria = getCriteraFromSearchDtos(dtos);


		Class dicomQueryClass = query.getClass();
		for(Criteria crit : criteria) {
			Method setCriteriaMethod = dicomQueryClass.getDeclaredMethod("setCriteria", crit.getClass());

			setCriteriaMethod.invoke(query, crit);
		}

		//we have to run the authorization filter
		//otherwise someone could make up criteria giving them whatever privilege they want
        AuthorizationManager man = new AuthorizationManager();
        man.authorizeCollections(query);
        man.authorizeSitesAndSSGs(query);

		PatientSearcher patientSearcher = new PatientSearcher();
		List<PatientSearchResult> results = patientSearcher.searchForPatients(query);
		return results;
	}


    /**
     * This method will build a set of criteria from te search DTOs passed into the
     * query.
     */
    private static Set<Criteria> getCriteraFromSearchDtos(SearchCriteriaDTO[] dtos) throws Exception {
        // A map to store each instance of saved criteria
        // The key is the concatenation of the class name and the instance number
        // This ensures one entry in the map per instance number and class
        // combination in the saved criteria
        Map<String, PersistentCriteria> map = new HashMap<String, PersistentCriteria>();

        //seems like an empty array can come across wire as null?????
        if(dtos!=null) {
	        for(int i = 0; i < dtos.length; i++) {
	            SearchCriteriaDTO attribute = dtos[i];
	
	            // See if a criteria of the class has already been added to the map
	            String className = attribute.getType();
	
	            String key = className;
	
	            PersistentCriteria criteria = map.get(key);
	
	            if (criteria == null) {
	                // If not in the map, create the criteria using reflection
	                criteria = (PersistentCriteria) Class.forName(className)
	                                                     .newInstance();
	                map.put(key, criteria);
	            }
	
	            // Add the attribute values to the criteria
	            QueryHistoryAttribute attr = new QueryHistoryAttribute();
	            attr.setAttributeName(attribute.getType());
	            attr.setAttributeValue(attribute.getValue());
	            attr.setSubAttributeName(attribute.getSubType());
	
	            //actually set the value for the criteria
	            criteria.addValueFromQueryAttribute(attr.getQueryAttributeWrapper());
	        }
        }

        // Convert to a Set to return
        return new HashSet<Criteria>(map.values());
    }
}