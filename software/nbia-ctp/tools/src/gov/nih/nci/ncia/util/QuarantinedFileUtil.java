/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: QuarantinedFileUtil.java 6368 2008-09-16 19:16:52Z kascice $
*
* $Log: not supported by cvs2svn $
* Revision 1.3  2007/08/20 17:47:43  lethai
* use PrintWriter
*
* Revision 1.2  2006/09/28 19:29:00  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author lethai
 *
 */
public class QuarantinedFileUtil {
    public void getActuallFiles(String quarantinedFileLocation,
        String outputFileName) throws Exception {
        FileInputStream fis;
        BufferedReader quaFile;
        String lineItem = null;
        File dir = new File(quarantinedFileLocation);
        File[] files = dir.listFiles();
        //SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        File outputFile = new File(outputFileName);
        PrintWriter pw= new PrintWriter(outputFile);

        // Also create list of .qe files
//      File qeOutputFile = new File("C:\\temp\\qe_" + outputFileName);
//        PrintWriter qpw= new PrintWriter(qeOutputFile);

        // go through loop of files, open it read the actual file name
        for (int k = 0; k < files.length; k++) {
            File a = files[k];
            long timestamp = files[k].lastModified();
            Date fileDate = new Date(timestamp);
            //Date fileDateWithFormat = sdf.parse(sdf.format(fileDate));
            if(a.getName().endsWith("qe")) {

            	//qpw.println( "/usr/local/tomcat-5.5.17/webapps/NCICBIMAGE/trial/quarantine/" + a.getName());

	            fis = new FileInputStream(a);
	            quaFile = new BufferedReader(new InputStreamReader(fis));
	            lineItem = quaFile.readLine();
	            //System.out.println("actual file location = " + lineItem);
	            fis.close();

	            //s +=lineItem + "\n";
	            //sb.append(lineItem + "\\n");
	            //String date = sdf.format(fileDateWithFormat);
	            sdf.parse(sdf.format(fileDate));
	            //pw.println(lineItem + "\t" + fileDateWithFormat);
	            pw.println(lineItem + "\t" + sdf.format(fileDate));
//	            pw.println(lineItem);
	            //props.setProperty(lineItem , "   "+ fileDateWithFormat);

	            // Uncomment to add jpegs to the list...
//	            String jpgBase= lineItem.replace(".dcm", "_base.jpeg");
//	            String jpgIcon96 = lineItem.replace(".dcm", "_icon96.jpeg");
//	            String jpgIcon = lineItem.replace(".dcm", "_icon.jpeg");
//	            pw.println(jpgBase);
//	            pw.println(jpgIcon96);
//	            pw.println(jpgIcon);
            }
        } // end of for loop

        pw.close();

//        qpw.close();

        //store(props, new File(outputFileName));
    }

//    void store(Properties props, File file) {
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//
//            props.store(fos, "Test Properties");
//            //System.out.println("fos " + fos.toString());
//            fos.flush();
//            fos.close();
//        } catch (Exception e) {
//            e.getMessage();
//        }
//    }
//
//    void store(StringBuilder sb, File file) {
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//
//            OutputStreamWriter out;
//            String s = sb.toString();
//            out = new OutputStreamWriter(fos, s);
//
//            System.out.println(s);
//            fos.flush();
//            fos.close();
//        } catch (IOException e2) {
//            System.out.println("The file " + file + " could not be opened.");
//        } catch (Exception e) {
//            e.getMessage();
//        }
//    }

    public static void main(String[] args) {
        QuarantinedFileUtil qfu = new QuarantinedFileUtil();
//        String location = args[0];

        try {
            qfu.getActuallFiles(args[0], args[1]);
            System.out.println("finished!");
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
