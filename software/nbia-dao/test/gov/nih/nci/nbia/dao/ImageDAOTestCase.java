/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;
import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.nbia.dto.ImageSecurityDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-hibernate-testContext.xml"})
public class ImageDAOTestCase extends AbstractDbUnitTestForJunit4 {
	@Test
	public void testFindKeyedImagesBySeriesPkId() throws Exception {
		final int[] SERIES_PK_ID2 = {6991};
		Map<Integer, List<ImageDTO>> map = imageDAO.findKeyedImagesBySeriesPkId(createSeriesIds(SERIES_PK_ID2));		
		System.out.println("size:"+map.size());
		Assert.assertTrue(map.size()==1);		
		Assert.assertTrue(map.get(6991).size()==139);		
	}
	
	@Test	
	public void testFindImagePath() {
		String imagePath = imageDAO.findImagePath(1381436);
		Assert.assertEquals(imagePath,"/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-57732.dcm");
		
		try {
			imagePath = imageDAO.findImagePath(6666666);
			Assert.fail(imagePath);
		}
		catch(Throwable t) {
			//doing good
		}
	}

	@Test	
	public void testFindImageSecurity() {
		ImageSecurityDTO imageSecurityDTO = imageDAO.findImageSecurity("1.3.6.1.4.1.9328.50.3.193");
		Assert.assertEquals(imageSecurityDTO.getFileName(),"/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-57732.dcm");
		Assert.assertEquals(imageSecurityDTO.getProject(),"LIDC");
		Assert.assertTrue(imageSecurityDTO.getSeriesVisibility());
		Assert.assertEquals(imageSecurityDTO.getSite(), "LIDC");
		Assert.assertEquals(imageSecurityDTO.getSOPInstanceUID(), "1.3.6.1.4.1.9328.50.3.193");
		Assert.assertTrue(imageSecurityDTO.getFrameNum()== 43);
		imageSecurityDTO = imageDAO.findImageSecurity("garbage");
		Assert.assertNull(imageSecurityDTO);	
	}
	
	@Test	
	public void testfindImageSecurityBySeriesInstanceUID() {
		List<ImageSecurityDTO> imageSecurityDTOs = (List<ImageSecurityDTO>)imageDAO.findImageSecurityBySeriesInstanceUID("1.3.6.1.4.1.9328.50.3.195");
		Assert.assertEquals(imageSecurityDTOs.get(0).getFileName(),"/usr/local/tomcat-5.5.9/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.3.194/https-57732.dcm");
		Assert.assertEquals(imageSecurityDTOs.get(0).getProject(),"LIDC-TRIAL-6");
		Assert.assertTrue(imageSecurityDTOs.get(0).getSeriesVisibility());
		Assert.assertEquals(imageSecurityDTOs.get(0).getSite(), "LIDC");
		Assert.assertEquals(imageSecurityDTOs.get(0).getSOPInstanceUID(), "1.3.6.1.4.1.9328.50.3.193");
		Assert.assertTrue(imageSecurityDTOs.get(0).getFrameNum()== 43);

	}
	
//	public void testfindDistinctVisibleConvolutionKernels() throws Exception {
//		Collection<String> kernels = imageDAO.findDistinctVisibleConvolutionKernels();
//		assertTrue(kernels.contains("BONE"));
//		assertTrue(kernels.contains("BONE1"));
//		assertTrue(kernels.contains("BONE2"));
//		assertTrue(kernels.contains("BONE3"));
//		assertTrue(kernels.size()==4);
//		
//	}
	
	@Test	
	public void testFindNoImagesBySeriesPkId() throws Exception {
		final int[] SERIES_PK_ID = {-1};

		//hqlProcess will sit on ALL exceptions????????????
		List<ImageDTO> resultSets = imageDAO.findImagesbySeriesPkID(createSeriesIds(SERIES_PK_ID));
		Assert.assertTrue("should have no results",
				    resultSets.size()==0);
	}


	/**
	 * No image criteria and just one series
	 */
	@Test	
	public void testFindImagesBySeriesPkId() throws Exception {
		final int[] SERIES_PK_ID = {6990};
		//hqlProcess will sit on ALL exceptions????????????
		List<ImageDTO> resultSets = imageDAO.findImagesbySeriesPkID(createSeriesIds(SERIES_PK_ID));
		System.out.println("resultSets len:"+resultSets.size());
		Assert.assertTrue(resultSets.size()==128);

		for(ImageDTO resultSet : resultSets) {
			Assert.assertEquals(resultSet.getSeriesPkId(),
					     (Integer)SERIES_PK_ID[0]);
			Assert.assertEquals(resultSet.getSeriesInstanceUid(),
					     "1.3.6.1.4.1.9328.50.3.69");
			Assert.assertTrue(resultSet.getSopInstanceUid().startsWith("1.3.6.1.4.1.9328.50.3"));

		}
	}

	@Test
	public void testMutlipleSeries() throws Exception {
		final int[] SERIES_PK_ID1 = {6990};

		List<ImageDTO> resultSets = imageDAO.findImagesbySeriesPkID(createSeriesIds(SERIES_PK_ID1));
		Assert.assertTrue(resultSets.size()==128);

		final int[] SERIES_PK_ID2 = {6991};
		resultSets = imageDAO.findImagesbySeriesPkID(createSeriesIds(SERIES_PK_ID2));		
		System.out.println("size:"+resultSets.size());
		Assert.assertTrue(resultSets.size()==139);

		final int[] SERIES_PK_ID3 = {6990, 6991};
		resultSets = imageDAO.findImagesbySeriesPkID(createSeriesIds(SERIES_PK_ID3));		
		Assert.assertTrue(resultSets.size()==(139+128));
	}


    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }

    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/patient_1044.xml";

    @Autowired
	private ImageDAO imageDAO;

	private static List<Integer> createSeriesIds(int[] seriesPkIds) {
		List<Integer> seriesIds = new ArrayList<Integer>();
		for(int seriesPkId : seriesPkIds) {
			seriesIds.add(seriesPkId);
		}
		return seriesIds;
	}
}
