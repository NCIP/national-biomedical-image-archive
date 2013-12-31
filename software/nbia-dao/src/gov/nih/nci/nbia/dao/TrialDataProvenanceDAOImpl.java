/**
 * 
 */
package gov.nih.nci.nbia.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author panq
 *
 */
public class TrialDataProvenanceDAOImpl extends AbstractDAO implements TrialDataProvenanceDAO {
	/**
	 * Fetch set of collection values.
	 *
	 * This method is used for NBIA Rest API.
	 */
	@Transactional(propagation=Propagation.REQUIRED)		
	public List<String> getCollectionValues() throws DataAccessException 
	{
		//Actually the Rest API only need project. Added second project just for using common util for format transferring.

		String hql = "select distinct(tdp.project) from TrialDataProvenance tdp order by upper(tdp.project)";
		List<String> rs =	getHibernateTemplate().find(hql);

        return rs;
	}
}
