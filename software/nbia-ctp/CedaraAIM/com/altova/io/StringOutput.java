/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// StringOutput.java
// This file contains generated code and will be overwritten when you rerun code generation.package com.altova.io;

package com.altova.io;

import java.io.StringWriter;

public class StringOutput extends WriterOutput 
{	
	public StringOutput()
	{
		super(new StringWriter());
	}
	
	public StringBuffer getString() {return ((StringWriter)getWriter()).getBuffer();}
}
