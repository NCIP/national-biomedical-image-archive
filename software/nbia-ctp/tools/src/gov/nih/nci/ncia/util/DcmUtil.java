/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: DcmUtil.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
*/
package gov.nih.nci.ncia.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.DcmParser;
import org.dcm4che.data.DcmParserFactory;
import org.dcm4che.data.FileFormat;


public class DcmUtil {
    final static DcmParserFactory pFact = DcmParserFactory.getInstance();
    final static DcmObjectFactory oFact = DcmObjectFactory.getInstance();

    /*
     * parse method is to parse a dicom file with a given stop tag
     * @param        File        -        dicom file
     *                         int                -        stop tag integer
     * @return        Dataset        -        dicom data set
     */
    public static Dataset parse(File f, int stopTag) throws IOException {
        FileFormat fileFormat = getFileFormat(f);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
        DcmParser parser = pFact.newDcmParser(in);
        Dataset dataset = oFact.newDataset();
        parser.setDcmHandler(dataset.getDcmHandler());
        parser.parseDcmFile(fileFormat, stopTag);
        in.close();

        return dataset;
    }

    //	public static void writeValue( String tag, String value, String filename ) throws IOException {
    //		Dataset dataset = parse( new File(filename), Tags.PixelData );
    //		String age = dataset.getString( Integer.valueOf(tag, 16) );
    //		
    //		int len = -1;
    //		String temp;
    //		byte[] buffer = new byte[100];
    //		BufferedInputStream in = new BufferedInputStream( new FileInputStream(filename));
    //		while( (len= in.read(buffer) ) != -1 ) {
    //			temp = new String(buffer);
    //			System.out.println("|" + temp +"|");
    //			
    //		}
    //		in.close();
    //	}

    /*
     * getFileFormat is to validate dicom file format
     * @param        File        -        a file
     * @return        FileFormat        -        a file format detected by dcm parser
     */
    public static FileFormat getFileFormat(File f) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
        DcmParser parser = pFact.newDcmParser(in);
        FileFormat fileFormat = parser.detectFileFormat();

        if (fileFormat == null) {
            throw new IOException("Unrecognized file format: " + f);
        }

        in.close();

        return fileFormat;
    }

    public static void eraseDICOMHeader(File f1) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f1));
        File f2 = new File(f1.getAbsolutePath() +
                System.getProperty("user.name") + System.currentTimeMillis());
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
                    f2));
        out.write(new byte[128]);

        //skip first 128 bytes
        in.read(new byte[128]);

        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) != -1) {
            if (len < buffer.length) {
                for (int i = 0; i < len; i++) {
                    out.write(buffer[i]);
                }
            } else {
                out.write(buffer);
            }
        }

        in.close();
        out.close();
        f1.delete();
        f2.renameTo(f1);
    }
}
