/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;
import gov.nih.nci.nbia.internaldomain.TrialDataProvenance;

import java.util.Map;

public interface GeneralImageOperationInterface {
	   public Object validate(Map numbers) throws Exception;
	   public void setDataProvenance(TrialDataProvenance dataProvenance) ;
	   public void setPatient(Patient patient);
	   public void setSeries(GeneralSeries series);
	   public void setStudy(Study study);
	   public boolean isReplacement();
}
