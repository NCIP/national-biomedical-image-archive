/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.ui;

import java.awt.event.MouseEvent;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class DownloadsTable extends JTable {

	public DownloadsTable(DownloadsTableModel tableModel) {
		super(tableModel);

		/* Allow only one row at a time to be selected.*/
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		/* Set up ProgressBar as renderer for progress column.*/
		ProgressRenderer progressRenderer = new ProgressRenderer(0, 100);
		progressRenderer.setStringPainted(true); // show progress text
		setDefaultRenderer(JProgressBar.class, progressRenderer);

		TableColumnModel columnModel = getColumnModel();
		FullValueTooltipRenderer fullValueTooltipRenderer = new FullValueTooltipRenderer();
		columnModel.getColumn(DownloadsTableModel.PATIENT_ID_COLUMN).setCellRenderer(fullValueTooltipRenderer);
		columnModel.getColumn(DownloadsTableModel.STUDY_ID_COLUMN).setCellRenderer(fullValueTooltipRenderer);
		columnModel.getColumn(DownloadsTableModel.SERIES_ID_COLUMN).setCellRenderer(fullValueTooltipRenderer);

		/* Set table's row height large enough to fit JProgressBar.*/
		setRowHeight(
				(int) progressRenderer.getPreferredSize().getHeight());			
	}

	//Implement table cell tool tips.
    public String getToolTipText(MouseEvent e) {
        String tip = null;
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        int realColumnIndex = convertColumnIndexToModel(colIndex);

        if (realColumnIndex == DownloadsTableModel.SERIES_ID_COLUMN+4) { //status column
            TableModel model = getModel();
            tip = "Detail status-"+(String)model.getValueAt(rowIndex,DownloadsTableModel.SERIES_ID_COLUMN+5).toString();
        } else { 
            //You can omit this part if you know you don't 
            //have any renderers that supply their own tool 
            //tips.
            tip = super.getToolTipText(e);
        }
        return tip;
    }
    public TableCellRenderer getCellRenderer( int row, int column ) { 
        if (column == DownloadsTableModel.SERIES_ID_COLUMN+4) { //status column
            return new StatusCellRenderer();
        } else { 
            return super.getCellRenderer(row, column);
        }
    } 


}
