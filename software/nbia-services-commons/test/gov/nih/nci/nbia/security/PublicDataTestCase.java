/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.security;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.dao.ImageDAO;
import gov.nih.nci.nbia.dao.PatientDAO;
import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.nbia.dto.PatientDTO;
import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;

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
@SuppressStaticInitializationFor({"gov.nih.nci.nbia.security.NCIASecurityManager$RoleType"})
@PrepareForTest({PublicData.class,  
                 SpringApplicationContext.class}) 
public class PublicDataTestCase {

	@Test
	public void testCheckPublicDataPatient() {
		PatientDTO publicPatientDTO = new PatientDTO();
        publicPatientDTO.setProject("public_project");
        publicPatientDTO.setSiteName("public_sitename");

        PatientDTO privatePatientDTO = new PatientDTO();
        privatePatientDTO.setProject("private_project");
        privatePatientDTO.setSiteName("private_sitename");
        
        PatientDAO patientDAOMock = createMock(PatientDAO.class);
        
        expect(SpringApplicationContext.getBean("patientDAO")).
            andReturn(patientDAOMock).anyTimes();
        expect(authorizationManagerMock.getAuthorizedSites()).
            andReturn(publicSites).times(2);
        expect(patientDAOMock.getPatientById(public_patientPkId)).
            andReturn(publicPatientDTO);  
        expect(patientDAOMock.getPatientById(nonpublic_patientPkID)).
            andReturn(privatePatientDTO);         
        
        replay(PatientDAO.class, patientDAOMock);
        replayMock();
	    
	    //OUT
		PublicData pd = new PublicData();
		pd.setAuthorizationManager(authorizationManagerMock);
		
		boolean isPatientPublic = pd.checkPublicPatient(new Integer(nonpublic_patientPkID));
		
		Assert.assertFalse(isPatientPublic);
		
		isPatientPublic = pd.checkPublicPatient(new Integer(public_patientPkId));
		
		Assert.assertTrue(isPatientPublic);
		
        verify(PatientDAO.class, patientDAOMock);
	}
	
	@Test
	public void testCheckPublicDataSeries() {
        GeneralSeriesDAO generalSeriesDAOMock = createMock(GeneralSeriesDAO.class);
        
        SeriesDTO publicSeriesDTO = new SeriesDTO();
        publicSeriesDTO.setProject("public_project");
        publicSeriesDTO.setDataProvenanceSiteName("public_sitename");

        SeriesDTO privateSeriesDTO = new SeriesDTO();
        privateSeriesDTO.setProject("private_project");
        privateSeriesDTO.setDataProvenanceSiteName("private_sitename");
        
        expect(SpringApplicationContext.getBean("generalSeriesDAO")).
            andReturn(generalSeriesDAOMock).anyTimes();
        expect(authorizationManagerMock.getAuthorizedSites()).
            andReturn(publicSites).times(2);
        expect(generalSeriesDAOMock.getGeneralSeriesByPKid(public_seriesPkId)).
            andReturn(publicSeriesDTO);  
        expect(generalSeriesDAOMock.getGeneralSeriesByPKid(nonpublic_seriesPkId)).
            andReturn(privateSeriesDTO);         
        
        replay(GeneralSeriesDAO.class, generalSeriesDAOMock);
        replayMock();
		
		boolean isSeriesPublic = pd.checkPublicSeries(new Integer(nonpublic_seriesPkId));
		
		Assert.assertFalse(isSeriesPublic);
		
		isSeriesPublic = pd.checkPublicSeries(new Integer(public_seriesPkId));
		
		Assert.assertTrue(isSeriesPublic);
		
        verify(GeneralSeriesDAO.class, generalSeriesDAOMock);
	}	

	@Test
	public void testCheckPublicDataImage() {
        ImageDAO imageDAOMock = createMock(ImageDAO.class);
        
        ImageDTO publicImageDTO = new ImageDTO();
        publicImageDTO.setProject("public_project");
        publicImageDTO.setSiteName("public_sitename");

        ImageDTO privateImageDTO = new ImageDTO();
        privateImageDTO.setProject("private_project");
        privateImageDTO.setSiteName("private_sitename");
        
        expect(SpringApplicationContext.getBean("imageDAO")).
            andReturn(imageDAOMock).times(2);
        expect(authorizationManagerMock.getAuthorizedSites()).
            andReturn(publicSites).times(2);
        expect(imageDAOMock.getGeneralImageByImagePkId(public_imagePkId)).
            andReturn(publicImageDTO);  
        expect(imageDAOMock.getGeneralImageByImagePkId(nonpublic_imagePkId)).
            andReturn(privateImageDTO);         
        
        replay(ImageDAO.class, imageDAOMock);
        replayMock();
		
		boolean isImagePublic = pd.checkPublicImage(new Integer(nonpublic_imagePkId));
		
		Assert.assertFalse(isImagePublic);
		
		isImagePublic = pd.checkPublicImage(new Integer(public_imagePkId));
		
		Assert.assertTrue(isImagePublic);
		
        verify(ImageDAO.class, imageDAOMock);
	}	
	
	@Before
	public void setUp() {
        mockStatic(SpringApplicationContext.class);
        authorizationManagerMock = createMock(AuthorizationManager.class);

        SiteData sd0 = new SiteData("public_project//public_sitename");
        
        publicSites = new ArrayList<SiteData>();
        publicSites.add(sd0);
        
		pd = new PublicData();
		pd.setAuthorizationManager(authorizationManagerMock);        
	}
	
	@After
	public void tearDown() {
		verifyMock();
	}
	
	//////////////////////////////////////PRIVATE////////////////////////////////////////////////
	
	private int nonpublic_patientPkID = 198;
	private int nonpublic_seriesPkId = 3146;
	private int nonpublic_imagePkId = 591060;
	
	private int public_patientPkId = 8454144;
	private int public_seriesPkId = 8519680;
	private int public_imagePkId = 8650859;
	
	private PublicData pd;
	
	private List<SiteData> publicSites;
	
	private AuthorizationManager authorizationManagerMock;

	private void verifyMock() {
        verify(SpringApplicationContext.class);
        verify(AuthorizationManager.class, authorizationManagerMock);	
	}
	private void replayMock() {
        replay(SpringApplicationContext.class);
	    replay(AuthorizationManager.class, authorizationManagerMock);
	}	
}
