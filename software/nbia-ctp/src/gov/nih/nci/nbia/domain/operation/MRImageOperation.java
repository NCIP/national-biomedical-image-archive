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
package gov.nih.nci.nbia.domain.operation;

import gov.nih.nci.nbia.internaldomain.GeneralImage;
import gov.nih.nci.nbia.internaldomain.MRImage;
import gov.nih.nci.nbia.util.DicomConstants;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public class MRImageOperation extends DomainOperation implements
        MRImageOperationInterface {

    private GeneralImage gi;

    public MRImageOperation() {
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object validate(Map numbers) throws Exception {

        MRImage mri = (MRImage) SpringApplicationContext.getBean("mrimage");

        try {
            String hql = "from MRImage as image where ";

            hql += (" image.generalImage.id = " + this.gi.getId()); // general_image
                                                                    // pk_id

            mri.setGeneralImage(gi);
            mri.setGeneralSeries(gi.getGeneralSeries());

            // the update method should be changed to search
            // mri = (MRImage) this.update(hql, mri);
            List ret = getHibernateTemplate().find(hql);
            if (ret != null && ret.size() > 0) {
                if (ret.size() == 1) {
                    mri = (MRImage) ret.get(0);
                } else if (ret.size() > 1) {
                    throw new Exception(
                            "mr_image table has duplicate records, please contact Data Team to fix data, then upload data again");
                }
            }

            populateMRImageFromNumbers(numbers, mri);

        } catch (Exception e) {
            // log.error("Exception in MRImageOperation " + e);
            throw new Exception("Exception in MRImageOperation " + e);
        }

        return mri;

    }

    public void setGeneralImage(GeneralImage gi) {
        this.gi = gi;
    }

    /**
     * Given the "numbers" map with all the parsed out dicom tag values we care
     * about..... populate the general image object with these values.
     */
    private static void populateMRImageFromNumbers(Map numbers, MRImage mri)
            throws Exception {
        String temp;

        if ((temp = (String) numbers.get(DicomConstants.IMAGE_TYPE)) != null) {
            String[] token = temp.split("\\\\");
            if (token.length >= 3) {
                mri.setImageTypeValue3(token[2]);
            }
        }

        if ((temp = (String) numbers.get(DicomConstants.SCANNING_SEQUENCE)) != null) {
            mri.setScanningSequence(temp.trim());
        }

        if ((temp = (String) numbers.get(DicomConstants.SEQUENCE_VARIANT)) != null) {
            mri.setSequenceVariant(temp.trim());
        }

        if ((temp = (String) numbers.get(DicomConstants.REPETITION_TIME)) != null) {
            mri.setRepetitionTime(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.ECHO_TIME)) != null) {
            mri.setEchoTime(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.INVERSION_TIME)) != null) {
            mri.setInversionTime(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.SEQUENCE_NAME)) != null) {
            mri.setSequenceName(temp.trim());
        }

        if ((temp = (String) numbers.get(DicomConstants.IMAGED_NUCLEUS)) != null) {
            mri.setImagedNucleus(temp.trim());
        }

        if ((temp = (String) numbers
                .get(DicomConstants.MAGNETIC_FIELD_STRENGTH)) != null) {
            mri.setMagneticFieldStrength(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.SAR)) != null) {
            mri.setSar(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.DB_DT)) != null) {
            mri.setDbDt(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.TRIGGER_TIME)) != null) {
            mri.setTriggerTime(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.ANGIO_FLAG)) != null) {
            mri.setAngioFlag(temp.trim());
        }
        // Please un-comment this for next release for better debug. commented
        // this bcoz need to add UNKNOWN for imageType3 for existing MR images
        // if (StringUtils.isEmpty(mri.getImageTypeValue3())) {
        // throw new Exception("Image Type 3 cannot be null");
        // }
        // if (StringUtils.isEmpty(mri.getScanningSequence())) {
        // throw new Exception("Scanning Sequence cannot be null");
        // }
        // if (StringUtils.isEmpty(mri.getSequenceVariant())) {
        // throw new Exception("Scanning Variant cannot be null");
        // }
    }
}
