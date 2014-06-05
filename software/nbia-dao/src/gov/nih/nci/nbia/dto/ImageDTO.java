/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.7  2007/12/28 21:17:05  bauerd
* *** empty log message ***
*
* Revision 1.6  2007/11/13 22:28:23  lethai
* Added fix for tracker id 9923
*
* Revision 1.5  2007/10/15 17:27:56  bauerd
* *** empty log message ***
*
* Revision 1.4  2007/10/01 12:24:58  bauerd
* *** empty log message ***
*
* Revision 1.3  2007/09/27 23:30:39  bauerd
* This is the checked in seperation of dependent classes betweeen the grid and the commons module...
*
* Revision 1.2  2007/08/14 14:04:29  mccrimmons
* modified to remove UI components that determined databasket state
*
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.29  2006/11/27 22:09:27  panq
* Get register ID from Application Factories.
*
* Revision 1.28  2006/11/27 18:06:28  shinohaa
* extracted constant
*
* Revision 1.27  2006/11/27 16:43:37  panq
* Modified for getting thumbnails from the grid.
*
* Revision 1.26  2006/11/21 16:36:14  shinohaa
* data basket grid functionality
*
* Revision 1.25  2006/11/15 15:40:06  shinohaa
* grid prototype
*
* Revision 1.24  2006/11/10 13:58:15  shinohaa
* grid prototype
*
* Revision 1.23  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.dto;

import java.util.Date;



public class ImageDTO implements Comparable<ImageDTO>  {


    private Integer imagePkId;
    private Date contentDate;
    private String contentTime;
    private Integer seriesPkId;
    private Integer instanceNumber;
    private String sopInstanceUid;
    private String seriesInstanceUid;
    private String studyInstanceUid;
    private Long size; // in MB
    private String fileURI;
    private String project;
    private String siteName;
    private int frameNum;


    /**
     * @return Returns the imagePkId.
     */
    public Integer getImagePkId() {
        return imagePkId;
    }


    /**
     * @param imagePkId The imagePkId to set.
     */
    public void setImagePkId(Integer imagePkId) {
        this.imagePkId = imagePkId;
    }


    /**
     *
     * @return the image's content date
     */
    public Date getContentDate() {
        return contentDate;
    }


    /**
     * Setter
     *
     * @param contentDate
     */
    public void setContentDate(Date contentDate) {
        this.contentDate = contentDate;
    }


    /**
     *
     * @return content time of the image
     */
    public String getContentTime() {
        return contentTime;
    }


    /**
     * Setter
     *
     * @param contentTime
     */
    public void setContentTime(String contentTime) {
        this.contentTime = contentTime;
    }


    /**
     * Used to sort.
     *
     * @param o
     */
    public int compareTo(ImageDTO o) {
        Integer instanceNumber = this.getInstanceNumber();
        Integer otherInstanceNumber = o.getInstanceNumber();

        if (instanceNumber == null) {
            return -1;
        }

        if (otherInstanceNumber == null) {
            return 1;
        }

        return instanceNumber.compareTo(otherInstanceNumber);
    }


    /**
     *
     *  @return series PK ID of the series the image belongs to
     */
    public Integer getSeriesPkId() {
        return seriesPkId;
    }


    public void setSeriesPkId(Integer seriesPkId) {
        this.seriesPkId = seriesPkId;
    }


    /**
     *  Getter for instance number
     */
    public Integer getInstanceNumber() {
        return instanceNumber;
    }


    /**
     * Setter for instance number
     *
     * @param instanceNumber
     */
    public void setInstanceNumber(Integer instanceNumber) {
        this.instanceNumber = instanceNumber;
    }


    public String getSeriesInstanceUid() {
    	return  this.seriesInstanceUid;
    }


    public void setSeriesInstanceUid(String seriesInstanceUid) {
        this.seriesInstanceUid = seriesInstanceUid;
    }


    public String getSopInstanceUid() {
    	return this.sopInstanceUid;
    }


    public void setSopInstanceUid(String sopInstanceUid) {
        this.sopInstanceUid = sopInstanceUid;
    }


	public void setFileURI(String fileName) {
		this.fileURI = fileName;
	}


	public String getFileURI() {
		return fileURI;
	}


	public Long getSize() {
		return size;
	}


	public void setSize(Long size) {
		this.size = size;
	}


	public String getProject() {
		return project;
	}


	public void setProject(String project) {
		this.project = project;
	}


	public String getSiteName() {
		return siteName;
	}


	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public int getFrameNum() {
		return frameNum;
	}


	public void setFrameNum(int frameNum) {
		this.frameNum = frameNum;
	}


	public String getStudyInstanceUid() {
		return studyInstanceUid;
	}


	public void setStudyInstanceUid(String studyInstanceUid) {
		this.studyInstanceUid = studyInstanceUid;
	}
	
}