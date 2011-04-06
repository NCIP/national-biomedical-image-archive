package gov.nih.nci.nbia.deletion;

import java.io.FileWriter;
import java.io.PrintWriter;

public class ImageFileDeletionTestCaseSupportImpl implements
		ImageFileDeletionTestCaseSupport {

	public void checkDeletion() {
		// TODO Auto-generated method stub

	}

	public void createImageAndRelatedFileFile(String dcmFile, String jpegFile, String annotationFile) throws Exception{
			PrintWriter pw = new PrintWriter(new FileWriter(dcmFile));
			pw.println("Test");
			pw.close();

			PrintWriter pw1 = new PrintWriter(new FileWriter(jpegFile));
			pw1.println("Test");
			pw1.close();
			

			PrintWriter pw2 = new PrintWriter(new FileWriter(annotationFile));
			pw2.println("Test");
			pw2.close();
	}

}
