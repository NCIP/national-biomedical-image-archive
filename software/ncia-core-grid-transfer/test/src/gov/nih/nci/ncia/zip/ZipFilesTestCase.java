/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.zip;

import gov.nih.nci.ncia.gridzip.ZipFiles;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.junit.Assert;
import org.junit.Test;

public class ZipFilesTestCase {
	@Test
	public void testStartNewFile() throws Exception {
		ZipFiles zipFiles = new ZipFiles();
		
		String tempDirStr = System.getProperty("java.io.tmpdir");
		File tempDir = new File(tempDirStr);	
		File tempZipFile = new File(tempDir, "testStartNewFile.zip");
		
		URL zipTestFile1Url = this.getClass().getClassLoader().getResource("zipTestFile1.txt");
		URL zipTestFile2Url = this.getClass().getClassLoader().getResource("zipTestFile2.txt");
			
		File zipTestFile1 = new File(zipTestFile1Url.getFile());
		File zipTestFile2 = new File(zipTestFile2Url.getFile());
		
		try {
			zipFiles.startNewFile(tempZipFile.getAbsolutePath(), 0);
			zipFiles.zip("fooDir1", 
					     zipTestFile1.getAbsolutePath(),
					     "newName1.txt");
			zipFiles.zip("fooDir2", 
				         zipTestFile2.getAbsolutePath(),
				         "newName2.txt");
		}
		finally {
			zipFiles.closeFile();
		}
		
		verifyZipStructure(tempZipFile);
		tempZipFile.delete();
	}
	
	private static String FILE_SEPARATOR = System.getProperty("file.separator");
	
	private void verifyZipStructure(File zipFile) throws Exception {
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.getAbsolutePath()));
		
	    // Get the first entry
		ZipEntry zipEntry = null;
		while((zipEntry=zis.getNextEntry())!=null) {
		    byte[] buffer = new byte[2048];
		    int nBytesRead = zis.read(buffer, 0, buffer.length);
		    String fileContents = new String(buffer, 0, nBytesRead);
                     
		    
			if(zipEntry.getName().equals("fooDir1"+FILE_SEPARATOR+"newName1.txt")) {
				Assert.assertEquals(fileContents, "This is a test file");
			}
			else  
			if(zipEntry.getName().equals("fooDir2"+FILE_SEPARATOR+"newName2.txt")) {
				Assert.assertEquals(fileContents, "This is another test file.");
			}
			else {
				Assert.fail(zipEntry.getName());
			}
		}
		zis.close();		
	}
	
	
}
