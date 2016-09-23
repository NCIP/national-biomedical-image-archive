/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchform;

import gov.nih.nci.nbia.beans.BeanManager;
import java.util.Enumeration;
import javax.faces.event.ValueChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import com.icesoft.faces.component.tree.IceUserObject;
import org.apache.log4j.Logger;

public class EquipmentTreeUserObject extends IceUserObject {
	public EquipmentTreeUserObject(DefaultMutableTreeNode wrapper) {
		super(wrapper);
	}

	public boolean isSelected() {
		return selected;
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public String select(ValueChangeEvent event) {
		recurseSelect(super.getWrapper(), (Boolean)event.getNewValue());

		SearchWorkflowBean swb = BeanManager.getSearchWorkflowBean();
		swb.setAdvanced(true);
		try {
			return swb.submitSearch();
		}
        catch (Exception e) {
            logger.error("Error submitting query", e);
            e.printStackTrace();
        }
		return "search";
	}

	/////////////////////////////////////////PRIVATE///////////////////////////////////////


	private boolean selected = false;
	private static Logger logger = Logger.getLogger(EquipmentTreeUserObject.class);

	private static void recurseSelect(DefaultMutableTreeNode node, boolean selected) {
		EquipmentTreeUserObject userObj = (EquipmentTreeUserObject)node.getUserObject();
		userObj.setSelected(selected);

		Enumeration children = node.children();
		while(children.hasMoreElements()) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)children.nextElement();

			recurseSelect(child, selected);
		}
	}
}