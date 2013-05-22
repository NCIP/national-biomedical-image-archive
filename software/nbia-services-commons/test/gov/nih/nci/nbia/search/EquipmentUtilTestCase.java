/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.dto.EquipmentDTO;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.Manufacturer;
import gov.nih.nci.ncia.search.Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

public class EquipmentUtilTestCase extends TestCase {

	public void testProcessManufacturerObjectArrayMissingField() {
		Map<String, Map<String, Set<String>>> manu
		    = new HashMap<String, Map<String, Set<String>>>();
		
		EquipmentDTO equipmentDTO = new EquipmentDTO(null,"foo","foo");
		EquipmentUtil.processManufacturerObjectArray(equipmentDTO, manu);
		assertTrue(manu.size()==0);
	}
	
	public void testProcessManufacturerObjectArray() {
		Map<String, Map<String, Set<String>>> manu
		    = new HashMap<String, Map<String, Set<String>>>();
		
		EquipmentDTO equipmentDTO = new EquipmentDTO("manu1","model1","v1");
		EquipmentUtil.processManufacturerObjectArray(equipmentDTO, manu);
		assertTrue(manu.size()==1);
		
		Map<String, Set<String>> modelMap = manu.get("manu1");
		assertNotNull(modelMap);
		assertTrue(modelMap.get("model1").contains("v1"));
		
		
		equipmentDTO = new EquipmentDTO("manu1","model2","v1");
		EquipmentUtil.processManufacturerObjectArray(equipmentDTO, manu);
		assertTrue(manu.size()==1);
		
		modelMap = manu.get("manu1");
		assertNotNull(modelMap);
		assertTrue(modelMap.get("model2").contains("v1"));

		equipmentDTO = new EquipmentDTO("manu1","model2","v2");
		EquipmentUtil.processManufacturerObjectArray(equipmentDTO, manu);
		assertTrue(manu.size()==1);		
		
		modelMap = manu.get("manu1");
		assertNotNull(modelMap);
		assertTrue(modelMap.get("model2").contains("v2"));
		
		equipmentDTO = new EquipmentDTO("manu2","model1","v1");
		EquipmentUtil.processManufacturerObjectArray(equipmentDTO, manu);
		assertTrue(manu.size()==2);	
		
		modelMap = manu.get("manu2");
		assertNotNull(modelMap);
		assertTrue(modelMap.get("model1").size()==1);
		assertTrue(modelMap.get("model1").contains("v1"));		

	}	

	public void testConvertEquipmentAvailableSearchTerms() {
		AvailableSearchTerms availableSearchTerms = new AvailableSearchTerms();
		availableSearchTerms.setEquipment(constructManufacturers());
		
		Map<String, Map<String, Set<String>>> manu =
			EquipmentUtil.convertEquipment(availableSearchTerms);
		
		assertTrue(manu.size()==2);
		
		Map<String, Set<String>> modelMap1 = manu.get("man1");
		Set<String> versionSet1 = modelMap1.get("model1");
		assertNotNull(modelMap1.get("model2"));
		assertTrue(versionSet1.size()==2);
		assertTrue(modelMap1.size()==2);
		
		Map<String, Set<String>> modelMap2 = manu.get("man2");
		Set<String> versionSet2 = modelMap2.get("model1");
		assertNotNull(modelMap2.get("model2"));
		assertNotNull(modelMap2.get("model3"));
		assertTrue(versionSet2.size()==2);
		assertTrue(modelMap2.size()==3);
	}

	public void testConvertEquipmentMapOfStringMapOfStringSetOfString() {
		
		Map<String, Map<String, Set<String>>> manu
	        = new LinkedHashMap<String, Map<String, Set<String>>>();
		
		Set<String> versions1 = new HashSet<String>();
		versions1.add("1.0");
		versions1.add("2.0");
		Map<String, Set<String>> modelMap1 = new HashMap<String, Set<String>>();
		modelMap1.put("model1", versions1);
		manu.put("man1", modelMap1);
		
		Set<String> versions2 = new HashSet<String>();
		versions2.add("3.0");
		versions2.add("4.0");
		Map<String, Set<String>> modelMap2 = new LinkedHashMap<String, Set<String>>();
		modelMap2.put("model2", versions2);
		modelMap2.put("model3", new HashSet<String>());
		manu.put("man2", modelMap2);		
		
		Manufacturer[] manufacturers = EquipmentUtil.convertEquipment(manu);		
		assertTrue(manufacturers.length==2);
		
		assertTrue(manufacturers[0].getName().equals("man1"));
		assertTrue(manufacturers[0].getModels().length==1);
		assertTrue(manufacturers[0].getModels()[0].getName().equals("model1"));
		assertTrue(manufacturers[0].getModels()[0].getVersions().length==2);
		assertTrue(contains(manufacturers[0].getModels()[0].getVersions(),"1.0"));
		assertTrue(contains(manufacturers[0].getModels()[0].getVersions(),"2.0"));
	
		assertTrue(manufacturers[1].getName().equals("man2"));
		assertTrue(manufacturers[1].getModels().length==2);
		assertTrue(manufacturers[1].getModels()[0].getName().equals("model2"));
		assertTrue(manufacturers[1].getModels()[1].getName().equals("model3"));
		assertTrue(manufacturers[1].getModels()[0].getVersions().length==2);
		assertTrue(contains(manufacturers[1].getModels()[0].getVersions(),"3.0"));
		assertTrue(contains(manufacturers[1].getModels()[0].getVersions(),"4.0"));
		assertTrue(manufacturers[1].getModels()[1].getVersions().length==0);
	}


	//////////////////////////////////////PRIVATE///////////////////////////////////////
	
	private static String[] createVersions(int begin, int cnt) {
		String[] versions = new String[cnt];
		for(int i=begin;i<begin+cnt;i++) {
			versions[i-begin] = i+".0";
		}
		return versions;
	}
	
	private static Model[] createModels(int cnt) {
		return createModels(1, 1, cnt);		
	}
	
	private static Model[] createModels(int mBegin, int vBegin, int cnt) {
		Model[] models = new Model[cnt];
		for(int i=mBegin;i<mBegin+cnt;i++) {
			Model model = new Model();
			model.setName("model"+i);
			model.setVersions(createVersions(vBegin, 2));
			
			models[i-mBegin]=model;
		}
		return models;		
	}	
	
	//man1->[m1,m2]->[1.0,2.0]
	//man2->[m1,m2, m3]->[1.0,2.0]
	public static Manufacturer[] constructManufacturers() {
		Manufacturer manu1 = new Manufacturer();
		manu1.setName("man1");
		manu1.setModels(createModels(2));
		
		Manufacturer manu2 = new Manufacturer();
		manu2.setName("man2");
		manu2.setModels(createModels(3));
		
		Manufacturer[] manus = new Manufacturer[2];
		manus[0] = manu1;
		manus[1] = manu2;
		
		return manus;
	}	
	
	private static boolean contains(String[] arr, String str) {
		for(String s : arr) {
			if(s.equals(str)) {
				return true;
			}
		}
		return false;
		
	}	
}
