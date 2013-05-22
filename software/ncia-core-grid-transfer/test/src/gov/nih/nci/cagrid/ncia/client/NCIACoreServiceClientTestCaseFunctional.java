/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 *
 */
package gov.nih.nci.cagrid.ncia.client;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.ivi.utils.ZipEntryInputStream;
import gov.nih.nci.ncia.domain.Annotation;
import gov.nih.nci.ncia.domain.Image;
import gov.nih.nci.ncia.domain.Patient;
import gov.nih.nci.ncia.domain.Series;
import gov.nih.nci.ncia.domain.Study;
import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultImpl;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.PatientSearchResultImpl;
import gov.nih.nci.ncia.search.SearchCriteriaDTO;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.ncia.search.StudySearchResult;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.ZipInputStream;

import javax.xml.namespace.QName;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.cagrid.transfer.context.client.TransferServiceContextClient;
import org.cagrid.transfer.context.client.helper.TransferClientHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;
import org.cagrid.transfer.descriptor.DataTransferDescriptor;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

/**
 * @author lethai
 *
 */
public class NCIACoreServiceClientTestCaseFunctional {
	String gridServiceUrl;
	{
		if(System.getProperty("grid.service.url")==null) {
			gridServiceUrl = "http://localhost:21080/wsrf/services/cagrid/NCIACoreService";
		}
		else {
			gridServiceUrl = System.getProperty("grid.service.url");
		}			
	}
	//String gridServiceUrl = "http://nciavgriddev5004.nci.nih.gov:18080/wsrf/services/cagrid/NCIACoreService";
	//String gridServiceUrl = "http://imaging4-qa.nci.nih.gov/wsrf/services/cagrid/NCIACoreService";
	//String localDownloadLocation = System.getProperty("java.io.tmpdir") + File.separator + "NCIAGridClientDownload";
	//String gridServiceUrl = "http://localhost:21080/wsrf/services/cagrid/NCIACoreService";
	//String gridServiceUrl = "http://imaging.nci.nih.gov/wsrf/services/cagrid/NCIACoreService";
	//String gridServiceUrl = "http://imaging4-qa.nci.nih.gov/wsrf/services/cagrid/NCIACoreService";
	//String gridServiceUrl = "http://localhost:8080/wsrf/services/cagrid/NCIACoreService";

	String clientDownLoadLocation ="NBIAGridClientDownLoad";

	@Test	
	public void testAnnotation() throws Exception {
		System.out.println("Retrieving Annotation");
		String filename = "test/resources/testCase11.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);
		
		long start = System.currentTimeMillis();
		// Execute Query
		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);
		long end = System.currentTimeMillis();		
		System.out.println("Total time to get meta data and print out result for testcase 1: " + (end - start) + " milli seconds");
		
		dumpCQLResult(result);
	}

	/**
	 * Test method for {@link gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient#retrieveDicomData(gov.nih.nci.cagrid.cqlquery.CQLQuery)}.
	 */
	@Test	
	public void testRetrieveDicomData() throws Exception {
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);
		Assert.assertNotNull("Connection with remote grid service could not be opened", client);

		String filename = "test/resources/testCase8.xml";
		CQLQuery newQuery = null;

		InputSource queryInput = new InputSource(new FileReader(filename));
		newQuery = (CQLQuery) ObjectDeserializer.deserialize(queryInput, CQLQuery.class);
		System.err.println(ObjectSerializer.toString(newQuery,
				new QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "CQLQuery")));

		Assert.assertNotNull(newQuery);

		TransferServiceContextReference tscr = client.retrieveDicomData(newQuery);

		downloadZipFileAndUnzip(tscr);
	}


	/**
	 * Test method for {@link gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient#retrieveDicomDataByNthStudyTimepointForPatient(java.lang.String, int)}.
	 */
	@Test	
	public void testRetrieveDicomDataByNthStudyTimepointForPatient() throws Exception {
		String patientId = "1.3.6.1.4.1.9328.50.14.0001";
		System.out.println("PatientId : " + patientId);
		int studyNumber = 2;

		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);
		Assert.assertNotNull("Connection with remote grid service could not be opened", client);

		System.out.println("............using transferService to retrieve data.................");
		TransferServiceContextReference tscr = client.retrieveDicomDataByNthStudyTimePointForPatient(patientId, studyNumber);

		downloadZipFileAndUnzip(tscr);
	}

	/**
	 * Test method for {@link gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient#retrieveDicomDataByPatientId(java.lang.String)}.
	 */
	@Test	
	public void testRetrieveDicomDataByPatientId() throws Exception {
		String patientId = "1.3.6.1.4.1.9328.50.14.0007";
		System.out.println("PatientId : " + patientId);

		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);
		Assert.assertNotNull("Connection with remote grid service could not be opened", client);


		System.out.println("............using transferService to retrieve data.................");
		TransferServiceContextReference tscr = client.retrieveDicomDataByPatientId(patientId);

		downloadZipFileAndUnzip(tscr);
	}

	/**
	 * Test method for {@link gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient#retrieveDicomDataBySeriesUID(java.lang.String)}.
	 */
	@Test	
	public void testRetrieveDicomDataBySeriesUID() throws Exception {
		String seriesInstanceUID = "1.3.6.1.4.1.9328.50.1.8862";
		//seriesInstanceUID = "1.2.840.113704.1.111.4076.1187279953.16";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.14.1344";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.14.868";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.14.3132";
		//seriesInstanceUID ="1.3.6.1.4.1.9328.50.6.555";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.14.4052";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.4.2";
		seriesInstanceUID = "1.3.6.1.4.1.9328.50.4.44589";
		seriesInstanceUID = "1.2.840.113704.1.111.3372.1187274264.6";
		System.out.println("seriesInstanceUID : " + seriesInstanceUID);
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);

		Assert.assertNotNull("Connection with remote grid service could not be opened", client);

		System.out.println("............using transferService to retrieve data.................");
		TransferServiceContextReference tscr = client.retrieveDicomDataBySeriesUID(seriesInstanceUID);
		downloadZipFileAndUnzip(tscr);	
	}

	/**
	 * Test method for {@link gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient#retrieveDicomDataByStudyUID(java.lang.String)}.
	 */
	@Test	
	public void testRetrieveDicomDataByStudyUID() throws Exception {
		String studyInstanceUID = "1.3.6.1.4.1.9328.50.1.4434";
		//studyInstanceUID ="1.3.6.1.4.1.9328.50.1.12324";
		//studyInstanceUID = "1.3.6.1.4.1.9328.50.1.8858";
		//studyInstanceUID = "1.3.6.1.4.1.9328.50.1.12324";
		//studyInstanceUID = "1.3.6.1.4.1.9328.50.1.139";
		//studyInstanceUID = "1.3.6.1.4.1.9328.50.6.3294";
		//studyInstanceUID = "1.2.840.113704.1.111.2408.1187279444.3";
		//studyInstanceUID = "1.3.6.1.4.1.9328.50.45.326662079066250663678557696078244480878";
		studyInstanceUID = "1.3.6.1.4.1.9328.50.45.275881025454183713545354420382217269222";

		System.out.println("studyInstanceUID : "+ studyInstanceUID);

		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);

		Assert.assertNotNull("Connection with remote grid service could not be opened", client);

		System.out.println("............using transferService to retrieve data.................");
		TransferServiceContextReference tscr = client.retrieveDicomDataByStudyUID(studyInstanceUID);
		downloadZipFileAndUnzip(tscr);	
	}

	/**
	 * Test method for {@link gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient#retrieveDicomDataByPatientIds(java.lang.String[])}.
	 */
	@Test	
	public void testRetrieveDicomDataByPatientIds() throws Exception {
		String fileName = "1.3.6.1.4.1.9328.50.1.0042.zip";

		//private patient: 1.3.6.1.4.1.9328.50.3.0015, E09932
		String [] patientArray = new String[1];
		/*patientArray[0] = "1.3.6.1.4.1.9328.50.14.0007";
		patientArray[1] = "SNMCB04";*/
		patientArray[0] = "1.3.6.1.4.1.9328.50.4.0042";
		/*patientArray[1] = "1.3.6.1.4.1.9328.50.4.0045";
		patientArray[2] = "1.3.6.1.4.1.9328.50.4.0049";
		patientArray[3] = "1.3.6.1.4.1.9328.50.4.0052";
		patientArray[4] = "1.3.6.1.4.1.9328.50.4.0019";
		patientArray[5] = "1.3.6.1.4.1.9328.50.4.0043";
		patientArray[6] = "1.3.6.1.4.1.9328.50.4.0080";
		patientArray[7] = "1.3.6.1.4.1.9328.50.4.0132";
		patientArray[8] = "1.3.6.1.4.1.9328.50.4.0104";
		patientArray[9] = "1.3.6.1.4.1.9328.50.4.0123";
		patientArray[10] = "1.3.6.1.4.1.9328.50.4.0127";
		patientArray[11] = "1.3.6.1.4.1.9328.50.4.0136";	*/


		/*patientArray[1] = "TCGA-06-0137";
		patientArray[1] = "TCGA-06-0147";
		patientArray[1] = "TCGA-06-0148";
		patientArray[1] = "TCGA-06-0164";
		patientArray[1] = "TCGA-06-0173";
		patientArray[1] = "TCGA-06-0176";
		patientArray[1] = "TCGA-06-0178";
		 */

		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);
		Assert.assertNotNull("Connection with remote grid service could not be opened", client);
		
		long start = System.currentTimeMillis();

		TransferServiceContextReference tscr = client.retrieveDicomDataByPatientIds(patientArray);
		downloadFile(tscr, fileName);

		long end = System.currentTimeMillis();
		System.out.println("Total time download images is " + (end - start) + " milli seconds");
	}

	/**
	 * Test method for {@link gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient#retrieveDicomDataBySeriesUIDs(java.lang.String[])}.
	 */
	@Test	
	public void testRetrieveDicomDataBySeriesUIDs() throws Exception {
		String fileName ="1.2.840.113704.1.111.4076.1187279953.16.zip";
		//String seriesInstanceUID = "1.3.6.1.4.1.9328.50.1.8862";
		//String seriesInstanceUID = "1.2.840.113704.1.111.4076.1187279953.16";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.14.1344";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.14.868";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.14.3132";
		//seriesInstanceUID ="1.3.6.1.4.1.9328.50.6.555";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.14.4052";
		//private series: 1.2.840.113619.2.134.1762388187.1967.1102076182.901; 1.2.840.113619.2.134.1762388187.1967.1102076183.45
		String [] seriesArray = new String[4];
		seriesArray[0] = "1.2.840.113704.1.111.4076.1187279953.16";
		seriesArray[1] = "1.3.6.1.4.1.9328.50.14.1344";
		seriesArray[2] = "1.3.6.1.4.1.9328.50.1.8862";
		seriesArray[3] = "1.2.840.113619.2.134.1762388187.1967.1102076182.901";

		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);

		Assert.assertNotNull("Connection with remote grid service could not be opened", client);
		long start = System.currentTimeMillis();

        TransferServiceContextReference tscr = client.retrieveDicomDataBySeriesUIDs(seriesArray);
		downloadFile(tscr, fileName);

		long end = System.currentTimeMillis();
		System.out.println("Total time download images is " + (end - start) + " milli seconds");
	}

	/**
	 * Test method for {@link gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient#retrieveDicomDataByStudyUIDs(java.lang.String[])}.
	 */
	@Test	
	public void testRetrieveDicomDataByStudyUIDs() throws Exception {
		String fileName = "1.3.6.1.4.1.9328.50.1.8858.zip";
		//private study 1.2.124.113532.128.231.96.56.20041202.111945.1331778;
		//1.2.124.113532.128.231.96.56.20050317.110736.1403277

		String [] studyArray = new String[1];
		//studyArray[0] = "1.3.6.1.4.1.9328.50.45.326662079066250663678557696078244480878";
		//studyArray[0] = "1.3.6.1.4.1.9328.50.45.275881025454183713545354420382217269222";
		//studyArray[0] = "1.3.6.1.4.1.9328.50.45.244356231255766995137159318570729147227";
		//studyArray[0] = "1.3.6.1.4.1.9328.50.45.278844795194298910084884570122397123437";
		//studyArray[0] = "1.3.6.1.4.1.9328.50.45.277258831415090891309462723056931217706";
		//studyArray[0] = "1.3.6.1.4.1.9328.50.45.198999303901323755527019586055500288037";
		studyArray[0] = "1.3.6.1.4.1.9328.50.45.240457512892149075774394662651059696506";
		//studyArray[0] = "1.3.6.1.4.1.9328.50.45.326662079066250663678557696078244480878";
		//studyArray[0] = "1.3.6.1.4.1.9328.50.1.8858";
		//studyArray[1] = "1.3.6.1.4.1.9328.50.6.3294";
		//studyArray[2] = "1.3.6.1.4.1.9328.50.1.4434";
		//studyArray[3] = "1.3.6.1.4.1.9328.50.6.3294";

		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);

		Assert.assertNotNull("Connection with remote grid service could not be opened", client);
		TransferServiceContextReference tscr = client.retrieveDicomDataByStudyUIDs(studyArray);
		downloadFile(tscr, fileName);
	}

	@Test	
	public void testStudy() throws Exception {
		System.out.println("Test case - Retrieve Study for patientID 1.3.6.1.4.1.9328.50.1.0019");
		String filename = "test/resources/testCase2.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);

		dumpCQLResult(result);
	}

	@Test	
	public void testStudy1() throws Exception {
		System.out.println("Test case - Retrieve Study for patientid 1.3.6.1.4.1.9328.50.1.0025");
		String filename = "test/resources/testCase2a.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);
		
		dumpCQLResult(result);
	}

	@Test	
	public void testStudy2() throws Exception {
		System.out.println("Test case  - Retrieve Study for study InstanceUID 1.3.6.1.4.1.9328.50.1.12324");
		String filename = "test/resources/testCase2b.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);

		dumpCQLResult(result);
	}
	
	@Test	
	public void testStudyWithORGroup() throws Exception {
		System.out.println("Test case - Retrieve Study ");
		String filename = "test/resources/testStudy.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);

		dumpCQLResult(result);
	}

	@Test	
	public void testSeries() throws Exception{
		System.out.println("Test case 3 - retrieving series");
		String filename = "test/resources/testCase3.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);

		dumpCQLResult(result);
	}

	@Test	
	public void testSeries1() throws Exception{
		System.out.println("Test case  - retrieve Series given patientid, studyInstanceUID and series InstanceUID");
		String filename = "test/resources/testCase3a.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);
		dumpCQLResult(result);
	}

	@Test	
	public void testSeries2() throws Exception{
		System.out.println("Test case 10. Trying to retrieve private series");
		String filename = "test/resources/testCase10.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);

		dumpCQLResult(result);
	}
	
	@Test
	public void testImage() throws Exception{
		System.out.println("Test case 4 - retrieve Images");
		//String filename = "test/resources/testImage.xml";
		String filename = "test/resources/testCase4.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);
		dumpCQLResult(result);
	}

	@Test
	public void testImage1() throws Exception{
		System.out.println("Test case 9 - Retrieve Images for series 1.3.6.1.4.1.9328.50.3.33747. This series is private. We should not get anything back.");
		String filename = "test/resources/testCase9.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);
		
		dumpCQLResult(result);
	}

	@Test
	public void testPatient() throws Exception{
		System.out.println("Test case 1 - Retrieve Patient for project RIDER");
		String filename = "test/resources/testCase1.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);
		dumpCQLResult(result);
	}

	@Test
	public void testPatient1() throws Exception{
		System.out.println("Test case 5 - retrieve Patient where patientID is 1.3.6.1.4.1.9328.50.1.0025");
		String filename = "test/resources/testCase5.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);
		dumpCQLResult(result);
	}

	@Test
	public void testPatient2() throws Exception{
		System.out.println("Test case 6 - retrieve Patient where project is Phantom and modalities are CT and PT");
		String filename = "test/resources/testCase6.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);
		dumpCQLResult(result);
	}

	@Test
	public void testPatientCaseInSensitive() throws Exception{
		System.out.println("Test case 1 - Retrieve Patient");
		String filename = "test/resources/testPatientCaseInSensitive.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);
		dumpCQLResult(result);
	}
	
	@Test
	public void testPatientWithORGroup() throws Exception{
		System.out.println("Test case 1 - Retrieve Patient ");
		String filename = "test/resources/testPatient.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);

		CQLQueryResults result = this.connectAndExecuteQuery(fcqlq);
		dumpCQLResult(result);
	}

	@Test
	public void testEPRname() throws Exception {
		TransferServiceContextClient tclient = null;

		String subTarget = gridServiceUrl.substring(gridServiceUrl.indexOf("http://"), gridServiceUrl.indexOf("wsrf"));

		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);

		String filename = "test/resources/testCase8.xml";
		CQLQuery newQuery = null;

		InputSource queryInput = new InputSource(new FileReader(filename));
		newQuery = (CQLQuery) ObjectDeserializer.deserialize(queryInput, CQLQuery.class);
		System.err.println(ObjectSerializer.toString(newQuery,
		                   new QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "CQLQuery")));
		Assert.assertNotNull(newQuery);


		System.out.println("............using transferService to retrieve data.................");
		TransferServiceContextReference tscr = client.retrieveDicomData(newQuery);
		EndpointReferenceType oldEpr = tscr.getEndpointReference();
		System.out.println("This should point to imaging/wsrf");
		System.out.println(oldEpr.toString());
		String strOldEpr = oldEpr.toString();
		String subOldEpr = strOldEpr.substring(strOldEpr.indexOf("http://"), strOldEpr.indexOf("wsrf"));
		System.out.println("subTarget = " + subTarget + " | subOldEpr = " + subOldEpr);
		if (!subOldEpr.equals(subTarget)){
			Assert.fail("the End Point Reference is not the same.  external user cannot access the DICOM data.");
		}
		tclient = new TransferServiceContextClient(oldEpr);

		DataTransferDescriptor tempDD = tclient.getDataTransferDescriptor();
		System.out.println("This should point to imaging/caGridTransfer");
		System.out.println(tempDD.getUrl());
	}

	@Test
	public void testGetNumberOfStudyTimepointForPatient() throws Exception {
		String patientId = "1.3.6.1.4.1.9328.50.1.0002";
		//patientId = "SNMCB04";
		//patientId = "1.3.6.1.4.1.9328.50.3.0015";

		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);
		int numberOfStudies = client.getNumberOfStudyTimePointForPatient(patientId);
		System.out.println("number of study time point for  patientid: " + patientId + " is " +numberOfStudies);
	}

	/**
	 * Test method for {@link gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient#getRepresentativeImageBySeries(java.lang.String)}.
	 */
	@Test
	public void testGetRepresentativeImageBySeries() throws Exception {
		String seriesInstanceUID = "1.3.6.1.4.1.9328.50.1.8862";
		//seriesInstanceUID = "1.2.840.113704.1.111.4076.1187279953.16";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.14.1344";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.14.868";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.14.3132";
		//seriesInstanceUID ="1.3.6.1.4.1.9328.50.6.555";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.14.4052";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.4.2";
		//seriesInstanceUID = "1.3.6.1.4.1.9328.50.4.44589";
		//seriesInstanceUID = "1.2.840.113704.1.111.3372.1187274264.6";
		//seriesInstanceUID = "1.2.840.113619.2.99.26.1046702390.711051";
		System.out.println("seriesInstanceUID : " + seriesInstanceUID);
		long start = System.currentTimeMillis();

		NCIACoreServiceClient	client = new NCIACoreServiceClient(gridServiceUrl);
		Image image = client.getRepresentativeImageBySeries(seriesInstanceUID);
		System.out.println("printing result: ");
		System.out.println("sopInstanceUID: " + image.getSopInstanceUID() + "\tinstanceNumber: " + image.getInstanceNumber() +
					"\t" + image.getAcquisitionDatetime() + image.getAcquisitionTime() + image.getAnatomicRegionSequence() );

		long end = System.currentTimeMillis();
		System.out.println("Total time get image is " + (end - start) + " milli seconds");

	}
	
	@Test
	public void testGetAvailableSearchTerms() throws Exception {
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);
		AvailableSearchTerms ast = client.getAvailableSearchTerms();

		System.out.println("AST:");
		System.out.println(Arrays.toString(ast.getAnatomicSites()));
		System.out.println(Arrays.toString(ast.getCollections()));
		System.out.println(Arrays.toString(ast.getConvolutionKernels()));
		System.out.println(Arrays.toString(ast.getModalities()));
//		System.out.println(Arrays.toString(ast.getUsMultiModalities()));

		//assert as if we are pointing to DEV?
	}

	@Test
	public void testSearchForPatients() throws Exception {
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);
		PatientSearchResult[] results = client.searchForPatients(new SearchCriteriaDTO[]{});

		//assert as if we are pointing to DEV?

		if(results==null) {
			System.out.println("no patients");
		}
		else {
			System.out.println("num patients:"+results.length);
		}
	}

	@Test
	public void testRetrieveStudyAndSeriesForPatient() throws Exception {
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);

		PatientSearchResultImpl patient = new PatientSearchResultImpl();
		patient.setId(1);

		StudySearchResult[] results = client.retrieveStudyAndSeriesForPatient(patient);

		//assert as if we are pointing to DEV?

		if(results==null) {
			System.out.println("no studies for patient id =1");
		}
		else {
			System.out.println("num studies:"+results.length);
			for(StudySearchResult result : results) {
				System.out.println("study uid:"+result.getStudyInstanceUid());
			}
		}
	}

	@Test
	public void testRetrieveImagesForSeries() throws Exception {
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);

		SeriesSearchResult series = new SeriesSearchResult();
		series.setId(1);

		ImageSearchResult[] results = client.retrieveImagesForSeries(series);

		//assert as if we are pointing to DEV?

		if(results==null) {
			System.out.println("no images for series id =1");
		}
		else {
			System.out.println("num imgs:"+results.length);
			for(ImageSearchResult result : results) {
				System.out.println("sop uid:"+result.getSopInstanceUid());
			}
		}
	}

	@Test
	public void testViewDicomHeader() throws Exception {
		NCIACoreServiceClient client = new NCIACoreServiceClient(gridServiceUrl);

		ImageSearchResultImpl image = new ImageSearchResultImpl();
		image.setId(1);
		DicomTagDTO[] results = client.viewDicomHeader(image);

		//assert as if we are pointing to DEV?

		if(results==null) {
			System.out.println("no images for series id =1");
		}
		else {
			System.out.println("num dicom tags:"+results.length);
			for(DicomTagDTO result : results) {
				System.out.println("dicom tag:"+result.getName());
			}
		}
	}


	/////////////////////////////////////////PRIVATE////////////////////////////
	private CQLQuery loadXMLFile(String filename) throws Exception {
		CQLQuery newQuery = null;

		InputSource queryInput = new InputSource(new FileReader(filename));
		newQuery = (CQLQuery) ObjectDeserializer.deserialize(queryInput, CQLQuery.class);
		System.err.println(ObjectSerializer.toString(newQuery,
				new QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "CQLQuery")));

		Assert.assertNotNull(newQuery);
		return newQuery;
	}


	private CQLQueryResults connectAndExecuteQuery(CQLQuery cqlQuery) throws Exception {

		CQLQueryResults result = null;
		NCIACoreServiceClient nciaClient = new NCIACoreServiceClient(gridServiceUrl);

		Assert.assertNotNull("Connection with remote grid service could not be opened", nciaClient);
		result = nciaClient.query(cqlQuery);
		return result;
	}

	private String downloadLocation(){
		String localClient= System.getProperty("java.io.tmpdir") + File.separator + clientDownLoadLocation;
		if(!new File(localClient).exists()){
			new File(localClient).mkdir();
		}
		System.out.println("Local download location: "+localClient);
		return localClient;
	}
	
	private void downloadFile(TransferServiceContextReference tscr,
	                          String fileNameToSaveTo) throws Exception {
		TransferServiceContextClient tclient = new TransferServiceContextClient(tscr.getEndpointReference());

        InputStream istream = TransferClientHelper.getData(tclient.getDataTransferDescriptor());

		System.out.println("getting zip input stream.............." + istream);
		Assert.assertNotNull("Input stream recieved from transfer service is null", istream);

		if(istream == null){
			System.out.println("istrea is null");
			return;
		}

		File f=new File(downloadLocation() + File.separator + fileNameToSaveTo);
		try {
		    OutputStream out=new FileOutputStream(f);
		    byte buf[]=new byte[1024];
		    int len;
		    while((len=istream.read(buf))>0){
		        out.write(buf,0,len);
		    }
		    out.flush();
		    out.close();
		    istream.close();
		}
		catch (EOFException e) {
			System.out.println("end of file");
            //break;
		}

        tclient.destroy();
	}
	
	private void downloadZipFileAndUnzip(TransferServiceContextReference tscr) throws Exception  {
		long start = System.currentTimeMillis();

		InputStream istream = null;
		TransferServiceContextClient tclient = null;	
		System.out.println("tscr............ "  + tscr);
    	tclient = new TransferServiceContextClient(tscr.getEndpointReference());

	    istream = TransferClientHelper.getData(tclient.getDataTransferDescriptor());
		long end = System.currentTimeMillis();
		System.out.println("Total time get images is " + (end - start) + " milli seconds");

		System.out.println("getting zip input stream.............." + istream);
		Assert.assertNotNull("Input stream recieved from transfer service is null", istream);

		if(istream == null){
			System.out.println("istream is null");
			return;
		}
		start = System.currentTimeMillis();
        ZipInputStream zis = new ZipInputStream(istream);
        ZipEntryInputStream zeis = null;
        BufferedInputStream bis = null;
        int ii = 1;
        while(true) {
        	try {
                zeis = new ZipEntryInputStream(zis);
                //System.out.println("zipentryInputstream " + zeis);
        	}
        	catch (EOFException e) {
        		break;
        	}

            String unzzipedFile = downloadLocation();
            System.out.println(ii++ + " filename: " + zeis.getName());

            bis = new BufferedInputStream(zeis);
            byte[] data = new byte[8192];
            int bytesRead = 0;

            File outputFile = new File(unzzipedFile, zeis.getName());
            outputFile.getParentFile().mkdirs();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));

            while ((bytesRead = (bis.read(data, 0, data.length))) > 0)  {
                bos.write(data, 0, bytesRead);
            }
            bos.flush();
		    bos.close();
        }

        zis.close();
		tclient.destroy();

		end = System.currentTimeMillis();
		System.out.println("Total time get download images is " + (end - start) + " milli seconds");		
	}	
	
	private static void dumpCQLResult(CQLQueryResults result) {
		long start = System.currentTimeMillis();
		if(result != null)	{
			CQLQueryResultsIterator iter2 = new CQLQueryResultsIterator(result);

			System.out.println("printing out CQL results");
			// Print Results
			int ii = 1;
			while (iter2.hasNext()) {
				Object obj = iter2.next();
				if (obj == null) {
					System.out.println("something not right.  obj is null" );
					continue;
				}
				else{
					dumpObject(obj, ii);
				}
			}
		}
		else{
			System.out.println("No results found (null)");
		}
		long end = System.currentTimeMillis();
		System.out.println("Total time to print out result for testcase 1: " + (end - start) + " milli seconds");
	}
	
	private static void dumpObject(Object obj, int ii) {
	    if(obj instanceof Annotation) {
	    	Annotation annotation = (Annotation)obj;
	    	System.out.println("Result " + ii++ + ". series instance id is " + annotation.getSeriesInstanceUID()+ " file path: " + annotation.getFilePath());
		}	    
		else
		if(obj instanceof Study) {
			Study study = (Study)obj;
			System.out.println("Result " + ii++ + ". study instance uid is " + study.getStudyInstanceUID() + " study description: " + study.getStudyDescription());
		}
		else
		if(obj instanceof Series) {
		    Series series = (Series)obj;
		    System.out.println("Result " + ii++ + ". series instance uid is " + series.getInstanceUID() + " modality: " + series.getModality());
		}		    
		else
		if(obj instanceof Image) {
		    Image image = (Image)obj;
		    System.out.println("Result " + ii++ + ". Image instance uid is " + image.getSopInstanceUID() + "\tImage sop class uid " + image.getSopClassUID());
		}
		else
		if(obj instanceof Patient) {
		    Patient patient = (Patient)obj;
		    System.out.println("Result " + ii++ + ". patient id is " + patient.getPatientId() + " patient name: " + patient.getPatientName());
		}
	}	    	
	
}
