/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class JsfUtil {
	
    public static UIData findNearestTable(UIComponent comp) {
		while(comp.getParent()!=null) {
			UIComponent parent = comp.getParent();
			if(parent instanceof UIData) {
				return (UIData)parent;
			}	
			else {
				comp = parent;
			}
		}
		return null;
	}   
    
	public static Object getSessionObject(String name) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getExternalContext().getSessionMap().get(name);
	}
	
	public static void putSessionObject(String name, Object object) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.getExternalContext().getSessionMap().put(name, object);
	}	
	
    public static List<SelectItem> getSelectItemsFromStrings(Collection<String> strings) {
    	return getSelectItemsFromStrings(strings, false);
    }
    
    public static List<SelectItem> getBooleanSelectItemsFromStrings(Collection<String> strings) {
    	List<SelectItem> selectItemList = new ArrayList<SelectItem>();
    	
    	for(String aString : strings) {
    		SelectItem item = new SelectItem();
    		item.setLabel(aString);
    		item.setValue(false);
    		selectItemList.add(item);    		
    	}
    	return selectItemList;
    }     
    
    /**
     * This will return a list of SelectItems that can be used for a drop down
     * based upon a list from the lookup manager.
     */    
    public static List<SelectItem> getSelectItemsFromStrings(Collection<String> strings, 
    		                                                 boolean addBlank) {
    	List<SelectItem> selectItemList = new ArrayList<SelectItem>();
    	
        if (addBlank) {
        	selectItemList.add(new SelectItem(""));
        }
        
    	for(String aString : strings) {
    		selectItemList.add(new SelectItem(aString));    		
    	}
    	return selectItemList;
    }    
    
    public static List<String> getStringValuesFromSelectItems(Collection<SelectItem> selectItems) {
    	List<String> valueList = new ArrayList<String>();
    	for(SelectItem selectItem : selectItems) {
    		valueList.add((String)selectItem.getValue());
    	}
    	return valueList;
    }   
    
    public static List<String> getLabelsFromSelectItems(Collection<SelectItem> selectItems) {
    	List<String> valueList = new ArrayList<String>();
    	for(SelectItem selectItem : selectItems) {
    		valueList.add(selectItem.getLabel());
    	}
    	return valueList;
    }     
    
    public static SelectItem findSelectItemByLabel(List<SelectItem> items, String label) {
    	for(SelectItem item : items) {
    		if(item.getLabel().equals(label)) {
    			return item;
    		}
    	}
    	return null;
    }      
    
    public static SelectItem findSelectItemByDescription(List<SelectItem> items, String description) {
    	for(SelectItem item : items) {
    		if(item.getDescription().equals(description)) {
    			return item;
    		}
    	}
    	return null;
    } 
    
    public static List<SelectItem> constructModalitySelectItems(Collection<String> strings, Map<String, String> modalityDescMap) {
    	List<SelectItem> selectItemList = new ArrayList<SelectItem>();
    	
    	for(String aString : strings) {
    		SelectItem item = new SelectItem();
    		item.setLabel(aString);
    		item.setValue(false);
    		String desc = modalityDescMap.get(item.getLabel());
    		if( desc!=null) {
    			item.setDescription(desc);
    		}
    		else {
    			item.setDescription("");
    		}
    		selectItemList.add(item);    		
    	}
    	return selectItemList;    	
    }    
}
