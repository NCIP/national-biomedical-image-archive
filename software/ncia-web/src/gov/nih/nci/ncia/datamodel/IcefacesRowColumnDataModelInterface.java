package gov.nih.nci.ncia.datamodel;

import gov.nih.nci.ncia.beans.searchresults.ImageResultWrapper;

import javax.faces.model.DataModel;

public interface IcefacesRowColumnDataModelInterface {
    public ImageResultWrapper getCellValue();
    public boolean getCellVisibility();
	public DataModel getRowDataModel(); 
	public DataModel getColumnDataModel();
	public String getImageLabel();
	public boolean getShowPaginator();
}