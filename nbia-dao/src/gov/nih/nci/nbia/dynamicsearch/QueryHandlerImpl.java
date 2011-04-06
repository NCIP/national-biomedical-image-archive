package gov.nih.nci.nbia.dynamicsearch;

import gov.nih.nci.nbia.dao.AbstractDAO;
import gov.nih.nci.nbia.dto.StudyNumberDTO;
import gov.nih.nci.nbia.dynamicsearch.criteria.CriteriaFactory;
import gov.nih.nci.nbia.dynamicsearch.criteria.CriteriaForAuthorizedSiteData;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.lookup.StudyNumberMap;
import gov.nih.nci.nbia.search.LocalNode;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.xmlobject.Element;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.PatientSearchResultImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//the name of this object should reflect that the target of the
//query is always a Patient.
public class QueryHandlerImpl extends AbstractDAO 
                              implements QueryHandler {
	private List<DynamicSearchCriteria> searchCriteria = new ArrayList<DynamicSearchCriteria>();
	private List<DynamicSearchCriteria> originalCriteria = null;
	private List<Element> elementTree;
	private String currentNode ="";
	private String statementRelation = "";
	private Map<String, String> sourceToAliasMap = new HashMap<String, String>();
	private List<PatientSearchResult> patients;
	private List<SiteData> authorizedSiteData;
	private List<String> seriesSecurityGroups;

	private StudyNumberMap studyNumberMap = null;

	private static final String LOGICAL_OPERATOR_AND = "AND";
	private static final String LOGICAL_OPERATOR_OR = "OR";

	public void setStudyNumberMap(StudyNumberMap theStudyNumberMap) {
		this.studyNumberMap = theStudyNumberMap;
	}

	@Transactional(propagation=Propagation.REQUIRED) 	
	public void query() throws DataAccessException {
		DetachedCriteria criteria = null;
		try
		{
	        criteria = DetachedCriteria.forClass(Patient.class);

			ProjectionList projectionList = Projections.projectionList();
            projectionList.add(Projections.property("id"));
            projectionList.add(Projections.property(generateAlias(elementTree.get(2).getAlias())+".id"));
            projectionList.add(Projections.property(generateAlias(elementTree.get(3).getAlias())+".id"));
            projectionList.add(Projections.property(generateAlias(elementTree.get(0).getAlias())+".project"));
            projectionList.add(Projections.property("patientId"));
            criteria.setProjection(Projections.distinct(projectionList));

			criteria = addAllJoinKeys(criteria);
			criteria = addAllRestriction(criteria, searchCriteria);
			//add user authorized site name and collection
			CriteriaForAuthorizedSiteData casd = new CriteriaForAuthorizedSiteData();
			casd.setAuthorizedSiteData(generateAlias(elementTree.get(0).getAlias()),
					                   criteria,
					                   authorizedSiteData);
			//add this for IDRI security group
			casd.setSeriesSecurityGroups(generateAlias(elementTree.get(3).getAlias()),
					                     criteria,
					                     seriesSecurityGroups);
			criteria.addOrder(Order.asc("patientId"));
			List<Object[]> p = executeQuery(criteria);

			List<PatientSearchResult> pList = this.createPatientDTOBasedUponResultSet(p);
			this.setPatients(pList);


		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		finally
		{
			criteria = null;
			removeSeriesVisibilityCriteria();
		}
	}


	/**
	 * This method prepares all necessary conditions for Query builder method.
	 * This method must be called before calling buildQuery().
	 */
	public void setQueryCriteria(List<DynamicSearchCriteria> criteria,
			                     String stateRelation, List<SiteData> aData,
			                     List<String> securityGroups) 
	{
		try {
			TableRelationships ro = new TableRelationships();
			elementTree = ro.getRelationTree();
			createMapKeys();
			//add visibility = 1 in generalSeries level
			criteria.add(createSeriesVisibilityCriteria());
	
			searchCriteria = ro.sortTableName(criteria);
			//reset currentNode
			originalCriteria = criteria;
			//what happens here if criteria is zero length?
			currentNode = elementTree.get(1).getAlias();
			this.statementRelation = stateRelation;
			this.authorizedSiteData = aData;
			this.seriesSecurityGroups = securityGroups;
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Transactional(propagation=Propagation.REQUIRED) 
	public List<String> getPermissibleData(String packageName,
			                              String dataSource,
			                              String field)	throws DataAccessException
	{
		try {
			List<String> fieldData = new ArrayList<String>();
	        DetachedCriteria criteria = DetachedCriteria.forClass(Class.forName(packageName+"."+dataSource));
			criteria.setProjection(Projections.distinct(Property.forName(field)));
			//special case, need to make it more generic.
			if (field.equalsIgnoreCase("usMultiModality")){
				criteria.add(Restrictions.isNotNull(field));
			}
			List<String> result = getHibernateTemplate().findByCriteria(criteria);
			if (result != null && result.size() > 0)
			{
				fieldData = result;
			}
	
			return fieldData;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/////////////////////////////PRIVATE////////////////////////////////

	private List<Object[]> executeQuery(DetachedCriteria c)
	{
	    return getHibernateTemplate().findByCriteria(c);
	}
	
	
	private DetachedCriteria addAllJoinKeys(DetachedCriteria criti)
	{
		String previousKey = "";
		//add all join key first

		//Since DataProvenance.project is always in the search result,
		//the join key must entered all time.
		criti.createAlias(elementTree.get(0).getAlias(),
				          generateAlias(elementTree.get(0).getAlias()));

		for (int i = 0; i < searchCriteria.size(); i++)
		{
			String node = searchCriteria.get(i).getDataGroup();
			if (sourceToAliasMap.get(node).equalsIgnoreCase(elementTree.get(0).getAlias()))
			{
				continue;
			}
			if (!sourceToAliasMap.get(node).equalsIgnoreCase(currentNode))
			{
				List<String> nodes = new ArrayList<String>();
				nodes = getRelations(node);
				for (String str : nodes)
				{
					if (str.equalsIgnoreCase(elementTree.get(2).getAlias()))
					{
						criti.createAlias(str,
								          generateAlias(str));
						previousKey = generateAlias(str);
					}
					else
					{
						criti.createAlias(previousKey+"."+str,
								          generateAlias(str));
						previousKey = generateAlias(str);
					}
				}
			}
		}

		return criti;
	}


	/**
	 * Build up a real Hibernate Criteria object....
	 * turn each DynamicSearchCriteria object into a modification of the real
	 * Criteria object
	 */
	private DetachedCriteria addAllRestriction(DetachedCriteria criteriaToBuild,
			                                   List<DynamicSearchCriteria> dynSearchCriteriaList) throws Exception
	{
		String fieldName = "";
		if (statementRelation.equalsIgnoreCase(LOGICAL_OPERATOR_AND))
		{
			for (DynamicSearchCriteria dCriteria : dynSearchCriteriaList)
			{
			    CriteriaFactory criteriaFactory = LogicalOperatorCriteriaFactories.getCriteriaFactory(dCriteria.getOperator().getValue());
			    if (dCriteria.getDataGroup().equalsIgnoreCase(elementTree.get(1).getSource()))
			    {
			    	fieldName = dCriteria.getField();
			    }
			    else
			    {
			    	fieldName = generateAlias(sourceToAliasMap.get(dCriteria.getDataGroup()))+
					"."+
					dCriteria.getField();
			    }
		    	criteriaToBuild.add(criteriaFactory.constructCriteria(fieldName,
		    			                                              dCriteria.getValue(),
		    			                                              DataFieldTypeMap.getDataFieldType(dCriteria.getField())));
			}
		}
		else
		if (statementRelation.equalsIgnoreCase(LOGICAL_OPERATOR_OR))
		{
			Disjunction disjunction = Restrictions.disjunction();
			for (DynamicSearchCriteria dCriteria : dynSearchCriteriaList)
			{
				CriteriaFactory criteriaFactory = LogicalOperatorCriteriaFactories.getCriteriaFactory(dCriteria.getOperator().getValue());
			    if (dCriteria.getDataGroup().equalsIgnoreCase(elementTree.get(1).getSource()))
			    {
			    	fieldName = dCriteria.getField();
			    }
			    else
			    {
			    	fieldName = generateAlias(sourceToAliasMap.get(dCriteria.getDataGroup()))+
						           "."+
						           dCriteria.getField();
			    }
        		Criterion criterion = criteriaFactory.constructCriteria(fieldName,
        				                                                dCriteria.getValue(),
        				                                                DataFieldTypeMap.getDataFieldType(dCriteria.getField()));

        		if (dCriteria.getField().equalsIgnoreCase("visibility"))
        		{
        			criteriaToBuild.add(criterion);
        		}
        		else
        		{
        			disjunction.add(criterion);
        		}
			}
			criteriaToBuild.add(disjunction);
		}
		else
		{
			throw new Exception("Statement Relationship cannot be recognized!");
		}

		return criteriaToBuild;
	}

	private List<String> getRelations(String node)
	{
		List<String> relations = new ArrayList<String>();
		String placeHolder = currentNode;
		boolean record = false;
		if (node.equalsIgnoreCase(elementTree.get(0).getSource()))
		{
			relations.add(elementTree.get(0).getAlias());
			return relations;
		}
		else
		{
			for (Element ele : elementTree)
			{
				if (ele.getAlias().equalsIgnoreCase(currentNode))
				{
					record = true;
				}
				if(record)
				{
					relations.add(ele.getAlias());
				}
				if (ele.getSource().equalsIgnoreCase(node))
				{
					currentNode = ele.getAlias();
					break;
				}
			}
			//exclude current node
			relations.remove(placeHolder);
		}

		return relations;
	}

	private void createMapKeys()
	{
		for(Element e : elementTree)
		{
			sourceToAliasMap.put(e.getSource(), e.getAlias());
		}
	}

	/**
	 * Fetch all patient based on users' selection
	 *
	 * @return List of Patient DTOs
	 */
	private List<PatientSearchResult> createPatientDTOBasedUponResultSet(List<Object[]> resultSet)
	throws Exception
	{


		Map<String, PatientSearchResultImpl> patientMap = new HashMap<String, PatientSearchResultImpl>();
		List<PatientSearchResult> dtoList = new ArrayList<PatientSearchResult>();
		for (Object[] row : resultSet)
		{
			if(patientMap.get(""+row[0]) == null)
			{
				PatientSearchResultImpl pdto = createNewPatientDTO(row);
				patientMap.put(""+row[0], pdto);

		        StudyNumberDTO cachedPatientData = studyNumberMap.getStudiesForPatient(pdto.getId());

				pdto.setTotalNumberOfStudies(cachedPatientData.getStudyNumber());
	        	pdto.setTotalNumberOfSeries(cachedPatientData.getSeriesNumber());
			}
			else
			{
				PatientSearchResultImpl dto = patientMap.get(""+row[0]);
				dto.addSeriesForStudy((Integer)row[1], (Integer)row[2]);

		        StudyNumberDTO cachedPatientData = studyNumberMap.getStudiesForPatient(dto.getId());

				dto.setTotalNumberOfStudies(cachedPatientData.getStudyNumber());
				dto.setTotalNumberOfSeries(cachedPatientData.getSeriesNumber());
			}
		}

		dtoList.addAll(patientMap.values());

		Collections.sort(dtoList);

		return dtoList;
	}

	private PatientSearchResultImpl createNewPatientDTO(Object[] row)
	{
		PatientSearchResultImpl pDto = new PatientSearchResultImpl();
		pDto.setId((Integer)row[0]);
		pDto.setProject((String)row[3]);
		pDto.setSubjectId((String)row[4]);
		pDto.addSeriesForStudy((Integer)row[1], (Integer)row[2]);
		pDto.associateLocation(LocalNode.getLocalNode());
		return pDto;
	}


	private static DynamicSearchCriteria createSeriesVisibilityCriteria() {
		DynamicSearchCriteria dsc = new DynamicSearchCriteria();
		dsc.setDataGroup("GeneralSeries");
		dsc.setField("visibility");
		dsc.setValue("1");
		Operator o = new Operator();
		o.setValue("=");
		dsc.setOperator(o);
		return dsc;
	}

	private void removeSeriesVisibilityCriteria()
	{
		for (DynamicSearchCriteria cri : originalCriteria)
		{
			if (cri.getDataGroup().equalsIgnoreCase("GeneralSeries")
					&& cri.getField().equalsIgnoreCase("visibility"))
			{
				originalCriteria.remove(cri);
				break;
			}
		}
	}


	public List<PatientSearchResult> getPatients() {
		return patients;
	}


	private void setPatients(List<PatientSearchResult> patients) {
		this.patients = patients;
	}


	private static String generateAlias(String foreignKeyPropertyName) {
		return foreignKeyPropertyName+"1";
	}

}


