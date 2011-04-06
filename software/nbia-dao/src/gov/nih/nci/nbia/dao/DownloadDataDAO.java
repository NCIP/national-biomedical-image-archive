package gov.nih.nci.ncia.dao;

import org.springframework.dao.DataAccessException;

public interface DownloadDataDAO  {

    public void record(String seriesInstanceUid,
    		           String loginName,
    		           String type,
    		           Long size)throws DataAccessException;

}
