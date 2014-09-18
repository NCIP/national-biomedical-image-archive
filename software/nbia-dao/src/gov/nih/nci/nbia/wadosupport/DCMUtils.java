package gov.nih.nci.nbia.wadosupport;
import java.awt.image.*;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.Iterator;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;
import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che2.imageioimpl.plugins.dcm.DicomImageReader;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
public class DCMUtils {
private static boolean scanned =false;
private static ImageReader reader;
private static DicomImageReadParam param;
static Logger log = Logger.getLogger(DCMUtils.class);
public synchronized static  JPEGResult getJPGFromFile(File file, WADOParameters params)
{
	JPEGResult returnValue=new JPEGResult();
	BufferedImage myJpegImage = null;
	if (!scanned)
	{
	   scanned=true;
	   ImageIO.scanForPlugins();
	   Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");
	   while (iter.hasNext()){
		 System.out.println("getting next reader");
	     try {
			reader=(ImageReader) iter.next();
			 System.out.println("The default ReadParam is "+reader.getDefaultReadParam().getClass().getName());
			 if (reader.getDefaultReadParam() instanceof DicomImageReadParam)
			 {
			     param = (DicomImageReadParam) reader.getDefaultReadParam();
			     System.out.println("right reader found "+reader.getClass().getName());
			     break;
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("------------ Problem with reader ---------------");
		}
	   }
	}
	if (reader==null)
	{
		   log.info("reader was null");
		   ImageIO.scanForPlugins();
		   Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");
		   reader=(ImageReader) iter.next();  
		   while (iter.hasNext()){
			   System.out.println("getting next reader");
			     try {
					reader=(ImageReader) iter.next();
					 System.out.println("The default ReadParam is "+reader.getDefaultReadParam().getClass().getName());
					 if (reader.getDefaultReadParam() instanceof DicomImageReadParam)
					 {
					     param = (DicomImageReadParam) reader.getDefaultReadParam();
					     System.out.println("right reader found "+reader.getClass().getName());
					     break;
					 }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println("------------ Problem with reader ---------------");
				}
		   }
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
        if (params.getRowsInt()!=-1)
        {
        	BufferedImage  bdest;
        	bdest=toBufferedImage(myJpegImage.getScaledInstance(-1, params.getRowsInt(), myJpegImage.SCALE_DEFAULT));
        	myJpegImage=bdest;
        }
        if (params.getColumnsInt()!=-1)
        {
        	BufferedImage  bdest;
        	bdest=toBufferedImage(myJpegImage.getScaledInstance(params.getColumnsInt(), -1, myJpegImage.SCALE_DEFAULT));
        	myJpegImage=bdest;
        }		
        if (params.getWindowWidthInt()!=-1)
        {
        	RescaleOp rescale= new RescaleOp(params.getWindowWidthInt(),params.getWindowCenterInt(), null);
        	myJpegImage=rescale.filter(myJpegImage,null);

        }	
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
		
		if (params!=null&&params.getQualityFloat()!=-1)
		{
			JPEGEncodeParam enParam=JPEGCodec.getDefaultJPEGEncodeParam(myJpegImage);
			enParam.setQuality(params.getQualityFloat(), true);
			encoder.setJPEGEncodeParam(enParam);
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
	        params.setImageQuality("10");
	        //params.setFrameNumber("16");
	        //params.setRows("50");
	        // params.setWindowCenter("10");
	       //  params.setWindowWidth("40");
	        File file = new File("/dev/test.dcm");
	        JPEGResult result=getJPGFromFile(file,params);
	        result.getImages().toString();
	        try {
				FileOutputStream fos = new FileOutputStream(new File("/dev/out.jpg"));

               //create an object of BufferedOutputStream
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				System.out.println(result.getImages().length);
				bos.write(result.getImages());
				bos.flush();
				bos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	    }
		private static BufferedImage toBufferedImage(Image img)
		{
		    if (img instanceof BufferedImage)
		    {
		        return (BufferedImage) img;
		    }

		    // Create a buffered image with transparency
		    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

		    // Draw the image on to the buffered image
		    Graphics2D bGr = bimage.createGraphics();
		    bGr.drawImage(img, 0, 0, null);
		    bGr.dispose();

		    // Return the buffered image
		    return bimage;
		}
}
