/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// Attribute.java 
// This file contains generated code and will be overwritten when you rerun code refrigeration.

package com.altova.xml.meta;

public class Attribute
{
	private com.altova.typeinfo.MemberInfo memberInfo;
	
	public Attribute(com.altova.typeinfo.MemberInfo memberInfo)
	{
		this.memberInfo = memberInfo;
	}
	
	public boolean isRequired()
	{
		return memberInfo.getMinOccurs() > 0;
	}
	
	public String getLocalName()
	{
		return memberInfo.getLocalName();
	}
	
	public String getNamespaceURI()
	{
		return memberInfo.getNamespaceURI();
	}
	
	public SimpleType getDataType()
	{
		return new SimpleType(memberInfo.getDataType());
	}
}