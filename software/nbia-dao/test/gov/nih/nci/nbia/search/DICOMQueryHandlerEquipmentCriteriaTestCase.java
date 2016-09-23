/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;
import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.ncia.criteria.AuthorizationCriteria;
import gov.nih.nci.ncia.criteria.ManufacturerCriteria;
import gov.nih.nci.ncia.criteria.ModelCriteria;
import gov.nih.nci.ncia.criteria.SoftwareVersionCriteria;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.SiteData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})

public class DICOMQueryHandlerEquipmentCriteriaTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testManufacturerCriteria() throws Exception {
		final String MANUFACTURER = "Bruker BioSpin MRI GmbH";
		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(createManufacturerCriteria(MANUFACTURER));
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 1);

		dicomQuery.setCriteria(createManufacturerCriteria("KODAK"));
		dicomQuery.setCriteria(createAuthorizationCriteria());

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield zero results",
				   resultSets.size() == 0);
	}
	@Test
	public void testSoftwareVersionCriteria() throws Exception {
		DICOMQuery dicomQuery = new DICOMQuery();

		SoftwareVersionCriteria softwareVersionCriteria = new SoftwareVersionCriteria();
		softwareVersionCriteria.setSoftwareVersionValue("Bruker BioSpin MRI GmbH||bruker_fake_model_name||PV 3.0.2pl1");

		dicomQuery.setCriteria(softwareVersionCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 1);

		softwareVersionCriteria = new SoftwareVersionCriteria();
		softwareVersionCriteria.setSoftwareVersionValue("Bruker BioSpin MRI GmbH||bruker_fake_model_name||garbage");
		dicomQuery.setCriteria(softwareVersionCriteria);
		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 0);

	}
	@Test
	public void testModelCriteria() throws Exception {
		DICOMQuery dicomQuery = new DICOMQuery();

		ModelCriteria modelCriteria = new ModelCriteria();
		modelCriteria.setCollectionValue("Bruker BioSpin MRI GmbH||bruker_fake_model_name");

		dicomQuery.setCriteria(modelCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 1);

		modelCriteria = new ModelCriteria();;
		modelCriteria.setCollectionValue("Bruker BioSpin MRI GmbH||garbage");
		dicomQuery.setCriteria(modelCriteria);
		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 0);
	}

    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }



    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_353337344.xml";
    @Autowired
    private DICOMQueryHandler dicomQueryHandler;

	private static ManufacturerCriteria createManufacturerCriteria(
			String manufacturerName) throws Exception {
		Collection<String> manufacturerNames = new ArrayList<String>();
		manufacturerNames.add(manufacturerName);

		ManufacturerCriteria manufacturerCriteria = new ManufacturerCriteria();
		manufacturerCriteria.setCollectionObjects(manufacturerNames);
		return manufacturerCriteria;
	}

	private static AuthorizationCriteria createAuthorizationCriteria()
			throws Exception {
		AuthorizationManager am = new AuthorizationManager("kascice");
		List<SiteData> authorizedSites = am.getAuthorizedSites(RoleType.READ);
		List<String> authorizedCollections = am
				.getAuthorizedCollections(RoleType.READ);
		List<String> authorizedSeriesSecurityGroups = am
				.getAuthorizedSeriesSecurityGroups(RoleType.READ);

		AuthorizationCriteria authorizationCriteria = new AuthorizationCriteria();
		authorizationCriteria.setCollections(authorizedCollections);
		authorizationCriteria.setSites(authorizedSites);
		authorizationCriteria
				.setSeriesSecurityGroups(authorizedSeriesSecurityGroups);

		return authorizationCriteria;
	}

}
