package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.download.SeriesData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lethai
 *
 */
public class JnlpArgumentsParser {

    public static List<SeriesData> parse(String [] args){
        int length = args.length;
        List<SeriesData> seriesDataList = new ArrayList<SeriesData>();
        for(int i=0; i< length; i++ ){
            SeriesData series = new SeriesData();
            String[] result = args[i].split("\\|");
            series.setCollection(result[0]);
            series.setPatientId(result[1]);
            series.setStudyInstanceUid(result[2]);
            series.setSeriesInstanceUid(result[3]);
            if(result[4].equalsIgnoreCase("yes") ){
                series.setHasAnnotation(true);
            }else if(Boolean.valueOf(result[4])){
                series.setHasAnnotation(true);
            }
            //series.setHasAnnotation(Boolean.valueOf(result[4]));
            series.setNumberImages(result[5]);
            series.setImagesSize(Integer.valueOf(result[6]));
            series.setAnnoSize(Integer.valueOf(result[7]));

            series.setUrl(result[8]);
            series.setDisplayName(result[9]);
            series.setLocal(Boolean.valueOf(result[10]));

            seriesDataList.add(series);
        }
        return seriesDataList;
    }
}