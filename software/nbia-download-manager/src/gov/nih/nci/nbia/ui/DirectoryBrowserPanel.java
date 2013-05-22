/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class DirectoryBrowserPanel extends JPanel {
	public DirectoryBrowserPanel() {
		JLabel fileLocation = new JLabel("Select Directory For Downloaded Files:");
		browseTextField = new JTextField(35);
		JButton browseButton = new JButton("Browse");

		add(fileLocation);
		add(browseTextField);
		add(browseButton);

		browseTextField.setText(System.getProperty("java.io.tmpdir"));
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionBrowse();
			}
		});
	}

	public String getDirectory() {
		return browseTextField.getText();
	}

	//////////////////////////////////PRIVATE///////////////////////////
	/* Add download text field. */
	private JTextField browseTextField;
	private JFileChooser chooser ;

	private void actionBrowse(){
		chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        /* disable the "All files" option. */
        chooser.setAcceptAllFileFilterUsed(false);
        //
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {


            browseTextField.setText(chooser.getSelectedFile().getPath());
        }
        else {
            System.out.println("No Selection ");
        }
	}


}
