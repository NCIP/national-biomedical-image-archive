/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/*
 * BasketBean.java
 *
 * Created on August 15, 2005, 1:23 PM
 */
package gov.nih.nci.nbia.beans.basket;

import gov.nih.nci.nbia.basket.Basket;
import gov.nih.nci.nbia.basket.BasketSeriesItemBean;
import gov.nih.nci.nbia.basket.BasketUtil;
import gov.nih.nci.nbia.basket.DownloadRecorder;
import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.searchresults.DefaultThumbnailURLResolver;
import gov.nih.nci.nbia.beans.searchresults.ImageResultWrapper;
import gov.nih.nci.nbia.beans.searchresults.PatientResultWrapper;
import gov.nih.nci.nbia.beans.security.AnonymousLoginBean;
import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.customserieslist.FileGenerator;
import gov.nih.nci.nbia.datamodel.IcefacesRowColumnDataModel;
import gov.nih.nci.nbia.datamodel.IcefacesRowColumnDataModelInterface;
import gov.nih.nci.nbia.jms.ImageZippingMessage;
import gov.nih.nci.nbia.search.DrillDown;
import gov.nih.nci.nbia.search.DrillDownFactory;
import gov.nih.nci.nbia.search.LocalDrillDown;
import gov.nih.nci.nbia.util.DynamicJNLPGenerator;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.NCIAConstants;
import gov.nih.nci.nbia.util.SlideShowUtil;
import gov.nih.nci.nbia.zip.ZipManager;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.NBIANode;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpServletRequest;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import com.icesoft.faces.async.render.SessionRenderer;
import com.icesoft.faces.context.ByteArrayResource;


/**
 * This is a session scope bean that holds the items that are placed into the
 * DataBasket.
 *
 * @author piparom, prashant, Andrew Shinohara
 */
public class BasketBean implements Serializable, IcefacesRowColumnDataModelInterface {

    public static final String HIGHLIGHTED = "highlightedData";
    public static final String NOT_HIGHLIGHTED = "";

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "java:/queue/imageQueue")
    private Queue queue;

    /**
     *
     * Creates a new instance of BasketBean. Reads configuration and other data
     * that only needs to be set once
     */
    public BasketBean() {
        // Get username from security bean in order to create smart filename
        sb = BeanManager.getSecurityBean();

        ftpThreshold = NCIAConfig.getFtpThreshold();
        basket.addBasketChangeListener(basketChangeListener);

		SessionRenderer.addCurrentSession("all");

    }

    public Basket getBasket() {
    	return this.basket;
    }

    /**
     * Returns whether or not this is a FTP sized download.
     */
    public boolean getFtpDownload() {
        return ftpDownload;
    }

    /**
     * Returns the sorted list of BasketSeriesItemBeans in the cart.
     */
    public List<BasketSeriesItemBean> getSeriesItems() {
    	return basket.getSeriesItems();
    }

    public List<BasketSeriesItemBean> getSortedSeriesItems() {
    	List<BasketSeriesItemBean> toSort = basket.getSeriesItems();
    	System.out.println("sortColumnName is - "+sortColumnName);
    	// if (!oldSort.equals(sortColumnName) ||
        //     oldAscending != ascending) {
    		 sort(toSort);
        //     oldSort = sortColumnName;
        //     oldAscending = ascending;
        //}
    	return toSort;
    }

	/**
	 * For a given series ID, tell whether that patient has been added to the basket.
	 * This is a bit awkward but necessary for execution through EL which doesnt allow
	 * parameterized accessors.
	 *
	 * <p>investigate more efficient method to hold the map
	 */
    public Map<String, Boolean> getInBasketMap() {
    	Map<String, Boolean> inBasketMap = new HashMap<String, Boolean>();
    	Map<String, BasketSeriesItemBean> seriesItemMap = basket.getSeriesItemMap();
    	for(BasketSeriesItemBean seriesItem : seriesItemMap.values()) {
    		inBasketMap.put(seriesItem.getSeriesPkId()+"||"+seriesItem.getGridLocation(), Boolean.TRUE);
    	}
    	return inBasketMap;
    }


	/**
	 * For a given patient ID, tell whether that patient has been added to the basket.
	 * This is a bit awkward but necessary for execution through EL which doesnt allow
	 * parameterized accessors.
	 *
	 * <p>investigate more efficient method to hold the map
	 */
    public Map<String, Boolean> getPatientInBasketMap() {
    	Map<String, Boolean> inBasketMap = new HashMap<String, Boolean>();
    	Map<String, BasketSeriesItemBean> seriesItemMap = basket.getSeriesItemMap();
    	for(BasketSeriesItemBean seriesItem : seriesItemMap.values()) {
    		inBasketMap.put(seriesItem.getPatientpk()+"||"+seriesItem.getGridLocation(), Boolean.TRUE);
    	}
    	return inBasketMap;
    }


    /**
     * Called when you press the remove button in the data basket. Removes the
     * selected series.
     *
     */
    public void removeSelectedSeries() {
        //basket.removeSelectedSeries();
        basket.removeSelectedSeries(toDelete);
        ftpDownload = (getDownloadType().equals(FTP_DOWNLOAD) ? true:false);
        logger.debug("ftpDownload="+ftpDownload);
    }

    private String toDelete;
    public String getToDelete() {
		return toDelete;
	}

    public void removeAllSeries() {
    	basket.removeAllSeries();
    }
	public void setToDelete(String toDelete) {
		this.toDelete = toDelete;
	}
    /**
     * Returns the total number of series in the basket.
     */
    public int getBasketSeriesCount() {
        return basket.getBasketSeriesCount();
    }

    /**
     * Called when you press the download button. Initiates the progress bar
     * that indicates the status of the zipper.
     */
    public String downloadBasket() throws Exception {
        if (basket.isEmpty()) {
            return null;
        }

        String downloadType = getDownloadType();

        if (basketChanged.getLocalValue() != null &&
              basketChanged.getLocalValue().equals("yes")) {
            hasBasketChangedSinceDownload = true;
        }

        // HTTP download
        if (downloadType.equals(BasketBean.HTTP_DOWNLOAD)) {

            path = NCIAConfig.getZipLocation();
            if (hasBasketChangedSinceDownload) {
                downloading = true;
                // create zip file
                zipper = new ZipManager();
                fileName = BasketUtil.generateFileName(sb.getUsername());

                storeZipFileNameInSession();

                // create zippers for local nodes
                //CONVERSION COMPLETED
                Map<String, SeriesSearchResult> localSeriesDTOs = new HashMap<String, SeriesSearchResult>();
                Map<String, BasketSeriesItemBean> seriesItemMap = basket.getSeriesItemMap();
                Set<String> keys = seriesItemMap.keySet();
                for(String key: keys){
                    BasketSeriesItemBean bsib = seriesItemMap.get(key);
                    System.out.println("::downloadBasket:"+bsib.getProject());

                    SeriesSearchResult seriesDTO = bsib.getSeriesSearchResult();
                    localSeriesDTOs.put(seriesDTO.getId().toString(),seriesDTO);
                }
                zipper.setItems(localSeriesDTOs);
                zipper.setTarget(new File(path, fileName));
                zipper.setBreakIntoMultipleFileIfLarge(false);
                zipper.setNoAnnotation(!getIncludeAnnotation());

                zipper.addZipManagerListener(new ZipManagerProgressManager());

                zipper.start();

                DownloadRecorder downloadRecorder = new DownloadRecorder();
                downloadRecorder.recordDownload(zipper.getItems(), sb.getUsername());
            }
            else {
            	downloading = true;
            	hardcodedPercent = 100;
            }

            hasBasketChangedSinceDownload = false;

            return BasketBean.HTTP_DOWNLOAD;
        } else if (hasBasketChangedSinceDownload) {
            ImageZippingMessage izm = new ImageZippingMessage();
            String mqURL = NCIAConfig.getMessagingUrl();
            izm.setEmailAddress(sb.getEmail());
            izm.setUserName(sb.getUsername());

            //add series items from local nodes
            //CONVERSION COMPLETED
            Map<String, SeriesSearchResult> localSeriesDTOs = new HashMap<String, SeriesSearchResult>();
            Map<String, BasketSeriesItemBean> seriesItemMap = basket.getSeriesItemMap();
            Set<String> keys = seriesItemMap.keySet();
            for(String key: keys){
                BasketSeriesItemBean bsib = seriesItemMap.get(key);
                SeriesSearchResult seriesDTO = bsib.getSeriesSearchResult();
                NBIANode node = seriesDTO.associatedLocation();
                //if (node instanceof RemoteNode) {
                //    RemoteNode location = RemoteNode.constructPartialRemoteNode(node.getDisplayName(),node.getURL());
                //    seriesDTO.associateLocation(location);
                //}
                localSeriesDTOs.put(seriesDTO.getId().toString(),seriesDTO);
            }
            izm.setItems(localSeriesDTOs);
            izm.setIncludeAnnotation(getIncludeAnnotation());
            izm.setNodeName(NCIAConfig.getLocalNodeName());
            fileName =  BasketUtil.generateFileName(sb.getUsername());

            // FTP Download
            ftpDownload = true;
            path = NCIAConfig.getFtpLocation();

            //create subfolder for this user
            createFTPUserDir(path);

            izm.setZipFilename(path + File.separator + sb.getUsername() + File.separator + fileName);

			Connection connection = null;
			try {
				Destination destination = queue;

				logger.debug("Sending messages to " + destination );
				connection = connectionFactory.createConnection();
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				MessageProducer messageProducer = session.createProducer(destination);
				connection.start();
				ObjectMessage message = session.createObjectMessage(izm);
				messageProducer.send(message);
				logger.debug("message sent out");

			} catch (JMSException e) {
				e.printStackTrace();
				logger.debug("A problem occurred during the delivery of online deletion message");
				logger.debug("Go your the JBoss Application Server console or Server log to see the error stack trace");
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
        }

            hasBasketChangedSinceDownload = false;

            return BasketBean.FTP_DOWNLOAD;
        }

        return null;
    }



    /**
     * Determines what method (HTTP, or FTP) will be used to obtain the data in
     * the basket
     *
     * @return the download type
     */
    public String getDownloadType() {
        double totalSize = basket.calculateSizeInMB();


        // HTTP download
        if (totalSize < ftpThreshold) {
            return BasketBean.HTTP_DOWNLOAD;
        } else {
            return BasketBean.FTP_DOWNLOAD;
        }

    }

    /**
     * Returns true if based on the current basket size the download would take
     * place via HTTP.
     *
     * @return true if download type is HTTP
     */
    public boolean getSizeBelowHttpDownloadThreshold() {
        return getDownloadType().equals(BasketBean.HTTP_DOWNLOAD);
    }


    /**
     * Called by the Ajax progressBar in order to determine the state of the
     * zipping thread.
     */
    public int getPercentage() {
        if (zipper == null) {
            return hardcodedPercent;
        } else {
            return zipper.getPercentProcessed();
        }
    }

    /**
     * Returns whether or not a file is currently being downloaded.  really this is whether
     * its being zipped, not downloaded
     */
    public boolean getDownloading() {
        return downloading;
    }

    /**
     * Called by the UI when percent reaches 100%
     */
    public String finishDownloading() {
    	this.zipper = null;
    	hardcodedPercent = 0;
        this.downloading = false;
        return null;
    }

    /**
     * Returns a string representing the total size of the image files. Will
     * determine what size increment to represent (GB vs. MB).
     */
    public String getImageSize() {
    	return BasketUtil.getExecSizeString(basket.calculateImageSizeInBytes());
    }



    /**
     * Returns a string representing the total size of the annotation files. Will
     * determine what size increment to represent (GB vs. MB).
     */
    public String getAnnotationSize() {
    	return BasketUtil.getExecSizeString(basket.calculateAnnotationSizeInBytes());
    }

    /**
     * Returns the component associated with the dataTable.
     */
    public UIData getBasketData() {
        return basketData;
    }

    /**
     * Sets the component for the dataTable.
     *
     * @param basketData
     */
    public void setBasketData(UIData basketData) {
        this.basketData = basketData;
    }

    /**
     * Returns a string representation of the time it will take to download via
     * DSL.
     */
    public String getDslDownload() {
    	return BasketUtil.getDslDownload(basket.calculateSizeInBytes());
    }

    /**
     * Returns a string representation of how long it will take to download via
     * T1.
     */
    public String getT1Download() {
    	return BasketUtil.getT1Download(basket.calculateSizeInBytes());
    }

    /**
     * JSF action method called to view the Data Basket. Makes sure that a user
     * is logged in before they can view.
     */
    public String viewBasket() {
        downloading = false;
        ftpDownload = false;

        SecurityBean secure = BeanManager.getSecurityBean();

        if (secure.getLoggedIn()) {
            return "dataBasket";
        } else {
            MessageUtil.addErrorMessage("MAINbody:loginForm:pass",
                "securityDataBasket");
            secure.setLoginFailure(false);
            return "loginFail";
        }
    }


    /**
     * Returns the filename that was created for the download zip file.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return Returns the includeAnnotation.
     */
    public boolean getIncludeAnnotation() {
        return includeAnnotation;
    }

    /**
     * @param includeAnnotation The includeAnnotation to set.
     */
    public void setIncludeAnnotation(boolean includeAnnotation) {
    	this.includeAnnotation = includeAnnotation;
    }

    /**
     * @return Returns the hasBasketChanged.
     */
    public UIInput getBasketChanged() {
        return basketChanged;
    }

    /**
     */
    public void setBasketChanged(UIInput basketChanged) {
        this.basketChanged = basketChanged;
    }


    /**
     * Used by the external data basket.
     */
    public void viewSeriesData(String seriesInstanceUid) throws Exception {

		LocalDrillDown drillDown = new LocalDrillDown();
		drillDown.setThumbnailURLResolver(new DefaultThumbnailURLResolver());

		SeriesSearchResult seriesResult = drillDown.retrieveSeries(seriesInstanceUid);
        this.series = seriesResult;
        thumbnailImageDto = drillDown.retrieveImagesForSeries(seriesResult.getId());

        List<ImageResultWrapper> wrappers = computeWrapperList(Arrays.asList(thumbnailImageDto));
        imageList = new ListDataModel(wrappers);
		icefacesDataModel = new IcefacesRowColumnDataModel(wrappers);
    }

    /**
     * Used by the view data basket to view remote and local images.
     * @param theSeries
     * @throws Exception
     */
    public void viewSeriesData(SeriesSearchResult theSeries) throws Exception {
        try {
            this.series = theSeries;
            DrillDown drillDown = DrillDownFactory.getDrillDown();
            thumbnailImageDto = drillDown.retrieveImagesForSeries(theSeries);
            List<ImageResultWrapper> wrappers= computeWrapperList(Arrays.asList(thumbnailImageDto));
            imageList =  new ListDataModel(wrappers);
            icefacesDataModel = new IcefacesRowColumnDataModel(wrappers);
        }catch (Exception e) {
            System.out.println("inside the exception" );
            e.printStackTrace();
    	}
    }

    /**
     * This does not search on remote node so not using it
     * Used by view series details from data basket
     */
    public String viewSeriesData() throws Exception
    {
        BasketSeriesItemBean bsib = (BasketSeriesItemBean)basketData.getRowData();
        String seriesInstanceUid = bsib.getSeriesId();

        viewSeriesData(seriesInstanceUid);

        return "popupForViewSeriesImage";
    }


    /**
     * Used by view series details from data basket
     */
    public SeriesSearchResult getSeries() {
        return series;
    }

    /**
     * Used by view series details from data basket
     */
    public void setSeries(SeriesSearchResult series) {
        this.series = series;
    }


    /**
     * used by slide show from data basket
     */
    public String getImageSeriesJavascript() {
        return SlideShowUtil.getImageSeriesJavascript(unwrap((List<ImageResultWrapper>)imageList.getWrappedData()));
    }

    /**
     * Used by view series details from data basket
     */
    public DataModel getImageList() {
        return imageList;
    }

    /**
     * Used by view series details from data basket
     */
    public void setImageList(DataModel imageList) {
        this.imageList = imageList;
    }

    /**
     * Used by view series details from data basket
     */
    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    /**
     * Used by view series details from data basket
     */
    public void setResultsPerPage(Integer resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    /**
     * Used by view series details from data basket
     */
    public UIData getImageData() {
        return imageData;
    }

    /**
     * Used by view series details from data basket
     */
    public void setImageData(UIData imageData) {
        this.imageData = imageData;
    }


    public com.icesoft.faces.context.Resource getLaunchDownloadManager() throws Exception{
        long currentTimeMillis = System.currentTimeMillis();
        jnlpFileName = "dynamic-jnlp-" + currentTimeMillis + ".jnlp";
        if(basket.isEmpty()){
            System.out.println("No data in data basket, do not show the download manager");
            return null;
        }

        DynamicJNLPGenerator djnlpg = new DynamicJNLPGenerator();
        String jnlp = djnlpg.generate(sb.getUsername(),
                                      sb.getPassword(),
                                      NCIAConfig.getImageServerUrl()+"/ncia",
                                      NCIAConfig.getDownloadServerUrl(),
                                      getIncludeAnnotation(),
                                      this.getSeriesItems(),currentTimeMillis, NCIAConfig.getNoOfRetry());

        ByteArrayResource bar = new ByteArrayResource(jnlp.getBytes());
        return bar;
    }

    public String getJnlpFileName(){
        return jnlpFileName;
    }

    private String exportFileName;

    public String getExportFileName(){
        return exportFileName;
    }

    /**
     * generate the csv file with the series instance uids in the data basket
     *
     * @throws Exception
     */
    public com.icesoft.faces.context.Resource getExport() throws Exception{
        exportFileName = "seriesInstanceUids" + System.currentTimeMillis() + ".csv";
        if(basket.isEmpty()){
            System.out.println("No data in data basket, do not show the export");
            return null;
        }
        FileGenerator fg = new FileGenerator();

        String exportString = fg.generate(this.getSeriesItems());
        ByteArrayResource bar = new ByteArrayResource(exportString.getBytes());
        return bar;
    }


    public boolean getEnableClassicDownload() {
        return NCIAConfig.getEnableClassicDownload() && isGuestAndFTP();
    }

    public boolean getEnableCreateAList(){
    	Boolean localSeries = getLocalSeriesList();
    	System.out.println("EnableCreateAList...  localSeries: " + localSeries);
        return (sb.getHasLoggedInAsRegisteredUser() && localSeries);
    }
    public boolean getLocalSeriesList(){
    	List<BasketSeriesItemBean> seriesItems = getBasket().getSeriesItems();
    	String localNodeName = NCIAConfig.getLocalNodeName();
    	List<BasketSeriesItemBean> localSeriesItems = new ArrayList<BasketSeriesItemBean>();
        //System.out.println("localNodeName: " + localNodeName);
        for( BasketSeriesItemBean bsib : seriesItems){
            if(!bsib.getLocationDisplayName().equals(localNodeName)){
                continue;
            }
            localSeriesItems.add(bsib);
        }
    	return localSeriesItems.size()>0 ? true:false;
    }

    public String getCustomListName(){
        return customListName;
    }

    public String getCustomListComment() {
        return customListComment;
    }

    public void setCustomListComment(String customListComment) {
        this.customListComment = customListComment;
	}

    public String getCustomListLink() {
		return customListLink;
    }

    public void setCustomListLink(String customListLink) {
        this.customListLink = customListLink;
    }

    public void setCustomListName(String customListName) {
        this.customListName = customListName;
    }

    public int getTotalImageCount()
    {
    	return thumbnailImageDto.length;
    }

    //////////////////////Dynamic Image Data Table layout///////////////////////

    public ImageResultWrapper getCellValue()
    {
    	return icefacesDataModel.getCellValue();
    }


    public boolean getCellVisibility()
    {
    	return icefacesDataModel.getCellVisibility();
    }


	public DataModel getRowDataModel() {
		return icefacesDataModel.getRowDataModel();
	}

	public DataModel getColumnDataModel(){
		return icefacesDataModel.getColumnDataModel();
	}

	public String getImageLabel() {
		return icefacesDataModel.getImageLabel();
	}

	public boolean getShowPaginator()
	{

		return icefacesDataModel.getShowPaginator();
	}
	//////////////////////////////////////////////////////////////////////

    public String createCustomizedBasket() throws Exception{
    	return "customizedBasket";
    }

    public void setCustomListSearch(boolean customListSearch){
    	this.customListSearch = customListSearch;
    }

    public boolean getCustomListSearch(){
        if(getSeriesItems().isEmpty()){
            customListSearch=false;
            customListName="";
            customListComment="";
            customListLink="";
        }
        return customListSearch;
    }


    /////////////////////////////////////PRIVATE//////////////////////////////////////////

    private Basket.BasketChangeListener basketChangeListener = new Basket.BasketChangeListener() {
        public void basketChanged(Basket basket) {
            hasBasketChangedSinceDownload = true;
        }
    };

    private ImageSearchResult[] thumbnailImageDto;
    private IcefacesRowColumnDataModelInterface icefacesDataModel;

    private boolean customListSearch=false;

    private String customListName;
    private String customListComment;
    private String customListLink;

    private static final long serialVersionUID = 1L;

    // Strings to represent the different types of downloads
    private static String HTTP_DOWNLOAD = "http_download";
    private static String FTP_DOWNLOAD = "ftp_download";

    /**
     * Logger for the class.
     */
    private static Logger logger = Logger.getLogger(BasketBean.class);

    /**
     * Stores whether or not a change has occurred since the last time a user
     * downloaded the basket's contents. Used to prevent unnecessary repeat
     * zipping.
     */
    private boolean hasBasketChangedSinceDownload = true;
    private UIInput basketChanged;

    /**
     * This is the threaded object that will zip up the files for the HTTP
     * download.
     */
    private ZipManager zipper;

    /**
     * These are flags that indicate the different downloads and tell the front
     * end what messages to display.
     */
    private boolean downloading;
    private boolean ftpDownload;
    /**
     * This string holds the filename, the filename is based upon the username
     * and the timestamp of when they create the file.
     */
    private String fileName;
    /**
     * This string holds the dynamic generated jnlpFilename, the jnlpFileName is generated based on
     * jnlpTemplate and list of seriesItem.
     */
    private String jnlpFileName;

    /**
     * The path of where to place the zip file.
     */
    private String path;

    /**
     * This is the UIComponent that the dataTable is bound to in the JSP page.
     */
    private UIData basketData;

    /**
     * The threshold for downloading using FTP
     */
    private double ftpThreshold;

    /**
     * The security bean containing the user's information
     */
    private SecurityBean sb;
    /**
     * The checkbox indicates whether to include the annotation file in the downloaded
     * files
     */
    private boolean includeAnnotation = true;

    /**
     * This is for the thumbnails on the view series page accessible from
     * the BasketBean.
     */
    private Integer resultsPerPage=12;

    /**
     * This is for the thumbnails on the view series page accessible from
     * the BasketBean.
     */
    private UIData imageData;

    /**
     * This is the series whose details are being viewed from the basket
     * page.
     */
    private SeriesSearchResult series;

    private DataModel imageList;
    /**
     * The zipper is mainly responsible for returning percentages, but
     * the UI needs to slam the number to 0 or 100 when zipping is
     * finished.  This is very rube goldberg.... when the finishDownloading
     * is called, this is set to 0 to reset the downloading metrics.  when
     * a download is initiated but the basket hasn't changed, we push this
     * up to 100 to trip the logic in dataBasket.xhtml to do an autodownload.
     * blow this up at first opportunity!!!
     */
    private int hardcodedPercent = 0;

    /**
     * This is the actual data structure for the basket (collection of series items).
     * This used to be part of this (presentation) bean but was split out
     * to create a more focused object.
     */
    private Basket basket = new Basket();


    private void createFTPUserDir(String path){
        File userFolder = new File(path, sb.getUsername());
        boolean rc=false;
        if(!userFolder.exists()){
            rc = userFolder.mkdir();
            if(!rc){
                logger.error("Unable to create sub directory for ftp download for user " + sb.getUsername());
            }
        }
    }

    private boolean isGuestAndFTP(){
        AnonymousLoginBean anonymousLoginBean = BeanManager.getAnonymousLoginBean();
        if(anonymousLoginBean.getGuestLoggedIn() ){
            double currentBasketSize = basket.calculateSizeInMB();
            double ftpLimit = NCIAConfig.getFtpThreshold();
            if(currentBasketSize >= ftpLimit){
                System.out.println("do not show download all button");
                return false;
            }
        }
        return true;
    }


    /**
     * Store the information needed by the download servlet in the session
     * after completing a download.
     */
    private void storeZipFileNameInSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest)externalContext.getRequest();

        request.getSession().setAttribute("httpDownloadZipFileName", fileName);
    }


	private static List<ImageResultWrapper> computeWrapperList(List<ImageSearchResult> imageList) {
		List<ImageResultWrapper> wrappers = new ArrayList<ImageResultWrapper>();
		for(ImageSearchResult result : imageList) {
			wrappers.add(new ImageResultWrapper(result));
		}
		return wrappers;
	}

	private static List<ImageSearchResult> unwrap(List<ImageResultWrapper> wrappers) {
		List<ImageSearchResult> results = new ArrayList<ImageSearchResult>();
		for(ImageResultWrapper wrapper : wrappers) {
			results.add(wrapper.getImage());
		}
		return results;
	}

	public boolean isNcicb()
	{
		String installationSite = NCIAConfig.getInstallationSite();
		if (installationSite.equalsIgnoreCase(NCIAConstants.INSTALLATION_SITE))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	private String exportIMFileName;

	public String getExportIMFileName(){
		return exportIMFileName;
	}
	/**
	 * generate the csv file with the series instance uids in the data basket
	 *
	 * @throws Exception
	*/
	public com.icesoft.faces.context.Resource getExportImageMetadata() throws Exception{
		exportIMFileName = "seriesInstanceUids" + System.currentTimeMillis() + ".csv";
		if(basket.isEmpty()){
			System.out.println("No data in data basket, do not show the export");
		    return null;
		}
		FileGenerator fg = new FileGenerator();
		String exportString = fg.generateImageMetadata(this.getSeriesItems());
		ByteArrayResource bar = new ByteArrayResource(exportString.getBytes());
		return bar;
	}
	//for sorting
	private static final String subjectIDHeader = "Subject ID";
	private static final String studyUIDHeader = "Study UID";
    private static final String studyDateHeader = "Study Date";
    private static final String studyDescHeader = "Study Description";
    private static final String seriesIDHeader = "Series ID";
    private static final String seriesDescHeader = "Series Description";
    private static final String numberOfImageHeader = "Number of Images";
    private static final String fileSizeHeader = "File Size";
    private static final String annotationFileSizeHeader= "Annotation File Size";


    private String sortColumnName= "Subject ID";
    private boolean ascending = true;
    // we only want to resort if the oder or column has changed.
    private String oldSort = sortColumnName;
    private boolean oldAscending = ascending;

   // ----------------
    /**
     * Gets the sortColumnName column.
     *
     * @return column to sortColumnName
     */
    public String getSortColumnName() {
        return sortColumnName;
    }

    /**
     * Sets the sortColumnName column
     *
     * @param sortColumnName column to sortColumnName
     */
    public void setSortColumnName(String sortColumnName) {
    	 oldSort = this.sortColumnName;
         this.sortColumnName = sortColumnName;

    }

    /**
     * Is the sortColumnName ascending.
     *
     * @return true if the ascending sortColumnName otherwise false.
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * Set sortColumnName type.
     *
     * @param ascending true for ascending sortColumnName, false for desending sortColumnName.
     */
    public void setAscending(boolean ascending) {
    	 oldAscending = this.ascending;
        this.ascending = ascending;
    }

    private void sort(List<BasketSeriesItemBean> toSort) {
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
            	BasketSeriesItemBean c1 = (BasketSeriesItemBean) o1;
            	BasketSeriesItemBean c2 = (BasketSeriesItemBean) o2;

                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(subjectIDHeader)) {
                    return compareObject(c1.getPatientId().compareTo(c2.getPatientId()),
                        c2.getPatientId().compareTo(c1.getPatientId()));
                } else if (sortColumnName.equals(studyUIDHeader)) {
                	return compareObject(c1.getStudyId().compareTo(c2.getStudyId()),
                		c2.getStudyId().compareTo(c1.getStudyId()));
                }else if (sortColumnName.equals(studyDateHeader)) {
                	return compareObject(c1.getStudyDate().compareTo(c2.getStudyDate()),
                		c2.getStudyDate().compareTo(c1.getStudyDate()));
                } else if (sortColumnName.equals(studyDescHeader)) {
                	if(c1.getStudyDescription() == null) {
                		return (c2.getStudyDescription() == null) ? 0 : -1;
                	}
                	if(c2.getStudyDescription() == null) {
                		return 1;
                	}
                	return compareObject(c1.getStudyDescription().compareTo(c2.getStudyDescription()),
                    		c2.getStudyDescription().compareTo(c1.getStudyDescription()));
                } else if (sortColumnName.equals(seriesIDHeader)) {
                	return compareObject(c1.getSeriesId().compareTo(c2.getSeriesId()),
                		c2.getSeriesId().compareTo(c1.getSeriesId()));
                }else if (sortColumnName.equals(seriesDescHeader)) {
                	if(c1.getSeriesDescription() == null) {
                		return (c2.getSeriesDescription() == null) ? 0 : -1;
                	}
                	if(c2.getSeriesDescription() == null) {
                		return 1;
                	}
                	return compareObject(c1.getSeriesDescription().compareTo(c2.getSeriesDescription()),
                		c2.getSeriesDescription().compareTo(c1.getSeriesDescription()));
               } else if (sortColumnName.equals(numberOfImageHeader)) {
                	return compareObject(c1.getTotalImagesInSeries().compareTo(c2.getTotalImagesInSeries()),
                		c2.getTotalImagesInSeries().compareTo(c1.getTotalImagesInSeries()));
               } else if (sortColumnName.equals(fileSizeHeader)) {
                	return compareObject(c1.getImageSizeInMB().compareTo(c2.getImageSizeInMB()),
                		c2.getImageSizeInMB().compareTo(c1.getImageSizeInMB()));
               }  else if (sortColumnName.equals(annotationFileSizeHeader)) {
                	return compareObject(c1.getAnnotationsSize().compareTo(c2.getAnnotationsSize()),
                		c2.getAnnotationsSize().compareTo(c1.getAnnotationsSize()));
               } else {
                	return 0;
                }
            }
        };
        Collections.sort(toSort, comparator);
    }

    private int compareObject(int result1, int result2){
    	return ascending ? result1 : result2;
    }

	public String getSubjectIDHeader() {
		return subjectIDHeader;
	}

	public String getStudyUIDHeader() {
		return studyUIDHeader;
	}

	public String getStudyDateHeader() {
		return studyDateHeader;
	}

	public String getStudyDescHeader() {
		return studyDescHeader;
	}

	public String getSeriesIDHeader() {
		return seriesIDHeader;
	}

	public String getSeriesDescHeader() {
		return seriesDescHeader;
	}

	public String getNumberOfImageHeader() {
		return numberOfImageHeader;
	}

	public String getFileSizeHeader() {
		return fileSizeHeader;
	}

	public String getAnnotationFileSizeHeader() {
		return annotationFileSizeHeader;
	}




}