package gov.nih.nci.ncia;

import gov.nih.nci.cagrid.ncia.service.NCIACoreServiceConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.dbunit.DatabaseTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import com.mockrunner.ejb.JNDIUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.globus.wsrf.Constants;

public abstract class AbstractDbTestCaseImpl extends DatabaseTestCase {

    protected AbstractDbTestCaseImpl() {
        super();
        //move this out to properties file


    	InputStream propertiesInputStream = this.getClass()
                                                .getClassLoader()
                                                .getResourceAsStream("testdb.properties");

    	try {
    		Properties connectionProperties = new Properties();
    		connectionProperties.load(propertiesInputStream);

    		for(Object propName : connectionProperties.keySet()) {
    			String propVal = (String)connectionProperties.getProperty((String)propName);

    			if(propName.equals("dbunit.driver_class")) {
    	            System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
    	            		            propVal );
    			}
    			if(propName.equals("dbunit.connection_url")) {
    	            System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
    	            		            propVal );
    			}
    			if(propName.equals("dbunit.username")) {
    	            System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
    	            		            propVal );
    			}
    			if(propName.equals("dbunit.password")) {
    	            System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
    	            		            propVal );
    			}
    		}
    	}
    	catch(IOException ioex) {
    		throw new RuntimeException(ioex);
    	}




    }

	protected void setUp() throws Exception {
		super.setUp();

		//this would happen in jndi-config.xml in real system.
		//hack around for unit testing
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(System.getProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS));
		dataSource.setJdbcUrl(System.getProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL));
		dataSource.setUser(System.getProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME));
		dataSource.setPassword(System.getProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD));

        NCIACoreServiceConfiguration mockConfig = new NCIACoreServiceConfiguration();
        mockConfig.setDatabaseDataSource("java:comp/env/nciaDatabase");
        NCIACoreServiceConfiguration.configuration = mockConfig;

		JNDIUtil.initMockContextFactory();
		InitialContext ctx = new InitialContext();
		ctx.bind("java:comp/env/nciaDatabase", //NCIACoreServiceConfiguration.getConfiguration().getDatabaseDataSource(),
				 dataSource);

	}

    /**
     * Return the name of the XML file that contains the data set.
     * This string should specify a file/resource on the class path
     */
    protected abstract String getDataSetResourceSpec();

    protected IDatabaseConnection getConnection() throws Exception {

        Class.forName(System.getProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS));

        Connection jdbcConnection = DriverManager.getConnection(System.getProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL),
                                                     System.getProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME),
                                                     System.getProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD));

        return new DatabaseConnection(jdbcConnection);
    }


    protected IDataSet getDataSet() throws Exception {
    	InputStream dataSetInputStream = this.getClass()
    	                                     .getClassLoader()
    	                                     .getResourceAsStream(getDataSetResourceSpec());
    	return new FlatXmlDataSet(dataSetInputStream);
    }
}
