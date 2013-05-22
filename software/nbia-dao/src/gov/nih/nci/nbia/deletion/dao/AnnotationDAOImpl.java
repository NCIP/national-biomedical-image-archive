/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion.dao;

import gov.nih.nci.nbia.exception.DataAccessException;
import gov.nih.nci.nbia.internaldomain.Annotation;
import gov.nih.nci.nbia.util.Util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class AnnotationDAOImpl extends HibernateDaoSupport implements AnnotationDAO{
	private static Logger logger = Logger.getLogger(AnnotationDAOImpl.class);
	private List<String> annotationFilePath = new ArrayList<String>();

	public List<String> deleteAnnotation(List<Integer> seriesIDs)
	{
		//this.session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		List<Annotation> annotations = getAnnotations(seriesIDs);
		if (annotations != null && annotations.size() > 0)
		{
			List<List<Annotation>> annotationList = Util.breakListIntoChunks(annotations, 900);
			for(List<Annotation> annoUnit : annotationList)
			{
				processDeletion(annoUnit);
			}
		}
		return annotationFilePath;
	}

	private void processDeletion(List<Annotation> annotations)
	throws DataAccessException
	{
		List<List<Annotation>> breakedAnnotationList = Util.breakListIntoChunks(annotations, 900);
		try
		{
			for (List<Annotation> unit : breakedAnnotationList)
			{
				for (Annotation ann : unit)
				{
					annotationFilePath.add(ann.getFilePath());
					getHibernateTemplate().delete(ann);
				}
				getHibernateTemplate().flush();
			}
		}catch (org.springframework.dao.DataAccessException e)
		{
			logger.error("In AnnotationDAOImpl class (processDeletion method)," +
					" data access exception: " + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	private List<Annotation> getAnnotations(List<Integer> seriesIds)
	throws DataAccessException
	{
		//Criteria criteria = null;
		List<Annotation> result = new ArrayList<Annotation>();

		try
		{
			//Oracle cannot handle in clause with 1000 limitation, need to break the list 
			//down to less than 1000 a time
			List<List<Integer>> breakdownList = Util.breakListIntoChunks(seriesIds, 900);
			for (List<Integer> unit : breakdownList)
			{
				DetachedCriteria criteria = DetachedCriteria.forClass(Annotation.class);
				criteria.add(Restrictions.in("generalSeriesPkId", unit));

				List<Annotation> tempHolder = getHibernateTemplate().findByCriteria(criteria);
				for(Annotation anno : tempHolder){
					result.add(anno);
				}
			}
		}catch(org.springframework.dao.DataAccessException e)
		{
			logger.error("In annotationDAOImpl class (getAnnotations method)," +
					" data access exception: " + e.getMessage());
			throw new DataAccessException(e.getMessage());
		}

		return result;
	}
}
