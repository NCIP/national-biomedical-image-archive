/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.dto.StudyDTO;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class StudyDAOImpl extends AbstractDAO
                          implements StudyDAO {

    /**
     * This method will deal with the query where a list of SeriesPkId's
     * is passed in as a Query to the QueryHandler.  It will then return
     * the SeriesListResultSet, which contains all of the information necessary
     * for the second level query.
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public List<StudyDTO> findStudiesBySeriesId(Collection<Integer> seriesPkIds) throws DataAccessException {
    	if(seriesPkIds.size()==0) {
    		return new ArrayList<StudyDTO>();
    	}

        String selectStmt = SQL_QUERY_SELECT;
        String fromStmt = SQL_QUERY_FROM;
        String whereStmt = SQL_QUERY_WHERE;

        // Add the series PK IDs to the where clause
        whereStmt += "and series.id IN (";

        whereStmt += constructSeriesIdList(seriesPkIds);

        whereStmt += ")";

        long start = System.currentTimeMillis();
        logger.info("Issuing query: " + selectStmt + fromStmt + whereStmt);

        List<Object[]> seriesResults = getHibernateTemplate().find(selectStmt + fromStmt + whereStmt);
        long end = System.currentTimeMillis();
        logger.info("total query time: " + (end - start) + " ms");


        // List of StudyDTOs to eventually be returned
        Map<Integer, StudyDTO> studyList = new HashMap<Integer, StudyDTO>();
        Iterator<Object[]> iter = seriesResults.iterator();

        // Loop through the results.  There is one result for each series
        while (iter.hasNext()) {
        	Object[] row = iter.next();

            // Create the seriesDTO
            SeriesDTO seriesDTO = new SeriesDTO();
            //modality should never be null... but currently possible
            seriesDTO.setModality(Util.nullSafeString(row[8]));
            //Getting real data from Surendra, find data for manufacture could be null
            seriesDTO.setManufacturer(Util.nullSafeString(row[9]));
            seriesDTO.setSeriesUID(row[3].toString());
            seriesDTO.setSeriesNumber(Util.nullSafeString(row[10]));
            seriesDTO.setSeriesPkId((Integer) row[0]);
            seriesDTO.setDescription(Util.nullSafeString(row[7]));
            seriesDTO.setNumberImages((Integer) row[6]);
            Boolean annotationFlag = (Boolean) row[11];
            if(annotationFlag!=null) {
            	seriesDTO.setAnnotationsFlag(true);
            }
            seriesDTO.setAnnotationsSize((row[15] != null) ? (Long) row[15] : 0);
            seriesDTO.setStudyPkId((Integer) row[1]);
            seriesDTO.setStudyId(row[2].toString());
            seriesDTO.setTotalImagesInSeries((Integer)row[6]);
            seriesDTO.setTotalSizeForAllImagesInSeries((Long)row[12]);
            seriesDTO.setPatientId((String)row[13]);
            seriesDTO.setProject((String)row[14]);
            seriesDTO.setMaxFrameCount((String)row[16]);
            // Try to get the study if it already exists
            StudyDTO studyDTO = studyList.get(seriesDTO.getStudyPkId());

            if (studyDTO != null) {
                // Study already exists.  Just add series info
                studyDTO.getSeriesList().add(seriesDTO);
            } else {
                // Create the StudyDTO
                studyDTO = new StudyDTO();
                studyDTO.setStudyId(row[2].toString());
                studyDTO.setDate((Date) row[4]);
                studyDTO.setDescription(Util.nullSafeString(row[5]));

                studyDTO.setId(seriesDTO.getStudyPkId());

                // Add the series to the study
                studyDTO.getSeriesList().add(seriesDTO);

                // Add the study to the list
                studyList.put(studyDTO.getId(), studyDTO);
            }
        }

        //maybe a candidate to move this out of DAO into higher level

        // Convert to a list for sorting and to be returned
        List<StudyDTO> returnList = new ArrayList<StudyDTO>(studyList.values());

        // Sort the studies
        Collections.sort(returnList);
        for (StudyDTO studyDTO : returnList) {
        	List<SeriesDTO> seriesList = new ArrayList<SeriesDTO>(studyDTO.getSeriesList());
            Collections.sort(seriesList);
            studyDTO.setSeriesList(seriesList);
        }
        return returnList;
    }

	/**
	 * Fetch a set of patient/study info filtered by query keys
	 * This method is used for NBIA Rest API.
	 * @param collection A label used to name a set of images collected for a specific trial or other reason.	 * Assigned during the process of curating the data. The info is kept under project column
	 * @param patientId Patient ID
	 * @param studyInstanceUid Study Instance UID
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Object[]> getPatientStudy(String collection, String patientId, String studyInstanceUid,List<SiteData> authorizedSites) throws DataAccessException
	{
		String hql = "select distinct s.studyInstanceUID, s.studyDate, s.studyDesc, s.admittingDiagnosesDesc, s.studyId, " +
				"s.patientAge, s.patient.patientId, s.patient.patientName, s.patient.patientBirthDate, s.patient.patientSex, " +
				"s.patient.ethnicGroup, s.patient.dataProvenance.project, " +
				"(select count(*) from GeneralSeries gs where s.studyInstanceUID=gs.studyInstanceUID) "  +
				"from Study as s, GeneralSeries gs where s.studyInstanceUID=gs.studyInstanceUID and gs.visibility = '1' ";
		StringBuffer where = new StringBuffer();
		List<Object[]> rs = null;
		List<String> paramList = new ArrayList<String>();
		int i = 0;

		if (collection != null) {
			where = where.append(" and UPPER(s.patient.dataProvenance.project)=?");
			paramList.add(collection.toUpperCase());
		++i;
		}
		if (patientId != null) {
			where = where.append(" and UPPER(s.patient.patientId)=?");
			paramList.add(patientId.toUpperCase());
			++i;
		}
		if (studyInstanceUid != null) {
			where = where.append(" and UPPER(s.studyInstanceUID)=?");
			paramList.add(studyInstanceUid.toUpperCase());
			++i;
		}
		where.append(addSecurityGroup(authorizedSites));
		if (i > 0) {
			Object[] values = paramList.toArray(new Object[paramList.size()]);
			rs = getHibernateTemplate().find(hql + where.toString(), values);
		} else
			rs = getHibernateTemplate().find(hql + where.toString());

        return rs;
	}
	private StringBuffer addSecurityGroup(List<SiteData> authorizedSites) {
		StringBuffer where = new StringBuffer();
		String authorisedProjectName = getProjectNames(authorizedSites);
		String authorisedSiteName = getSiteNames(authorizedSites);
		if(authorisedProjectName != null && authorisedSiteName != null) {
			where = where.append(" and UPPER(gs.project) in (").append(authorisedProjectName).append(")").append(" and UPPER(gs.site) in (" + authorisedSiteName+")");
		}
		return where;
	}

	/////////////////////////////////////PRIVATE/////////////////////////////////////////
    private static final String SQL_QUERY_SELECT = "SELECT distinct series.id, study.id, study.studyInstanceUID, series.seriesInstanceUID, study.studyDate, study.studyDesc, series.imageCount, series.seriesDesc, series.modality, ge.manufacturer, series.seriesNumber, series.annotationsFlag, series.totalSize, series.patientId, study.patient.dataProvenance.project, series.annotationTotalSize, series.maxFrameCount  ";
    private static final String SQL_QUERY_FROM = "FROM Study study join study.generalSeriesCollection series join series.generalEquipment ge ";
    private static final String SQL_QUERY_WHERE = "WHERE series.visibility = '1' ";

	private static Logger logger = Logger.getLogger(ImageDAO.class);


    /**
     * Given a collection of integers, return a Stirng that is a comma
     * separate list of the single-quoted integers, without a trailing comma
     */
    private static String constructSeriesIdList(Collection<Integer> theSeriesPkIds) {
    	String theWhereStmt = "";
    	for (Iterator<Integer> i = theSeriesPkIds.iterator(); i.hasNext();) {
            Integer seriesPkId = i.next();
            theWhereStmt += ("'" + seriesPkId + "'");

            if (i.hasNext()) {
            	theWhereStmt += ",";
            }
        }
    	return theWhereStmt;
    }
    private static int PARAMETER_LIMIT = 700;

	private <T> Collection<Collection<T>> split(Collection<T> bigCollection, int maxBatchSize) {
		Collection<Collection<T>> result = new ArrayList<Collection<T>>();
		ArrayList<T> currentBatch = null;
		for (T t : bigCollection) {
			if (currentBatch == null) {
				currentBatch = new ArrayList<T>();
			} else if (currentBatch.size() >= maxBatchSize) {
				result.add(currentBatch);
				currentBatch = new ArrayList<T>();
			}
			currentBatch.add(t);
		}

		if (currentBatch != null) {
			result.add(currentBatch);
		}

		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<StudyDTO> findStudiesBySeriesIdDBSorted(
			Collection<Integer> seriesPkIds) throws DataAccessException {
		if (seriesPkIds.size() == 0) {
			return new ArrayList<StudyDTO>();
		}
		String selectStmt = "SELECT distinct series.id, study.id, study.studyInstanceUID, series.seriesInstanceUID, study.studyDate, study.studyDesc, series.imageCount, series.seriesDesc, series.modality, ge.manufacturer, series.seriesNumber, series.annotationsFlag, series.totalSize, series.patientId, study.patient.dataProvenance.project, series.annotationTotalSize , ge.manufacturerModelName, ge.softwareVersions ";
		String fromStmt = SQL_QUERY_FROM;
		String whereStmt = SQL_QUERY_WHERE;
		String oderBy = " Order by study.patient.dataProvenance.project,series.patientId,study.studyDate, study.studyDesc, series.modality, series.seriesDesc,ge.manufacturer, ge.manufacturerModelName, ge.softwareVersions, series.seriesInstanceUID";

		StringBuffer hql = null;
		List<Integer> partitions = null;
		;
		int listSize = seriesPkIds.size();
		List<StudyDTO> returnList = new ArrayList<StudyDTO>();
		long start = System.currentTimeMillis();

		if (listSize > PARAMETER_LIMIT) {
			Collection<Collection<Integer>> seriesPkIdsBatches = split(
					seriesPkIds, PARAMETER_LIMIT);
			for (Collection<Integer> seriesPkIdBatch : seriesPkIdsBatches) {
				whereStmt = new String() + SQL_QUERY_WHERE;
				whereStmt += "and series.id IN (";
				whereStmt += constructSeriesIdList(seriesPkIdBatch);
				whereStmt += ")";
				returnList.addAll(getResults(selectStmt + fromStmt + whereStmt
						+ oderBy));
			}
		} else {
			whereStmt += "and series.id IN (";
			whereStmt += constructSeriesIdList(seriesPkIds);
			whereStmt += ")";
			hql = new StringBuffer().append(selectStmt + fromStmt + whereStmt
					+ oderBy);
			returnList.addAll(getResults(hql.toString()));
		}
		long end = System.currentTimeMillis();
		logger.info("total time to excute all queries: " + (end - start)
				+ " ms");
		return returnList;
	}

	private List<StudyDTO> getResults(String hql) {
		List<StudyDTO> resultList = new ArrayList<StudyDTO>();
		long start = System.currentTimeMillis();
		List<Object[]> seriesResults = getHibernateTemplate().find(hql);
		long end = System.currentTimeMillis();
		logger.info("total query time: " + (end - start) + " ms");
		Iterator<Object[]> iter = seriesResults.iterator();
		// Loop through the results. There is one result for each series
		while (iter.hasNext()) {
			Object[] row = iter.next();
			// Create the seriesDTO
			SeriesDTO seriesDTO = new SeriesDTO();
			// modality should never be null... but currently possible
			seriesDTO.setModality(Util.nullSafeString(row[8]));
			// Getting real data from Surendra, find data for manufacture could
			// be null
			seriesDTO.setManufacturer(Util.nullSafeString(row[9]));
			seriesDTO.setSeriesUID(row[3].toString());
			seriesDTO.setSeriesNumber(Util.nullSafeString(row[10]));
			seriesDTO.setSeriesPkId((Integer) row[0]);
			seriesDTO.setDescription(Util.nullSafeString(row[7]));
			seriesDTO.setNumberImages((Integer) row[6]);
			Boolean annotationFlag = (Boolean) row[11];
			if (annotationFlag != null) {
				seriesDTO.setAnnotationsFlag(true);
			}
			seriesDTO
					.setAnnotationsSize((row[15] != null) ? (Long) row[15] : 0);
			seriesDTO.setStudyPkId((Integer) row[1]);
			seriesDTO.setStudyId(row[2].toString());
			seriesDTO.setTotalImagesInSeries((Integer) row[6]);
			seriesDTO.setTotalSizeForAllImagesInSeries((Long) row[12]);
			seriesDTO.setPatientId((String) row[13]);
			seriesDTO.setProject((String) row[14]);

			seriesDTO.setManufacturerModelName((String) row[16]);
			seriesDTO.setSoftwareVersion((String) row[17]);
			// Try to get the study if it already exists
			StudyDTO studyDTO = new StudyDTO();
			studyDTO.setStudyId(row[2].toString());
			studyDTO.setDate((Date) row[4]);
			studyDTO.setDescription(Util.nullSafeString(row[5]));
			studyDTO.setId(seriesDTO.getStudyPkId());
			// Add the series to the study
			studyDTO.getSeriesList().add(seriesDTO);
			resultList.add(studyDTO);
		}
		return resultList;
	}

		private String getProjectNames(Collection<SiteData> sites) {
			if(sites == null || sites.isEmpty()) {
				return null;
			}
			String projectNameStmt = "";
	    	for (Iterator<SiteData> i = sites.iterator(); i.hasNext();) {
	    		SiteData str = i.next();
	            projectNameStmt += ("'" + str.getCollection().toUpperCase() + "'");

	            if (i.hasNext()) {
	            	projectNameStmt += ",";
	            }
	        }
	    	return projectNameStmt;
	    }
		private String getSiteNames(Collection<SiteData> sites) {
			if(sites == null || sites.isEmpty()) {
				return null;
			}
			String siteWhereStmt = "";
	    	for (Iterator<SiteData> i = sites.iterator(); i.hasNext();) {
	    		SiteData str = i.next();
	            siteWhereStmt += ("'" + str.getSiteName().toUpperCase() + "'");

	            if (i.hasNext()) {
	            	siteWhereStmt += ",";
	            }
	        }
	    	return siteWhereStmt;
    }
}
