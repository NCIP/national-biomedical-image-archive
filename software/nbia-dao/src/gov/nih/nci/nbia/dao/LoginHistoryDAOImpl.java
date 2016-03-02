/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.internaldomain.LoginHistory;

import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class LoginHistoryDAOImpl extends AbstractDAO 
                                 implements LoginHistoryDAO {
    /**
     * Records a user login in the database
     */
	@Transactional(propagation=Propagation.REQUIRED)	
    public void recordUserLogin(String ipAddress) throws DataAccessException {

		LoginHistory loginHistory = new LoginHistory();
		loginHistory.setLoginTimestamp(new Date());
		// Pull the IP address off of the HTTP request
		loginHistory.setIPAddress(ipAddress);
		

		getHibernateTemplate().save(loginHistory);
    }
}
