/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;


/**
 * Represents a DICOM image (retrieved through drill down).
 * 
 * <P><b>WARNING!</b> This object is serialized so if you change it, you risk
 * breaking remote search and the grid interface
 */
public interface ImageSearchResult extends Comparable<ImageSearchResult>  {
	/**
	 * This should identify an image uniquely at a given node.
	 * 
	 * <P>Likely implementation is a pkid from the database.
	 */	
    public Integer getId();
    
    
	/**
	 * The DICOM SOP instance uid for this image.
	 * Example: "1.3.4.x.y.z"
	 */	    
    public String getSopInstanceUid();

    
	/**
	 * The DICOM series instance uid for the series this image is in.
	 * Example: "1.3.4.x.y.z"
	 */    
    public String getSeriesInstanceUid();   
    
    
    /**
     * Return the series (pk) id of the series the image belongs to.
     */
    public Integer getSeriesId(); 
   
    
    /**
     * Return the instance number of this image within the series (from DICOM tags ultimately)
     * 
     * <p>This may be null.
     */
    public Integer getInstanceNumber();

    
    /**
     * This returns a URL to retrieve a thumbnail image for this result.
     * This can be null if the search code isn't sure about the URL (local).
     * 
     * <p>I'm torn about this.  In a way, this is really a presentation detail
     * v. part of the "result".  On the other hand, the presentation layer
     * would have to assume alot about the URL to reach a thumbnail on a remote
     * machine.... so put it here to let it be computed as part of search.
     * 
     * <p>In reality, the local v. remote thumbnails are done differently anyway
     * because the local thumbnail stuff is handled in session and allows private
     * images to be seen whereas the remote is only public images.... all the
     * more reason to put this here.
     */
    public String getThumbnailURL();
	
    
    /**
     * The size of the DICOM image in BYTES.
     */
	public Long getSize();
	
    /**
     * The node that this image was found on.
     * 
	 * <p>This is intentionally not a property to avoid serialization.
     */
	public NBIANode associatedLocation();
	
	
	/**
	 * Associate a node with this result.  This should only be called once
	 * by a result generator.
	 * 
	 * <p>This is intentionally not a property to avoid serialization.
	 */
	public void associateLocation(NBIANode nbiaNode);  	
}