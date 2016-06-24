/**
 *
 */
package gov.nih.nci.nbia.csmDao;

import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 * @author panq
 *
 */

public interface UserDao {

	public List<Object []> getAllCsmUser() throws DataAccessException;

}
