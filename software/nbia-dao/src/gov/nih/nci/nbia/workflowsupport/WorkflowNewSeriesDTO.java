package gov.nih.nci.nbia.workflowsupport;
import gov.nih.nci.nbia.dto.WorkflowDTO;
public class WorkflowNewSeriesDTO {
	private String seriesUID;
	private WorkflowDTO wDTO;
	public WorkflowDTO getwDTO() {
		return wDTO;
	}

	public void setwDTO(WorkflowDTO wDTO) {
		this.wDTO = wDTO;
	}

	public String getPatientUID() {
		return patientUID;
	}

	public void setPatientUID(String patientUID) {
		this.patientUID = patientUID;
	}

	private String patientUID;

	public String getSeriesUID() {
		return seriesUID;
	}

	public void setSeriesUID(String seriesUID) {
		this.seriesUID = seriesUID;
	}
}
