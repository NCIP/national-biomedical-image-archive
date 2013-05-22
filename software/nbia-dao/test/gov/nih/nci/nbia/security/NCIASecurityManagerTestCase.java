/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.security;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.HashSet;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class NCIASecurityManagerTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testGetSecurityMapForPublicRole() throws Exception {
		//dbUnit MUST run before this.... so can't autowire
		NCIASecurityManager nciaSecurityManager = (NCIASecurityManager)SpringApplicationContext.getBean("nciaSecurityManager");

		Set<String> foundProjects = new HashSet<String>();
		
		Set<TableProtectionElement> protectionElements = nciaSecurityManager.getSecurityMapForPublicRole();
		for(TableProtectionElement pe : protectionElements) {
			String projectName = pe.getAttributeValue();
		    System.out.println("found:"+projectName);		
			foundProjects.add(projectName);
		}

		Set<String> projectsToFind = getPublicProjectsToFind();
		for(String foundProject : foundProjects) {
			if(!projectsToFind.contains(foundProject)) {
				Assert.fail("found project shouldnt have:"+foundProject);
			}
		}
		for(String projectToFind : projectsToFind) {
			if(!foundProjects.contains(projectToFind)) {
				Assert.fail("didnt find project should have:"+projectToFind);
			}
		}		
		
	}


	@Test	
	public void testGetSecurityMapString() throws Exception {
		//dbUnit MUST run before this.... so can't autowire
		NCIASecurityManager nciaSecurityManager = (NCIASecurityManager)SpringApplicationContext.getBean("nciaSecurityManager");

		Set<String> foundProjects = new HashSet<String>();
		
		String userId = knownExistingUserId;
		Set<TableProtectionElement> protectionElements = nciaSecurityManager.getSecurityMap(userId);
		for(TableProtectionElement pe : protectionElements) {
			String projectName = pe.getAttributeValue();
				
			foundProjects.add(projectName);
		}

		Set<String> projectsToFind = getProjectsToFind();
		for(String foundProject : foundProjects) {
			if(!projectsToFind.contains(foundProject)) {
				Assert.fail("found project shouldnt have:"+foundProject);
			}
		}
		for(String projectToFind : projectsToFind) {
			if(!foundProjects.contains(projectToFind)) {
				Assert.fail("didnt find project should have:"+projectToFind);
			}
		}		
		
		try {
		    protectionElements = nciaSecurityManager.getSecurityMap("bogusCrap");
		    Assert.fail("should have thrown an exception");
		}
		catch(Throwable cne) {
			//want to be here
		}
	}


	@Test	
	public void testGetUserId() throws Exception {
		//dbUnit MUST run before this.... so can't autowire
		NCIASecurityManager nciaSecurityManager = (NCIASecurityManager)SpringApplicationContext.getBean("nciaSecurityManager");

		
		String userId = nciaSecurityManager.getUserId(knownExistingUserLogin);
		System.out.println("userId:"+userId);
		Assert.assertTrue(knownExistingUserLogin+" user is incorrect",
				   userId.equals(knownExistingUserId));

		try {
			userId = nciaSecurityManager.getUserId("fakeBogusUser");
			Assert.fail("fakeBogusUser is found?");
		}
		catch(Throwable t) {
		}
	}


	@Test	
	public void testGetUserEmail() throws Exception {
		//dbUnit MUST run before this.... so can't autowire
		NCIASecurityManager nciaSecurityManager = (NCIASecurityManager)SpringApplicationContext.getBean("nciaSecurityManager");

		String email = nciaSecurityManager.getUserEmail(knownExistingUserLogin);
		System.out.println("email:"+email);
		Assert.assertTrue(knownExistingUserLogin+" email is incorrect",
				          email.equals(knownExistingUserEmail));

		try {
			email = nciaSecurityManager.getUserEmail("fakeBogusUser");
			Assert.fail("fakeBogusUser is found?");
		}
		catch(Throwable t) {
		}
	}

    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
        return TEST_DB_FLAT_FILE;
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/csm_user_kascice2.xml";

	/**
	 * these fields need to match up for a given user (login, id, email)
	 */
	private String knownExistingUserLogin = "kascice";
	private String knownExistingUserId = "4097";
	private String knownExistingUserEmail = "kascice@mail.nih.gov";
	
	private static Set<String> getPublicProjectsToFind() {
		Set<String> projectsToFind = new HashSet<String>();

		projectsToFind.add("NCIA.RIDER//MDACC"); 
		projectsToFind.add("NCIA.RIDER//MSKCC"); 
		projectsToFind.add("NCIA.RIDER"); 
		projectsToFind.add("NCIA.LIDC"); 
		projectsToFind.add("NCIA.LIDC//LIDC"); 
		projectsToFind.add("NCIA.CT Colonography//ACRIN"); 
		projectsToFind.add("NCIA.CT Colonography"); 
		projectsToFind.add("NCIA.NCRI"); 
		projectsToFind.add("NCIA.NCRI//NHS"); 
		projectsToFind.add("NCIA.Virtual Colonoscopy"); 
		projectsToFind.add("NCIA.Virtual Colonoscopy//WRAMC"); 
		projectsToFind.add("NCIA.Virtual Colonoscopy//SanDiego"); 

		projectsToFind.add("NCIA.6668-TRIAL-7831552"); 
		projectsToFind.add("NCIA.NCIA.6668-TRIAL-7831552//ACRIN"); 
		projectsToFind.add("NCIA.RIDER//Roswell"); 
		  
		projectsToFind.add("NCIA.Phantom//WashU-IRL"); 
		projectsToFind.add("NCIA.Phantom"); 
		projectsToFind.add("NCIA.RoswellStrong//ROSWELL"); 
		projectsToFind.add("NCIA.RoswellStrong"); 
		projectsToFind.add("NCIA.RoswellStrong//Strong"); 
		projectsToFind.add("NCIA.RIDER//WashU-IRL"); 
		projectsToFind.add("NCIA.DCEMRI//MGH"); 
		projectsToFind.add("NCIA.DCEMRI"); 
		projectsToFind.add("NCIA.LiverCT"); 
		projectsToFind.add("NCIA.LiverCT//GeorgetownU");
		projectsToFind.add("NCIA.Biochange08"); 
		projectsToFind.add("NCIA.Phantom-FDA"); 
		projectsToFind.add("NCIA.Biochange08//Cornell");
		projectsToFind.add("NCIA.Phantom-FDA//FDA"); 
		    
		return projectsToFind;
	}
	
	private static Set<String> getProjectsToFind() {
		Set<String> projectsToFind = new HashSet<String>();

		projectsToFind.add("NCIA.RIDER//MDACC"); 
		projectsToFind.add("NCIA.RIDER//MSKCC"); 
		projectsToFind.add("NCIA.RIDER"); 
		projectsToFind.add("NCIA.LIDC"); 
		projectsToFind.add("NCIA.LIDC//LIDC"); 
		projectsToFind.add("NCIA.IDRI"); 
		projectsToFind.add("NCIA.IDRI//IDRI"); 
		projectsToFind.add("NCIA.IDRICXR"); 
		projectsToFind.add("NCIA.IDRI-CT"); 
		projectsToFind.add("NCIA.ISPY//ACRIN"); 
		projectsToFind.add("NCIA.ISPY"); 
		projectsToFind.add("NCIA.Head-Neck Cetuximab//ITC-ACRIN"); 
		projectsToFind.add("NCIA.Head-Neck Cetuximab"); 
		projectsToFind.add("NCIA.ISPY//NCICB"); 
		projectsToFind.add("NCIA.Vasari"); 
		//direct user through pg9
		projectsToFind.add("NCIA.GBM Avastin"); 
		projectsToFind.add("NCIA.GBM Avastin//Clinical Center"); 
		projectsToFind.add("NCIA.CT Colonography//ACRIN"); 
		projectsToFind.add("NCIA.CT Colonography"); 
		projectsToFind.add("NCIA.NCRI"); 
		projectsToFind.add("NCIA.NCRI//NHS"); 
		projectsToFind.add("NCIA.Virtual Colonoscopy"); 
		projectsToFind.add("NCIA.Virtual Colonoscopy//WRAMC"); 
		projectsToFind.add("NCIA.Virtual Colonoscopy//SanDiego"); 
		projectsToFind.add("NCIA.IDRI//MDACC"); 
		projectsToFind.add("NCIA.IDRI//MSKCC"); 
        projectsToFind.add("NCIA.6668-TRIAL-7831552"); 
		projectsToFind.add("NCIA.NCIA.6668-TRIAL-7831552//ACRIN"); 
		projectsToFind.add("NCIA.RIDER//Roswell"); 
		projectsToFind.add("NCIA.IDRICONDUIT//MDACC"); 
		projectsToFind.add("NCIA.IDRICONDUIT"); 
		projectsToFind.add("NCIA.IDRICONDUIT//MSKCC"); 
		projectsToFind.add("NCIA.COMBIDEX-VCU"); 
		projectsToFind.add("NCIA.COMBIDEX-VCU//VCU"); 
		projectsToFind.add("NCIA.Phantom//WashU-IRL"); 
		projectsToFind.add("NCIA.Phantom"); 
		projectsToFind.add("NCIA.RoswellStrong//ROSWELL"); 
		projectsToFind.add("NCIA.RoswellStrong"); 
		projectsToFind.add("NCIA.Osteoarthritis Initiative"); 
		projectsToFind.add("NCIA.Osteoarthritis Initiative//OAI"); 
		projectsToFind.add("NCIA.RoswellStrong//Strong"); 
		projectsToFind.add("NCIA.GBM Rembrandt"); 
		projectsToFind.add("NCIA.GBM Rembrandt//Clinical Center"); 
		projectsToFind.add("NCIA.RIDER//WashU-IRL"); 
		projectsToFind.add("NCIA.DCEMRI//MGH"); 
		projectsToFind.add("NCIA.DCEMRI"); 
		projectsToFind.add("NCIA.LiverCT"); 
		projectsToFind.add("NCIA.LiverCT//GeorgetownU"); 
		projectsToFind.add("NCIA.Biochange08"); 
		projectsToFind.add("NCIA.Phantom-FDA"); 
		projectsToFind.add("NCIA.Biochange08//Cornell"); 
		projectsToFind.add("NCIA.Phantom-FDA//FDA"); 
		 		
		return projectsToFind;
	}
		
}
