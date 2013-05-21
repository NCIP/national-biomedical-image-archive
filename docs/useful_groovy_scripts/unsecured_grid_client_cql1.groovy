import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator
import gov.nih.nci.ncia.domain.Patient;

import groovy.xml.MarkupBuilder
import org.xml.sax.InputSource;
import org.globus.wsrf.encoding.ObjectDeserializer;


def gridServiceUrl = "http://imaging.nci.nih.gov/wsrf/services/cagrid/NCIACoreService"
//def gridServiceUrl = "http://localhost:8081/wsrf/services/cagrid/NCIACoreService"


def cql1Writer = new StringWriter()
def cql1Builder = new MarkupBuilder(cql1Writer)
cql1Builder.'ns1:CQLQuery'('xmlns:ns1': 'http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery') {
  'ns1:Target'(name:'gov.nih.nci.ncia.domain.Patient',
               'xmlns:xsi': 'http://www.w3.org/2001/XMLSchema-instance',
               'xsi:type': 'ns1:Object') {
    'ns1:Attribute'(name:'id', 
                    predicate:'EQUAL_TO', 
                    value:'1', 
                    'xsi:type':'ns1:Attribute') 
  }
}



//////////////////////////////////////////
	
try {	
    def nciaCoreServiceClient = new NCIACoreServiceClient(gridServiceUrl)


		def queryInput = new InputSource(new StringReader(cql1Writer.toString()));
		def cqlQuery = (CQLQuery) ObjectDeserializer.deserialize(queryInput, CQLQuery.class);
		
		def results = nciaCoreServiceClient.query(cqlQuery);

    println "num results:"+results.getObjectResult()?.length
    def iter = new CQLQueryResultsIterator(results)
    while(iter.hasNext()) {
        def patient = iter.next()
        println "id:"+ patient.getId()
	  }
}
catch(e) {
    e.printStackTrace();
}
