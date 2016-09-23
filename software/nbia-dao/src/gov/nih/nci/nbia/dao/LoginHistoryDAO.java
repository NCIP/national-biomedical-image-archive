/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import org.springframework.dao.DataAccessException;

public interface LoginHistoryDAO  {
    /**
     * Records a user login in the database
     *
     * @throws Exception
     */
    public void recordUserLogin(String ipAddress) throws DataAccessException;
}
