/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion.dao;


import gov.nih.nci.nbia.exception.DataAccessException;
import gov.nih.nci.nbia.internaldomain.GeneralImage;
import gov.nih.nci.nbia.util.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ImageDAOImpl extends HibernateDaoSupport implements ImageDAO {
	private static Logger logger = Logger.getLogger(ImageDAOImpl.class);
	private Session session = null;

	public List<String> removeImages(List<Integer> seriesIds) throws DataAccessException{
		session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		List<String> imageFiles = new ArrayList<String>();

		if (session == null)
		{
			throw new DataAccessException("Cannot get session from session Factory in ImageDAOImpl");
		}
		try
		{
			if (seriesIds != null && seriesIds.size() > 0)
			{
				List<GeneralImage> deletedImageObject = getImageObject(seriesIds);
				boolean isMR = false;
				for (GeneralImage image : deletedImageObject) {
					imageFiles.add(image.getFilename());
					if(!isMR && image.getGeneralSeries().getModality().equalsIgnoreCase("MR")) {
						isMR = true;
					}
				}
				deleteImage(seriesIds);
				if(isMR) {
					deleteMRImage(seriesIds); //include MR images
				}
				deleteCTImage(seriesIds);
			}
		}catch(org.springframework.dao.DataAccessException e)
		{
			logger.error("Failed to remove Images or CT image in ImageDAOImpl!!");
			throw new DataAccessException("Failed to remove Images or CT image in ImageDAOImpl!!");
		}

		return imageFiles;
	}
	
	private void deleteMRImage(List<Integer> seriesIds) {
		for(final Integer seriesId : seriesIds) {
			getHibernateTemplate().execute(
		       new HibernateCallback() {
		    	   public Object doInHibernate(Session session) throws HibernateException, SQLException {

		    		   Query deleteGiQuery = session.createQuery("delete from MRImage where seriesPKId = ?");
		    		   deleteGiQuery.setInteger(0, seriesId);
		    		   /*int result = */deleteGiQuery.executeUpdate();

		    		   return null;
		    	   }
		       }
		    );
		}
	}

	public void deleteCTImage(List<Integer> seriesIds)
	{
		for(final Integer seriesId : seriesIds) {
			getHibernateTemplate().execute(
		       new HibernateCallback() {
		    	   public Object doInHibernate(Session session) throws HibernateException, SQLException {

		    		   Query deleteGiQuery = session.createQuery("delete from GeneralImage where seriesPKId = ?");
		    		   deleteGiQuery.setInteger(0, seriesId);
		    		   /*int result = */deleteGiQuery.executeUpdate();

		    		   return null;
		    	   }
		       }
		    );
		}
	}

	public void deleteImage (List<Integer> seriesIds)
	{
		for(final Integer seriesId : seriesIds) {
			getHibernateTemplate().execute(
		       new HibernateCallback() {
		    	   public Object doInHibernate(Session session) throws HibernateException, SQLException {

		    		   Query deleteCtQuery = session.createQuery("delete from CTImage where seriesPKId = ?");
		    		   deleteCtQuery.setInteger(0, seriesId);
		    		   /*int result = */deleteCtQuery.executeUpdate();

		    		   return null;
		    	   }
		       }
		    );
		}
	}

	public List<GeneralImage> getImageObject(List<Integer> seriesIds)
	{
		List<GeneralImage> imageObjects = new ArrayList<GeneralImage>();

		List<List<Integer>> breakdownList = Util.breakListIntoChunks(seriesIds, 900);
		for(List<Integer> unit : breakdownList)
		{
			Criteria criteria = session.createCriteria(GeneralImage.class);
			criteria.add(Restrictions.in("seriesPKId", unit));

			List<GeneralImage> tempHolder = criteria.list();
			for (GeneralImage image : tempHolder){
				imageObjects.add(image);
			}
		}

		return imageObjects;
	}

}
