/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// FileInput.java
// This file contains generated code and will be overwritten when you rerun code generation.package com.altova.io;

package com.altova.io;

public class FileInput extends StreamInput 
{
	private String filename;
	public FileInput(String filename) throws Exception
	{
		super(new java.io.FileInputStream(filename));
		this.filename = filename;
	}
	
	public String getFilename() {return filename;}
	public void close() throws Exception {getStream().close();}
}
