package gov.nih.nci.ncia;

import com.thoughtworks.selenium.SeleneseTestCase;

public class AbstractSelenTestCaseImpl extends SeleneseTestCase {

	public void setUp() throws Exception {
		String seleniumBrowser = System.getProperty("selenium.browser");
		System.out.println("seleniumBrowser:"+seleniumBrowser);
		System.out.println("selnurl:"+System.getProperty("selenium.url"));
		if(seleniumBrowser==null) {
			seleniumBrowser = "*firefox";
		}
		setUp(System.getProperty("selenium.url"),
		      seleniumBrowser);

	}

//////////////////////////////////////////LOGIN STUFF///////////////////////////

	public void login() {
		login("nciadevtest", "saicT3@m16");
	}

	public void login(String username, String password) {
		selenium.open("/ncia/");
		selenium.type("MAINbody:sideBarView:loginForm:uName2", username);
		selenium.type("MAINbody:sideBarView:loginForm:pass2", password);
		selenium.click("MAINbody:sideBarView:loginForm:loginButton2");

		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:navigationForm:searchLink')",
				                  "30000");
	}

	public void loginAsGuest() {
		selenium.open("/ncia/login.jsf");
		selenium.click("MAINbody:guestEnabled:navigationForm1:guestSearchLink");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:searchForm')",
                                  "30000");
	}

	public void loginToISPYPortal() {
		selenium.type("MAINbody:sideBarView:loginForm:uName2", "nciadevtest");
		selenium.type("MAINbody:sideBarView:loginForm:pass2", "saicT3@m16");
		selenium.click("MAINbody:sideBarView:loginForm:loginButton2");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:imageForm:imageTable')",
	                              "30000");
	}

/////////////////////////////////////////SELECT OR TWEAK PAGE CONTROLS///////////////////////////////////

	public void selectAnatomicalSite(String anatomicalSite) {
		String id = selenium.getValue("xpath=id('MAINbody:searchForm:anatomicalSitesCheckboxesTable')//tr[td='"+anatomicalSite+"']/td//input/@id");
		selenium.click(id);
	}


	public void selectModalitySearchCriteria(String modality) {
		String id = selenium.getValue("xpath=id('MAINbody:searchForm:modalityCheckboxesTable')//tr[td='"+modality+"']/td//input/@id");
		selenium.click(id);
	}

	public void submitSearch() {
		selenium.click("MAINbody:searchForm:submitSearchButton");

		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:dataForm:tableOfPatientResultTables')",
                                  "30000");
	}


	public void selectSubmissionReportCollection(String collectionSiteName) {
		selenium.select("MAINbody:submissionReportCriteriaForm:collectionSiteMenu", "label="+collectionSiteName);
	}

	public void selectSubmissionReportDateRange(String fromDate, String toDate) {
		selenium.type("MAINbody:submissionReportCriteriaForm:fromDate", fromDate);
		selenium.type("MAINbody:submissionReportCriteriaForm:toDate", toDate);
	}


	public void submitAccrualReport() {
		selenium.click("MAINbody:submissionReportCriteriaForm:accrualReportSubmit");

	}

	public void submitAnnotationReport() {
		selenium.click("MAINbody:submissionReportCriteriaForm:annotationReportSubmit");
	}

	public void waitForEmptyAnnotationResults() {
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:annotationNoResultsMsg')",
                                  "30000");
    }

	public void waitForEmptyAccrualResults() {
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:accrualNoResultsMsg')",
                                  "30000");
    }

	public void waitForAccrualResults() {
        selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:accrualByDayForm:accrualByDayTable')",
                                  "30000");
    }

	public void selectPatientsWithAllModalities() {
		selenium.click("//input[@name='MAINbody:searchForm:andSearchModalityCheck' and @value='all']");
	}

	public void selectEnhancedContrast() {

		selenium.click("//input[@name='MAINbody:searchForm:contrastSelector' and @value='Enhanced']");
	}


	public void selectCollectionCriteria(String collectionName) {
		//contains finds Phantom, but = does not.  starts-with doesnt find it either...
		//not sure i understand why it wont match
		String id = selenium.getValue("xpath=id('MAINbody:searchForm:collectionSearchCriteriaRow:collectionsCheckboxesTable')//tr[normalize-space(td)='"+collectionName+"']/td/input/@id");

		selenium.click(id);
	}

	public void selectImageSliceThickness(String leftOp, String leftVal, String rightOp, String rightVal) {
		selenium.click("MAINbody:searchForm:imageSliceRadio");
		selenium.select("MAINbody:searchForm:cmpLeft", "label="+leftOp);
		selenium.select("MAINbody:searchForm:cmpRight", "label="+rightOp);
		selenium.select("MAINbody:searchForm:valLeft", "label="+leftVal);
		selenium.select("MAINbody:searchForm:imgThick", "label="+rightVal);
	}

	public void setDateRange(String fromDate, String toDate) {
		selenium.click("MAINbody:searchForm:fromDate");
		selenium.type("MAINbody:searchForm:fromDate", fromDate);
		selenium.click("MAINbody:searchForm:toDate");
		selenium.type("MAINbody:searchForm:toDate", toDate);
	}

	public void setResultsPerPage(String numResults) {
		selenium.select("MAINbody:searchForm:resultsPerPage", "label="+numResults);
	}

	public void selectOnlySeriesThatHaveNoAnnotations() {
		selenium.click("//input[@name='MAINbody:searchForm:annotationSelector' and @value='Return Only Series That Do Not Have Annotations']");
	}

	public void saveQuery(String queryName) {
		selenium.type("MAINbody:dataForm:saveQueryView:queryName", queryName);
		selenium.click("MAINbody:dataForm:saveQueryView:saveQueryButton");
	}

	public void addFirstPatientOfFirstNodeResultsToBasket() {
		selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:0:addPatientToBasketCheckbox");
		selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:addPatientToBasketButton");
	}

//////////////////////////////////////////READ FROM PAGE///////////////////////////////////////

	public String getOverallCountOfPatientsAffected() {
		String overallCntTableLocator =	"xpath=id('overallResultsCount')//table[1]";
		return selenium.getTable(overallCntTableLocator+".1.1").trim();
	}

	public String getOverallCountOfStudiesAffected() {
		String overallCntTableLocator =	"xpath=id('overallResultsCount')//table[1]";
		return selenium.getTable(overallCntTableLocator+".2.1").trim();
	}

	public String getOverallCountOfSeriesAffected() {
		String overallCntTableLocator =	"xpath=id('overallResultsCount')//table[1]";
		return selenium.getTable(overallCntTableLocator+".3.1").trim();
	}


	public boolean isAllModalitySelected() {
    	return selenium.isChecked("//input[@name='MAINbody:searchForm:andSearchModalityCheck' and @value='all']");

	}

	public int getNumModalitiesSelected() {
		return selenium.getXpathCount("//table[@id='MAINbody:searchForm:selectedModalitiesTable']//tr").intValue();
	}

	public int getNumCollectionsSelected() {
		return selenium.getXpathCount("//table[@id='MAINbody:searchForm:collectionSearchCriteriaRow:selectedCollectionsTable']//tr").intValue();
	}

	public String getFromDate() {
		return selenium.getValue("//input[@name='MAINbody:searchForm:fromDate']");
	}

	public String getToDate() {
		return selenium.getValue("//input[@name='MAINbody:searchForm:toDate']");
	}

	public int getNumAnatomicalSitesSelected() {
		return selenium.getXpathCount("//table[@id='MAINbody:searchForm:selectedAnatomicalSitesTable']//tr").intValue();
	}

	public boolean isReturnOnlySeriesWithNoAnnotationsSelected() {
		return selenium.isChecked("//input[@name='MAINbody:searchForm:annotationSelector' and @value='Return Only Series That Do Not Have Annotations']");
	}

	public boolean isReturnOnlySeriesWithAnnotationsSelected() {
		return selenium.isChecked("//input[@name='MAINbody:searchForm:annotationSelector' and @value='Return Only Annotated Series']");
	}

	public String getSelectedNumResultsPerPage() {
		return selenium.getSelectedLabel("id=MAINbody:searchForm:resultsPerPage");
	}

	public boolean isConstrastEnhancedSelected() {
		return selenium.isChecked("//input[@name='MAINbody:searchForm:contrastSelector' and @value='Enhanced']");
	}

	public boolean isContrastUnenhancedSelected() {
		return selenium.isChecked("//input[@name='MAINbody:searchForm:contrastSelector' and @value='Unenhanced']");
	}

	public boolean isImageSliceThicknessSelected() {
		return selenium.isChecked("id=MAINbody:searchForm:imageSliceRadio");
	}

	public String getImageSliceThicknessLeftOp() {
		return selenium.getSelectedLabel("id=MAINbody:searchForm:cmpLeft");
	}

	public String getImageSliceThicknessLeftValue() {
		return selenium.getSelectedLabel("id=MAINbody:searchForm:valLeft");
	}

	public String getImageSliceThicknessRightValue() {
		return selenium.getSelectedLabel("id=MAINbody:searchForm:imgThick");
	}

	public String getImageSliceThicknessRightOp() {
		return selenium.getSelectedLabel("id=MAINbody:searchForm:cmpRight");
	}

	public int getNumOfRowsInDataBasket() {
		return selenium.getXpathCount("id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr").intValue();
	}

	public String getPatientIdFromDataBasket(int rowNum) {
		final int PATIENT_ID_COL = 2;

        return selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr["+rowNum+"]/td["+PATIENT_ID_COL+"]");
	}

	public String getStudyIdFromDataBasket(int rowNum) {
		final int STUDY_ID_COL = 3;

		return selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr["+rowNum+"]/td["+STUDY_ID_COL+"]");

	}

	public String getSeriesIdFromDataBasket(int rowNum) {
		final int SERIES_ID_COL = 4;

		return selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr["+rowNum+"]/td["+SERIES_ID_COL+"]");
	}

	public int getNumOfPatientsForAnnotationSubmissionReportDay(int nthDay) {
		String patientDetailsTable =
			"MAINbody:annotationByDayForm:annotationByDayTable:"+nthDay+":patientDetailsTable";

		return selenium.getXpathCount("id('"+patientDetailsTable+"')//tr").intValue();
	}


	//day is 0 based, patient is 1 based
	public String getNthPatientIdForAnnotationSubmissionReportDay(int nthDay, int nthPatient) {
		String patientDetailsTable =
			"MAINbody:annotationByDayForm:annotationByDayTable:"+nthDay+":patientDetailsTable";

		return selenium.getTable(patientDetailsTable+"."+nthPatient+".0").trim();
	}

	public String getNthPatientNumStudiesForAnnotationSubmissionReportDay(int nthDay, int nthPatient) {
		String patientDetailsTable =
			"MAINbody:annotationByDayForm:annotationByDayTable:"+nthDay+":patientDetailsTable";

		return selenium.getTable(patientDetailsTable+"."+nthPatient+".1").trim();
	}

	public String getNthPatientNumSeriesForAnnotationSubmissionReportDay(int nthDay, int nthPatient) {
		String patientDetailsTable =
			"MAINbody:annotationByDayForm:annotationByDayTable:"+nthDay+":patientDetailsTable";

		return selenium.getTable(patientDetailsTable+"."+nthPatient+".2").trim();
	}

	public String getNthPatientNumAnnotationsForAnnotationSubmissionReportDay(int nthDay, int nthPatient) {
		String patientDetailsTable =
			"MAINbody:annotationByDayForm:annotationByDayTable:"+nthDay+":patientDetailsTable";

		return selenium.getTable(patientDetailsTable+"."+nthPatient+".3").trim();
	}

	//day is 0 based, patient and study is 1 based
	public String getNthStudyIdForPatientAnnotationSubmissionReportDay(int nthDay, int nthPatient, int nthStudy) {
		String patientDetailsTable =
			"MAINbody:annotationByDayForm:annotationByDayTable:"+nthDay+":patientDetailsTable";

		return selenium.getTable(patientDetailsTable+"."+(nthPatient+nthStudy)+".1").trim();
	}

	public String getNthNumSeriesForPatientAnnotationSubmissionReportDay(int nthDay, int nthPatient, int nthStudy) {
		String patientDetailsTable =
			"MAINbody:annotationByDayForm:annotationByDayTable:"+nthDay+":patientDetailsTable";

		return selenium.getTable(patientDetailsTable+"."+(nthPatient+nthStudy)+".2").trim();
	}

	public String getNthNumAnnotationsForPatientAnnotationSubmissionReportDay(int nthDay, int nthPatient, int nthStudy) {
		String patientDetailsTable =
			"MAINbody:annotationByDayForm:annotationByDayTable:"+nthDay+":patientDetailsTable";

		return selenium.getTable(patientDetailsTable+"."+(nthPatient+nthStudy)+".3").trim();
	}

/////////////////////////////////////////////////NAVIGATE////////////////////////////////////////
	public void navigateToDataBasketPage() {
		selenium.click("MAINbody:dataForm:viewBasketButton");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:basketForm:dataBasketDataTable')",
                                  "30000");
	}

	public void navigateToSavedQueriesPage() {
		selenium.click("link=View Saved Queries");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:queryForm:savedQueryMode:savedQueryModeDataTable')",
                                  "30000");
	}

	public void navigateToSearchPage() {
		selenium.click("MAINbody:navigationForm:searchLink");

		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:searchForm:modalityCheckboxesTable')",
                                  "30000");
	}

	public void navigateToEditFirstSavedQuery() {
		//the :0: means first row in table
		//forceId from myfaces might make this more stable
		selenium.click("MAINbody:queryForm:savedQueryMode:savedQueryModeDataTable:0:editQueryButton");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:searchForm')",
                                  "30000");
	}

	public void navigateToISPYPortal(String image1Label,
			                         String image1PatientId,
			                         String image1StudyInstanceUid,
			                         String image1SeriesInstanceUid,
			                         String image1ImageSopInstanceUid,
			                         String image2Label,
			                         String image2PatientId,
			                         String image2StudyInstanceUid,
			                         String image2SeriesInstanceUid,
			                         String image2ImageSopInstanceUid) {

		/*selenium.open("/ncia/referencedImages.jsf?source=ISPY&image1Label="+image1Label+
        "&image1TrialId=ISPY&"+
        "image1PatientId="+image1PatientId+
        "&image1StudyInstanceUid="+image1StudyInstanceUid+
        "&image1SeriesInstanceUid="+image1SeriesInstanceUid+
        "&image1ImageSopInstanceUid="+image1ImageSopInstanceUid +
        "&image1dataName=Patient%20Id" +
        "&image1dataValue=2" +
        "&image1dataName=Baseline%20Morphology" +
        "&image1dataValue=3=Area%20enhancement%20with%20irregular%20margins%20-%20with%20nodularity&image1dataName=Longest%20Diameter_PCT_CHANGE_T1-T2" +
        "&image1dataValue=-10.34" +
        "&image1dataName=Clinical%20Response_T1-T2" +
        "&image1dataValue=3=Stable%20Disease" +
        "&image2StudyInstanceUid=" + image2StudyInstanceUid +
        "&image2SeriesInstanceUid=" + image2SeriesInstanceUid +
        "&image2ImageSopInstanceUid=" + image2ImageSopInstanceUid+
        "&image2Label=" + image2Label +
        "&image2TrialId=ISPY" +
        "&image2PatientId=" + image2PatientId +
        "&image2dataName=Patient%20Id&image2dataValue=2&image2dataName=Baseline%20Morphology" +
        "&image2dataValue=3=Area%20enhancement%20with%20irregular%20margins%20-%20with%20nodularity" +
        "&image2dataName=Longest%20Diameter_PCT_CHANGE_T1-T4&image2dataValue=-44.83" +
        "&image2dataName=Clinical%20Response_T1-T4" +
        "&image2dataValue=3=Stable%20Disease");*/

		selenium.open("/ncia/referencedImages.jsf?source=ISPY&image1Label="+image1Label+
		              "&image1TrialId=ISPY&image1PatientId=2&image1StudyInstanceUid=1.2.124.113532.192.9.54.60.20020702.141304.3659576&image1SeriesInstanceUid=1.2.840.113619.2.5.1762805546.3105.1025559471.58&image1ImageSopInstanceUid=1.2.840.113619.2.5.1762805546.3105.1025559471.276&image1dataName=Patient%20Id&image1dataValue=2&image1dataName=Baseline%20Morphology&image1dataValue=3=Area%20enhancement%20with%20irregular%20margins%20-%20with%20nodularity&image1dataName=Longest%20Diameter_PCT_CHANGE_T1-T2&image1dataValue=-10.34&image1dataName=Clinical%20Response_T1-T2&image1dataValue=3=Stable%20Disease&image2StudyInstanceUid=1.2.124.113532.192.9.54.60.20021230.122345.403213&image2SeriesInstanceUid=1.2.840.113619.2.5.1762805546.2376.1041867495.89&image2ImageSopInstanceUid=1.2.840.113619.2.5.1762805546.2376.1041867495.189&image2Label=Post&image2TrialId=ISPY&image2PatientId=2&image2dataName=Patient%20Id&image2dataValue=2&image2dataName=Baseline%20Morphology&image2dataValue=3=Area%20enhancement%20with%20irregular%20margins%20-%20with%20nodularity&image2dataName=Longest%20Diameter_PCT_CHANGE_T1-T4&image2dataValue=-44.83&image2dataName=Clinical%20Response_T1-T4&image2dataValue=3=Stable%20Disease");

		selenium.type("MAINbody:sideBarView:loginForm:uName2", "nciadevtest");
		selenium.type("MAINbody:sideBarView:loginForm:pass2", "saicT3@m16");
		selenium.click("MAINbody:sideBarView:loginForm:loginButton2");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:imageForm:imageTable')",
                                  "30000");
	}


	public void navigateToSubmissionReports() {
		selenium.click("SUBmenu:sideMenuForm:adminToolsView:feedbackView:submissionReportMenuItem");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:submissionReportCriteriaForm:collectionSiteMenu')",
                                  "30000");
	}

	public void expandAnnotationSubmissionReportDailyDetails(int nthDay) {
		String patientDetailsTable =
			"MAINbody:annotationByDayForm:annotationByDayTable:"+nthDay+":patientDetailsTable";

		selenium.click("MAINbody:annotationByDayForm:annotationByDayTable:"+nthDay+":expand");

		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('"+patientDetailsTable+"')",
                                  "30000");
	}

	//both are 0 based
	public void expandOrCollapsePatientInAnnotationSubmissionReportDailyDetails(int nthDay, int nthPatient) {
		String patientDetailsTable =
			"MAINbody:annotationByDayForm:annotationByDayTable:"+nthDay+":patientDetailsTable";

		selenium.click(patientDetailsTable+":"+nthPatient+":expandContractImage");
		pause(30000);  //alternative is to wait for count of table rows to increase. this is lazy but easy
	}

	//expand study?
	//selenium.click(patientDetailsTable+":1:expandContractImage");
	////pause(30000);  //alternative is to wait for count of table rows to increase. this is lazy but easy


}