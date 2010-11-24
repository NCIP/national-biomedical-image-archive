package gov.nih.nci.ncia.beans.basket;

import gov.nih.nci.ncia.basket.BasketSeriesItemBean;
import gov.nih.nci.ncia.dao.DownloadDataDAO;
import gov.nih.nci.ncia.util.SpringApplicationContext;

import java.util.Map;


public class DownloadRecorder {
    public void recordDownload(Map<String, BasketSeriesItemBean> seriesItemMap, 
    		                   String loginName) throws Exception {
        DownloadDataDAO downloadDAO = (DownloadDataDAO)SpringApplicationContext.getBean("downloadDataDAO");

        for(String key : seriesItemMap.keySet()){
            BasketSeriesItemBean bsib = seriesItemMap.get(key);
            long size = bsib.getExactSize();
            downloadDAO.record(bsib.getSeriesId(), 
            		           loginName, 
            		           "classic", 
            		           size);
        }
    }
}
