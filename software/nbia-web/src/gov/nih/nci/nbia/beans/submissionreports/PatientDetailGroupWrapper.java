/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.submissionreports;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

/**
 * This is a row in the patient details table, but it includes
 * information about "child" rows that would be shown when
 * the row is expanded (to show study, series details underneath).
 */
public class PatientDetailGroupWrapper extends PatientDetailWrapper {

	public PatientDetailGroupWrapper(String patient,
			                         String study,
			                         String series,
			                         String img,
			                         List<PatientDetailGroupWrapper> topLevelList) {
		super(patient, study, series, img);
		this.topLevelList = topLevelList;
	}

	/**
	 * Rows that should be shown when this row is expanded.
	 * Add a child by mutating the list returned here (e.g. getChildren().add(xxx))
	 */
	public List<PatientDetailGroupWrapper> getChildren() {
		return children;
	}

    /**
     * Toggles the expanded state of this row/record.
     */
    public void toggleSubGroupAction(ActionEvent event) {
        // toggle expanded state
        isExpanded = !isExpanded;

        // add sub elements to list
        if (isExpanded) {
            expandNodeAction();
        }
        // remove items from list
        else {
            contractNodeAction();
        }
    }

    /**
     * Gets the image which will represent either the expanded or contracted
     * state of the <code>FilesGroupRecordBean</code>.
     *
     * @return name of image to draw
     */
    public String getExpandContractImage() {
        String img = isExpanded ? "tree_nav_top_close_no_siblings.gif" : "tree_nav_top_open_no_siblings.gif";
        return DEFAULT_IMAGE_DIR + img;
    }

    //////////////////////////////PRIVATE///////////////////////////////////

	private List<PatientDetailGroupWrapper> children = new ArrayList<PatientDetailGroupWrapper>();

	/**
	 * reference back to the list that contains this object.  need
	 * this to handle expand/collapse operations
	 */
	private List<PatientDetailGroupWrapper> topLevelList;

    private boolean isExpanded;

    /**
     * change this to change the theme.
     */
    private static final String DEFAULT_IMAGE_DIR = "/xmlhttp/css/rime/css-images/";

    /**
     * Utility method to add all child nodes to the parent dataTable list.
     */
    private void expandNodeAction() {
        if (children != null && children.size() > 0) {
            // get index of current node
            int index = topLevelList.indexOf(this);

            // add all items in childFilesRecords to the parent list
            topLevelList.addAll(index + 1, children);
        }

    }

    /**
     * Utility method to remove all child nodes from the parent dataTable list.
     */
    private void contractNodeAction() {
        if (children != null && children.size() > 0) {
            // remove all items in childFilesRecords from the parent list
        	removeChildren(this);
        }
    }

    private void removeChildren(PatientDetailGroupWrapper w) {
    	if(w.children==null || w.children.size()==0) {
    		return;
    	}

    	for(PatientDetailGroupWrapper child : w.children) {
    		child.isExpanded = false;
    		removeChildren(child);
    	}

    	topLevelList.removeAll(w.children);
    }
}