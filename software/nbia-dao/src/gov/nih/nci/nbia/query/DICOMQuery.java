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
* Revision 1.2  2007/08/15 20:02:32  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/07 12:05:22  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.39  2007/01/06 06:36:29  zhouro
* added new search criteria AcquisitionMatrix, ReconstructionDiameter, DataCollectionDiameter(CT) and DataCollectionDiameter(DX)
*
* Revision 1.38  2006/12/15 18:09:10  dietrich
* Grid enhancement to display nodes being searched
*
* Revision 1.37  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.36  2006/12/06 22:18:13  zhouro
* enhancement on annotation option
*
* Revision 1.35  2006/11/27 18:10:20  shinohaa
* grid functionality
*
* Revision 1.34  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Jul 27, 2005
 *
 *
 *
 */
package gov.nih.nci.nbia.query;

import gov.nih.nci.ncia.criteria.AcquisitionMatrixCriteria;
import gov.nih.nci.ncia.criteria.AnatomicalSiteCriteria;
import gov.nih.nci.ncia.criteria.AnnotationOptionCriteria;
import gov.nih.nci.ncia.criteria.AuthorizationCriteria;
import gov.nih.nci.ncia.criteria.CollectionCriteria;
import gov.nih.nci.ncia.criteria.ColorModeOptionCriteria;
import gov.nih.nci.ncia.criteria.ContrastAgentCriteria;
import gov.nih.nci.ncia.criteria.ConvolutionKernelCriteria;
import gov.nih.nci.ncia.criteria.Criteria;
import gov.nih.nci.ncia.criteria.CurationStatusDateCriteria;
import gov.nih.nci.ncia.criteria.DataCollectionDiameterCriteria;
import gov.nih.nci.ncia.criteria.DateRangeCriteria;
import gov.nih.nci.ncia.criteria.DxDataCollectionDiameterCriteria;
import gov.nih.nci.ncia.criteria.ImageModalityCriteria;
import gov.nih.nci.ncia.criteria.ImageSliceThickness;
import gov.nih.nci.ncia.criteria.ImagingObservationCharacteristicCodeMeaningCriteria;
import gov.nih.nci.ncia.criteria.ImagingObservationCharacteristicCodeValuePairCriteria;
import gov.nih.nci.ncia.criteria.ImagingObservationCharacteristicQuantificationCriteria;
import gov.nih.nci.ncia.criteria.KilovoltagePeakDistribution;
import gov.nih.nci.ncia.criteria.ManufacturerCriteria;
import gov.nih.nci.ncia.criteria.MinNumberOfStudiesCriteria;
import gov.nih.nci.ncia.criteria.ModalityAndedSearchCriteria;
import gov.nih.nci.ncia.criteria.ModelCriteria;
import gov.nih.nci.ncia.criteria.NumFrameOptionCriteria;
import gov.nih.nci.ncia.criteria.NumOfMonthsCriteria;
import gov.nih.nci.ncia.criteria.PatientCriteria;
import gov.nih.nci.ncia.criteria.PersistentCriteria;
import gov.nih.nci.ncia.criteria.ReconstructionDiameterCriteria;
import gov.nih.nci.ncia.criteria.SeriesDescriptionCriteria;
import gov.nih.nci.ncia.criteria.SoftwareVersionCriteria;
import gov.nih.nci.ncia.criteria.UrlParamCriteria;
import gov.nih.nci.ncia.criteria.UsMultiModalityCriteria;
import gov.nih.nci.nbia.util.CriteriaComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * This bundles up all the criteria selected from the simple and advanced
 * "classic" search.
 *
 * <p>If adding a new criteria, be sure to add setCriteria(xCriteria) so that
 * the grid service can properly set criteria.  The grid service uses reflection
 * to do this.
 *
 * @author Prashant Shah, Ajay - NCICB/SAIC
 */
public class DICOMQuery extends Query {
    private ImageModalityCriteria imageModalityCriteria;
    private UsMultiModalityCriteria usMultiModalityCriteria;
    private KilovoltagePeakDistribution kilovoltagePeakDistribution;
    private ReconstructionDiameterCriteria reconstructionDiameterCriteria;
    private AcquisitionMatrixCriteria acquisitionMatrixCriteria;
    private DataCollectionDiameterCriteria dataCollectionDiameterCriteria;
    private DxDataCollectionDiameterCriteria dxDataCollectionDiameterCriteria;
    private ContrastAgentCriteria contrastAgentCriteria;
    private NumFrameOptionCriteria numFrameOptionCriteria;
    private ColorModeOptionCriteria colorModeOptionCriteria;
    private AnnotationOptionCriteria annotationOptionCriteria;
    private AnatomicalSiteCriteria anatomicalSiteCriteria;
    private MinNumberOfStudiesCriteria minNumberOfStudiesCriteria;
    private NumOfMonthsCriteria numOfMonthsCriteria;
    private CollectionCriteria collectionCriteria;
    private ConvolutionKernelCriteria convolutionKernelCriteria;
    private SoftwareVersionCriteria softwareVersionCriteria;
    private SeriesDescriptionCriteria seriesDescriptionCriteria;
    private ImageSliceThickness imageSliceThickness;
    private PatientCriteria patientCriteria;
    private ManufacturerCriteria manufacturerCriteria;
    private ModelCriteria modelCriteria;
    private CurationStatusDateCriteria curationStatusDateCriteria;
    private AuthorizationCriteria authorizationCriteria;
    private List<UrlParamCriteria> urlParamCriteria;
    private ModalityAndedSearchCriteria modalityAndedSearchCriteria;
    private DateRangeCriteria dateRangeCriteria;
    private boolean queryFromUrl = false;
    private ImagingObservationCharacteristicCodeMeaningCriteria imagingObservationCharacteristicCodeMeaningCriteria;
    private ImagingObservationCharacteristicCodeValuePairCriteria imagingObservationCharacteristicCodeValuePairCriteria;
    private ImagingObservationCharacteristicQuantificationCriteria imagingObservationCharacteristicQuantificationCriteria;

    // The number of milliseconds it took to run this query
    private long elapsedTimeInMillis;

    // List of all criteria that is part of the query
    private List<Criteria> criteriaList;

    // Primary key of the saved query that was run for this query
    private long savedQueryId = 0;

    // Primay key of the query history record that was created as a result
    // of running this query
    private long queryHistoryId = 0;
    private String queryName;

    /**
     *
     */
    public DICOMQuery() {
        super();
        criteriaList = new ArrayList<Criteria>();
    }

    public ImagingObservationCharacteristicQuantificationCriteria getImagingObservationCharacteristicQuantificationCriteria() {
        return imagingObservationCharacteristicQuantificationCriteria;
    }

    public void setCriteria(ImagingObservationCharacteristicQuantificationCriteria imagingObservationCharacteristicQuantificationCriteria) {
        if (imagingObservationCharacteristicQuantificationCriteria != null) {
            criteriaList.add(imagingObservationCharacteristicQuantificationCriteria);
            this.imagingObservationCharacteristicQuantificationCriteria = imagingObservationCharacteristicQuantificationCriteria;
        }
    }


    public ImagingObservationCharacteristicCodeMeaningCriteria getImagingObservationCharacteristicCodeMeaningCriteria() {
        return imagingObservationCharacteristicCodeMeaningCriteria;
    }

    public void setCriteria(ImagingObservationCharacteristicCodeMeaningCriteria imagingObservationCharacteristicCodeMeaningCriteria) {
        if (imagingObservationCharacteristicCodeMeaningCriteria != null) {
            criteriaList.add(imagingObservationCharacteristicCodeMeaningCriteria);
            this.imagingObservationCharacteristicCodeMeaningCriteria = imagingObservationCharacteristicCodeMeaningCriteria;
        }
    }

    public ImagingObservationCharacteristicCodeValuePairCriteria getImagingObservationCharacteristicCodeValuePairCriteria() {
        return imagingObservationCharacteristicCodeValuePairCriteria;
    }

    public void setCriteria(ImagingObservationCharacteristicCodeValuePairCriteria imagingObservationCharacteristicCodeValuePairCriteria) {
        if (imagingObservationCharacteristicCodeValuePairCriteria != null) {
            criteriaList.add(imagingObservationCharacteristicCodeValuePairCriteria);
            this.imagingObservationCharacteristicCodeValuePairCriteria = imagingObservationCharacteristicCodeValuePairCriteria;
        }
    }

    /**
     * @return Returns the getSoftwareVersionC
     */
    public SeriesDescriptionCriteria getSeriesDescriptionCriteria() {
        return seriesDescriptionCriteria;
    }

    /**
     */
    public void setCriteria(SeriesDescriptionCriteria seriesDescriptionCriteria) {
        if (seriesDescriptionCriteria != null) {
            criteriaList.add(seriesDescriptionCriteria);
            this.seriesDescriptionCriteria = seriesDescriptionCriteria;
        }
    }

    /**
     *
     */
    public ModalityAndedSearchCriteria getModalityAndedSearchCriteria() {
        return modalityAndedSearchCriteria;
    }

    /**
     * @param modalityAndedSearchCriteria
     */
    public void setCriteria(ModalityAndedSearchCriteria modalityAndedSearchCriteria) {
        if (modalityAndedSearchCriteria != null) {
            criteriaList.add(modalityAndedSearchCriteria);
            this.modalityAndedSearchCriteria = modalityAndedSearchCriteria;
        }
    }

    /**
     * @return Returns the getSoftwareVersionC
     */
    public SoftwareVersionCriteria getSoftwareVersionCriteria() {
        return softwareVersionCriteria;
    }

    /**
     */
    public void setCriteria(SoftwareVersionCriteria softwareVersionCriteria) {
        if (softwareVersionCriteria != null) {
            criteriaList.add(softwareVersionCriteria);
            this.softwareVersionCriteria = softwareVersionCriteria;
        }
    }

    /**
     * @return Returns the ConvolutionKernel.
     */
    public ConvolutionKernelCriteria getConvolutionKernelCriteria() {
        return convolutionKernelCriteria;
    }

    /**
     */
    public void setCriteria(ConvolutionKernelCriteria convolutionKernelCriteria) {
        if (convolutionKernelCriteria != null) {
            criteriaList.add(convolutionKernelCriteria);
            this.convolutionKernelCriteria = convolutionKernelCriteria;
        }
    }

    public AnatomicalSiteCriteria getAnatomicalSiteCriteria() {
        return anatomicalSiteCriteria;
    }

    /**
     * @param anatomicalSiteCriteria The anatomicalSiteCritera to set.
     */
    public void setCriteria(AnatomicalSiteCriteria anatomicalSiteCriteria) {
        if (anatomicalSiteCriteria != null) {
            criteriaList.add(anatomicalSiteCriteria);
            this.anatomicalSiteCriteria = anatomicalSiteCriteria;
        }
    }

    /**
     * @return Returns the collectionCriteria.
     */
    public CollectionCriteria getCollectionCriteria() {
        return collectionCriteria;
    }

    /**
     * @return Returns the patientCriteria.
     */
    public PatientCriteria getPatientCriteria() {
        return patientCriteria;
    }


    /**
     * @param collectionCriteria The collectionCriteria to set.
     */
    public void setCriteria(CollectionCriteria collectionCriteria) {
        if (collectionCriteria != null) {
            criteriaList.add(collectionCriteria);
            this.collectionCriteria = collectionCriteria;
        }
    }

    /**
     * @param patientCriteria The patientCriteria to set.
     */
    public void setCriteria(PatientCriteria patientCriteria) {
        if (patientCriteria != null) {
            criteriaList.add(patientCriteria);
            this.patientCriteria = patientCriteria;
        }
    }

    /**
     * @return Returns the manufactureCriteria.
     */
    public ManufacturerCriteria getManufacturerCriteria() {
        return manufacturerCriteria;
    }

    /**
     *
     */
    public void setCriteria(
        ManufacturerCriteria manufacturerCriteria) {
        if (manufacturerCriteria != null) {
            criteriaList.add(manufacturerCriteria);
            this.manufacturerCriteria = manufacturerCriteria;
        }
    }

    public ModelCriteria getModelCriteria() {
        return modelCriteria;
    }

    /**
     *
     */
    public void setCriteria(ModelCriteria modelCriteria) {
        if (modelCriteria != null) {
            criteriaList.add(modelCriteria);
            this.modelCriteria = modelCriteria;
        }
    }

    /**
     * @return Returns the contrastAgentCriteria.
     */
    public ContrastAgentCriteria getContrastAgentCriteria() {
        return contrastAgentCriteria;
    }

    /**
     * @param contrastAgentCriteria The contrastAgentCriteria to set.
     */
    public void setCriteria(ContrastAgentCriteria contrastAgentCriteria) {
        criteriaList.add(contrastAgentCriteria);
        this.contrastAgentCriteria = contrastAgentCriteria;
    }
    
    /**
     * @return Returns the numFrameOptionCriteria.
     */
    public NumFrameOptionCriteria getNumFrameOptionCriteria() {
        return numFrameOptionCriteria;
    }

    /**
     * @param numFrameOptionCriteria The numFrameOptionCriteriato set.
     */
    public void setCriteria(NumFrameOptionCriteria numFrameOptionCriteria) {
    	if (numFrameOptionCriteria != null){
    		criteriaList.add(numFrameOptionCriteria);
    		this.numFrameOptionCriteria = numFrameOptionCriteria;
    	}
    }  
    
    /**
     * @return Returns the numFrameOptionCriteria.
     */
    public ColorModeOptionCriteria getColorModeOptionCriteria() {
        return colorModeOptionCriteria;
    }

    /**
     * @param ColorModeOptionCriteria The ColorModeOptionCriteria to set.
     */
    public void setCriteria(ColorModeOptionCriteria colorModeOptionCriteria) {
    	if (colorModeOptionCriteria != null){
    		criteriaList.add(colorModeOptionCriteria);
    		this.colorModeOptionCriteria = colorModeOptionCriteria;
    	}
    } 

    /**
	 * @return Returns the annotationOptionCriteria.
	 */
	public AnnotationOptionCriteria getAnnotationOptionCriteria() {
		return annotationOptionCriteria;
	}

	/**
	 * @param annotationOptionCriteria The annotationOptionCriteria to set.
	 */
	public void setCriteria(AnnotationOptionCriteria annotationOptionCriteria) {
		criteriaList.add(annotationOptionCriteria);
		this.annotationOptionCriteria = annotationOptionCriteria;
	}

	/**
     * @return Returns the imageModalityCriteria.
     */
    public ImageModalityCriteria getImageModalityCriteria() {
        return imageModalityCriteria;
    }

    /**
     * @param imageModalityCriteria The imageModalityCriteria to set.
     */
    public void setCriteria(ImageModalityCriteria imageModalityCriteria) {
        if (imageModalityCriteria != null) {
            criteriaList.add(imageModalityCriteria);
            this.imageModalityCriteria = imageModalityCriteria;
        }
    }
    
	/**
     * @return Returns the usMultiModalityCriteria.
     */
    public UsMultiModalityCriteria getUsMultiModalityCriteria() {
        return usMultiModalityCriteria;
    }

    /**
     * @param usMultiModalityCriteria The usMultiModalityCriteria to set.
     */
    public void setCriteria(UsMultiModalityCriteria usMultiModalityCriteria) {
        if (usMultiModalityCriteria != null) {
            criteriaList.add(usMultiModalityCriteria);
            this.usMultiModalityCriteria = usMultiModalityCriteria;
        }
    }

    /**
     * @return Returns the minNumberOfStudiesCriteria.
     */
    public MinNumberOfStudiesCriteria getMinNumberOfStudiesCriteria() {
        return minNumberOfStudiesCriteria;
    }

    /**
     * @param minNumberOfStudiesCriteria The minNumberOfStudiesCriteria to set.
     */
    public void setCriteria(MinNumberOfStudiesCriteria minNumberOfStudiesCriteria) {
        if (minNumberOfStudiesCriteria != null) {
            criteriaList.add(minNumberOfStudiesCriteria);
            this.minNumberOfStudiesCriteria = minNumberOfStudiesCriteria;
        }
    }

    /**
     * @return Returns the numOfMonthsCriteria.
     */
    public NumOfMonthsCriteria getNumOfMonthsCriteria() {
        return numOfMonthsCriteria;
    }

    /**
     * @param numOfMonthsCriteria The numOfMonthsCriteria to set.
     */
    public void setCriteria(NumOfMonthsCriteria numOfMonthsCriteria) {
        if (numOfMonthsCriteria != null) {
            criteriaList.add(numOfMonthsCriteria);
            this.numOfMonthsCriteria = numOfMonthsCriteria;
        }
    }

    /**
     * @return Returns the imageSliceThickness.
     */
    public ImageSliceThickness getImageSliceThickness() {
        return imageSliceThickness;
    }

    /**
     * @param imageSliceThickness The imageSliceThickness to set.
     */
    public void setCriteria(ImageSliceThickness imageSliceThickness) {
        criteriaList.add(imageSliceThickness);
        this.imageSliceThickness = imageSliceThickness;
    }


    /**
     * @return Returns the imageSliceThickness.
     */
    public KilovoltagePeakDistribution getKilovoltagePeakDistribution() {
        return kilovoltagePeakDistribution;
    }

    /**
     *
     */
    public void setCriteria(KilovoltagePeakDistribution kilovoltagePeakDistribution) {
        criteriaList.add(kilovoltagePeakDistribution);
        this.kilovoltagePeakDistribution = kilovoltagePeakDistribution;
    }

    /**
	 * @return Returns the reconstructionDiameter.
	 */
	public ReconstructionDiameterCriteria getReconstructionDiameterCriteria() {
		return reconstructionDiameterCriteria;
	}

	/**
	 *
	 */
	public void setCriteria(ReconstructionDiameterCriteria reconstructionDiameterCriteria) {
        criteriaList.add(reconstructionDiameterCriteria);
		this.reconstructionDiameterCriteria = reconstructionDiameterCriteria;
	}

	/**
	 * @return Returns the acquisitionMatrix.
	 */
	public AcquisitionMatrixCriteria getAcquisitionMatrixCriteria() {
		return acquisitionMatrixCriteria;
	}

	/**
	 *
	 */
	public void setCriteria(AcquisitionMatrixCriteria acquisitionMatrixCriteria) {
		criteriaList.add(acquisitionMatrixCriteria);
		this.acquisitionMatrixCriteria = acquisitionMatrixCriteria;
	}

	/**
	 * @return Returns the dataCollectionDiameter.
	 */
	public DataCollectionDiameterCriteria getDataCollectionDiameterCriteria() {
		return dataCollectionDiameterCriteria;
	}

	/**
	 *
	 */
	public void setCriteria(DataCollectionDiameterCriteria dataCollectionDiameterCriteria) {
		criteriaList.add(dataCollectionDiameterCriteria);
		this.dataCollectionDiameterCriteria = dataCollectionDiameterCriteria;
	}

	/**
	 * @return Returns the dxDataCollectionDiameter.
	 */
	public DxDataCollectionDiameterCriteria getDxDataCollectionDiameterCriteria() {
		return dxDataCollectionDiameterCriteria;
	}

	/**
	 *
	 */
	public void setCriteria(DxDataCollectionDiameterCriteria dxDataCollectionDiameterCriteria) {
		criteriaList.add(dxDataCollectionDiameterCriteria);
		this.dxDataCollectionDiameterCriteria = dxDataCollectionDiameterCriteria;
	}

	public List<Criteria> getCriteriaList() {
        Collections.sort(criteriaList, new CriteriaComparator());
        return criteriaList;
    }



    /**
     * Get number of milliseconds that it took to run the query
     */
    public long getElapsedTimeInMillis() {
        return elapsedTimeInMillis;
    }

    /**
     * Set number of milliseconds that it took to run the query
    */
    public void setElapsedTimeInMillis(long millisecs) {
        elapsedTimeInMillis = millisecs;
    }

    /**
     * Filters the query's list of criteria to include
     * only those criteria that are PersistentCriterian
     */
    public List<PersistentCriteria> getPersistentCriteria() {
        List<PersistentCriteria> returnList = new ArrayList<PersistentCriteria>();

        for (Criteria criteria : getCriteriaList()) {
            if (criteria instanceof PersistentCriteria) {
                returnList.add((PersistentCriteria) criteria);
            }
        }

        return returnList;
    }


    /**
     * Getter for curation status date
     */
    public CurationStatusDateCriteria getCurationStatusDateCriteria() {
        return curationStatusDateCriteria;
    }

    /**
     * Setter for curation status date
     */
    public void setCriteria(CurationStatusDateCriteria curationStatusDateCriteria) {
        this.curationStatusDateCriteria = curationStatusDateCriteria;
    }

    /**
     * The name that this query was saved under
     *
     * @return the saved query name
     */
    public String getQueryName() {
        return queryName;
    }

    /**
     * Setter for query name
     *
     * @param queryName - the name that this query was saved under
     */
    public void setQueryName(String queryName) {
        if (queryName != null) {
            this.queryName = queryName;
        }
    }

    /**
     *
     * @return ID of the query
     */
    public long getSavedQueryId() {
        return savedQueryId;
    }

    /**
     * Setter
     *
     * @param id
     */
    public void setSavedQueryId(long id) {
        this.savedQueryId = id;
    }

    /**
     *
     * @return ID of the query
     */
    public long getQueryHistoryId() {
        return queryHistoryId;
    }

    /**
     * Setter
     *
     * @param id
     */
    public void setQueryHistoryId(long id) {
        this.queryHistoryId = id;
    }

    /**
     * Getter for authorization criteria
     */
    public AuthorizationCriteria getAuthorizationCriteria() {
        return authorizationCriteria;
    }

    /**
     * Setter for authorization criteria
     */
    public void setCriteria(AuthorizationCriteria authorizationCriteria) {
        this.authorizationCriteria = authorizationCriteria;
    }


    /**
     * Returns a list of the URLParamCriteria
     */
    public List<UrlParamCriteria> getUrlParamCriteria() {
        return urlParamCriteria;
    }

    /**
     * Adds a URLParamCriteria to the query.  Due to specifications, it should only
     * ever contain 2 items, so that check is made.
     *
     * @param urlParamCriteria
     */
    public void addUrlParamCriteria(UrlParamCriteria urlParamCriteria) {
        if (this.urlParamCriteria == null) {
            queryFromUrl = true;
            this.urlParamCriteria = new ArrayList<UrlParamCriteria>();
        }

        if (this.urlParamCriteria.size() < 2) {
            this.urlParamCriteria.add(urlParamCriteria);
        }
    }

    /**
     * Returns true if it is a URL query
     */
    public boolean isQueryFromUrl() {
        return queryFromUrl;
    }

    /**
     * Sets whether or not it is a query from the URL.
     *
     * @param queryFromUrl
     */
    public void setQueryFromUrl(boolean queryFromUrl) {
        this.queryFromUrl = queryFromUrl;
    }

	public DateRangeCriteria getDateRangeCriteria() {
		return dateRangeCriteria;
	}

	public void setCriteria(DateRangeCriteria dateRangeCriteria) {
		if ((dateRangeCriteria != null) && !dateRangeCriteria.isEmpty()) {
			criteriaList.add(dateRangeCriteria);
		}
		this.dateRangeCriteria = dateRangeCriteria;
	}

}
