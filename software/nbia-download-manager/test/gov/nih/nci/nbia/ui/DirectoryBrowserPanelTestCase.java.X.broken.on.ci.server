/**
 * 
 */
package gov.nih.nci.nbia.ui;

import junit.framework.TestCase;
import gov.nih.nci.nbia.ui.DirectoryBrowserPanel;
/**
 * @author lethai
 *
 */
public class DirectoryBrowserPanelTestCase extends TestCase {
	/**
	 * Test method for {@link gov.nih.nci.nbia.ui.DirectoryBrowserPanel#getDirectory()}.
	 */
    public void testGetDirectory() {
        DirectoryBrowserPanel dp = new DirectoryBrowserPanel();
        String dir = System.getProperty("java.io.tmpdir");
        assertTrue(dp.getDirectory().equals(dir));
    }
}
