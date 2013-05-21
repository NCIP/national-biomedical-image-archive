import org.cagrid.cql2.CQLQuery;
import org.cagrid.cql.utilities.CQL2SerializationUtil;
import org.cagrid.cql2.results.CQLQueryResults;
import gov.nih.nci.nbia.client.NBIAServiceClient;
import groovy.xml.MarkupBuilder

def gridServiceUrl = "http://nciavgriddev5004.nci.nih.gov:39380/wsrf/services/cagrid/NBIAService"



def cql2Writer = new StringWriter()
def cql2Builder = new MarkupBuilder(cql2Writer)
cql2Builder.
'ns1:CQLQuery'('xmlns:ns1': 'http://CQL.caBIG/2/org.cagrid.cql2') {
  'ns1:CQLTargetObject'(className:'gov.nih.nci.ncia.domain.Patient') {
    'ns1:CQLAttribute'(name:'id') {
      'ns1:BinaryPredicate'('EQUAL_TO')
      'ns1:AttributeValue' {
        'ns1:IntegerValue'('1')
      }
    }  
  }
  
  'ns1:AssociationPopulationSpecification' {
    'ns1:NamedAssociationList' {
      'ns1:NamedAssociation'(endName:'studyCollection')
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
