/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 *
 */
package gov.nih.nci.ncia;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.introduce.servicetools.security.SecurityUtils;
import gov.nih.nci.cagrid.ncia.service.NCIACoreServiceConfiguration;
import gov.nih.nci.nbia.util.Util;
import gov.nih.nci.ncia.domain.TrialDataProvenance;
import gov.nih.nci.ncia.griddao.ImageDAOInterface;
import gov.nih.nci.ncia.gridzip.ZipManager;
import gov.nih.nci.ncia.gridzip.ZippingDTO;

import java.io.File;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.cagrid.transfer.context.service.helper.TransferServiceHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;

/**
 * @author lethai
 *
 */
public class ServiceImplHelper {
	private static Logger logger = Logger.getLogger(ServiceImplHelper.class);

	/**
	 * retrieve image files path from the file system
	 */
	
	// STEP 1: run your CQLQuery --> get the images filepath
	// return Map that contains sopInstanceUID as key and filepath
	public static Map<String, String> retrieveImageFiles(CQLQuery cQLQuery, ImageDAOInterface imageDao) {
		try {
			//ImageDAO imageDao = new ImageDAO();
	
			/***********************************************************************
			 * STEP 1.0 Modify the CQLQuery so that it always returns Image Object
			 * in order to get list of SOPInstanceUIDs
			 **********************************************************************/	
			cQLQuery = CustomizedCQLQuery.modifyCQLQueryToTargetImage(cQLQuery);
			if(cQLQuery==null) { //something like Equipment or TrialSite or something
				return new HashMap<String, String>();
			}
			GridUtil.dumpCQLQuery(cQLQuery);
	
			/***********************************************************************
			 * STEP 1.1 We will first run the CQL query as a regular query and get a
			 * list of SOPInstanceUIDs
			 **********************************************************************/		
			List<String> sbSOPInstanceUIDList = CQLQueryProcessorUtil.computeSopInstanceUidOrClauseFromQuery(cQLQuery);
			//String sopString = sbSOPInstanceUIDList.toString();
			//logger.debug("sopString list: " + sopString.trim());
			if (sbSOPInstanceUIDList == null) {
				return null;
			}
	
			/***********************************************************************
			 * STEP 1.2 Using this list of SOPInstanceUIDs get the actual files
			 **********************************************************************/
			//add some thing hererer --------------==============================
			Map<String, String> retrievedFileNames = new HashMap<String, String>();
			List<List<String>> breakdownList = Util.breakListIntoChunks(sbSOPInstanceUIDList, 900);
			for(List<String> unit : breakdownList)
			{
				Map<String, String> fileNames = imageDao.getImagesFiles(unit);
				retrievedFileNames.putAll(fileNames);
			}
			return retrievedFileNames;
		}
		catch(Exception e){
			logger.error("Error getting image files" , e);
			e.printStackTrace();
			throw new RuntimeException("Error getting image files: ", e);
		}		

	}

	/**
	 * Empty output stream
	 */
	public static TransferServiceContextReference getEmptyOutputStream() {
		try {		
			TransferServiceContextReference tscr = null;
			PipedOutputStream pos = new PipedOutputStream();
			PipedInputStream pis = new PipedInputStream();

			pis.connect(pos);
			pos.flush();
			pos.close();
			
			tscr = TransferServiceHelper.createTransferContext(pis, null);
			return tscr;			
		} 
		catch (Exception e) {
			logger.error("Error in getEmptyOutputStream ", e);
			throw new RuntimeException(e);
		}
	}

	/*
	 * This methods calls to csm tables to get public access trial data
	 * provenance and compare with the trial data provenance of the query.
	 * Return true if the query trial data provenance is in the public group,
	 * false otherwise.
	 */
	public static boolean getPublicGroupAndCheckForPublicAccess(TrialDataProvenance tdp) throws Exception {

		String userDN = SecurityUtils.getCallerIdentity();
        
		System.out.println("userDN:"+userDN);
		if(userDN==null) {
			String publicGroupName = NCIACoreServiceConfiguration.getConfiguration().getNciaPublicGroup();
			List<TrialDataProvenance> authorizedTdp = NCIAQueryFilter.getDataFilterByGroupName(publicGroupName);
			if( tdp != null ) {
				return GridUtil.isFound(tdp, authorizedTdp);
			}
			else {
				return false;
			}
		}
		else {
			List<TrialDataProvenance> authorizedTdp = NCIAQueryFilter.getDataFilterByUserName(userDN);
			if( tdp != null ) {
				return GridUtil.isFound(tdp, authorizedTdp);
			}
			else {
				return false;
			}
		}
	}

	
	/**
	 * check whether given trial data provenance map is publicly available.
	 */
	public static List<String> getPublicGroupAndCheckForPublicAccess(Map<String, TrialDataProvenance> tdps) throws Exception {

		boolean isPublic = false;
		List<String> publicIds = new ArrayList<String>();
	
		List<TrialDataProvenance> tdpList = null;
		String userDN = SecurityUtils.getCallerIdentity();
		System.out.println("userDN:"+userDN);
		if(userDN==null) {
			String publicGroupName = NCIACoreServiceConfiguration.getConfiguration().getNciaPublicGroup();
			tdpList = NCIAQueryFilter.getDataFilterByGroupName(publicGroupName);
		}
		else {
			tdpList = NCIAQueryFilter.getDataFilterByUserName(userDN);
		}
		

		//for each id and tdp, check whether it's in public group
		for(Map.Entry<String, TrialDataProvenance> entry : tdps.entrySet()) {

			String id = entry.getKey();
			logger.info("key: " + id );
			TrialDataProvenance tdp = entry.getValue();

			isPublic = GridUtil.isFound(tdp, tdpList);
			if(isPublic){
				publicIds.add(id);
				logger.info("\n++++++++++++ public key: " + id);
			}
		}

		return publicIds;
	}
	
	
	/**
	 * This method zips all the files in zdto into zip file (fileName) and store at filePath location
	 * @param zdto
	 * @param filePath
	 * @param fileName
	 * @throws RemoteException
	 */
	public static TransferServiceContextReference getDicomData(List<ZippingDTO> zdto, 
			                                                   String filePath, 
			                                                   String fileName) throws RemoteException {

		long start = System.currentTimeMillis();
		ZipManager zipManager = null;
		try {
			zipManager = new ZipManager();
			zipManager.setZdto(zdto);
			zipManager.setDestinationFileName(fileName);
			zipManager.setDestinationFilePath(filePath);
			logger.info("\n\nstart zipping.....");
			logger.info("filepath: " + filePath + " \tfileName: " + fileName );
			zipManager.zip();

			long end = System.currentTimeMillis();
			logger.info("\nTotal time to read and zip " + filePath + fileName
					    + ": " + (end - start) + " milli seconds");
		} 
		catch (Exception e) {
			logger.error("Error zipping file " + e.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		} 
		finally {
			zipManager = null;
		}
		
		File zipFile = new File(filePath + fileName);
		logger.info("file name: " + zipFile.getName() + " path: " + zipFile.getPath());
		
		TransferServiceContextReference tscr = TransferServiceHelper.createTransferContext(zipFile,
				                                                                           null, 
				                                                                           true);

		return tscr;
	}
	
	
	/**
	 * get the temp location to store the zip file
	 *
	 * @throws Exception
	 */
	public static String getTempZipLocation() throws Exception{

		String filepath="";
	
		filepath = NCIACoreServiceConfiguration.getConfiguration().getTempZipLocation();
		logger.info("filepath: " + filepath);
			
		//check for the forward slash at the end
		if(!filepath.endsWith("/")){
			filepath += "/";
		}
		logger.info("filepath: " + filepath);

		return filepath;
	}
}

