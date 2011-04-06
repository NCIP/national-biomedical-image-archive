package gov.nih.nci.nbia.beans.searchresults;

import gov.nih.nci.ncia.search.SeriesSearchResult;

public class SeriesResultWrapper {
	public SeriesResultWrapper(SeriesSearchResult series) {
		this.series = series;
	}
	
	public String getBasketKey() {
		return series.getId()+"||"+series.associatedLocation().getURL();
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