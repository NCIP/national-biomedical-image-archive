package gov.nih.nci.ncia.util;

import gov.nih.nci.ncia.search.ImageSearchResult;

import java.util.List;

public class SlideShowUtil {
	public static String getImageSeriesJavascript(
			List<ImageSearchResult> thumbnailList) {
		StringBuffer js = new StringBuffer("[");
		for (int i = 0; i < thumbnailList.size(); i++) {
			ImageSearchResult image = thumbnailList.get(i);
			Integer frameNumI = image.getFrameNum();
			int frameSize = (frameNumI==null) ? 0 : frameNumI.intValue();

			if (frameSize <= 1) {
				String tnURL = "'"+image.getThumbnailURL()+"'";
				js.append(tnURL);
			}
			else { //frameSize > 1
				for (int j = 0; j < frameSize; ++j) {

					js.append("'" + 
							  image.getThumbnailURL() + 
							  "-" + 
							  j +
							  "'");
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
		//System.out.println("java script:" + js.toString());
		return js.toString();
	}
}
