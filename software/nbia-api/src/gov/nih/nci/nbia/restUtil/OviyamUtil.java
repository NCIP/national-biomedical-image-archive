package gov.nih.nci.nbia.restUtil;



import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.util.*;
public class OviyamUtil {
	
    private static Map<String, String> userMap = new HashMap<String, String>();
    private static Map<String, CollectionUtil> userCollections = new HashMap<String, CollectionUtil>();
    
	public static String getUser(String oviyamId, String wadoUrl)
	{
		String returnValue = null;
		if (userMap.get(oviyamId)!=null)
		{
			System.out.println("found user for "+oviyamId+" is "+userMap.get(oviyamId));
			return userMap.get(oviyamId);
		}
		String fullURL = wadoUrl+"?requestType=oviyamLookup&oviyamId="+oviyamId;
		System.out.println("User request: "+fullURL);
		HttpGet httpGet = new HttpGet(fullURL);
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse response1 = httpclient.execute(httpGet);
			returnValue = EntityUtils.toString(response1.getEntity());
			if (returnValue.equals("NOT FOUND")){
				returnValue=null;
			} else {
				userMap.put(oviyamId, returnValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	public static List<String> getUserCollections(String user){
		long now = System.currentTimeMillis();
		CollectionUtil util = userCollections.get(user);
		if (util==null)
		{
			return null;
		}
		else
		{
			if (now < util.getCreated())
			{
				return util.getAuthorizedCollections();
			}
			else
			{
				return null;
			}
		}
	}
	public static void setUserCollections(String user, List authorizedCollections){
		long now = System.currentTimeMillis() + 6000000L;
		CollectionUtil util = new CollectionUtil();
        util.setCreated(now);
        util.setAuthorizedCollections(authorizedCollections);
        userCollections.put(user, util);
	}
	
}
