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
 * Revision 1.7  2007/11/15 22:18:57  bauerd
 * *** empty log message ***
 *
 * Revision 1.6  2007/10/01 12:22:10  bauerd
 * *** empty log message ***
 *
 * Revision 1.5  2007/08/29 19:11:19  bauerd
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/15 20:02:32  bauerd
 * *** empty log message ***
 *
 * Revision 1.3  2007/08/15 16:54:45  bauerd
 * *** empty log message ***
 *
 * Revision 1.2  2007/08/15 12:22:05  bauerd
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/07 12:05:22  bauerd
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/05 21:44:39  bauerd
 * Initial Check in of reorganized components
 *
 * Revision 1.47  2007/01/19 18:25:14  mccrimms
 * added if logic for creating the manufacturer list. remote nodes pull back a list of domain objects from the caCORE query while local searches pull back object arrays. this was causing a class cast exception.
 *
 * Revision 1.46  2007/01/11 22:44:28  dietrich
 * Defect 174
 *
 * Revision 1.45  2006/12/13 14:04:14  dietrich
 * Grid enhancement
 * Revision 1.44 2006/12/07 15:17:43 dietrich
 * Added comment
 * 
 * Revision 1.43 2006/12/07 15:09:19 dietrich Added import
 * 
 * Revision 1.42 2006/12/05 19:13:47 dietrich Same as 1.40
 * 
 * Revision 1.40 2006/12/05 19:02:45 dietrich Defect 146
 * 
 * Revision 1.38 2006/11/21 21:46:42 dietrich Defect 146
 * 
 * Revision 1.37 2006/11/21 16:36:39 shinohaa data basket grid functionality
 * 
 * Revision 1.36 2006/11/10 13:58:45 shinohaa grid prototype
 * 
 * Revision 1.35 2006/10/10 18:48:24 shinohaa 2.1 enhancements
 * 
 * Revision 1.34 2006/09/27 20:46:28 panq Reformated with Sun Java Code Style
 * and added a header for holding CVS history.
 * 
 */
package gov.nih.nci.nbia.lookup;

import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.dao.ImageDAO;
import gov.nih.nci.nbia.dto.EquipmentDTO;
import gov.nih.nci.nbia.search.EquipmentUtil;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.util.Ultrasound_Util;
import gov.nih.nci.nbia.searchresult.Manufacturer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * 
 * @author BauerD, Prashant
 */
public class LookupManagerImpl implements LookupManager {
    /**
     * Construct the object paying attention to which collections
     * the (current user) is authorized to see. 
     * 
     * <p>Authorized collections does NOT inhibit modality, kernel, site 
     * list!
     */
    public LookupManagerImpl(Collection<String> authorizedCollections){
    	init(authorizedCollections);    	
    }

	/**
	 * {@inheritDoc}
	 * 
	 * <P>This impl returns all values for the system regardless of authorization.
	 * For example, even if the user can't see the only images with modality=CT,
	 * CT will be returned here if it is in the system.
	 * <P>This impl will only return modalities for images from visible series.
	 */	
    public List<String> getModality() {
    	return modalityList;
    }


	/**
	 * {@inheritDoc}
	 * 
	 * <P>This impl returns all values for the system regardless of authorization.
	 * For example, even if the user can't see the only images with site=HEAD,
	 * HEAD will be returned here if it is in the system.
	 * <P>This impl will only return sites for images from visible series.
	 */	    
    public List<String> getAnatomicSite() {
        return anatomicList;
    }
    
    /**
	 * {@inheritDoc}
	 * 
	 * <P>This impl returns all values for the system regardless of authorization.
	 * For example, even if the user can't see the only images with site=HEAD,
	 * HEAD will be returned here if it is in the system.
	 * <P>This impl will only return sites for images from visible series.
	 */	    
    public List<String> getUsMultiModality() {
        return usMultiModalityList;
    }


	/**
	 * {@inheritDoc}
	 * 
	 * <P>This impl returns all values for the system regardless of authorization.
	 * For example, even if the user can't see the only images with kernel=B41f,
	 * B41f will be returned here if it is in the system.
	 * <P>This impl will only return kernels for images from visible series.
	 */	    
    public List<String> getDICOMKernelType() {
        return convolutionKernelList;
    }


	/**
	 * {@inheritDoc}
	 * 
	 * <P>This impl returns only collections for which the user has been authorized
	 * to see.  NOTE: This pays atttention to NCIA.PROJECT protection elements,
	 * NOT the DP_SITE_NAME protection elements.
	 */	    
    public List<String> getSearchCollection() {
        return collectionList;
    }

	
	/**
	 * {@inheritDoc}
	 * 
	 * <P>This impl returns all values for the system regardless of authorization.
	 * For example, even if the user can't see the only images with manufacturer=Toshiba,
	 * Toshiba will be returned here if it is in the system.
	 * <P>This impl will only return equipment for images from visible series.
	 */	    
    public Map<String, Map<String, Set<String>>> getManufacturerModelSoftwareItems() {
        return manufacturerModelSoftwareItems;
    }
    
    ///////////////////////////////////////PRIVATE///////////////////////////////////////////

    private static Logger logger = Logger.getLogger(LookupManagerImpl.class);

    private List<String> modalityList;
    private List<String> anatomicList = null;
    private List<String> usMultiModalityList = null;
    private List<String> convolutionKernelList = null;
    private List<String> collectionList = null;

    private Map<String, Map<String, Set<String>>> manufacturerModelSoftwareItems;
    
    //i18n issue?
    private static final String BODY_PART_EXAMINED_NOT_SPECIFIED = "NOT SPECIFIED";

    private GeneralSeriesDAO generalSeriesDAO = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");

    /**
     * Note: The LookupManager will load all drop downs once per session. If new
     * data is entered, the user will need to log out to see the new data.
     */
    private void init(Collection<String> authorizedCollections){
        // Load Modalities
        try {
            initModalities();
            
            initAnatomicSites();
            initUsMultiModalities();
            initConvolutionKernels();
            
            initSearchCollections(authorizedCollections);
            
            // Populate tree for model, manufacturer and software version 
            Collection<EquipmentDTO> resultSetList = generalSeriesDAO.findEquipmentOfVisibleSeries();
            manufacturerModelSoftwareItems = createManufacturerMap(resultSetList);        
        }
        catch (Exception e) {
        	e.printStackTrace();
            logger.error("Problem creating LookupManager", e);
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Accepts a List of Object[] or a List of ManufacturerModelSoftware
     * 
     * Returns a Map of manufacturer -> Map of ModelName -> {versions}
     */
    private static Map<String, Map<String, Set<String>>> createManufacturerMap(Collection<EquipmentDTO> resultSetList) {
        Map<String, Map<String, Set<String>>> manufacturerMap = new LinkedHashMap<String, Map<String, Set<String>>>();
        for (EquipmentDTO equipment : resultSetList) {
        	EquipmentUtil.processManufacturerObjectArray(equipment, manufacturerMap);            	              
        }
        return manufacturerMap;
    }
    

    private void initModalities() throws Exception{
    	modalityList = new ArrayList<String>(generalSeriesDAO.findDistinctModalitiesFromVisibleSeries());
    }
    
    private void initAnatomicSites() throws Exception {
    	anatomicList = new ArrayList<String>(generalSeriesDAO.findDistinctBodyPartsFromVisibleSeries());  	      	

//              GForge id 7129 - When value is null, display "NOT SPECIFIED" in the option list
        if(anatomicList.contains(null) && !anatomicList.contains(BODY_PART_EXAMINED_NOT_SPECIFIED)) {                    	
        	anatomicList.add(BODY_PART_EXAMINED_NOT_SPECIFIED);
        	
        }   
        anatomicList.remove(null); //one way or the other
    }
    
    private void initUsMultiModalities() throws Exception {
    	usMultiModalityList = new ArrayList<String>();
	
    	ImageDAO imageDAO = (ImageDAO)SpringApplicationContext.getBean("imageDAO");
    	ArrayList<String> rawList = new ArrayList<String>(imageDAO.findAllImageType());  	      	
 
        rawList.remove(null); //one way or the other
        
        for(String umms : rawList) {
        	String [] ummsList = umms.split(",");
        	
        	for (int i = 0; i < ummsList.length; ++i){
        		String mmlable = Ultrasound_Util.getTextByGivenImageTypeCode(ummsList[i]);
        		if (!usMultiModalityList.contains(mmlable)){
        			usMultiModalityList.add(mmlable);
        		}
        	}
    	}
    }
    
    private void initConvolutionKernels() throws Exception {
    	ImageDAO imageDAO = (ImageDAO)SpringApplicationContext.getBean("imageDAO");
    	convolutionKernelList = new ArrayList<String>(imageDAO.findDistinctConvolutionKernels());  	
    }
    
    private void initSearchCollections(Collection<String> authorizedCollections) throws Exception {
    	List<String> allCollections = new ArrayList<String>(generalSeriesDAO.findProjectsOfVisibleSeries());
    	this.collectionList = new ArrayList<String>();
    	for(String collection : allCollections) {
    		if(authorizedCollections.contains(collection)) {
    			collectionList.add(collection);
    		}
    	}
    	
    }    
   
  
    private static String[] toArray(List<String> list) {
    	return list.toArray(new String[]{});
    }    
}
