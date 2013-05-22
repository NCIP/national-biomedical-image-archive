/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.net.URL;


public class Util {


	/**
	 * Load the specified properties file from the classpath, and add every property
	 * as a system property.  This is typically used to assist testing.
	 *
	 * <P>If resrouce is not to be found, a RuntimeException will be thrown.
	 */
    public static void loadSystemPropertiesFromPropertiesResource(String propertiesResourceName) {
		try {
        	Properties props = new Properties();
        	URL url = ClassLoader.getSystemResource(propertiesResourceName);
        	props.load(url.openStream());
        	for(Object key : props.keySet()) {
				System.setProperty((String)key, (String)props.get(key));
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

    /**
     * Utility method for creating display values
     *
     * @see gov.nih.nci.ncia.criteria.Criteria#getDisplayValue()
     */
    public static String getCommaSeparatedList(Collection<String> stringValues) {
        // Create a comma separated list
        StringBuilder values = new StringBuilder();

        if(stringValues.size()==0) {
        	return values.toString();
        }

        Iterator<String> i = stringValues.iterator();
        values.append((String) i.next());

        while (i.hasNext()) {
            values.append(", ");
            values.append((String) i.next());
        }

        return values.toString();
    }

    public static String nullSafeString(Object obj) {
    	if(obj==null) {
    		return null;
    	}
    	else {
    		return obj.toString();
    	}
    }

	public static <T> Collection<T> removeNullElement(Collection<T> collection) {
	    Collection<T> result = new ArrayList<T>();
	    for(T t : collection) {
            if(t!=null) {
				result.add(t);
			}
		}
		return result;
    }

	/**
	 * Take a list like [1,2,3,4,5] and with a chunk size of 2 break it into a list
	 * like [[1,2],[3,4],[5]]
	 */
	public static <T> List<List<T>> breakListIntoChunks(List<T> masterList, int CHUNK_SIZE) {
        List<List<T>> chunks = new ArrayList<List<T>>();
        List<T> currentChunk = new ArrayList<T>();

        chunks.add(currentChunk);

        int count = 0;

        for(T object : masterList)
        {
        	if (count == CHUNK_SIZE)
        	{
        		currentChunk = new ArrayList<T>();
        		chunks.add(currentChunk);
        		count = 0;
        	}

        	currentChunk.add(object);

        	count++;
        }
        return chunks;
	}

	public static <T> void dumpCollection(Collection<T> collection) {
		for(T o : collection) {
			System.out.println(o);
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean isEmptyCollection(Collection list)	{
		boolean empty = false;
		if (list != null && list.size() > 0){
		    empty = false;
	    }
		else{
			empty = true;
		}
		return empty;
	}

	public static boolean hasAtLeastOneNonBlankArgument(String ... args)	{
		if(args==null) {
			return false;
		}
		for(int i=0;i<args.length;i++) {
			if(!args[i].equals("")) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasAtLeastOneNonNullArgument(Object ... args)
	{
		if(args==null) {
			return false;
		}
		for(int i=0;i<args.length;i++) {
			if(args[i]!=null) {
				return true;
			}
		}
		return false;
	}


    public static String calculateOffsetValue(Date date1, Date date2){
    	String offsetValue=null;
    	long offsetMonths=0;
    	long offsetDays=0;
    	//make sure date2 is greater than date1
    	if(date2.after(date1)){
	    	long diffInMilliSec = date2.getTime() - date1.getTime();

	    	long days = diffInMilliSec/MILLISEC_PER_DAY;
	    	//month different, take an average of 30 days per month
	    	offsetMonths = days/30;

	    	long remainder= days%30;

	    	if(remainder >=15){
	    		offsetMonths++;
	    	}
	    	else{
	    		//only days different
	    		offsetDays=remainder;
	    		offsetValue = Long.valueOf(offsetDays).intValue() + " day(s)";
	    	}
    	}
    	if(offsetMonths > 0){
    		offsetValue = Long.valueOf(offsetMonths).intValue() + " month(s)";
    	}
    	return offsetValue;
    }

    public static String currrentDateTimeString() {
    	Date now = new Date();
    	return DateFormat.getDateTimeInstance().format(now);
    }

    private static Integer MILLISEC_PER_DAY = 1000*60*60*24;
}
