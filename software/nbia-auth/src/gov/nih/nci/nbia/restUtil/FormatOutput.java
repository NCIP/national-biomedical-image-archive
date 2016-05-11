package gov.nih.nci.nbia.restUtil;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.util.List;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.lang.reflect.Field;

import org.owasp.esapi.ESAPI;

/**
 * This utility will convert data to different formats. Note: this java
 * class requires the ESAPI 1.4.4 jar file ESAPI is used to encode data
 * 
 * @author panq
 */
public class FormatOutput {

	/**
	 * This method will format data into JSON format.
	 * 
	 * It important to check to make sure that all DataType that are being used
	 * is properly encoding.
	 * 
	 * String is currently the only dataType that is being encode by ESAPI
	 * 
	 * @param columns - String[]
	 *        data    - List<Object[]>
	 * @return - JSON array
	 */
	public static JSONArray toJSONArrayInstance(String[] columns, List<Object[]> data) {
		JSONArray json = new JSONArray(); // JSON array that will be returned
//		String temp = null;

		try {

			for (Object[] objects : data) {
				JSONObject obj = new JSONObject();
				for (int i = 0; i < columns.length; i++) {
					//Do not think json format needs this protection so comment the encoding out
//					if (objects[i] != null && objects[i].getClass().getName().equals("java.lang.String")) {
//						temp = (String)objects[i]; // saving column data to temp variable
//						temp = ESAPI.encoder().canonicalize(temp); // decoding data to base state
//						temp = ESAPI.encoder().encodeForHTML(temp); // encoding
//						obj.put(columns[i], temp);
//					}
//					else 	
				//	System.out.println("column:"+columns[i]+"-object"+objects[i]);
					if (objects[i]!=null)
					{
					    obj.put(columns[i], objects[i]);
					} else
					{
						obj.put(columns[i], new Long(1));
					}
				}
				json.put(obj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json; // return JSON array
	}
	/**
	 * This method will format data into JSON format.
	 * 
	 * It important to check to make sure that all DataType that are being used
	 * is properly encoding.
	 * 
	 * String is currently the only dataType that is being encode by ESAPI
	 * 
	 * @param columns - String[]
	 *        data    - List<Object[]>
	 * @return - JSON array
	 */
	public static JSONArray toJSONArray(String[] columns, List<Object[]> data) {
		JSONArray json = new JSONArray(); // JSON array that will be returned
//		String temp = null;

		try {

			for (Object[] objects : data) {
				JSONObject obj = new JSONObject();
				for (int i = 0; i < columns.length; i++) {
					//Do not think json format needs this protection so comment the encoding out
//					if (objects[i] != null && objects[i].getClass().getName().equals("java.lang.String")) {
//						temp = (String)objects[i]; // saving column data to temp variable
//						temp = ESAPI.encoder().canonicalize(temp); // decoding data to base state
//						temp = ESAPI.encoder().encodeForHTML(temp); // encoding
//						obj.put(columns[i], temp);
//					}
//					else 	
					
					obj.put(columns[i], objects[i]);
				}
				json.put(obj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json; // return JSON array
	}
	
	/**
	 * This method will format data into HTML table format.
	 * 
	 * It important to check to make sure that all DataType that are being used
	 * is properly encoding.
	 * 
	 * String is currently the only dataType that is being encode by ESAPI
	 * 
	 * @param columns - String[]
	 *        data    - List<Object[]>
	 * @return - String
	 */
	public static String toHtml(String[] columns, List<Object[]> data) {
		StringBuffer sb= new StringBuffer();
		String temp = null;

		try {
			sb.append("<P ALIGN='center'><TABLE BORDER=1>");
			 // table header
			 sb.append("<TR>");
			 for (int i = 0; i < columns.length; i++) {
				   sb.append("<TH>" + columns[i] + "</TH>");
				   }
				 sb.append("</TR>");
			for (Object[] objects : data) {
				sb.append("<TR>");
				for (int i = 0; i < columns.length; i++) {

					if (objects[i] != null){
						if (objects[i].getClass().getName().equals("java.lang.String")) {
						temp = (String)objects[i]; // saving column data to temp variable
						temp = ESAPI.encoder().canonicalize(temp); // decoding data to base state
						temp = ESAPI.encoder().encodeForHTML(temp); // encoding
						sb.append("<TD>" + temp + "</TD>");
						}
						else sb.append("<TD>" + objects[i].toString() + "</TD>");
					}
					else 																	
						sb.append("<TD>" + "&nbsp;" + "</TD>");
				}
				sb.append("</TR>");
			}
			sb.append("</TABLE></P>");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString(); 
	}
	
	/**
	 * This method will format data into XML format.
	 * 
	 * It important to check to make sure that all DataType that are being used
	 * is properly encoding.
	 * 
	 * String is currently the only dataType that is being encode by ESAPI
	 * 
	 * @param columns - String[]
	 *        data    - List<Object[]>
	 * @return - String
	 */
	public static String toXml(String[] columns, List<Object[]> data) {
		StringBuffer sb= new StringBuffer();
		String temp = null;

		try {
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
			 // table header
			 sb.append("<Results>");
	  				 
			for (Object[] objects : data) {
				sb.append("<Row>");
				for (int i = 0; i < columns.length; i++) {
					if (objects[i] != null){
						String type = objects[i].getClass().getName();
					if (type.equals("java.lang.String")) {
						
						temp = (String)objects[i]; // saving column data to temp variable
						temp = ESAPI.encoder().canonicalize(temp); // decoding data to base state
						temp = ESAPI.encoder().encodeForHTML(temp); // encoding
						sb.append("<"+columns[i] + " type='" +type +"'>" + temp + "</"+ columns[i]+">");
					}
					//keep the commented code there because it might be needed for changing the output of time format 
//					else if (type.equals("java.sql.Timestamp")) {
//						sb.append("<"+columns[i] + " type='" +type +"'>" + ((java.sql.Timestamp)objects[i]).toString() + "</"+ columns[i]+">");
//					}
					else 
						sb.append("<"+columns[i] + " type='" +type +"'>" + objects[i].toString() + "</"+ columns[i]+">");
					}
				}
				sb.append("</Row>");
			}
			sb.append("</Results>");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString(); 
	}
	
	/**
	 * This method will format data into CSV format.
	 * 
	 * It important to check to make sure that all DataType that are being used
	 * is properly encoding.
	 * 
	 * String is currently the only dataType that is being encode by ESAPI
	 * 
	 * @param columns - String[]
	 *        data    - List<Object[]>
	 * @return - String
	 */
	public static String toCsv(String[] columns, List<Object[]> data) {
		StringBuffer sb= new StringBuffer();
		String temp = null;

		try {
			 // table header
			 for (int i = 0; i < columns.length; i++) {
				 if (i > 0)
				   sb.append("," + columns[i]);
				 else sb.append(columns[i]);
				   }
				 sb.append("\n");
			for (Object[] objects : data) {
				for (int i = 0; i < columns.length; i++) {
					if (objects[i] != null){
						//Do not think csv format needs this protection so comment the encoding out
						if (objects[i].getClass().getName().equals("java.lang.String")) {
						temp = (String)objects[i]; // saving column data to temp variable
//						temp = ESAPI.encoder().canonicalize(temp); // decoding data to base state
//						temp = ESAPI.encoder().encodeForHTML(temp); // encoding
						
						if (i > 0)
							sb.append("," + temp);
						else sb.append(temp);
						}
						else {
							if (i > 0)
								sb.append("," + objects[i].toString());
							else sb.append(objects[i].toString());
						}
					}
					else /* object is null */
					{	
						if (i > 0)
							sb.append("," );
					}
				}
				sb.append("\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString(); 
	}

	/**
	 * This method will format data into HTML table format.
	 * 
	 * It important to check to make sure that all DataType that are being used
	 * is properly encoding.
	 * 
	 * String is currently the only dataType that is being encode by ESAPI
	 * 
	 * @param column - String
	 *        data    - List<Object[]>
	 * @return - String
	 */
	public static String toHtml(String column, List<String> data) {
		StringBuffer sb= new StringBuffer();
		String temp = null;

		try {
			sb.append("<P ALIGN='center'><TABLE BORDER=1>");
			 // table header
			 sb.append("<TR>");
			 sb.append("<TH>" + column + "</TH>");
			 sb.append("</TR>");
			for (String object : data) {
				sb.append("<TR>");
				if (object != null){
					temp = (String)object; // saving column data to temp variable
					temp = ESAPI.encoder().canonicalize(temp); // decoding data to base state
					temp = ESAPI.encoder().encodeForHTML(temp); // encoding
					sb.append("<TD>" + temp + "</TD>");
					}
					else 																	
						sb.append("<TD>" + "&nbsp;" + "</TD>");
				sb.append("</TR>");
			}
			sb.append("</TABLE></P>");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString(); 
	}

	/**
	 * This method will format data into JSON format.
	 * 
	 * It important to check to make sure that all DataType that are being used
	 * is properly encoding.
	 * 
	 * String is currently the only dataType that is being encode by ESAPI
	 * 
	 * @param columns - String
	 *        data    - List<String>
	 * @return - JSON array
	 */
	public static JSONArray toJSONArray(String column, List<String> data) {
		JSONArray json = new JSONArray(); // JSON array that will be returned
//		String temp = null;

		try {
			for (String object : data) {
				JSONObject obj = new JSONObject();
				//Do not think json format needs this protection so comment the encoding out				
//				if (object != null) {
//					temp = object; // saving column data to temp variable
//					temp = ESAPI.encoder().canonicalize(temp); // decoding data
//																// to base state
//					temp = ESAPI.encoder().encodeForHTML(temp); // encoding
//					obj.put(column, temp);
//				} else
					obj.put(column, object);

				json.put(obj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json; // return JSON array
	}

	/**
	 * This method will format data into XML format.
	 * 
	 * It important to check to make sure that all DataType that are being used
	 * is properly encoding.
	 * 
	 * String is currently the only dataType that is being encode by ESAPI
	 * 
	 * @param column - String
	 *        data    - List<String>
	 * @return - String
	 */
	public static String toXml(String column, List<String> data) {
		StringBuffer sb = new StringBuffer();
		String temp = null;

		try {
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
			// table header
			sb.append("<Results>");

			for (String object : data) {
				sb.append("<Row>");
				if (object != null) {
					//The type is String obviously now, but for the purpose of supporting data as list of generic object 
					//e.g. a list of TimeStamp, let's use the following statement to figure out the type in runtime 
					//Of course we need to change the second argument type to "List<Object> data" for support generic data type.
					String type = object.getClass().getName();
					temp = object; // saving column data to temp
											// variable
					temp = ESAPI.encoder().canonicalize(temp); // decoding data
																// to base state
					temp = ESAPI.encoder().encodeForHTML(temp); // encoding
					sb.append("<" + column + " type='" + type + "'>" + temp
							+ "</" + column + ">");
				}
				sb.append("</Row>");
			}
			sb.append("</Results>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * This method will format data into CSV format.
	 * 
	 * It important to check to make sure that all DataType that are being used
	 * is properly encoding.
	 * 
	 * String is currently the only dataType that is being encode by ESAPI
	 * 
	 * @param column - String
	 *        data    - List<String>
	 * @return - String
	 */
	public static String toCsv(String column, List<String> data) {
		StringBuffer sb = new StringBuffer();
//		String temp = null;

		try {
			// table header
			sb.append(column + "\n");
			for (String object : data) {
				if (object != null) {
					//Do not think csv format needs this protection so comment the encoding out
//					temp = object; // saving column data to temp variable
//					temp = ESAPI.encoder().canonicalize(temp); // decoding data
//																// to base state
//					temp = ESAPI.encoder().encodeForHTML(temp); // encoding
//					sb.append(temp);
					sb.append(object);
				} else /* object is null */
				{
					sb.append("\n");
				}
				sb.append("\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
