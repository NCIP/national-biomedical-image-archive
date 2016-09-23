/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.util.HqlUtils;
import junit.framework.TestCase;
import java.util.*;

public class HqlUtilsTestCase extends TestCase {

	public void testBuildInClause() {
		String textBeforeParen = "foofoo ";
		
		String result = HqlUtils.buildInClause(textBeforeParen, null);		
		assertEquals(result, textBeforeParen+" ()");
		
		Collection<String> ids = new ArrayList<String>();
		result = HqlUtils.buildInClause(textBeforeParen, ids);		
		assertEquals(result, textBeforeParen+" ()");
		
		ids.add("id");
		result = HqlUtils.buildInClause(textBeforeParen, ids);		
		assertEquals(result, textBeforeParen+" ('id')");
		
		ids.add("id2");
		result = HqlUtils.buildInClause(textBeforeParen, ids);		
		assertEquals(result, textBeforeParen+" ('id', 'id2')");		
	}

	public void testBuildInClauseUsingIntegers() {
		String textBeforeParen = "foofoo ";
		
		String result = HqlUtils.buildInClauseUsingIntegers(textBeforeParen, null);		
		assertEquals(result, textBeforeParen+" ()");
		
		Collection<Integer> ids = new ArrayList<Integer>();
		result = HqlUtils.buildInClauseUsingIntegers(textBeforeParen, ids);		
		assertEquals(result, textBeforeParen+" ()");
		
		ids.add(0);
		result = HqlUtils.buildInClauseUsingIntegers(textBeforeParen, ids);		
		assertEquals(result, textBeforeParen+" ('0')");
		
		ids.add(1);
		result = HqlUtils.buildInClauseUsingIntegers(textBeforeParen, ids);		
		assertEquals(result, textBeforeParen+" ('0', '1')");	
	}

}
