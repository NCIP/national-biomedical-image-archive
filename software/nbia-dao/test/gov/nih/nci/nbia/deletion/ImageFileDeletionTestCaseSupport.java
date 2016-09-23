/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion;

public interface ImageFileDeletionTestCaseSupport {

	public void createImageAndRelatedFileFile(String s1, String s2, String s3)throws Exception;
	public void checkDeletion() throws Exception;
}
