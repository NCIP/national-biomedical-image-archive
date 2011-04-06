package gov.nih.nci.ncia.basket;

import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.ncia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.util.ArrayList;
import java.util.List;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class ExternalDataBasketTestCase extends AbstractDbUnitTestForJunit4 {

    @Test
    public void testGetSeriesDTOFromPatientIDs() throws Exception
    {
        IDatabaseConnection connection = databaseTester.getConnection();
        ITable table = connection.createQueryTable("patient", "select patient_id from patient");

        List<String> pids = new ArrayList<String>();
        for (int i = 0; i < table.getRowCount(); i++)
        {
            pids.add((String)table.getValue(i, "patient_id"));
            System.out.println("pids: " + pids.get(i));
        }


        List<SeriesSearchResult> seriesDtos = rsh.getSeriesDTOList(pids, null, null, authMgr);

        System.out.println("Size: " + seriesDtos.size());
        Assert.assertEquals( seriesDtos.size(), 51);
    }

    @Test
    public void testGetSeriesDTOFromStudyIDs() throws Exception
    {
        IDatabaseConnection connection = databaseTester.getConnection();
        ITable table = connection.createQueryTable("study", "select study_instance_uid from study");

        List<String> studyIds = new ArrayList<String>();
        for (int i = 0; i < table.getRowCount(); i++)
        {
            studyIds.add((String)table.getValue(i, "study_instance_uid"));
        }

        List<SeriesSearchResult> seriesDtos = rsh.getSeriesDTOList(null, studyIds, null, authMgr);
        System.out.println("Size: " + seriesDtos.size());
        Assert.assertEquals (seriesDtos.size(), 51);
    }

    @Test
    public void testGetSeriesDTOFromSeriesIDs() throws Exception
    {
        IDatabaseConnection connection = databaseTester.getConnection();
        ITable table = connection.createQueryTable("general_series", "select series_instance_uid from general_series");

        List<String> seriesIds = new ArrayList<String>();
        for (int i = 0; i < table.getRowCount(); i++)
        {
            seriesIds.add((String)table.getValue(i, "series_instance_uid"));
        }

        List<SeriesSearchResult> seriesDtos = rsh.getSeriesDTOList(null, null, seriesIds, authMgr);
           System.out.println("Size: " + seriesDtos.size());
        Assert.assertEquals (seriesDtos.size() , 51);
    }

    @Test
    public void testGetSeriesDTOFromPatientAndStudy() throws Exception
    {
        List<String> patientIds = new ArrayList<String>();
		patientIds.add("1.3.6.1.4.1.9328.50.1.0003");
		final int NUM_SERIES_IN_PATIENT = 21;
		
        List<String> studyIds = new ArrayList<String>();
        studyIds.add("1.3.6.1.4.1.9328.50.1.2100"); //overlapp
        studyIds.add("1.3.6.1.4.1.9328.50.1.2674");
        final int NUM_SERIES_IN_STUDY = 3;
        
        List<SeriesSearchResult> seriesDtos = rsh.getSeriesDTOList(patientIds, 
        		                                                   studyIds, 
        		                                                   null, 
        		                                                   authMgr);
        System.out.println("Size: " + seriesDtos.size());

        Assert.assertEquals(seriesDtos.size() ,NUM_SERIES_IN_PATIENT + NUM_SERIES_IN_STUDY);
     }


     @Test
     public void testGetSeriesDTOFromPatientAndSeries() throws Exception
     {
         List<String> patientIds = new ArrayList<String>();
         patientIds.add("1.3.6.1.4.1.9328.50.1.0003");

         List<String> seriesIds = new ArrayList<String>();


         IDatabaseConnection connection = databaseTester.getConnection();

         ITable seriesTable = connection.createQueryTable("general_series",
                                                          "select series_instance_uid from general_series where patient_pk_id <> 3");

         final int NUM_SERIES_NOT_IN_PATIENT = 30;
         Assert.assertEquals(seriesTable.getRowCount() ,NUM_SERIES_NOT_IN_PATIENT);


         for(int i = 0; i < seriesTable.getRowCount(); i++)
         {
             seriesIds.add((String)seriesTable.getValue(i, "series_instance_uid"));
         }

         List<SeriesSearchResult> seriesDtos = rsh.getSeriesDTOList(patientIds, null, seriesIds, authMgr);

         final int NUM_SERIES_IN_PATIENT = 21;
         Assert.assertEquals(seriesDtos.size(), NUM_SERIES_NOT_IN_PATIENT + NUM_SERIES_IN_PATIENT);
     }

    @Test
    public void testGetSeriesDTOFromStudyAndSeries() throws Exception
    {
		List<String> studyIds = new ArrayList<String>();
        List<String> seriesIds = new ArrayList<String>();

        studyIds.add("1.3.6.1.4.1.9328.50.1.1149");

        final int NUM_SERIES_IN_STUDY = 3;

        seriesIds.add("1.3.6.1.4.1.9328.50.1.1150"); //overlap
        seriesIds.add("1.3.6.1.4.1.9328.50.1.13857");
        seriesIds.add("1.3.6.1.4.1.9328.50.1.13861");


        List<SeriesSearchResult> seriesDtos = rsh.getSeriesDTOList(null, studyIds, seriesIds, authMgr);

        Assert.assertEquals(seriesDtos.size(), NUM_SERIES_IN_STUDY + 3 - 1);
     }


    @Test
    public void testGetSeriesDTOFromPatientAndStudyAndSeries() throws Exception
    {
		List<String> patientIds = new ArrayList<String>();
		patientIds.add("1.3.6.1.4.1.9328.50.1.0003");
		final int NUM_SERIES_IN_PATIENT = 21;

        List<String> studyIds = new ArrayList<String>();
        studyIds.add("1.3.6.1.4.1.9328.50.1.14021");
        studyIds.add("1.3.6.1.4.1.9328.50.1.1149"); //overlapp
        final int NUM_SERIES_IN_STUDY = 3;

        List<String> seriesIds = new ArrayList<String>();
        seriesIds.add("1.3.6.1.4.1.9328.50.1.14026"); //overlapp
        seriesIds.add("1.3.6.1.4.1.9328.50.1.14161");

        List<SeriesSearchResult> seriesDtos = rsh.getSeriesDTOList(patientIds, studyIds, seriesIds, authMgr);
        System.out.println("Size: " + seriesDtos.size());
        Assert.assertEquals(seriesDtos.size(), NUM_SERIES_IN_PATIENT + NUM_SERIES_IN_STUDY + 1);
     }


    /*
     * @see TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        rsh = new ExternalDataBasket();
        authMgr = new AuthorizationManager("kascice");
    }


    protected String getDataSetResourceSpec() {

        return TEST_DB_FLAT_FILE;
    }


    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/min_studies_testdata.xml";

    private ExternalDataBasket rsh = null;

    private AuthorizationManager authMgr;
}
