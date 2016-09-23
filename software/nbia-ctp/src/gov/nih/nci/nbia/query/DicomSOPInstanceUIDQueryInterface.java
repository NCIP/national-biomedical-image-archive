/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.query;

import java.util.Map;
import java.util.Set;

import org.rsna.ctp.stdstages.database.UIDResult;

public interface DicomSOPInstanceUIDQueryInterface {
	public void setDicomSOPInstanceUIDQuery(Set<String> sopUid);
	public Map<String, UIDResult> getUIDResult();
	
}
