package gov.nih.nci.nbia.download;

import gov.nih.nci.nbia.util.NBIAIOUtils;
import gov.nih.nci.nbia.util.StringUtil;
import gov.nih.nci.ncia.search.NBIANode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

/**This class downloads each series.
 *
 * @author lethai
 *
 */
public class LocalSeriesDownloader extends AbstractSeriesDownloader {

    /* Constructor for SeriesDownloader */
    public LocalSeriesDownloader() {

    }


    /**
     * {@inheritDoc}
     *
     * <p>Do a POST to the download servlet to retrieve the image files
     * (with authentication/authorization for non-public artifacts)
     */
    public void runImpl() throws Exception {
        this.sopUids = StringUtil.encodeListEntriesWithSingleQuotes(this.sopUidsList);

        computeTotalSize();
        URL url = new URL(serverUrl);
        this.connectAndReadFromURL(url);
    }


    /**
     * {@inheritDoc}
     */
    public NBIANode constructNode(String url, String displayName, boolean local) throws Exception {
        return new NBIANode(local, displayName, url);
    }

    ///////////////////////////////////////////PRIVATE//////////////////////////////////////

    /**
     * images already received... sent back to server for pause and resume
     * so server wont send these again.
     */
    private List<String> sopUidsList = new ArrayList<String>();

    private String createDirectory(){

        String localLocation = outputDirectory.getAbsolutePath() +
                               File.separator +
                               this.collection +
                               File.separator +
                               this.patientId +
                               File.separator +
                               this.studyInstanceUid +
                               File.separator +
                               this.seriesIdentifier;
        File f = new File(localLocation);
        try{
            int count = 0;
            boolean mkdirResult=false;
            /* Attempt to create the directory again if it fails for 3 times.
             * The reason for this is because 3 threads are started by default,
             * sometime the directory is not created. We think it could be thread concurrency issue
             * with parent directories creation, but couldn't reproduce the problem.
             */
            while(count < 3){
               if(!f.exists()){
                    mkdirResult = f.mkdirs();
                    if(mkdirResult==false) {
                        System.out.println("couldnt create directory: " + localLocation + " \tattempt number: " + count);
                        count++;
                   }else{
                        break;
                   }
               }else{
            	   mkdirResult=true;
            	   break;
               }
            }
            if(!mkdirResult){
                throw new RuntimeException("Could not create directory after 3 attempts: " + localLocation);
        }
        }catch(SecurityException se){
            throw new RuntimeException("SecurityException on creating directory: "+localLocation + "  " +se);
        }
        return localLocation;
    }

    private void connectAndReadFromURL(URL url) throws Exception {
        HttpClient httpClient = new HttpClient();

        PostMethod postMethod = new PostMethod(url.toString());
        postMethod.addParameter("userId", this.userId);
        postMethod.addParameter("sopUids", this.sopUids );
        postMethod.addParameter("seriesUid", this.seriesInstanceUid);
        postMethod.addParameter("includeAnnotation", Boolean.toString(this.includeAnnotation));
        postMethod.addParameter("hasAnnotation", Boolean.toString(this.hasAnnotation));
        //this is ignored
        postMethod.setRequestHeader("Range", "bytes=" + downloaded + "-");
        postMethod.setRequestHeader("password",	password);

        /* Connect to server. */
        try {
            int responseCode = httpClient.executeMethod(postMethod);
            System.out.println("response code: " + responseCode);

            /* Make sure response code is in the 200 range.*/
            if (responseCode / 100 != 2) {
                error();
            }

            /* Set the size for this download if it
             * hasn't been already set. */
            if (size == -1) {
                status = NO_DATA;
                stateChanged();
             }

             readFromConnection(postMethod.getResponseBodyAsStream());
        }
        finally {
             postMethod.releaseConnection();
        }
    }

    private void readFromConnection(InputStream is) throws Exception {
        String location = createDirectory();

        TarArchiveInputStream zis = new TarArchiveInputStream(is);

        int imageCnt = 0;
        try {
        	//the pause button affects this loop
        	//per tar entry, will check status... which pause button could
        	//change... then we will drop out of downloading.
            while(status == DOWNLOADING) {
                TarArchiveEntry tarArchiveEntry = zis.getNextTarEntry();
                if(tarArchiveEntry==null) {
                    System.out.println(this.seriesInstanceUid+" reaching end of file");
                    status = COMPLETE;
                    break;
                }

                String sop = tarArchiveEntry.getName();
                int pos = sop.indexOf(".dcm");
                OutputStream outputStream = null;
                if(pos > 0){
                    sopUidsList.add(sop.substring(0, pos));
                    outputStream = new FileOutputStream(location  + File.separator + StringUtil.displayAsSixDigitString(imageCnt)+".dcm");
                }
                else {
                    outputStream = new FileOutputStream(location  + File.separator + sop);
                }

                try {
                    NBIAIOUtils.copy(zis, outputStream, progressUpdater);
                }
                finally {
                    outputStream.flush();
                    outputStream.close();
                }

                imageCnt += 1;
            }
        }
        finally {
            if(zis!=null) {
                zis.close();
            }
        }
    }


    static {
        Protocol.registerProtocol("https",
                 new Protocol("https",
                 new EasySSLProtocolSocketFactory(),
                 443));
    }
}