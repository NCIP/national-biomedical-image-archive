/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

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
