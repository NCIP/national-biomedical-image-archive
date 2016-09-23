/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

import gov.nih.nci.nbia.util.Util;
import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.searchresult.PatientSearchResult;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.lookup.StudyNumberMap;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class QueryHandlerImplTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testTdpQuery() throws Exception {
		AuthorizationManager am = new AuthorizationManager("kascice");

		List<DynamicSearchCriteria> myList = new ArrayList<DynamicSearchCriteria>();

		DynamicSearchCriteria dsc = new DynamicSearchCriteria();
		dsc.setDataGroup("TrialDataProvenance");
		dsc.setField("dpSiteName");
		dsc.setValue("LIDC");
		Operator o = new Operator();
		o.setValue("=");
		dsc.setOperator(o);

		myList.add(dsc);

		qb.setStudyNumberMap(new StudyNumberMap());
		qb.setQueryCriteria(myList, "AND", am.getAuthorizedSites(), am.getAuthorizedSeriesSecurityGroups());
		qb.query();

		List<PatientSearchResult> patientDtos = qb.getPatients();
		Assert.assertEquals(patientDtos.size(), 2);

		myList = new ArrayList<DynamicSearchCriteria>();
		dsc = new DynamicSearchCriteria();
		dsc.setDataGroup("TrialDataProvenance");
		dsc.setField("dpSiteName");
		dsc.setValue("dummy");
		o = new Operator();
		o.setValue("=");
		dsc.setOperator(o);
		myList.add(dsc);

		qb.setQueryCriteria(myList, "AND", am.getAuthorizedSites(), am.getAuthorizedSeriesSecurityGroups());
		qb.query();

		patientDtos = qb.getPatients();
		Assert.assertEquals(patientDtos.size(), 0);
	}

	@Test	
	public void testPatientQuery() throws Exception {
		AuthorizationManager am = new AuthorizationManager("kascice");

		List<DynamicSearchCriteria> myList = new ArrayList<DynamicSearchCriteria>();

		DynamicSearchCriteria dsc = new DynamicSearchCriteria();
		dsc.setDataGroup("Patient");
		dsc.setField("patientId");
		dsc.setValue("1.3.6.1.4.1.9328.50.3.0022");
		Operator o = new Operator();
		o.setValue("=");
		dsc.setOperator(o);

		myList.add(dsc);

		qb.setStudyNumberMap(new StudyNumberMap());
		qb.setQueryCriteria(myList, "AND", am.getAuthorizedSites(), am.getAuthorizedSeriesSecurityGroups());
		qb.query();

		List<PatientSearchResult> patientDtos = qb.getPatients();
		Assert.assertEquals(patientDtos.size(), 1);

		qb.setQueryCriteria(myList, "OR", am.getAuthorizedSites(), am.getAuthorizedSeriesSecurityGroups());
		qb.query();

		patientDtos = qb.getPatients();
		Assert.assertEquals(patientDtos.size(), 1);

		try {
			qb.setQueryCriteria(myList, "garbage", am.getAuthorizedSites(), am.getAuthorizedSeriesSecurityGroups());
			qb.query();
			Assert.fail("shoudlnt get here");
		}
		catch(Exception ex) {

		}
	}

	@Test	
	public void testAndQuery() throws Exception {
		AuthorizationManager am = new AuthorizationManager("kascice");

		List<DynamicSearchCriteria> myList = new ArrayList<DynamicSearchCriteria>();

		DynamicSearchCriteria dsc = new DynamicSearchCriteria();
		dsc.setDataGroup("CTImage");
		dsc.setField("exposure");
		dsc.setValue("960");
		Operator o = new Operator();
		o.setValue("=");
		dsc.setOperator(o);

		myList.add(dsc);

		dsc = new DynamicSearchCriteria();
		dsc.setDataGroup("CTImage");
		dsc.setField("convolutionKernel");
		dsc.setValue("BONE");
		o = new Operator();
		o.setValue("=");
		dsc.setOperator(o);
		myList.add(dsc);

		qb.setStudyNumberMap(new StudyNumberMap());
		qb.setQueryCriteria(myList, "AND", am.getAuthorizedSites(), am.getAuthorizedSeriesSecurityGroups());
		qb.query();


		List<PatientSearchResult> patientDtos = qb.getPatients();
		System.out.println(patientDtos.size());
		Assert.assertEquals(patientDtos.size(), 1);

		Assert.assertEquals(patientDtos.get(0).getSubjectId(), "1.3.6.1.4.1.9328.50.3.0023");
	}

	@Test
	public void testOrQuery() throws Exception {
		AuthorizationManager am = new AuthorizationManager("kascice");

		List<DynamicSearchCriteria> myList = new ArrayList<DynamicSearchCriteria>();

		DynamicSearchCriteria dsc = new DynamicSearchCriteria();
		dsc.setDataGroup("CTImage");
		dsc.setField("exposure");
		dsc.setValue("960");
		Operator o = new Operator();
		o.setValue("=");
		dsc.setOperator(o);
		myList.add(dsc);

		dsc = new DynamicSearchCriteria();
		dsc.setDataGroup("CTImage");
		dsc.setField("convolutionKernel");
		dsc.setValue("BONE");
		o = new Operator();
		o.setValue("=");
		dsc.setOperator(o);
		myList.add(dsc);

		qb.setStudyNumberMap(new StudyNumberMap());
		qb.setQueryCriteria(myList, "OR", am.getAuthorizedSites(), am.getAuthorizedSeriesSecurityGroups());
		qb.query();


		List<PatientSearchResult> patientDtos = qb.getPatients();
		System.out.println(patientDtos.size());
		Assert.assertEquals(patientDtos.size(), 2);
	}

	@Test	
	public void testGetPermissibleData() throws Exception {

		qb.setStudyNumberMap(new StudyNumberMap());
		List<String> permissibleData = qb.getPermissibleData("gov.nih.nci.nbia.internaldomain",
                                                             "GeneralSeries",
                                                             "modality");
		Assert.assertEquals(permissibleData.size(), 1);
		Assert.assertEquals(permissibleData.get(0),"CT");

		permissibleData = qb.getPermissibleData("gov.nih.nci.nbia.internaldomain",
				                                "Study",
				                                "occupation");
		Assert.assertEquals(permissibleData.size(), 1);
        System.out.println("permissibleData:"+permissibleData.get(0));

		try {
			permissibleData = qb.getPermissibleData("garbage",
                                     				"garbage",
			                                        "garbage");
			Assert.fail("gbg should throw exception");
		}
		catch(Exception ex) {
			System.out.println("should get here");
		}
	}
	
    @Before
	public void setUp() throws Exception {
		URL url = TableRelationships.class.getClassLoader().getResource("relationship.xml");
		File file = new File(url.toURI());
		System.setProperty("jboss.server.data.dir", file.getParent());

		Util.loadSystemPropertiesFromPropertiesResource("ncia.properties");
	}	

    //////////////////////////////PROTECTED/////////////////////////////////

	@Autowired
	protected QueryHandler qb;
	
    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }




    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_1044.xml";


}
