//import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import org.cagrid.cql2.CQLQuery;
import org.cagrid.cql.utilities.CQL2SerializationUtil;
//import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import org.cagrid.cql2.results.CQLQueryResults;
import org.xml.sax.InputSource;
import gov.nih.nci.nbia.client.NBIAServiceClient;
import org.globus.wsrf.encoding.ObjectDeserializer;
import gov.nih.nci.ncia.domain.Patient;
import org.cagrid.cql.utilities.iterator.*;



//def gridServiceUrl = "http://imaging-dev.nci.nih.gov:80/wsrf/services/cagrid/NCIACoreService";
def gridServiceUrl = "http://nciavgriddev5004.nci.nih.gov:39380/wsrf/services/cagrid/NBIAService";
//def gridServiceUrl = "http://nci-kascice-1:8080/wsrf/services/cagrid/NBIAService";


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

String cql2 =
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
" <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.TrialDataProvenance\"/>"+
"</ns1:CQLQuery>";


String cql3 =
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"  <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.TrialDataProvenance\">"+
"   <ns2:CQLAttribute name=\"project\" xmlns:ns2=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"     <ns2:BinaryPredicate>EQUAL_TO</ns2:BinaryPredicate>"+
"     <ns2:AttributeValue>"+
"       <ns2:StringValue>TCGA</ns2:StringValue>"+
"     </ns2:AttributeValue>"+
"   </ns2:CQLAttribute>"+
"  </ns1:CQLTargetObject>"+
"</ns1:CQLQuery>";

//search for public data to confirm grid service is searching correct data (comparing with SQL)
String cql_public_data_search_patient = 
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"   <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.Patient\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.TrialDataProvenance\" endName=\"dataProvenance\">"+
"   <ns2:CQLAttribute name=\"project\" xmlns:ns2=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"     <ns2:BinaryPredicate>EQUAL_TO</ns2:BinaryPredicate>"+
"     <ns2:AttributeValue>"+
"       <ns2:StringValue>LIDC</ns2:StringValue>"+
"     </ns2:AttributeValue>"+
"   </ns2:CQLAttribute>"+    
"   </ns1:CQLAssociatedObject> " +
"  </ns1:CQLTargetObject>"+
"</ns1:CQLQuery>";   


String cql_public_data_search_study = 
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"   <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.Study\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Patient\" endName=\"patient\">"+
"   <ns2:CQLAttribute name=\"patientId\" xmlns:ns2=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"     <ns2:BinaryPredicate>EQUAL_TO</ns2:BinaryPredicate>"+
"     <ns2:AttributeValue>"+
"       <ns2:StringValue>1.3.6.1.4.1.9328.50.3.0248</ns2:StringValue>"+
"     </ns2:AttributeValue>"+
"   </ns2:CQLAttribute>"+    
"   </ns1:CQLAssociatedObject> " +
"  </ns1:CQLTargetObject>"+
"</ns1:CQLQuery>";  

String cql_public_data_search_series = 
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"   <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.Series\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Study\" endName=\"study\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Patient\" endName=\"patient\">"+
"   <ns2:CQLAttribute name=\"patientId\" xmlns:ns2=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"     <ns2:BinaryPredicate>EQUAL_TO</ns2:BinaryPredicate>"+
"     <ns2:AttributeValue>"+
"       <ns2:StringValue>NoduleLayout_2</ns2:StringValue>"+
"     </ns2:AttributeValue>"+
"   </ns2:CQLAttribute>"+    
"   </ns1:CQLAssociatedObject> " +
"   </ns1:CQLAssociatedObject> " +
"  </ns1:CQLTargetObject>"+
"</ns1:CQLQuery>";  


String cql_public_data_search_image = 
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"   <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.Image\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Series\" endName=\"series\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Study\" endName=\"study\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Patient\" endName=\"patient\">"+
"   <ns2:CQLAttribute name=\"patientId\" xmlns:ns2=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"     <ns2:BinaryPredicate>EQUAL_TO</ns2:BinaryPredicate>"+
"     <ns2:AttributeValue>"+
"       <ns2:StringValue>LIDC-2655259078</ns2:StringValue>"+
"     </ns2:AttributeValue>"+
"   </ns2:CQLAttribute>"+    
"   </ns1:CQLAssociatedObject> " +
"   </ns1:CQLAssociatedObject> " +
"   </ns1:CQLAssociatedObject> " +
"  </ns1:CQLTargetObject>"+
"</ns1:CQLQuery>";  

//////////////////////////////////////////

//search for public data to confirm grid service is searching correct data (comparing with SQL)
String cql_private_data_search_patient = 
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"   <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.Patient\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.TrialDataProvenance\" endName=\"dataProvenance\">"+
"   <ns2:CQLAttribute name=\"project\" xmlns:ns2=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"     <ns2:BinaryPredicate>EQUAL_TO</ns2:BinaryPredicate>"+
"     <ns2:AttributeValue>"+
"       <ns2:StringValue>Test</ns2:StringValue>"+
"     </ns2:AttributeValue>"+
"   </ns2:CQLAttribute>"+    
"   </ns1:CQLAssociatedObject> " +
"  </ns1:CQLTargetObject>"+
"</ns1:CQLQuery>"; 


String cql_private_data_search_study = 
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"   <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.Study\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Patient\" endName=\"patient\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.TrialDataProvenance\" endName=\"dataProvenance\">"+
"   <ns2:CQLAttribute name=\"project\" xmlns:ns2=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"     <ns2:BinaryPredicate>EQUAL_TO</ns2:BinaryPredicate>"+
"     <ns2:AttributeValue>"+
"       <ns2:StringValue>IDRI</ns2:StringValue>"+
"     </ns2:AttributeValue>"+
"   </ns2:CQLAttribute>"+    
"   </ns1:CQLAssociatedObject> " +
"  </ns1:CQLAssociatedObject>"+
"  </ns1:CQLTargetObject>"+
"</ns1:CQLQuery>"; 


String cql_private_data_search_series = 
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2>"+
"   <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.Series\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Study\" endName=\"study\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Patient\" endName=\"patient\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.TrialDataProvenance\" endName=\"dataProvenance\">"+
"   <ns2:CQLAttribute name=\"project\" xmlns:ns2=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"     <ns2:BinaryPredicate>EQUAL_TO</ns2:BinaryPredicate>"+
"     <ns2:AttributeValue>"+
"       <ns2:StringValue>LIDC-IDRI</ns2:StringValue>"+
"     </ns2:AttributeValue>"+
"   </ns2:CQLAttribute>"+    
"  </ns1:CQLAssociatedObject>"+
"   </ns1:CQLAssociatedObject> " +
"  </ns1:CQLAssociatedObject>"+
"  </ns1:CQLTargetObject>"+
"</ns1:CQLQuery>"; 

String cql_private_data_search_image = 
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"   <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.Image\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Series\" endName=\"series\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Study\" endName=\"study\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Patient\" endName=\"patient\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.TrialDataProvenance\" endName=\"dataProvenance\">"+
"   <ns2:CQLAttribute name=\"project\" xmlns:ns2=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"     <ns2:BinaryPredicate>EQUAL_TO</ns2:BinaryPredicate>"+
"     <ns2:AttributeValue>"+
"       <ns2:StringValue>LIDC-IDRI</ns2:StringValue>"+
"     </ns2:AttributeValue>"+
"   </ns2:CQLAttribute>"+    
"  </ns1:CQLAssociatedObject>"+
"   </ns1:CQLAssociatedObject> " +
"  </ns1:CQLAssociatedObject>"+
"  </ns1:CQLAssociatedObject>"+
"  </ns1:CQLTargetObject>"+
"</ns1:CQLQuery>"; 

String cql_attribute_search_image =
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"   <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.Patient\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Study\" endName=\"studyCollection\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Series\" endName=\"seriesCollection\">"+
"   <ns1:CQLAttribute name=\"modality\">"+
"     <ns1:BinaryPredicate>EQUAL_TO</ns1:BinaryPredicate>"+
"     <ns1:AttributeValue>"+
"       <ns1:StringValue>US</ns1:StringValue>"+
"     </ns1:AttributeValue>"+
"   </ns1:CQLAttribute>"+    
"  </ns1:CQLAssociatedObject>"+
"  </ns1:CQLAssociatedObject>"+
"  </ns1:CQLTargetObject>"+
"</ns1:CQLQuery>";


////////////////////// aggregations ////////////////////////
String cql_data_image_aggregation_count = 
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"   <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.Image\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Series\" endName=\"series\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Study\" endName=\"study\">"+
"   <ns1:CQLAssociatedObject className=\"gov.nih.nci.ncia.domain.Patient\" endName=\"patient\">"+
"   <ns1:CQLAttribute name=\"id\">"+
"     <ns1:BinaryPredicate>LESS_THAN</ns1:BinaryPredicate>"+
"     <ns1:AttributeValue>"+
"       <ns1:IntegerValue>20</ns1:IntegerValue>"+
"     </ns1:AttributeValue>"+
"   </ns1:CQLAttribute>"+    
"  </ns1:CQLAssociatedObject>"+
"  </ns1:CQLAssociatedObject>"+
"  </ns1:CQLAssociatedObject>"+
"  </ns1:CQLTargetObject>"+
"   <ns1:CQLQueryModifier> "+
"    <ns1:CountOnly>true</ns1:CountOnly>"+ 
"   </ns1:CQLQueryModifier>" +
"</ns1:CQLQuery>";


String cql_data_image_aggregation_min = 
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"   <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.Image\">"+
"   <ns1:CQLAttribute name=\"id\">"+
"     <ns1:BinaryPredicate>LESS_THAN</ns1:BinaryPredicate>"+
"     <ns1:AttributeValue>"+
"       <ns1:IntegerValue>20</ns1:IntegerValue>"+
"     </ns1:AttributeValue>"+
"   </ns1:CQLAttribute>"+    
"  </ns1:CQLTargetObject>"+
"   <ns1:CQLQueryModifier> "+
"	<ns1:DistinctAttribute attributeName=\"contentTime\" aggregation=\"MIN\" /> " +		
"   </ns1:CQLQueryModifier>" +
"</ns1:CQLQuery>";

String cql_data_image_aggregation_max = 
"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"   <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.Image\">"+
"   <ns1:CQLAttribute name=\"id\">"+
"     <ns1:BinaryPredicate>LESS_THAN</ns1:BinaryPredicate>"+
"     <ns1:AttributeValue>"+
"       <ns1:IntegerValue>20</ns1:IntegerValue>"+
"     </ns1:AttributeValue>"+
"   </ns1:CQLAttribute>"+    
"  </ns1:CQLTargetObject>"+
"   <ns1:CQLQueryModifier> "+
"	<ns1:DistinctAttribute attributeName=\"contentTime\" aggregation=\"MAX\" /> " +		
"   </ns1:CQLQueryModifier>" +
"</ns1:CQLQuery>";

//////////////// Fetch studies from a patient //////////////////////////
String fetch_studies_from_patient_query = 

"<ns1:CQLQuery xmlns:ns1=\"http://CQL.caBIG/2/org.cagrid.cql2\">"+
"   <ns1:CQLTargetObject className=\"gov.nih.nci.ncia.domain.Patient\">"+
"   <ns1:CQLAttribute name=\"id\">" +
"   <ns1:BinaryPredicate>LESS_THAN</ns1:BinaryPredicate> " +
"	<ns1:AttributeValue> " + 
"           <ns1:IntegerValue>20</ns1:IntegerValue> " +
"       </ns1:AttributeValue> " +
"   </ns1:CQLAttribute> "+
"   </ns1:CQLTargetObject> "+
"   </ns1:CQLQuery> ";	

String fetch_studies_on_Patient_query = """
<ns1:CQLQuery xmlns:ns1="http://CQL.caBIG/2/org.cagrid.cql2">
<ns1:CQLTargetObject className="gov.nih.nci.ncia.domain.Patient">
</ns1:CQLTargetObject>
<ns1:AssociationPopulationSpecification>
<ns1:NamedAssociationList>
<ns1:NamedAssociation endName="studyCollection"/>
</ns1:NamedAssociationList>
</ns1:AssociationPopulationSpecification>
</ns1:CQLQuery>
""";


//////////////////////////// Association Search ///////////////////////////////////
String fetch_association_from_patient_query = """
<ns1:CQLQuery xmlns:ns1="http://CQL.caBIG/2/org.cagrid.cql2">
   <ns1:CQLTargetObject className="gov.nih.nci.ncia.domain.Patient">
   <ns1:CQLAssociatedObject className="gov.nih.nci.ncia.domain.Study" endName="studyCollection">
   <ns1:CQLAssociatedObject className="gov.nih.nci.ncia.domain.Series" endName="seriesCollection">
   <ns1:CQLAttribute name="modality">
   <ns1:BinaryPredicate>EQUAL_TO</ns1:BinaryPredicate>
     <ns1:AttributeValue>
       <ns1:StringValue>US</ns1:StringValue>
     </ns1:AttributeValue>
   </ns1:CQLAttribute>    
  </ns1:CQLAssociatedObject>
  </ns1:CQLAssociatedObject>
  </ns1:CQLTargetObject>
  <ns1:AssociationPopulationSpecification> 
     <ns1:NamedAssociationList>
	<ns1:NamedAssociation endName="studyCollection" />
    </ns1:NamedAssociationList> 
  </ns1:AssociationPopulationSpecification> 
</ns1:CQLQuery>
"""

/*
String fetch_association_from_study_query = """
<ns1:CQLQuery xmlns:ns1="http://CQL.caBIG/2/org.cagrid.cql2">
   <ns1:CQLTargetObject className="gov.nih.nci.ncia.domain.Study">
   <ns1:CQLAssociatedObject className="gov.nih.nci.ncia.domain.Study" endName="studyCollection">
   <ns1:CQLAssociatedObject className="gov.nih.nci.ncia.domain.Series" endName="seriesCollection">
   <ns1:CQLAttribute name="modality">
   <ns1:BinaryPredicate>EQUAL_TO</ns1:BinaryPredicate>
     <ns1:AttributeValue>
       <ns1:StringValue>US</ns1:StringValue>
     </ns1:AttributeValue>
   </ns1:CQLAttribute>    
  </ns1:CQLAssociatedObject>
  </ns1:CQLAssociatedObject>
  </ns1:CQLTargetObject>
  <ns1:AssociationPopulationSpecification> 
     <ns1:NamedAssociationList>
	<ns1:NamedAssociation endName="studyCollection" />
    </ns1:NamedAssociationList> 
  </ns1:AssociationPopulationSpecification> 
</ns1:CQLQuery>
*/


//"	<ns1:NamedAssociationList>" +
//"		<ns1:NamedAssociation endName=\"studyCollection\" /> "+
//"       </ns1:NamedAssociationList> "+

//<ns1:PopulationDepth depth="2" />

try {	

    def nbiaServiceClient = new NBIAServiceClient(gridServiceUrl);


		//def queryInput = new InputSource(new StringReader(cql3));

		//def cqlQuery = (CQLQuery) ObjectDeserializer.deserialize(queryInput, CQLQuery.class);
		def cql2Query = CQL2SerializationUtil.deserializeCql2Query(fetch_studies_on_Patient_query);
		
		//def results = nciaCoreServiceClient.query(cqlQuery);
		def results = nbiaServiceClient.executeQuery(cql2Query);
		def iter = new CQL2QueryResultsIterator(results, true);
/*
		while(iter.hasNext()) {
		//(Patient) p = (Patient)iter.next();
			println iter.next();
		}*/
/*		if (results.getObjectResult() != null)
		{
		 	for (def p in results.getObjectResult()){
		 		println("Patient ID: " + p.getValue().getPatientId());
		 	}
		}
		else {
			println("results : null");		
		}
		
		if(results.getObjectResult()==null) {
				println("Object results: null");
		}
		else {
				//println("Number of Count is : " + results.getAggregationResult().getValue());
				println("Object results:"+results.getObjectResult().length);
				for (int i = 0; i < results.getObjectResult(); i++){
					println "Patient ID : " + (Patient)(results.getObjectResult().getAt(i));
				}
	  	}    
	  	
	 	if (results.getAggregationResult() != null)
	 	{
	 		println("Aggregation Result is : " + results.getAggregationResult().getValue());	
	 	}
	 	else{
	 		println "Aggregation result is NULL";
	 	}
	 	
	 	if(results.getAttributeResult() != null)
	 	{
	 		println("Attribute results:"+results.getAttributeResult().length);
	  		
	 	}else{
	 		println "Attribute Result is NULL";
	 	}
*/}
catch(e) {
    e.printStackTrace();
}
