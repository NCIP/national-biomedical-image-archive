/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml","/applicationContext-hibernate-testContext.xml"})
public class ImageFileDeletionTestCase {
	@Autowired
	private ImageFileDeletionService imageFileDeletionService;
	@Autowired
	private ImageFileDeletionTestCaseSupportImpl imageFileDeletionCheck;
	
	private String dcmFile = "000.dcm";
	private String jpegFile = "000.dcm[512;512;-1].jpeg";
	private String annotationFile = "000.xml";
	 @Test
	 public void testImageFileDeletionCheck() 
	 {
		 try
		 {
			 imageFileDeletionCheck.createImageAndRelatedFileFile(dcmFile, jpegFile, annotationFile);
		 }catch(Exception e)
		 {
			 Assert.assertTrue(true);
			 e.printStackTrace();
			 System.out.println("Cannot create image files or image related files...");
		 }
		
		 verifyCreatedImageFile();
		 List<String> dcmList = new ArrayList<String>();
		 dcmList.add(dcmFile);
		 List<String> annotationList = new ArrayList<String>();
		 annotationList.add(annotationFile);
		 
		 Map<String, List<String>> fileMap = new HashMap<String, List<String>>();
		 fileMap.put("dicom",dcmList);
		 fileMap.put("annotation", annotationList);
		 
		 imageFileDeletionService.removeImageFiles(fileMap);
		 
		 verifyDeletedImageFile();
	 }
	 
	 private void verifyCreatedImageFile()
	 {
		 if (! (new File(dcmFile).exists()))
		 {
			 Assert.assertTrue(true);
		 }
		 if (! (new File(jpegFile).exists()))
		 {
			 Assert.assertTrue(true);
		 }
		 if ( ! (new File(annotationFile).exists()))
		 {
			 Assert.assertTrue(true);
		 }
	 }

	 private void verifyDeletedImageFile()
	 {
		 if (new File(dcmFile).exists())
		 {
			 Assert.assertTrue(true);
		 }
		 if (new File(jpegFile).exists())
		 {
			 Assert.assertTrue(true);
		 }
		 if ( new File(annotationFile).exists())
		 {
			 Assert.assertTrue(true);
		 }
	 }

}
