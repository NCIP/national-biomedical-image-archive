package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.domain.operation.MRImageOperationInterface;
import gov.nih.nci.nbia.internaldomain.GeneralImage;
import gov.nih.nci.nbia.internaldomain.MRImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmElement;
import org.dcm4che.dict.DictionaryFactory;
import org.dcm4che.dict.TagDictionary;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.rsna.ctp.objects.DicomObject;
import org.rsna.ctp.pipeline.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MRUtil {
    private static Logger logger = Logger.getLogger(MRUtil.class);
    private static final int rowsPerCommit = 1000;
    private ClassPathXmlApplicationContext ctx = null;
    MRUtil mrUtil = null;
    @Autowired
    private MRImageOperationInterface mrio;
    Map dicomTagMap = new HashMap();
    Properties dicomProp = new Properties();
    static final String DICOM_PROPERITIES = "dicom.properties";

    public static void main(String[] args) {
        String LOG_FILE = "dbUpdatorLog4j.properties";
        Properties logProp = new Properties();
        ClassLoader classLoader = MRUtil.class.getClassLoader();
        try {
            logProp.load(classLoader.getResourceAsStream(LOG_FILE));
            PropertyConfigurator.configure(logProp);
            System.out.println("Logging enabled");
            logger.info("Logging enabled");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Logging not enabled");
        }
        new MRUtil().reProcessMR();
        logger.info("completed the re processing of MR images");
    }

    public void reProcessMR() {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        mrio = (MRImageOperationInterface) ctx.getBean("mrio");
        logger.info("loaded the application context");
        SessionFactory sf1 = (SessionFactory) ctx.getBean("sessionFactory");
        Session s = sf1.openSession();
        try {
            // get the MR images
            String hql = "SELECT gi.id,"
                    + "gi.filename FROM GeneralImage as gi, GeneralSeries as gs  WHERE gi.seriesPKId = gs.id AND gs.modality = 'MR'"
                    + "and gi.id not in ( Select generalImage.id from MRImage)";
            s.getTransaction().begin();
            Query q = s.createQuery(hql);
            q.setFirstResult(0);
            List<Object[]> searchResults = q.list();
            int commitSize = 0;
            DicomObject dicomObjectFile = null;
            for (Object[] row : searchResults) {
                try {
                    Integer giId = (Integer) row[0];
                    String dicomUri = (String) row[1];
                    File storedFile = new File(dicomUri);
                    dicomObjectFile = new DicomObject(storedFile);
                    Dataset set = dicomObjectFile.getDataset();
                    parseDICOMPropertiesFile(set);
                    Status stat = updateStoredDicomObject(s, giId);
                    logger.info("Status of reprocessed MR Image:- " + stat
                            + " image pk Id:- " + giId + "  dicomUri:- "
                            + dicomUri);
                    commitSize++;
                    if (commitSize == rowsPerCommit) {
                        logger.info("flusing records..");
                        s.flush();
                        if (!s.getTransaction().wasCommitted()) {
                            logger.info("committing records..");
                            s.getTransaction().commit();
                        }
                        commitSize = 0;
                    }

                } catch (Exception e) {
                    logger.error(e);
                    logger.info("continue processing");
                } finally {
                    if (dicomObjectFile != null) {
                        dicomObjectFile.close();
                    }
                }
            }
            s.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private Status updateStoredDicomObject(Session s, Integer imagePkId) {
        GeneralImage gi = null;
        try {
            gi = (GeneralImage) s.load(GeneralImage.class, imagePkId);
        } catch (Exception e) {
            logger.error("File " + gi.getFilename() + " " + e);
            e.printStackTrace();
            return Status.FAIL;
        }
        if (dicomTagMap.get(DicomConstants.MODALITY).equals("MR")) {
            try {
                mrio.setGeneralImage(gi);
                MRImage mr = (MRImage) mrio.validate(dicomTagMap);
                mr.setGeneralSeries(gi.getGeneralSeries());
                s.merge(mr);
            } catch (Exception e) {
                logger.error("Exception in MRImageOperation " + e);
                return Status.FAIL;
            }
        }
        return Status.OK;
    }

    private void parseDICOMPropertiesFile(Dataset dicomSet) throws Exception {
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
            } else if (isSQFieldThatWeCareAbout(propname)) {
                elementheader = handleSQField(dicomSet, pName);
            } else {
                try {
                    elementheader = getElementValue(pName, dicomSet);
                } catch (UnsupportedOperationException uoe) {
                    elementheader = null;
                }
            }
            if (elementheader != null) {
                elementheader = elementheader.replaceAll("'", "");

                String[] temp = dicomProp.getProperty(propname).split("\t");
                dicomTagMap.put(temp[0], elementheader);
                if (logger.isDebugEnabled()) {
                    logger.debug("Parsing:" + propname + "/" + temp[0] + " as "
                            + elementheader);
                }
            }
        } // while
        in.close();
    }

    private boolean isSQFieldThatWeCareAbout(String propname) {
        return propname.equals("00081084") || propname.equals("00082218");
    }

    private boolean isMultiStringFieldThatWeCareAbout(String propname) {
        return propname.equals("00200037") || propname.equals("00200032")
                || propname.equals("00080008") || propname.equals("00180021");
    }

    static String handleSQField(Dataset dicomSet, int pName) throws Exception {
        String elementHeader = "";
        DcmElement dcm_Element = dicomSet.get(pName);
        if (dcm_Element != null) {
            for (int i = 0; i < dcm_Element.countItems(); i++) {
                Dataset ds = dcm_Element.getItem(i);
                Iterator iterator = ds.iterator();
                while (iterator.hasNext()) {
                    DcmElement dcmElement = (DcmElement) iterator.next();
                    String tagIdentifier = getTagIdentifierByTagId(dcmElement
                            .tag());
                    String elementValue = dcmElement.getString(null);
                    elementHeader += tagIdentifier + "=" + elementValue + "/";
                }
            }
            elementHeader = elementHeader.substring(0,
                    elementHeader.lastIndexOf('/'));
        }
        return elementHeader;
    }

    private static String getTagIdentifierByTagId(int tag) {
        TagDictionary dict = DictionaryFactory.getInstance()
                .getDefaultTagDictionary();
        String tagIdentifier = dict.toString(tag);
        int beginIndex = tagIdentifier.indexOf('(');
        int endIndex = tagIdentifier.indexOf(')');
        tagIdentifier = tagIdentifier.substring(beginIndex, endIndex + 1);

        return tagIdentifier;
    }

    private String getElementValue(int tag, Dataset dataset) {
        String value = null;
        try {
            value = dataset.getString(tag);
        } catch (Exception notAvailable) {
            logger.warn("in MRUtil, cannot get element value"
                    + Integer.toHexString(tag));
        }
        if (value != null) {
            value = value.trim();
        }
        return value;
    }

}
