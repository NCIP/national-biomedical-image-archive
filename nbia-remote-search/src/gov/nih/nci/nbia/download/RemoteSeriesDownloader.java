package gov.nih.nci.nbia.download;

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
    	
    	RemoteSeriesFileRetriever remoteSeriesFileRetriever = new RemoteSeriesFileRetriever();
    	remoteSeriesFileRetriever.setProgressDelegate(progressUpdater);
    	remoteSeriesFileRetriever.setOutputDirectory(outputDirectory);
    	remoteSeriesFileRetriever.setSeriesIdentifier(seriesIdentifier);
    	
    	SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
    	seriesSearchResult.associateLocation(node);
    	seriesSearchResult.setProject(collection);
    	seriesSearchResult.setPatientId(patientId);
    	seriesSearchResult.setStudyInstanceUid(studyInstanceUid);
    	seriesSearchResult.setSeriesInstanceUid(seriesInstanceUid);

    	//exclusion list? enhancement for later
    	//sop exlucions list for "resume" - enhancement for later
    	try {
    		remoteSeriesFileRetriever.retrieveImages(seriesSearchResult);
    		status = COMPLETE;
    	}
    	catch(Exception ex) {
    		super.error();
    	}
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