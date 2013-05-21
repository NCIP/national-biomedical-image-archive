package gov.nih.nci.nbia.dto;

public class ImageFileDTO   {

    public ImageFileDTO(String filePath, Long size, String sopInstanceUid) {
    	this.sopInstanceUid = sopInstanceUid;
    	this.size = size;
    	this.filePath = filePath;
    }


    public String getSopInstanceUid() {
    	return this.sopInstanceUid;
    }


	public String getFileURI() {
		return filePath;
	}


	public Long getSize() {
		return size;
	}
	
	
	///////////////////////////////////////PRIVATE//////////////////////////////////
    private String sopInstanceUid;
    private Long size; // in MB
    private String filePath;	
}