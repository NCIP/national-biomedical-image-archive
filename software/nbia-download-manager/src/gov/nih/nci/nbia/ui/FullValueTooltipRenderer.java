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
