/**
 *
 */
package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.ncia.internaldomain.GeneralImage;
import gov.nih.nci.ncia.internaldomain.TrialDataProvenance;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import gov.nih.nci.ncia.dao.AbstractDAO;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lethai
 *
 */
public class ImageDAOImpl extends AbstractDAO
                          implements ImageDAO {
	private static Logger logger = Logger.getLogger(ImageDAO.class);

    /**
     * Return all the images for a given series.  Optionally exclude
     * sop instance uid's from the returned list.
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public List<ImageDTO> findImagesBySeriesUid(String seriesUid,
    		                                    String exclusionSopUidList) throws DataAccessException {
    	String query="";
    	if(exclusionSopUidList.equals("")) {
    		query = "select distinct gimg from GeneralImage gimg join gimg.dataProvenance dp where gimg.seriesInstanceUID = '"+
                    seriesUid + "'";
    	}
    	else {
    		query = "select distinct gimg from GeneralImage gimg join gimg.dataProvenance dp where gimg.seriesInstanceUID = '"+
                    seriesUid +
                    "' and gimg.SOPInstanceUID not in (" + exclusionSopUidList + ")";
    	}


        List<GeneralImage> results = getHibernateTemplate().find(query);
        List<ImageDTO> imageResults = new ArrayList<ImageDTO>();

        if(results == null || results.isEmpty()){
        	logger.info("No image found for request seriesuid="+seriesUid);
        	return imageResults;
        }
        TrialDataProvenance tdp = results.get(0).getDataProvenance();
        String ssg = results.get(0).getGeneralSeries().getSecurityGroup();
        for(GeneralImage gi: results){
        	ImageDTO image = new ImageDTO(gi.getSOPInstanceUID(),
        			gi.getFilename(),
        			gi.getDicomSize(),
        			tdp.getProject(),
        			tdp.getDpSiteName(),
        			ssg, gi.getUsFrameNum());
        	imageResults.add(image);
        }
        return imageResults;
    }
}
