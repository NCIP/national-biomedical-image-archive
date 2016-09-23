package gov.nih.nci.nbia.csmDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;


import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

	@Transactional(propagation = Propagation.REQUIRED)
	public List<Object []> getAllCsmUser() {
		List<Object []> userOptions= new ArrayList<Object[]>();
		List<Object> results = null;
		String hql = "select user.loginName from NCIAUser user";

		try {		
			results = getHibernateTemplate().find(hql);
			if (results != null && results.size() > 0) {
				Collections.sort(results, new Comparator<Object>() {
					public int compare(Object s1, Object s2) {
					   //ascending order
					   return s1.toString().compareTo(s2.toString());
				    }
				});
				
				for(Object existUser : results) {
		            //userNames.add(existUser.getLoginName());
		            Object [] objs = {existUser, existUser};
					userOptions.add(objs);
		        }
			}
			else {
				Object [] placeHold = {"Warning: No User Defined Yet. Creat the User First!", ""};
				userOptions.add(placeHold);
			}
					
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException("Could not execute the query.");
		}
		return userOptions;
	}
}
