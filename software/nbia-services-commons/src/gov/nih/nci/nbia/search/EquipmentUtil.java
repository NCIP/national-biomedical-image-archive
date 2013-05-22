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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EquipmentUtil {
	/**
	 * Take an EquipmentDTO object that comes from a DAO.... add it to the
	 * appopriate place in the specified equipment map.
	 * 
	 * <p>The the manufacturer doesn't exist, add an entry... if it does
	 * add the model to it.... if that doesn't exist, add a model entry, etc.
	 * 
	 * <p>If the DTO doesn contain ALL fields: manufactuer, model, version, then
	 * DO NOTHING, i.e. don't add it to the map.
	 */
    public static void processManufacturerObjectArray(EquipmentDTO equipment,
    		                                          Map<String, Map<String, Set<String>>> manu) {
        String manufacturer = equipment.getManufacturer();
        String model = equipment.getModel();
        String softwareVersion = equipment.getVersion();
        
        Map<String, Set<String>> modelHashMap;
        Set<String> softwareVersions;

        if ((manufacturer != null) && (model != null) && (softwareVersion != null)) {
            if (manu.get(manufacturer) != null) {
                modelHashMap = manu.get(manufacturer);
            }
            else {
                modelHashMap = new LinkedHashMap<String, Set<String>>();
                manu.put(manufacturer, modelHashMap);
            }

            if (modelHashMap.get(model) != null) {
                softwareVersions = modelHashMap.get(model);
            }
            else {
                softwareVersions = new HashSet<String>();
                modelHashMap.put(model, softwareVersions);
            }

            softwareVersions.add(softwareVersion);
        }    	
    }	
    
    /**
     * Given an AvailableSearchTerms object with its array of Manufactuer objects....
     * turn it into the Map of manufacturer->models->versions used elsewhere in the
     * system.
     */
	public static Map<String, Map<String, Set<String>>> convertEquipment(AvailableSearchTerms availableSearchTerms) {
    	Map<String, Map<String, Set<String>>> equipmentMap = new HashMap<String, Map<String, Set<String>>>();
    
		Manufacturer[] equipment = availableSearchTerms.getEquipment();
		for(Manufacturer manufacturer : equipment) {
		    Map<String, Set<String>> modelMap = equipmentMap.get(manufacturer.getName());
		    if(modelMap==null) {
		    	modelMap = new HashMap<String, Set<String>>();
		    	equipmentMap.put(manufacturer.getName(), modelMap);
		    }
		    
		    for(Model model : manufacturer.getModels()) {
		    	Set<String> versions = modelMap.get(model.getName());
		    	if(versions==null) {
		    		versions = new HashSet<String>();
		    		modelMap.put(model.getName(), versions);
		    	}
		    	versions.addAll(Arrays.asList(model.getVersions()));
		    }
		}	
		return equipmentMap;
	}
	
	
    /**
     * Given an the Map of equipment used throughout the system....
     * turn it into an AvailableSearchTerms object (for transit over grid interface).
     */	
	public static Manufacturer[] convertEquipment(Map<String, Map<String, Set<String>>> map) {
		
		Manufacturer[] manufacturers = new Manufacturer[map.size()];
		int i = 0;
		for(String manuName : map.keySet()) {
			Manufacturer manufacturer = new Manufacturer();
			manufacturer.setName(manuName);
			
			List<Model> models = new ArrayList<Model>();
			for(String modelName : map.get(manuName).keySet()) {
				Model model = new Model();
				model.setName(modelName);
				
				Set<String> versions = map.get(manuName).get(modelName);
				model.setVersions(versions.toArray(new String[]{}));
				
				models.add(model);
			}
			
			manufacturer.setModels(models.toArray(new Model[]{}));
			
			manufacturers[i] = manufacturer;
			i+=1;
		}
		return manufacturers;
	}
}
