/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// UrlInput.java
// This file contains generated code and will be overwritten when you rerun code generation.package com.altova.io;

package com.altova.io;

public class UrlInput extends StreamInput 
{
	String url;
	UrlInput(String url) throws Exception
	{
		super(new java.net.URL(url).openStream());
		this.url = url;
	}
	
	public String getFilename() {return url;}
	public void close() throws Exception {getStream().close();}
}