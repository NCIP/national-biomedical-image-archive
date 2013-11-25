/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.basket.BasketSeriesItemBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * This object takes a JNLP template for the download manager
 * and transforms it into a concrete JNLP file with host and session
 * specific values.
 *
 * <p>It expects the template to be in the classpath with a specific
 * resource name.
 */
public class DynamicJNLPGenerator {

    private static final String jnlpTemplate="/jnlpTemplate.jnlp";

    public String generate(String userId,
                           String password,
                           String codebase,
                           String downloadServerUrl,
                           Boolean includeAnnotation,
                           List<BasketSeriesItemBean> seriesItems,long currentTimeMillis, String noOfRetry){
        this.codebase = codebase;
        String jnlp="";
        try{
            StringBuffer jnlpBuilder = this.getJnlp();
            int size = seriesItems.size();
            StringBuffer argsBuilder = new StringBuffer();
            List<String> seriesDownloadData = new ArrayList<String>();
            for(int i=0; i<size; i++){
            	BasketSeriesItemBean seriesItem = seriesItems.get(i);

                String collection = seriesItem.getProject();
                String patientId = seriesItem.getPatientId();
                String studyInstanceUid = seriesItem.getStudyId();
                String seriesInstanceUid =seriesItem.getSeriesId();
                String annotation = seriesItem.getAnnotated();
                Integer numberImages = seriesItem.getTotalImagesInSeries();
                Long imagesSize = seriesItem.getExactSize();
                Long annoSize = seriesItem.getAnnotationsSize();
                String url = seriesItem.getSeriesSearchResult().associatedLocation().getURL();
                String displayName = seriesItem.getSeriesSearchResult().associatedLocation().getDisplayName();
                boolean local = seriesItem.getSeriesSearchResult().associatedLocation().isLocal();

                String argument = "" +
                                  collection + "|" +
                                  patientId + "|"+
                                  studyInstanceUid + "|" +
                                  seriesInstanceUid+ "|" +
                                  annotation + "|" +
                                  numberImages + "|" +
                                  imagesSize + "|" +
                                  annoSize + "|" +
                                  url + "|" +
                                  displayName+ "|" +
                                  local;
                seriesDownloadData.add(argument);
            }
           File dataFile = new File(System.getProperty("java.io.tmpdir"), "jnlp-data"+currentTimeMillis+".txt");
           OutputStream os = new FileOutputStream(dataFile);
           IOUtils.writeLines(seriesDownloadData, System.getProperty("line.separator"), os);
           argsBuilder.append("<argument>").append(dataFile.getAbsolutePath()).append("</argument>");
            //get user id and included annotation
            StringBuffer propXMLBuilder = new StringBuffer();
            propXMLBuilder.append(this.getPropertyXML( "jnlp.includeAnnotation", includeAnnotation.toString()));
            propXMLBuilder.append(this.getPropertyXML( "jnlp.userId", userId));
            //this should be over secure connection
            String encryptedPassword = this.encrypt(userId, password);
            propXMLBuilder.append(this.getPropertyXML( "jnlp.password", encryptedPassword));
            propXMLBuilder.append(this.getPropertyXML("jnlp.codebase", this.codebase));
            propXMLBuilder.append(this.getPropertyXML("jnlp.downloadServerUrl", downloadServerUrl));
            propXMLBuilder.append(this.getPropertyXML("jnlp.noofretry", noOfRetry));
            this.replaceProperties(propXMLBuilder, jnlpBuilder);
            this.replaceArguments(argsBuilder.toString(), jnlpBuilder);

            jnlp = jnlpBuilder.toString();
        }
        catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return jnlp;
    }

    //////////////////////////////////////PRIVATE////////////////////////////////////
    private String codebase;

    private String getPropertyXML( String name, String value ) {
        return "<property name=\"" + name + "\" value=\"" + value + "\"/>\n";
    }

    private void replaceCodebase( StringBuffer jnlpBuilder ) {
        int start = jnlpBuilder.toString().indexOf( "$$codebase" );
        int length = "$$codebase".length();

        jnlpBuilder.replace( start, start+length, codebase + "/");

        start = jnlpBuilder.toString().indexOf( "$$codebase" );
        if(start > 0){
            jnlpBuilder.replace( start, start+length, codebase );
        }
    }

    private void replaceProperties( StringBuffer propXmlBuilder,  StringBuffer jnlpBuilder ) {
        if( jnlpBuilder.toString().indexOf( "$$properties" ) < 0 ) {
            return;
        }

        int start = jnlpBuilder.toString().indexOf( "$$properties" );
        int length = "$$properties".length();

        jnlpBuilder.replace( start, start+length, propXmlBuilder.toString() );
    }

    private void replaceArguments(String arg, StringBuffer jnlpBuilder){
        int start = jnlpBuilder.toString().indexOf( "$$arguments" );
        int length = "$$arguments".length();

        jnlpBuilder.replace( start, start+length, arg );
    }

    private StringBuffer getJnlp() throws IOException {
        InputStream jnlpInputStream=null;
        StringBuffer sbuilder=null;
        try{
            jnlpInputStream = this.getClass().getResourceAsStream(jnlpTemplate);
            String jnlpString = IOUtils.toString(jnlpInputStream);
            sbuilder = new StringBuffer(jnlpString);
            this.replaceCodebase( sbuilder );
        }catch(IOException e){
            throw e;
        }finally{
            if(jnlpInputStream != null){
                jnlpInputStream.close();
            }
        }
        return sbuilder;
    }

    private String encrypt(String userId, String password) throws Exception{
        if(userId.equals(NCIAConfig.getGuestUsername())){
            return "";
        }
        StringEncrypter encrypter = new StringEncrypter();
        return encrypter.encryptString(password);
    }
}