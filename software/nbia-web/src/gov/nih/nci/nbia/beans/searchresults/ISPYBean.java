/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchresults;
import gov.nih.nci.ncia.criteria.UrlParamCriteria;
import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.basket.BasketBean;
import gov.nih.nci.nbia.beans.searchform.SearchWorkflowBean;
import gov.nih.nci.nbia.ispy.UrlParams;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.search.LocalDrillDown;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.StudyUtil;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.StudySearchResult;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIData;
import javax.faces.context.FacesContext;

public class ISPYBean {

	/**
	 * Drill down method from study to series.
	 */
	public String viewSeriesForRefImage() throws Exception {
		ISPYImageWrapper ispyImageWrapper = (ISPYImageWrapper) getRefImageData().getRowData();
		List<PatientSearchResult> patients = extractPatientDTO(getPatients());

		PatientSearchResult selectedPatient = StudyUtil.findLastMatchingPatientForSeries(patients,
				                                                                         ispyImageWrapper.getImage().getSeriesId());

		LocalDrillDown drillDown = new LocalDrillDown();
		drillDown.setThumbnailURLResolver(new DefaultThumbnailURLResolver());
		StudySearchResult[] studyResults = drillDown.retrieveStudyAndSeriesForPatient(selectedPatient);

		searchResultBean.viewPatient(new PatientResultWrapper(selectedPatient));

        StudyUtil.StudySeriesPair studySeriesPair = StudyUtil.findFirstMatchingStudyForSeries(studyResults,
        		                                                                              ispyImageWrapper.getImage().getSeriesId());
        studiesSearchResultBean.viewSeries(studySeriesPair.studyDto,
				                           studySeriesPair.seriesDto);

		return "viewImages";
	}

	/**
	 * Adds all of the selected images to the data basket.
	 */
	public String addImagesToBasketForRefImage() throws Exception {
		BasketBean dataBasket = BeanManager.getBasketBean();

		ISPYImageWrapper imageSearchResult = (ISPYImageWrapper) refImageData.getRowData();

		dataBasket.getBasket().addThumbnail(imageSearchResult.getImage());

		return null;
	}



	/**
	 * Returns the two images from the URL query.
	 */
	public List<ISPYImageWrapper> getReferencedImages() {
		UrlParams urlParams = (UrlParams) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("UrlParams");

		try {
			queryUsingUrl();

			List<PatientResultWrapper> patientData = getPatients();

			// Get series IDs from patient
			List<Integer> seriesIds = getSeriesIdsFromPatients(patientData);

			LocalDrillDown drillDown = new LocalDrillDown();
			drillDown.setThumbnailURLResolver(new DefaultThumbnailURLResolver());

			ImageSearchResult[] results = drillDown.retrieveImagesbySeriesPkID(seriesIds);

			UrlParamCriteria leftCrit = searchWorkflowBean.getQuery().getUrlParamCriteria().get(0);
			UrlParamCriteria rightCrit = searchWorkflowBean.getQuery().getUrlParamCriteria().get(1);
			ISPYImageWrapper tempRightImage = null;

			referencedImages = new ArrayList<ISPYImageWrapper>();
			for (ImageSearchResult dto : results) {
				if (seriesAndSopMatch(dto,leftCrit)) {
					ISPYImageWrapper imageWrapper = new ISPYImageWrapper(dto);
					imageWrapper.setLabel(urlParams.getImage1Label());
					imageWrapper.setParamNameValues(urlParams.getImage1dataNameValues());
					referencedImages.add(0, imageWrapper);
				}

				if (seriesAndSopMatch(dto,rightCrit)) {
					ISPYImageWrapper imageWrapper = new ISPYImageWrapper(dto);
					tempRightImage = imageWrapper;

					imageWrapper.setLabel(urlParams.getImage2Label());
					imageWrapper.setParamNameValues(urlParams.getImage2dataNamesValues());
				}
			}

			if (tempRightImage != null) {
				if (referencedImages.size() == 0) {
					referencedImages.add(0, tempRightImage);
				}
				else
				if (referencedImages.size() == 1) {
					referencedImages.add(1, tempRightImage);
				}
				//else error?
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			MessageUtil.addErrorMessage("MAINbody:errorMsgForm:errorMsg",
					                    "imageNotFound");
		}

		return referencedImages;
	}


	public UIData getRefImageData() {
		return refImageData;
	}


	public void setRefImageData(UIData refImageData) {
		this.refImageData = refImageData;
	}

	//click from submenu
	public String viewRefImage() {
		//setDataBasketStateForDTO();
		return "referencedImages";
	}

	/**
	 * Returns true if it is a URL query
	 */
	public boolean getIsQueryFromUrl() {
		if (searchWorkflowBean.getQuery() != null) {
			return searchWorkflowBean.getQuery().isQueryFromUrl();
		} else {
			return false;
		}
	}

	public SearchWorkflowBean getSearchWorkflowBean() {
		return searchWorkflowBean;
	}

	public void setSearchWorkflowBean(SearchWorkflowBean searchWorkflowBean) {
		this.searchWorkflowBean = searchWorkflowBean;
	}

	public StudiesSearchResultBean getStudiesSearchResultBean() {
		return studiesSearchResultBean;
	}

	public void setStudiesSearchResultBean(StudiesSearchResultBean studiesSearchResultBean) {
		this.studiesSearchResultBean = studiesSearchResultBean;
	}

	public SearchResultBean getSearchResultBean() {
		return searchResultBean;
	}

	public void setSearchResultBean(SearchResultBean searchResultBean) {
		this.searchResultBean = searchResultBean;
	}

	////////////////////////////////////PRIVATE//////////////////////////////////////////

	/**
	 * Holds the referenced images from a URL query
	 */
	private List<ISPYImageWrapper> referencedImages;

	private UIData refImageData;

	private SearchResultBean searchResultBean;

	private StudiesSearchResultBean studiesSearchResultBean;

	private SearchWorkflowBean searchWorkflowBean;

    private void queryUsingUrl() throws Exception {
        DICOMQuery query = new DICOMQuery();

        UrlParams urlParams = (UrlParams) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UrlParams");
        UrlParamCriteria crit = new UrlParamCriteria();
        crit.setPatientId(urlParams.getImage1PatientId());
        crit.setStudyInstanceUid(urlParams.getImage1StudyInstanceUid());
        crit.setSeriesInstanceUid(urlParams.getImage1SeriesInstanceUid());
        crit.setImageSopInstanceUid(urlParams.getImage1ImageSopInstanceUid());

        query.addUrlParamCriteria(crit);

        UrlParamCriteria crit2 = new UrlParamCriteria();
        crit2.setPatientId(urlParams.getImage2PatientId());
        crit2.setStudyInstanceUid(urlParams.getImage2StudyInstanceUid());
        crit2.setSeriesInstanceUid(urlParams.getImage2SeriesInstanceUid());
        crit2.setImageSopInstanceUid(urlParams.getImage2ImageSopInstanceUid());

        query.addUrlParamCriteria(crit2);
        query.setQueryFromUrl(true);

		searchWorkflowBean.synchronousLocalQuery(query);
    }

	private static List<Integer> getSeriesIdsFromPatients(List<PatientResultWrapper> patientDtoList) {
		List<Integer> seriesIds = new ArrayList<Integer>();

		for (PatientResultWrapper patient : patientDtoList) {
			for (Integer seriesId : patient.getPatient().computeListOfSeriesIds()) {
				seriesIds.add(seriesId);
			}
		}
		return seriesIds;
	}

	private static boolean seriesAndSopMatch(ImageSearchResult imageDto,
                                             UrlParamCriteria urlParamCriteria ) {
		return imageDto.getSeriesInstanceUid().equals(urlParamCriteria.getSeriesInstanceUid()) &&
		       imageDto.getSopInstanceUid().equals(urlParamCriteria.getImageSopInstanceUid());
	}

	private List<PatientResultWrapper> getPatients() {
		return (List<PatientResultWrapper>) searchResultBean.getPatientResults();
	}

	private static List<PatientSearchResult> extractPatientDTO(List<PatientResultWrapper> wrappers) {
		List<PatientSearchResult> dtos = new ArrayList<PatientSearchResult>();
		for(PatientResultWrapper wrapper : wrappers) {
			dtos.add(wrapper.getPatient());
		}
		return dtos;
	}
}
