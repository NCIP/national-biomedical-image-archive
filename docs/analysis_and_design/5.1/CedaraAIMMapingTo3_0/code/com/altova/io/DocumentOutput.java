/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// DocumentOutput.java
// This file contains generated code and will be overwritten when you rerun code generation.

package com.altova.io;

import org.w3c.dom.Document;

public class DocumentOutput extends Output 
{
	private Document document;
	
	public DocumentOutput(Document document)
	{
		super(Output.IO_DOM);
		this.document = document;
	}
	
	public Document getDocument() {return document;}
}
