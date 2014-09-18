/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is part of dcm4che, an implementation of DICOM(TM) in
 * Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
 *
 * The Initial Developer of the Original Code is
 * Bill Wallace, Agfa HealthCare Inc., 
 * Portions created by the Initial Developer are Copyright (C) 2007
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * Bill Wallace <bill.wallace@agfa.com>
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
package gov.nih.nci.nbia.wadosupport;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

import org.dcm4che.data.DcmObject;
//import org.dcm4che.data.;
import org.apache.log4j.Logger;

/**
 * A wado image is a buffered image return object that includes methods to
 * retrieve window level information.
 * 
 * @author bwallace
 * 
 */
public class WadoImage   {
   private static final String ERROR_KEY = "_ERROR";

private static final Logger log = Logger.getLogger(WadoImage.class);
   
	public static String WINDOW_CENTER = "windowCenter";
	public static String WINDOW_WIDTH = "windowWidth";
	public static String FRAME_NUMBER= "frameNumber";
	
	public static String IMG_AS_BYTES = "asBytes";
	public static String AS_BYTES_RETURNED_TRANSFER_SYNTAX = "asBytesReturnedTransferSyntax";
	
	/** 
	 * A return parameter value when IMG_AS_BYTES is a returned byte[] parameter,
	 *  will have this as the key, and a java.awt.Point as the subsample factors. 
	 */
	public static String AS_BYTES_RETURNED_SUBSAMPLE_FACTOR = "asBytesReturnedSubsampleFactor";

	public static String IMG_AS_BYTES_ONLY_FOR_TRANSFER_SYNTAXES = "asBytesOnlyForTransferSyntaxes";
	
	private DcmObject ds;
	private int stored;
	private String filename;
	private BufferedImage value;
	
	/** 
	 * Create a Wado image on the given buffered image object
	 * 
	 * @param value is the source image
	 * @param stored the number of bits stored for this image.  May not agree with the header value
     *        if this is computed based on other factors to get the right value.
	 * @param value the BufferedImage value of the object.  If this is null, and there is no
	 *        IMG_AS_BYTES parameter set (for raw data), then this is no pixel data.
     */
	public WadoImage(DcmObject ds, int stored, BufferedImage value) {
		this.ds = ds;
		this.stored = stored;
	}

	/** Used for clone operations */
	protected WadoImage(WadoImage fr) {
		this.ds = fr.ds;
		this.stored = fr.stored;
		this.filename = fr.filename;
	}


	/** Clone this object */
	public WadoImage clone() {
		WadoImage ret = new WadoImage(this);
		return ret;
	}

	/**
	 * Gets the dimension of the image data returned, which could be a BufferedImage
	 * or a byte[] in the IMG_AS_BYTES parameter.
	 * @return the actual buffer dimensions (which may not match the DICOM dimensions),
	 *         or null if there is no image data here.
	 */
	public Dimension getBufferDimension() {
		BufferedImage image = value;
		if (image != null) {
			int width = image.getWidth();
			int height = image.getHeight();
			return new Dimension(width,height);
		} else {
			// fix
			//Point subsample = (Point)getParameter(	WadoImage.AS_BYTES_RETURNED_SUBSAMPLE_FACTOR);
			Point subsample = new Point();
			if ( (ds != null ) && ( subsample != null ) && ( subsample.x > 0 ) && ( subsample.y > 0 ) ) {
				//int width = ds.getInt(Tag.Columns);
				//int height = ds.getInt(Tag.Rows);
				// fix
				int width = 1;
				int height = 1;
				if ( ( width > 0) && ( height > 0 ) ) {
					width = ( width + subsample.x - 1 ) / subsample.x;
					height = ( height + subsample.y - 1 ) / subsample.y;
					return new Dimension( width, height );
				} else {
					log.warn("Dicom dimensions of " + width + "x" + height + " are invalid.");
				}
			} else {
				if ( ds == null ) {
					log.warn("No DICOM header.  Can't get dimensions.");
				} else {
					log.warn("No valid set subsample indices for raw asBytes buffer.  Can't get buffer dimensions");
				}
			}
		}
		return null;
	}
	
	/**
	 * Figure out the size of this object - somewhat of a hueristic, as we don't
	 * want to go through all the map data, but assuming that the map isn't too
	 * large, then the size is some multiple of the parameter map size, plus a
	 * multiple of the width and height of the pixel data.
	 * @return number of bytes that this image takes up, plus heuristic amount of bytes for space for the parameters.
	 */
	public long getSize() {
		//long size = getParameterMap().size() * 128l;
		long size = 1 * 128l;
		log.debug("Size from parameter map only "+size);
		BufferedImage image = value;
		if (image != null) {
			int width = image.getWidth();
			int height = image.getHeight();
			int bits = image.getColorModel().getPixelSize();
			int channels = image.getColorModel().getNumComponents();
			int totalBits = bits * channels;
			if (totalBits <= 8)
				size += width * height;
			else if (totalBits <= 16)
				size += width * height * 2;
			else if (totalBits <= 32)
				size += width * height * 2;
			else
				size += width * height * channels * 2;
			log.debug("Size from parameter map + image "+size);
		}
		// fix
	    //byte[] raw = (byte[]) getParameter(IMG_AS_BYTES);
		///if( raw!=null ) {
		//   size += raw.length;
		//   log.debug("Size adding from image as bytes "+size);
		//}
		return size;
	}

	/** Splits region into sub-parts 
	 */
	public static double[] splitRegion(String region) {
		// fix
		//return FilterUtil.splitDouble(region,4);
		return null;
	}

	/** Returns the dicom object associated with this image */
   public DcmObject getDicomObject() {
      return ds;
   }

   /** Returns the number of bits stored for this image.  May not agree with the header value
    * if this is computed based on other factors to get the right value.
    */
   public int getStored() {
      return stored;
   }

   public String getFilename() {
   	return filename;
   }
   public void setFilename(String filename) {
   	this.filename = filename;
   }

   /** Indicate that one of the required transforms failed - must have a null image as well to be non-recoverable */
   public void setError(Exception e) {
	   // fix
      // this.setParameter(ERROR_KEY, e);
   }
   
   public Throwable getError() {
	   // fix
       //return (Throwable) this.getParameter(ERROR_KEY);
	   return new Exception();
   }

   /** Indicates a non-recoverable error has occurred */
   public boolean hasError() {
	   // fix
       //return getValue()==null && getError()!=null;
	   return false;
   }

}
