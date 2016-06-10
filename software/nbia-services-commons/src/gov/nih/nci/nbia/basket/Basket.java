/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.basket;

import gov.nih.nci.nbia.search.LocalDrillDown;
import gov.nih.nci.nbia.searchresult.ImageSearchResult;
import gov.nih.nci.nbia.searchresult.SeriesSearchResult;
import gov.nih.nci.nbia.lookup.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This a data structure that represents the basket that series items are
 * dropped into.  It's really a collection of series items.
 */
public class Basket {

	public static interface BasketChangeListener {
		public void basketChanged(Basket b);
	}


	public void addBasketChangeListener(BasketChangeListener l) {
		listeners.add(l);
	}


	public void removeBasketChangeListener(BasketChangeListener l) {
		listeners.remove(l);
	}


    /**
     * Retrieve all the series items sorted by paitent/study/series
     */
    public List<BasketSeriesItemBean> getSeriesItems() {
        List<BasketSeriesItemBean> toSort = new ArrayList<BasketSeriesItemBean>(seriesItems.values());
        //Collections.sort(toSort);

        return toSort;
    }


    /**
     * Retrieve all the series items keyed by the series instance UID+grid/node location.
     */
    public Map<String, BasketSeriesItemBean> getSeriesItemMap() {
    	return Collections.unmodifiableMap(this.seriesItems);
    }


    /**
     * Determines if the series is in the basket.
     */
    public boolean isSeriesInBasket(Integer seriesPkId) {
        return (seriesItems.get(seriesPkId) != null);
    }


    /**
     * Add all the specified series to the basket.
     */
    public void addSeries(List<SeriesSearchResult> seriesDTOs) throws Exception {
        List<BasketSeriesItemBean> seriesBeans = new ArrayList<BasketSeriesItemBean>();
        for(SeriesSearchResult seriesDTO : seriesDTOs){
            BasketSeriesItemBean bsib = convert(seriesDTO);
            seriesBeans.add(bsib);
        }

        addResults(seriesBeans);

        fireChangeEvent();
    }


    /**
     * Adds the series of the thumbnail to the data basket.
     *
     * <p>This method is only used by ISPY which is local, so no remote stuff here.
     * Need to add a method to DrillDown (to return series from image) if this needs
     * to be used remotely.
     */
    public void addThumbnail(ImageSearchResult dto) {

        Integer seriesId = dto.getSeriesId();
        // Find the series
        BasketSeriesItemBean seriesForImage = seriesItems.get(seriesId);

        // Check to see if the series is in the basket. If not, create it
        if (seriesForImage == null) {

    		LocalDrillDown drillDown = new LocalDrillDown();
    		SeriesSearchResult seriesSearchResult = drillDown.retrieveSeries(seriesId);

            List<BasketSeriesItemBean> seriesBeans = new ArrayList<BasketSeriesItemBean>();
            BasketSeriesItemBean bsib = convert(seriesSearchResult);
            seriesBeans.add(bsib);

            addResults(seriesBeans);
        }
        //else do nothing

        fireChangeEvent();
    }
    /**
     * Remove all series items
     */
    public void removeAllSeries() {
        List<String> seriesIdsToRemove = new ArrayList<String>();

        // Build list of IDs to remove. We can't remove them yet
        // because we're iterating through the list
        for (BasketSeriesItemBean item : seriesItems.values()) {
           seriesIdsToRemove.add(item.getSeriesSearchResult().getId().toString());
        }

        // Actually remove the items
        for (String seriesId : seriesIdsToRemove) {
            seriesItems.remove(seriesId);
        }

        fireChangeEvent();
    }

    /**
     * Remove all series items that are "selected".  Revisit the conception of
     * selection which is probably more in the domain of presentation.
     */
    public void removeSelectedSeries() {
        List<String> seriesIdsToRemove = new ArrayList<String>();

        // Build list of IDs to remove. We can't remove them yet
        // because we're iterating through the list
        for (BasketSeriesItemBean item : seriesItems.values()) {
            if (item.isSelected()) {
                seriesIdsToRemove.add(item.getSeriesSearchResult().getId().toString());
            }
        }

        // Actually remove the items
        for (String seriesId : seriesIdsToRemove) {
            seriesItems.remove(seriesId);
        }

        fireChangeEvent();
    }


    /**
     * Returns the total number of series in the basket.
     */
    public int getBasketSeriesCount() {
        return seriesItems.size();
    }


    /**
     * Are there any series items in the basket?
     */
    public boolean isEmpty() {
    	return seriesItems.size()==0;
    }

    /**
     * Calculates the total size of the basket in bytes.
     */
    public double calculateSizeInBytes() {
        double size = 0;

        for (BasketSeriesItemBean bsib : seriesItems.values()) {
            size += bsib.getExactSize();
        }

        return size;
    }

    /**
     * Calculates the total size of the basket in MB.
     */
    public double calculateSizeInMB() {

        return calculateSizeInBytes() / 1000000.0;
    }

    /**
     * Calculates the size of just the images in the basket in bytes.
     */
    public double calculateImageSizeInBytes() {
    	return calculateSizeInBytes() - calculateAnnotationSizeInBytes();
    }


    /**
     * Calculates the size of just the annotations in the basket in bytes.
     */
    public double calculateAnnotationSizeInBytes() {
    	double size = 0;

        for (BasketSeriesItemBean bsib : seriesItems.values()) {
            size += bsib.getAnnotationsSize();
        }

        return size;
    }

    ///////////////////////////////////////////PRIVATE////////////////////////////////
    private List<BasketChangeListener> listeners = new ArrayList<BasketChangeListener>();

    /**
     * This is the underlying map that stores all of the items that are placed
     * in the Data Basket. Key is the series ID
     */
    private Map<String, BasketSeriesItemBean> seriesItems = new HashMap<String, BasketSeriesItemBean>();

    private void fireChangeEvent() {
    	for(BasketChangeListener l : listeners) {
    		l.basketChanged(this);
    	}
    }


    /**
     * Several other methods call this to add their series results to the basket
     *
     * @param results
     */
    private void addResults(Collection<BasketSeriesItemBean> results) {
        // Loop through each result row
        for (BasketSeriesItemBean result : results) {
        	System.out.println("$$$ add results="+result.getSeriesId().toString());
            BasketSeriesItemBean alreadyExisting =
            	seriesItems.get(result.getSeriesSearchResult().getId().toString());
System.out.println("!!!form get= "+result.getSeriesSearchResult().getId().toString());
            // If the series does not exist, add it
            if (alreadyExisting == null) {
                seriesItems.put(result.getSeriesSearchResult().getId().toString(), result);
                System.out.println("****not already existing put = "+result.getSeriesSearchResult().getId().toString());
            }
            else {
            	System.out.println("&&&&&&&&&&alreadyExisting="+alreadyExisting.getSeriesId());
            	  System.out.println("&&&&&&&&&&&&&&&&&neither="+result.getSeriesSearchResult().getId());
                continue;
            }
        }
        System.out.println("end add results="+results.size());
    }

	private static BasketSeriesItemBean convert(SeriesSearchResult seriesDTO){
		BasketSeriesItemBean returnBean = new BasketSeriesItemBean(seriesDTO);

		returnBean.setAnnotationsFlag(seriesDTO.isAnnotated());
		returnBean.setAnnotationsSize(seriesDTO.getAnnotationsSize());
		returnBean.setPatientId(seriesDTO.getPatientId());
		returnBean.setProject(seriesDTO.getProject());
		returnBean.setSeriesId(seriesDTO.getSeriesInstanceUid());
		returnBean.setSeriesPkId(seriesDTO.getId());
		returnBean.setStudyId(seriesDTO.getStudyInstanceUid());
		returnBean.setStudyPkId(seriesDTO.getStudyId());
		returnBean.setTotalImagesInSeries(seriesDTO.getNumberImages());
		returnBean.setTotalSizeForAllImagesInSeries(seriesDTO.getTotalSizeForAllImagesInSeries());
		returnBean.setStudyDate(seriesDTO.getStudyDate());
		returnBean.setStudyDescription(seriesDTO.getStudyDescription());
		returnBean.setSeriesDescription(seriesDTO.getDescription());
		returnBean.setPatientpk(seriesDTO.getPatientpk());
		return returnBean;
	}


	public void removeSelectedSeries(String toDeleteSeriesId) {
		System.out.println("id to delete" + toDeleteSeriesId);
        seriesItems.remove(toDeleteSeriesId);
        fireChangeEvent();
	}
}