/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// Xs.java
// This file contains generated code and will be overwritten when you rerun code generation.

package com.altova.xml;

import com.altova.typeinfo.ValueFormatter;

public class Xs
{
	public static ValueFormatter StandardFormatter = new XmlFormatter();
	public static ValueFormatter TimeFormatter = new XmlTimeFormatter();
	public static ValueFormatter DateFormatter = new XmlDateFormatter();
	public static ValueFormatter DateTimeFormatter = StandardFormatter;
	public static ValueFormatter GYearFormatter = new XmlGYearFormatter();
	public static ValueFormatter GMonthFormatter = new XmlGMonthFormatter();
	public static ValueFormatter GDayFormatter = new XmlGDayFormatter();
	public static ValueFormatter GYearMonthFormatter = new XmlGYearMonthFormatter();
	public static ValueFormatter GMonthDayFormatter = new XmlGMonthDayFormatter();
	public static ValueFormatter HexBinaryFormatter = new XmlHexBinaryFormatter();
	public static ValueFormatter IntegerFormatter = new XmlIntegerFormatter();
	public static ValueFormatter DecimalFormatter = StandardFormatter;
	public static ValueFormatter AnySimpleTypeFormatter = StandardFormatter;
	public static ValueFormatter DurationFormatter = StandardFormatter;
	public static ValueFormatter DoubleFormatter = StandardFormatter;
	public static ValueFormatter Base64BinaryFormatter = StandardFormatter;
}
