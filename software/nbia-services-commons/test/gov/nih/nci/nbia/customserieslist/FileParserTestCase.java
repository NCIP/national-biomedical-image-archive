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
package gov.nih.nci.nbia.customserieslist;

import gov.nih.nci.nbia.customserieslist.FileParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author lethai
 *
 */
public class FileParserTestCase extends TestCase {
	private FileParser parser;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		parser = new FileParser();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link gov.nih.nci.nbia.customserieslist.FileParser#parse(java.io.File)}.
	 */
	public void testParse() throws Exception{

		File f;
		f=new File("testfile.csv");
		if(!f.exists()){
			try {
				f.createNewFile();
				System.out.println("New file \"testfile.csv\" has been created to the current directory");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String seriesUids = "series 1, series 2, series 3, series 4";

		Writer output = null;
	    output = new BufferedWriter(new FileWriter(f));
	    output.write(seriesUids);
	    output.close();

	    List<String> parsedUids = parser.parse(f);
	    System.out.println("parsed size: " + parsedUids.size());
	    assertEquals(parsedUids.size() , 4);
	}
}