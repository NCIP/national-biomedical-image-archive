/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.query;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rsna.ctp.stdstages.database.UIDResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })

public class UidQueryTestCase extends AbstractDbUnitTestForJunit4 {
	private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_353337344.xml";
	private String sopUID = "2.16.756.5.5.14.3078913146.1137516344.276618.0";
	@Autowired
	DicomSOPInstanceUIDQueryInterface ndatabase;
	
	@Override
	protected String getDataSetResourceSpec() {
		return TEST_DB_FLAT_FILE;
	}
	@Test
	public void testUIDQUery() throws Exception
	{
		Set<String> uidSet = new HashSet<String>();
		uidSet.add(sopUID);
		
		ndatabase.setDicomSOPInstanceUIDQuery(uidSet);
		Map<String, UIDResult> result = ndatabase.getUIDResult();
		UIDResult uidClazz= (UIDResult)result.get(sopUID);
		long datetime = uidClazz.getDateTime();
		Assert.assertEquals(datetime, 1182398400000L);
		Assert.assertTrue(uidClazz.isPRESENT()== true);
	}
	
}
