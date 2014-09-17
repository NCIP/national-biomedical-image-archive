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
import org.dcm4che2.imageioimpl.plugins.dcm.DicomImageReader;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
public class DCMUtils {
private static boolean scanned =false;
private static ImageReader reader;
private static DicomImageReadParam param;
public static JPEGResult getJPGFromFile(File file, WADOParameters params)
{
	JPEGResult returnValue=new JPEGResult();
	BufferedImage myJpegImage = null;
	if (!scanned)
	{
	   scanned=true;
	   ImageIO.scanForPlugins();
	   Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");
	   while (iter.hasNext()){
	     reader=(ImageReader) iter.next();
	     System.out.println("reader is "+reader.getDefaultReadParam().getClass().getName());
	     if (reader.getDefaultReadParam() instanceof DicomImageReadParam)
	     {
	         param = (DicomImageReadParam) reader.getDefaultReadParam();
	         System.out.println("right reader found "+reader.getClass().getName());
	         break;
	     }
	   }
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
		if (params.getFrameNumberInt()!=0){
		  try
		  {
			DicomImageReader dReader = (DicomImageReader)reader;
			int numberOfImages=dReader.getNumImages(true);
			if (numberOfImages-1<params.getFrameNumberInt())
			{
				returnValue.setErrors("Frame number requested is larger than number of images");
				return returnValue;
			}
		  } catch (Exception e)
		  {
			returnValue.setErrors("Cannot use dicom specific reader");
			e.printStackTrace();
		  }
		   myJpegImage = reader.read(params.getFrameNumberInt(), param);
		} else {
		   myJpegImage = reader.read(0, param);   
		}
		iis.close();
	
		if (myJpegImage == null) {
			System.out.println("ERROR: Cannot read DICOM image. BufferedImage is NULL");
			returnValue.setErrors("Cannot read DICOM image. BufferedImage is NULL");
			return returnValue;
		}
         
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
		JPEGEncodeParam enParam=JPEGCodec.getDefaultJPEGEncodeParam(myJpegImage);
		if (params!=null&&params.getQualityFloat()!=-1)
		{
			enParam.setQuality(params.getQualityFloat(), true);
		}
		
		encoder.encode(myJpegImage);
		output.close();
		returnValue.setImages(output.toByteArray());
		return returnValue;
    
	} catch(IOException e) {
		System.out.println("ERROR: Cannot read DICOM image");
		returnValue.setErrors("Cannot read DICOM image");
		e.printStackTrace();
	}
	return returnValue;
}
	    
		public static void main(String[] args) {
	        WADOParameters params = new WADOParameters();
	        params.setImageQuality("50");
	        params.setFrameNumber("16");
	        File file = new File("/dev/test2.dcm");
	        getJPGFromFile(file,params);

	    }

}
