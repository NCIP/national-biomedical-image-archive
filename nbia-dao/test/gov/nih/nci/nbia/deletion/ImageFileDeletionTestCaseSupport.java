package gov.nih.nci.nbia.deletion;

public interface ImageFileDeletionTestCaseSupport {

	public void createImageAndRelatedFileFile(String s1, String s2, String s3)throws Exception;
	public void checkDeletion() throws Exception;
}
