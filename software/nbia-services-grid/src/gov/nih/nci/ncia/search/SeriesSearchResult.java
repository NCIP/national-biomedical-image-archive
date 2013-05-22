/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;

import org.apache.log4j.Logger;
import java.io.Serializable;

/**
 * Represents a series search result (drill down from a patient search result)
 * 
 * <P>This object differs from the other "result" objects in that there is not
 * a series interface and implementation.  There was meant to be.... but couldn't
 * get the Axis serializer to behave properly with an array of these objects
 * as a member of another object (StudySearchResult) unless this was a concrete impl.
 * 
 * <P>Lines up with a SeriesDTO, but with extra stuff not found in db.
 * 
 * <p>Note: this class has a natural ordering that is inconsistent with equals.
 */
public class SeriesSearchResult implements Comparable<SeriesSearchResult>,
                                           Serializable {

	/**
	 * Return the number of DICOM images in this series.
	 */
	public Integer getNumberImages() {
        return numberImages;
    }

    public void setNumberImages(Integer numberImages) {
        this.numberImages = numberImages;
    }


    /**
     * Return the (DICOM) series number for this series (within a study).
     * 
     * <p>This can be null?
     */
    public String getSeriesNumber() {
        return seriesNumber;
    }

    
    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    
    /**
     * Return the name of the manufacturer of the equipment that this
     * series was generated on.
     * 
     * <p>This can be null?
     */    
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    
    /**
     * Return the modality for this series.
     * Example: CT
     */
    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }


    /**
     * Sort by series number is ascending order.
     * 
     * <P>If series number is null.... the nulls should end up at the front?
     */
    public int compareTo(SeriesSearchResult o) {
        if (this.getSeriesNumber() == null) {
            return -1;
        }

        if (o.getSeriesNumber() == null) {
            return 1;
        }  
        
        return this.getSeriesNumber().compareTo(o.getSeriesNumber());
    }

    
    /**
     * Return true if there are one or more annotations in this series.
     * False otherwise.
     */
    public boolean isAnnotated() {
        return annotationsFlag;
    }

    public void setAnnotated(boolean annotationsFlag) {
        this.annotationsFlag = annotationsFlag;
    }

    /**
     * Return the collection/project that this series is in.
     * Example: LIDC
     */
    public String getProject() {
        return this.project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    
    /**
     * Return the DICOM patient ID for the patient this series is in.
	 * Example: "1.3.4.x.y.z"
     */      
    public String getPatientId() {
        return this.patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    
    /**
     * Return the DICOM series instance UID for this series
	 * Example: "1.3.4.x.y.z"
     */    
    public String getSeriesInstanceUid() {
        return this.seriesUID;
    }

    public void setSeriesInstanceUid(String seriesInstanceUID) {
        this.seriesUID = seriesInstanceUID;

    }

    
    /**
     * Return the DICOM study instance UID for the study this
     * series is in.
	 * Example: "1.3.4.x.y.z"
     */
    public String getStudyInstanceUid() {
        return this.studyId;
    }

    public void setStudyInstanceUid(String studyInstanceUID) {
        this.studyId = studyInstanceUID;
    }

    
    /**
     * Return the unique (pk) id for the study that this series is in.
     */
    public Integer getStudyId() {
        return this.studyPkId;
    }

    public void setStudyId(Integer studyPKId) {
        this.studyPkId = studyPKId;
    }

    /**
     * Return the total size of all DICOM image files in this series in BYTES.
     * (Excludes annotations).
     */
    public Long getTotalSizeForAllImagesInSeries() {
        return totalSizeForAllImagesInSeries;
    }


    public void setTotalSizeForAllImagesInSeries(Long totalSizeForAllImagesInSeries) {
        this.totalSizeForAllImagesInSeries = totalSizeForAllImagesInSeries;
    }

    
	/**
	 * This should identify a series uniquely at a given node.
	 * 
	 * <P>Likely implementation is a pkid from the database.
	 */    
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer seriesPKId) {
        this.id = seriesPKId;
    }


    /**
     * Computes the exact size (in bytes) of all images and annotations in this series
     * 
     * <P>If the image size and annotation size isn't set, this will return 0
     */
    public Long computeExactSize() {
        Long exactSize =  totalSizeForAllImagesInSeries + annotationsSize;
        logger.debug("getExactSize()=" + exactSize);
        return exactSize;
    }

    
    /**
     * Return the size of all annotation files in this series in BYTES.
     */
    public Long getAnnotationsSize() {
        return annotationsSize;
    }

    public void setAnnotationsSize(Long annotationsSize) {
        this.annotationsSize = annotationsSize;
    }

    
    /**
     * Return the series description,
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    /**
     * The node that this series was found on.
     * 
	 * <p>This is intentionally not a property to avoid serialization.
     */
	public NBIANode associatedLocation() {
		return location;
	}

	
	/**
	 * Associate a node with this result.  This should only be called once
	 * by a result generator.
	 * 
	 * <p>This is intentionally not a property to avoid serialization.
	 */
	public void associateLocation(NBIANode location) {
		this.location = location;
	}    
    
    ////////////////////////////////////PRIVATE//////////////////////////////////////
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
    private Long totalSizeForAllImagesInSeries = 0L;
    private String project;
    private String description;
    private static Logger logger = Logger.getLogger(SeriesSearchResult.class);
    private NBIANode location;
}