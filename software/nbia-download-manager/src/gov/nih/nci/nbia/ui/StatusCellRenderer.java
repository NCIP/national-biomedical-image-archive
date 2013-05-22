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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.lang.StringUtils;

/**
 * @author niktevv
 *
 */
public class StatusCellRenderer extends JPanel implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        this.setBackground(Color.WHITE);
        JComponent comp = new JPanel();
        comp.setBackground(Color.WHITE);
        comp.setLayout(new BoxLayout(comp, BoxLayout.LINE_AXIS));
        JTextField text =new JTextField(value.toString()){ 
            @Override public void setBorder(Border border) { 
            // No! 
            } 
        }; 
        comp.add(text);
        if(StringUtils.equalsIgnoreCase(value.toString(), "Complete") || StringUtils.equalsIgnoreCase(value.toString(), "Error")) {
            JButton button = new JButton("...");
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setPreferredSize(new Dimension(25, 15));  
            comp.add(Box.createVerticalGlue()); 
            comp.add(button);
        }
        this.setLayout(new BorderLayout());
        this.add(comp,BorderLayout.CENTER);
        return this; 
	}

}
