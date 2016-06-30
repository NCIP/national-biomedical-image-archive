/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.QcSearchResultDTO;
import gov.nih.nci.nbia.dto.QcStatusHistoryDTO;
import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.internaldomain.QCStatusHistory;
import gov.nih.nci.nbia.qctool.VisibilityStatus;
import gov.nih.nci.nbia.util.CrossDatabaseUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class QcStatusDAOImpl extends AbstractDAO
                             implements QcStatusDAO{

	private static final int PARAMETER_LIMIT = 800;

	@Transactional(propagation=Propagation.REQUIRED)
	public List<QcSearchResultDTO> findSeries(String[] qcStatus,
			                                  List<String> collectionSites, String[] additionalQcFlagList,
			                                  String[] patients) throws DataAccessException {
		return findSeries(qcStatus, collectionSites, additionalQcFlagList, patients, null, null, 100000);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<QcSearchResultDTO> findSeries(String[] qcStatus,
			                                  List<String> collectionSites, String[] additionalQcFlagList, 
			                                  String[] patients, Date fromDate, Date toDate, int maxRows) throws DataAccessException {
		
		String selectStmt = "SELECT gs.project," +
                "gs.site," +
                "gs.patientId,"+
                "gs.studyInstanceUID," +
                "gs.seriesInstanceUID,"+
                "gs.visibility," +
                "gs.maxSubmissionTimestamp,"+
                "gs.modality, "+
                "gs.seriesDesc, " +
                
                "gs.batch, " +
                "gs.submissionType, gs.releasedStatus, tdp.id ";
		
		String fromStmt = "FROM GeneralSeries as gs, Patient as pt, TrialDataProvenance as tdp ";
			
		String whereStmt = " WHERE " +
		                   computeVisibilityCriteria(qcStatus) +
		                   computeCollectionCriteria(collectionSites) +
		                   computeAdditionalFlags(additionalQcFlagList) +	                 
		                   computePatientCriteria(patients) +
		                   computeSubmissionDateCriteria(fromDate, toDate);

		List<QcSearchResultDTO> searchResultDtos = new ArrayList<QcSearchResultDTO>();

		String hql = selectStmt + fromStmt + whereStmt;

		System.out.println("In QcStatusDAOImpl:findSeries(...) with additional qc values: " 
	        		+ "Batch = '" + additionalQcFlagList[0] + "', submissionType = '" + additionalQcFlagList[1] + "', releasedStatus = '"  + additionalQcFlagList[2] + "'");
	    
		System.out.println("\n In QcStatusDAOImpl:findSeries(...) with hibernate hql query = " + hql + "\n");
		
//		List<Object[]> searchResults = getHibernateTemplate().find(hql);

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session s = sf.getCurrentSession();
		Query q = s.createQuery(hql);
		q.setFirstResult(0);
		q.setMaxResults(maxRows);
		List<Object[]> searchResults = q.list();

		for (Object[] row : searchResults) {
			String collection = (String) row[0];
			String site = (String) row[1];
			String patient = (String) row[2];
			String study = (String) row[3];
			String series = (String) row[4];
			String visibilitySt = (String) row[5];
			Timestamp submissionDate = (Timestamp) row[6];
			String modality = (String) row[7];
			String seriesDesc = (String) row[8];
			
			String batch = "" + row[9];
			String submissionType = (String) row[10];
			String releasedStatus = (String) row[11];
			String trialDpPkId = "" + row[12];
			
			Date subDate = null;
			if(submissionDate != null) {
				subDate = new Date(submissionDate.getTime());
			} 

			QcSearchResultDTO qcSrDTO = new QcSearchResultDTO(collection,
					                                          site,
					                                          patient,
					                                          study,
					                                          series,
					                                          subDate,
					                                          visibilitySt, 
					                                          modality, 
					                                          seriesDesc, 
					                                          batch, submissionType, releasedStatus, trialDpPkId);
			searchResultDtos.add(qcSrDTO);
		}

		return searchResultDtos;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<QcStatusHistoryDTO> findQcStatusHistoryInfo(List<String> seriesList) throws DataAccessException{
		
		List<QcStatusHistoryDTO> qcsList = new ArrayList<QcStatusHistoryDTO>();
		String selectStmt = "SELECT qsh.historyTimestamp,"
				+ "qsh.seriesInstanceUid," + "qsh.oldValue," + "qsh.newValue," 
				+ "qsh.oldBatch, qsh.newBatch, " 
				+ "qsh.oldSubmissionType, qsh.newSubmissionType,"
				+ "qsh.oldReleasedStatus, qsh.newReleasedStatus,"
				+ "qsh.comment," + "qsh.userId ";
		String fromStmt = "FROM QCStatusHistory as qsh";
		String whereStmt = " WHERE qsh.seriesInstanceUid in (";

		if (seriesList.size() > PARAMETER_LIMIT) {
			Collection<Collection<String>> seriesPkIdsBatches = split(new ArrayList<String>(seriesList), PARAMETER_LIMIT);
			for (Collection<String> seriesPkIdBatch : seriesPkIdsBatches) {
				String hql = new String() + selectStmt + fromStmt + whereStmt;
				hql += constructSeriesIdList(new ArrayList<String>(seriesPkIdBatch));
				hql += ") ORDER BY qsh.seriesInstanceUid,qsh.historyTimestamp";
				qcsList.addAll(getResults(hql));
			}
		} else {
		
			whereStmt = whereStmt + constructSeriesIdList(seriesList) + ")"
					+ " ORDER BY qsh.seriesInstanceUid,qsh.historyTimestamp";
			String hql = selectStmt + fromStmt + whereStmt;
			qcsList.addAll(getResults(hql));
		}
		return qcsList;

	}
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

	private List<QcStatusHistoryDTO> getResults(String hql) {
		List<QcStatusHistoryDTO> qcsList = new ArrayList<QcStatusHistoryDTO>();
		List<Object[]> searchResults = getHibernateTemplate().find(hql);

		for (Object[] row : searchResults) {
			Timestamp historyTimestamp = (Timestamp) row[0];
			String series = (String) row[1];
			String oldValue = (String) row[2];
			String newValue = (String) row[3];
			
			String oldBatch = (String) row[4];
			String newBatch = (String) row[5];
			String oldSubmissionType = (String) row[6];
			String newSubmissionType = (String) row[7];
			
			String oldReleasedStatus = (String) row[8];
			String newReleasedStatus = (String) row[9];
	 		
			String comment = (String) row[10];
			String userId = (String) row[11];
			
			QcStatusHistoryDTO qcshDTO = new QcStatusHistoryDTO(new Date(historyTimestamp.getTime()),
					 series, newValue, oldValue, oldBatch, newBatch, oldSubmissionType, newSubmissionType, oldReleasedStatus, newReleasedStatus,
					 comment, userId);
			
			qcsList.add(qcshDTO);
		}
		return qcsList;
	}
	private String constructSeriesIdList(List<String> seriesList) {
		StringBuffer sb = new StringBuffer();
		if (seriesList != null) {
			for (int i = 0; i < seriesList.size(); ++i) {
				if (i == 0) {
					sb.append("'" + seriesList.get(i) + "'");
				} else {
					sb.append(", '" + seriesList.get(i) + "'");
				}
			}
		}
		return sb.toString();
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateQcStatus(List<String> seriesList,
			                   List<String> statusList,
			                   String newStatus, 
			                   String[] additionalQcFlagList, String[] newAdditionalQcFlagList, 
			                   String userName, String comment) throws DataAccessException {
		
		System.out.println("========== In QcStatusDAOImpl:updateQcStatus(...) - statusList size is: " + statusList.size());
		
		
		
		for (int i = 0; i < seriesList.size(); ++i) {
			String seriesId = seriesList.get(i);
			
			System.out.println("========== In QcStatusDAOImpl:updateQcStatus(...) - in forLoop B4 calling updateDb with the following params: ");
			System.out.println("========== In QcStatusDAOImpl:updateQcStatus(...) -  seriesId = " + seriesId);
			System.out.println("========== In QcStatusDAOImpl:updateQcStatus(...) -  statusList.get(" + i + ") = " + statusList.get(i));
			System.out.println("========== In QcStatusDAOImpl:updateQcStatus(...) -  newStatus = " + newStatus);
			System.out.println("========== In QcStatusDAOImpl:updateQcStatus(...) - additionalQcFlagList[0] is batchNum b4 = " + additionalQcFlagList[0]);
			System.out.println("========== In QcStatusDAOImpl:updateQcStatus(...) - newAdditionalQcFlagList[0] is batchNum after = " + newAdditionalQcFlagList[0]);
			System.out.println("========== In QcStatusDAOImpl:updateQcStatus(...) - additionalQcFlagList[1] is submissionType b4 = " + additionalQcFlagList[1]);
			System.out.println("========== In QcStatusDAOImpl:updateQcStatus(...) - newAdditionalQcFlagList[1] is submissionType after = " + newAdditionalQcFlagList[1]);
			
			System.out.println("========== In QcStatusDAOImpl:updateQcStatus(...) - additionalQcFlagList[2] is releasedStatus b4 = " + additionalQcFlagList[2]);
			System.out.println("========== In QcStatusDAOImpl:updateQcStatus(...) - newAdditionalQcFlagList[2] is releasedStatus after = " + newAdditionalQcFlagList[2]);
			
			updateDb(seriesId, statusList.get(i), newStatus, additionalQcFlagList, newAdditionalQcFlagList, userName, comment);
			
		}
		
		
		
	}

	//////////////////////////////////////PRIVATE//////////////////////////////////////////

	private static String computeSubmissionDateCriteria(Date fromDate, Date toDate) {
		if( fromDate == null && toDate == null ) {
			return "";
		}
		else if( fromDate != null && toDate == null ) {
			toDate = Calendar.getInstance().getTime();
		}
		SimpleDateFormat dateFormat = CrossDatabaseUtil.getDatabaseSpecificDatePattern();

		// add a day to toDate because Oracle between command does not include the toDate
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate);
		cal.add( Calendar.DATE, 1 );
		toDate = cal.getTime();

		StringBuffer sb = new StringBuffer(49);
		sb.append( " and gs.maxSubmissionTimestamp between '" );
		sb.append( dateFormat.format(fromDate) );
		sb.append( "' and '" );
		sb.append(dateFormat.format(toDate) );
		sb.append( '\'' );

		return sb.toString();
	}

	private static String computePatientCriteria(String[] patients) {
		StringBuffer sb = new StringBuffer();
		if (patients != null) {
			for (int i = 0; i < patients.length; ++i) {
				if (i == 0) {
					sb.append(" and (gs.patientId in ('" + patients[i] + "'");
				} else {
					sb.append(",'" + patients[i] + "'");
				}
			}
			sb.append("))");
		}
		return sb.toString();
	}

	private static String computeCollectionCriteria(List<String> collectionSites) {
		StringBuffer sb = new StringBuffer();
		if ((collectionSites != null) && (collectionSites.size() >= 1)) {
			sb.append(" and (");
			for (int i = 0; i < collectionSites.size(); ++i) {
				String item = (String) collectionSites.get(i);
				String[] collectionSiteNames = item.split("//");

				if (i == 0) {
					sb
							.append(" (gs.project='" + collectionSiteNames[0]
									+ "' and gs.site='"
									+ collectionSiteNames[1] + "')");
				} else {
					sb
							.append(" or (gs.project='"
									+ collectionSiteNames[0]
									+ "' and gs.site='"
									+ collectionSiteNames[1] + "')");
				}
			}
			sb.append(')');
		}
		return sb.toString();
	}
	
	private static String computeAdditionalFlags(String[] additionalQcFlagList){
		String retStr = "";
		
		retStr = " and gs.patientPkId = pt.id and pt.dataProvenance = tdp.id ";
		
		if(additionalQcFlagList[0] != null && additionalQcFlagList[0].trim().length() > 0){	
			int batchNum = Integer.parseInt(additionalQcFlagList[0]);				
			if(batchNum > 0){
				retStr += " and gs.batch=" + batchNum;
			}			
		}
		
		if(additionalQcFlagList[1] != null && additionalQcFlagList[1].trim().length() > 0){		
			if(additionalQcFlagList[1].toUpperCase().contains("YES"))
				retStr += " and gs.submissionType='Complete' ";
	    	else if(additionalQcFlagList[1].toUpperCase().contains("NO"))
	    		retStr += " and gs.submissionType='Ongoing' ";	
	    }
		
		if(additionalQcFlagList[2] != null && additionalQcFlagList[2].trim().length() > 0){		
			if(additionalQcFlagList[2].toUpperCase().contains("YES"))
				retStr += " and gs.releasedStatus='Yes' ";
	    	else if(additionalQcFlagList[2].toUpperCase().contains("NO"))
	    		retStr += " and gs.releasedStatus='No' ";	
	    }
		
		System.out.println("In QcStatusDAOImpl:computeAdditionalFlags(...) retStr is: " + retStr);
		
		return retStr;
	}
	
	private static String computeVisibilityCriteria(String[] qcStatus) {
		StringBuffer sb = new StringBuffer();
		if (qcStatus != null && qcStatus.length > 0) {
			for (int j = 0; j < qcStatus.length; ++j) {
				if (j == 0) {
					sb.append("(gs.visibility='"
							+ VisibilityStatus.stringStatusFactory(qcStatus[j])
									.getNumberValue().intValue() + "'");
				} else {
					sb.append(" or gs.visibility='"
							+ VisibilityStatus.stringStatusFactory(qcStatus[j])
									.getNumberValue().intValue() + "'");
				}
			}
			sb.append(')');
		}
		return sb.toString();
	}

	
	private void updateDb(String seriesId,
			              String oldStatus,
			              String newStatus,
			              String[] additionalQcFlagList, String[] newAdditionalQcFlagList, 
			              String userName,
			              String comment) {

		QCStatusHistory qsh = new QCStatusHistory();
		qsh.setNewValue(newStatus);
		qsh.setHistoryTimestamp(new Date());
		qsh.setOldValue(oldStatus);
		qsh.setSeriesInstanceUid(seriesId);
		qsh.setUserId(userName);
		qsh.setComment(comment);
	
	//// Additional QC Flags settings before and after changes ////
		if(additionalQcFlagList[0] != null && additionalQcFlagList[0].trim().length() > 0){
			qsh.setOldBatch(additionalQcFlagList[0]);
		}
	    if(newAdditionalQcFlagList[0] != null && newAdditionalQcFlagList[0].trim().length() > 0){	
	    	qsh.setNewBatch(newAdditionalQcFlagList[0]);
	    }
		
	    //-------------------------------------------------------------
	    if(additionalQcFlagList[1] != null && additionalQcFlagList[1].trim().length() > 0){
	    	if(additionalQcFlagList[1].toUpperCase().contains("YES"))
		    	qsh.setOldSubmissionType("Complete");
		    else if(additionalQcFlagList[1].toUpperCase().contains("NO"))
		    	qsh.setOldSubmissionType("Ongoing");
	    }
	    
	    if(newAdditionalQcFlagList[1] != null && newAdditionalQcFlagList[1].trim().length() > 0){
		    if(newAdditionalQcFlagList[1].toUpperCase().contains("YES"))
		    	qsh.setNewSubmissionType("Complete");
		    else if(newAdditionalQcFlagList[1].toUpperCase().contains("NO"))
		    	qsh.setNewSubmissionType("Ongoing");  	
	    }		
	    //---------------------------------------------------------------------
	    
	    if(additionalQcFlagList[2] != null && additionalQcFlagList[2].trim().length() > 0){
	    	if(additionalQcFlagList[2].toUpperCase().contains("YES"))
		    	qsh.setOldReleasedStatus("Yes");
		    else if(additionalQcFlagList[2].toUpperCase().contains("NO"))
		    	qsh.setOldReleasedStatus("No");
	    }
	    
	    if(newAdditionalQcFlagList[2] != null && newAdditionalQcFlagList[2].trim().length() > 0){
		    if(newAdditionalQcFlagList[2].toUpperCase().contains("YES"))
		    	qsh.setNewReleasedStatus("Yes");
		    else if(newAdditionalQcFlagList[2].toUpperCase().contains("NO"))
		    	qsh.setNewReleasedStatus("No");  	
	    }
	    
		String hql = "select distinct gs from GeneralSeries gs where gs.seriesInstanceUID ='"
				+ seriesId + "'";
		final String updateHql = createUpdateCurationTStatement(seriesId);
		List searchResults = getHibernateTemplate().find(hql);
		
		if (searchResults != null) {
			GeneralSeries gs = (GeneralSeries) (searchResults.get(0));
			
			gs.setVisibility(newStatus);
			
			if(newAdditionalQcFlagList[0] != null && newAdditionalQcFlagList[0].trim().length() > 0){
				gs.setBatch(Integer.parseInt(newAdditionalQcFlagList[0]));
			}
				
			if(newAdditionalQcFlagList[1] != null && newAdditionalQcFlagList[1].trim().length() > 0){
			   	if(newAdditionalQcFlagList[1].toUpperCase().contains("YES"))
			   		gs.setSubmissionType("Complete");
			    else if(newAdditionalQcFlagList[1].toUpperCase().contains("NO"))
			    	gs.setSubmissionType("Ongoing");
			}
			
			if(newAdditionalQcFlagList[2] != null && newAdditionalQcFlagList[2].trim().length() > 0){
			   	if(newAdditionalQcFlagList[2].toUpperCase().contains("YES"))
			   		gs.setReleasedStatus("Yes");
			    else if(newAdditionalQcFlagList[2].toUpperCase().contains("NO"))
			    	gs.setReleasedStatus("No");
			}
		
			getHibernateTemplate().update(gs);
			getHibernateTemplate().bulkUpdate(updateHql);	
			getHibernateTemplate().saveOrUpdate(qsh);
			
		}
	}

	private String createUpdateCurationTStatement(String seriesId){
	    String hql = "update GeneralImage set curationTimestamp = current_timestamp() where seriesInstanceUID = '"+seriesId+"'";
	    return hql;
	}
}
