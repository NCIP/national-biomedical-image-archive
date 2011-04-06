package gov.nih.nci.nbia.basket;

import gov.nih.nci.nbia.dao.DownloadDataDAO;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.util.Map;


public class DownloadRecorder {
    public void recordDownload(Map<String, SeriesSearchResult> seriesItemMap, 
    		                   String loginName) throws Exception {
        DownloadDataDAO downloadDAO = (DownloadDataDAO)SpringApplicationContext.getBean("downloadDataDAO");

        for(String key : seriesItemMap.keySet()){
        	SeriesSearchResult ssr = seriesItemMap.get(key);
            long size = ssr.computeExactSize();
            downloadDAO.record(ssr.getSeriesInstanceUid(), 
            		           loginName, 
            		           "classic", 
            		           size);
        }
    }
}
