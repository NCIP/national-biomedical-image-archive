/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dbadapter;

import gov.nih.nci.nbia.annotations.AnnotationStorage;
import gov.nih.nci.nbia.query.DicomSOPInstanceUIDQueryInterface;
import gov.nih.nci.nbia.util.AdapterUtil;
import gov.nih.nci.nbia.util.DicomConstants;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmElement;
import org.dcm4che.dict.DictionaryFactory;
import org.dcm4che.dict.TagDictionary;
import org.rsna.ctp.objects.DicomObject;
import org.rsna.ctp.objects.FileObject;
import org.rsna.ctp.objects.XmlObject;
import org.rsna.ctp.objects.ZipObject;
import org.rsna.ctp.pipeline.Status;
import org.rsna.ctp.stdstages.database.UIDResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class NCIADatabaseDelegator {


    @Transactional (propagation=Propagation.REQUIRED)
    public void process(DicomObject file, File storedFile,String url) throws RuntimeException {
        if (storedFile == null)
        {
            log.error("Unable to obtain the stored DICOM file");
            failedSubmission("Unable to obtain the stored DICOM file");
        }
        String filename = storedFile.getAbsolutePath();

        //long filesize = storedFile.length();
        boolean visibility=false;

        try {
            numbers = new HashMap();
            numbers.put("current_timestamp", new java.util.Date());

            //Dataset set = DcmUtil.parse(storedFile, 0x5FFFffff);
            //Based on what John Perry's request
            Dataset set = file.getDataset();
            parseDICOMPropertiesFile(set);

            //enhancement of storage service
            if (!preProcess()) {
                log.error("Storage Service - Preprocess: Error occurs when trying to find project, site in preprocess() for file " + file.getFile().getAbsolutePath());
                failedSubmission("Storage Service - Preprocess: Error occurs when trying to find project, site in preprocess() for file " + file.getFile().getAbsolutePath());
            }

            String temp = (String) numbers.get(DicomConstants.TRIAL_VISIBILITY);

            if ((temp != null) &&
                    temp.equals(DicomConstants.SPECIFIC_CHARACTER_SET)) {
                visibility = true;
            } else {
                visibility = false;
            }

         // get the checksum with the file unannoymized for DB verifier to work
            String md5 = file.getDigest()== null? " " : file.getDigest();
            imageStorage.setMd5(md5);
            Status status = imageStorage.storeDicomObject(numbers,filename,visibility);
            if(status.equals(Status.FAIL)) {
                log.error("Rollback in process(DicomObject,String) for file " + file.getFile().getAbsolutePath());
                failedSubmission("Rollback in process(DicomObject,String) for file " + file.getFile().getAbsolutePath());
            }

        }
        catch (Exception e) {
            failedSubmission("Rollback in process(DicomObject,String) for file " + file.getFile().getAbsolutePath());
        }
    }


    /* (non-Javadoc)
     * @see org.rsna.mircsite.util.DatabaseAdapter#process(org.rsna.mircsite.util.XmlObject, java.lang.String)
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public Status process(XmlObject file,
                          File storedFile,
                          String url) {

        return annotationStorage.process(file, storedFile);
    }

    /* (non-Javadoc)
     * @see org.rsna.mircsite.util.DatabaseAdapter#process(org.rsna.mircsite.util.ZipObject, java.lang.String)
     */
    @Transactional (propagation=Propagation.REQUIRED)
    public Status process(ZipObject file,
                          File storedFile,
                          String url) {

        return annotationStorage.process(file, storedFile);
    }

    @Transactional (propagation=Propagation.REQUIRED)
    public Status process(FileObject file, File storedFile, String url) {
        String fileExtension = file.getExtension();
        log.info("file extension: " + fileExtension);
        log.error("FileObject is not supported yet" + storedFile.getAbsolutePath() + "\tfile extension is " + fileExtension);

        return Status.FAIL;
    }

    public Map<String, UIDResult> uidQuery(Set<String> uidSet) {
        Map<String, UIDResult> result = null;
        try
        {
            sopQuery.setDicomSOPInstanceUIDQuery(uidSet);
            result = sopQuery.getUIDResult();
        }catch(Exception e)
        {
            log.error("In NCIA database uidQuery method, " + e.getMessage());
        }

        return result;
    }

    ////////////////////////////////////////PACAKAGE/////////////////////////////////////////////

    static String handleSQField(Dataset dicomSet, int pName) throws Exception {
        String elementHeader = "";
        DcmElement dcm_Element = dicomSet.get(pName);
           if (dcm_Element != null){
            for (int i = 0; i < dcm_Element.countItems(); i++)
            {
                Dataset ds = dcm_Element.getItem(i);
                Iterator iterator = ds.iterator();
                while(iterator.hasNext())
                {
                    DcmElement dcmElement = (DcmElement)iterator.next();
                    String tagIdentifier = getTagIdentifierByTagId(dcmElement.tag());
                    String elementValue = dcmElement.getString(null);
                    elementHeader += tagIdentifier + "=" + elementValue + "/";
                }
            }
            elementHeader = elementHeader.substring(0,elementHeader.lastIndexOf('/'));
           }
        return elementHeader;
    }

    /////////////////////////////////////////PRIVATE///////////////////////////////////////////
    Logger log = Logger.getLogger(NCIADatabaseDelegator.class);

    @Autowired
    private AdapterUtil adapterUtil;

    @Autowired
    private ImageStorage imageStorage;

    @Autowired
    private AnnotationStorage annotationStorage;

    @Autowired
    private    DicomSOPInstanceUIDQueryInterface sopQuery;

    Map numbers = new HashMap();
    Properties dicomProp = new Properties();
    static final String DICOM_PROPERITIES = "dicom.properties";

    ClassPathXmlApplicationContext ctx;
    NCIADatabaseDelegator nciaDelegator;


    /**
     * If ALL of the group 13 tags aren't found in the number map,
     * then reject this submission.
     *
     * <p>Historically this method tried to piece together the missing provenance
     * information if some of the provenance information wasn't found.
     * Too complicated for to little gain... so submitters must include
     * all provenance information.
     */
    private boolean preProcess() throws Exception {
        boolean ok = false;
        // Pass the check if none of the project name, site id, site name and trial name is null.
        if ((numbers.get(DicomConstants.PROJECT_NAME) != null) &&
            (numbers.get(DicomConstants.SITE_ID) != null) &&
            (numbers.get(DicomConstants.SITE_NAME) != null) &&
            (numbers.get(DicomConstants.TRIAL_NAME) != null)) {

              ok = adapterUtil.checkSeriesStatus(numbers);
        }
        return ok;
    }

    private static boolean isSQFieldThatWeCareAbout(String propname) {
        return propname.equals("00081084") || propname.equals("00082218");
    }

    private static boolean isMultiStringFieldThatWeCareAbout(String propname) {
        return propname.equals("00200037") || propname.equals("00200032")||propname.equals("00080008")||propname.equals("00180021");
    }



    private static String getTagIdentifierByTagId(int tag)
    {
        TagDictionary  dict = DictionaryFactory.getInstance().getDefaultTagDictionary();
        String tagIdentifier = dict.toString(tag) ;
        int beginIndex = tagIdentifier.indexOf('(');
        int endIndex = tagIdentifier.indexOf(')');
        tagIdentifier = tagIdentifier.substring(beginIndex, endIndex+1);

        return tagIdentifier;
    }
    private void parseDICOMPropertiesFile(Dataset dicomSet)
        throws Exception {
        InputStream in = Thread.currentThread().getContextClassLoader()
                               .getResourceAsStream(DICOM_PROPERITIES);
        dicomProp.load(in);

        Enumeration enum1 = dicomProp.propertyNames();

        while (enum1.hasMoreElements()) {
            String propname = enum1.nextElement().toString();
            int pName = Integer.parseInt(propname, 16);
            String elementheader = null;

            if (isMultiStringFieldThatWeCareAbout(propname)) {
                String[] temp = dicomSet.getStrings(pName);

                if ((temp != null) && (temp.length > 0)) {
                    elementheader = temp[0];

                    for (int i = 1; i < temp.length; i++) {
                        elementheader += ("\\" + temp[i]);
                    }
                }
            }
            else if (isSQFieldThatWeCareAbout(propname))
            {
                elementheader = handleSQField(dicomSet, pName);
            }
            else {
                try {
                    elementheader = getElementValue(pName, dicomSet);
                } catch (UnsupportedOperationException uoe) {
                    elementheader = null;
                }
            }

            if (elementheader != null) {
                elementheader = elementheader.replaceAll("'", "");

                String[] temp = dicomProp.getProperty(propname).split("\t");
                numbers.put(temp[0], elementheader);

                if(log.isDebugEnabled()) {
                    log.debug("Parsing:"+propname+"/"+temp[0]+" as "+elementheader);
                }
            }
        } //while
    }

    /**
     * Get the contents of a DICOM element in the DicomObject's dataset.
     * If the element is part of a private group owned by CTP, it returns the
     * value as text. This method returns the defaultString argument if the
     * element does not exist.
     * @param tag the tag specifying the element (in the form 0xggggeeee).
     * @param dataset is Dicom dataset.
     * @return the text of the element, or defaultString if the element does not exist.
     * Notes: Make sure defaultString initial value must be null
     */
    private String getElementValue(int tag, Dataset dataset) {
        boolean ctp = false;

        //if group is odd (private) and element is > 1000 (VR=UN or content v. header)
        if (((tag & 0x00010000) != 0) && ((tag & 0x0000ff00) != 0)) {

            //each block of 1000hex in the tag address space maps
            //back to a slot in the first 1000hex.  from the tag address
            //compute the header address.  so 0013,1010 -> 0013, 0010
            //
            //at a bit level - strip the element/keep the group and
            //then compute the element by stripping of the bottom two
            //
            int blk = (tag & 0xffff0000) | ((tag & 0x0000ff00) >> 8);
            try {
                ctp = dataset.getString(blk).equals("CTP");
            }
            catch (Exception notCTP) {
                log.warn("Is [0013,0010] missing, or it doesnt equal CTP?");
                notCTP.printStackTrace();
            }
        }
        String value = null;
        try {
            if (ctp) {
               value = new String(dataset.getByteBuffer(tag).array());
            }
            else {
               value = dataset.getString(tag);
            }
        }
        catch (Exception notAvailable) {
        	//notAvailable.printStackTrace();
            log.warn("in NICADatabase class, cannot get element value"+Integer.toHexString(tag));
        }
        if (value != null) {
            value = value.trim();
         }
        return value;
    }


    private void failedSubmission(String message) throws RuntimeException
    {
        throw new RuntimeException(message);
    }

    public void setCorrectFileSize(File file) {
    	 // Temporary fix until new CTP release provides a better solution
    	long fileSize = file.length();
        imageStorage.setFileSize(fileSize);
        /*JP needs the digest with file not being anonymized for DB Verifier to work
    	try {
        DicomObject tempFile = new DicomObject(file);
        String md5 = tempFile.getDigest()== null? " " : tempFile.getDigest();

        imageStorage.setMd5(md5);
        file.delete();
        }
    	catch (Exception ex) {
    		log.warn("Bad DICOM file:"+file.getAbsolutePath());
    	}
    	*/
    }
}
