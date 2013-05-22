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
package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.util.NBIAIOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * @author lethai
 *
 */
public class NBIAIOUtilsTestCase extends TestCase {

    /**
     * Test method for {@link gov.nih.nci.nbia.util.NBIAIOUtils#copy(java.io.InputStream, java.io.OutputStream, gov.nih.nci.nbia.util.NBIAIOUtils.ProgressInterface)}.
     */
    public void testCopy() {
        InputStream is = new ByteArrayInputStream("1234567890Test".getBytes());
        OutputStream os = new ByteArrayOutputStream();
        try{
            int bytes = NBIAIOUtils.copy(is, os, this.progressUpdater);
            assertEquals(os.toString(), "1234567890Test");
            assertTrue(bytes>0);
        }catch(IOException ie){
            System.out.println("IOException " + ie.getMessage());
        }
    }

    /**
     * Test method for {@link gov.nih.nci.nbia.util.NBIAIOUtils#copyLarge(java.io.InputStream, java.io.OutputStream, gov.nih.nci.nbia.util.NBIAIOUtils.ProgressInterface)}.
     */
    public void testCopyLarge() {
        InputStream is = new ByteArrayInputStream("1234567890TestBytes".getBytes());
        OutputStream os = new ByteArrayOutputStream();
        try{
            long bytes = NBIAIOUtils.copyLarge(is, os, this.progressUpdater);
            assertEquals(os.toString(), "1234567890TestBytes");
            assertTrue(bytes>0);
        }catch(IOException ie){
            System.out.println("IOException " + ie.getMessage());
        }
    }

    private class ProgressUpdater implements NBIAIOUtils.ProgressInterface {
        private int totalBytes=0;
        public void bytesCopied(int n) {
            totalBytes +=n;
        }
    }

    private NBIAIOUtils.ProgressInterface progressUpdater = new ProgressUpdater();
}
