/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.ui;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.apache.commons.lang.StringUtils;

import gov.nih.nci.nbia.util.ThreadPoolListener;

/**
 * @author lethai
 *
 */
public class ButtonUpdater implements ThreadPoolListener {

    /* (non-Javadoc)
     * @see gov.nih.nci.nbia.util.ThreadPoolListener#update()
     */
    private JButton  pauseButton, resumeButton; 
    private JLabel errorLabel;  // lrt - added errorLabel
    public ButtonUpdater(JButton pauseButton, JButton resumeButton, JLabel errorLabel){
        this.pauseButton = pauseButton;
        this.resumeButton = resumeButton;
	// lrt added errorLabel to record final status
	this. errorLabel = errorLabel;
    }
    public void update() {
        // TODO Auto-generated method stub
        //disable pause/resume buttons
        this.pauseButton.setEnabled(false);
        this.resumeButton.setEnabled(false);
        if (!errorLabel.isVisible()) {
            this.errorLabel.setText("Downloads Complete");  // lrt - let user know that we are done with all downloads and retries
        } else {
            String errLabletext = this.errorLabel.getText();
            this.errorLabel.setText(errLabletext + " Please contact support for failed series. Downloads Complete");
        }
	this.errorLabel.setVisible(true);
    }
}