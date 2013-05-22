/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch.requests;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.util.*;
import gov.nih.nci.ncia.criteria.*;
import gov.nih.nci.ncia.search.SearchCriteriaDTO;

import java.util.*;
import java.text.*;
import org.junit.Test;

public class DICOMQueryUtilTestCase {

	@Test
	public void testCreateSearchCriteria() throws Exception  {
		Util.loadSystemPropertiesFromPropertiesResource("ncia.properties");
		
		SimpleDateFormat sdf = NCIAConfig.getDateFormat();
		
		SearchCriteriaDTO[] critArr = DICOMQueryUtil.createSearchCriteria(createQuery());
		assertEquals(critArr.length, 4);
		
		SearchCriteriaDTO collectionCriteria = critArr[0];
		assertEquals(collectionCriteria.getType(), 
				     "gov.nih.nci.ncia.criteria.CollectionCriteria");
		assertEquals(collectionCriteria.getValue(), "fred");

		SearchCriteriaDTO dateRangeCriteria = critArr[1];
		assertEquals(dateRangeCriteria.getType(), 
				     "gov.nih.nci.ncia.criteria.DateRangeCriteria");
		assertEquals(dateRangeCriteria.getValue(), 
				     "2006-11-19");
		assertEquals(dateRangeCriteria.getSubType(),"1");
		
		dateRangeCriteria = critArr[2];
		assertEquals(dateRangeCriteria.getType(), 
				     "gov.nih.nci.ncia.criteria.DateRangeCriteria");
		assertEquals(dateRangeCriteria.getValue(), 
				     "2006-11-21");
		assertEquals(dateRangeCriteria.getSubType(),"2");		
		
		SearchCriteriaDTO imageModalityCrit = critArr[3];
		assertEquals(imageModalityCrit.getType(), 
				     "gov.nih.nci.ncia.criteria.ImageModalityCriteria");
		assertEquals(imageModalityCrit.getValue(), "CT");

	}

	/////////////////////////////////PRIVATE/////////////////////////////////
	
	private static DICOMQuery createQuery() throws Exception {
		AuthorizationCriteria authorizationCriteria = new AuthorizationCriteria();
		
		DateRangeCriteria dateRangeCriteria = new DateRangeCriteria();
		SimpleDateFormat sdf = NCIAConfig.getDateFormat();
		Date fromDate = sdf.parse("2006-11-19");
		dateRangeCriteria.setFromDate(fromDate);
		Date toDate = sdf.parse("2006-11-21");
		dateRangeCriteria.setToDate(toDate);
		
		CollectionCriteria collectionCriteria = new CollectionCriteria();
		collectionCriteria.setCollectionValue("fred");
		
		ImageModalityCriteria imageModalityCrit = new ImageModalityCriteria();
		List<String> imageModVal = new ArrayList<String>();
		imageModVal.add("CT");
		imageModalityCrit.setImageModalityObjects(imageModVal);
		
		// Build Query
		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setQueryName("Query 1");
		dicomQuery.setUserID("kascice");

		dicomQuery.setCriteria(imageModalityCrit);
		dicomQuery.setCriteria(authorizationCriteria);
		dicomQuery.setCriteria(collectionCriteria);
		dicomQuery.setCriteria(dateRangeCriteria);
		
		return dicomQuery;
	}	
}
