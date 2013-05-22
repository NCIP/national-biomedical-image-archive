/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// XmlGMonthFormatter.java
// This file contains generated code and will be overwritten when you rerun code generation.

package com.altova.xml;

import com.altova.CoreTypes;
import com.altova.types.CalendarBase;
import com.altova.types.DateTime;

class XmlGMonthFormatter extends XmlFormatter
{
	public String format(DateTime dt)
	{
		String result = "--";
		int month = dt.getMonth();
		result += CoreTypes.formatNumber(month, 2);
		if (dt.hasTimezone() != CalendarBase.TZ_MISSING)
			result += CoreTypes.formatTimezone(dt.getTimezoneOffset());
		return result;
	}
}
