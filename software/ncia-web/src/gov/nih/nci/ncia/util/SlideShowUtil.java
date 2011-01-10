package gov.nih.nci.ncia.util;

import gov.nih.nci.ncia.search.ImageSearchResult;

import java.util.List;

public class SlideShowUtil {
	public static String getImageSeriesJavascript(
			List<ImageSearchResult> thumbnailList) {
		StringBuffer js = new StringBuffer("[");
		for (int i = 0; i < thumbnailList.size(); i++) {
			ImageSearchResult image = thumbnailList.get(i);
			// int frameSize = image.getFrameSize().intValue();
			int frameSize = 30;
			// js.append("'"+image.getThumbnailURL()+"'");
			if (frameSize <= 1) {
				js.append("'" + image.getThumbnailURL());
			}
			for (int j = 0; j < frameSize; ++j) {
				if (frameSize > 1) {
					js.append("'" + image.getThumbnailURL() + "-" + j + "'");
					if (j != frameSize - 1) {
						js.append(',');
					}
				}

			}

			if (i != thumbnailList.size() - 1) {
				js.append(',');
			}
		}
		js.append(']');
		System.out.println("java sript:" + js.toString());
		return js.toString();
	}
}
