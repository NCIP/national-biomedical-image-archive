/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.qctool;

import javax.faces.model.SelectItem;

public class QcUtil {
	   /**
     * Gets the options for number of displaying items for QC Result.
     *
     * @return array of predefined numbers for displaying search result
     */
    public SelectItem[] getDispItemNums() {
        SelectItem[] dispItemNums = new SelectItem[6];
        
        dispItemNums[0] = new SelectItem( System.getProperty("qctool.search.results.per.page.option.1") );     
        dispItemNums[1] = new SelectItem( System.getProperty("qctool.search.results.per.page.option.2") );
        dispItemNums[2] = new SelectItem( System.getProperty("qctool.search.results.per.page.option.3") );
        dispItemNums[3] = new SelectItem( System.getProperty("qctool.search.results.per.page.option.4") );
        dispItemNums[4] = new SelectItem( System.getProperty("qctool.search.results.per.page.option.5") );
        dispItemNums[5] = new SelectItem( System.getProperty("qctool.search.results.per.page.option.6") );

        return dispItemNums;
    }
    
    /*
     * show the Check All/Uncheck All button based on the return value
     */
    public boolean isShowCheckUncheckAll() {
    	String retValue = System.getProperty("qctool.search.results.check.uncheck.option");
    	
    	if( retValue.equals("1")) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    /*
     * returns the maximum number of rows to show in QC Tool Search Results
     */
    public static int getMaxNumberOfRowsToShow() {
    	String retValue = System.getProperty("qctool.search.results.max.number.of.rows");
    	
    	return Integer.valueOf(retValue);
    }
}
