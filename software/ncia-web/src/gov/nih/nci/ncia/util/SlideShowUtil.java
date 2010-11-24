package gov.nih.nci.ncia.util;

import gov.nih.nci.ncia.search.ImageSearchResult;

import java.util.List;

public class SlideShowUtil {
	public static String getImageSeriesJavascript(List<ImageSearchResult> thumbnailList) {
		StringBuffer js = new StringBuffer("[");
		for(int i=0;i<thumbnailList.size();i++) {
			ImageSearchResult image = thumbnailList.get(i);
			js.append("'"+image.getThumbnailURL()+"'");
			if(i!=thumbnailList.size()-1) {
				js.append(',');
			}
		}
		js.append(']');
		return js.toString();
	}
}
