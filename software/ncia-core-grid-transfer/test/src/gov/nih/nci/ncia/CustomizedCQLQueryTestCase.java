/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.ncia;

import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;

import java.io.InputStream;

import javax.xml.namespace.QName;

import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.encoding.SerializationException;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;


public class CustomizedCQLQueryTestCase {
	@Test
	public void testModifyCQLQueryImageTarget() {
		String filename = "testImage.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);
		
		CQLQuery modifiedCqlQuery = CustomizedCQLQuery.modifyCQLQueryToTargetImage(fcqlq);
				
		Assert.assertTrue(modifiedCqlQuery==fcqlq);	
	}

	@Test
	public void testModifyCQLQuerySeriesTargetJustAssociation() {
		String filename = "testCase3.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);
		
		CQLQuery modifiedCqlQuery = CustomizedCQLQuery.modifyCQLQueryToTargetImage(fcqlq);
		
		Assert.assertNotNull("results is not null", modifiedCqlQuery);
		
		Assert.assertTrue(modifiedCqlQuery.getTarget().getName().equals("gov.nih.nci.ncia.domain.Image"));
		
		Association seriesAssociation = modifiedCqlQuery.getTarget().getAssociation();
		Assert.assertTrue(seriesAssociation.getName().equals("gov.nih.nci.ncia.domain.Series"));
		Assert.assertTrue(seriesAssociation.getRoleName().equals("series"));

		Assert.assertNull(seriesAssociation.getAttribute());
		Assert.assertNull(seriesAssociation.getGroup());
	}
	
	@Test	
	public void testModifyCQLQueryStudyTargetWithJustAttribute() {
		String filename = "testCase-studyTargetJustAttribute.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);
		
		Attribute beforeAttribute = fcqlq.getTarget().getAttribute();
		
		CQLQuery modifiedCqlQuery = CustomizedCQLQuery.modifyCQLQueryToTargetImage(fcqlq);
		
		Assert.assertNotNull("results is not null", modifiedCqlQuery);
		
		Assert.assertTrue(modifiedCqlQuery.getTarget().getName().equals("gov.nih.nci.ncia.domain.Image"));
		
		Association seriesAssociation = modifiedCqlQuery.getTarget().getAssociation();
		Assert.assertTrue(seriesAssociation.getName().equals("gov.nih.nci.ncia.domain.Series"));
		Assert.assertTrue(seriesAssociation.getRoleName().equals("series"));
		
		Association studyAssociation = seriesAssociation.getAssociation();
		Assert.assertTrue(studyAssociation.getName().equals("gov.nih.nci.ncia.domain.Study"));
		Assert.assertTrue(studyAssociation.getRoleName().equals("study"));
		Assert.assertTrue(studyAssociation.getAttribute().equals(beforeAttribute));
		
		Assert.assertNull(studyAssociation.getAssociation());
		Assert.assertNull(studyAssociation.getGroup());		
	}	
	
	@Test	
	public void testModifyCQLQueryStudyTargetWithJustAssociation() {
		String filename = "testCase-studyTargetJustAssociation.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);
		
		Association beforeAssociation = fcqlq.getTarget().getAssociation();
		
		CQLQuery modifiedCqlQuery = CustomizedCQLQuery.modifyCQLQueryToTargetImage(fcqlq);
		
		Assert.assertNotNull("results is not null", modifiedCqlQuery);
		
		Assert.assertTrue(modifiedCqlQuery.getTarget().getName().equals("gov.nih.nci.ncia.domain.Image"));
		
		Association seriesAssociation = modifiedCqlQuery.getTarget().getAssociation();
		Assert.assertTrue(seriesAssociation.getName().equals("gov.nih.nci.ncia.domain.Series"));
		Assert.assertTrue(seriesAssociation.getRoleName().equals("series"));
		
		Association studyAssociation = seriesAssociation.getAssociation();
		Assert.assertTrue(studyAssociation.getName().equals("gov.nih.nci.ncia.domain.Study"));
		Assert.assertTrue(studyAssociation.getRoleName().equals("study"));
		Assert.assertTrue(studyAssociation.getAssociation().equals(beforeAssociation));
		
		Assert.assertNull(studyAssociation.getAttribute());
		Assert.assertNull(studyAssociation.getGroup());		
	}
	
	@Test	
	public void testModifyCQLQueryPatientTargetWithJustAttribute() {
		String filename = "testCase-patientTargetJustAttribute.xml";
		final CQLQuery fcqlq = this.loadXMLFile(filename);
		
		Attribute beforeAttribute = fcqlq.getTarget().getAttribute();
		
		CQLQuery modifiedCqlQuery = CustomizedCQLQuery.modifyCQLQueryToTargetImage(fcqlq);
		
		Assert.assertNotNull("results is not null", modifiedCqlQuery);
		
		Assert.assertTrue(modifiedCqlQuery.getTarget().getName().equals("gov.nih.nci.ncia.domain.Image"));
		
		Association seriesAssociation = modifiedCqlQuery.getTarget().getAssociation();
		Assert.assertTrue(seriesAssociation.getName().equals("gov.nih.nci.ncia.domain.Series"));
		Assert.assertTrue(seriesAssociation.getRoleName().equals("series"));
		
		Association studyAssociation = seriesAssociation.getAssociation();
		Assert.assertTrue(studyAssociation.getName().equals("gov.nih.nci.ncia.domain.Study"));
		Assert.assertTrue(studyAssociation.getRoleName().equals("study"));	
		
		Association patientAssociation = studyAssociation.getAssociation();
		Assert.assertTrue(patientAssociation.getName().equals("gov.nih.nci.ncia.domain.Patient"));
		Assert.assertTrue(patientAssociation.getRoleName().equals("patient"));
		Assert.assertTrue(patientAssociation.getAttribute().equals(beforeAttribute));
		
		Assert.assertNull(patientAssociation.getAssociation());
		Assert.assertNull(patientAssociation.getGroup());		
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
			Assert.fail("test Query XML file could not be deserialized " + e);
		} catch (SerializationException e) {
			Assert.fail("test Query XML file could not be serialized " + e);
		}
		Assert.assertNotNull(newQuery);
		return newQuery;
	}
}
