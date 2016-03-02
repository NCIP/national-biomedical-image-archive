/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchresults;

import gov.nih.nci.nbia.searchresult.SeriesSearchResult;

public class SeriesResultWrapper {
	public SeriesResultWrapper(SeriesSearchResult series) {
		this.series = series;
	}
	
	public String getBasketKey() {
		return series.getId().toString();
	}
	
	
	public boolean isChecked() {
		return this.checked;
	}
	
	public void setChecked(boolean b) {
		this.checked = b;
	}
	
	public SeriesSearchResult getSeries() {
		return this.series;
	}
	
	private boolean checked;
	
	private SeriesSearchResult series;	
}