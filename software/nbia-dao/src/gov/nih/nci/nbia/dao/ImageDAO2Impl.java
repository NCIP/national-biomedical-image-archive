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
package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ImageDTO2;
import gov.nih.nci.nbia.internaldomain.GeneralImage;
import gov.nih.nci.nbia.internaldomain.TrialDataProvenance;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lethai
 *
 */
public class ImageDAO2Impl extends AbstractDAO
                          implements ImageDAO2 {
	private static Logger logger = Logger.getLogger(ImageDAO2.class);

    /**
     * Return all the images for a given series.  Optionally exclude
     * sop instance uid's from the returned list.
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public List<ImageDTO2> findImagesBySeriesUid(String seriesUid,
    		                                    String exclusionSopUidList) throws DataAccessException {
    	String query="";
    	if(exclusionSopUidList.equals("")) {
    		query = "select distinct gimg.SOPInstanceUID, gimg.filename, gimg.dicomSize, gimg.usFrameNum,dp.project, dp.dpSiteName, gs.securityGroup " +
    				"from GeneralImage gimg join gimg.dataProvenance dp  join gimg.generalSeries gs " +
    				"where gimg.seriesInstanceUID = '"+
                    seriesUid + "'";
    	}
    	else {
    		query = "select distinct gimg.SOPInstanceUID, gimg.filename, gimg.dicomSize, gimg.usFrameNum,dp.project, dp.dpSiteName, gs.securityGroup " +
    				"from GeneralImage gimg join gimg.dataProvenance dp  join gimg.generalSeries gs " +
    				"where gimg.seriesInstanceUID = '"+
                    seriesUid +
                    "' and gimg.SOPInstanceUID not in (" + exclusionSopUidList + ")";
    	}
    	// Submit the search
        long start = System.currentTimeMillis();
    	logger.info("Issuing query: ");
        List results = getHibernateTemplate().find(query);
        long end = System.currentTimeMillis();
        logger.info("total query time: " + (end - start) + " ms");
        List<ImageDTO2> imageResults = new ArrayList<ImageDTO2>();

        if(results == null || results.isEmpty()){
        	logger.info("No image found for request seriesuid="+seriesUid);
        	return imageResults;
        }
//        TrialDataProvenance tdp = results.get(0).getDataProvenance();
//        String ssg = results.get(0).getGeneralSeries().getSecurityGroup();
        for(Object item: results){
        	Object[] row = (Object[]) item;

//        	ImageDTO2 image = new ImageDTO2(gi.getSOPInstanceUID(),
//        			gi.getFilename(),
//        			gi.getDicomSize(),
//        			tdp.getProject(),
//        			tdp.getDpSiteName(),
//        			ssg, gi.getUsFrameNum());
        	ImageDTO2 image = new ImageDTO2((String)row[0],
        			(String)row[1],
        			(Long)row[2],
        			(String)row[4],
        			(String)row[5],
        			(String)row[6], (String)row[3]);
        	imageResults.add(image);
        }
        return imageResults;
    }
	
	/**
	 * Fetch set of list of file path/name for given the series instance uid
	 * This method is used for NBIA Rest API.
	 * @param seriesInstanceUid series instance UID
	 */
	@Transactional(propagation=Propagation.REQUIRED)	
	public List<String> getImage(String seriesInstanceUid) throws DataAccessException {
		String hql = "select gi.filename "
				+ "from  GeneralImage gi "
				+ "where gi.generalSeries.visibility in ('1', '12')"
				+ " and UPPER(gi.seriesInstanceUID) = ? ";
		
		System.out.println("===== In nbia-dao, ImageDAO2Impl:getImage(..) - hql statement call with where visibility in ('1', '12'): " + hql);
		
				List<String> rs = getHibernateTemplate().find(hql, seriesInstanceUid.toUpperCase()); // protect against sql injection				

        return rs;
	}
}
