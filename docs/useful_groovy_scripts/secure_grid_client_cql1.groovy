import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import org.xml.sax.InputSource;
import gov.nih.nci.cagrid.ncia.util.SecureClientUtil;
import org.globus.gsi.GlobusCredential;
import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import org.globus.wsrf.encoding.ObjectDeserializer;

/////////////////////////////////////////////////////////////////
//NOTE: this script doesn't work because of XML parser collisions
/////////////////////////////////////////////////////////////////

println System.getProperty("java.endorsed.dirs");

def gridServiceUrl = "https://imaging-secure-dev.nci.nih.gov:8443/wsrf/services/cagrid/NCIACoreService";

def gridUsername = "xxxx";
def gridPassword = "xxxxx.";
def authUrl = "https://cagrid-auth-stage.nci.nih.gov:8443/wsrf/services/cagrid/AuthenticationService";
def dorianURL = "https://cagrid-dorian-stage.nci.nih.gov:8443/wsrf/services/cagrid/Dorian";
	
	
/*String cql = 
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery\">"+
"  <ns1:Target name=\"gov.nih.nci.ncia.domain.Image\""+
"              xsi:type=\"ns1:Object\""+
"              xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"+
"    <ns1:Attribute name=\"sopInstanceUID\""+
"                   predicate=\"EQUAL_TO\""+
"                   value=\"1.1.33844956.480\""+
"                   xsi:type=\"ns1:Attribute\"/>"+
"</ns1:Target>"+
"</ns1:CQLQuery>";
*/
	
String cql = 
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


//////////////////////////////////////////
	
try {	
		def globusCred = SecureClientUtil.generateGlobusCredential(gridUsername, 
																															 gridPassword,
																															 dorianURL,
																															 authUrl);        
		println("identity:"+globusCred.getIdentity());		

		def nciaCoreServiceClient = new NCIACoreServiceClient(gridServiceUrl, globusCred*);
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
