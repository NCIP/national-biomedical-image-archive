/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.cagrid.ncia.client;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;

import java.io.File;
import java.io.FileReader;

import javax.xml.namespace.QName;

import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.xml.sax.InputSource;

public abstract class DataServiceTestCaseFunctional {
	/////////////////////////////////////////PRIVATE///////////////////////////////////////////////////////
	
	protected CQLQueryResults sendCQLQuery(NCIACoreServiceClient client, File cqlFile) throws Exception {
		InputSource queryInput = new InputSource(new FileReader(cqlFile));
		
		CQLQuery cqlQuery = (CQLQuery) ObjectDeserializer.deserialize(queryInput, CQLQuery.class);
		dumpCQL(cqlQuery);
		
		CQLQueryResults results = client.query(cqlQuery);
		dumpResults(results);
		return results;
	}
	
	protected static void dumpResults(CQLQueryResults results) {
		if(results.getObjectResult()==null) {
			System.out.println("Num results: null");

		}
		else {
			System.out.println("Num results:"+results.getObjectResult().length);
		}		
	}
	
	
	
	protected static class CqlFileFilter implements java.io.FileFilter {
		public boolean accept(File file) {
			return file.getAbsolutePath().endsWith(".xml");
		}
	}
	protected static File[] getCQLFiles(String relativePath) {
		File directory = new File(relativePath);
		return directory.listFiles(new CqlFileFilter());
	}
	
	protected static void dumpCQL(CQLQuery query) throws Exception {
		System.err.println(ObjectSerializer.toString(query,
            				                         new QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "CQLQuery")));		
	}		

}
