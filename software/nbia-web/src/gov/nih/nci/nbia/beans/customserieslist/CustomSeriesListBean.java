/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.customserieslist;


import gov.nih.nci.nbia.lookup.*;
import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.basket.BasketBean;
import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.customserieslist.CustomSeriesListProcessor;
import gov.nih.nci.nbia.customserieslist.FileParser;
import gov.nih.nci.nbia.dto.CustomSeriesListDTO;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.NCIAConfig;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
/**
 * Create customized series list either by uploading a file or 
 * from content of the data basket page.
 * @author Thai Le
 *
 */
public class CustomSeriesListBean {
    /**
     * Logger for the class.
     */
    private static Logger logger = Logger.getLogger(CustomSeriesListBean.class);
    private SecurityBean sb;
    private String name="";
    private String comment="";
    private String hyperlink="";
    
    private boolean fromBasket=false;
    private CustomSeriesListProcessor processor;
    private FileParser parser;
    private List<String> seriesUidsList = new ArrayList<String>();
    
    private static final String CREATE_CUSTOM_SERIES_LIST = "createCustomSeriesList";

    /**
     * This is the underlying map that stores all of the items that are placed
     * in the Data Basket. Key is the series ID
     */
    private List<BasketSeriesItemBean> seriesList= new ArrayList<BasketSeriesItemBean>();
    private List<String> noPermissionSeries = new ArrayList<String>();
    private List<String> privateSeries = new ArrayList<String>();
    private boolean infoMessage=false;
    private String message;
    private FileInfo currentFile;
    private int seriesCount=0;
    private int privateSeriesCount=0;
    private int noPermissionSeriesCount=0;
    private int fileProgress=-1;
    
    public CustomSeriesListBean() throws Exception{
        sb = BeanManager.getSecurityBean();
        AuthorizationManager am = sb.getAuthorizationManager();
        processor = new CustomSeriesListProcessor(sb.getUsername(), am);
        parser = new FileParser();
    }
    public void uploadActionListener(ActionEvent actionEvent) {
        InputFile inputFile = (InputFile) actionEvent.getSource();
        currentFile = inputFile.getFileInfo();
        if (currentFile.getStatus() == FileInfo.SAVED) {
            /* clear whatever in the list */
            seriesUidsList.clear();
            privateSeries.clear();
            if(parser.isCSVFile(currentFile.getFile())){
                System.out.println("not .cvs file " + currentFile.getFile().getName());
                infoMessage = true;
                message = "Please upload an .csv file.";
                fileProgress = -1;
            }else{
                seriesUidsList = parser.parse(currentFile.getFile());
                //check for permission to see all the series in the list
                noPermissionSeries.clear();
                noPermissionSeries  = processor.validate(seriesUidsList);
                System.out.println("noPermissionSeries size: " + noPermissionSeries.size());
                if(!noPermissionSeries.isEmpty()){
                    logger.info("user doesn't have permission to see some series instance uid" + noPermissionSeries.size());
                }else{
                    privateSeries = processor.isAnyPrivate(seriesUidsList);
                }
                seriesCount = seriesUidsList.size();
                infoMessage = false;
            }
        }else{
            System.out.println("uploaded file status " + currentFile.getStatus());			
            infoMessage = true;
            message = "Error uploading the file. Please check the file and load again.";
        }
    }    

    /**
     * <p>This method is bound to the inputFile component and is executed
     * multiple times during the file upload process.  Every call allows
     * the user to finds out what percentage of the file has been uploaded.
     * This progress information can then be used with a progressBar component
     * for user feedback on the file upload progress. </p>
     *
     * @param event holds a InputFile object in its source which can be probed
     *              for the file upload percentage complete.
     */
    public void fileUploadProgress(EventObject event) {
        InputFile ifile = (InputFile) event.getSource();
        fileProgress = ifile.getFileInfo().getPercent();
    }
    public String createListFromLink(){
        this.fromBasket = false;
        reset();
        return CREATE_CUSTOM_SERIES_LIST;
    }

    public String createListFromBasket(){
        //call this from the databasket page
        reset();
        this.fromBasket = true;
        BasketBean bb= BeanManager.getBasketBean();
        List<BasketSeriesItemBean> tempList = bb.getSeriesItems();

        for( BasketSeriesItemBean bsib : tempList){
            seriesUidsList.add(bsib.getSeriesId());
            seriesList.add(bsib);
        }
        logger.debug("seriesUidsList: " + seriesUidsList.size());
        //is there any series that is in private collection
        privateSeries = processor.isAnyPrivate(seriesUidsList);
        return CREATE_CUSTOM_SERIES_LIST;
    }

    public String submit(){
        System.out.println("submit button is clicked.. ");
        CustomSeriesListDTO dto = new CustomSeriesListDTO();
        dto.setComment(comment);
        
        if(!((hyperlink.startsWith("https://")) || (hyperlink.startsWith("http://")) || (hyperlink.startsWith("//"))))
		        {
		        	hyperlink = "//".concat(hyperlink);
		        	dto.setHyperlink(hyperlink);
		        }
		   else
				{
					dto.setHyperlink(hyperlink);
				}
        
        dto.setName(name);
        dto.setSeriesInstanceUIDs(seriesUidsList);

        if(dto.getSeriesInstanceUIDs().isEmpty()){
            infoMessage = true;
            message = "You can not create a list without any series instance uid.";
            return null;
        }
        if(!noPermissionSeries.isEmpty()){
            infoMessage = true;
            message="Please make sure you have permission to see all the series. The list (" + name + ") has not been created.";
        }else{
            long returnCode = processor.create(dto);
            if(returnCode > 0){
                infoMessage=true;
                message = "The list (" + name + ") created successfully.";
                MessageUtil.addInfoMessage("MAINbody:customSeriesListForm:infoMsg","customListCreated", new Object[] { name });
            }
        }
        return CREATE_CUSTOM_SERIES_LIST;
    }

    /**
     * Reset 
     */
    public void reset(){
        comment="";
        hyperlink="";
        name="";
        message="";
        seriesList.clear();
        seriesUidsList.clear();
        infoMessage = false;
        noPermissionSeries.clear();
        privateSeries.clear();
        seriesCount = 0;
        privateSeriesCount = 0;
        noPermissionSeriesCount = 0;
        fileProgress=-1;
    }

    public void validateName(FacesContext context, UIComponent validate, Object value){
        String name = (String)value;
        if (processor.isDuplicateName(name)){
            ((UIInput)validate).setValid(false);
            FacesMessage msg = new FacesMessage("The entered name already exists. Please choose a different name.");
            context.addMessage(validate.getClientId(context), msg);
        }
    }

    public boolean isInfoMessage() {
        return infoMessage;
    }

    public void setInfoMessage(boolean infoMessage) {
        this.infoMessage = infoMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FileInfo getCurrentFile() {
        return currentFile;
    }
    public List<BasketSeriesItemBean> getSeriesList() throws Exception {
        return seriesList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public boolean isFromBasket() {
        return this.fromBasket;
    }
    public void setFromBasket(boolean fromBasket) {
        this.fromBasket = fromBasket;
    }
    public List<String> getNoPermissionSeries() {
        return noPermissionSeries;
    }
    public void setNoPermissionSeries(List<String> noPermissionSeries) {
        this.noPermissionSeries = noPermissionSeries;
    }
    public List<String> getPrivateSeries() {
        return privateSeries;
    }

    public void setPrivateSeries(List<String> privateSeries) {
        this.privateSeries = privateSeries;
    }

    public int getSeriesCount() {
        if(seriesUidsList != null){
            seriesCount = seriesUidsList.size();
        }
        return seriesCount;
    }
    public int getPrivateSeriesCount() {
        if(privateSeries != null){
            privateSeriesCount = privateSeries.size();
        }
        return privateSeriesCount;
    }
    public int getNoPermissionSeriesCount() {
        if(noPermissionSeries != null){
             noPermissionSeriesCount = noPermissionSeries.size();
        }
        return noPermissionSeriesCount;
    }
    public List<String> getSeriesUidsList() {
        return seriesUidsList;
    }
    public int getFileProgress() {
        return fileProgress;
    }
}
