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

import gov.nih.nci.nbia.internaldomain.CTImage;
import gov.nih.nci.nbia.internaldomain.GeneralImage;
import gov.nih.nci.nbia.util.DicomConstants;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public class CTImageOperation extends DomainOperation implements CTImageOperationInterface{
	
	private GeneralImage gi;

	public CTImageOperation() {
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Object validate(Map numbers) throws Exception {
		
		CTImage cti = (CTImage)SpringApplicationContext.getBean("ctimage");
		
		try {
	        String hql = "from CTImage as image where ";        
	
	        hql += (" image.generalImage.id = " + this.gi.getId()); //general_image pk_id
	
	        cti.setGeneralImage(gi);
	        cti.setGeneralSeries(gi.getGeneralSeries());
	        
	        //the update method should be changed to search
	       // cti = (CTImage) this.update(hql, cti);
	        List ret = getHibernateTemplate().find(hql);
	        if(ret != null && ret.size() > 0) {
	        	if(ret.size() == 1) {
	        		cti = (CTImage)ret.get(0);
	        	}else if (ret.size() > 1){
	        		throw new Exception("ct_image table has duplicate records, please contact Data Team to fix data, then upload data again");
	        	}
	        }
	
	        populateCTImageFromNumbers(numbers, cti);
	        
//	        if (this.visibility) {
//	            cti.setVisibility("1");
//	        }
		}catch(Exception e) {
			//log.error("Exception in CTImageOperation " + e);
			throw new Exception("Exception in CTImageOperation " + e);
		}

        return cti;
		
	}
	
	public void setGeneralImage(GeneralImage gi) {
		this.gi = gi;
	}
//	public void setVisibility(boolean visibility) {
//		this.visibility = visibility;
//		
//	}
	
    /**
     * Given the "numbers" map with all the parsed out dicom tag values we
     * care about..... populate the general image object with these values.
     */
    private static void populateCTImageFromNumbers(Map numbers, 
    		                                       CTImage cti) throws Exception {
		String temp;

    	if ((temp = (String) numbers.get(DicomConstants.KVP)) != null) {
            cti.setKVP(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.SCAN_OPTIONS)) != null) {
            cti.setScanOptions(temp.trim());
        }

        if ((temp = (String) numbers.get(
                        DicomConstants.DATA_COLLECTION_DIAMETER)) != null) {
            cti.setDataCollectionDiameter(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.RECONSTRUCTION_DIAMETER)) != null) {
            cti.setReconstructionDiameter(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.GANTRY_DETECTOR_TILT)) != null) {
            cti.setGantryDetectorTilt(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.EXPOSURE_TIME)) != null) {
            cti.setExposureTime(Integer.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.X_RAY_TUBE_CURRENT)) != null) {
            cti.setXRayTubeCurrent(Integer.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.EXPOSURE)) != null) {
            cti.setExposure(Integer.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.EXPOSURE_IN_MAS)) != null) {
            cti.setExposureInMicroAs(Integer.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.CONVOLUTION_KERNEL)) != null) {
            cti.setConvolutionKernel(temp.trim());
        }

        if ((temp = (String) numbers.get(DicomConstants.REVOLUTION_TIME)) != null) {
            cti.setRevolutionTime(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(
                        DicomConstants.SINGLE_COLLIMATION_WIDTH)) != null) {
            cti.setSingleCollimationWidth(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.TOTAL_COLLIMATION_WIDTH)) != null) {
            cti.setTotalCollimationWidth(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.TABLE_SPEED)) != null) {
            cti.setTableSpeed(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.TABLE_FEED_PER_ROTATION)) != null) {
            cti.setTableFeedPerRotation(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(DicomConstants.CT_PITCH_FACTOR)) != null) {
            cti.setCTPitchFactor(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get(
                        DicomConstants.ANATOMIC_REGION_SEQUENCE)) != null) {
            cti.setAnatomicRegionSeq(temp.trim());
        }    	
    }	
}
