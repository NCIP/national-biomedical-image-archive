/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * $Id: DataBuilder.java 4417 2008-04-18 20:43:12Z saksass $
 *
 * $Log: not supported by cvs2svn $
 * Revision 1.17  2006/12/19 16:24:42  zhouro
 * changed data type from Integer to Double
 *
 * Revision 1.16  2006/09/28 19:29:00  panq
 * Reformated with Sun Java Code Style and added a header for holding CVS history.
 *
 */
/*
 * Created on Jul 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.ncia.tool.mirctest;

import gov.nih.nci.ncia.db.IDataAccess;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Rona Zhou
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DataBuilder {

	Logger log = Logger.getLogger(DataBuilder.class);

	private IDataAccess ida;

	public DataBuilder(IDataAccess ida) throws Exception {
		this.ida = ida;
	}

	protected Object update(String hql, Object o) throws Exception {
		// Hibernate native way
		List retList = null;
		retList = (List) ida.search(hql);

		// System.out.println( "size: " + retList.size() );
		if ((retList != null) && (retList.size() != 0)) {
			o = retList.get(0);
		}
		return o;
	}

	protected Date stringToDate(String numbers) throws Exception {
		Date retval = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		if (numbers != null) {
			retval = sdf.parse(numbers);
		}

		return retval;
	}
	
	protected IDataAccess getIDataAccess(){
		return ida;
	}

}
