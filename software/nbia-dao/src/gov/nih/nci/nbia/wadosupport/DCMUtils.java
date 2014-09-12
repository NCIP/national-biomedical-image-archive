package gov.nih.nci.nbia.wadosupport;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
public class DCMUtils {
private static boolean scanned =false;
private static ImageReader reader;
private static DicomImageReadParam param;
public static synchronized byte[] getJPGFromFile(File file)
{

	BufferedImage myJpegImage = null;
	if (!scanned)
	{
	   scanned=true;
	   ImageIO.scanForPlugins();
	   Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");
	   reader=(ImageReader) iter.next();
	   System.out.println("reader is not has been set");
	   param = (DicomImageReadParam) reader.getDefaultReadParam();
	}
	if (reader==null)
	{
		   System.out.println("reader was null");
		   ImageIO.scanForPlugins();
		   Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");
		   reader=(ImageReader) iter.next();  
	}
    
	try {
		ImageInputStream iis = ImageIO.createImageInputStream(file);
		reader.setInput(iis, false);
		myJpegImage = reader.read(0, param);   
		iis.close();
	
		if (myJpegImage == null) {
			System.out.println("ERROR: Cannot read DICOM image. BufferedImage is NULL");
			return null;
		}
         
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
		encoder.encode(myJpegImage);
		output.close();
		return output.toByteArray();
    
	} catch(IOException e) {
		System.out.println("ERROR: Cannot read DICOM image");
		e.printStackTrace();
	}
	return null;
}
	    
		public static void main(String[] args) {
	        

	    }

}
