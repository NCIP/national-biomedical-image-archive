/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * This renderer shows the full value of the cell in the
 * tooltip.  
 */
public class FullValueTooltipRenderer extends DefaultTableCellRenderer  {


    public Component getTableCellRendererComponent(JTable table, 
    		                                       Object value,
    		                                       boolean isSelected, 
    		                                       boolean hasFocus,
    		                                       int row, 
    		                                       int column) {
            
        setToolTipText(value.toString());
        return super.getTableCellRendererComponent(table, 
        		                                   value, 
        		                                   isSelected, 
        		                                   hasFocus, 
        		                                   row,
        		                                   column);
    }        
}
