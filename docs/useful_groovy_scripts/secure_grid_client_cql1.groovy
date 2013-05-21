import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import org.xml.sax.InputSource;
import gov.nih.nci.cagrid.ncia.util.SecureClientUtil;
import org.globus.gsi.GlobusCredential;
import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import org.globus.wsrf.encoding.ObjectDeserializer;

///////////////////////////////////////////////////////////////////////////////
//NOTE: this script won't work unless you pull out javax/xml/transform 
//      from jaxrpc.jar from Globus.  StreamSource classes with JAXP instance
///////////////////////////////////////////////////////////////////////////////



def gridServiceUrl = "https://imaging-secure.nci.nih.gov:8443/wsrf/services/cagrid/NCIACoreService";

def gridUsername = "xxxxx";
def gridPassword = "xxxxx";
def authUrl = "https://cagrid-auth.nci.nih.gov:8443/wsrf/services/cagrid/AuthenticationService";
def dorianURL = "https://cagrid-dorian.nci.nih.gov:8443/wsrf/services/cagrid/Dorian";
	
	
String cql = 
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery\">"+
"  <ns1:Target name=\"gov.nih.nci.ncia.domain.Patient\""+
"              xsi:type=\"ns1:Object\""+
"              xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"+
"    <ns1:Attribute name=\"id\""+
"                   predicate=\"EQUAL_TO\""+
"                   value=\"1\""+
"                   xsi:type=\"ns1:Attribute\"/>"+
"</ns1:Target>"+
"</ns1:CQLQuery>";


//////////////////////////////////////////
	
try {	
		def globusCred = SecureClientUtil.generateGlobusCredential(gridUsername, 
																															 gridPassword,
																															 dorianURL,
																															 authUrl);        
		println("identity:"+globusCred.getIdentity());		

		def nciaCoreServiceClient = new NCIACoreServiceClient(gridServiceUrl, globusCred);
		nciaCoreServiceClient.setAnonymousPrefered(false);


		def queryInput = new InputSource(new StringReader(cql));

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
