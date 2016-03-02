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
* Revision 1.2  2007/08/29 19:11:07  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/07 12:05:23  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.7  2006/11/10 13:58:03  shinohaa
* grid prototype
*
* Revision 1.6  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.datamodel;

import gov.nih.nci.nbia.searchresult.PatientSearchResult;

import java.util.Arrays;
import java.util.Comparator;

import javax.faces.model.DataModel;
import javax.faces.model.DataModelListener;


public class SortPatientModel extends DataModel {
    private static Comparator<Row> byTrialId = new Comparator<Row>() {
            public int compare(Row o1, Row o2) {
                Row r1 = o1;
                Row r2 = o2;
                PatientSearchResult p1 = (PatientSearchResult) r1.getData();
                PatientSearchResult p2 = (PatientSearchResult) r2.getData();

                return p1.getProject().compareTo(p2.getProject());
            }
        };

    private static Comparator<Row> bySubjectId = new Comparator<Row>() {
            public int compare(Row o1, Row o2) {
                Row r1 = o1;
                Row r2 = o2;
                PatientSearchResult p1 = (PatientSearchResult) r1.getData();
                PatientSearchResult p2 = (PatientSearchResult) r2.getData();

                return p1.getSubjectId().compareTo(p2.getSubjectId());
            }
        };

    private static Comparator<Row> byStudies = new Comparator<Row>() {
            public int compare(Row o1, Row o2) {
                Row r1 = (Row) o1;
                Row r2 = (Row) o2;
                PatientSearchResult p1 = (PatientSearchResult) r1.getData();
                PatientSearchResult p2 = (PatientSearchResult) r2.getData();

                return p1.computeFilteredNumberOfStudies()
                         .compareTo(p2.computeFilteredNumberOfStudies());
            }
        };

    private static Comparator<Row> bySeries = new Comparator<Row>() {
            public int compare(Row o1, Row o2) {
                Row r1 = (Row) o1;
                Row r2 = (Row) o2;
                PatientSearchResult p1 = (PatientSearchResult) r1.getData();
                PatientSearchResult p2 = (PatientSearchResult) r2.getData();

                return p1.computeFilteredNumberOfSeries()
                         .compareTo(p2.computeFilteredNumberOfSeries());
            }
        };

    private DataModel model;
    private Row[] rows;

    public SortPatientModel(DataModel model) {
        this.model = model;

        int rowCount = model.getRowCount();

        if (rowCount != -1) {
            rows = new Row[rowCount];

            for (int i = 0; i < rowCount; ++i) {
                rows[i] = new Row(i);
            }
        }
    }

    public String sortByTrialId() {
        Arrays.sort(rows, byTrialId);

        return null;
    }

    public String sortBySubjectId() {
        Arrays.sort(rows, bySubjectId);

        return null;
    }

    public String sortByStudies() {
        Arrays.sort(rows, byStudies);

        return null;
    }

    public String sortBySeries() {
        Arrays.sort(rows, bySeries);

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
