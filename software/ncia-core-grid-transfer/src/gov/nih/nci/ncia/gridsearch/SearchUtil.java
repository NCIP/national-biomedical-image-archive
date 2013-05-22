/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.gridsearch;

import gov.nih.nci.nbia.dicomtags.LocalDicomTagViewer;
import gov.nih.nci.nbia.lookup.LookupManagerImpl;
import gov.nih.nci.nbia.search.LocalDrillDown;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.UsAvailableSearchTerms;
import gov.nih.nci.ncia.search.ImageSearchResult;

import java.rmi.RemoteException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * This method does a lot of the actual leg work for the service impl.  This
 * shuold help keep the service impl less complex.
 */
public class SearchUtil {
	
	/**
	 * Return the public search terms for this node.
	 */  
	public static AvailableSearchTerms computePublicSearchTerms() throws RemoteException {
		try {
			AuthorizationManager authorizationManager = new AuthorizationManager();
			LookupManagerImpl lookupManager = new LookupManagerImpl(authorizationManager.getAuthorizedCollections());
			return lookupManager.getAvailableSearchTerms();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public static UsAvailableSearchTerms computePublicUsSearchTerms() throws RemoteException {
		try {
			AuthorizationManager authorizationManager = new AuthorizationManager();
			LookupManagerImpl lookupManager = new LookupManagerImpl(authorizationManager.getAuthorizedCollections());
			return lookupManager.getUsAvailableSearchTerms();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(ex.getMessage());
		}
	}
	
	
	/**
	 * Compute all the DICOM tag objects for a given image.
	 */
	public static DicomTagDTO[] viewTags(ImageSearchResult imageSearchResult) throws RemoteException {
		List<gov.nih.nci.ncia.dto.DicomTagDTO> results = null;
		try {
			LocalDicomTagViewer localDicomTagViewer = new LocalDicomTagViewer(true, 
					                                                          new AuthorizationManager());
			results = localDicomTagViewer.viewDicomHeader(imageSearchResult.getId());
		}
		catch(Exception e) {
			logger.error("In the SearchUtil: Cannot get DicomTagDTO");
			throw new RemoteException(e.getMessage());
		}
		return results.toArray(new DicomTagDTO[]{});		
	}
	
	
	/**
	 * Construct an appropriate drill down object that only will deal with
	 * public patients.
	 */
	public static LocalDrillDown getDrillDown() throws RemoteException {
        LocalDrillDown localDrillDown = new LocalDrillDown();
	 	localDrillDown.setThumbnailURLResolver(new GridThumbnailUrlResolver());
	 	try	{
	 	    localDrillDown.setAuthorizationManager(new AuthorizationManager());
	 	}
	 	catch(Exception e) {
	 	    logger.error("In the SearchUtil: Cannot create AuthorizationManager. " + e.getMessage());
	 		throw new RemoteException(e.getMessage());
	 	}
	 	localDrillDown.setPatientPublic(true);
	 	 
	 	return localDrillDown;
	}

	///////////////////////////////////////PRIVATE///////////////////////////////////////
	
	private static Logger logger = Logger.getLogger(SearchUtil.class);
}
