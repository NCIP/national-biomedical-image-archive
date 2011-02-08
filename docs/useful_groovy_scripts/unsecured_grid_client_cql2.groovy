import org.cagrid.cql2.CQLQuery;
import org.cagrid.cql.utilities.CQL2SerializationUtil;
import org.cagrid.cql2.results.CQLQueryResults;
import gov.nih.nci.nbia.client.NBIAServiceClient;


def gridServiceUrl = "http://xxxx:xxxxx/wsrf/services/cagrid/NCIACoreService"



def cql2Writer = new StringWriter()
def cql2Builder = new MarkupBuilder(cql2Writer)
cql2Builder.'ns1:CQLQuery'('xmlns:ns1': 'http://CQL.caBIG/2/org.cagrid.cql2') {
  'ns1:CQLTargetObject'(className:'gov.nih.nci.ncia.domain.TrialDataProvenance') {
    'ns1:CQLAttribute'(name:'project') {
      'ns1:BinaryPredicate'('EQUAL_TO')
      'ns1:AttributeValue' {
        'ns1:StringValue'('TCGA')
      }
    }  
  }
}



//////////////////////////////////////////
	
try {	

    def nbiaServiceClient = new NBIAServiceClient(gridServiceUrl);

    def cql2Query = CQL2SerializationUtil.deserializeCql2Query(cql2Writer.toString());
		
		def results = nbiaServiceClient.executeQuery(cql2Query);

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
