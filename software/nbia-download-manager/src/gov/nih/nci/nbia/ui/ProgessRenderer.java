/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.ui;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

//This class renders a JProgressBar in a table cell.
class ProgressRenderer extends JProgressBar
                       implements TableCellRenderer
{
//	Constructor for ProgressRenderer.
	public ProgressRenderer(int min, int max) {
		super(min, max);
		this.setStringPainted(true);
	}

	/* Returns this JProgressBar as the renderer
for the given table cell. */
	public Component getTableCellRendererComponent(JTable table, 
			                                       Object value, 
			                                       boolean isSelected,
			                                       boolean hasFocus, 
			                                       int row, 
			                                       int column)	{
//		Set JProgressBar's percent complete value.
		setValue((int) ((Float) value).floatValue());
		return this;
	}	
}