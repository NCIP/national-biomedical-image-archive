/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: CTImageBuilder.java 6363 2008-09-16 19:15:43Z kascice $
*
* $Log: not supported by cvs2svn $
* Revision 1.17  2006/12/19 16:24:42  zhouro
* changed data type from Integer to Double
*
* Revision 1.16  2006/09/28 19:29:00  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Jul 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.ncia.tool.mirctest;

import gov.nih.nci.ncia.db.IDataAccess;
import gov.nih.nci.ncia.domain.CTImage;
import gov.nih.nci.ncia.domain.GeneralImage;
import gov.nih.nci.ncia.util.DicomConstants;

import java.util.Hashtable;
import java.util.List;
import org.apache.log4j.Logger;


/**
 * @author Rona Zhou
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CTImageBuilder extends DataBuilder  {
    Logger log = Logger.getLogger(CTImageBuilder.class);

    public CTImageBuilder(IDataAccess ida) throws Exception{
    	super(ida);
    }

    public CTImage buildCTImage(Hashtable numbers,GeneralImage gi) throws Exception {
        String temp;
        String hql = "from CTImage as image where ";
        CTImage cti = new CTImage();

        hql += (" image.generalImage.id = " + gi.getId());

        cti.setGeneralImage(gi);

        //System.out.println( "CTIMAGE HQL: " + hql );
        cti = (CTImage) this.update(hql, cti);

        if ((temp = (String) numbers.get("59")) != null) {
            cti.setKVP(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("56")) != null) {
            cti.setScanOptions(temp.trim());
        }

        if ((temp = (String) numbers.get("60")) != null) {
            cti.setDataCollectionDiameter(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("64")) != null) {
            cti.setReconstructionDiameter(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("67")) != null) {
            cti.setGantryDetectorTilt(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("68")) != null) {
            cti.setExposureTime(Integer.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("69")) != null) {
            cti.setXRayTubeCurrent(Integer.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("70")) != null) {
            cti.setExposure(Integer.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("71")) != null) {
            cti.setExposureInMicroAs(Integer.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("74")) != null) {
            cti.setConvolutionKernel(temp.trim());
        }

        if ((temp = (String) numbers.get("76")) != null) {
            cti.setRevolutionTime(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("77")) != null) {
            cti.setSingleCollimationWidth(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("78")) != null) {
            cti.setTotalCollimationWidth(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("79")) != null) {
            cti.setTableSpeed(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("80")) != null) {
            cti.setTableFeedPerRotation(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("81")) != null) {
            cti.setCTPitchFactor(Double.valueOf(temp.trim()));
        }

        if ((temp = (String) numbers.get("25")) != null) {
            cti.setAnatomicRegionSeq(temp.trim());
        }

        return cti;
    }

	public   CTImage retrieveCTImageFromDB(Integer ctImagePkId) throws Exception {
        	
            String hql = "from CTImage as image where ";

            if (ctImagePkId != null) {
                hql += (" image.id = " + ctImagePkId + " ");
            }

            List retList = null;
            retList = (List) getIDataAccess().search(hql);

            CTImage ct=null;
            if ((retList != null) && (retList.size() != 0)) {
                ct = (CTImage) retList.get(0);
            } else {
                System.out.println("cannot find the ct image");
            }
            return ct;
    }
   
    public Hashtable addCTImage(Hashtable numbersInDb, CTImage ct) {

    	if (ct==null) {
    		System.out.println("CT Image is null");
    		return numbersInDb;
    	}
    	if (ct.getKVP()!=null) {
    		numbersInDb.put(DicomConstants.KVP, ct.getKVP());
    	}
    	if (ct.getScanOptions()!=null) {
    		numbersInDb.put(DicomConstants.SCAN_OPTIONS, ct.getScanOptions());
    	}
    	if (ct.getDataCollectionDiameter()!=null) {
    		numbersInDb.put(DicomConstants.DATA_COLLECTION_DIAMETER, ct.getDataCollectionDiameter());
    	}
    	if (ct.getReconstructionDiameter()!=null) {
    		numbersInDb.put(DicomConstants.RECONSTRUCTION_DIAMETER, ct.getReconstructionDiameter());
    	}
    	if (ct.getGantryDetectorTilt()!=null) {
    		numbersInDb.put(DicomConstants.GANTRY_DETECTOR_TILT, ct.getGantryDetectorTilt());
    	}
    	if (ct.getXRayTubeCurrent()!=null) {
    		numbersInDb.put(DicomConstants.X_RAY_TUBE_CURRENT, ct.getXRayTubeCurrent());
    	}
    	if (ct.getExposure()!=null) {
    		numbersInDb.put(DicomConstants.EXPOSURE, ct.getExposure());
    	}
    	if (ct.getExposureInMicroAs()!=null) {
    		numbersInDb.put(DicomConstants.EXPOSURE_IN_MAS, ct.getExposureInMicroAs());
    	}
    	if (ct.getConvolutionKernel()!=null) {
    		numbersInDb.put(DicomConstants.CONVOLUTION_KERNEL, ct.getConvolutionKernel());
    	}
    	if (ct.getRevolutionTime()!=null) {
    		numbersInDb.put(DicomConstants.REVOLUTION_TIME, ct.getRevolutionTime());
    	}
    	if (ct.getSingleCollimationWidth()!=null) {
    		numbersInDb.put(DicomConstants.SINGLE_COLLIMATION_WIDTH, ct.getSingleCollimationWidth());
    	}
    	if (ct.getTotalCollimationWidth()!=null) {
    		numbersInDb.put(DicomConstants.TOTAL_COLLIMATION_WIDTH, ct.getTotalCollimationWidth());
    	}
    	if (ct.getTableSpeed()!=null) {
    		numbersInDb.put(DicomConstants.TABLE_SPEED, ct.getTableSpeed());
    	}
    	if (ct.getTableFeedPerRotation()!=null) {
    		numbersInDb.put(DicomConstants.TABLE_FEED_PER_ROTATION, ct.getTableFeedPerRotation());
    	}
    	if (ct.getCTPitchFactor()!=null) {
    		numbersInDb.put(DicomConstants.CT_PITCH_FACTOR, ct.getCTPitchFactor());
    	}
    	if (ct.getAnatomicRegionSeq()!=null) {
    		numbersInDb.put(DicomConstants.ANATOMIC_REGION_SEQUENCE, ct.getAnatomicRegionSeq());
    	}
    	if (ct.getExposureTime()!=null) {
    		numbersInDb.put(DicomConstants.EXPOSURE_TIME, ct.getExposureTime());
    	}

		return numbersInDb;
    }
}
