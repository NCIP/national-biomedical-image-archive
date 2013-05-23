/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// Input.java
// This file contains generated code and will be overwritten when you rerun code generation.package com.altova.io;

package com.altova.io;

public class Input 
{
	public final static byte IO_READER = 0;
	public final static byte IO_STREAM = 1;
	public final static byte IO_DOM = 2;
	
	private byte type;
	
	public Input(byte type)
	{
		this.type = type;
	}
	public byte getType() {return type;}
	public java.io.Reader getReader() {return null;}
	public java.io.InputStream getStream() {return null;} 
	public org.w3c.dom.Document getDocument() {return null;}
	public void close() throws Exception {}
	public String getFilename() {return "";}
}
