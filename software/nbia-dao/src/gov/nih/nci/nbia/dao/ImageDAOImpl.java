/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.nbia.dto.ImageSecurityDTO;
import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.internaldomain.CTImage;
import gov.nih.nci.nbia.internaldomain.GeneralImage;
import gov.nih.nci.nbia.util.HqlUtils;
import gov.nih.nci.nbia.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ImageDAOImpl extends AbstractDAO
                          implements ImageDAO{

	/**
	 * Copied down from ImageServlet in ncia-web and made generic
	 */
	@Transactional(propagation=Propagation.REQUIRED)
    public Collection<ImageSecurityDTO> findImageSecurityBySeriesInstanceUID(String seriesInstanceUid) throws DataAccessException {
        String query = "select distinct gimg "+
                       "from GeneralImage gimg "+
                       "     join gimg.dataProvenance dp "+
                       "where gimg.seriesInstanceUID = '"+seriesInstanceUid+ "'";

        Collection<ImageSecurityDTO> dtos = new ArrayList<ImageSecurityDTO>();
        List<GeneralImage> results = getHibernateTemplate().find(query);
        
        
        for (GeneralImage gi : results) {
            ImageSecurityDTO imageSecurityDTO = new ImageSecurityDTO(gi.getSOPInstanceUID(),
                                                                     gi.getFilename(),
                                                                     gi.getProject(),
                                                                     gi.getDataProvenance().getDpSiteName(),
                                                                     gi.getGeneralSeries().getSecurityGroup(),
                                                                     gi.getGeneralSeries().getVisibility().equals("1") || gi.getGeneralSeries().getVisibility().equals("12"),
                                                                     gi.getUsFrameNum());
            
            dtos.add(imageSecurityDTO);
        }
        
     System.out.println("===== In nbia-dao, ImageDAOImpl:findImageSecurityBySeriesInstanceUID(..) - create ImageSecurityDTO obj with gi.getGeneralSeries().getVisibility().equals 1 OR 12");
        
        return dtos;
    }

	@Transactional(propagation=Propagation.REQUIRED)
	public String findImagePath(Integer imagePkId)throws DataAccessException {

        GeneralImage generalImage = (GeneralImage) getHibernateTemplate().load(GeneralImage.class,
                                                                               imagePkId);
        if(generalImage==null){
        	return null;
        }
        return generalImage.getFilename();

	}

	@Transactional(propagation=Propagation.REQUIRED)
	public ImageSecurityDTO findImageSecurity(final String imageSopInstanceUid)throws DataAccessException {

		return (ImageSecurityDTO)getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) {
                Criteria criteria = session.createCriteria(GeneralImage.class, "i");
                ProjectionList projectionList = Projections.projectionList();
                projectionList.add(Projections.property("i.filename"));
                projectionList.add(Projections.property("gs.visibility"));
                projectionList.add(Projections.property("gs.securityGroup"));
                projectionList.add(Projections.property("tdp.project"));
                projectionList.add(Projections.property("tdp.dpSiteName"));
                projectionList.add(Projections.property("i.usFrameNum"));

                criteria = criteria.createCriteria("generalSeries","gs");
                criteria = criteria.createCriteria("study","s");
                criteria = criteria.createCriteria("patient","p");
                criteria = criteria.createCriteria("dataProvenance","tdp");
                criteria.setProjection(projectionList);

                criteria.add(Restrictions.eq("i.SOPInstanceUID", imageSopInstanceUid));
                //criteria.setResultTransformer(resultTransformer)
                Collection<Object[]> imageResults = (Collection<Object[]>)criteria.list();
                assert imageResults.size() <= 1;

                if(imageResults.size()==0) {
                	return null;
                }
                else {
                	Object[] imageResult = imageResults.iterator().next();
                	String fileName = (String)imageResult[0];
                	String seriesVisibility = (String)imageResult[1];

                	String securityGroup = (String)imageResult[2];
                	String project = (String)imageResult[3];
                	String dpSiteName = (String)imageResult[4];
                	String fn = (String)imageResult[5];	
                	
        	        ImageSecurityDTO imageSecurityDTO = new ImageSecurityDTO(imageSopInstanceUid,
        	        		                                                 fileName,
        	        		                                                 project,
        	        		                                                 dpSiteName,
        	        		                                                 securityGroup,
        	        		                                                 seriesVisibility.equals("1") || seriesVisibility.equals("12"),
        	        		                                                 fn);
        	   
        	     System.out.println("===== In nbia-dao, ImageDAOImpl:findImageSecurity(..) - doInHibernate method, I passed in: seriesVisibility.equals 1 OR seriesVisibility.equals 12");	        
        	        
        	        return imageSecurityDTO;
                }
            }
        });


	}

	@Transactional(propagation=Propagation.REQUIRED)
	public Collection<String> findDistinctConvolutionKernels()throws DataAccessException {

        DetachedCriteria criteria = DetachedCriteria.forClass(CTImage.class, "ct");
        criteria.setProjection(Projections.distinct(Projections.property("convolutionKernel")));
        criteria.add(Restrictions.isNotNull("convolutionKernel"));
        //criteria = criteria.createCriteria("generalImage");
        //criteria = criteria.createCriteria("generalSeries");
        criteria.addOrder(Order.asc("ct.convolutionKernel"));
        //criteria.add(Restrictions.eq("visibility", "1"));

        return (Collection<String>)getHibernateTemplate().findByCriteria(criteria);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)	
	public Collection<String> findAllImageType()throws DataAccessException {

        DetachedCriteria criteria = DetachedCriteria.forClass(GeneralImage.class, "gi");
        criteria.setProjection(Projections.distinct(Projections.property("usMultiModality")));
        criteria.add(Restrictions.isNotNull("usMultiModality"));
        criteria.addOrder(Order.asc("gi.usMultiModality"));

        return (Collection<String>)getHibernateTemplate().findByCriteria(criteria);
	}


	@Transactional(propagation=Propagation.REQUIRED)
    public Map<Integer, List<ImageDTO>> findKeyedImagesBySeriesPkId(List<Integer> seriesIds)throws DataAccessException {

        // Build return list
        List<ImageDTO> imageList = findImagesbySeriesPkID(seriesIds);

        // Build results hashmap for Dicom files based on results from database
        Map<Integer, List<ImageDTO>> dicomFilePaths = new HashMap<Integer, List<ImageDTO>>();

        for (ImageDTO biib : imageList) {
            List<ImageDTO> imagesForSeries = dicomFilePaths.get(biib.getSeriesPkId());

            if (imagesForSeries != null) {
                // Series is already in map.  Just add the filename
                imagesForSeries.add(biib);
            } else {
                // Add series and then add the filename to it
                imagesForSeries = new ArrayList<ImageDTO>();
                imagesForSeries.add(biib);
                dicomFilePaths.put(biib.getSeriesPkId(), imagesForSeries);
            }
        }

        return dicomFilePaths;
    }

    /**
     * Retrieve the maximum curation timestamp from the database
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public Date findLastCurationDate()throws DataAccessException {
        // Submit the search
        long start = System.currentTimeMillis();
        logger.info("Issuing query: " + LAST_CURATION_DATE_HQL);

        List result = getHibernateTemplate().find(LAST_CURATION_DATE_HQL);
        long end = System.currentTimeMillis();
        logger.debug("total query time: " + (end - start) + " ms");

        return (Date) result.get(0);
    }

	@Transactional(propagation=Propagation.REQUIRED)
	public List<ImageDTO> findImagesbySeriesDTO(SeriesDTO seriesDTO)throws DataAccessException {
		Collection<Integer> ids = new ArrayList<Integer>();
		ids.add(seriesDTO.getSeriesPkId());
		return this.findImagesbySeriesPkID(ids);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<ImageDTO> findImagesbySeriesPkID(Integer seriesPkId)throws DataAccessException {
		Collection<Integer> ids = new ArrayList<Integer>();
		ids.add(seriesPkId);
		return this.findImagesbySeriesPkID(ids);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<ImageDTO> findImagesbySeriesPkID(Collection<Integer> seriesPkIds)throws DataAccessException {
		String whereStmt = HqlUtils.buildInClauseUsingIntegers(" image.seriesPKId in ", seriesPkIds);
        return searchImagesbyHql(whereStmt);
    }

	@Transactional(propagation=Propagation.REQUIRED)
	public List<ImageDTO> findImagesbySeriesInstandUid(Collection<String> seriesInstanceUids)throws DataAccessException {
		String whereStmt = HqlUtils.buildInClause(" image.seriesInstanceUID in ",seriesInstanceUids);
		return searchImagesbyHql(whereStmt);
	}


	@Transactional(propagation=Propagation.REQUIRED)
	public ImageDTO getGeneralImageByImagePkId(Integer imagePkId) throws DataAccessException {

		ImageDTO imageDto = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(GeneralImage.class);
		criteria.add(Restrictions.eq("id", imagePkId));

		List<GeneralImage> result = getHibernateTemplate().findByCriteria(criteria);
		if (result != null && result.size() > 0)
		{
			imageDto = new ImageDTO();
			GeneralImage image = (GeneralImage)result.get(0);
			imageDto.setProject(image.getDataProvenance().getProject());
			imageDto.setSiteName(image.getDataProvenance().getDpSiteName());
		}

		return imageDto;
	}


	//////////////////////////////////////PRIVATE///////////////////////////////////////////

    private static Logger logger = Logger.getLogger(ImageDAO.class);

    private static final String LAST_CURATION_DATE_HQL = "select max(gi.curationTimestamp) from GeneralImage gi";

	private static final String SQL_QUERY_SELECT = "SELECT image.id, image.contentDate, image.contentTime, image.filename, image.seriesPKId, image.dicomSize, image.instanceNumber, image.seriesInstanceUID, image.SOPInstanceUID, image.usFrameNum, image.studyInstanceUID ";
    private static final String SQL_QUERY_FROM = "FROM GeneralImage image ";
    private static final String SQL_QUERY_WHERE = "WHERE ";

	private List<ImageDTO> searchImagesbyHql(String whereCondi) throws DataAccessException {
        String selectStmt = SQL_QUERY_SELECT;
        String fromStmt = SQL_QUERY_FROM;
        String whereStmt = SQL_QUERY_WHERE;
        List<ImageDTO> imageList = new ArrayList<ImageDTO>();
        whereStmt += whereCondi;


        // Submit the search
        long start = System.currentTimeMillis();
        logger.info("Issuing query: " + selectStmt + fromStmt + whereStmt);

        List seriesQuery = getHibernateTemplate().find(selectStmt + fromStmt + whereStmt);
        long end = System.currentTimeMillis();
        logger.debug("total query time: " + (end - start) + " ms");

        for (Object item : seriesQuery) {
            Object[] row = (Object[]) item;


            String imageFileName = row[3].toString();

            ImageDTO thumbnailDTO = new ImageDTO();
            thumbnailDTO.setImagePkId((Integer) row[0]);
            thumbnailDTO.setContentDate((Date) row[1]);
            thumbnailDTO.setContentTime(Util.nullSafeString(row[2]));
            thumbnailDTO.setSeriesPkId((Integer) row[4]);
            thumbnailDTO.setInstanceNumber((Integer) row[6]);
            thumbnailDTO.setSeriesInstanceUid(row[7].toString());
            thumbnailDTO.setSopInstanceUid(row[8].toString());
            thumbnailDTO.setFileURI(imageFileName);
            thumbnailDTO.setSize((Long) row[5]);
             if (row[9]== null) {
			            	thumbnailDTO.setFrameNum(0);
			            }
			            else {
			            	thumbnailDTO.setFrameNum(Integer.parseInt((String) row[9]));
            }
             thumbnailDTO.setStudyInstanceUid(row[10].toString());
            imageList.add(thumbnailDTO);
        }

        Collections.sort(imageList);

        return imageList;
    }
}
