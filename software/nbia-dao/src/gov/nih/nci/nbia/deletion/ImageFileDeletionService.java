/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion;

import java.util.List;
import java.util.Map;
/**
 * This interface defines all necessary service for image file deletion.
 * @author zhoujim
 *
 */
public interface ImageFileDeletionService {
	/**
	 * Remove all image files and annotation files
	 * @param files
	 */
	public void removeImageFiles(Map<String, List<String>> files);
	/**
	 * Physically remove all specified images files 
	 * @param fileNames
	 */
	public void removeImageFiles(List<String> fileNames);
	/**
	 * Remove all related jpeg files.
	 * @param fileNames
	 */
	public void removeRelatedFile(List<String> fileNames);
	/**
	 * remove all image files which are submitted by MIRC
	 * @param dir
	 * @param name
	 */
	public void removeMIRCRelatedFile(String dir, String name);
	
	/**
	 * Physically remove all specified annotation files
	 * @param fileNames
	 */
	public void removeAnnotationFiles(List<String> fileNames);
}
