/**
 * 
 */
package gov.nih.nci.ncia;

import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;

import java.io.InputStream;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.encoding.SerializationException;
import org.xml.sax.InputSource;

/**
 * @author lethai
 *
 */
public class CustomizedCQLQueryTestCase extends TestCase {

	public void testModifyCQLQueryImageTarget() {
		String filename = "testImage.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);
		
		CQLQuery modifiedCqlQuery = CustomizedCQLQuery.modifyCQLQueryToTargetImage(fcqlq);
				
		assertTrue(modifiedCqlQuery==fcqlq);	
	}

	public void testModifyCQLQuerySeriesTargetJustAssociation() {
		String filename = "testCase3.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);
		
		CQLQuery modifiedCqlQuery = CustomizedCQLQuery.modifyCQLQueryToTargetImage(fcqlq);
		
		assertNotNull("results is not null", modifiedCqlQuery);
		
		assertTrue(modifiedCqlQuery.getTarget().getName().equals("gov.nih.nci.ncia.domain.Image"));
		
		Association seriesAssociation = modifiedCqlQuery.getTarget().getAssociation();
		assertTrue(seriesAssociation.getName().equals("gov.nih.nci.ncia.domain.Series"));
		assertTrue(seriesAssociation.getRoleName().equals("series"));

		assertNull(seriesAssociation.getAttribute());
		assertNull(seriesAssociation.getGroup());
	}
	
	public void testModifyCQLQueryStudyTargetWithJustAttribute() {
		String filename = "testCase-studyTargetJustAttribute.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);
		
		Attribute beforeAttribute = fcqlq.getTarget().getAttribute();
		
		CQLQuery modifiedCqlQuery = CustomizedCQLQuery.modifyCQLQueryToTargetImage(fcqlq);
		
		assertNotNull("results is not null", modifiedCqlQuery);
		
		assertTrue(modifiedCqlQuery.getTarget().getName().equals("gov.nih.nci.ncia.domain.Image"));
		
		Association seriesAssociation = modifiedCqlQuery.getTarget().getAssociation();
		assertTrue(seriesAssociation.getName().equals("gov.nih.nci.ncia.domain.Series"));
		assertTrue(seriesAssociation.getRoleName().equals("series"));
		
		Association studyAssociation = seriesAssociation.getAssociation();
		assertTrue(studyAssociation.getName().equals("gov.nih.nci.ncia.domain.Study"));
		assertTrue(studyAssociation.getRoleName().equals("study"));
		assertTrue(studyAssociation.getAttribute().equals(beforeAttribute));
		
		assertNull(studyAssociation.getAssociation());
		assertNull(studyAssociation.getGroup());		
	}	
	
	public void testModifyCQLQueryStudyTargetWithJustAssociation() {
		String filename = "testCase-studyTargetJustAssociation.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);
		
		Association beforeAssociation = fcqlq.getTarget().getAssociation();
		
		CQLQuery modifiedCqlQuery = CustomizedCQLQuery.modifyCQLQueryToTargetImage(fcqlq);
		
		assertNotNull("results is not null", modifiedCqlQuery);
		
		assertTrue(modifiedCqlQuery.getTarget().getName().equals("gov.nih.nci.ncia.domain.Image"));
		
		Association seriesAssociation = modifiedCqlQuery.getTarget().getAssociation();
		assertTrue(seriesAssociation.getName().equals("gov.nih.nci.ncia.domain.Series"));
		assertTrue(seriesAssociation.getRoleName().equals("series"));
		
		Association studyAssociation = seriesAssociation.getAssociation();
		assertTrue(studyAssociation.getName().equals("gov.nih.nci.ncia.domain.Study"));
		assertTrue(studyAssociation.getRoleName().equals("study"));
		assertTrue(studyAssociation.getAssociation().equals(beforeAssociation));
		
		assertNull(studyAssociation.getAttribute());
		assertNull(studyAssociation.getGroup());		
	}
	
	public void testModifyCQLQueryPatientTargetWithJustAttribute() {
		String filename = "testCase-patientTargetJustAttribute.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);
		
		Attribute beforeAttribute = fcqlq.getTarget().getAttribute();
		
		CQLQuery modifiedCqlQuery = CustomizedCQLQuery.modifyCQLQueryToTargetImage(fcqlq);
		
		assertNotNull("results is not null", modifiedCqlQuery);
		
		assertTrue(modifiedCqlQuery.getTarget().getName().equals("gov.nih.nci.ncia.domain.Image"));
		
		Association seriesAssociation = modifiedCqlQuery.getTarget().getAssociation();
		assertTrue(seriesAssociation.getName().equals("gov.nih.nci.ncia.domain.Series"));
		assertTrue(seriesAssociation.getRoleName().equals("series"));
		
		Association studyAssociation = seriesAssociation.getAssociation();
		assertTrue(studyAssociation.getName().equals("gov.nih.nci.ncia.domain.Study"));
		assertTrue(studyAssociation.getRoleName().equals("study"));	
		
		Association patientAssociation = studyAssociation.getAssociation();
		assertTrue(patientAssociation.getName().equals("gov.nih.nci.ncia.domain.Patient"));
		assertTrue(patientAssociation.getRoleName().equals("patient"));
		assertTrue(patientAssociation.getAttribute().equals(beforeAttribute));
		
		assertNull(patientAssociation.getAssociation());
		assertNull(patientAssociation.getGroup());		
	}	
	
//	public void testParseCQLQuery() {
//		String filename = "testCase3.xml";
//		final CQLQuery fcqlq = this.loadXMLFile(filename);
//			
//		Map<String, String> attrMap = CustomizedCQLQuery.parseCQLQuery(fcqlq);
//		
//		assertNotNull("results is not null", attrMap);
//		System.out.println("size:"+attrMap.size());
//		assertTrue(attrMap.size()==2);
//		assertTrue(attrMap.get("patientId").equals("1.3.6.1.4.1.9328.50.1.0019"));
//		assertTrue(attrMap.get("studyInstanceUID").equals("1.3.6.1.4.1.9328.50.1.8858"));				
//	}		

	
	private CQLQuery loadXMLFile(String filename){
		CQLQuery newQuery = null;
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
			//System.out.println( "is is " + is);
			InputSource query = new InputSource(is);
			newQuery = (CQLQuery) ObjectDeserializer.deserialize(query, CQLQuery.class);
			System.out.println(ObjectSerializer.toString(newQuery,
					new QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "CQLQuery")));		
		} catch (DeserializationException e) {
			fail("test Query XML file could not be deserialized " + e);
		} catch (SerializationException e) {
			fail("test Query XML file could not be serialized " + e);
		}
		assertNotNull(newQuery);
		return newQuery;
	}
}
