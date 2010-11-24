package gov.nih.nci.nbia.ui.actions;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ExitAction extends AbstractAction {

	public ExitAction() {
		putValue(MNEMONIC_KEY, KeyEvent.VK_X);
	}
	
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}
