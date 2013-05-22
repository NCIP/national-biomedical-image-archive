/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;



public class NCIAQueryFilterTestCase {

	@Test
	public void testConvertTDPToCQL(){
		List<gov.nih.nci.ncia.domain.TrialDataProvenance> tdpList = new ArrayList<gov.nih.nci.ncia.domain.TrialDataProvenance>();
		gov.nih.nci.ncia.domain.TrialDataProvenance tdp = null;

		tdp = new gov.nih.nci.ncia.domain.TrialDataProvenance();
		tdp.setProject("RIDER");
		tdp.setSiteName("MSKCC");
		tdpList.add(tdp);

		tdp = new gov.nih.nci.ncia.domain.TrialDataProvenance();
		tdp.setProject("RIDER");
		tdp.setSiteName("MDACC");
		tdpList.add(tdp);
		tdp = new gov.nih.nci.ncia.domain.TrialDataProvenance();
		tdp.setProject("LIDC");
		tdp.setSiteName("LIDC");
		tdpList.add(tdp);
		tdp = new gov.nih.nci.ncia.domain.TrialDataProvenance();
		tdp.setProject("RIDER");
		tdp.setSiteName("");
		tdpList.add(tdp);

		gov.nih.nci.cagrid.cqlquery.Group g = NCIAQueryFilter.convertTDPToCQL(tdpList);

		Assert.assertNotNull("results is not null", g);
		Assert.assertTrue("returned expected number of results: " + g.getGroup().length, g.getGroup().length == 3);
		Assert.assertTrue("contains RIDER:  " + g.getGroup(0).getAttribute(0).getValue(), g.getGroup(0).getAttribute(0).getValue() == "RIDER");
	}
}