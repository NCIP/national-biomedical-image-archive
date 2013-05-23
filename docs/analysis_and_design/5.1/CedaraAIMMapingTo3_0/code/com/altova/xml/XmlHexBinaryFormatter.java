/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// XmlHexBinaryFormatter.java
// This file contains generated code and will be overwritten when you rerun code generation.

package com.altova.xml;

public class XmlHexBinaryFormatter extends XmlFormatter
{
	public String format(byte[] v)
	{
		return com.altova.HexBinary.encode(v);
	}

	public byte[] parseBinary(String s)
	{
		return com.altova.HexBinary.decode(s);
	}
}