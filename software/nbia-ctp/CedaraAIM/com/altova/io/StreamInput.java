/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// StreamInput.java
// This file contains generated code and will be overwritten when you rerun code generation.package com.altova.io;

package com.altova.io;

import java.io.InputStream;

public class StreamInput extends Input
{
	private InputStream stream;
	
	public StreamInput (InputStream stream)
	{
		super(Input.IO_STREAM);
		this.stream = stream;
	}
	
	public InputStream getStream() {return stream;}
	public void close() throws Exception {stream.close();}
	
	static public StreamInput createInput(String u) throws Exception
	{
		if (u.trim().indexOf(':') > 1)
		{
			java.net.URL url = new java.net.URL(u);
			
			if ("file".equals(url.getProtocol()))
				return new FileInput(url.getPath());
			return new UrlInput(u);
		}
			
		return new FileInput(u);
	}
}
