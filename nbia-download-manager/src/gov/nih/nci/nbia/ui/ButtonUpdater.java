package gov.nih.nci.nbia.ui;

import javax.swing.JButton;

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
    public ButtonUpdater(JButton pauseButton, JButton resumeButton){
        this.pauseButton = pauseButton;
        this.resumeButton = resumeButton;
    }
    public void update() {
        // TODO Auto-generated method stub
        //disable pause/resume buttons
        this.pauseButton.setEnabled(false);
        this.resumeButton.setEnabled(false);
    }
}