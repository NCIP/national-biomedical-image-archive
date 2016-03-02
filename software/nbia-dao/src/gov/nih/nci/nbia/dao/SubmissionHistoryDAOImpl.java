/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.DayCountDTO;
import gov.nih.nci.nbia.dto.SeriesSubmissionCountDTO;
import gov.nih.nci.nbia.dto.SubmissionCountsDTO;
import gov.nih.nci.nbia.internaldomain.SubmissionHistory;
import gov.nih.nci.nbia.util.CrossDatabaseUtil;
import gov.nih.nci.nbia.util.NCIAConfig;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;
import org.hibernate.type.Type;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * This object encapsulates data access pertaining to the submission_history table.
 * Theoretically would contain all data access CRUD operations, but currently only
 * has "find" operations.
 */
public class SubmissionHistoryDAOImpl extends AbstractDAO 
                                      implements SubmissionHistoryDAO {

	/**
	 * Legal value for operationType
	 */
	public static final int NEW_IMAGE_SUBMISSION_OPERATION = SubmissionHistory.NEW_IMAGE_SUBMISSION_OPERATION;
	public static final int REPLACE_IMAGE_SUBMISSION_OPERATION = SubmissionHistory.REPLACE_IMAGE_SUBMISSION_OPERATION;
	public static final int ANNOTATION_SUBMISSION_OPERATION = SubmissionHistory.ANNOTATION_SUBMISSION_OPERATION;

   	/**
	 * For a given project/site in a date range (inclusive), return an object for each series
	 * that had a submission of the given type (annotation, new image, replace image).
	 *
	 * <p>For each series, the study id, patient id, and count of submissions for that series are
	 * included.
	 *
	 * <p>Theoretically, this method could return quite a bit of data so be careful.
	 */
	@Transactional(propagation=Propagation.REQUIRED)	
    public List<SeriesSubmissionCountDTO> findSeriesSubmissionCountInTimeFrame(Date startDate,
    		                                                                   Date endDate,
                                                                               String projectName,
                                                                               String siteName,
                                                                               int operationType) throws DataAccessException {
//        String hql = "SELECT gi.seriesInstanceUID, "+
//                     "       gi.studyInstanceUID,"+  //this is cool because it must be same per group of series
//                     "       gi.patientId,"+//this is cool be cause per group
//                     "       COUNT(*) as submissionCount "+
//                     "FROM SubmissionHistory gi "+
//                     "WHERE gi.project='"+projectName+"' and "+
//                     "      gi.site='"+siteName+"' and "+
//                     "      gi.operationType="+operationType+" AND"+
//                     "      gi.submissionDate between '"+startDate+"' and '"+endDate+"' "+
//                     "GROUP BY gi.seriesInstanceUID";


        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("seriesInstanceUID"));
        projectionList.add(Projections.property("studyInstanceUID"));
        projectionList.add(Projections.property(PATIENT_ID));
        projectionList.add(Projections.rowCount());
        projectionList.add(Projections.groupProperty("seriesInstanceUID"));
        
        //mysql lets you do projections without a grouping clause
        if(NCIAConfig.getDatabaseType().equals("oracle")) {
        	projectionList.add(Projections.groupProperty("studyInstanceUID")); //necessary for oracle
        	projectionList.add(Projections.groupProperty(PATIENT_ID)); //necessary for oracle
        }
        //this leads to extra select, but not going to customize code around
        //this hidden detail

        DetachedCriteria criteria = DetachedCriteria.forClass(SubmissionHistory.class);
        criteria.setProjection(projectionList);
        criteria.add(Restrictions.eq(PROJECT, projectName));
        criteria.add(Restrictions.eq(SITE, siteName));
        criteria.add(Restrictions.eq(OPERATION_TYPE, operationType));
        criteria.add(createDateRangeRestriction(startDate, endDate));
        criteria.addOrder(Order.asc(PATIENT_ID));

        List<SeriesSubmissionCountDTO> seriesDtos = new ArrayList<SeriesSubmissionCountDTO>();

        List<Object[]> seriesResultList = (List<Object[]>)getHibernateTemplate().findByCriteria(criteria);
        for(Object[] seriesResult : seriesResultList) {
        	String seriesInstanceUid = (String)seriesResult[0];
        	String studyInstanceUid = (String)seriesResult[1];
        	String patientId = (String)seriesResult[2];
        	long submissionCount = (Integer)seriesResult[3];

        	SeriesSubmissionCountDTO vsSeriesDTO  = new SeriesSubmissionCountDTO(patientId,
        			                                                             studyInstanceUid,
        			                                                             seriesInstanceUid,
        			                                                             submissionCount);

            seriesDtos.add(vsSeriesDTO);
        }
        return seriesDtos;
   }

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of distinct patient id/study id/series id that were associated with
    * new image submissions.  Also return the number of new image submissions.
    */
   @Transactional(propagation=Propagation.REQUIRED)	
   public SubmissionCountsDTO findImageCounts(Date startDate,
                                              Date endDate,
                                              String projectName,
                                              String siteName) throws DataAccessException {
       return findAffectedCounts(startDate,
                                 endDate,
                                 projectName,
                                 siteName,
                                 SubmissionHistory.NEW_IMAGE_SUBMISSION_OPERATION);
   }

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of distinct patient id/study id/series id that were associated with
    * corrected/replace image submissions.  Also return the number of corrected/replaced image submissions.
    */
   @Transactional(propagation=Propagation.REQUIRED)	
   public SubmissionCountsDTO findCorrectedCounts(Date startDate,
                                                  Date endDate,
                                                  String projectName,
                                                  String siteName) throws DataAccessException {
       return findAffectedCounts(startDate,
                                  endDate,
                                  projectName,
                                  siteName,
                                  SubmissionHistory.REPLACE_IMAGE_SUBMISSION_OPERATION);
   }

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of distinct patient id/study id/series id that were associated with
    * annotation submissions.  Also return the number of annoatation submissions.
    */
   @Transactional(propagation=Propagation.REQUIRED)   
   public SubmissionCountsDTO findAnnotationCounts(Date startDate,
                                                   Date endDate,
                                                   String projectName,
                                                   String siteName)  throws DataAccessException {
       return findAffectedCounts(startDate,
                                  endDate,
                                  projectName,
                                  siteName,
                                  SubmissionHistory.ANNOTATION_SUBMISSION_OPERATION);
   }

   /**
    * For a given project/site in a date range (inclusive), return the number
    * of new image submissions.
    */
   @Transactional(propagation=Propagation.REQUIRED)
   public long findImageSubmissionCountInTimeFrame(Date startDate,
                                                   Date endDate,
                                                   String projectName,
                                                   String siteName) throws DataAccessException {

        return findOperationTypeCountInTimeFrame(startDate,
                                                 endDate,
                                                 projectName,
                                                 siteName,
                                                 SubmissionHistory.NEW_IMAGE_SUBMISSION_OPERATION);
   }


   /**
    * For a given project/site in a date range (inclusive), find the number
    * of series that had a submission before the time frame, and a submission in the
    * time frame, aka the count of "updated" series.
    */
   @Transactional(propagation=Propagation.REQUIRED)
   public int findUpdatedSeriesCountInTimeFrame(Date startDate,
                                                Date endDate,
                                                String projectName,
                                                String siteName) throws DataAccessException {

       return findCountInTimeFrame("updatedSeriesCountQuery",
                                   startDate,
                                   endDate,
                                   projectName,
                                   siteName);
   }

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of series that DID NOT have a submission before the time frame, and a
    * they DO have a submission in the time frame, aka the count of "new" series.
    */
   @Transactional(propagation=Propagation.REQUIRED)
   public int findNewSeriesCountInTimeFrame(Date startDate,
                                            Date endDate,
                                            String projectName,
                                            String siteName) throws DataAccessException {
       return findCountInTimeFrame("newSeriesCountQuery",
                                   startDate,
                                   endDate,
                                   projectName,
                                   siteName);
   }

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of studies that had a submission before the time frame, and a submission in the
    * time frame, aka the count of "updated" studies.
    */
   @Transactional(propagation=Propagation.REQUIRED)
   public int findUpdatedStudyCountInTimeFrame(Date startDate,
                                               Date endDate,
                                               String projectName,
                                               String siteName) throws DataAccessException {

       return findCountInTimeFrame("updatedStudyCountQuery",
                                   startDate,
                                   endDate,
                                   projectName,
                                   siteName);
   }

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of studies that DID NOT have a submission before the time frame, and a
    * they DO have a submission in the time frame, aka the count of "new" studies.
    */
   @Transactional(propagation=Propagation.REQUIRED)   
   public int findNewStudyCountInTimeFrame(Date startDate,
                                           Date endDate,
                                           String projectName,
                                           String siteName) throws DataAccessException {
       return findCountInTimeFrame("newStudyCountQuery",
                                   startDate,
                                   endDate,
                                   projectName,
                                   siteName);
   }

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of patients that had a submission before the time frame, and a submission in the
    * time frame, aka the count of "updated" patients.
    */
   @Transactional(propagation=Propagation.REQUIRED)   
   public int findUpdatedPatientCountInTimeFrame(Date startDate,
                                                 Date endDate,
                                                 String projectName,
                                                 String siteName) throws DataAccessException {

       return findCountInTimeFrame("updatedPatientCountQuery",
                                   startDate,
                                   endDate,
                                   projectName,
                                   siteName);
   }

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of patients that DID NOT have a submission before the time frame, and a
    * they DO have a submission in the time frame, aka the count of "new" patients.
    */
   @Transactional(propagation=Propagation.REQUIRED)   
   public int findNewPatientCountInTimeFrame(Date startDate,
                                             Date endDate,
                                             String projectName,
                                             String siteName) throws DataAccessException {

       return findCountInTimeFrame("newPatientCountQuery",
                                   startDate,
                                   endDate,
                                   projectName,
                                   siteName);
   }

   /**
    * For a given project/site in a date range (inclusive), find the days where
    * a submission of the given type occurred.  The day returned will be associated
    * with the count of submissions for that day (the sum of all the specified
    * operation types on that day).
    */
   @Transactional(propagation=Propagation.REQUIRED) 
   public List<DayCountDTO> findSubmissionDatesInTimeFrame(Date startDate,
		                                                   Date endDate,
		                                                   String projectName,
		                                                   String siteName,
		                                                   Integer[] operationTypes) throws DataAccessException {
       //this smells a little.  better to have different dao per platform i guess
	   if(NCIAConfig.getDatabaseType().equals("mysql")) {
       	   return findSubmissionDatesInTimeFrame_mysql(startDate, endDate, projectName, siteName, operationTypes);
       }
       else
       if(NCIAConfig.getDatabaseType().equals("oracle")) {
       	   return findSubmissionDatesInTimeFrame_oracle(startDate, endDate, projectName, siteName, operationTypes);
       }
	   throw new RuntimeException("bad db type");
   }
   
  

   
   /**
    * For a given project/site in a date range (inclusive), return the number
    * of annotation submissions.
    */
	@Transactional(propagation=Propagation.REQUIRED)   
    public long findAnnotationSubmissionCountInTimeFrame(Date startDate,
                                                         Date endDate,
                                                         String projectName,
                                                         String siteName) throws DataAccessException {

        return findOperationTypeCountInTimeFrame(startDate,
                                                 endDate,
                                                 projectName,
                                                 siteName,
                                                 SubmissionHistory.ANNOTATION_SUBMISSION_OPERATION);
    }

    ///////////////////////////////////////PRIVATE//////////////////////////////////////////

    private static final String PROJECT = "project";

    private static final String SITE = "site";

    private static final String OPERATION_TYPE = "operationType";

    private static final String SUBMISSION_DATE = "submissionDate";

    private static final String PATIENT_ID = "patientId";
    
    /**
     * This is a helper method to call the named queries that are necessary
     * for the accrual report.  Check out SubmissionHistory.hbm.xml to see the queries.
     */
    private int findCountInTimeFrame(final String namedQuery,
    		                         final Date startDate,
    		                         final Date endDate,
    		                         final String projectName,
    		                         final String siteName) {


        return (Integer)getHibernateTemplate().execute(  
            new HibernateCallback() {
            	
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
     			    	
    	
				    SQLQuery newPatientCountQuery = (SQLQuery)session.getNamedQuery(CrossDatabaseUtil.getNamedQuery(namedQuery));
				    newPatientCountQuery.setParameter("projectName", projectName);
				    newPatientCountQuery.setParameter("siteName", siteName);
				    newPatientCountQuery.setParameter("startDate", startDate, new DateType());
				    newPatientCountQuery.setParameter("endDate", endDate, new DateType());
		    		                         
                    List countList = newPatientCountQuery.list();
                    //assert countList.size == 1
                    return (Integer)countList.get(0);
   			    }
            });
   }

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of submissions of a given type.
    */
   private long findOperationTypeCountInTimeFrame(Date startDate,
                                                  Date endDate,
                                                  String projectName,
                                                  String siteName,
                                                  int operationType) {


//
//            String hql = "SELECT count(*) "+
//                         "FROM SubmissionHistory gi "+
//                         "WHERE gi.project='"+projectName+"'"+_AND_+
//                         "      gi.site='"+siteName+"'"+_AND_+
//                         "      gi.operationType="+operationType+_AND_+
//                         "      gi.submissionDate between '"+startDate+"'"+_AND_+"'"+endDate+"'";
//
//            List resultSet = dataAccess.search(hql);


        DetachedCriteria criteria = DetachedCriteria.forClass(SubmissionHistory.class);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(PROJECT, projectName));
        criteria.add(Restrictions.eq(SITE, siteName));
        criteria.add(Restrictions.eq(OPERATION_TYPE, operationType));
        criteria.add(createDateRangeRestriction(startDate, endDate));

        List resultSet = getHibernateTemplate().findByCriteria(criteria);
        long count = (Integer)resultSet.get(0);

        return count;
    }


    /**
     * For a given project/site in a date range (inclusive), find the number
     * of distinct patient id/study id/series id that were associated with
     * a given submission type. Also return the number of submissions of that type.
     */
    private SubmissionCountsDTO findAffectedCounts(Date startDate,
                                                   Date endDate,
                                                   String projectName,
                                                   String siteName,
                                                   int operationType) {
//        String hql = "SELECT count(distinct patientId), count(distinct studyInstanceUid), count(distinct seriesInstanceUid), count(*)"+
//                     "FROM SubmissionHistory gi "+
//                     "WHERE gi.project='"+projectName+"'"+_AND_+
//                     "      gi.site='"+siteName+"'"+_AND_+
//                     "      gi.operationType=1"+_AND_+
//                     "      gi.submissionDate between '"+startDate+"'"+_AND_+"'"+endDate+"'";

            ProjectionList projectionList = Projections.projectionList();
            projectionList.add(Projections.countDistinct(PATIENT_ID));
            projectionList.add(Projections.countDistinct("studyInstanceUID"));
            projectionList.add(Projections.countDistinct("seriesInstanceUID"));
            projectionList.add(Projections.rowCount());

            DetachedCriteria criteria = DetachedCriteria.forClass(SubmissionHistory.class);
            criteria.setProjection(projectionList);
            criteria.add(Restrictions.eq(PROJECT, projectName));
            criteria.add(Restrictions.eq(SITE, siteName));
            criteria.add(Restrictions.eq(OPERATION_TYPE, operationType));
            criteria.add(createDateRangeRestriction(startDate, endDate));

            List<Object[]> countsList = (List<Object[]>)getHibernateTemplate().findByCriteria(criteria);
            //List<Long[]> countsList = (List<Long[]>)dataAccess.search(hql);
            //assert size == 1
            Object[] counts = countsList.get(0);
            long correctedPatients = (Integer)counts[0];
            long correctedStudies = (Integer)counts[1];
            long correctedSeries = (Integer)counts[2];
            long correctedImages = (Integer)counts[3];

            SubmissionCountsDTO correctedCountsDTO = new SubmissionCountsDTO(correctedPatients,
                                                                             correctedStudies,
                                                                             correctedSeries,
                                                                             correctedImages);

            return correctedCountsDTO;
    }

    private static Criterion createDateRangeRestriction(Date startDate,
                                                        Date endDate) {

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String startDateStr = sdf.format(startDate);
    	String endDateStr = sdf.format(endDate);

    	return Restrictions.sqlRestriction(CrossDatabaseUtil.submissionTimeStampRange(startDateStr, endDateStr));


    }
    
   private List<DayCountDTO> findSubmissionDatesInTimeFrame_mysql(Date startDate,
                                                           Date endDate,
                                                           String projectName,
                                                           String siteName,
                                                           Integer[] operationTypes) throws DataAccessException {

        DetachedCriteria criteria = DetachedCriteria.forClass(SubmissionHistory.class);

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.sqlProjection("date(submission_timestamp) as the_date",
                                                     new String[]{"the_date"},
                                                     new Type[] {Hibernate.DATE}));
        projectionList.add(Projections.rowCount());
        projectionList.add(Projections.sqlGroupProjection("date(submission_timestamp) as just_date",
                                                          "just_date",
                                                          new String[]{"just_date"},
                                                          new Type[] {Hibernate.DATE}));
        criteria.setProjection(projectionList);
        criteria.add(Restrictions.eq(PROJECT, projectName));
        criteria.add(Restrictions.eq(SITE, siteName));
        criteria.add(Restrictions.in(OPERATION_TYPE, operationTypes));
        criteria.add(createDateRangeRestriction(startDate, endDate));
        //this is broken for oracle i believe
        criteria.addOrder(Order.asc(SUBMISSION_DATE));

        List<DayCountDTO> dayCountDtoList = new ArrayList<DayCountDTO>();
        
        List<Object[]> submissionDayAndCountList = (List<Object[]>)getHibernateTemplate().findByCriteria(criteria);
        for(Object[] submissionDayAndCount : submissionDayAndCountList) {
        	Date day = (Date)submissionDayAndCount[0];
        	int count = (Integer)submissionDayAndCount[1];
        	DayCountDTO dayCountDTO = new DayCountDTO(day, count);
        	dayCountDtoList.add(dayCountDTO);
        }
        return dayCountDtoList;
    }

 
   private List<DayCountDTO> findSubmissionDatesInTimeFrame_oracle(final Date startDate,
                                                            final Date endDate,
                                                            final String projectName,
                                                            final String siteName,
                                                            final Integer[] operationTypes) throws DataAccessException {

       return (List<DayCountDTO>)getHibernateTemplate().execute(  
       new HibernateCallback() {             	
           public Object doInHibernate(Session session) throws HibernateException, SQLException {
        			    	
       	
   	           SQLQuery findSubmissionDatesInTimeFrameQuery = (SQLQuery)session.getNamedQuery(CrossDatabaseUtil.getNamedQuery("findSubmissionDatesInTimeFrame"));
   	           findSubmissionDatesInTimeFrameQuery.setParameter("projectName", projectName);
   	           findSubmissionDatesInTimeFrameQuery.setParameter("siteName", siteName);
   	           findSubmissionDatesInTimeFrameQuery.setParameter("startDate", startDate, new DateType());
   	           findSubmissionDatesInTimeFrameQuery.setParameter("endDate", endDate, new DateType());
   	           findSubmissionDatesInTimeFrameQuery.setParameterList("operationTypes", operationTypes);
   
   	           List<DayCountDTO> dayCountDtoList = new ArrayList<DayCountDTO>();
   	           List<Object[]> submissionDayAndCountList = (List<Object[]>)findSubmissionDatesInTimeFrameQuery.list();
   			   for(Object[] submissionDayAndCount : submissionDayAndCountList) {
   				   Date day = (Date)submissionDayAndCount[0];
   				   int count = (Integer)submissionDayAndCount[1];
   				   DayCountDTO dayCountDTO = new DayCountDTO(day, count);
   				   dayCountDtoList.add(dayCountDTO);
   		       }
   			   return dayCountDtoList;
   		        
      	   }
       });	   
         
    }    
}
