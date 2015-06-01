package gov.nih.nci.ncia.search;
import java.util.*;
public class APIURLHolder {
	private static String url;
	private static String wadoUrl;
	private static Map<String, String> userMap=new HashMap<String, String>();
	public static String getUrl()
	{
		return url;
	}
	public static String addUser(String user)
	{
		try {
		for(Map.Entry<String, String> entry : userMap.entrySet()){
		    System.out.printf("Key : %s and Value: %s %n", entry.getKey(), entry.getValue());
		    if (entry.getValue().equals(user)){
		    	return entry.getKey();
		    }
		}
		} catch (Exception e)
		{

		}
		UUID userKey = UUID.randomUUID();
		userMap.put(userKey.toString(), user);
		System.out.println("!!!!!!!!!!!Added user-"+user+"Key-"+userKey.toString());
		return userKey.toString();

	}
	public static String getUser(String key){
		return userMap.get(key);
	}
	public static void setUrl(String urlIn){
		url=urlIn;
		wadoUrl=urlIn+"/ncia/wado";
		System.out.println("-------> api url is "+url);
	}
	public static String getWadoUrl()
	{
		return wadoUrl;
	}
    public static void main(String[] args)
    {
    	setUrl("http://localhost:8080/nbia/home.jsf");
    }
}
