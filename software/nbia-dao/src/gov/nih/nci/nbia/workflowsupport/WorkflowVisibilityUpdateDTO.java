package gov.nih.nci.nbia.workflowsupport;
import gov.nih.nci.nbia.dto.WorkflowDTO;
public class WorkflowVisibilityUpdateDTO {
	private String newVisibility;
	private String seriesUID;
	private String patientUID;
	private WorkflowDTO wDTO;
	public WorkflowDTO getwDTO() {
		return wDTO;
	}

	public void setwDTO(WorkflowDTO wDTO) {
		this.wDTO = wDTO;
	}

	public String getNewVisibility() {
		return newVisibility;
	}

	public void setNewVisibility(String newVisibility) {
		this.newVisibility = newVisibility;
	}

	public String getSeriesUID() {
		return seriesUID;
	}

	public void setSeriesUID(String seriesUID) {
		this.seriesUID = seriesUID;
	}

	public String getPatientUID() {
		return patientUID;
	}

	public void setPatientUID(String patientUID) {
		this.patientUID = patientUID;
	}


}
