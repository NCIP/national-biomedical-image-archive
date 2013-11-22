/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.populator;

import gov.nih.nci.nbia.basket.BasketSeriesItemBean;
import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.basket.BasketBean;
import gov.nih.nci.nbia.beans.searchresults.SeriesSearchResultBean;
import gov.nih.nci.nbia.search.LocalDrillDown;
import gov.nih.nci.ncia.search.NBIANode;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.util.List;

/**
 * This bean became necessary after switching to ICEfaces.
 *
 * <p>
 * The dicom tag view popup browser window worked historically by opening the
 * new browser window with javascript, then using a hidden form.... the target
 * was changed with javascript to the new window and a command action fired off
 * to load the dicom tag info for the new page to use. This doesn't appear to
 * work with ICEfaces. Looks like iceSubmit() js method doesn't care at all
 * about the target attribute of a form.
 *
 * <p>
 * So..... just open the new browser window, point it to the page... and here is
 * the page load action so all the dicom tag info is loaded in time to be
 * displayed.
 */
public class ViewSeriesPopulatorMgBean {

	public String getSeriesId() {
		return this.seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}

	public void populate() throws Exception {
		BasketBean basketBean = BeanManager.getBasketBean();

		List<BasketSeriesItemBean> bsitem = basketBean.getSeriesItems();
		NBIANode node = new NBIANode(false, getLocation(), getUrl());
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.setSeriesNumber(seriesId);
		seriesSearchResult.associateLocation(node);
		basketBean.viewSeriesData(getSeries(seriesId, location, bsitem));
	}

	/**
	 * Determines if the series is in the basket.
	 */
	private SeriesSearchResult getSeries(String seriesId, String location,
			List<BasketSeriesItemBean> seriesItems) {
		SeriesSearchResult seriesItem = null;
		if (seriesItems != null) {
			for (BasketSeriesItemBean item : seriesItems) {
				if (item.getSeriesId() != null
						&& item.getSeriesId().equalsIgnoreCase(seriesId)
						&& item.getLocationDisplayName() != null
						&& item.getLocationDisplayName().equalsIgnoreCase(
								location)) {
					seriesItem = item.getSeriesSearchResult();
					break;
				}
			}
		} else {
			System.out.println("collection is NULL waste............");
		}
		if (seriesItem != null) {
			System.out.println("leaving getSeries info "
					+ seriesItem.associatedLocation().getDisplayName());
		} else {
			System.out.println("seriesItem is NULLL");
		}
		return seriesItem;
	}


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private String seriesId;
	private String location;
	private String url;

	/**
	 * This method is only on the local drill down.  It's used by the
	 * view thumbnails.
	 */
	public void populateImages() throws Exception {
		LocalDrillDown localDrillDown = new LocalDrillDown();
		SeriesSearchResult theSeries = localDrillDown.retrieveSeries(Integer.parseInt(seriesId));
		SeriesSearchResultBean bean = BeanManager.getSeriesSearchResultBean();
		bean.populate(null);
		bean.viewSeries(theSeries);
	}
}

