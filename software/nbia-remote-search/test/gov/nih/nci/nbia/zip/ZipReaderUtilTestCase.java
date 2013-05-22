/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ZipReaderUtilTestCase {

	@Test
	public void testReadZipOfImageFilesForSeries() throws Exception {
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		File outputDir = new File(tempDir, "fakefakedir");
		outputDir.mkdirs();		
	
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.setProject("proj1");
		seriesSearchResult.setPatientId("pat1");
		seriesSearchResult.setStudyInstanceUid("study1");
		seriesSearchResult.setSeriesInstanceUid("series1");
		
		ZipReaderUtil.readZipOfImageFilesForSeries(outputDir,
				                                   createZipInputStream(),
                                                   seriesSearchResult,
                                                   "seriesId",
                                                   null);
		
		File t1 = new File(outputDir, "proj1");
		t1 = new File(t1, "pat1");
		t1 = new File(t1, "study1");
		t1 = new File(t1, "seriesId");
		t1 = new File(t1, "000000.dcm");
		assertTrue(t1.exists());
		
		String s1 = FileUtils.readFileToString(t1);
		assertEquals(s1,"testcontent1");
		
		File t2 = new File(t1.getParentFile(), "000001.dcm");
		assertTrue(t2.exists());
		
		String s2 = FileUtils.readFileToString(t2);
		assertEquals(s2,"testcontent2a");	
	}
		
		
   private static InputStream createZipInputStream() throws Exception {
       File tempDir = new File(System.getProperty("java.io.tmpdir"));
       String testContent1 = "testcontent1";
       String testContent2 = "testcontent2a";    
      
       File tzFile = new File(tempDir, "tz1.zip");
       tzFile.deleteOnExit();
       
       FileOutputStream fos = new FileOutputStream(tzFile);
       ZipOutputStream zos = new ZipOutputStream(fos);
       
       zos.putNextEntry(new ZipEntry("t1.dcm"));
       zos.write(testContent1.getBytes());
       zos.closeEntry();
       
       zos.putNextEntry(new ZipEntry("t2.dcm"));
       zos.write(testContent2.getBytes());
       zos.closeEntry();
       
       zos.close();
     
	   return new FileInputStream(tzFile);
   }

}
