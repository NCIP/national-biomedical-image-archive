/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.ncia.search.ImageSearchResultEx;
import gov.nih.nci.ncia.search.ImageSearchResult;
import java.util.List;

public class SlideShowUtil {
	public static String getImageSeriesJavascriptEx(
			List<ImageSearchResultEx> thumbnailList) {
		StringBuffer js = new StringBuffer("[");
		for (int i = 0; i < thumbnailList.size(); i++) {
			ImageSearchResultEx image = thumbnailList.get(i);
			int frameSize = 0;
			if (image.getNameValuesPairs()!= null){
				String frameNumI = image.getNameValuesPairs().getValues()[0];
				frameSize = (frameNumI==null) ? 0 : Integer.parseInt(frameNumI);		
			}

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
	
	public static String getImageSeriesJavascriptEx(
			List<ImageSearchResultEx> thumbnailList, int i) {
		ImageSearchResultEx image = thumbnailList.get(i);
		int frameSize = 0;
		StringBuffer js = new StringBuffer("[");
		
		if (image.getNameValuesPairs() != null) {
			String frameNumI = image.getNameValuesPairs().getValues()[0];
			frameSize = (frameNumI == null) ? 0 : Integer.parseInt(frameNumI);
		}

		if (frameSize <= 1) {
			String tnURL = "'" + image.getThumbnailURL() + "'";
			js.append(tnURL);
		} else { // frameSize > 1
			for (int j = 0; j < frameSize; ++j) {

				js.append("'" + image.getThumbnailURL() + "-" + j + "'");
				if (j != frameSize - 1) {
					js.append(',');
				}
			}
		}

		js.append(']');
		// System.out.println("java script:" + js.toString());
		return js.toString();
	}
	
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
