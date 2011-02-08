import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;


import org.xml.sax.InputSource;
import org.globus.wsrf.encoding.ObjectDeserializer;


def gridServiceUrl = "http://xxxxx:xxxxx/wsrf/services/cagrid/NCIACoreService"


/*String cql = 
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery\">"+
"  <ns1:Target name=\"gov.nih.nci.ncia.domain.TrialDataProvenance\""+
"              xsi:type=\"ns1:Object\""+
"              xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"+
"    <ns1:Attribute name=\"project\""+
"                   predicate=\"EQUAL_TO\""+
"                   value=\"TCGA\""+
"                   xsi:type=\"ns1:Attribute\"/>"+
"</ns1:Target>"+
"</ns1:CQLQuery>";

*/

def cql1Writer = new StringWriter()
def cql1Builder = new MarkupBuilder(cql1Writer)
cql1Builder.'ns1:CQLQuery'('xmlns:ns1': 'http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery') {
  'ns1:Target'(name:'gov.nih.nci.ncia.domain.TrialDataProvenance',
               'xmlns:xsi': 'http://www.w3.org/2001/XMLSchema-instance',
               'xsi:type': 'ns1:Object') {
    'ns1:Attribute'(name:'project', 
                    predicate:'EQUAL_TO', 
                    value:'TCGA', 
                    'xsi:type':'ns1:Attribute') 
  }
}



//////////////////////////////////////////
	
try {	
    def nciaCoreServiceClient = new NCIACoreServiceClient(gridServiceUrl)


		def queryInput = new InputSource(new StringReader(cqlWriter.toString()));
		def cqlQuery = (CQLQuery) ObjectDeserializer.deserialize(queryInput, CQLQuery.class);
		
		def results = nciaCoreServiceClient.query(cqlQuery);

		if(results.getObjectResult()==null) {
				println("Num results: null");
		}
		else {
				println("Num results:"+results.getObjectResult().length);
	  }
}
catch(e) {
    e.printStackTrace();
}
