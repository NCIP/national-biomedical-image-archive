/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/07 12:05:23  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.4  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.datamodel;

import gov.nih.nci.nbia.dto.SavedQueryDTO;

import java.util.Arrays;
import java.util.Comparator;

import javax.faces.model.DataModel;
import javax.faces.model.DataModelListener;


public class SortSavedQueryModel extends DataModel {
    private static Comparator<Row> byQueryName = new Comparator<Row>() {
            public int compare(Row o1, Row o2) {
                Row r1 = o1;
                Row r2 = o2;
                SavedQueryDTO p1 = (SavedQueryDTO) r1.getData();
                SavedQueryDTO p2 = (SavedQueryDTO) r2.getData();

                return p1.getQueryName().compareTo(p2.getQueryName());
            }
        };

	private static Comparator<Row> byCreatorID = new Comparator<Row>() {
		public int compare(Row o1, Row o2) {
			Row r1 = o1;
			Row r2 = o2;
			SavedQueryDTO p1 = (SavedQueryDTO) r1.getData();
			SavedQueryDTO p2 = (SavedQueryDTO) r2.getData();

			return p1.getUser().getLoginName().compareTo(p2.getUser().getLoginName());
		}
	};

    private static Comparator<Row> byLastExecute = new Comparator<Row>() {
            public int compare(Row o1, Row o2) {
                Row r1 = o1;
                Row r2 = o2;
                SavedQueryDTO p1 = (SavedQueryDTO) r1.getData();
                SavedQueryDTO p2 = (SavedQueryDTO) r2.getData();

                return p1.getExecutionTime().compareTo(p2.getExecutionTime());
            }
        };

    private DataModel model;
    private Row[] rows;

    public SortSavedQueryModel(DataModel model) {
        this.model = model;

        int rowCount = model.getRowCount();

        if (rowCount != -1) {
            rows = new Row[rowCount];

            for (int i = 0; i < rowCount; ++i) {
                rows[i] = new Row(i);
            }
        }
    }

    public String sortByQueryName() {
        Arrays.sort(rows, byQueryName);

        return null;
    }

    public String sortByQueryCreatorID() {
        Arrays.sort(rows, byCreatorID);

        return null;
    }

    public String sortByLastExecute() {
        Arrays.sort(rows, byLastExecute);

        return null;
    }

    public void setRowIndex(int rowIndex) {
        if ((rowIndex == -1) || (rowIndex >= model.getRowCount())) {
            model.setRowIndex(rowIndex);
        } else {
            model.setRowIndex(rows[rowIndex].row);
        }
    }

    public boolean isRowAvailable() {
        return model.isRowAvailable();
    }

    public int getRowCount() {
        return model.getRowCount();
    }

    public Object getRowData() {
        return model.getRowData();
    }

    public int getRowIndex() {
        return model.getRowIndex();
    }

    public Object getWrappedData() {
        return model.getWrappedData();
    }

    public void setWrappedData(Object data) {
        model.setWrappedData(data);
    }

    public void addDataModelListener(DataModelListener listener) {
        model.addDataModelListener(listener);
    }

    public DataModelListener[] getDataModelListeners() {
        return model.getDataModelListeners();
    }

    public void removeDataModelListener(DataModelListener listener) {
        model.removeDataModelListener(listener);
    }

    private class Row {
        private int row;

        public Row(int row) {
            this.row = row;
        }

        public Object getData() {
            int origIndex = model.getRowIndex();
            model.setRowIndex(row);

            Object thisRowData = model.getRowData();
            model.setRowIndex(origIndex);

            return thisRowData;
        }
    }
}
