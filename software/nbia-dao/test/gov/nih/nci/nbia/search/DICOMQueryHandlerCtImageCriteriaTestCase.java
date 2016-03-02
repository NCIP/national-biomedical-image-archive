/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.ncia.criteria.AcquisitionMatrixCriteria;
import gov.nih.nci.ncia.criteria.AuthorizationCriteria;
import gov.nih.nci.ncia.criteria.ConvolutionKernelCriteria;
import gov.nih.nci.ncia.criteria.DataCollectionDiameterCriteria;
import gov.nih.nci.ncia.criteria.DxDataCollectionDiameterCriteria;
import gov.nih.nci.ncia.criteria.KilovoltagePeakDistribution;
import gov.nih.nci.ncia.criteria.ReconstructionDiameterCriteria;
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

public class DICOMQueryHandlerCtImageCriteriaTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testCtImageCriteria() throws Exception {
		testKiloVoltagePeakDistributionJustFrom();
		testKiloVoltagePeakDistributionFromAndTo();
		
		testReconstructionDiameterJustFrom();
		testReconstructionDiameterFromAndTo();
		
		testCtDataCollectionDiameterJustFrom();
		testCtDataCollectionDiameterFromAndTo();
		
		testDxDataCollectionDiameterJustFrom();
		testDxDataCollectionDiameterFromAndTo();
		
		testConvolutionKernel();
		testAcquisitionMatrixJustFrom();
		
		testAcquisitionMatrixFromAndTo();
	}

    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }

    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/collections_testdata.xml";
    @Autowired
    private DICOMQueryHandler dicomQueryHandler;

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

	private void testKiloVoltagePeakDistributionJustFrom() throws Exception {
		KilovoltagePeakDistribution kilovoltagePeakDistribution = new KilovoltagePeakDistribution("<" ,"80",
				                                                                                  "", "");

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(kilovoltagePeakDistribution);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue(resultSets.size() == 0);

		kilovoltagePeakDistribution = new KilovoltagePeakDistribution("=" ,"80", "", "");
		dicomQuery.setCriteria(kilovoltagePeakDistribution);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue(resultSets.size() == 2);

		kilovoltagePeakDistribution = new KilovoltagePeakDistribution(">=" ,"80", "", "");
		dicomQuery.setCriteria(kilovoltagePeakDistribution);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue(resultSets.size() == 8);

		kilovoltagePeakDistribution = new KilovoltagePeakDistribution(">" ,"120", "", "");
		dicomQuery.setCriteria(kilovoltagePeakDistribution);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue(resultSets.size() == 0);
		
		testKiloVoltagePeakDistributionFromAndTo();
	}

	private void testKiloVoltagePeakDistributionFromAndTo() throws Exception {
		KilovoltagePeakDistribution kilovoltagePeakDistribution = new KilovoltagePeakDistribution(">" ,"80",
				                                                                                  "<", "120");

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(kilovoltagePeakDistribution);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 0);

		kilovoltagePeakDistribution = new KilovoltagePeakDistribution(">=" ,"80", "<=", "120");
		dicomQuery.setCriteria(kilovoltagePeakDistribution);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 8);

		//this is goofy but acts like =80
		kilovoltagePeakDistribution = new KilovoltagePeakDistribution("=" ,"80", "<=", "120");
		dicomQuery.setCriteria(kilovoltagePeakDistribution);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 2);
	}
	
	private void testReconstructionDiameterJustFrom() throws Exception {
		ReconstructionDiameterCriteria reconstructionDiameterCriteria = new ReconstructionDiameterCriteria("<" ,"300",
				                                                                                           "", "");

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(reconstructionDiameterCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 0);

	    /////////////////////
	    reconstructionDiameterCriteria = new ReconstructionDiameterCriteria("=" ,"300",
                                                                            "", "");

		dicomQuery.setCriteria(reconstructionDiameterCriteria);

        resultSets = dicomQueryHandler.findTriples(dicomQuery);
        Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 1);

	    /////////////////////
	    reconstructionDiameterCriteria = new ReconstructionDiameterCriteria(">=" ,"300",
                                                                            "", "");

		dicomQuery.setCriteria(reconstructionDiameterCriteria);

        resultSets = dicomQueryHandler.findTriples(dicomQuery);
        Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 8);

	    /////////////////////
	    reconstructionDiameterCriteria = new ReconstructionDiameterCriteria(">" ,"300",
                                                                            "", "");

		dicomQuery.setCriteria(reconstructionDiameterCriteria);

        resultSets = dicomQueryHandler.findTriples(dicomQuery);
        Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 7);
	}
	
	private void testReconstructionDiameterFromAndTo() throws Exception {
		ReconstructionDiameterCriteria reconstructionDiameterCriteria = new ReconstructionDiameterCriteria(">=" ,"300",
				                                                                                           "<=", "310");

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(reconstructionDiameterCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 2);

	    /////////////////////
	    reconstructionDiameterCriteria = new ReconstructionDiameterCriteria("=" ,"300",
                                                                            "<=", "310");

		dicomQuery.setCriteria(reconstructionDiameterCriteria);

        resultSets = dicomQueryHandler.findTriples(dicomQuery);

        Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 1);
	}	
	
	
	private void testCtDataCollectionDiameterJustFrom() throws Exception {
		DataCollectionDiameterCriteria dataCollectionDiameterCriteria = new DataCollectionDiameterCriteria("=" ,"500.084",
				                                                                                           "", "");

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(dataCollectionDiameterCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);

		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 1);

	    /////////////////////
	}
	
	private void testCtDataCollectionDiameterFromAndTo() throws Exception {
		DataCollectionDiameterCriteria dataCollectionDiameterCriteria = new DataCollectionDiameterCriteria(">" ,"500",
				                                                                                           "<", "501");

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(dataCollectionDiameterCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);

		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 1);

	    /////////////////////

		dataCollectionDiameterCriteria = new DataCollectionDiameterCriteria(">=" ,"500",
		                                                                    "<", "500.1");

		dicomQuery.setCriteria(dataCollectionDiameterCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);

		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 8);
	}	
	
	private void testDxDataCollectionDiameterJustFrom() throws Exception {
		DxDataCollectionDiameterCriteria dxDataCollectionDiameterCriteria = new DxDataCollectionDiameterCriteria("=" ,"1",
				                                                                                           "", "");

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(dxDataCollectionDiameterCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);

		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 1);

	    /////////////////////

		dxDataCollectionDiameterCriteria = new DxDataCollectionDiameterCriteria("<" ,"1",
                "", "");

		dicomQuery.setCriteria(dxDataCollectionDiameterCriteria);
		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				   resultSets.size() == 7);

	}

	private void testDxDataCollectionDiameterFromAndTo() throws Exception {
		DxDataCollectionDiameterCriteria dxDataCollectionDiameterCriteria = new DxDataCollectionDiameterCriteria(">" ,"0",
  			                                                                                                  "<", "2");

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(dxDataCollectionDiameterCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);

		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 1);
	}	
	
	private void testConvolutionKernel() throws Exception {
		Collection<String> convolutionKernels = new ArrayList<String>();
		convolutionKernels.add("B30f");
		ConvolutionKernelCriteria convolutionKernelCriteria = new ConvolutionKernelCriteria();
		convolutionKernelCriteria.setConvolutionKernelValues(convolutionKernels);

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(convolutionKernelCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);

		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 2);

	    /////////////////////

		convolutionKernelCriteria = new ConvolutionKernelCriteria();
		convolutionKernels.clear();
		convolutionKernels.add("garbage_kernel");
		convolutionKernelCriteria.setConvolutionKernelValues(convolutionKernels);
		dicomQuery.setCriteria(convolutionKernelCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);

		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 0);

	    /////////////////////

		convolutionKernelCriteria = new ConvolutionKernelCriteria();
		convolutionKernels.clear();
		convolutionKernels.add("B30f");
		convolutionKernels.add("SOFT");
		convolutionKernelCriteria.setConvolutionKernelValues(convolutionKernels);
		dicomQuery.setCriteria(convolutionKernelCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);

		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 4);
	}

	private void testAcquisitionMatrixJustFrom() throws Exception {
		AcquisitionMatrixCriteria acquisitionMatrixCriteria = new AcquisitionMatrixCriteria("=" ,"96",
				                                                                            "", "");

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(acquisitionMatrixCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 1);

	    /////////////////////

	    acquisitionMatrixCriteria = new AcquisitionMatrixCriteria("<" ,"96",
				                                                  "", "");

		dicomQuery.setCriteria(acquisitionMatrixCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 6);

		/////////////////////

	    acquisitionMatrixCriteria = new AcquisitionMatrixCriteria(">" ,"96",
				                                                  "", "");

		dicomQuery.setCriteria(acquisitionMatrixCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 1);

		/////////////////////

	    acquisitionMatrixCriteria = new AcquisitionMatrixCriteria(">=" ,"96",
				                                                  "", "");

		dicomQuery.setCriteria(acquisitionMatrixCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 2);

		/////////////////////

	    acquisitionMatrixCriteria = new AcquisitionMatrixCriteria("<=" ,"96",
				                                                  "", "");

		dicomQuery.setCriteria(acquisitionMatrixCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 7);
	}
		
	private void testAcquisitionMatrixFromAndTo() throws Exception {
		AcquisitionMatrixCriteria acquisitionMatrixCriteria = new AcquisitionMatrixCriteria(">=" ,"96",
				                                                                            "<=", "128");

		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setCriteria(acquisitionMatrixCriteria);
		dicomQuery.setCriteria(createAuthorizationCriteria());

		List<PatientStudySeriesTriple> resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 2);

	    /////////////////////

	    acquisitionMatrixCriteria = new AcquisitionMatrixCriteria(">" ,"96",
				                                                  "<=", "128");

		dicomQuery.setCriteria(acquisitionMatrixCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 1);

		/////////////////////

	    acquisitionMatrixCriteria = new AcquisitionMatrixCriteria(">" ,"100",
				                                                  "<", "129");

		dicomQuery.setCriteria(acquisitionMatrixCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 1);

		/////////////////////

	    acquisitionMatrixCriteria = new AcquisitionMatrixCriteria(">" ,"130",
				                                                  "<", "200");

		dicomQuery.setCriteria(acquisitionMatrixCriteria);

		resultSets = dicomQueryHandler.findTriples(dicomQuery);
		Assert.assertTrue("manufacturer criteria should yield non-zero results",
				    resultSets.size() == 0);
	}	
}
