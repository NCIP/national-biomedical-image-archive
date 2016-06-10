/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.customserieslist;

import gov.nih.nci.nbia.lookup.*;
import gov.nih.nci.nbia.dao.StudyDAO;
import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.dto.StudyDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
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
	/**
	 * generate comma separate string from the series instance uid list
	 * @param seriesItems
	*/
	public String generateImageMetadata(List<BasketSeriesItemBean> seriesItems) {
		String[] csvHeaders= new String[]{"Collection","Patient Id","Study Date","Study Description","Modality","Series Description","Manufacturer","Manufacturer Model","Software Version","Series UID"};
		StringBuilder sb = new StringBuilder();
		for (String csvHeader : csvHeaders) {
			sb.append(csvHeader);
			sb.append(",");
		}
		sb.append("\n");
		int size = seriesItems.size();
		List<Integer> seriesPKIdLst = new ArrayList<Integer>();
		for(int i=0; i < size ; i++) {
			BasketSeriesItemBean bsib = seriesItems.get(i);
			seriesPKIdLst.add(bsib.getSeriesPkId());
		}
		StudyDAO studyDAO = (StudyDAO)SpringApplicationContext.getBean("studyDAO");
		List<StudyDTO> studies = studyDAO.findStudiesBySeriesIdDBSorted(seriesPKIdLst);
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		for (StudyDTO study: studies) {
			List<SeriesDTO> seriesLst = study.getSeriesList();
			for (SeriesDTO series:seriesLst ) {
				sb.append(series.getProject());
				sb.append(",");
				sb.append(series.getPatientId());
				sb.append(",");
				sb.append(df.format(study.getDate()));
				sb.append(",");
				sb.append(StringEscapeUtils.escapeCsv(study.getDescription()));
				sb.append(",");
				sb.append(series.getModality());
				sb.append(",");
				sb.append(StringEscapeUtils.escapeCsv(series.getDescription()));
				sb.append(",");
				sb.append(StringEscapeUtils.escapeCsv(series.getManufacturer()));
				sb.append(",");
				sb.append(StringEscapeUtils.escapeCsv(series.getManufacturerModelName()));
				sb.append(",");
				sb.append(StringEscapeUtils.escapeCsv(series.getSoftwareVersion()));
				sb.append(",");
				sb.append(series.getSeriesId());
				sb.append(",");
				sb.append("\n");
			}
		}
		int lastComma = sb.lastIndexOf(",");
		sb.replace(lastComma, lastComma, "");
		sb.trimToSize();
		return sb.toString();
	}
}
