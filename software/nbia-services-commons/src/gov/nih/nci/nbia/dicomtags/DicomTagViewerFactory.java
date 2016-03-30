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
          return new LocalDicomTagViewer();		
	}	
}
