/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.basket;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.search.LocalNode;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.ncia.search.NBIANode;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"gov.nih.nci.nbia.security.NCIASecurityManager$RoleType",
                                  "gov.nih.nci.nbia.search.LocalNode"})
@PrepareForTest({ExternalDataBasket.class,  
                 SpringApplicationContext.class, 
                 LocalNode.class})                                  
public class ExternalDataBasketTestCase {

    @Test
    public void testGetSeriesDTOFromPatientIDs() throws Exception
    {
        List<String> patientIds = new ArrayList<String>();
        patientIds.add("1.3.6.1.4.1.9328.50.1.0003");
        patientIds.add("1.3.6.1.4.1.9328.50.1.0028");
        patientIds.add("1.3.6.1.4.1.9328.50.1.0005");
                    
        final int NUM_SERIES = 6;
        
        //////expectations////////////
        expect(generalSeriesDAOMock.findSeriesByPatientId(patientIds, sites, ssgs)).
            andReturn(createSeriesDTOResponse(NUM_SERIES));    
        expect(generalSeriesDAOMock.findSeriesByStudyInstanceUid(null, sites, ssgs)).
            andReturn(null);  // i believe this indicates a mostly harmless design flaw
        expect(generalSeriesDAOMock.findSeriesBySeriesInstanceUID(null, sites, ssgs)).
            andReturn(null); // i believe this indicates a mostly harmless design flaw        

        //replay the mock
        replayMocks();
        
        //OUT
        externalDataBasket  = new ExternalDataBasket();
        List<SeriesSearchResult> seriesSearchResultList = externalDataBasket.getSeriesDTOList(patientIds, 
                                                                                              null, 
                                                                                              null, 
                                                                                              authorizationManagerMock);
        Assert.assertEquals( seriesSearchResultList.size(), NUM_SERIES);
    }
    


    @Test
    public void testGetSeriesDTOFromStudyIDs() throws Exception
    {
        List<String> studyIds = new ArrayList<String>();
        studyIds.add("1.3.6.1.4.1.9328.50.1.0003");
        studyIds.add("1.3.6.1.4.1.9328.50.1.0028");
        studyIds.add("1.3.6.1.4.1.9328.50.1.0005");
                     
        final int NUM_SERIES = 22;
        
        //////expectations////////////
        expect(generalSeriesDAOMock.findSeriesByStudyInstanceUid(studyIds, sites, ssgs)).
            andReturn(createSeriesDTOResponse(NUM_SERIES)); 
        expect(generalSeriesDAOMock.findSeriesBySeriesInstanceUID(null, sites, ssgs)).
            andReturn(null); // i believe this indicates a mostly harmless design flaw        

        replayMocks();
        
        //OUT
        externalDataBasket  = new ExternalDataBasket();
        List<SeriesSearchResult> seriesSearchResultList = externalDataBasket.getSeriesDTOList(null, 
                                                                                              studyIds, 
                                                                                              null, 
                                                                                              authorizationManagerMock);
        Assert.assertEquals( seriesSearchResultList.size(), NUM_SERIES);      
    }

   
    @Test
    public void testGetSeriesDTOFromSeriesIDs() throws Exception
    {
        List<String> seriesIds = new ArrayList<String>();
        seriesIds.add("1.3.6.1.4.1.9328.50.1.0003");
        seriesIds.add("1.3.6.1.4.1.9328.50.1.0028");
        seriesIds.add("1.3.6.1.4.1.9328.50.1.0005");
                 
        final int NUM_SERIES = 12;
        
        //////expectations////////////
        expect(generalSeriesDAOMock.findSeriesByStudyInstanceUid(null, sites, ssgs)).
            andReturn(createSeriesDTOResponse(NUM_SERIES));    // i believe this indicates a mostly harmless design flaw    
        expect(generalSeriesDAOMock.findSeriesBySeriesInstanceUID(seriesIds, sites, ssgs)).
            andReturn(createSeriesDTOResponse(NUM_SERIES)); ////
        ////////////////////////////////////////////////////////
        
        replayMocks();
        
        //OUT
        externalDataBasket  = new ExternalDataBasket();
        List<SeriesSearchResult> seriesSearchResultList = externalDataBasket.getSeriesDTOList(null, 
                                                                                              null, 
                                                                                              seriesIds, 
                                                                                              authorizationManagerMock);
        Assert.assertEquals( seriesSearchResultList.size(), NUM_SERIES);         
    }

    
    
    @Test
    public void testGetSeriesDTOFromPatientAndStudy() throws Exception
    {
        List<String> patientIds = new ArrayList<String>();
        patientIds.add("1.3.6.1.4.1.9328.50.1.0003");
        final int NUM_SERIES_IN_PATIENT = 21;
        
        List<String> studyIds = new ArrayList<String>();
        studyIds.add("1.3.6.1.4.1.9328.50.1.2100"); 
        studyIds.add("1.3.6.1.4.1.9328.50.1.2674");
        final int NUM_SERIES_IN_STUDY = 3;      
        
        final int NUM_OVERLAPPING_SERIES = 2;
        
        //////expectations////////////
        expect(generalSeriesDAOMock.findSeriesByPatientId(patientIds, sites, ssgs)).
               andReturn(createSeriesDTOResponse(NUM_SERIES_IN_PATIENT));        
        expect(generalSeriesDAOMock.findSeriesByStudyInstanceUid(studyIds, sites, ssgs)).
            andReturn(createSeriesDTOResponse(NUM_SERIES_IN_PATIENT-NUM_OVERLAPPING_SERIES,
                                              NUM_SERIES_IN_PATIENT-NUM_OVERLAPPING_SERIES+NUM_SERIES_IN_STUDY));        
        expect(generalSeriesDAOMock.findSeriesBySeriesInstanceUID(null, sites, ssgs)).
            andReturn(null); // i believe this indicates a mostly harmless design flaw    

        replayMocks();

        
        //OUT
        externalDataBasket  = new ExternalDataBasket();
        List<SeriesSearchResult> seriesSearchResultList = externalDataBasket.getSeriesDTOList(patientIds, 
                                                                                              studyIds, 
                                                                                              null, 
                                                                                              authorizationManagerMock);
        Assert.assertEquals(seriesSearchResultList.size() ,NUM_SERIES_IN_PATIENT + NUM_SERIES_IN_STUDY - NUM_OVERLAPPING_SERIES);
        
     }


     @Test
     public void testGetSeriesDTOFromPatientAndSeries() throws Exception
     {
         List<String> patientIds = new ArrayList<String>();
         patientIds.add("1.3.6.1.4.1.9328.50.1.0003");
         final int NUM_SERIES_IN_PATIENT = 21;

         List<String> seriesIds = new ArrayList<String>();
         seriesIds.add("19");         
         seriesIds.add("20");
         seriesIds.add("21");
         seriesIds.add("22");
         seriesIds.add("23");
                  
         final int NUM_OVERLAPPING_SERIES = 3;
         
              
         //////expectations////////////
         expect(generalSeriesDAOMock.findSeriesByPatientId(patientIds, sites, ssgs)).
             andReturn(createSeriesDTOResponse(NUM_SERIES_IN_PATIENT));        
         expect(generalSeriesDAOMock.findSeriesByStudyInstanceUid(null, sites, ssgs)).
            andReturn(null); // i believe this indicates a mostly harmless design flaw    
         expect(generalSeriesDAOMock.findSeriesBySeriesInstanceUID(seriesIds, sites, ssgs)).
            andReturn(createSeriesDTOResponse(NUM_SERIES_IN_PATIENT-NUM_OVERLAPPING_SERIES,
                                              NUM_SERIES_IN_PATIENT-NUM_OVERLAPPING_SERIES+seriesIds.size()));    

         replayMocks();

         
         //OUT
         externalDataBasket  = new ExternalDataBasket();
         
         List<SeriesSearchResult> seriesSearchResultList = externalDataBasket.getSeriesDTOList(patientIds, 
                                                                                               null,
                                                                                               seriesIds,
                                                                                               authorizationManagerMock);
         Assert.assertEquals(seriesSearchResultList.size() ,NUM_SERIES_IN_PATIENT + seriesIds.size() - NUM_OVERLAPPING_SERIES);
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
            
        //////expectations////////////
        expect(generalSeriesDAOMock.findSeriesByPatientId(null, sites, ssgs)).
            andReturn(null); // i believe this indicates a mostly harmless design flaw        
        expect(generalSeriesDAOMock.findSeriesByStudyInstanceUid(studyIds, sites, ssgs)).
            andReturn(createSeriesDTOResponse(NUM_SERIES_IN_STUDY));    
        expect(generalSeriesDAOMock.findSeriesBySeriesInstanceUID(seriesIds, sites, ssgs)).
           andReturn(createSeriesDTOResponse(NUM_SERIES_IN_STUDY-1,
                                             NUM_SERIES_IN_STUDY-1+seriesIds.size()));    
         
        replayMocks();

         
        //OUT
        externalDataBasket  = new ExternalDataBasket();
          
        List<SeriesSearchResult> seriesSearchResultList = externalDataBasket.getSeriesDTOList(null, studyIds, seriesIds, authorizationManagerMock);
        Assert.assertEquals(seriesSearchResultList.size(), NUM_SERIES_IN_STUDY + seriesIds.size() - 1);
    }
    

    @Test
    public void testGetSeriesDTOFromPatientAndStudyAndSeries() throws Exception
    {
        List<String> patientIds = new ArrayList<String>();
        patientIds.add("1.3.6.1.4.1.9328.50.1.0003");
        final int NUM_SERIES_IN_PATIENT = 21; //0..20

        List<String> studyIds = new ArrayList<String>();
        studyIds.add("1.3.6.1.4.1.9328.50.1.14021");
        studyIds.add("1.3.6.1.4.1.9328.50.1.1149"); //20..22
        final int NUM_SERIES_IN_STUDY = 3;

        List<String> seriesIds = new ArrayList<String>();
        seriesIds.add("15"); 
        seriesIds.add("21");
        seriesIds.add("23");
        seriesIds.add("24");        

        final int PATIENT_STUDY_OVERLAP = 1;
        final int PATIENT_SERIES_OVERLAP = 1;
        final int STUDY_SERIES_OVERLAP = 1;
        final int TOTAL_OVERLAP = PATIENT_STUDY_OVERLAP + PATIENT_SERIES_OVERLAP + STUDY_SERIES_OVERLAP;
        
        final int TOTAL_NUM_SERIES = NUM_SERIES_IN_PATIENT + NUM_SERIES_IN_STUDY + 4 - TOTAL_OVERLAP;
        
        //////expectations////////////
        expect(generalSeriesDAOMock.findSeriesByPatientId(patientIds, sites, ssgs)).
            andReturn(createSeriesDTOResponse(NUM_SERIES_IN_PATIENT));        
        expect(generalSeriesDAOMock.findSeriesByStudyInstanceUid(studyIds, sites, ssgs)).
            andReturn(createSeriesDTOResponse(NUM_SERIES_IN_PATIENT-1, NUM_SERIES_IN_PATIENT-1+NUM_SERIES_IN_STUDY));    
        expect(generalSeriesDAOMock.findSeriesBySeriesInstanceUID(seriesIds, sites, ssgs)).
            andReturn(createSeriesDTOResponse(new int[]{15,21,23,24}));    
         
        replayMocks();

         
        //OUT
        externalDataBasket  = new ExternalDataBasket();        
        List<SeriesSearchResult> seriesSearchResultList = externalDataBasket.getSeriesDTOList(patientIds, 
        		                                                                              studyIds,
        		                                                                              seriesIds,
        		                                                                              authorizationManagerMock);
        
        Assert.assertEquals(seriesSearchResultList.size(), TOTAL_NUM_SERIES);
    }
    
    /*
     * @see TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        mockStatic(SpringApplicationContext.class);
        mockStatic(LocalNode.class);        
        generalSeriesDAOMock = createMock(GeneralSeriesDAO.class);
        authorizationManagerMock = createMock(AuthorizationManager.class); 
        
        
        sites = new ArrayList<SiteData>();
        ssgs = new ArrayList<String>();   
        
        expect(SpringApplicationContext.getBean("generalSeriesDAO")).
            andReturn(generalSeriesDAOMock);
        expect(LocalNode.getLocalNode()).
            andReturn(createLocalNode()).anyTimes();
        
        expect(authorizationManagerMock.getAuthorizedSites()).
            andReturn(sites).anyTimes();  // i believe this indicates a mostly harmless design flaw
        expect(authorizationManagerMock.getAuthorizedSeriesSecurityGroups()).
            andReturn(ssgs).anyTimes();  // i believe this indicates a mostly harmless design flaw        
    }

    
    @After
    public void tearDown() throws Exception {        
    	verifyMocks();
    }
    
    /////////////////////////////////PRIVATE///////////////////////////////////////////////
    
    private GeneralSeriesDAO generalSeriesDAOMock;
    private AuthorizationManager authorizationManagerMock;
    private ExternalDataBasket externalDataBasket;
    private List<SiteData> sites = new ArrayList<SiteData>();
    private List<String> ssgs = new ArrayList<String>();    
    
    private void replayMocks() {
        //replay the mock
        replay(LocalNode.class);
        replay(SpringApplicationContext.class);
        replay(authorizationManagerMock, AuthorizationManager.class);
        replay(generalSeriesDAOMock, GeneralSeriesDAO.class);        
        //replay(ExternalDataBasket.class);    	
    }
    
    private void verifyMocks() {
        //verify the mock
        verify(LocalNode.class);
        verify(SpringApplicationContext.class);
        verify(AuthorizationManager.class);        
        verify(GeneralSeriesDAO.class);
        verify(ExternalDataBasket.class);     	
    }
    
    private static List<SeriesDTO> createSeriesDTOResponse(int[] ids) {
        List<SeriesDTO> response = new ArrayList<SeriesDTO>();
        for(int i=0;i<ids.length;i++) {
            SeriesDTO dto = new SeriesDTO();
            dto.setSeriesId(Integer.toString(ids[i]));
            response.add(dto);
        }
        return response;
    }
    
    private static List<SeriesDTO> createSeriesDTOResponse(int start, int end) {
        List<SeriesDTO> response = new ArrayList<SeriesDTO>();
        for(int i=start;i<end;i++) {
            SeriesDTO dto = new SeriesDTO();
            dto.setSeriesId(Integer.toString(i));
            response.add(dto);
        }
        return response;
    }
    
    private static List<SeriesDTO> createSeriesDTOResponse(int end) {
        return createSeriesDTOResponse(0,end);
    }
    
    private static NBIANode createLocalNode() {
        NBIANode node = new NBIANode(true, "displayName", "http://foo.com");
        return node;
    }
}
