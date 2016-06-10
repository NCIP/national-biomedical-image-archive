/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.nbia.servlet;

/**
 * @author lethai
 *
 */

import gov.nih.nci.nbia.lookup.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DynamicJNLPServlet extends HttpServlet
{
	//private static Logger logger = Logger.getLogger(DynamicJNLPServlet.class);
    
	private String jnlpTemplate="jnlpTemplate.jnlp";

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
		String jnlp="";
		StringBuffer jnlpBuilder = this.getJnlp( req );
		try{
			String userId =(String)req.getSession().getAttribute("userId");
			Boolean includeAnnotation =(Boolean)req.getSession().getAttribute("includeAnnotation");
			//String userPassword =(String)req.getSession().getAttribute("userPassword");

			//System.out.println("userId: " + userId + " includeAnnotation " + includeAnnotation );
			List<BasketSeriesItemBean> bsib = (List<BasketSeriesItemBean>)req.getSession().getAttribute("series");
			
			//System.out.println("request type: " + req.getMethod());

			int size = bsib.size();
			StringBuffer argsBuilder = new StringBuffer();

			for(int i=0; i<size; i++){
				String collection = bsib.get(i).getProject();
				String patientId = bsib.get(i).getPatientId();
				String studyInstanceUid = bsib.get(i).getStudyId();
				String seriesInstanceUid =bsib.get(i).getSeriesId();
				String annotation = bsib.get(i).getAnnotated();
				Integer numberImages = bsib.get(i).getTotalImagesInSeries();
				String argument = "<argument>" +collection + "|" + patientId + 
				"|"+studyInstanceUid + "|" + seriesInstanceUid+ "|" + annotation + "|" + numberImages +"</argument>" ;
				//System.out.println(i + "\t" + collection + "|" + patientId + "|"+studyInstanceUid + "|" + seriesInstanceUid+ "|" + annotation);

				argsBuilder.append(argument);
			}

			//get user id and included annotation
			StringBuffer propXMLBuilder = new StringBuffer();
			propXMLBuilder.append(this.getPropertyXML( "includeAnnotation", includeAnnotation.toString()));
			propXMLBuilder.append(this.getPropertyXML( "userId", userId));

			this.replaceProperties(propXMLBuilder, jnlpBuilder);

			this.replaceArguments(argsBuilder.toString(), jnlpBuilder);

			jnlp = jnlpBuilder.toString();   		
			
		}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	
        try {
        	System.out.println(jnlp);
        	  String attachment = "inline; filename=\"dynamic-unique-jnlp-"  
              	+ System.currentTimeMillis() + ".jnlp\"";   
			System.out.println(attachment);
			res.setContentType("application/x-java-jnlp-file");   
			res.setHeader("Content-Disposition", attachment); 
			
			OutputStreamWriter out = new OutputStreamWriter(res.getOutputStream());   
			System.out.println("outputstreamwrite: " + out);
			
			out.write(jnlp);   
			System.out.println("write out the stream...");
			out.flush();   
			System.out.println("flush.....");
			out.close();  
			System.out.println("close....... the stream");
		}
		catch( IOException e ) {
			System.err.println( "DynamicJNLPServlet Error: " + e.getMessage() );
			res.sendError( HttpServletResponse.SC_NOT_FOUND );
		}catch(Exception e){
			e.printStackTrace();
			System.err.println( "DynamicJNLPServlet Error: " + e.getMessage() );
			res.sendError( HttpServletResponse.SC_NOT_FOUND );
		}
		
		
	}

	public void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
		doGet(req,res);
	}

	private String getPropertyXML( String name, String value ) {
		return "<property name=\"" + name + "\" value=\"" + value + "\"/>\n";
	}



	private StringBuffer getJnlp( HttpServletRequest req ) throws IOException {
		/*String uriSansContext = req.getRequestURI().substring(
            req.getRequestURI().indexOf( req.getContextPath() ) + req.getContextPath().length() );
		 */
		String filepath = this.getServletContext().getRealPath( jnlpTemplate );

		System.out.println( "Path: " + filepath );

		int ch;
		StringBuffer sbuilder = new StringBuffer();
		BufferedReader br = new BufferedReader( new FileReader( filepath ) );

		while( ( ch = br.read() ) > -1 ) {
			sbuilder .append((char)ch);
		}

		br.close();

		this.replaceCodebase( req, sbuilder );
		return sbuilder;
	}

	private void replaceCodebase( HttpServletRequest req, StringBuffer jnlpBuilder ) {
		int start = jnlpBuilder.toString().indexOf( "$$codebase" );
		int length = "$$codebase".length();

		String uriSansContext = req.getRequestURI().substring(
				req.getRequestURI().indexOf( req.getContextPath() ) + req.getContextPath().length() );

		String url = req.getRequestURL().toString();
		System.out.println("url: " + url);
		int codeBasePosition = url.indexOf(uriSansContext);

		String codebase = url.substring( 0, codeBasePosition  );

		jnlpBuilder.replace( start, start+length, codebase + "/");

		start = jnlpBuilder.toString().indexOf( "$$codebase" );
		if(start > 0){
			jnlpBuilder.replace( start, start+length, codebase );
		}
	}



	private void replaceProperties( StringBuffer propXmlBuilder,  StringBuffer jnlpBuilder ) {
		if( jnlpBuilder.toString().indexOf( "$$properties" ) < 0 ) {
			return;
		} 

		int start = jnlpBuilder.toString().indexOf( "$$properties" );
		int length = "$$properties".length();

		jnlpBuilder.replace( start, start+length, propXmlBuilder.toString() );
	}


	private void replaceArguments(String arg, StringBuffer jnlpBuilder){
		int start = jnlpBuilder.toString().indexOf( "$$arguments" );
		int length = "$$arguments".length();

		jnlpBuilder.replace( start, start+length, arg );    

	}


//	private void replaceDisplayName( String title, StringBuffer sb ) {
//		int start = sb.toString().indexOf( "$$title" );
//		System.out.println("title position: " + start);
//		int length = "$$title".length();
//		System.out.println("title length: " + length);
//
//		sb.replace( start, start+length, title );
//
//	}
}
