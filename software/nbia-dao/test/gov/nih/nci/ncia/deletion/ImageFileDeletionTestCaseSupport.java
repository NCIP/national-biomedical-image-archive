package gov.nih.nci.ncia.deletion;

public interface ImageFileDeletionTestCaseSupport {

	public void createImageAndRelatedFileFile(String s1, String s2, String s3)throws Exception;
	public void checkDeletion() throws Exception;
}
