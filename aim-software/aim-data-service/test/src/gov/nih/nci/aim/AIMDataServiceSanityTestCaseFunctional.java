/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.aim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.xml.namespace.QName;
import junit.framework.TestCase;
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.xml.sax.InputSource;
import edu.northwestern.radiology.aim.ImageAnnotation;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;

public class AIMDataServiceSanityTestCaseFunctional extends TestCase {
    public static final String AIM_URL = "http://localhost:30210/wsrf/services/cagrid/AIMDataService";


    
	public void testSubmitAnnotations() throws Exception {
		String localAimFile = "test/resources/0022BaselineA_Model.xml";
		
		AIMDataServiceHelper helper = new AIMDataServiceHelper();
		helper.submitAnnotations(localAimFile, AIM_URL);
	}
	
	public void testRetrieveAnnotations() throws Exception {
		final CQLQuery fcqlq = makeQuery("test/resources/queryAllAIMForSingleSeries.xml");
		
		AIMDataServiceHelper helper = new AIMDataServiceHelper();
		helper.retrieveAnnotations(fcqlq, 
				                   AIM_URL);

	}

	public void testRetrieveAnnotationsByTransfer() throws Exception {
		final CQLQuery fcqlq = makeQuery("test/resources/queryAllAIMForSingleSeries.xml");

		File localDownloadLocation = new File("/tmp/TestAimDownloadDir");
		if (!localDownloadLocation.exists()) {
			localDownloadLocation.mkdirs();
		}
		
		AIMDataServiceHelper helper = new AIMDataServiceHelper();
		helper.retrieveAnnotations(fcqlq, 
				                   AIM_URL, 
				                   "/tmp/TestAimDownloadDir");

	}	


//	public void testDeserialization() throws Exception {
//	String localAimFile = "test/resources/0022BaselineA_Model.xml";
//	FileReader reader = new FileReader(localAimFile);
//	
////	Object o = ObjectDeserializer.deserialize(new InputSource(reader), 
////			                                  ImageAnnotation.class);
//	Object o = gov.nih.nci.cagrid.common.Utils.deserializeObject(reader, ImageAnnotation.class);
//
//	assertNotNull(o);
//}

//public void testAIMQuery() throws Exception {
//
//	CQLQuery cqlq = AIMDataServiceHelper.generateImageAnnotationQuery("1.3.6.1.4.1.9328.50.1.10607", 
//         				                                              "1.3.6.1.4.1.9328.50.1.10697", 
//			                                                          "NWU");
//
//	System.out.println(ObjectSerializer.toString(cqlq, 
//			                                     new QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery",
//				                                           "CQLQuery")));
//
//
//	DataServiceClient aimDataService = new DataServiceClient(AIM_URL);
//	CQLQueryResults result = aimDataService.query(cqlq);
//
//
//	CQLQueryResultsIterator iter2 = new CQLQueryResultsIterator(result, true);
//	int ii = 0;
//	while (iter2.hasNext()) {
//		String xml = (String)iter2.next();
//
//		System.out.println("xml: " + xml);
//		System.out.println("Result " + ++ii + ". ");
//	}
//}
//
	///////////////////////////////////////PRIVATE////////////////////////////////////////////////////

	
	private CQLQuery makeQuery(String filename) {
		CQLQuery newQuery = null;
		try {
			InputSource queryInput = new InputSource(new FileReader(filename));
			newQuery = (CQLQuery) ObjectDeserializer.deserialize(queryInput,
					CQLQuery.class);
		} catch (FileNotFoundException e) {
			fail("test Query XML file not found");
		} catch (DeserializationException e) {
			fail("test Query XML file could not be deserialized");
		}
		assertNotNull(newQuery);
		return newQuery;
	}

}
