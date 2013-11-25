/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.download;

import java.sql.Timestamp;

import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.ncia.search.NBIANode;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.nbia.zip.RemoteSeriesFileRetriever;

/**
 * This class downloads a series from a remote node.
 */
public class RemoteSeriesDownloader extends AbstractSeriesDownloader {

	/**
	 * {@inheritDoc}
	 *
	 * <p>This impl make a remote grid call to download the DICOM images files from
	 * the series this downloader is tied to.
	 */
    public void runImpl() throws Exception {
    	//for pause/resume
    	downloaded = 0;
    	int retryAttempts =0;
    	RemoteSeriesFileRetriever remoteSeriesFileRetriever = new RemoteSeriesFileRetriever();
    	remoteSeriesFileRetriever.setProgressDelegate(progressUpdater);
    	remoteSeriesFileRetriever.setOutputDirectory(outputDirectory);
    	remoteSeriesFileRetriever.setSeriesIdentifier(seriesInstanceUid);
    	
    	SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
    	seriesSearchResult.associateLocation(node);
    	seriesSearchResult.setProject(collection);
    	seriesSearchResult.setPatientId(patientId);
    	seriesSearchResult.setStudyInstanceUid(studyInstanceUid);
    	seriesSearchResult.setSeriesInstanceUid(seriesInstanceUid);

    	//exclusion list? enhancement for later
    	//sop exlucions list for "resume" - enhancement for later
    	try {
    		status = retrieveSeriesImages(remoteSeriesFileRetriever, seriesSearchResult, retryAttempts);
    	}
    	catch(Exception ex) {
    		super.error();
    	}
    }


	private int retrieveSeriesImages(RemoteSeriesFileRetriever remoteSeriesFileRetriever,
			SeriesSearchResult seriesSearchResult, int retryAttempt) throws Exception {
		if (retryAttempt >= noOfRetry) {
			additionalInfo.append(getTimeStamp() + " For series " + this.seriesInstanceUid +" Reached max retry (" + noOfRetry + ") attempts.\n");
			System.out.println(additionalInfo + "--attempt" + retryAttempt +" changing to error status");
			super.error();
			return AbstractSeriesDownloader.ERROR;
		}
		try {
			remoteSeriesFileRetriever.retrieveImages(seriesSearchResult);
    	}
    	catch(Exception ex) {
    		additionalInfo.append(getTimeStamp() + ex.getMessage()).append(" retry attempt "+ retryAttempt +".\n");
    		retrieveSeriesImages(remoteSeriesFileRetriever, seriesSearchResult, retryAttempt+1);
    	}
		return AbstractSeriesDownloader.COMPLETE;
	}
	private String getTimeStamp () {
		return new Timestamp(System.currentTimeMillis()).toString();
	}

    /**
     * {@inheritDoc}
     *
     * <p>Makes a remote node with enough state to do what we need to do.
     */
    public NBIANode constructNode(String url,
    		                      String displayName,
    		                      boolean local) throws Exception{
    	return RemoteNode.constructPartialRemoteNode(displayName, url);
    }
}