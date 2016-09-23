package gov.nih.nci.nbia.dicomapi;
import java.util.*;

public class DICOMSupportDTO {

	private String filePath;
	private String fileName;
	private String fileSize;
	private String fileHash;
	private String errors;
    private Hashtable <String, String> fieldMap;
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileHash() {
		return fileHash;
	}
	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}
	public String getErrors() {
		return errors;
	}
	public void setErrors(String errors) {
		this.errors = errors;
	}
		
	public Hashtable <String, String> getFieldMap() {
		return fieldMap;
	}
	public void setFieldMap(Hashtable <String, String>  fieldMap) {
		this.fieldMap = fieldMap;
	}
	@Override
	public String toString() {
		return "DICOMSupportDTO [filePath=" + filePath + ", fileName="
				+ fileName + ", fileSize=" + fileSize + ", fileHash="
				+ fileHash + ", errors=" + errors + "]";
	}
	
}
