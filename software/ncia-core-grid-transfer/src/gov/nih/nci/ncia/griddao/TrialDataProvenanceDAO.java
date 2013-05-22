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
package gov.nih.nci.ncia.griddao;

import gov.nih.nci.nbia.util.HqlUtils;
import gov.nih.nci.ncia.domain.TrialDataProvenance;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This object does hand-rolled data access for anything data provenance related in the
 * grid service.  It violates patterns a bit by potentially returning Hibernate
 * objects.... need to think about that a bit but this is a first cut...
 * 
 * <p>This came from a massive object called ImageFileProcessor which was split
 * into this object and several other "DAO" objects to isolate data access according
 * to similar pattern as rest of system.
 * 
 * @author lethai
 */
public class TrialDataProvenanceDAO extends HibernateDaoSupport implements TrialDataProvenanceDAOInterface  {
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Map<String, TrialDataProvenance> getTDPByPatientId(Collection<String> patientIds) throws DataAccessException {
			if (patientIds == null) {
				return null;
			}
			String hql = "SELECT p.patientId, p.dataProvenance "+
            "FROM Patient p "+
            "WHERE p.patientId in "+ HqlUtils.buildInClause("", patientIds);

			List<Object[]> rs = this.getHibernateTemplate().find(hql);
			
			return processTDP(rs);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public Map<String, TrialDataProvenance> getTDPByStudyInstanceUID(Collection<String> studyInstanceUIDs) throws DataAccessException {
		if (studyInstanceUIDs == null) {
			return null;
		}
		String hql = "SELECT s.studyInstanceUID, s.patient.dataProvenance "+
        "FROM Study s, Patient p "+
        "WHERE p.id = s.patient.id and s.studyInstanceUID in "+ HqlUtils.buildInClause("", studyInstanceUIDs);
	
		List<Object[]> rs = getHibernateTemplate().find(hql);
		
		return processTDP(rs);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public Map<String, TrialDataProvenance> getTDPBySeriesInstanceUID(
			Collection<String> seriesInstanceUIDs) throws DataAccessException{
		if (seriesInstanceUIDs == null) {
			return null;
		}

		String hql = "SELECT series.seriesInstanceUID, series.project, series.site "+
        "FROM GeneralSeries series "+
        "WHERE series.seriesInstanceUID in "+ HqlUtils.buildInClause("", seriesInstanceUIDs);

		List<Object[]> rs = this.getHibernateTemplate().find(hql);
				
		return processTDPForSeries(rs);
	}

	///////////////////////////////////PRIVATE//////////////////////////////////////////////
	
	private static Map<String,TrialDataProvenance> processTDP(List<Object[]> rs) {
		Map<String, TrialDataProvenance> tdpList = new HashMap<String, TrialDataProvenance>();

		for(Object[] result : rs) {
			String id = (String)result[0];
			gov.nih.nci.nbia.internaldomain.TrialDataProvenance tdp = (gov.nih.nci.nbia.internaldomain.TrialDataProvenance)result[1];
			//needs to convert internalDomain TrialDataProvenance to domain TrialDataProvenance
			TrialDataProvenance tdp_domain = new TrialDataProvenance();
			tdp_domain.setProject(tdp.getProject());
			tdp_domain.setSiteName(tdp.getDpSiteName());
			tdpList.put(id, tdp_domain);
		}
		return tdpList;
	}
	
	private static Map<String, TrialDataProvenance> processTDPForSeries(List<Object[]> rs){
		Map<String, TrialDataProvenance> tdpList = new HashMap<String, TrialDataProvenance>();
		
		for (Object[] result : rs){
			String id = (String)result[0];
			String project = (String)result[1];
			String siteName = (String)result[2];
			TrialDataProvenance tdp = new TrialDataProvenance();
			tdp.setProject(project);
			tdp.setSiteName(siteName);
			tdpList.put(id, tdp);
		}
		return tdpList;
	}

}
