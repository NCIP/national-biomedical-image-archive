/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;

/**
 * Represents a DICOM image (retrieved through drill down).
 *
 * <P><b>WARNING!</b> This object is serialized so if you change it, you risk
 * breaking remote search and the grid interface
 *
 * <p>Note: this class has a natural ordering that is inconsistent with equals.
 */
public class ImageSearchResultImpl implements ImageSearchResult  {

	public ImageSearchResultImpl() {

	}

    /**
     * @return Returns the imagePkId.
     */
    public Integer getId() {
        return imagePkId;
    }


    /**
     * @param imagePkId The imagePkId to set.
     */
    public void setId(Integer imagePkId) {
        this.imagePkId = imagePkId;
    }


    /**
     * Sort this by the instance number within the series.
     *
     * <p>Instance numbers aren't mandatory and can be null.  A null
     * instance number will be push the image to the front.
     */
    public int compareTo(ImageSearchResult o) {
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
     * {@inheritDoc}
     */
    public Integer getSeriesId() {
        return seriesPkId;
    }


    /**
     * Set the series (pk) id for the series this image is in
     */
    public void setSeriesId(Integer seriesPkId) {
        this.seriesPkId = seriesPkId;
    }


    /**
     * {@inheritDoc}
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


    /**
     * {@inheritDoc}
     */
    public String getSeriesInstanceUid() {
    	return  this.seriesInstanceUid;
    }


    /**
     * Set the DICOM series instance UID for the series this image is in
     */
    public void setSeriesInstanceUid(String seriesInstanceUid) {
        this.seriesInstanceUid = seriesInstanceUid;
    }


    /**
     * {@inheritDoc}
     */
    public String getSopInstanceUid() {
    	return this.sopInstanceUid;
    }

    /**
     * Set the DICOM SOP instance UID
     */
    public void setSopInstanceUid(String sopInstanceUid) {
        this.sopInstanceUid = sopInstanceUid;
    }


    /**
     * {@inheritDoc}
     */
	public Long getSize() {
		return size;
	}

	/**
	 * Set the size of the image file in bytes.
	 * @param size
	 */
	public void setSize(Long size) {
		this.size = size;
	}

	/**
     * {@inheritDoc}
     */
	public NBIANode associatedLocation() {
		return location;
	}


    /**
     * {@inheritDoc}
     */
	public void associateLocation(NBIANode location) {
		this.location = location;
	}

    /**
     * {@inheritDoc}
     */
    public String getThumbnailURL() {
    	return thumbnailURL;
    }

	/**
     * Set the URL to retrieve the thumbnail for this image result.
     */
	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}
	
    public String getStudyInstanceUid() {
		return studyInstanceUid;
	}

	public void setStudyInstanceUid(String studyInstanceUid) {
		this.studyInstanceUid = studyInstanceUid;
	}
    ///////////////////////////////////////////////PRIVATE///////////////////////////////////




	protected Integer imagePkId;
    protected Integer seriesPkId;
    protected Integer instanceNumber;
    protected String sopInstanceUid;
    protected String seriesInstanceUid;
    protected String studyInstanceUid;
    protected Long size;
    protected NBIANode location;
    protected String thumbnailURL;
}