/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.qctool;

import javax.faces.model.SelectItem;
import gov.nih.nci.nbia.util.NCIAConfig;
public class QcUtil {
	   /**
     * Gets the options for number of displaying items for QC Result.
     *
     * @return array of predefined numbers for displaying search result
     */
    public SelectItem[] getDispItemNums() {
        SelectItem[] dispItemNums = new SelectItem[6];
        
        dispItemNums[0] = new SelectItem( NCIAConfig.getQctoolSearchResultsPerPageOption1());     
        dispItemNums[1] = new SelectItem( NCIAConfig.getQctoolSearchResultsPerPageOption2());
        dispItemNums[2] = new SelectItem( NCIAConfig.getQctoolSearchResultsPerPageOption3());
        dispItemNums[3] = new SelectItem( NCIAConfig.getQctoolSearchResultsPerPageOption4());
        dispItemNums[4] = new SelectItem( NCIAConfig.getQctoolSearchResultsPerPageOption5());
        dispItemNums[5] = new SelectItem( NCIAConfig.getQctoolSearchResultsPerPageOption6());

        return dispItemNums;
    }
    
    /*
     * show the Check All/Uncheck All button based on the return value
     */
    public boolean isShowCheckUncheckAll() {
    	String retValue = NCIAConfig.getQctoolSearchResultsCheckUncheckOption();
    	
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
    	String retValue = NCIAConfig.getQctoolSearchResultsMaxNumberOfRows();
    	
    	return Integer.valueOf(retValue);
    }
}
