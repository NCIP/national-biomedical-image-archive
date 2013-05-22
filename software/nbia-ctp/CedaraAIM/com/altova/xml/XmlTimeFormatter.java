/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// XmlTimeFormatter.java
// This file contains generated code and will be overwritten when you rerun code generation.

package com.altova.xml;

import com.altova.types.DateTime;

public class XmlTimeFormatter extends XmlFormatter
{
	public String format(DateTime dt)
	{
		return dt.toTimeString("0.0######");
	}
}
