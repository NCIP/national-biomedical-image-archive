/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.basket.BasketSeriesItemBean;
import gov.nih.nci.nbia.beans.searchresults.SeriesResultWrapper;
import gov.nih.nci.nbia.beans.searchresults.StudyResultWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CedaraUtil {
    //move into separate object to provide unit test
    public static String constructUidParameterString(List<StudyResultWrapper> studyResults) {
        int count = 0;
        String uid = null;
        for (int i = 0; i < studyResults.size(); i++) {
        	StudyResultWrapper curr = studyResults.get(i);
            List<SeriesResultWrapper> sList = curr.getSeriesResults();

            for (int j = 0; j < sList.size(); j++) {
            	SeriesResultWrapper item = sList.get(j);

                if (item.isChecked()) {
                    item.setChecked(false);

                    if (count > 0 ) {
                        uid = uid + "&uid=" + item.getSeries().getSeriesInstanceUid();
                    }
                    else {
                    	uid = item.getSeries().getSeriesInstanceUid();
                    }
                    ++count;
                }
            }
        }
        return uid;
    }


    public static boolean containsRemoteSeries(Collection<BasketSeriesItemBean> seriesItems) {
        for (BasketSeriesItemBean item : seriesItems) {
            if (item.isSelected() && !item.getSeriesSearchResult().associatedLocation().isLocal())
            {
                return true;
            }
        }
        return false;
    }

    public static boolean containsSeriesWhichImageLessThanFour(Collection<BasketSeriesItemBean> seriesItems)
    {
    	boolean containLess4 = false;
    	int selectedItemSize = 0;

    	for (BasketSeriesItemBean item : seriesItems)
    	{
    		if (item.isSelected())
    		{
    			selectedItemSize++;
    			if (item.getTotalImagesSelectedFromSeries() < 4)
    			{
    				containLess4 = true;
    			}
    		}
    	}

    	if ( selectedItemSize <= 1)
    	{
    		return false;
    	}

    	return containLess4;
    }


    public static boolean containsMultiplePatients(Collection<BasketSeriesItemBean> seriesItems) {
       // String localNodeName = NCIAConfig.getLocalNodeName();
        List<String> pIds = new ArrayList<String>();

        for (BasketSeriesItemBean item : seriesItems) {
//            if (item.getGridLocation().equalsIgnoreCase(localNodeName) && item.isSelected())
        	//Remote nodes are already filtered out, will not check for remote node anymore.
        	if (item.isSelected())
        	{
                String pid = item.getPatientId();
                if(!pIds.contains(pid))
                {
                    pIds.add(pid);
                }
                if (pIds.size() > 1)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static String constructUidParameterString(Collection<BasketSeriesItemBean> seriesItems) {
        String localNodeName = NCIAConfig.getLocalGridURI();

        int count = 0;
        String uid = null;
        for (BasketSeriesItemBean item : seriesItems) {
            if (item.getGridLocation().equalsIgnoreCase(localNodeName) && item.isSelected()) {
                if (count > 0 ) {
                    uid = uid + "&uid=" + item.getSeriesId();
                }
                else {
                    uid = item.getSeriesId();
                }
                ++count;
            }
        }
        return uid;
    }
}
