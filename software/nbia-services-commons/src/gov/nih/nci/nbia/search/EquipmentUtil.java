/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.dto.EquipmentDTO;
import gov.nih.nci.nbia.searchresult.Manufacturer;
import gov.nih.nci.nbia.searchresult.Model;

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
}

