/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: LoginDataProcessor.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
*/
/*
 * Created on Jul 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.ncia.util;

import gov.nih.nci.ncia.db.DataAccessProxy;
import gov.nih.nci.ncia.db.IDataAccess;

import java.io.FileInputStream;
import java.util.Properties;


/**
 * @author Jin Chen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoginDataProcessor implements IDataProcessor {
    Properties databaseProp = new Properties();
    IDataAccess ida = null;

    /* (non-Javadoc)
     * @see gov.nih.nci.ncia.util.IDataProcessor#connect()
     */
    public void connect() throws Exception {
        // TODO Auto-generated method stub
        databaseProp.load(new FileInputStream(IDataProcessor.DATABASE_MAPPING));
        ida = (new DataAccessProxy()).getInstance(Integer.parseInt(
                    databaseProp.getProperty("database_mapping")));
        ida.open();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ncia.util.IDataProcessor#disconnect()
     */
    public void disconnect() throws Exception {
        // TODO Auto-generated method stub
        ida.close();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ncia.util.IDataProcessor#process(java.lang.Object)
     */
    public void process(Object o) {
        // TODO Auto-generated method stub
    }

    public void process(Object o, String url) throws Exception {
        // TODO Auto-generated method stub
    }
}
