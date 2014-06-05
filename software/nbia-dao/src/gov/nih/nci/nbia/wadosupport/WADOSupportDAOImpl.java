/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.wadosupport;

import gov.nih.nci.nbia.dao.AbstractDAO;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.io.FileUtils;
import java.io.*;
import gov.nih.nci.nbia.security.*;
import gov.nih.nci.nbia.util.*;

public class WADOSupportDAOImpl extends AbstractDAO
                               implements WADOSupportDAO
{
	static Logger log = Logger.getLogger(WADOSupportDAOImpl.class);
    
    private final static String WADO_QUERY="select distinct gs.project, gs.site, dicom_file_uri from general_image gi, general_series gs" +
    		" where gs.study_instance_uid = :study and gs.series_instance_uid = :series and gi.sop_instance_uid = :image " +
    		"  and gs.general_series_pk_id and gi.general_series_pk_id";

public WADOSupportDTO getWADOSupportDTO(String study, String series, String image)
{
	String user =  NCIAConfig.getGuestUsername();
	return getWADOSupportDTO(study, series, image, user );
}
	
@Transactional(propagation=Propagation.REQUIRED)
public WADOSupportDTO getWADOSupportDTO(String study, String series, String image, String user )
{
	WADOSupportDTO returnValue = new WADOSupportDTO();
	log.info("Study-"+study+" series-"+series+" image-"+image);
	try {
		List <Object[]>images= this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(WADO_QUERY)
		  .setParameter("study", study)
		  .setParameter("series", series)
		  .setParameter("image", image).list();
		if (images.size()==0) {
			   log.info("image not found");
			   return null; //nothing to do
		}
		AuthorizationManager manager = new AuthorizationManager(user);
		List<SiteData> authorizedSites = manager.getAuthorizedSites();
		returnValue.setCollection((String)images.get(0)[0]);
		returnValue.setSite((String)images.get(0)[1]);
		boolean isAuthorized = false;
		for (SiteData siteData : authorizedSites)
		{
			if (siteData.getCollection().equals(returnValue.getCollection()))
			{
				if (siteData.getSiteName().equals(returnValue.getSite()))
				{
					isAuthorized = true;
					break;
				}
			}
		}
		if (!isAuthorized)
		{
			System.out.println("User: "+user+" noy authorized");
			return null; //not authorized
		}
		String filePath = (String)images.get(0)[2];
		File imageFile = new File(filePath);
		if (!imageFile.exists())
		{
			log.error("File " + filePath + " does not exist");
			return null;
		}
		returnValue.setImage(FileUtils.readFileToByteArray(imageFile));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	
	return returnValue;
}

}
