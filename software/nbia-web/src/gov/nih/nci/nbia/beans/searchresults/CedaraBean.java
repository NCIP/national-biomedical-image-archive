/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchresults;

import gov.nih.nci.nbia.basket.BasketSeriesItemBean;
import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.basket.BasketBean;
import gov.nih.nci.nbia.beans.security.AnonymousLoginBean;
import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.util.CedaraUtil;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.util.Collection;
import java.util.List;

public class CedaraBean {

	public static final String visualizeImageTag = "MAINbody:basketForm:visualizeImages";
	
    public String getIrwVersion() {
        return NCIAConfig.getMappedIRWVersion();
    }


    public String getIrwLink() {
        return NCIAConfig.getMappedIRWLink();
    }


	/**
	 * Check whether the Cedara I-Response Workstation is running or not.
	 */
	public Boolean getIsIRWAvailable() {
		return this.isIRWAvailable;
	}


	/**
	 * Get the Cedara IGS information from property file
	 */
	public String getHost() {
		String hostAddress = NCIAConfig.getCedaraIGSAddress();
		String hostPort = NCIAConfig.getCedaraIGSPort();
		return "http://" + hostAddress + ":" + hostPort;
	}


	/**
	 * Get current user name
	 */
	public String getUser() {
		SecurityBean secure = BeanManager.getSecurityBean();
		return secure.getUsername();
	}


    public String getUid() {
        return uid;
    }


    public void setUid(String uid) {
    	this.uid = uid;
    }


    /**
     * Get the select uid and redirect to check IRW page.
     */
    public String visualizeSelectedSeries() {
    	fromDataBasket = false;
    	String dataFormVisualizeImageTag = "MAINbody:dataForm:visualizeImages";
        //if guest user, give them warning that they need to login to use this feature
        AnonymousLoginBean anonymousLoginBean = BeanManager.getAnonymousLoginBean();
		if(anonymousLoginBean.getGuestLoggedIn()){
			//set message here
			String warningText = dataFormVisualizeImageTag;
			MessageUtil.addErrorMessage(warningText,
			                            "visulizeImagesForGuest");
			return null;
		}
		if (countSelectedSeriesWhichImageLessThan4(studiesSearchResultBean.getStudyResults()))
		{
			String warningText = dataFormVisualizeImageTag;
			MessageUtil.addErrorMessage(warningText,
			                            "imageLessThan4");
			return null;
		}
		
		uid = CedaraUtil.constructUidParameterString(studiesSearchResultBean.getStudyResults());

        return "checkIRW";
    }

    private boolean countSelectedSeriesWhichImageLessThan4(List<StudyResultWrapper> studyResults)
    {
    	boolean lessThan4 = false;
    	int count = 0;
    	
    	 for (int i = 0; i < studyResults.size(); i++) 
    	 {
    		 StudyResultWrapper curr = studyResults.get(i);
             List<SeriesResultWrapper> sList = curr.getSeriesResults();
             for (int j = 0; j < sList.size(); j++) 
             {
             	SeriesResultWrapper item = sList.get(j);
                 if (item.isChecked()) 
                 {
                	 count++;
                	 SeriesSearchResult series = item.getSeries();
                     if (series.getNumberImages() < 4)
                    {
                    	lessThan4 = true;
                    }
                 }
             }
    	 }
    	 if (count <= 1)
    	 {
    		 return false;
    	 }
    	 
    	return lessThan4;
    }
    
    public boolean isLocal() {
    	PatientSearchResult patientSearchResult = BeanManager.getSearchResultBean().getPatient();
    	if(patientSearchResult!=null) { //went to basket page before search
    		return patientSearchResult.associatedLocation().isLocal();
    	}
    	else {
    		return true;
    	}
    }


    /**
     * This property returns a URL to the "portal" markup servlet.
     * Cedara I-Response is passed this in checkIRW.xhtml.  This
     * enables I-Response to talk directly to our markup servlets
     * to read and save markup
     **/
    public String getArchiveValue() {
        // get JBoss url from external properties
        String url = NCIAConfig.getJBossPublicUrl()+"MarkupQuery";
        System.out.println(url);
        return url;
    }


    /**
     * Was Cedara initiated from the data basket (true) or from the study
     * results page (false).
     */
    public boolean isFromBasket() {
    	return fromDataBasket;
    }

    /**
     * Get the select uid and redirect to check IRW page.
     */
    public String initiateCedaraAgainstSelectedBasketItems() throws Exception {
    	fromDataBasket = true;

        AnonymousLoginBean anonymousLoginBean = BeanManager.getAnonymousLoginBean();
        if(anonymousLoginBean.getGuestLoggedIn()){
        	//set message here
            String warningText = visualizeImageTag;
            MessageUtil.addErrorMessage(warningText,
                                        "visulizeImagesForGuest");
            return null;
        }

        Collection<BasketSeriesItemBean> basketItems = basketBean.getSeriesItems();
        if(CedaraUtil.containsRemoteSeries(basketItems)) {
            String warningText = visualizeImageTag;
            MessageUtil.addErrorMessage(warningText,
                                        "remoteNode");
            return null;
        }
        if(CedaraUtil.containsMultiplePatients(basketItems)) {
            String warningText = visualizeImageTag;
            MessageUtil.addErrorMessage(warningText,
                                          "multiplePatient");
            return null;
        }
        if (CedaraUtil.containsSeriesWhichImageLessThanFour(basketItems))
        {
        	 String warningText = visualizeImageTag;
             MessageUtil.addErrorMessage(warningText,
                                           "imageLessThan4");
             return null;
        }
        uid = CedaraUtil.constructUidParameterString(basketItems);


        setUid(uid);

        return "checkIRW";
    }


    /**
     * For dependency injection
     */
	public StudiesSearchResultBean getStudiesSearchResultBean() {
		return studiesSearchResultBean;
	}


    /**
     * For dependency injection
     */
	public void setStudiesSearchResultBean(StudiesSearchResultBean studiesSearchResultBean) {
		this.studiesSearchResultBean = studiesSearchResultBean;
	}


    /**
     * For dependency injection
     */
	public BasketBean getBasketBean() {
		return basketBean;
	}


    /**
     * For dependency injection
     */
	public void setBasketBean(BasketBean basketBean) {
		this.basketBean = basketBean;
	}

	/////////////////////////////////////PRIVATE//////////////////////////////////

	private boolean fromDataBasket;

    private StudiesSearchResultBean studiesSearchResultBean;

    private BasketBean basketBean;

    private Boolean isIRWAvailable = true;

    private String uid;
}