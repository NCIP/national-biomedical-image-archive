/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.zip;

import gov.nih.nci.ncia.gridzip.ZipManager;
import gov.nih.nci.ncia.gridzip.ZippingDTO;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.junit.Assert;
import org.junit.Test;

public class ZipManagerTestCase {
	@Test
	public void testZip() throws Exception {
		String tempDirStr = System.getProperty("java.io.tmpdir");
		
		URL zipTestFile1Url = this.getClass().getClassLoader().getResource("zipTestFile1.txt");
		URL zipTestFile2Url = this.getClass().getClassLoader().getResource("zipTestFile2.txt");
			
		File zipTestFile1 = new File(zipTestFile1Url.getFile());
		File zipTestFile2 = new File(zipTestFile2Url.getFile());
		
		
		ZippingDTO zdto1 = new ZippingDTO();
		zdto1.setSopInstanceUidAsFileName("fakeSop1");
		zdto1.setProject("fakeProject1");
		zdto1.setPatientId("fakePatient1");
		zdto1.setSeriesInstanceUid("fakeSeries1");
		zdto1.setStudyInstanceUid("fakeStudy1");
		zdto1.setFilePath(zipTestFile1.getAbsolutePath());

		ZippingDTO zdto2 = new ZippingDTO();
		zdto2.setSopInstanceUidAsFileName("fakeSop2");
		zdto2.setProject("fakeProject2");
		zdto2.setPatientId("fakePatient2");
		zdto2.setSeriesInstanceUid("fakeSeries2");
		zdto2.setStudyInstanceUid("fakeStudy2");
		zdto2.setFilePath(zipTestFile2.getAbsolutePath());		
								
		List<ZippingDTO> zdtos = new ArrayList<ZippingDTO>();
		zdtos.add(zdto1);
		zdtos.add(zdto2);
		
		File tempZipFile = new File(tempDirStr, "testZipManager.zip");

		ZipManager zipManager = new ZipManager();
		zipManager.setDestinationFilePath(tempZipFile.getParent()+System.getProperty("file.separator"));
		zipManager.setDestinationFileName(tempZipFile.getName());
		zipManager.setZdto(zdtos);
		zipManager.zip();
		//zipManager.run();
		
		System.out.println("temp:"+tempZipFile.getAbsolutePath());
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
                     
			if(zipEntry.getName().equals("fakeProject1"+FILE_SEPARATOR+"fakePatient1"+FILE_SEPARATOR+"fakeStudy1"+FILE_SEPARATOR+"fakeSeries1"+FILE_SEPARATOR+"fakeSop1")) {
				Assert.assertEquals(fileContents, "This is a test file");
			}
			else  
			if(zipEntry.getName().equals("fakeProject2"+FILE_SEPARATOR+"fakePatient2"+FILE_SEPARATOR+"fakeStudy2"+FILE_SEPARATOR+"fakeSeries2"+FILE_SEPARATOR+"fakeSop2")) {
				Assert.assertEquals(fileContents, "This is another test file.");
			}
			else {
				Assert.fail(zipEntry.getName());
			}
		}
		zis.close();		
	}	
}
