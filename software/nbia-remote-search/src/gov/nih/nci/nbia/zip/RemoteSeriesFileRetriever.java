/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.zip;

import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.nbia.dto.AnnotationFileDTO;
import gov.nih.nci.nbia.dto.DicomFileDTO;
import gov.nih.nci.nbia.dto.ImageFileDTO;
import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.nbia.util.NBIAIOUtils;
import gov.nih.nci.nbia.util.NCIADicomObject;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.cagrid.transfer.context.client.TransferServiceContextClient;
import org.cagrid.transfer.context.client.helper.TransferClientHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;

/**
 * This implementation will invoke the grid (transfer) service to retrieve
 * the files from remote nodes and to store them locally for zipping.
 */
public class RemoteSeriesFileRetriever extends Observable
                                       implements SeriesFileRetriever {

	/**
	 * {@inheritDoc}
	 */
	public void cleanupResultsDirectory() {
		//never downloaded anything and didnt set the output directory manually.
		if(resultsDirectory==null) {
			return;
		}

		try {
			FileUtils.deleteDirectory(resultsDirectory);
		}
		catch(IOException ex) {
			System.out.println("Could not remove results dir:"+resultsDirectory.getAbsolutePath());
			ex.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Use the grid transfer service to pull down all the images for the specified
	 * series to the local filesystem.
	 */
	public DicomFileDTO retrieveImages(SeriesSearchResult seriesSearchResult) {
		RemoteNode remoteNode = (RemoteNode)seriesSearchResult.associatedLocation();
		String serviceAddress = remoteNode.getEndpointReferenceType().getAddress().toString();

		InputStream istream = null;
		TransferServiceContextClient tclient = null;
		try {
			NCIACoreServiceClient nbiaServiceClient = new NCIACoreServiceClient(serviceAddress);

			TransferServiceContextReference tscr = nbiaServiceClient.retrieveDicomDataBySeriesUID(seriesSearchResult.getSeriesInstanceUid());

			tclient = new TransferServiceContextClient(tscr.getEndpointReference());

			istream = TransferClientHelper.getData(tclient.getDataTransferDescriptor());

	        //hasnt been set by caller/client so make one up
	        if(resultsDirectory==null) {
	        	resultsDirectory = NBIAIOUtils.createNewDirectory();
	        }

			ZipReaderUtil.readZipOfImageFilesForSeries(resultsDirectory,
					                                   istream,
					                                   seriesSearchResult,
					                                   seriesIdentifier,
					                                   progressDelegate);
			return computeImageDTO(resultsDirectory);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		finally {
			IOUtils.closeQuietly(istream);
			try	{
				tclient.destroy();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}


	/**
	 * {@inheritDoc}
	 *
	 * <P>For remote, we aren't bothering to retrieve annotations given that annotation
	 * stuff could change quite a bit because of AIM.
	 */
	public List<AnnotationFileDTO> retrieveAnnotations(SeriesSearchResult seriesSearchResult) {
		return new ArrayList<AnnotationFileDTO>();
	}


	/**
	 * Optional progress delegate that reports bytes as they retrieved.
	 */
	public void setProgressDelegate(NBIAIOUtils.ProgressInterface progressDelegate) {
		this.progressDelegate = progressDelegate;
	}

	/**
	 * Set this before invoking retrieveImages.  If reusing this object, it is advisable
	 * to reset this per invocation.
	 *
	 * <P>If this isn't set, the retriever will make up a directory, but reuse of the
	 * object will then be problematic as it will not reset the output directory.
	 */
	public void setOutputDirectory(File resultsDirectory) {
		this.resultsDirectory = resultsDirectory;
	}
	
	public String getSeriesIdentifier() {
		return seriesIdentifier;
	}

	/**
	 * The name of the local directory to create for this series's downloads.
	 */
	public void setSeriesIdentifier(String seriesIdentifier) {
		this.seriesIdentifier = seriesIdentifier;
	}	
	//////////////////////////////////////////PRIVATE///////////////////////////////////////////

	private String seriesIdentifier;
	
	private File resultsDirectory;

	private NBIAIOUtils.ProgressInterface progressDelegate;
	
	private static DicomFileDTO computeImageDTO(File outputDirectory) {
    	List<ImageFileDTO> fileDtoList = new ArrayList<ImageFileDTO>();
    	List<AnnotationFileDTO> annoFileDtoList = new ArrayList<AnnotationFileDTO>();
    	
		Iterator<File> fileIter = FileUtils.iterateFiles(outputDirectory,
				                                         TrueFileFilter.TRUE,
				                                         TrueFileFilter.TRUE );

		while(fileIter.hasNext()) {
			File dicomFile = fileIter.next();

			String sopInstanceUid = deduceSOPInstanceUID(dicomFile);
			//skip
			if(sopInstanceUid==null) {
				String xmlPath = dicomFile.getAbsolutePath();
				AnnotationFileDTO annotationFileDto = new AnnotationFileDTO(
														new Integer(0),
														xmlPath,
														new Integer(""+dicomFile.length()));
				annoFileDtoList.add(annotationFileDto);
				continue;
			}
			ImageFileDTO imageFileDTO = new ImageFileDTO(dicomFile.getAbsolutePath(),
					                                     dicomFile.length(),
					                                     sopInstanceUid);
			fileDtoList.add(imageFileDTO);
		
		}
		DicomFileDTO dicom = new DicomFileDTO();
		dicom.setAnnotationDTOList(annoFileDtoList);
		dicom.setImageFileDTOList(fileDtoList);
		//recurse over directory
		//make a DTO per file
		return dicom;

	}


	private static String deduceSOPInstanceUID(File dicomFile) {

		try {
			return NCIADicomObject.loadSOPInstanceUID(dicomFile);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			//skip this file
			return null;
		}
	}
}