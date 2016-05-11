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
	public List<String> getCollectionValues(List<String> authorizedProjAndSites) throws DataAccessException {
	// Actually the Rest API only need project. Added second project just
	// for using common util for format transferring.

	String hql = "select distinct(tdp.project) from TrialDataProvenance tdp " + addAuthorizedProjAndSites(authorizedProjAndSites);
	String orderBy = " order by upper(tdp.project)";
	List<String> rs = getHibernateTemplate().find(hql + orderBy);

	return rs;
}

	/**
	 * Fetch set of collection (ie. project) and site values.
	 *
	 * This method is used in User Authorization Tool.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public List<String> getProjSiteValues() throws DataAccessException {
	// Actually the Rest API only need project. Added second project just
	// for using common util for format transferring.

	String hql = "select distinct(CONCAT(tdp.project, '//', tdp.dpSiteName)) from TrialDataProvenance tdp ";
	String orderBy = " order by CONCAT(tdp.project, '//', tdp.dpSiteName)";
	List<String> rs = getHibernateTemplate().find(hql + orderBy);

	return rs;
}
	/**
	 * Construct the partial where clause which contains checking with authorized project and site combinations.
	 *
	 * This method is used for NBIA Rest API filter.
	 */
	private StringBuffer addAuthorizedProjAndSites(List<String> authorizedProjAndSites) {
		StringBuffer where = new StringBuffer();

		if ((authorizedProjAndSites != null) && (!authorizedProjAndSites.isEmpty())){
			where = where.append("where tdp.projAndSite in (");

			for (Iterator<String> projAndSites =  authorizedProjAndSites.iterator(); projAndSites .hasNext();) {
	    		String str = projAndSites.next();
	            where.append (str);

	            if (projAndSites.hasNext()) {
	            	where.append(",");
	            }
	        }
			where.append(")");
		}
		System.out.println("&&&&&&&&&&&&where clause for project and group=" + where.toString());
		return where;
	}
}
