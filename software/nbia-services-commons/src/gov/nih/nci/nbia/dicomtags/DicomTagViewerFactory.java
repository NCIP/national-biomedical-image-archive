/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dicomtags;


public class DicomTagViewerFactory {
	public static DicomTagViewer getDicomTagViewer() {
		if(instance==null) {
			instance = createDicomTagViewer();
		}
		return instance;
	}
	
	/////////////////////////////////////PRIVATE////////////////////////////////////
	
	private static DicomTagViewer instance;
	
	
	private static DicomTagViewer createDicomTagViewer() {
		String dicomTagViewerClassName = System.getProperty("dicomTagViewer.className");

		if(dicomTagViewerClassName==null) {
			throw new RuntimeException("dicomTagViewer.className must be defined in system properties");
		}
		else {
			try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Class clazz = Class.forName(dicomTagViewerClassName, false, loader);
				DicomTagViewer dicomTagViewer =  (DicomTagViewer)clazz.newInstance();
				return dicomTagViewer;
			}
			catch(Exception ex) {
				throw new RuntimeException(ex);
			}
		}			
	}	
}
