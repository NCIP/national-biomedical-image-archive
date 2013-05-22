/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// XmlGYearFormatter.java
// This file contains generated code and will be overwritten when you rerun code generation.

package com.altova.xml;

import com.altova.CoreTypes;
import com.altova.types.CalendarBase;
import com.altova.types.DateTime;

class XmlGYearFormatter extends XmlFormatter
{
	public String format(DateTime dt)
	{
		String result = "";
		int year = dt.getYear();
		if (year < 0)
		{
			result += '-';
			year = -year;
		}

		result += CoreTypes.formatNumber(year, 4);
		if (dt.hasTimezone() != CalendarBase.TZ_MISSING)
			result += CoreTypes.formatTimezone(dt.getTimezoneOffset());
		return result;
	}
}
