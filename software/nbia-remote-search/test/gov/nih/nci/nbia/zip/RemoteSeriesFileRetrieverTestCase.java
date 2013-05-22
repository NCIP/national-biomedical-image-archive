/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.zip;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.nbia.dto.AnnotationFileDTO;
import gov.nih.nci.nbia.dto.DicomFileDTO;
import gov.nih.nci.nbia.dto.ImageFileDTO;
import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.cagrid.transfer.context.client.TransferServiceContextClient;
import org.cagrid.transfer.context.client.helper.TransferClientHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;
import org.cagrid.transfer.descriptor.DataTransferDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.apache.commons.io.*;

@RunWith(PowerMockRunner.class)
public class RemoteSeriesFileRetrieverTestCase {

	@Test
	public void testCleanupResultsDirectory() {
		RemoteSeriesFileRetriever remoteSeriesFileRetriever = new RemoteSeriesFileRetriever();

		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		File outputDir = new File(tempDir, "fakefakedir");
		outputDir.mkdirs();

		assertTrue(outputDir.exists());

		remoteSeriesFileRetriever.setOutputDirectory(outputDir);

		remoteSeriesFileRetriever.cleanupResultsDirectory();

		assertFalse(outputDir.exists());
	}

	@Test
	@PrepareForTest({RemoteSeriesFileRetriever.class, TransferClientHelper.class} )
	public void testRetrieveImages() throws Exception {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File resultsDir = new File(tempDir, "foo");
        resultsDir.mkdirs();

		EndpointReferenceType endpointReferenceType = new EndpointReferenceType();
		DataTransferDescriptor dataTransferDescriptor = new DataTransferDescriptor();

		String serviceAddress = "http://fakeAddress";

		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.setProject("proj1");
		seriesSearchResult.setPatientId("pat1");
		seriesSearchResult.setStudyInstanceUid("study1");
		seriesSearchResult.setSeriesInstanceUid("series1");
		seriesSearchResult.associateLocation(constructRemoteNode());

		//create mocks
		NCIACoreServiceClient nbiaServiceClientMock = createMock(NCIACoreServiceClient.class);
		TransferServiceContextReference transferServiceContextReferenceMock = createMock(TransferServiceContextReference.class);
		TransferServiceContextClient transferServiceContextClientMock = createMock(TransferServiceContextClient.class);
		mockStatic(TransferClientHelper.class);

		//set expectations for mock - so complex... maybe use a nice mock instead?
		expectNew(NCIACoreServiceClient.class, serviceAddress).
	        andReturn(nbiaServiceClientMock);
		expect(nbiaServiceClientMock.retrieveDicomDataBySeriesUID(seriesSearchResult.getSeriesInstanceUid())).
		    andReturn(transferServiceContextReferenceMock);
		expect(transferServiceContextReferenceMock.getEndpointReference()).
	        andReturn(endpointReferenceType);
		expectNew(TransferServiceContextClient.class, endpointReferenceType).
            andReturn(transferServiceContextClientMock);
		expect(transferServiceContextClientMock.getDataTransferDescriptor()).
            andReturn(dataTransferDescriptor);
		expect(TransferClientHelper.getData(dataTransferDescriptor)).
	        andReturn(createZipInputStream());
		expect(transferServiceContextClientMock.destroy()).
		    andReturn(null);

    	replay(nbiaServiceClientMock, NCIACoreServiceClient.class);
    	replay(transferServiceContextReferenceMock, TransferServiceContextReference.class);
    	replay(transferServiceContextClientMock, TransferServiceContextClient.class);
    	replay(TransferClientHelper.class);


		RemoteSeriesFileRetriever remoteSeriesFileRetriever = new RemoteSeriesFileRetriever();
		remoteSeriesFileRetriever.setOutputDirectory(resultsDir);
		remoteSeriesFileRetriever.setSeriesIdentifier("seriesId");
		DicomFileDTO image = remoteSeriesFileRetriever.retrieveImages(seriesSearchResult);
		List<ImageFileDTO> images = image.getImageFileDTOList();
		File seriesDir = new File(resultsDir, "proj1");
		seriesDir = new File(seriesDir, "pat1");
		seriesDir = new File(seriesDir, "study1");
		seriesDir = new File(seriesDir, "seriesId");

		assertEquals(2, images.size());
		if(images.get(0).getFileURI().endsWith("000000.dcm")) {
			assertEquals(images.get(0).getFileURI(), (new File(seriesDir,"000000.dcm")).getAbsolutePath());
			System.out.println(images.get(0).getFileURI());
			//assertTrue(images.get(0).getSize()==12);
			assertEquals(images.get(0).getSopInstanceUid(), "1234");

			assertEquals(images.get(1).getFileURI(), (new File(seriesDir,"000001.dcm")).getAbsolutePath());
			//assertTrue(images.get(1).getSize()==13);
			assertEquals(images.get(1).getSopInstanceUid(), "56789");
		}
		else
		if(images.get(0).getFileURI().endsWith("000001.dcm")) {
			assertEquals(images.get(0).getFileURI(), (new File(seriesDir,"000001.dcm")).getAbsolutePath());
			System.out.println(images.get(0).getFileURI());
			//assertTrue(images.get(0).getSize()==13);
			assertEquals(images.get(0).getSopInstanceUid(), "56789");

			assertEquals(images.get(1).getFileURI(), (new File(seriesDir,"000000.dcm")).getAbsolutePath());
			//assertTrue(images.get(1).getSize()==12);
			assertEquals(images.get(1).getSopInstanceUid(), "1234");
		}
		else {
			fail("not t1 nor t2");
		}

		remoteSeriesFileRetriever.cleanupResultsDirectory();

    	//verify the mock
    	verify(nbiaServiceClientMock, NCIACoreServiceClient.class);
    	verify(transferServiceContextReferenceMock, TransferServiceContextClient.class);
    	verify(TransferClientHelper.class);

	}

	@Test
	public void testRetrieveAnnotations() {
		RemoteSeriesFileRetriever remoteSeriesFileRetriever = new RemoteSeriesFileRetriever();
		List<AnnotationFileDTO> annotations = remoteSeriesFileRetriever.retrieveAnnotations(null);
		assertEquals(annotations.size(),0);
	}

	///////////////////////////////////////////PRIVATE//////////////////////////////////////

    private static InputStream createZipInputStream() throws Exception {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));

        File tzFile = new File(tempDir, "tz1.zip");
        tzFile.deleteOnExit();

        ClassLoader cl = RemoteSeriesFileRetrieverTestCase.class.getClassLoader();
        InputStream firstDicom  = cl.getResourceAsStream("1234.dcm");
        InputStream secondDicom  = cl.getResourceAsStream("56789.dcm");

        FileOutputStream fos = new FileOutputStream(tzFile);
        ZipOutputStream zos = new ZipOutputStream(fos);

        zos.putNextEntry(new ZipEntry("000000.dcm"));
        IOUtils.copy(firstDicom, zos);
        zos.closeEntry();

        zos.putNextEntry(new ZipEntry("000001.dcm"));
        IOUtils.copy(secondDicom, zos);;
        zos.closeEntry();

        zos.close();
        firstDicom.close();
        secondDicom.close();
        
        return new FileInputStream(tzFile);
    }

	private static RemoteNode constructRemoteNode() throws Exception {
		ServiceMetadata serviceMetadata = new ServiceMetadata();
		ServiceMetadataHostingResearchCenter serviceMetadataHostingResearchCenter = new ServiceMetadataHostingResearchCenter();
		ResearchCenter researchCenter = new ResearchCenter();
		researchCenter.setDisplayName("foo");
		serviceMetadataHostingResearchCenter.setResearchCenter(researchCenter);
		serviceMetadata.setHostingResearchCenter(serviceMetadataHostingResearchCenter);

		EndpointReferenceType endpointReferenceType = new EndpointReferenceType();
		endpointReferenceType.setAddress(new AttributedURI("http://fakeAddress"));
		AvailableSearchTerms availableSearchTerms = new AvailableSearchTerms();
		RemoteNode remoteNode = new RemoteNode(serviceMetadata,
				                               endpointReferenceType,
				                               availableSearchTerms);
		return remoteNode;
	}
}
