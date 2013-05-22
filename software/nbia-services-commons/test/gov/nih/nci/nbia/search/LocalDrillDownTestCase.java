/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.dao.ImageDAO;
import gov.nih.nci.nbia.dao.StudyDAO;
import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.dto.StudyDTO;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.PublicData;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultEx;
import gov.nih.nci.ncia.search.NBIANode;
import gov.nih.nci.ncia.search.NameValuesPairs;
import gov.nih.nci.ncia.search.PatientSearchResultImpl;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.ncia.search.StudySearchResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"gov.nih.nci.nbia.security.NCIASecurityManager$RoleType",
	                              "gov.nih.nci.nbia.search.LocalNode"})
@PrepareForTest({LocalDrillDown.class,  
                 SpringApplicationContext.class,
                 LocalNode.class}) 
public class LocalDrillDownTestCase {

	@Test
	public void testRetrieveImagesForSeriesSeriesSearchResultWithoutPublicFilter() {

        expect(SpringApplicationContext.getBean("imageDAO")).
            andReturn(imageDAOMock);
        expect(imageDAOMock.findImagesbySeriesPkID(Collections.singletonList(102236166))).
            andReturn(constructImageDTOList());
        expect(LocalNode.getLocalNode()).
            andReturn(createLocalNode()).anyTimes();
        
        replay(SpringApplicationContext.class);
        replay(LocalNode.class);
        replay(imageDAOMock, ImageDAO.class);
        
        ///////////////////////
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setPatientPublic(false);
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());
		
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.setId(102236166);
		seriesSearchResult.setSeriesInstanceUid("1.3.6.1.4.1.9328.50.6.40560");
		ImageSearchResult[] results = localDrillDown.retrieveImagesForSeries(seriesSearchResult);

		Assert.assertTrue(results.length==429);
		boolean found = false;
		for(ImageSearchResult result : results) {
			if(result.getInstanceNumber()==1) {
				found = true;
				
				Assert.assertTrue(result.getId()==102107844);
				Assert.assertTrue(result.getSeriesInstanceUid().equals("1.3.6.1.4.1.9328.50.6.40560"));
				Assert.assertTrue(result.getSopInstanceUid().equals("1.3.6.1.4.1.9328.50.6.40563"));
				Assert.assertTrue(result.getSeriesId()==102236166);
				Assert.assertTrue(result.getSize()==526116);
				Assert.assertTrue(result.getThumbnailURL().equals("foo"));
			}		
		}
		Assert.assertTrue(found);
		
        verify(SpringApplicationContext.class);
        verify(imageDAOMock, ImageDAO.class);		
	}
	

	@Test
	public void testRetrieveImagesForSeriesSeriesSearchResultWithPublicFilter() throws Exception {
		expect(publicDataMock.checkPublicSeries(102236166)).
		    andReturn(false);
        expectNew(PublicData.class).
            andReturn(publicDataMock);        
        publicDataMock.setAuthorizationManager(authorizationManagerMock);
		
        replay(publicDataMock, PublicData.class);
        replay(authorizationManagerMock, AuthorizationManager.class);	
        
        //OUT
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setPatientPublic(true);
		localDrillDown.setAuthorizationManager(authorizationManagerMock);
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());
		
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.setId(102236166);
		seriesSearchResult.setSeriesInstanceUid("1.3.6.1.4.1.9328.50.6.40560");
		ImageSearchResult[] results = localDrillDown.retrieveImagesForSeries(seriesSearchResult);

		Assert.assertTrue(results.length==0);
		
        verify(publicDataMock, PublicData.class);
        verify(authorizationManagerMock, AuthorizationManager.class);			
	}


	@Test
	public void testRetrieveStudiesAndSeriesWithoutPublicFilter() {
		PatientSearchResultImpl patientSearchResult = new PatientSearchResultImpl();
		patientSearchResult.setId(8454144);		
		patientSearchResult.addSeriesForStudy(8486912, 8519680);
		patientSearchResult.addSeriesForStudy(8486912, 8716288);
						
        expect(SpringApplicationContext.getBean("studyDAO")).
            andReturn(studyDAOMock);
	    expect(studyDAOMock.findStudiesBySeriesId(patientSearchResult.computeListOfSeriesIds())).
	        andReturn(constructStudyDTOList());
        expect(LocalNode.getLocalNode()).
            andReturn(createLocalNode()).anyTimes();

        replay(SpringApplicationContext.class);
	    replay(StudyDAO.class, studyDAOMock);
        replay(LocalNode.class);
    
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setPatientPublic(false);
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());

		StudySearchResult[] results = localDrillDown.retrieveStudyAndSeriesForPatient(patientSearchResult);

		Assert.assertTrue(results.length==1);
		Assert.assertTrue(results[0].getId()==8486912);
		Assert.assertTrue(results[0].getStudyInstanceUid().equals("1.3.6.1.4.1.9328.50.99.1"));
		Assert.assertTrue(results[0].getDescription().equals("Anonymized"));
		
		//offset desc looks like presentation state jammed in.  maybe get rid of
		//when have more time luxury
		Assert.assertNull(results[0].getOffSetDesc());
	
		//Oracle=dd-MMM-yyyy
		//MySql=yyyy-MM-dd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(results[0].getDate());
		Assert.assertTrue(dateStr.equals("2001-01-01"));
		
		Assert.assertTrue(results[0].getSeriesList().length==2);
		
		verify(SpringApplicationContext.class);
		verify(StudyDAO.class, studyDAOMock);		
	}


	@Test
	public void testRetrieveStudiesAndSeriesWithPublicFilter() throws Exception {

		expect(publicDataMock.checkPublicPatient(8454144)).
		    andReturn(false);
        expectNew(PublicData.class).
            andReturn(publicDataMock);        
        publicDataMock.setAuthorizationManager(authorizationManagerMock);
		
        replay(publicDataMock, PublicData.class);
        replay(authorizationManagerMock, AuthorizationManager.class);	
        /////////////////////////////////////////////////
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setPatientPublic(true);
		localDrillDown.setAuthorizationManager(authorizationManagerMock);
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());
		
		PatientSearchResultImpl patientSearchResult = new PatientSearchResultImpl();
		patientSearchResult.setId(8454144);		
		patientSearchResult.addSeriesForStudy(8486912, 8519680);
		patientSearchResult.addSeriesForStudy(8486912, 8716288);
		
		StudySearchResult[] results = localDrillDown.retrieveStudyAndSeriesForPatient(patientSearchResult);

		Assert.assertTrue(results.length==0);
		
        verify(publicDataMock, PublicData.class);
        verify(authorizationManagerMock, AuthorizationManager.class);			
	}
	

	@Test
	public void testRetrieveImagesForSeriesId() throws Exception {

        expect(SpringApplicationContext.getBean("imageDAO")).
            andReturn(imageDAOMock);
        expect(imageDAOMock.findImagesbySeriesPkID(Collections.singletonList(102236166))).
            andReturn(constructImageDTOList());
        expect(LocalNode.getLocalNode()).
            andReturn(createLocalNode()).anyTimes();
        
        replay(SpringApplicationContext.class);
        replay(LocalNode.class);
        replay(imageDAOMock, ImageDAO.class);
        
        /////////////////////////////////////////////////////////////////////
        
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());

		ImageSearchResult[] results = localDrillDown.retrieveImagesForSeries(102236166);
		Assert.assertTrue(results.length==429);	
	}
	

	@Test
	public void testRetrieveImagesForSeriesIdForMultiFrame() throws Exception {

        expect(SpringApplicationContext.getBean("imageDAO")).
            andReturn(imageDAOMock);
        expect(imageDAOMock.findImagesbySeriesPkID(Collections.singletonList(102236166))).
            andReturn(constructImageDTOList());
        expect(LocalNode.getLocalNode()).
            andReturn(createLocalNode()).anyTimes();
        
        replay(SpringApplicationContext.class);
        replay(LocalNode.class);
        replay(imageDAOMock, ImageDAO.class);
        
        /////////////////////////////////////////////////////////////////////
        
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());

		ImageSearchResultEx[] results = localDrillDown.retrieveImagesForSeriesEx(102236166);
		Assert.assertTrue(results.length==429);	
		
		NameValuesPairs nvp = results[0].getNameValuesPairs();
		Assert.assertTrue(nvp.getName().equals("USFrameNum"));		
		Assert.assertTrue(nvp.getValues(0).equals("666"));
	}
	

	@Test
	public void testRetrieveImagesForSeriesBySeriesInstanceUid() throws Exception {

        expect(SpringApplicationContext.getBean("imageDAO")).
            andReturn(imageDAOMock);
        expect(imageDAOMock.findImagesbySeriesInstandUid(Collections.singletonList("1.3.6.1.4.1.9328.50.99.456"))).
            andReturn(constructImageDTOList());
        expect(LocalNode.getLocalNode()).
            andReturn(createLocalNode()).anyTimes();
        
        replay(SpringApplicationContext.class);
        replay(LocalNode.class);
        replay(imageDAOMock, ImageDAO.class);
        
        /////////////////////////////////////////////////////////////////////
        
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());

		ImageSearchResult[] results = localDrillDown.retrieveImagesForSeries("1.3.6.1.4.1.9328.50.99.456");
		Assert.assertTrue(results.length==429);		
	}
	

	@Test
	public void testRetrieveSeriesByInstanceUid() {

        expect(SpringApplicationContext.getBean("generalSeriesDAO")).
            andReturn(generalSeriesDAOMock);
        expect(generalSeriesDAOMock.findSeriesBySeriesInstanceUID(Collections.singletonList("1.3.6.1.4.1.9328.50.99.456"))).
            andReturn(constructSeriesDTOList());
        expect(LocalNode.getLocalNode()).
            andReturn(createLocalNode()).anyTimes();
        
        replay(SpringApplicationContext.class);
        replay(LocalNode.class);
        replay(generalSeriesDAOMock, ImageDAO.class);
        
        /////////////////////////////////////////////////////////////////////
        	
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());

		SeriesSearchResult result = localDrillDown.retrieveSeries("1.3.6.1.4.1.9328.50.99.456");
		Assert.assertNotNull(result);
		Assert.assertTrue(result.getId()==8716288);
		Assert.assertTrue(result.getSeriesInstanceUid().equals("1.3.6.1.4.1.9328.50.99.456"));
		Assert.assertTrue(result.getDescription().equals("desc"));
		Assert.assertTrue(result.getManufacturer().equals("CBIIT"));
		Assert.assertTrue(result.getModality().equals("CT"));	
		
		Assert.assertTrue(result.getPatientId().equals("WRAMC VC-001M"));		
		Assert.assertTrue(result.getProject().equals("Virtual Colonoscopy"));		
		Assert.assertTrue(result.getSeriesNumber().equals("2"));		
		Assert.assertTrue(result.getStudyInstanceUid().equals("1.3.6.1.4.1.9328.50.99.1"));		
		Assert.assertTrue(result.getStudyId()==8486912);		
	}

	
	@Test
	public void testRetrieveSeriesById() {

        expect(SpringApplicationContext.getBean("generalSeriesDAO")).
            andReturn(generalSeriesDAOMock);
        expect(generalSeriesDAOMock.findSeriesBySeriesPkId(Collections.singletonList(8716288))).
            andReturn(constructSeriesDTOList());
        expect(LocalNode.getLocalNode()).
            andReturn(createLocalNode()).anyTimes();
        
        replay(SpringApplicationContext.class);
        replay(LocalNode.class);
        replay(generalSeriesDAOMock, ImageDAO.class);
        
        /////////////////////////////////////////////////////////////////////
        
		LocalDrillDown localDrillDown = new LocalDrillDown();
		localDrillDown.setThumbnailURLResolver(new FakeThumbnailURLResolver());

		SeriesSearchResult result = localDrillDown.retrieveSeries(8716288);
		Assert.assertTrue(result.getId()==8716288);
	}

	
	@Before
	public void setUp() {
        imageDAOMock = createMock(ImageDAO.class);
		mockStatic(SpringApplicationContext.class);
        mockStatic(LocalNode.class);  
        
		publicDataMock = createMock(PublicData.class);
        authorizationManagerMock = createMock(AuthorizationManager.class);
		studyDAOMock = createMock(StudyDAO.class);
        generalSeriesDAOMock = createMock(GeneralSeriesDAO.class);
	}
	
	
	public static class FakeThumbnailURLResolver implements ThumbnailURLResolver {
		/**
		 * For a given local DICOM image, return a URL (String) to get to a thumbnail of it.	 
		 */	
		public String resolveThumbnailUrl(ImageDTO imageDto) {
			return "foo";
		}
	}	
	
    ////////////////////////////////////PRIVATE/////////////////////////////////

	private ImageDAO imageDAOMock;
	
	private PublicData publicDataMock;
	
	private AuthorizationManager authorizationManagerMock; 
	
	private StudyDAO studyDAOMock;
	
	private GeneralSeriesDAO generalSeriesDAOMock;

    private static NBIANode createLocalNode() {
        NBIANode node = new NBIANode(true, "displayName", "http://foo.com");
        return node;
    }
    
	private List<StudyDTO> constructStudyDTOList() {
		List<StudyDTO> list = new ArrayList<StudyDTO>();
		
		List<SeriesDTO> seriesList = new ArrayList<SeriesDTO>();
		
		SeriesDTO seriesDTO0 = new SeriesDTO();
		SeriesDTO seriesDTO1 = new SeriesDTO();
		seriesList.add(seriesDTO0);
		seriesList.add(seriesDTO1);
		
		StudyDTO studyDTO0 = new StudyDTO();
		studyDTO0.setId(8486912);
		studyDTO0.setStudyId("1.3.6.1.4.1.9328.50.99.1");
        studyDTO0.setDescription("Anonymized");
        studyDTO0.setDate(new Date(101,0,1));
        
        studyDTO0.setSeriesList(seriesList);
        list.add(studyDTO0);
		return list;
	}    
	
	
	private List<ImageDTO> constructImageDTOList() {
		List<ImageDTO> imageDtoList = new ArrayList<ImageDTO>();
		ImageDTO imageDTO = new ImageDTO();
		imageDTO.setImagePkId(102107844);
		imageDTO.setInstanceNumber(1);
		imageDTO.setSeriesInstanceUid("1.3.6.1.4.1.9328.50.6.40560");
		imageDTO.setSopInstanceUid("1.3.6.1.4.1.9328.50.6.40563");
		imageDTO.setSeriesPkId(102236166);
		imageDTO.setSize(526116L);
		imageDTO.setFrameNum(666);
		imageDtoList.add(imageDTO);
		for(int i=2;i<429+1;i++) {
			ImageDTO newDTO = new ImageDTO();
			newDTO.setInstanceNumber(i);
			imageDtoList.add(newDTO);
		}
		return imageDtoList;
	}
	
	private List<SeriesDTO> constructSeriesDTOList() {
		List<SeriesDTO> seriesDtoList = new ArrayList<SeriesDTO>();
		SeriesDTO seriesDTO = new SeriesDTO();
		seriesDTO.setSeriesUID("1.3.6.1.4.1.9328.50.99.456");
		seriesDTO.setSeriesPkId(8716288);
		seriesDTO.setStudyId("1.3.6.1.4.1.9328.50.99.1");
		seriesDTO.setStudyPkId(8486912);
		seriesDTO.setNumberImages(666);
		seriesDTO.setSeriesNumber("2");
		seriesDTO.setModality("CT");
		seriesDTO.setProject("Virtual Colonoscopy");
		seriesDTO.setAnnotationsFlag(false);
		seriesDTO.setPatientId("WRAMC VC-001M");
		seriesDTO.setAnnotationsSize(0L);
		seriesDTO.setDescription("desc");
		seriesDTO.setTotalSizeForAllImagesInSeries(5150L);
		seriesDTO.setManufacturer("CBIIT");
		
		seriesDtoList.add(seriesDTO);
		
		return seriesDtoList;
	}  	
}
