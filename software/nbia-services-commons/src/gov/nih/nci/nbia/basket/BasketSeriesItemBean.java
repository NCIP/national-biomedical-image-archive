/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.basket;

import gov.nih.nci.nbia.util.HashCodeUtil;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.text.DecimalFormat;

/**
 *
 * @author piparom
 */
public class BasketSeriesItemBean implements Comparable<BasketSeriesItemBean> {

    /**
     *
     */
    public BasketSeriesItemBean() {
    }

    /**
     *
     */
    public BasketSeriesItemBean(SeriesSearchResult seriesSearchResult) {
        this.seriesSearchResult = seriesSearchResult;
    }

    /**
     * Total number of images selected
     */
    public Integer getTotalImagesSelectedFromSeries() {
        return  totalImagesInSeries;
    }

    /**
     * Computes the exact size (in bytes) of all images in this series
     */
    public Long getExactSize() {
        Long totalInBytes = totalSizeForAllImagesInSeries;

        // Add annotations size also
        //System.out.println("totalSizeForAllImagesInSeries:"+totalSizeForAllImagesInSeries);
        //System.out.println("totalInBytes:"+totalInBytes);
        //System.out.println("annotationsSize:"+annotationsSize);

        return totalInBytes + annotationsSize;
    }

    /**
     * Gets the size in megabytes with one decimal point (used by UI)
     */
    public String getSize() {
        DecimalFormat nf = (DecimalFormat) DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(1);
        nf.setMinimumFractionDigits(1);

        return nf.format(getExactSize() / 1000000.0);
    }

    /**
     * Gets the Image size in megabytes with one decimal point (used by UI)
     */
    public String getImageSize() {
        DecimalFormat nf = (DecimalFormat) DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(1);
        nf.setMinimumFractionDigits(1);

        return nf.format((getExactSize()- getAnnotationsSize()) / 1000000.0);
    }

    /**
     * Gets the Annotation size in megabytes with three decimal point (used by UI)
     */
    public String getAnnotationSize3Digits() {
        if (getAnnotationsSize()== 0) {
            return "N/A";
        }

        DecimalFormat nf = (DecimalFormat) DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        nf.setMinimumFractionDigits(1);

        return nf.format(getAnnotationsSize() / 1000000.0);
    }


    /**
     * Use to hold checkboxes on the data basket page
     */
    public void setSelected(boolean v) {
        selected = v;
    }

    /**
     *
     */
    public boolean isSelected() {
        return selected;
    }


    /**
     *
     * @param id
     */
    public void setPatientId(String id) {
        patientId = id;
    }

    /**
     *
     */
    public String getPatientId() {
    	return patientId;
    }


    /**
     *
     * @param id
     */
    public void setStudyId(String id) {
        studyId = id;
    }

    /**
     *
     */
    public String getStudyId() {
        return studyId;
    }

    /**
     *
     * @param id
     */
    public void setSeriesId(String id) {
        seriesId = id;
    }

    /**
     *
     */
    public String getSeriesId() {
        return seriesId;
    }

    /**
     *
     * @param id
     */
    public void setSeriesPkId(Integer id) {
        seriesPkId = id;
    }

    /**
     *
     */
    public Integer getSeriesPkId() {
        return seriesPkId;
    }

    /**
     *
     * @param t
     */
    public void setTotalImagesInSeries(Integer t) {
        totalImagesInSeries = t;
    }

    /**
     *
     */
    public Integer getTotalImagesInSeries() {
        return totalImagesInSeries;
    }

    /**
     *
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BasketSeriesItemBean)) {
            return false;
        }

        final BasketSeriesItemBean entity = (BasketSeriesItemBean) o;

        if (!compareId(seriesSearchResult.getId(),entity.getSeriesSearchResult().getId())) {
            return false;
        }

        if (!compareId(this.getSeriesId(),entity.getSeriesId())) {
            return false;
        }

        if (!compareId(this.getPatientId(),entity.getPatientId())) {
            return false;
        }

        if (!compareId(this.getStudyId(),entity.getStudyId())) {
            return false;
        }

        return true;
    }

    private static boolean compareId(Object id1, Object id2) {
    	return (id1 != null) ? !id1.equals(id2) : (id2 != null);
    }
    /**
     *
     */
    public int hashCode() {
        int result = HashCodeUtil.SEED;
        result = HashCodeUtil.hash(result, this.seriesSearchResult.getId());
        result = HashCodeUtil.hash(result, this.getSeriesId());
        result = HashCodeUtil.hash(result, this.getPatientId());
        result = HashCodeUtil.hash(result, this.getStudyId());

        return result;
    }

    /**
     *
     * @param o
     */
    public int compareTo(BasketSeriesItemBean o) {
        int patientComparisonResult = this.getPatientId()
                                          .compareTo(o.getPatientId());
        int studyComparisonResult = this.getStudyId().compareTo(o.getStudyId());
        int seriesComparisonResult = this.getSeriesId()
                                         .compareTo(o.getSeriesId());

        // Sort by patient ID first, then study, then series ID
        if (patientComparisonResult != 0) {
            return patientComparisonResult;
        } else {
            return (studyComparisonResult != 0) ? studyComparisonResult
                                                : seriesComparisonResult;
        }
    }

    /**
     *
     */
    public String getProject() {
        return project;
    }

    /**
     *
     * @param project
     */
    public void setProject(String project) {
        this.project = project;
    }

    /**
     *
     */
    public Integer getStudyPkId() {
        return studyPkId;
    }

    /**
     *
     * @param studyPkId
     */
    public void setStudyPkId(Integer studyPkId) {
        this.studyPkId = studyPkId;
    }

    /**
     *
     */
    public Long getTotalSizeForAllImagesInSeries() {
        return totalSizeForAllImagesInSeries;
    }

    /**
     *
     * @param totalSizeForAllImagesInSeries
     */
    public void setTotalSizeForAllImagesInSeries(Long totalSizeForAllImagesInSeries) {
        this.totalSizeForAllImagesInSeries = totalSizeForAllImagesInSeries;
    }

    public boolean isAnnotationsFlag() {
        return annotationsFlag;
    }

    public void setAnnotationsFlag(boolean annotationsFlag) {
        this.annotationsFlag = annotationsFlag;
    }

    public Long getAnnotationsSize() {
        return annotationsSize;
    }

    public void setAnnotationsSize(Long annotationsSize) {
        this.annotationsSize = annotationsSize;
    }

    public String getAnnotated() {
        if (annotationsFlag) {
            return "Yes";
        } else {
            return "No";
        }
    }


    public String getGridLocation() {
        return gridLocation;
    }

    public void setGridLocation(String gridLocation) {
        this.gridLocation = gridLocation;
    }

	public SeriesSearchResult getSeriesSearchResult() {
		return seriesSearchResult;
	}

	public String getLocationDisplayName() {
		return locationDisplayName;
	}

	public void setLocationDisplayName(String locationDisplayName) {
		this.locationDisplayName = locationDisplayName;
	}

	////////////////////////////////////////PRIVATE////////////////////////////////////

    // Data fields
    private String patientId;
    private String studyId;
    private String seriesId;
    private Integer seriesPkId;
    private SeriesSearchResult seriesSearchResult;
    private Integer studyPkId;
    private Long totalSizeForAllImagesInSeries;
    private String project;
    private boolean annotationsFlag;
    private Long annotationsSize = 0L;
    private String gridLocation;

    private String locationDisplayName;

    // Used by the UI to indicate if this has been selected for deletion
    private boolean selected = false;

    // The total number of images in the series (not neccesarily the total number selected)
    private Integer totalImagesInSeries;
    
    private String seriesDescription;
    private String studyDescription;
    private String studyDate;
    
	public String getStudyDescription() {
		return studyDescription;
	}

	public void setStudyDescription(String studyDescription) {
		this.studyDescription = studyDescription;
	}

	public String getStudyDate() {
		return studyDate;
	}

	public void setStudyDate(String studyDate) {
		this.studyDate = studyDate;
	}

	public String getSeriesDescription() {
		return seriesDescription;
	}

	public void setSeriesDescription(String seriesDescription) {
		this.seriesDescription = seriesDescription;
	}
}
