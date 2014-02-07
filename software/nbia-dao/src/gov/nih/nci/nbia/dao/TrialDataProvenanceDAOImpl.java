/**
 *
 */
package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.util.SiteData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author panq
 *
 */
public class TrialDataProvenanceDAOImpl extends AbstractDAO implements
		TrialDataProvenanceDAO {
	/**
	 * Fetch set of collection values.
	 *
	 * This method is used for NBIA Rest API.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public List<String> getCollectionValues(List<SiteData> siteData) throws DataAccessException {
		// Actually the Rest API only need project. Added second project just
		// for using common util for format transferring.

		String hql = "select distinct(tdp.project) from TrialDataProvenance tdp " + addSecurityGroup(siteData);
		String orderBy = " order by upper(tdp.project)";
		List<String> rs = getHibernateTemplate().find(hql + orderBy);

		return rs;
	}

	private StringBuffer addSecurityGroup(List<SiteData> authorizedSites) {
		StringBuffer where = new StringBuffer();
		String authorisedProjectName = getProjectNames(authorizedSites);
		String authorisedSiteName = getSiteNames(authorizedSites);
		if(authorisedProjectName != null && authorisedSiteName != null) {
			where = where.append(" where UPPER(tdp.project) in (").append(authorisedProjectName).append(")").append(" and UPPER(tdp.dpSiteName) in (" + authorisedSiteName+")");
		}
		return where;
	}
	private String getProjectNames(Collection<SiteData> sites) {
		if(sites == null || sites.isEmpty()) {
			return null;
		}
		String projectNameStmt = "";
    	for (Iterator<SiteData> i = sites.iterator(); i.hasNext();) {
    		SiteData str = i.next();
            projectNameStmt += ("'" + str.getCollection().toUpperCase() + "'");

            if (i.hasNext()) {
            	projectNameStmt += ",";
            }
        }
    	return projectNameStmt;
    }
	private String getSiteNames(Collection<SiteData> sites) {
		if(sites == null || sites.isEmpty()) {
			return null;
		}
		String siteWhereStmt = "";
    	for (Iterator<SiteData> i = sites.iterator(); i.hasNext();) {
    		SiteData str = i.next();
            siteWhereStmt += ("'" + str.getSiteName().toUpperCase() + "'");

            if (i.hasNext()) {
            	siteWhereStmt += ",";
            }
        }
    	return siteWhereStmt;
    }
}
