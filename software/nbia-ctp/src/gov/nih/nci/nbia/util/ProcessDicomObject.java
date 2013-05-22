/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.domain.operation.MRImageOperationInterface;
import gov.nih.nci.nbia.internaldomain.GeneralImage;
import gov.nih.nci.nbia.internaldomain.MRImage;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmElement;
import org.dcm4che.dict.DictionaryFactory;
import org.dcm4che.dict.TagDictionary;
import org.hibernate.Session;
import org.rsna.ctp.objects.DicomObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProcessDicomObject extends HibernateDaoSupport {
    @Autowired
    private MRImageOperationInterface mrio;
    private static Logger logger = Logger.getLogger(ProcessDicomObject.class);
    private static final int rowsPerCommit = 1000;
    Map dicomTagMap = new HashMap();
    Properties dicomProp = new Properties();
    static final String DICOM_PROPERITIES = "dicom.properties";

    public void process(List<Object[]> searchResults) {
        logger.info("start processing the result");
        int commitSize = 0;
        DicomObject dicomObjectFile = null;
        Session s = getHibernateTemplate().getSessionFactory().openSession();
        for (Object[] row : searchResults) {
            try {
                Integer giId = (Integer) row[0];
                String dicomUri = (String) row[1];
                File storedFile = new File(dicomUri);
                dicomObjectFile = new DicomObject(storedFile);
                Dataset set = dicomObjectFile.getDataset();
                parseDICOMPropertiesFile(set);
                updateStoredDicomObject(s, giId);
                logger.info("MR Image: image pk Id:- " + giId + "  dicomUri:- "
                        + dicomUri);
                commitSize++;
                if (commitSize == rowsPerCommit) {
                    logger.info("committing records..");
                    commitSize = 0;
                    s.flush();
                    s.clear();
                }

            } catch (Exception e) {
                s.clear();
                logger.error(e);
                logger.info("continue processing");
            } finally {
                if (dicomObjectFile != null) {
                    dicomObjectFile.close();
                }
            }
        }
        s.close();
    }

    private void updateStoredDicomObject(Session s, Integer imagePkId)
            throws Exception {
        GeneralImage gi = null;
        gi = (GeneralImage) s.load(GeneralImage.class, imagePkId);
        if (dicomTagMap.get(DicomConstants.MODALITY).equals("MR")) {
            mrio.setGeneralImage(gi);
            MRImage mr = null;
            mr = (MRImage) mrio.validate(dicomTagMap);
            if (StringUtils.isEmpty(mr.getImageTypeValue3())) {
                logger.info("Image type 3 is null; set it to UNKNOWN; image id:-"
                        + imagePkId);
                mr.setImageTypeValue3("UNKNOWN");
            }
            mr.setGeneralSeries(gi.getGeneralSeries());
            getHibernateTemplate().saveOrUpdate(mr);
        }
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
