/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.internaldomain.Annotation;
import gov.nih.nci.nbia.internaldomain.GeneralSeries;

import java.util.Map;

public interface AnnotationOperationInterface {
	public Object validate(Map numbers) throws Exception;
	public void updateAnnotation(GeneralSeries series) throws Exception;
	public void insertOrReplaceAnnotation(Annotation annotation,
            String filename, 
            String studyInstanceUID) throws Exception;
	
	
}
