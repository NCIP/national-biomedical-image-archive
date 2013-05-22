/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.zip;

import gov.nih.nci.ivi.utils.ZipEntryInputStream;
import gov.nih.nci.nbia.util.NBIAIOUtils;
import gov.nih.nci.nbia.util.StringUtil;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;

public class ZipReaderUtil {
	/**
	 * The input stream should be a zip containing (DICOM image) files that
	 * correspond to a given series.
	 *
	 * <p>In the results directory, construct an output directory
	 * hierarchy of project/patient/study/series based upon the series
	 * search result.
	 *
	 * <p>Unzip the zip stream into the series directory.
	 *
	 * @param progressDelegate callback to incrementally report bytes copied (up to caller to know total).
	 *                         this can be null.
	 */
	public static void readZipOfImageFilesForSeries(File resultsDirectory,
			                                        InputStream istream,
			                                        SeriesSearchResult seriesSearchResult,
			                                        String seriesIdentifier,
			                                        NBIAIOUtils.ProgressInterface progressDelegate) throws Exception {
        ZipInputStream zis = new ZipInputStream(istream);
        ZipEntryInputStream zeis = null;

        int imageCnt = 0;
        while(true) {
        	try {
        		//advantage in buffering this stream?
        		zeis = new ZipEntryInputStream(zis);
        	}
        	catch (EOFException e) {
        		break;
            }

            String localLocation = resultsDirectory.getAbsolutePath() +
                                   File.separator +
                                   seriesSearchResult.getProject() +
                                   File.separator +
                                   seriesSearchResult.getPatientId() +
                                   File.separator +
                                   seriesSearchResult.getStudyInstanceUid() +
                                   File.separator +
                                   seriesIdentifier;

            File projectPatientStudySeriesImageFile = null;
            if (zeis.getName().toUpperCase().indexOf(".DCM") >=0){
            	projectPatientStudySeriesImageFile = new File(localLocation +
        			                                           File.separator +
        			                                           StringUtil.displayAsSixDigitString(imageCnt)+".dcm");
            }else{
            	projectPatientStudySeriesImageFile = new File(localLocation +
            												  File.separator +
            												  zeis.getName());
            }

            boolean doesExist = projectPatientStudySeriesImageFile.getParentFile().exists();
            boolean dirCreateResult = projectPatientStudySeriesImageFile.getParentFile().mkdirs();
        	if (!doesExist && dirCreateResult==false) {
    		    System.out.println("couldnt create directory - might already exist tho so not failing");
        	}

            OutputStream fos = new FileOutputStream(projectPatientStudySeriesImageFile);
            try {
            	NBIAIOUtils.copy(zeis, fos, progressDelegate);
            }
            finally {
    			IOUtils.closeQuietly(fos);
    			IOUtils.closeQuietly(zeis);
            }

            imageCnt += 1;
        }
        //let caller close istream
	}
}
