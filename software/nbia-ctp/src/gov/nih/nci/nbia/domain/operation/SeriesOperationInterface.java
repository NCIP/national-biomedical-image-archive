/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.internaldomain.GeneralEquipment;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;

import java.util.Map;

public interface SeriesOperationInterface {
	public Object validate(Map numbers) throws Exception;
	public void setEquip(GeneralEquipment equip);
	public void setPatient(Patient patient);
	public void setStudy(Study study);
	
}
