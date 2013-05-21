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

def query = """
<ns1:CQLQuery xmlns:ns1="http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery">
  <ns1:Target name="gov.nih.nci.ncia.domain.Image">
		<ns1:Association name="gov.nih.nci.ncia.domain.Series" roleName="series">
			<ns1:Attribute name="instanceUID" predicate="EQUAL_TO" value="2.16.124.113543.6003.3342685917.53028.19438.979964858"/>
		</ns1:Association>   
  </ns1:Target>
  <ns1:QueryModifier countOnly="false">
    <ns1:AttributeNames>sopInstanceUID</ns1:AttributeNames>
  </ns1:QueryModifier>
</ns1:CQLQuery>      
"""

/*
      <ns1:Association name="gov.nih.nci.ncia.domain.Series" roleName="series">
        <ns1:Association name="gov.nih.nci.ncia.domain.Study" roleName="study">
          <ns1:Association name="gov.nih.nci.ncia.domain.Patient" roleName="patient">
            <ns1:Association name="gov.nih.nci.ncia.domain.TrialDataProvenance" roleName="dataProvenance">
              <ns1:Group logicRelation="OR">
                <ns1:Group logicRelation="AND">
                  <ns1:Attribute name="project" predicate="EQUAL_TO" value="RIDER Pilot"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="MSKCC"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="LIDC"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="LIDC"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="Phantom-FDA"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="FDA"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="RIDER Neuro MRI"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="Duke"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="CT Colonography"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="ACRIN"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="RIDER Phantom MRI"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="MDACC"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="RIDER Lung PET CT"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="WashU-IRL"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="RIDER Breast MRI"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="UMich"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="Phantom"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="WashU-IRL"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="CT Colonography"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="ACRIN CTP"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="RoswellStrong"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="Strong"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="Virtual Colonoscopy"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="WRAMC"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="TCGA"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="UCSF"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="RIDER Pilot"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="MDACC"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="RIDER Lung CT"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="MSKCC CTP"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="TCGA"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="Henry Ford"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="NCRI"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="NHS"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="VASARI"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="Henry Ford"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="RoswellStrong"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="ROSWELL"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="VASARI"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="TJU"/></ns1:Group><ns1:Group logicRelation="AND"><ns1:Attribute name="project" predicate="EQUAL_TO" value="Virtual Colonoscopy"/><ns1:Attribute name="siteName" predicate="EQUAL_TO" value="SanDiego"/></ns1:Group></ns1:Group></ns1:Association></ns1:Association></ns1:Association></ns1:Association>
                 */



//////////////////////////////////////////
	
try {	
    def nciaCoreServiceClient = new NCIACoreServiceClient(gridServiceUrl)


		def queryInput = new InputSource(new StringReader(query));
		def cqlQuery = (CQLQuery) ObjectDeserializer.deserialize(queryInput, CQLQuery.class);

    def before = System.currentTimeMillis();		
		def results = nciaCoreServiceClient.query(cqlQuery);
    def end = System.currentTimeMillis();
    
    println results
    println "time:"+(end-before)+" ms"
}
catch(e) {
    e.printStackTrace();
}
