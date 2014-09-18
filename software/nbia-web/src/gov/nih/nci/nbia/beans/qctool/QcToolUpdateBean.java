/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.qctool;

import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.populator.ViewSeriesPopulatorQCBean;
import gov.nih.nci.nbia.beans.searchresults.DefaultThumbnailURLResolver;
import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.dao.CustomSeriesListDAO;
import gov.nih.nci.nbia.dao.QcStatusDAO;
import gov.nih.nci.nbia.dicomtags.LocalDicomTagViewer;
import gov.nih.nci.nbia.dto.QcCustomSeriesListDTO;
import gov.nih.nci.nbia.dto.QcSearchResultDTO;
import gov.nih.nci.nbia.mail.MailManager;
import gov.nih.nci.nbia.qctool.VisibilityStatus;
import gov.nih.nci.nbia.search.LocalDrillDown;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.SlideShowUtil;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.util.StringUtil;
import gov.nih.nci.nbia.util.Util;
import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.APIURLHolder;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultEx;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

/**
 * This bean is used to update the QC status
 */
public class QcToolUpdateBean {

	/**
	 * @return the selectedQcStatus
	 */
	public String getSelectedQcStatus() {
		return selectedQcStatus;
	}

	/**
	 * @param selectedQcStatus
	 *            the selectedQcStatus to set
	 */
	public void setSelectedQcStatus(String selectedQcStatus) {
		this.selectedQcStatus = selectedQcStatus;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the currDicomSopId
	 */
	public String getCurrDicomSopId() {
		return currDicomSopId;
	}

	/**
	 * @param currDicomSopId
	 *            the currDicomSopId to set
	 */
	public void setCurrDicomSopId(String currDicomSopId) {
		this.currDicomSopId = currDicomSopId;
	}

	/**
	 * @return the tagInfo
	 */
	public List<DicomTagDTO> getTagInfo() {
		return tagInfo;
	}

	/**
	 * @param tagInfo
	 *            the tagInfo to set
	 */
	public void setTagInfo(List<DicomTagDTO> tagInfo) {
		this.tagInfo = tagInfo;
	}

	/**
	 * This gets list for search result displayed in QC serarch page
	 */
	public List<QcSearchResultDTO> getQsrDTOList() {
		List<QcSearchResultDTO> qsrDTOList = qcToolSearchBean.getQsrDTOList();
		return qsrDTOList;
	}

	public String toggleDescriptionPopup() {
	    	descriptionPopupRendered = false;
	    	return null;
    }

    public boolean getDescriptionPopupRendered() {
	    	return this.descriptionPopupRendered;
    }

	private boolean isErrorFree(String type) {
		if (type.equals(BULK)){
		if (StringUtil.isEmptyTrim(selectedQcStatus)) {
			MessageUtil.addErrorMessage(
					"MAINbody:qcToolForm:slctQcStatusUpdate", REQUIRED_FIELD);
			return false;
		}
		if (comments.length()>= 4000) {
			MessageUtil.addErrorMessage(
					"MAINbody:qcToolForm:slctQcStatusUpdate", COMMENTS_TOO_LONG);
			return false;
		}
		}
		else {
			if (StringUtil.isEmptyTrim(selectedQcStatusSingle)) {
				MessageUtil.addErrorMessage(
						"MAINbody:qcToolViewerForm:slctQcStatus", REQUIRED_FIELD);
				return false;
			}
			if (comments.length()>= 4000) {
				MessageUtil.addErrorMessage(
						"MAINbody:qcToolViewerForm:slctQcStatus", COMMENTS_TOO_LONG);
				return false;
			}
		}
		return true;
	}

	/**
	 * This action is called when the update button is clicked.
	 */
	public String update() throws Exception {
		List<QcSearchResultDTO> qsrDTOList = qcToolSearchBean.getQsrDTOList();
		List<String> seriesCheckList = new ArrayList<String>();

		qcToolSearchBean.setIfNotClickedSubmit(true);
		if (qcToolSearchBean.getQcToolBean().isSuperRole()) {
			selectedQcStatus = DELETE;
			qcToolSearchBean.setIfNotClickedSubmit(true);
		}

		if (!isErrorFree(BULK)){
			return null;
		}
		if (qsrDTOList != null) {
			newQsrDTOList = new ArrayList<QcSearchResultDTO>();
			seriesList.clear();
			statusList.clear();
			for (int i = 0; i < qsrDTOList.size(); ++i) {
				QcSearchResultDTO aDTO = qsrDTOList.get(i);
				newQsrDTOList.add(i, new QcSearchResultDTO(aDTO));
				if (aDTO.isSelected()) {
					seriesList.add(aDTO.getSeries());
					statusList.add(aDTO.getVisibility());
					if (resultAndSelectedStatusIsVisible(aDTO, selectedQcStatus)) {
						seriesCheckList.add(aDTO.getSeries());
					}
					newQsrDTOList.get(i).setVisibility(
							VisibilityStatus.stringStatusFactory(
									selectedQcStatus).getNumberValue()
									.toString());
					newQsrDTOList.get(i).setSelected(false);
					qsrDTOList.get(i).setSelected(false);
				}
			}
		}
		if (seriesList.size() == 0) {
			MessageUtil.addErrorMessage("MAINbody:qcToolForm:SlctRec",
					ERRORMSG_RPT);
			return null;
		}
		if (seriesCheckList.size() > 0) {
			userNameList = findCustomerListInfo(seriesCheckList);
			if (!Util.isEmptyCollection(userNameList)) {
				searchPgPopupRendered = true;
				return null;
			}
		}

		String newStatus = VisibilityStatus.stringStatusFactory(
				selectedQcStatus).getNumberValue().toString();
		doUpdate(seriesList, statusList, newStatus);
		qcToolSearchBean.setQsrDTOList(newQsrDTOList);
		newQsrDTOList = null;
		return null;
	}

	private static boolean resultAndSelectedStatusIsVisible(QcSearchResultDTO aDTO, String selectedQcStatus) {
		return aDTO.getVisibility().equals(VISIBLENUM)&& !selectedQcStatus.equals(VISIBLE);
	}


	private void doUpdate(List<String> seriesList, List<String> statusList,
			String newStatus) {

		SecurityBean secure = BeanManager.getSecurityBean();
		if (comments.equals(INITIAL_COMMENT)) {
			comments = "";
		}
		QcStatusDAO qsDao = (QcStatusDAO)SpringApplicationContext.getBean("qcStatusDAO");
		qsDao.updateQcStatus(seriesList, statusList, newStatus, secure
				.getUsername(), comments);
		comments = INITIAL_COMMENT;
		selectedQcStatus = null;
	}

	private List<QcCustomSeriesListDTO> findCustomerListInfo(
			List<String> seriesCheckList) {
		CustomSeriesListDAO cslDao = (CustomSeriesListDAO)SpringApplicationContext.getBean("customSeriesListDAO");
		List<QcCustomSeriesListDTO> rList = cslDao
				.findSharedListBySeriesInstanceUids(seriesCheckList);
		if (rList != null && rList.size() > 0) {
			List<QcCustomSeriesListDTO> cList = new ArrayList<QcCustomSeriesListDTO>();
			for (int i = 0; i < rList.size(); ++i) {
				cList.add(rList.get(i));
			}
			return cList;
		} else {
			return null;
		}
	}

	/**
	 * This action is called when the update button is clicked.
	 */
	public String skipToNext(){
		int listSize = qcToolSearchBean.getQsrDTOList().size();

		if (selectedRow+1 < listSize) {
			++selectedRow;
			seriesId = qcToolSearchBean.getQsrDTOList().get(selectedRow).getSeries();
		}
		imageCount = 0;
		currentImgNum ="0";
		selectedImgNumField = "1";
		updateParam(seriesId);
		return "qcToolSlideShow";
	}

	/**
	 * This action is called when the update button is clicked.
	 */
	public String updateSingle() throws Exception {
		if (qcToolSearchBean.getQcToolBean().isSuperRole()) {
			selectedQcStatusSingle = DELETE;
		}

		if (!isErrorFree(SINGLE)){
			return null;
		}

		seriesList.clear();
		statusList.clear();
		List<QcSearchResultDTO> qsrDTOList = qcToolSearchBean.getQsrDTOList();
		String visibility = qsrDTOList.get(selectedRow).getVisibility();
		List<String> seriesCheckList = new ArrayList<String>();
		seriesId = qsrDTOList.get(selectedRow).getSeries();
		seriesList.add(seriesId);
		statusList.add(visibility);

		if (visibility.equals(VISIBLENUM)
				&& (!selectedQcStatusSingle.equals(VISIBLE))) {
			seriesCheckList.add(seriesId);
			userNameList = findCustomerListInfo(seriesCheckList);
			if (userNameList != null && userNameList.size() > 0) {
				popupRendered = true;
				return null;
			}
		}
		String newStatus = VisibilityStatus.stringStatusFactory(
				selectedQcStatusSingle).getNumberValue().toString();
		doUpdate(seriesList, statusList, newStatus);
		qsrDTOList.get(selectedRow).setVisibility(newStatus);
		++selectedRow;

		if (selectedRow == qsrDTOList.size()) {
			--selectedRow;
			qsrDTOList.get(selectedRow).setVisibility(newStatus);

		} else {
			seriesId = qsrDTOList.get(selectedRow).getSeries();
		}
		qcToolSearchBean.updateStatus();
		comments = INITIAL_COMMENT;
		selectedQcStatusSingle = null;
		imageCount = 0;
		currentImgNum ="0";
		selectedImgNumField = "1";
		updateParam(seriesId);
		return "qcToolSlideShow";

	}

	/**
	 * Method to wire in the other bean that contains the input controls/values
	 */
	public void setQcToolSearchBean(QcToolSearchBean qcToolSearchBean) {
		this.qcToolSearchBean = qcToolSearchBean;

	}

	public String getImageSeriesJavascript() {
		String javaScriptbits = null;
		List<String> seriesInstanceUids = new ArrayList<String>();

		seriesInstanceUids.add(seriesId);
		getSeriesInfo(seriesId);
		qcToolSearchBean.requestRpt(seriesId);
		if (selectedRow == 0 && qcToolSearchBean.getQsrDTOList().size() == 1) {
			lastRecord = true;
			if (qcToolSearchBean.getQcToolBean().isSuperRole()) {
				buttonLabel = DELETE;
			} else {
				buttonLabel = "Update";
			}
		} else if (selectedRow + 1 == qcToolSearchBean.getQsrDTOList().size()) {
			lastRecord = true;
			if (qcToolSearchBean.getQcToolBean().isSuperRole()) {
				buttonLabel = DELETE;
			} else {
				buttonLabel = "Update";
			}
		} else {
			lastRecord = false;
			if (qcToolSearchBean.getQcToolBean().isSuperRole()) {
				buttonLabel = "Delete/Next Series";
			} else {
				buttonLabel = "Update/Next Series";
			}
		}
		
		imageCount = Integer.parseInt(currentImgNum) - 1;
		if (imageCount < 0) {
			imageCount = 0;
		}	

		try {
			LocalDrillDown drillDown = new LocalDrillDown();
			drillDown
					.setThumbnailURLResolver(new DefaultThumbnailURLResolver());
            
			if (isHasMultiFrame()) {
				List<ImageSearchResultEx> imageList = Arrays.asList(drillDown
						.retrieveImagesForSeriesEx(seriesId));
				javaScriptbits = SlideShowUtil.getImageSeriesJavascriptEx(imageList, Integer.parseInt(getSelectedImgNumField())-1);
				LocalDicomTagViewer ldtv = new LocalDicomTagViewer();
				tagInfo = ldtv.viewDicomHeader(imageList.get(Integer.parseInt(getSelectedImgNumField())-1).getId());
				currentSeriesSize = imageList.size();
				createLink(imageList.get(imageCount));
			}else {
				
				
				List<ImageSearchResult> imageList = Arrays.asList(drillDown
						.retrieveImagesForSeries(seriesId));
				javaScriptbits = SlideShowUtil.getImageSeriesJavascript(imageList);
				LocalDicomTagViewer ldtv = new LocalDicomTagViewer();
				tagInfo = ldtv.viewDicomHeader(imageList.get(imageCount).getId());
				currentSeriesSize = imageList.size();
				createLink(imageList.get(imageCount));
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return javaScriptbits;
	}

	public String getSeriesId() {
		return this.seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}

	public void getSeriesInfo(String seriesId) {
		List<QcSearchResultDTO> slist = getQsrDTOList();
		for (int i = 0; i < slist.size(); ++i) {
			if (slist.get(i).getSeries().equals(seriesId)) {
				selectedRow = i;
				break;
			}
		}
		setHasMultiFrame(slist.get(selectedRow).getModality().equalsIgnoreCase("US"));
	}

	public void populate(String seriesId) {
		if (!seriesId.equals(this.seriesId)){
			currentImgNum = "0";
			selectedImgNumField = "1";
		}
		setSeriesId(seriesId);
	}

	public int getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}

	public String getSelectedQcStatusSingle() {
		return selectedQcStatusSingle;
	}

	public void setSelectedQcStatusSingle(String selectedQcStatusSingle) {
		this.selectedQcStatusSingle = selectedQcStatusSingle;
	}

	public String getButtonLabel() {
		return buttonLabel;
	}

	public void setButtonLabel(String buttonLabel) {
		this.buttonLabel = buttonLabel;
	}

	public String getCurrentImgNum() {
		return currentImgNum;
	}

	public void setCurrentImgNum(String currentImgNum) {
		this.currentImgNum = currentImgNum;
	}

	public void cancel() {
		searchPgPopupRendered = false;
		popupRendered = false;
		newQsrDTOList = null;
	}

	public void continueUpdate() {
		String newStatus = VisibilityStatus.stringStatusFactory(
				selectedQcStatus).getNumberValue().toString();
		doUpdate(seriesList, statusList, newStatus);
		qcToolSearchBean.setQsrDTOList(newQsrDTOList);
		searchPgPopupRendered = false;
		newQsrDTOList = null;
		imageCount= 0;
		sendMail();
	}

	public String continueUpdateSingle() {
		String newStatus = VisibilityStatus.stringStatusFactory(
				selectedQcStatusSingle).getNumberValue().toString();
		doUpdate(seriesList, statusList, newStatus);
		popupRendered = false;
		List<QcSearchResultDTO> qsrDTOList = qcToolSearchBean.getQsrDTOList();
		qsrDTOList.get(selectedRow).setVisibility(newStatus);
		++selectedRow;

		if (selectedRow == qsrDTOList.size()) {
			--selectedRow;
			qsrDTOList.get(selectedRow).setVisibility(newStatus);

		} else {
			seriesId = qsrDTOList.get(selectedRow).getSeries();
		}
		qcToolSearchBean.updateStatus();

		selectedQcStatusSingle = null;
		imageCount = 0;
		currentImgNum ="0";
		selectedImgNumField = "1";
		sendMail();
		updateParam(seriesId);
		return "qcToolSlideShow";
	}

	private void updateParam(String seriesId){
		ViewSeriesPopulatorQCBean viewSeriesPopulatorQCBean = BeanManager.getViewSeriesPopulatorQCBean();
		viewSeriesPopulatorQCBean.setSeriesId(seriesId);
	}

	private void sendMail(){
		SecurityBean sb = BeanManager.getSecurityBean();
        String email = sb.getEmail();
        StringBuffer userInfo=new StringBuffer();
        Set<String> set = new HashSet<String>();
        Set<String> userInfoSet = new HashSet<String>();
        for (QcCustomSeriesListDTO aDTO : userNameList){
        	set.add(aDTO.getSeriesInstanceUID());
        	userInfoSet.add(aDTO.getName()+"\t"+ aDTO.getEmail());
        }
        for (String s : userInfoSet) {
        	userInfo.append(s).append(", ");
        }
        StringBuffer impactList = new StringBuffer();
        for (String series : set) {
        	impactList.append(series).append(", ");
        }
        MailManager.sendQCShareListEmail(email, impactList.toString(), userInfo.toString());

	}

	public boolean isPopupRendered() {
		return popupRendered;
	}

	public void setPopupRendered(boolean popupRendered) {
		this.popupRendered = popupRendered;
	}

	public List<QcCustomSeriesListDTO> getUserNameList() {
		return userNameList;
	}

	public void setUserNameList(List<QcCustomSeriesListDTO> userNameList) {
		this.userNameList = userNameList;
	}

	public boolean isSearchPgPopupRendered() {
		return searchPgPopupRendered;
	}

	public void setSearchPgPopupRendered(boolean searchPgPopupRendered) {
		this.searchPgPopupRendered = searchPgPopupRendered;
	}
	public boolean isLastRecord() {
		return lastRecord;
	}

	public void setLastRecord(boolean lastRecord) {
		this.lastRecord = lastRecord;
	}

	public int getImageCount() {
		return imageCount;
	}

	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}
	
	public boolean isHasMultiFrame() {
		return hasMultiFrame;
	}

	public void setHasMultiFrame(boolean hasMultiFrame) {
		this.hasMultiFrame = hasMultiFrame;
	}
	
	
	public List<SelectItem> getImgNumItems() {
		if (isHasMultiFrame()) {
				imgNumItems = new ArrayList<SelectItem>();
				for (int j = 0; j < currentSeriesSize; ++j)
					imgNumItems.add(j,
							new SelectItem(String.valueOf(j + 1),"Image #" + String.valueOf(j + 1)));

				return imgNumItems;
		}
		else
			return null;
	}

	public void setImgNumItems(List<SelectItem> imgNumItems) {
		this.imgNumItems = imgNumItems;
	}


	public String getSelectedImgNumField() {
		return selectedImgNumField;
	}

	public void setSelectedImgNumField(String selectedImgNumField) {
		this.selectedImgNumField = selectedImgNumField;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

    private void createLink(ImageSearchResult imageSearchResult)
    {
        SecurityBean secure = BeanManager.getSecurityBean();
        String userName = secure.getUsername();
    	String url = APIURLHolder.getUrl()+"/nbia-api/services/o/wado?contentType=application/dicom&objectUID="+
    	imageSearchResult.getSopInstanceUid()+"&oviyamId="+APIURLHolder.addUser(userName)+
		"&wadoUrl="+APIURLHolder.getWadoUrl();
		setImageLink(url);
    }


	// //////////////////////////PRIVATE///////////////////////////////////////
	private static final String DELETE = "Delete";
	private static final String VISIBLE = VisibilityStatus.VISIBLE.getText();
	private static final String VISIBLENUM = VisibilityStatus.VISIBLE
			.getNumberValue().toString();
	private static final String REQUIRED_FIELD = "qcTool_requiedField_Update";
	private static final String ERRORMSG_RPT = "qcTool_requiedSeries";
	private static final String COMMENTS_TOO_LONG = "qcTool_logTooLong";
	private QcToolSearchBean qcToolSearchBean;
	private String selectedQcStatus = null;
	private String selectedQcStatusSingle = null;
	private static final String INITIAL_COMMENT = "Enter change log here...";
	private String comments = INITIAL_COMMENT;
	private String currDicomSopId;
	private List<DicomTagDTO> tagInfo;
	private String imageLink;
	private String seriesId;
	private int selectedRow;
	private int imageCount = 0;
	private String buttonLabel = "Update/Next Series";
	private String currentImgNum = "0";
	private boolean popupRendered = false;
	private boolean lastRecord=false;
	private boolean descriptionPopupRendered = false;
	private boolean searchPgPopupRendered = false;
	private List<QcCustomSeriesListDTO> userNameList;
	private List<QcSearchResultDTO> newQsrDTOList = null;
	private List<String> seriesList = new ArrayList<String>();
	private List<String> statusList = new ArrayList<String>();
	private static final String BULK="BulkUpdate";
	private static final String SINGLE="SingleUpdate";
	private boolean hasMultiFrame = false;
	private List<SelectItem> imgNumItems;
	private int currentSeriesSize = 0;
	private String selectedImgNumField = "1";
}