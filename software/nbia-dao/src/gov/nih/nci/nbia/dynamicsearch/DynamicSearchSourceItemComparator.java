package gov.nih.nci.nbia.dynamicsearch;


import gov.nih.nci.nbia.xmlobject.SourceItem;

import java.util.Comparator;


public class DynamicSearchSourceItemComparator implements Comparator<SourceItem> {
    public int compare(SourceItem o1, SourceItem o2) {
    	SourceItem one = (SourceItem) o1;
    	SourceItem two = (SourceItem) o2;
    	
    	//jim wants to push all the nulls to the end
        if ((one == null) && (two == null)) {
            return 0;
        }
        else
        if(one == null && two != null) {
        	return 1;
        }
        else
        if(one != null && two == null) {
        	return -1;
        }
        else {
        	return one.getItemLabel().compareTo(two.getItemLabel());
        }
    }
}

