/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.datamodel;

import gov.nih.nci.nbia.beans.searchresults.ImageResultWrapper;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

public class IcefacesRowColumnDataModel implements IcefacesRowColumnDataModelInterface {

	 
	public IcefacesRowColumnDataModel(List<ImageResultWrapper> thumbnailImageDto)
	{
		this.thumbnailImageDto = thumbnailImageDto;
		generateDataModels();
	}
	

    public ImageResultWrapper getCellValue()
	{
	    if (rowDataModel.isRowAvailable() && columnDataModel.isRowAvailable() ){
	    	String row = (String)rowDataModel.getRowData();
            int currentRow = Integer.parseInt(row);
            Object column = (String)columnDataModel.getRowData();
           int currentColumn = ((List<String>)columnDataModel.getWrappedData()).indexOf(column);
            intValue = currentRow + currentColumn + currentRow*(actureColumns -1);

            if (intValue <= thumbnailImageDto.size()-1)
            {
            	return thumbnailImageDto.get(intValue);
            }
	    }
	    return null;
	}
	    
	    
    public boolean getCellVisibility()
    {
        if (rowDataModel.isRowAvailable() && columnDataModel.isRowAvailable())
        {
            String row = (String)rowDataModel.getRowData();
            int currentRow = Integer.parseInt(row);
            Object column = (String)columnDataModel.getRowData();
            int currentColumn = ((List<String>)columnDataModel.getWrappedData()).indexOf(column);
        
            return getRegisteredId(currentRow, currentColumn);
        }
  
        return false;
    }
		

	public DataModel getColumnDataModel() {
		return columnDataModel;
	}

	public void setColumnDataModel(DataModel columnDataModel) {
		this.columnDataModel = columnDataModel;
	}

	public DataModel getRowDataModel() {
		return rowDataModel;
	}

	public void setRowDataModel(DataModel rowDataModel) {
		this.rowDataModel = rowDataModel;
	}

	public String getImageLabel() {
		return imageLabel;
	}

	public void setImageLabel(String imageLabel) {
		this.imageLabel = imageLabel;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public boolean getShowPaginator()
	{
		int totalImage = 4*columns;
		if (thumbnailImageDto.size() < totalImage)
		{
			return false;
		}
		else
		{	
			return true;
		}
	}
	
	
	///////////////////////////////////////////////////////PRIVATE/////////////////////////////////////
	private DataModel columnDataModel;
	private DataModel rowDataModel;
	private List<ImageResultWrapper> thumbnailImageDto; 
	private int columns = 5;
	private int actureColumns = 5;
	private  int rows = 4;
	private int intValue = 0;
	private String imageLabel;
	 
	private boolean getRegisteredId(int r, int c)
	{
		intValue = r + c + r*(actureColumns -1);

		if (intValue > thumbnailImageDto.size()-1)
		{
			return false;
		}
		this.setImageLabel(""+(intValue +1));
		return true;
	}
	
	private void calculateRows()
	{
		rows = thumbnailImageDto.size() / actureColumns;
		if (thumbnailImageDto.size() % actureColumns != 0)
		{
			rows += 1;
		}
	}	
	
	private void generateDataModels()
	{
		List<String> rowList = new ArrayList<String>();
		calculateRows();
		for (int i = 0; i < rows; i++)
		{
			rowList.add(""+i);
		}
		if (rowDataModel == null)
		{
			rowDataModel = new ListDataModel(rowList);
		}
		else
		{
			rowDataModel.setWrappedData(rowList);
		}
		
		List<String> columnList = new ArrayList<String>();
		
		if (thumbnailImageDto.size() < columns)
		{
			actureColumns = thumbnailImageDto.size();
		}
		else
		{
			actureColumns = columns;
		}
		
		for (int i = 0; i < actureColumns; i++)
		{
			columnList.add(""+i);
		}
		if (columnDataModel == null)
		{
			columnDataModel = new ListDataModel(columnList);
		}
		else
		{
			columnDataModel.setWrappedData(columnList);
		}
	}	
}
