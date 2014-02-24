/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchresults;

import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.basket.BasketBean;
import gov.nih.nci.nbia.beans.security.AnonymousLoginBean;
import gov.nih.nci.nbia.search.DrillDown;
import gov.nih.nci.nbia.search.DrillDownFactory;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.NCIAConstants;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.ncia.search.StudySearchResult;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIData;

import org.apache.log4j.Logger;

//all the studies for a given patient
public class StudiesSearchResultBean {

	
    //table for study view... that lists all the series per study
	public UIData getSeriesData() {
		return seriesData;
	}

	public void setSeriesData(UIData seriesData) {
		this.seriesData = seriesData;
	}
	
	
	public UIData getStudyData() {
		return studyData;
	}

	public void setStudyData(UIData studyData) {
		this.studyData = studyData;
	}
	
	
	public StudySearchResult getStudy() {
		return study;
	}
	
	
	public List<StudyResultWrapper> getStudyResults() {
		String installationSite = NCIAConfig.getInstallationSite();
		if (installationSite.equalsIgnoreCase(NCIAConstants.INSTALLATION_SITE)) {
			//check if in basket
			BasketBean dataBasket = BeanManager.getBasketBean();
			Map<String, Boolean> basketMap = dataBasket.getInBasketMap();
			for(StudyResultWrapper study : studyResults) {
				for(SeriesResultWrapper seriesWrapper : study.getSeriesResults()) {
					if(basketMap.containsKey(seriesWrapper.getBasketKey())) {
						seriesWrapper.setChecked(true);
					}
				}
			}
		}
		return studyResults;
	}

	
	public List<StudySearchResult> getUnwrappedStudyResults() {
		List<StudySearchResult> studies = new ArrayList<StudySearchResult>();
		for(StudyResultWrapper study : studyResults) {
			studies.add(study.getStudy());
		}
		return studies;
	}

	/**
	 * Adds all of the selected series to the basket.
	 */
	public String addSeriesToBasket() throws Exception {
		SeriesSearchResult seriesSearchResult = getSelectedSeries(toAdd);
		setSeriesCheckBox(true);
		addToBasket(Arrays.asList(seriesSearchResult));
		return null;
	}

	private void setSeriesCheckBox(boolean value) {
		for(StudyResultWrapper studywraper : studyResults) {
			for(SeriesResultWrapper seriesWrapper : studywraper.getSeriesResults()) {
				if(seriesWrapper.getSeries().getId() == toAdd) {
					seriesWrapper.setChecked(value);
				}
			}
		}
	}
	
	private String addToBasket(List<SeriesSearchResult> selectedSeriesList) throws Exception {
		BasketBean dataBasket = BeanManager.getBasketBean();
		AnonymousLoginBean anonymousLoginBean = BeanManager.getAnonymousLoginBean();
		// Anonymous login: need to get the size from current data basket and calculate the selected ones.
		// The total should not >= 3GB. Otherwise, show the warning message.
		if(anonymousLoginBean.getGuestLoggedIn() ){
			double selectedSize = calculateSelectedSeriesSize(selectedSeriesList);
			double currentBasketSize = dataBasket.getBasket().calculateSizeInMB();
			double ftpLimit = NCIAConfig.getFtpThreshold();
			double total = currentBasketSize + selectedSize;
			if(total >= ftpLimit){
				//show warning message
				String fieldId = "MAINbody:dataForm:addSeriesToBasketButton";
				showMessage(fieldId, total, ftpLimit);
				return null;
			}
    	}
		//uncheckAllSeries();
		dataBasket.getBasket().addSeries(selectedSeriesList);
		return null;
	}
	
	private int toAdd;

	public int getToAdd() {
		return toAdd;
	}
	
	public void setToAdd(int toAdd) {
		this.toAdd = toAdd;
	}

	public String removeSeriesFromBasket() {
		try {
			SeriesSearchResult s = getSelectedSeries(toAdd);
			String toDelete = s.getId() + "||" + s.associatedLocation().getURL();
			BeanManager.getBasketBean().getBasket().removeSelectedSeries(toDelete);
			setSeriesCheckBox(false);		
		} catch(Exception ex) {
			MessageUtil.addErrorMessage("MAINbody:dataForm:tableOfPatientResultTables",
                    "drillDownRequestFailure",
                    null );
		}		
		return null;
	}


	private SeriesSearchResult getSelectedSeries(int seriesId) {

		for(StudyResultWrapper studywraper : studyResults) {
			for(SeriesResultWrapper seriesWrapper : studywraper.getSeriesResults()) {
				if(seriesWrapper.getSeries().getId() == seriesId) {
					SeriesSearchResult series = seriesWrapper.getSeries();
					series.setStudyDate(studywraper.getDateString());
					series.setStudyDescription(studywraper.getStudy().getDescription());
					return series;
				}
			}
		}	
		return null;
	}
	
	/**
	 * Checks all of the series in the list.
	 */
	public String checkAllSeries() {
		for(StudyResultWrapper study : studyResults) {
			for(SeriesResultWrapper seriesWrapper : study.getSeriesResults()) {
				seriesWrapper.setChecked(true);
			}
		}	

		return null;
	}

	/**
	 * Unchecks all of the series in the list.
	 */
	public String uncheckAllSeries() {
		for(StudyResultWrapper study : studyResults) {
			for(SeriesResultWrapper seriesWrapper : study.getSeriesResults()) {
				seriesWrapper.setChecked(false);
			}
		}	

		return null;
	}	


	public void viewPatient(PatientSearchResult patientSearchResult) throws Exception {
	

		DrillDown drillDown = DrillDownFactory.getDrillDown();
		StudySearchResult[] studies = drillDown.retrieveStudyAndSeriesForPatient(patientSearchResult);
			
		this.setStudyResults(studies);		
	}	


	public String viewSeries() throws Exception {
		StudyResultWrapper theStudy = (StudyResultWrapper) studyData.getRowData();
		List<SeriesResultWrapper> seriesList = theStudy.getSeriesResults();
		SeriesResultWrapper theSeries = seriesList.get(seriesData.getRowIndex());
		try {
			return viewSeries(theStudy.getStudy(), theSeries.getSeries());
		}
		catch(Exception ex) {
			MessageUtil.addErrorMessage("MAINbody:dataForm:studyTable",
                                        "drillDownRequestFailure",
                                        null );
			return null;
		}
	}	
	
	
	public String viewSeries(StudySearchResult theStudy, 
			                 SeriesSearchResult theSeries) throws Exception {
		logger.debug("viewing series");
		study = theStudy;
		
		seriesSearchResultBean.viewSeries(theSeries);
		return "viewImages";
	}		
	
	public SeriesSearchResultBean getSeriesSearchResultBean() {
		return seriesSearchResultBean;
	}

	public void setSeriesSearchResultBean(SeriesSearchResultBean seriesSearchResultBean) {
		this.seriesSearchResultBean = seriesSearchResultBean;
	}		
	
    ////////////////////////////////////////PRIVATE///////////////////////////////////////////
	
    private SeriesSearchResultBean seriesSearchResultBean;

	/**
	 * List of studies returned from search.
	 */
	private List<StudyResultWrapper> studyResults;
	
	private UIData studyData;
	
	private UIData seriesData;
	
	private StudySearchResult study;
	
	/**
	 * Logger for the class.
	 */
	private static Logger logger = Logger.getLogger(StudiesSearchResultBean.class);
	
	/**
	 * calculate the selected series size
	 * @param seriesDTOs
	 */
	private static double calculateSelectedSeriesSize(List<SeriesSearchResult> seriesDTOs){
		BasketBean dataBasket = BeanManager.getBasketBean();
		long size=0;
		for(SeriesSearchResult seriesDTO: seriesDTOs){
    		if(!dataBasket.getBasket().isSeriesInBasket(seriesDTO.getId(),  
    				                                    seriesDTO.associatedLocation().getURL())){
    			size +=seriesDTO.computeExactSize();
			}
    	}
        return Long.valueOf(size).doubleValue() / 1000000.0;

	}
	/**
	 * show warning message when data basket and selected are not http for anonymous login
	 * @param fieldId
	 * @param selectedSize
	 */
	private static void showMessage(String fieldId, double selectedSize, double ftpLimit){
		DecimalFormat nf = (DecimalFormat) DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(1);
		Object param[] = new Object[2];
		param[0]=nf.format(ftpLimit/1000) + " GB";
		param[1]=nf.format(selectedSize /1000) + " GB";
		MessageUtil.addErrorMessage(fieldId,
		                            "downloadWarningForGuest",
		                            param );
	}
	
	private void setStudyResults(StudySearchResult[] studies) {
		this.studyResults = new ArrayList<StudyResultWrapper>(studies.length);
		for(StudySearchResult study : studies) {
			studyResults.add(new StudyResultWrapper(study));
		}
	}
	
	private List<SeriesSearchResult> findSelectedSeries() {
		List<SeriesSearchResult> selectedSeriesList = new ArrayList<SeriesSearchResult>();
		for(StudyResultWrapper study : studyResults) {
			for(SeriesResultWrapper seriesWrapper : study.getSeriesResults()) {
				if(seriesWrapper.isChecked()) {
					selectedSeriesList.add(seriesWrapper.getSeries());
				}
			}
		}	
		return selectedSeriesList;
	}
	public String addAllSeriesToBasket() throws Exception {
		List<SeriesSearchResult> seriesList = new ArrayList<SeriesSearchResult>();
		for(StudyResultWrapper study : studyResults) {
			for(SeriesResultWrapper seriesWrapper : study.getSeriesResults()) {
				SeriesSearchResult series = seriesWrapper.getSeries();
				series.setStudyDate(study.getDateString());
				series.setStudyDescription(study.getStudy().getDescription());
				seriesList.add(series);
				seriesWrapper.setChecked(true);
			}
		}		
		addToBasket(seriesList);
		return null;
	}
}
