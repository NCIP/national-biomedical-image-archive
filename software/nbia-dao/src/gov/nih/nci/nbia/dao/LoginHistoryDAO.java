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
