/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.AnnotationDTO;
import gov.nih.nci.nbia.dto.AnnotationFileDTO;
import gov.nih.nci.nbia.internaldomain.Annotation;
import gov.nih.nci.nbia.util.HqlUtils;
import gov.nih.nci.nbia.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class AnnotationDAOImpl extends AbstractDAO
                               implements AnnotationDAO {

	@Transactional(propagation=Propagation.REQUIRED)
    public Map<Integer, List<AnnotationFileDTO>> findKeyedAnnotationBySeriesPkId(List<Integer> seriesIds) throws DataAccessException {
        Map<Integer, List<AnnotationFileDTO>> returnMap = new HashMap<Integer, List<AnnotationFileDTO>>();

        if (seriesIds.isEmpty()) {
            return returnMap;
        }

        List<AnnotationFileDTO> annotations = findAnnotationBySeriesPkId(seriesIds);

        for(AnnotationFileDTO dbars : annotations) {

            List<AnnotationFileDTO> annotsForSeries = returnMap.get(dbars.getSeriesPkId());

            if (annotsForSeries != null) {
                annotsForSeries.add(dbars);
            } else {
                annotsForSeries = new ArrayList<AnnotationFileDTO>();
                annotsForSeries.add(dbars);
                returnMap.put(dbars.getSeriesPkId(), annotsForSeries);
            }
        }

        return returnMap;
    }

	@Transactional(propagation=Propagation.REQUIRED)	
	public List<AnnotationFileDTO> findAnnotationBySeriesPkId(List<Integer> seriesPkIds) throws DataAccessException {
        String selectStmt = SQL_QUERY_SELECT;
        String fromStmt = SQL_QUERY_FROM;
        
        // Get handle to IDataAccess
        List<AnnotationFileDTO> returnValue = new ArrayList<AnnotationFileDTO>();

        List<List<Integer>> breakDownList = Util.breakListIntoChunks(seriesPkIds, 900);
        // Build the where clause based on the critiera
      
        List tempHolder = new ArrayList();
        for (List<Integer> unit : breakDownList)
        {	
        	  String whereStmt = HqlUtils.buildInClauseUsingIntegers(SQL_QUERY_WHERE +"annot.generalSeriesPkId IN ",
                      unit);
	        // Run the query
	        long start = System.currentTimeMillis();
	        List resultsData = getHibernateTemplate().find(selectStmt + fromStmt + whereStmt);
	        logger.info("Ran this query to retrieve annotations for series: " +
	            selectStmt + fromStmt + whereStmt);
	
	        long end = System.currentTimeMillis();
	        logger.debug("total query time: " + (end - start) + " ms");
	        for (Object o : resultsData)
	        {
	        	tempHolder.add(o);
	        }
        }
        // Map the rows retrieved from hibernate to the DataBasketResultSet objects.
        for (Object item : tempHolder) {
            Object[] row = (Object[]) item;
            AnnotationFileDTO dbars = new AnnotationFileDTO((Integer)row[0],
            		                                        row[1].toString(),
      	            		                                (Integer) row[2]);
            // Add to the list to return
            returnValue.add(dbars);
        }
        return returnValue;
    }


	@Transactional(propagation=Propagation.REQUIRED)
	public List<AnnotationDTO> findAnnotationBySeriesUid(String seriesInstanceUid) throws DataAccessException {

		List<AnnotationDTO> annotationList = new ArrayList<AnnotationDTO>();

		 String query = "select distinct ann from Annotation ann  where ann.seriesInstanceUID = '"+
	                    seriesInstanceUid + "'";


        List<Annotation> results = getHibernateTemplate().find(query);
        for(Annotation a:results){
        	AnnotationDTO dto = new AnnotationDTO(a.getFilePath(),
        			                              a.getFileName(),
        			                              a.getAnnotationType(),
        			                              a.getFileSize());
        	annotationList.add(dto);

        }
        return annotationList;
	}

	////////////////////////////////////////////PRIVATE///////////////////////////////////////////////
    private String SQL_QUERY_SELECT = "SELECT annot.generalSeriesPkId, annot.filePath, annot.fileSize ";
    private String SQL_QUERY_FROM = "FROM Annotation annot ";
    private String SQL_QUERY_WHERE = "WHERE ";

	private static Logger logger = Logger.getLogger(AnnotationDAO.class);
}
