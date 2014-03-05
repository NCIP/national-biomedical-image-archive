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
			                                  List<String> collectionSites,
			                                  String[] patients) throws DataAccessException {
		return findSeries(qcStatus, collectionSites, patients, null, null, 100000);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<QcSearchResultDTO> findSeries(String[] qcStatus,
			                                  List<String> collectionSites,
			                                  String[] patients, Date fromDate, Date toDate, int maxRows) throws DataAccessException {

		String selectStmt = "SELECT gs.project," +
		                           "gs.site," +
		                           "gs.patientId,"+
		                           "gs.studyInstanceUID," +
		                           "gs.seriesInstanceUID,"+
		                           "gs.visibility," +
		                           "gs.maxSubmissionTimestamp,"+
		                           "gs.modality, "+
		                           "gs.seriesDesc ";
		String fromStmt = "FROM GeneralSeries as gs";
		String whereStmt = " WHERE " +
		                   computeVisibilityCriteria(qcStatus) +
		                   computeCollectionCriteria(collectionSites) +
		                   computePatientCriteria(patients) +
		                   computeSubmissionDateCriteria(fromDate, toDate);

		List<QcSearchResultDTO> searchResultDtos = new ArrayList<QcSearchResultDTO>();

		String hql = selectStmt + fromStmt + whereStmt;

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
					                                          visibilitySt, modality, seriesDesc);
			searchResultDtos.add(qcSrDTO);
		}

		return searchResultDtos;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<QcStatusHistoryDTO> findQcStatusHistoryInfo(List<String> seriesList) throws DataAccessException{

		List<QcStatusHistoryDTO> qcsList = new ArrayList<QcStatusHistoryDTO>();
		String selectStmt = "SELECT qsh.historyTimestamp,"
				+ "qsh.seriesInstanceUid," + "qsh.oldValue," + "qsh.newValue,"
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
			String comment = (String) row[4];
			String userId = (String) row[5];
			QcStatusHistoryDTO qcshDTO = new QcStatusHistoryDTO(new Date(
					historyTimestamp.getTime()), series, newValue,
					oldValue, comment, userId);
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
			                   String userName,
			                   String comment) throws DataAccessException {
		for (int i = 0; i < seriesList.size(); ++i) {
			String seriesId = seriesList.get(i);
			updateDb(seriesId, statusList.get(i), newStatus, userName, comment);
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
			              String userName,
			              String comment) {

		QCStatusHistory qsh = new QCStatusHistory();
		qsh.setNewValue(newStatus);
		qsh.setHistoryTimestamp(new Date());
		qsh.setOldValue(oldStatus);
		qsh.setSeriesInstanceUid(seriesId);
		qsh.setUserId(userName);
		qsh.setComment(comment);

		String hql = "select distinct gs from GeneralSeries gs where gs.seriesInstanceUID ='"
				+ seriesId + "'";
		final String updateHql = createUpdateCurationTStatement(seriesId);
		List searchResults = getHibernateTemplate().find(hql);
		if (searchResults != null) {
			GeneralSeries gs = (GeneralSeries) (searchResults.get(0));
			gs.setVisibility(newStatus);

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
