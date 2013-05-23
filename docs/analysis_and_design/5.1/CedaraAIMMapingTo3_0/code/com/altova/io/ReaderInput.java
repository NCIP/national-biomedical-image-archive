/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// ReaderInput.java
// This file contains generated code and will be overwritten when you rerun code generation.package com.altova.io;

package com.altova.io;

import java.io.Reader;

public class ReaderInput extends Input
{
	private Reader reader;
	
	public ReaderInput(Reader reader)
	{
		super(Input.IO_READER);
		this.reader = reader;
	}
	
	public Reader getReader() {return reader;}
}

