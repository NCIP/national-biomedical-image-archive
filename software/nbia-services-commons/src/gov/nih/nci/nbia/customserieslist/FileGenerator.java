/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.customserieslist;

import gov.nih.nci.nbia.basket.BasketSeriesItemBean;

import java.util.List;

public class FileGenerator {
	
	/**
	 * generate comma separate string from the series instance uid list
	 * @param seriesItems
	 */
	public String generate(List<BasketSeriesItemBean> seriesItems){
		StringBuilder sb = new StringBuilder();
		int size = seriesItems.size();
		for(int i=0; i < size ; i++) {
			BasketSeriesItemBean bsib = seriesItems.get(i);
			sb.append(bsib.getSeriesId());
			sb.append(",");
			sb.append("\n");
		}
		int lastComma = sb.lastIndexOf(",");
		
		//System.out.println("char at length - 1: " + sb.charAt(sb.length() - 1) +  " length: " + sb.length() + " lastComma: " + lastComma);
		sb.replace(lastComma, lastComma, "");
		sb.trimToSize();
		//System.out.println("length: " + sb.length());
		return sb.toString();
	}
}