package gov.nih.nci.ncia.search;

import junit.framework.TestCase;

public class SearchCriteriaDTOTestCase extends TestCase {

	public void testSearchCriteriaDTO() {
		SearchCriteriaDTO searchCriteriaDTO = new SearchCriteriaDTO();
		searchCriteriaDTO.setType("type1");
		searchCriteriaDTO.setSubType("subtype1");
		searchCriteriaDTO.setValue("val1");
		
		assertTrue(searchCriteriaDTO.getType().equals("type1"));
		assertTrue(searchCriteriaDTO.getSubType().equals("subtype1"));
		assertTrue(searchCriteriaDTO.getValue().equals("val1"));			
		
	}

}
