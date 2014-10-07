/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.CustomSeriesDTO;
import gov.nih.nci.nbia.dto.CustomSeriesListAttributeDTO;
import gov.nih.nci.nbia.dto.CustomSeriesListDTO;
import gov.nih.nci.nbia.dto.QcCustomSeriesListDTO;
import gov.nih.nci.nbia.internaldomain.CustomSeriesList;
import gov.nih.nci.nbia.internaldomain.CustomSeriesListAttribute;
import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.security.NCIASecurityManager;
import gov.nih.nci.nbia.security.NCIAUser;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * handle all database transactions for the custom series list
 *
 * @author lethai
 *
 */
/**
 * @author lethai
 *
 */
public class CustomSeriesListDAOImpl extends AbstractDAO
                                     implements CustomSeriesListDAO {

	private static Logger logger = Logger.getLogger(CustomSeriesListDAO.class);

	/**
	 * query database table to check for existence of name
	 *
	 * @param name
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean isDuplicateName(String name) throws DataAccessException {
		List<CustomSeriesList> customList = null;

        DetachedCriteria criteria = DetachedCriteria.forClass(CustomSeriesList.class);
		criteria.add(Expression.eq("name",name.trim()).ignoreCase());
		customList = getHibernateTemplate().findByCriteria(criteria);
		if (customList.size() > 0) {
			return true;
		}

		return false;
	}
	/**
	 * find series that belongs to public group
	 * @param seriesUids
	 * @param authorizedPublicSites
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<CustomSeriesDTO> findSeriesForPublicCollection(List<String> seriesUids,
									                           List<SiteData> authorizedPublicSites) throws DataAccessException {
		List<GeneralSeries> seriesList = new ArrayList<GeneralSeries>();
		List<CustomSeriesDTO> seriesDTOList = null;

		if (seriesUids == null || seriesUids.size() <= 0) {
			return new ArrayList<CustomSeriesDTO>();
		}

		List<List<String>> breakdownList = Util.breakListIntoChunks(seriesUids, 900);
		for (List<String> unitList : breakdownList)
		{
			List<GeneralSeries> temp = getSeriesList(unitList, authorizedPublicSites);
			for (GeneralSeries series : temp)
			{
				seriesList.add(series);
			}
		}

		seriesDTOList = convertHibernateObjectToCustomSeriesDTO(seriesList);

		return seriesDTOList;
	}

	private List<GeneralSeries> getSeriesList(List<String> seriesUids, List<SiteData> authorizedPublicSites)
	{

		DetachedCriteria criteria = DetachedCriteria.forClass(GeneralSeries.class);
		criteria.add(Restrictions.in("seriesInstanceUID", seriesUids));
		criteria.add(Restrictions.eq("visibility", "1"));
		criteria = criteria.createCriteria("study");
		criteria = criteria.createCriteria("patient");
		criteria = criteria.createCriteria("dataProvenance");
		if (authorizedPublicSites != null) {
			setAuthorizedSiteData(criteria,
					authorizedPublicSites);
		}

		List<GeneralSeries> seriesList = getHibernateTemplate().findByCriteria(criteria);

		return seriesList;
	}

	/**
	 * find all series that contains all the seriesuids and user has permission
	 * to see
	 *
	 * @param seriesUids
	 * @param authorizedSites
	 * @param authorizedSeriesSecurityGroups
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<CustomSeriesDTO> findSeriesBySeriesInstanceUids(List<String> seriesUids,
			                                                    List<SiteData> authorizedSites,
			                                                    List<String> authorizedSeriesSecurityGroups) throws DataAccessException {
		List<GeneralSeries> seriesList = null;
		List<CustomSeriesDTO> seriesDTOList = null;

		if (seriesUids == null || seriesUids.size() <= 0) {
			return new ArrayList<CustomSeriesDTO>();
		}
		GeneralSeriesDAOImpl generalSeriesDAO = new GeneralSeriesDAOImpl();
		seriesList = generalSeriesDAO.getSeriesFromSeriesInstanceUIDs(seriesUids,
						                                              authorizedSites,
						                                              authorizedSeriesSecurityGroups);
		seriesDTOList = convertHibernateObjectToCustomSeriesDTO(seriesList);

		return seriesDTOList;
	}

	/**
	 * Find all shared list by a given list of series instance uids
	 * @param seriesUids
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<QcCustomSeriesListDTO> findSharedListBySeriesInstanceUids(List<String> seriesUids) throws DataAccessException
	{
			List<QcCustomSeriesListDTO> returnList = new ArrayList<QcCustomSeriesListDTO>();
			List<Object[]> results = new ArrayList<Object[]>();

			List<List<String>> breakdownList = Util.breakListIntoChunks(seriesUids, 900);

			for (List<String> unitList : breakdownList)
			{
				List<Object[]> temp = getSharedListResult(unitList);
				for (Object[] obj : temp)
				{
					results.add(obj);
				}
			}

			for (Object[] row : results){
					String uName = (String) row[0];
					String name = (String) row[1];
					String series = (String) row[2];
					String email = findEmailByUserName(uName);
					returnList.add(new QcCustomSeriesListDTO(uName, name, series, email));
				}

			return returnList;
		}


/*	@Transactional(propagation=Propagation.REQUIRED)
	public String findEmailByUserName(String uName) throws DataAccessException {
        DetachedCriteria criteria = DetachedCriteria.forClass(NCIAUser.class);
		criteria.setProjection(Projections.property("email"));

		criteria.add(Restrictions.eq("loginName", uName.trim()));
		List<String> results = getHibernateTemplate().findByCriteria(criteria);
		if (results != null && results.size() > 0)
		{
			return results.get(0);
		}
		else
		{
			return null;
		}
	}*/

	//Since after CSM5.1 has been implemented, the Email_ID field in csm_user table has been encrypted.
	//So instead of using database pull, we use NCIASecurityManager

	public String findEmailByUserName(String uName){
		NCIASecurityManager nciaSecurityManager = (NCIASecurityManager)SpringApplicationContext.getBean("nciaSecurityManager");
        String email = nciaSecurityManager.getUserEmail(uName);
        return email;
	}


	/**
	 * update database with data in the dto
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public long update(CustomSeriesListDTO editList,
			           String userName,
			           Boolean updatedSeries) throws DataAccessException {
		CustomSeriesList seriesList = new CustomSeriesList();
		seriesList.setName(editList.getName());
		seriesList.setComment(editList.getComment());
		seriesList.setHyperlink(editList.getHyperlink());
		seriesList.setId(editList.getId());
		seriesList.setCustomSeriesListTimestamp(new Date());
		seriesList.setUserName(userName);
		List<CustomSeriesListAttributeDTO> attributeDtos = editList
				.getSeriesInstanceUidsList();

		if(updatedSeries){
			logger.debug("updatedSeries = " + updatedSeries + "  ... delete then insert... ");
			CustomSeriesList existingList = (CustomSeriesList)getHibernateTemplate().load(CustomSeriesList.class, editList.getId());
			Set<CustomSeriesListAttribute> children = existingList.getCustomSeriesListAttributes();
			logger.debug("total children to delete: " + children.size());
			existingList.getCustomSeriesListAttributes().removeAll(children);
			for(Iterator<CustomSeriesListAttribute> itr = children.iterator(); itr.hasNext();){
				getHibernateTemplate().delete((CustomSeriesListAttribute)itr.next());
			}
			getHibernateTemplate().flush();
			existingList.setName(editList.getName());
			existingList.setComment(editList.getComment());
			existingList.setHyperlink(editList.getHyperlink());
			existingList.setCustomSeriesListTimestamp(new Date());
			existingList.setUserName(userName);

			for (CustomSeriesListAttributeDTO dto : attributeDtos) {
				CustomSeriesListAttribute attr = new CustomSeriesListAttribute();
				attr.setSeriesInstanceUid(dto.getSeriesInstanceUid());
				attr.setCustomSeriesListPkId(existingList.getId());
				existingList.addChild(attr);
			}
			getHibernateTemplate().update(existingList);
		}
		else{
			logger.debug("updatedSeries = " + updatedSeries + " ... updating..");
			getHibernateTemplate().update(seriesList);
		}

		return 1L;
	}

	/**
	 * insert a new record for the custom series list
	 *
	 * @param customList
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public long insert(CustomSeriesListDTO customList,
			           String username) throws DataAccessException {
		CustomSeriesList seriesList = new CustomSeriesList();
		seriesList.setName(customList.getName());
		seriesList.setComment(customList.getComment());
		seriesList.setHyperlink(customList.getHyperlink());
		seriesList.setUserName(username);
		seriesList.setCustomSeriesListTimestamp(new Date());
		List<String> seriesUids = customList.getSeriesInstanceUIDs();

		for (String seriesInstanceUid : seriesUids) {
			CustomSeriesListAttribute attr = new CustomSeriesListAttribute();
			attr.setSeriesInstanceUid(seriesInstanceUid);
			seriesList.addChild(attr);
		}
		getHibernateTemplate().saveOrUpdate(seriesList);

		return seriesList.getId();
	}


	/**
	 * find all the shared list for a given user
	 * @param username
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<CustomSeriesListDTO> findCustomSeriesListByUser(String username) throws DataAccessException {
		List<CustomSeriesList> customSeriesList = null;
		List<CustomSeriesListDTO> returnList = null;

        DetachedCriteria criteria = DetachedCriteria.forClass(CustomSeriesList.class);

		criteria.add(Expression.eq("userName",username).ignoreCase());
		customSeriesList = getHibernateTemplate().findByCriteria(criteria);
		returnList = convertHibernateObjectToCustomSeriesListDTOList(customSeriesList);

		return returnList;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public CustomSeriesListDTO findCustomSeriesListByName(String name) throws DataAccessException {
		List<CustomSeriesList> customSeriesList = null;
		CustomSeriesListDTO returnList = null;

        DetachedCriteria criteria = DetachedCriteria.forClass(CustomSeriesList.class);
		criteria.add(Expression.eq("name",name).ignoreCase());
		customSeriesList = getHibernateTemplate().findByCriteria(criteria);
		if (customSeriesList != null && customSeriesList.size() == 0) {
			return returnList;
		}
		returnList = convertHibernateObjectToCustomSeriesListDTO(customSeriesList);

		return returnList;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<CustomSeriesListDTO> findCustomSeriesListByNameLikeSearch(String name)  throws DataAccessException {
		List<CustomSeriesList> customSeriesList = null;
		List<CustomSeriesListDTO> returnList = null;

		DetachedCriteria criteria = DetachedCriteria.forClass(CustomSeriesList.class);
		if(name.contains("*")) {
			name = name.replace("*", "");
			criteria.add(Restrictions.like("name", name +"%"));
		} else {
			criteria.add(Restrictions.ilike("name","%" + name +"%"));
		}
		customSeriesList = getHibernateTemplate().findByCriteria(criteria);
		if (customSeriesList != null && customSeriesList.size() == 0) {
			return returnList;
		}
		returnList = convertHibernateObjectToCustomSeriesListDTOList(customSeriesList);

		return returnList;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<CustomSeriesListAttributeDTO> findCustomSeriesListAttribute(
			Integer customSeriesListPkId)  throws DataAccessException {
		List<CustomSeriesListAttribute> customSeriesListAttribute = null;
		List<CustomSeriesListAttributeDTO> returnList = null;

		DetachedCriteria criteria = DetachedCriteria.forClass(CustomSeriesListAttribute.class);
		criteria.add(Restrictions.eq("customSeriesListPkId",
				customSeriesListPkId));
		customSeriesListAttribute = getHibernateTemplate().findByCriteria(criteria);
		returnList = convertHibernateObjectToCustomSeriesListAttributeDTO(customSeriesListAttribute);

		return returnList;
	}

	//////////////////////////////////PRIVATE/////////////////////////////////////////////////

	private List<CustomSeriesListDTO> convertHibernateObjectToCustomSeriesListDTOList(List<CustomSeriesList> daos) {
		List<CustomSeriesListDTO> returnList = new ArrayList<CustomSeriesListDTO>();
		if (daos != null) {
			for (CustomSeriesList dataRow : daos) {
				Date d = dataRow.getCustomSeriesListTimestamp();
				CustomSeriesListDTO dto = new CustomSeriesListDTO();
				dto.setName(dataRow.getName());
				dto.setComment(dataRow.getComment());
				dto.setHyperlink((String) dataRow.getHyperlink());
				dto.setDate(d);
				dto.setId(dataRow.getId());
				dto.setUserName(dataRow.getUserName());
				Set<CustomSeriesListAttribute> children = dataRow.getCustomSeriesListAttributes();
				List<String> seriesUidsList = new ArrayList<String>();
				for (Iterator<CustomSeriesListAttribute> iter = children
						.iterator(); iter.hasNext();) {
					CustomSeriesListAttribute attr = iter.next();
					String seriesInstanceUid = attr.getSeriesInstanceUid();
					seriesUidsList.add(seriesInstanceUid);
				}
				dto.setSeriesInstanceUIDs(seriesUidsList);
				returnList.add(dto);
			}
		}
		return returnList;
	}

	private CustomSeriesListDTO convertHibernateObjectToCustomSeriesListDTO(List<CustomSeriesList> daos) {
		List<CustomSeriesListDTO> returnList = new ArrayList<CustomSeriesListDTO>();
		//if (daos != null) {
			// this should contain only 1 record for customSeriesList
			for (int i = 0; i < daos.size(); i++) {
				CustomSeriesListDTO dto = new CustomSeriesListDTO();
				CustomSeriesList series = (CustomSeriesList) daos.get(0);
				Set<CustomSeriesListAttribute> children = series
						.getCustomSeriesListAttributes();
				List<String> seriesUidsList = new ArrayList<String>();
				for (Iterator<CustomSeriesListAttribute> iter = children
						.iterator(); iter.hasNext();) {
					CustomSeriesListAttribute attr = iter.next();
					String seriesInstanceUid = attr.getSeriesInstanceUid();
					seriesUidsList.add(seriesInstanceUid);
				}
				dto.setName(series.getName());
				dto.setComment(series.getComment());
				dto.setHyperlink(series.getHyperlink());
				dto.setDate(series.getCustomSeriesListTimestamp());
				dto.setId(series.getId());
				dto.setSeriesInstanceUIDs(seriesUidsList);
				dto.setUserName(series.getUserName());
				returnList.add(dto);
			}
		/*} else {
			throw new Exception("No record found the query.");
		}*/
		return returnList.get(0);
	}

	private List<CustomSeriesDTO> convertHibernateObjectToCustomSeriesDTO(List<GeneralSeries> daos)  {
		List<CustomSeriesDTO> returnList = new ArrayList<CustomSeriesDTO>();
		if (daos != null) {
			for (int i = 0; i < daos.size(); i++) {
				CustomSeriesDTO sd = new CustomSeriesDTO();
				GeneralSeries gs = (GeneralSeries) (daos.get(i));
				sd.setAnnotationsFlag(gs.getAnnotationsFlag() == null ? false
						: gs.getAnnotationsFlag());
				sd.setAnnotationsSize(gs.getAnnotationTotalSize());
				sd.setDescription(gs.getSeriesDesc());
				sd.setModality(gs.getModality());
				sd.setNumberImages(gs.getImageCount());
				sd.setPatientId(gs.getPatientId());
				sd.setSeriesUID(gs.getSeriesInstanceUID());
				sd.setStudyUid(gs.getStudy().getStudyInstanceUID());
				sd.setStudyPkId(gs.getStudy().getId());
				sd.setProject(gs.getStudy().getPatient().getDataProvenance()
						.getProject());
				sd.setSecurityGroup(gs.getSecurityGroup());
				returnList.add(sd);
			}
		}
		return returnList;
	}

	private List<CustomSeriesListAttributeDTO> convertHibernateObjectToCustomSeriesListAttributeDTO(
			List<CustomSeriesListAttribute> daos)  {
		List<CustomSeriesListAttributeDTO> returnList = new ArrayList<CustomSeriesListAttributeDTO>();

		if (daos != null) {
			// this should contain only 1 record for customSeriesList
			for (int i = 0; i < daos.size(); i++) {
				CustomSeriesListAttribute rowData = daos.get(i);
				CustomSeriesListAttributeDTO dto = new CustomSeriesListAttributeDTO(
						rowData.getId(), rowData.getSeriesInstanceUid());
				returnList.add(dto);
			}
		}/* else {
			throw new Exception("No record found for the query.");
		}*/
		return returnList;
	}


	private static void setAuthorizedSiteData(DetachedCriteria criteria, List<SiteData> sites){
		Disjunction disjunction = Restrictions.disjunction();

		for (SiteData sd : sites){
			Conjunction con = new Conjunction();
			con.add(Restrictions.eq("dpSiteName",sd.getSiteName()));
			con.add(Restrictions.eq("project", sd.getCollection()));
			disjunction.add(con);
		}
		criteria.add(disjunction);
	}

	private List<Object[]> getSharedListResult (List<String> seriesUids)
	{
		List<Object[]> results = new ArrayList<Object[]>();

		if (seriesUids != null && seriesUids.size() != 0){
	        DetachedCriteria criteria = DetachedCriteria.forClass(CustomSeriesListAttribute.class);
			ProjectionList projectionList = Projections.projectionList();

			projectionList.add(Projections.property("csl.userName"));
			projectionList.add(Projections.property("csl.name"));
			projectionList.add(Projections.property("seriesInstanceUid"));

			criteria.setProjection(Projections.distinct(projectionList));

			criteria.createAlias("parent", "csl");

			criteria.add(Restrictions.in("seriesInstanceUid", seriesUids));

			results = getHibernateTemplate().findByCriteria(criteria);
		}

		return results;
	}
		 /**
		 * @param toRemove
		 * @return
		 * @throws DataAccessException
		 */
		@Transactional(propagation=Propagation.REQUIRED)
		public long delete(CustomSeriesListDTO toRemove) throws DataAccessException {
				CustomSeriesList existingList = (CustomSeriesList)getHibernateTemplate().load(CustomSeriesList.class, toRemove.getId());
				getHibernateTemplate().delete(existingList);
				getHibernateTemplate().flush();
			return 1L;
		}
		public List<String> getSharedListUserNames() {
			DetachedCriteria criteria = DetachedCriteria.forClass(CustomSeriesList.class);
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("userName"));
			criteria.setProjection(Projections.distinct(projectionList));
			List<String> results = new ArrayList<String>();
			results = getHibernateTemplate().findByCriteria(criteria);
			return results;
		}


}