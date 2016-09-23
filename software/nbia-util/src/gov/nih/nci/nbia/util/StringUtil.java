/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.util.List;

public class StringUtil {
	//if gt then 999,999... who cares i guess....
	public static String displayAsSixDigitString(int seriesCnt) {
        return String.format("%06d", seriesCnt);
    }

	public static boolean isEmpty(String string) {
		return (string == null) || string.equals("");
	}

	/**
	 * Expects a non-null string..... grabs the last token from the
	 * string.
	 *
	 * <p>So, "foo, cow, moo" with separator , would yield "moo".
	 * If the separator can't be found, this returns null.
	 */
	public static String lastToken(String str, String separator) {
		int dex = str.lastIndexOf(separator);
		if(dex==-1) {
			return null;
		}
		else {
			return str.substring(dex+separator.length());
		}
	}

	/**
	 * A little more efficient than string.trim().length()==0
	 * because it doesnt create a new string
	 */
	public static boolean isEmptyTrim(String string) {
		if(string == null) {
			return true;
		}
		for(int i=0;i<string.length();i++) {
			if(!Character.isWhitespace(string.charAt(i))) {
				return false;
			}
		}
		return true;
	}

    public static String removeIllegitCharacters(String text) {
    	StringBuffer buffer = new StringBuffer();

    	for(int i = 0; i < text.length(); i++) {
    		char ch = text.charAt(i);
    		if(charsToRemove.indexOf(ch) == -1) {
    			buffer.append(ch);
    		}
    		//else ignore it
    	}
    	return buffer.toString();
    }

    public static boolean doesContainIllegitCharacters(String text) {
    	boolean spChars = false;

    	for(int i = 0; i < text.length(); i++) {
    		char ch = text.charAt(i);
    		if(charsToRemove.indexOf(ch) > -1) {
    			spChars = true;
    			break;
    		}
    	}
    	return spChars;
    }


	public static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 3);
		for (int b : bytes) {
			int workingByte = b;
			workingByte &= 0xff;
		    sb.append(HEXDIGITS[workingByte >> 4]);
		    sb.append(HEXDIGITS[workingByte & 15]);
		    sb.append(' ');
		}
		return sb.toString();
	}

	/**
	 * Remove the html tag from the input string
	 * @param htmlString
	 * @return string without html tags
	 */
	public static String removeHTML(String htmlString){
		String noHTMLString = htmlString.replaceAll("\\<.*?\\>", "");

		// Remove Carriage return from java String
		noHTMLString = noHTMLString.replaceAll("\r", "<br/>");

		// Remove New line from java string and replace html break
		noHTMLString = noHTMLString.replaceAll("\n", " ");
		noHTMLString = noHTMLString.replaceAll("\'", "&#39;");
		noHTMLString = noHTMLString.replaceAll("\"", "&quot;");
		return noHTMLString;
	}

	public static String encodeListEntriesWithSingleQuotes(List<String> list){
        StringBuilder sb = new StringBuilder();
        for(String sop: list){
            sb.append("'" + sop + "',");
        }

        if(sb.length() > 0){
            sb.deleteCharAt(sb.length() - 1);

            return sb.toString();
        }else{
            return "";
        }
    }
	private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

    private static final String charsToRemove = "`~!@#$%^*()-+={}[]|;:'<>?,./";

    private StringUtil () {

    }
}
