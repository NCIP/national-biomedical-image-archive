package gov.nih.nci.nbia.query;

import java.util.Map;
import java.util.Set;

import org.rsna.ctp.stdstages.database.UIDResult;

public interface DicomSOPInstanceUIDQueryInterface {
	public void setDicomSOPInstanceUIDQuery(Set<String> sopUid);
	public Map<String, UIDResult> getUIDResult();
	
}
