/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;
/**
 * Image File Deletion Service Implementation
 * @author zhoujim
 *
 */
public class ImageFileDeletionServiceImpl implements ImageFileDeletionService {
	
	public void removeImageFiles(Map<String, List<String>> files)
	{
		List<String> dicomFilePath = files.get("dicom");
		if (dicomFilePath != null && dicomFilePath.size() > 0)
		{
			removeImageFiles(dicomFilePath);
		}
		List<String> annotationFilePath = files.get("annotation");
		if (annotationFilePath != null && annotationFilePath.size() > 0)
		{
			removeAnnotationFiles(annotationFilePath);
		}
	}
	
	public void removeImageFiles(List<String> fileNames)
	{
		for (String path : fileNames)
		{
			File file = new File(path);
			file.delete();
		}
		removeRelatedFile(fileNames);
	}
	/**
	 * Remove all thumb nail images
	 * @param fileNames
	 */
	public void removeRelatedFile(List<String> fileNames)
	{
		for (String name : fileNames)
		{
			File f = new File(name);
			String dir = f.getParent();
			if (dir == null) // avoid null point exception
			{
				dir = ".";
			}
			String fileName = f.getName();
			File directory = new File(dir);
			ThumbNailFileFilter  tnff = new ThumbNailFileFilter();
			tnff.setPattern(fileName+"[");
			String[] filterName = directory.list(tnff);
			//Not CTP upload files, it is MIRC uploaded Files
			if (filterName == null || filterName.length <= 0)
			{
				removeMIRCRelatedFile(dir, fileName);
			}
			//remove CTP uploaded files
			else
			{
				for (String s : filterName)
				{
					File deleteFile = new File(dir+File.separator+s);
					if (deleteFile != null)
					{
						deleteFile.delete();
					}
				}
			}
		}
	}
	
	public void removeMIRCRelatedFile(String dir, String name)
	{
		File directory = new File(dir);
		String fileNameWithoutExt = name.substring(0, name.lastIndexOf('.'));
		ThumbNailFileFilter  tnff = new ThumbNailFileFilter();
		tnff.setPattern(fileNameWithoutExt+"_");

		String[] filterName = directory.list(tnff);
		if (filterName == null)
		{
			return;
		}
		for (String s : filterName)
		{
			System.out.println("file: " + dir + "/"+s);
			File deleteFile = new File(dir + File.separator + s);
			if (deleteFile != null)
			{
				deleteFile.delete();
			}
		}
	}
	
	/**  
	 * find the corrected Image JPEG file
	 * @author zhoujim
	 *
	 */
	public class ThumbNailFileFilter implements FilenameFilter
	{
		String pattern = "";

		public void setPattern(String str)
		{
			pattern = str;
		}
		public boolean accept(File dir, String name)
		{
			return name.startsWith(pattern);
		}
	}
	
	public void removeAnnotationFiles(List<String> fileNames)
	{
		for (String path : fileNames)
		{
			File file = new File(path);
			file.delete();
		}
	}
}
