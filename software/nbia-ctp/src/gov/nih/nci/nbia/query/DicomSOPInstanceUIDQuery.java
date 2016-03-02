/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.query;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import gov.nih.nci.nbia.internaldomain.GeneralImage;

import org.rsna.ctp.stdstages.database.UIDResult;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class DicomSOPInstanceUIDQuery extends HibernateDaoSupport implements DicomSOPInstanceUIDQueryInterface{

	Set<String> sopUIDSet;

	public DicomSOPInstanceUIDQuery()
	{
	}

	public void setDicomSOPInstanceUIDQuery(Set<String> sopUid)
	{
		this.sopUIDSet = sopUid;
	}
	
	@Transactional (propagation=Propagation.REQUIRED)
	public Map<String, UIDResult> getUIDResult()
	{
		Map<String, UIDResult> resultMap = new HashMap<String, UIDResult>();
		UIDResult result = null;
		String[] uidArray = sopUIDSet.toArray(new String[0]);
		for (int i=0; i < uidArray.length; i++)
		{
			result = getIndividualUIDResult(uidArray[i]);
			resultMap.put(uidArray[i], result);
		}

		return resultMap;
	}

	private UIDResult getIndividualUIDResult(String sopInstanceUID)
	{
		UIDResult uidResult = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(GeneralImage.class);
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("submissionDate"));
		projectionList.add(Projections.property("md5Digest"));
		criteria.setProjection(projectionList);
    	criteria.add(Restrictions.eq("SOPInstanceUID",sopInstanceUID));
    	List result = getHibernateTemplate().findByCriteria(criteria);
    	//Since SOPInstanceUID is unique, the result either 0 or 1
    	if (result != null && result.size() > 0)
    	{
    		Object[] o = (Object[])result.get(0);
    		Date submissionDate = (Date)o[0];
    		long datetime = submissionDate.getTime();
    		String mdDigest = (String)o[1] == null ? "" : (String)o[1];
    		uidResult = UIDResult.PRESENT(datetime, mdDigest);
    	}
    	if (result == null || result.size() == 0)
    	{
    		uidResult = UIDResult.MISSING();
    	}

		return uidResult;
	}
}
