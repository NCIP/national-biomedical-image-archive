/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
*/
package drivers;

import java.net.*;
import java.io.*;

import junit.framework.TestCase;



/**
 * This JUnit Test class tests if three MarkupServlet is functioning in the NCIA portal.
 * @author Q. Pan
 * @version 1.0, Oct. 8 , 2007
 *
**/
public class MarkupServletTest extends TestCase {
    private static final String MarkupQueryIP="http://nciavjbossdev5001.nci.nih.gov:59080/ncia/MarkupQuery";
    private static final String MarkupReadIP="http://nciavjbossdev5001.nci.nih.gov:59080/ncia/MarkupRead";
    private static final String MarkupSaveIP="http://nciavjbossdev5001.nci.nih.gov:59080/ncia/MarkupSave";
    private static final String TestUid="1.3.6.1.4.1.9328.50.1.9075";
    private static final String QStr="~usr=panq@mail.nih.gov&uid="+TestUid;
    private static final String RQStr="uid=" + TestUid +"&";
    private static final String RRStr="<dicom:PresentationState xmlns:dicom=\"urn:openeyes.data.dicom.Dicom/Dicom.xml\">";
    private static final String RSStr="There is no Markup for series "+TestUid;
    /**
     * Constructor for MarkupServletTest.
     * @param name
     */
    public MarkupServletTest(String name) {
        super(name);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMarkupServletTest() {
        MarkupServletTest(MarkupQueryIP);
        MarkupServletTest(MarkupReadIP);
        MarkupServletTest(MarkupSaveIP);
    }
    
  
    
    public void MarkupServletTest(String servletIP) {
        try {
            URL url = new URL(servletIP);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            OutputStreamWriter ostream = new OutputStreamWriter(connection.getOutputStream());
            BufferedWriter out = new BufferedWriter(ostream);
            out.write(QStr);
            out.flush();
            out.close();

            InputStream stream = connection.getInputStream();
            BufferedInputStream in = new BufferedInputStream(stream);

            BufferedReader rdr = new BufferedReader(new InputStreamReader(in));        
            String s=rdr.readLine();
            try {
            if (servletIP.equals(MarkupQueryIP))
                assertEquals(RQStr, s);
            else if (servletIP.equals(MarkupReadIP))
                assertEquals(RRStr, s);
            else if (servletIP.equals(MarkupSaveIP))
                assertEquals(RSStr, s);
            }
            catch (Exception ex){
               ex.printStackTrace(); 
            }
            in.close();
            stream.close();
        }
        catch (MalformedURLException e) {
        }
        catch (IOException ee) {
        }   
    }    
}
