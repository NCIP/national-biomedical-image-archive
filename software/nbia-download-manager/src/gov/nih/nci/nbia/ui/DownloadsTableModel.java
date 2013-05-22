/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.nbia.ui;

import gov.nih.nci.nbia.download.AbstractSeriesDownloader;
import gov.nih.nci.nbia.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

/**
 * @author lethai
 *
 */
//This class manages the download table's data.
public class DownloadsTableModel extends AbstractTableModel
                                 implements Observer
{
	/*pkg*/ static final int LOCATION_COLUMN = 0;
	/*pkg*/ static final int COLLECTION_COLUMN = 1;
	/*pkg*/ static final int PATIENT_ID_COLUMN = 2;
	/*pkg*/ static final int STUDY_ID_COLUMN = 3;
	/*pkg*/ static final int SERIES_ID_COLUMN = 4;
	/*pkg*/ static final int PROGRESS_COLUMN = SERIES_ID_COLUMN+3;

	
//	These are the names for the table's columns.
	private static final String[] columnNames = {
		"Location",
		"Collection",
		"Patient ID", 
		"Study Instance UID", 
		"Series Instance UID",
		"Size",
		"Number Of Images", 
		"Progress", 
		"Status"};

//	These are the classes for each column's values.
	private static final Class[] columnClasses = {
		String.class,
		String.class,
		String.class,
		String.class,
		String.class,
		String.class, 
		String.class, 
		JProgressBar.class, 
		String.class};

//	The table's list of downloads.
	private List<AbstractSeriesDownloader> downloadList =
		new ArrayList<AbstractSeriesDownloader>();

//	Add a new download to the table.
	public void addDownload(AbstractSeriesDownloader download) {
//		Register to be notified when the download changes.
		download.addObserver(this);

		downloadList.add(download);

//		Fire table row insertion notification to table.
		fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
	}

//	Get a download for the specified row.
	public AbstractSeriesDownloader getDownload(int row) {
		return downloadList.get(row);
	}

//	Remove a download from the list.
	public void clearDownload(int row) {
		downloadList.remove(row);

//		Fire table row deletion notification to table.
		fireTableRowsDeleted(row, row);
	}

//	Get table's column count.
	public int getColumnCount() {
		return columnNames.length;
	}

//	Get a column's name.
	public String getColumnName(int col) {
		return columnNames[col];
	}

//	Get a column's class.
	public Class getColumnClass(int col) {
		return columnClasses[col];
	}

//	Get table's row count.
	public int getRowCount() {
		return downloadList.size();
	}

//	Get value for a specific row and column combination.
	public Object getValueAt(int row, int col) {
		AbstractSeriesDownloader download = downloadList.get(row);
		switch (col) {
		    case LOCATION_COLUMN:
		    	return download.getNode().getDisplayName();
			case COLLECTION_COLUMN: 
				return download.getCollection();
			case PATIENT_ID_COLUMN: 
				return download.getPatientId();
			case STUDY_ID_COLUMN: 			
				return download.getStudyInstanceUid();
			case SERIES_ID_COLUMN:
				return download.getSeriesInstanceUid();
			case SERIES_ID_COLUMN+1:
				long size = download.getSize();				
		        return computeSizeString(size);
			case SERIES_ID_COLUMN+2:
				return download.getNumberOfImages();
	
			case PROGRESS_COLUMN: // Progress
				return new Float(download.getProgress());
			case SERIES_ID_COLUMN+4: // Status
				return AbstractSeriesDownloader.STATUSES[download.getStatus()];
			case SERIES_ID_COLUMN+5:// Additional Info
				return download.getAdditionalInfo();
		}
		return "";
	}


	/* Update is called when a Download notifies its
observers of any changes */
	public void update(final Observable o, Object arg) {

		if(SwingUtilities.isEventDispatchThread()){			
			int index = downloadList.indexOf( o );
			if( index != -1 ) {
				fireTableRowsUpdated( index, index );
			}
		}else{
			SwingUtilities.invokeLater( new Runnable()
			{
				public void run()
				{
					int index = downloadList.indexOf( o );
					if( index != -1 ) {
						fireTableRowsUpdated( index, index );
					}
				}
			} );
		}
	}	
	
	private static String computeSizeString(long size) {
		return (size == -1)? "" : DisplayUtil.computeSizeString(size);
	}	
}

