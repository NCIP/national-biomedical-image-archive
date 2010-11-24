package gov.nih.nci.nbia.ui;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

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
}
