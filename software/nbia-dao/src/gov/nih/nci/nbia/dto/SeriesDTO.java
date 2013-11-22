/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

import gov.nih.nci.nbia.util.UidDisplayUtil;

import org.apache.log4j.Logger;

/**
 * Represents a series for data transfer purposes
 *
 * @author dietrichj
 */
public class SeriesDTO implements Comparable<SeriesDTO>  {
    private static Logger logger = Logger.getLogger(SeriesDTO.class);


    private String seriesNumber;
    private String seriesUID;
    private Integer numberImages;
    private String modality;
    private String manufacturer;
    private Integer id;
    private boolean annotationsFlag;
    private Long annotationsSize = 0L;
    private String patientId;
    private String studyId;
    private Integer studyPkId;
    private Long totalSizeForAllImagesInSeries;
    private String project;
    private String description;
    private String dataProvenanceSiteName;
    private String manufacturerModelName;
    private String softwareVersion;
    private String maxFrameCount;

    public Integer getNumberImages() {
        return numberImages;
    }

    public void setNumberImages(Integer numberImages) {
        this.numberImages = numberImages;
    }

    public void setTotalImagesInSeries(Integer totalImagesInSeries) {
        this.numberImages = totalImagesInSeries;
    }


    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getSeriesUID() {
        return UidDisplayUtil.getDisplayUid(seriesUID);
    }

    public void setSeriesUID(String seriesUID) {
        this.seriesUID = seriesUID;
    }

    /**
     * Used to sort.
     *
     * @param o
     */
    public int compareTo(SeriesDTO o) {
        // Series should be ordered according to series number
        if ((getSeriesNumber() != null) && (o.getSeriesNumber() != null)) {
            return this.getSeriesNumber().compareTo(o.getSeriesNumber());
        } else {
            return 0;
        }
    }

    public boolean isAnnotationsFlag() {
        return annotationsFlag;
    }

    public void setAnnotationsFlag(boolean annotationsFlag) {
        this.annotationsFlag = annotationsFlag;
    }

    public String getProject() {
        return this.project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getPatientId() {
        return this.patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getStudyId() {
        return this.studyId;
    }

    public void setStudyId(String studyInstanceUID) {
        this.studyId = studyInstanceUID;

    }

    public Integer getStudyPkId() {
        return this.studyPkId;
    }

    public void setStudyPkId(Integer studyPKId) {
        this.studyPkId = studyPKId;
    }

    /**
     * 
     */
    public Long getTotalSizeForAllImagesInSeries() {
        return totalSizeForAllImagesInSeries;
    }

    /**
     * @param totalSizeForAllImagesInSeries
     */
    public void setTotalSizeForAllImagesInSeries(Long totalSizeForAllImagesInSeries) {
        this.totalSizeForAllImagesInSeries = totalSizeForAllImagesInSeries;
    }

    public String getSeriesId() {
        return this.seriesUID;
    }

    public void setSeriesId(String seriesInstanceUID) {
        this.seriesUID = seriesInstanceUID;
    }

    public Integer getSeriesPkId() {
        return this.id;
    }

    public void setSeriesPkId(Integer seriesPKId) {
        this.id = seriesPKId;
    }

    /**
     * Computes the exact size (in bytes) of all images in this series
     */
    public Long getExactSize() {
        Long totalInBytes = new Long(0L);


        // No images in list, so use the total for the series
        if (totalSizeForAllImagesInSeries != null) {
            totalInBytes = totalSizeForAllImagesInSeries;
        }

        logger.debug("totalInBytes: " + ( ( totalInBytes == null ) ? "NULL!" : totalInBytes.toString() ) );
        logger.debug("annotationsSize: " + ( ( annotationsSize == null ) ? "NULL!" : annotationsSize.toString() ) );
        logger.debug("Will I thorw a Null pointer here?");
        logger.debug("getExactSize()=" + (totalInBytes + annotationsSize));
        // Add annotations size also
        return totalInBytes + annotationsSize;
    }

    public Long getAnnotationsSize() {
        return annotationsSize;
    }

    public void setAnnotationsSize(Long annotationsSize) {
        this.annotationsSize = annotationsSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getDataProvenanceSiteName() {
		return dataProvenanceSiteName;
	}

	public void setDataProvenanceSiteName(String dataProvenanceSiteName) {
		this.dataProvenanceSiteName = dataProvenanceSiteName;
	}

	public String getManufacturerModelName() {
		return manufacturerModelName;
	}

	public void setManufacturerModelName(String manufacturerModelName) {
		this.manufacturerModelName = manufacturerModelName;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getMaxFrameCount() {
		return maxFrameCount;
	}

	public void setMaxFrameCount(String maxFrameCount) {
		this.maxFrameCount = maxFrameCount;
	}
	
}

