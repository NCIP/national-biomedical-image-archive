package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.internaldomain.DownloadDataHistory;

import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class DownloadDataDAOImpl extends AbstractDAO
                                 implements DownloadDataDAO  {

	@Transactional(propagation=Propagation.REQUIRED)
    public void record(String seriesInstanceUid,
    		           String loginName,
    		           String type,
    		           Long size)throws DataAccessException{

		DownloadDataHistory downloadHistory = new DownloadDataHistory();
    	downloadHistory.setLoginName(loginName);
    	downloadHistory.setSeriesInstanceUid(seriesInstanceUid);
    	downloadHistory.setSize(size);
    	downloadHistory.setDownloadTimestamp(new Date());
    	downloadHistory.setType(type);

    	getHibernateTemplate().save(downloadHistory);
    }

}