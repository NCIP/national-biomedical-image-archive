/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.datamodel;

import gov.nih.nci.nbia.beans.searchresults.ImageResultWrapper;

import javax.faces.model.DataModel;

public interface IcefacesRowColumnDataModelInterface {
    public ImageResultWrapper getCellValue();
    public boolean getCellVisibility();
	public DataModel getRowDataModel(); 
	public DataModel getColumnDataModel();
	public String getImageLabel();
	public boolean getShowPaginator();
}