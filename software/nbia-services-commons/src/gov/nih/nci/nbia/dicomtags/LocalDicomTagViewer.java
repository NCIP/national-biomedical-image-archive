/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dicomtags;

import gov.nih.nci.nbia.dao.ImageDAO;
import gov.nih.nci.nbia.dto.DicomTagDTO;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.PublicData;
import gov.nih.nci.nbia.util.NCIADicomObject;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.searchresult.ImageSearchResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalDicomTagViewer implements DicomTagViewer {

	/**
	 * Construct a tag viewer that will not care about
	 * public/private images.
	 */
	public LocalDicomTagViewer() {		
	}
	
	
	/**
	 * Construct a tag viewer that will optionally care about
	 * public/private images.
	 * 
	 * <p>If isPublic==true, the am must not be null
	 */	
	public LocalDicomTagViewer(boolean isPublic, 
			                   AuthorizationManager am) {
		this.isPublicData = isPublic;
		this.authorizationManager = am;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<DicomTagDTO> viewDicomHeader(ImageSearchResult image) {
        if (image != null) {
        	return viewDicomHeader(image.getId());
        } 
        else {
            return new ArrayList<DicomTagDTO>();
        }
	}


	/**
	 * Local implementation only for where calling code doesn't have
	 * an ImageSearchResult to send (qc tool).
	 */
	public List<DicomTagDTO> viewDicomHeader(Integer imagePkId) throws RuntimeException{
		//check if data is public data or not. If not, return empty list of DicomTagDTO
		if (isPublicData) {
			PublicData publicData = new PublicData();
			publicData.setAuthorizationManager(authorizationManager);
			if (!publicData.checkPublicImage(imagePkId)) {
        		return new ArrayList<DicomTagDTO>();
        	}
		}
		
		if(imagePkId!=null) {
        	ImageDAO imageDAO = (ImageDAO)SpringApplicationContext.getBean("imageDAO");
        	
        	String dicomFilePath = imageDAO.findImagePath(imagePkId);

            NCIADicomObject dicomObject = null;
            try {
            	dicomObject = new NCIADicomObject(new File(dicomFilePath));

            	return dicomObject.getTagElements();
            }
            catch(Exception ex) {
            	ex.printStackTrace();
            	throw new RuntimeException(ex);            	
            }			
		}
        else {
            return new ArrayList<DicomTagDTO>();
        }		
	}

	/**
	 * Set this true if drill down should only work for public patients.
	 * 
	 * <p>The grid service reuses this code and calls this (locally) and sets to true.  For
	 * a regular drill down this is set false.
	 */
	public void setPublicData(boolean isPublicData) {
		this.isPublicData = isPublicData;
	}

	/**
	 * If the "patient public" bit is set, the authorization manager to use
	 * must be set, otherwise it won't be able to tell what is public.
	 */
	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	//////////////////////////////////////////PRIVATE////////////////////////////////////
	
	private boolean isPublicData = false;
	private AuthorizationManager authorizationManager = null;	
}