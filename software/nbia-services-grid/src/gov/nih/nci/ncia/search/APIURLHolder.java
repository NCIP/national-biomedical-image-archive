package gov.nih.nci.ncia.search;

public class APIURLHolder {
	private static String url;
	public static String getUrl()
	{
		return url;
	}
	public static void setUrl(String urlIn){
		String temp = urlIn.substring(0, urlIn.lastIndexOf("/")-1);
		temp = temp.substring(0, temp.lastIndexOf("/"));
		url=temp;
		System.out.println("-------> api url is "+url);
	}
    public static void main(String[] args)
    {
    	setUrl("http://localhost:45210/nbia/home.jsf");
    }
}
