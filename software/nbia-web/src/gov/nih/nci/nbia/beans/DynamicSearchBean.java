package gov.nih.nci.nbia.beans;

import gov.nih.nci.nbia.beans.searchresults.SearchResultBean;
import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.dynamicsearch.DataFieldParser;
import gov.nih.nci.nbia.dynamicsearch.DataFieldTypeMap;
import gov.nih.nci.nbia.dynamicsearch.DynamicSearchCriteria;
import gov.nih.nci.nbia.dynamicsearch.Operator;
import gov.nih.nci.nbia.dynamicsearch.QueryHandler;
import gov.nih.nci.nbia.dynamicsearch.TableRelationships;
import gov.nih.nci.nbia.factories.ApplicationFactory;
import gov.nih.nci.nbia.lookup.LookupManager;
import gov.nih.nci.nbia.lookup.LookupManagerFactory;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.util.SelectItemLabelComparator;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.util.StringUtil;
import gov.nih.nci.nbia.util.Ultrasound_Util;
import gov.nih.nci.nbia.xmlobject.DataGroup;
import gov.nih.nci.nbia.xmlobject.DataSource;
import gov.nih.nci.nbia.xmlobject.Element;
import gov.nih.nci.nbia.xmlobject.SourceItem;
import gov.nih.nci.ncia.search.PatientSearchResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

public class DynamicSearchBean {

	//private static Logger logger = Logger.getLogger(DynamicSearchBean.class);
	private DataGroup dataGroup;
	private Map<String, String> itemLabelTable;
	private Map<String, String> itemActualSource;
	private String initialDataGroup;
	private Map<String, List<SourceItem>> dataFieldItems;
	private Map<String, SourceItem> dataFieldSourceItems;
	//private String initialDataFieldItem;

	private String[] operands = {">","<",">=","<=","=","!=","starts with","ends with","contains","equals"};
	private String[] otherOperands =  {">","<",">=","<=","=","!="};
	private int[] stringOperandValues={6,7,8,9};
	private int[] otherOperandValues={0,1,2,3,4,5};
	private String[] stringOperands={"starts with","ends with","contains","equals"};
	private int[] resultPerPage = {10,25,50,100};
	private List<SourceItem> preLoadFieldItems;
	private String selectedDataGroup;
	private List<SelectItem> dataGroupItems;
	private List<SelectItem> fieldItems;
	private List<SelectItem> operandItems;
	private List<SelectItem> resultPerPageItems;
	private String selectedOperand;
	private String inputValue;
	private String newValue;
	private List<DynamicSearchCriteria> criteria = new ArrayList<DynamicSearchCriteria>();
	private boolean showCriteria = false;
	private boolean hasDuplicate = false;
	private UIData table;
	private String relation = "AND";
	private String selectedResultPerPage="10";
	private boolean hasPermissibleData = false;
	private List<SelectItem> permissibleData;

	private String defaultSelectValue="please select";
	private String defaultSelectLabel="--Please Select--";
	private SelectItem defaultSelectItem = new SelectItem(defaultSelectValue, defaultSelectLabel);
	private String selectedField=defaultSelectItem.getValue().toString();
	private AuthorizationManager man;
	private List<SiteData> authorizedSiteData;
	private List<String> seriesSecurityGroups;
	private static final String notPopulated = "Not Populated (NULL)";
	private String permissibleDataValue = "";

	private SourceItem defaultSourceItem = new SourceItem();

	public String getPermissibleDataValue() {
		return permissibleDataValue;
	}

	public void setPermissibleDataValue(String permissibleDataValue) {
		this.permissibleDataValue = permissibleDataValue;
	}

	public DynamicSearchBean() throws Exception
	{
		SecurityBean sb = BeanManager.getSecurityBean();
		String userName = sb.getUsername();
		man = new AuthorizationManager(userName.trim());
		authorizedSiteData = man.getAuthorizedSites();
		seriesSecurityGroups = man.getAuthorizedSeriesSecurityGroups();

		defaultSourceItem.setItemName(defaultSelectValue);
		defaultSourceItem.setItemLabel(defaultSelectLabel);

		dataGroup = DataFieldParser.getDataGroup();
		itemLabelTable = DataFieldParser.getItemLabel();
		itemActualSource = DataFieldParser.getItemActualSource();
		initialDataGroup = dataGroup.getDataSource().get(0).getSourceName();
		dataFieldItems = DataFieldParser.getSourceItem();
		dataFieldSourceItems = DataFieldParser.getDataFieldSourceItem();
		//initialDataFieldItem = dataFieldItems.get(initialDataGroup).get(0).getItemName();
		preLoadFieldItems = dataGroup.getDataSource().get(0).getSourceItem();

		fieldItems = new ArrayList<SelectItem>();
		fieldItems.add(0, defaultSelectItem);
	}

	public String getSelectedDataGroup() {
		return selectedDataGroup;
	}

	public void setSelectedDataGroup(String selectedDataGroup) {
		this.selectedDataGroup = selectedDataGroup;
	}

	public List<SelectItem> getDataGroupItems() {
		dataGroupItems = new ArrayList<SelectItem>();
		List<DataSource> dataSourceList = dataGroup.getDataSource();
		dataGroupItems.add(0, defaultSelectItem);
		for(DataSource ds : dataSourceList)
		{
			dataGroupItems.add(new SelectItem(ds.getSourceName(), ds.getSourceLabel()));
		}
		return dataGroupItems;
	}

	public void setDataGroupItems(ArrayList<SelectItem> dataGroupItems) {
		this.dataGroupItems = dataGroupItems;
	}

	public void setResultPerPageItems(ArrayList<SelectItem> items)
	{
		resultPerPageItems = items;
	}

	public List<SelectItem> getResultPerPageItems()
	{
		resultPerPageItems = new ArrayList<SelectItem>();
		for (int num : resultPerPage)
		{
			resultPerPageItems.add(new SelectItem(""+num, ""+num));
		}
		return resultPerPageItems;
	}

	public List<SelectItem> getFieldItems()
	{
		return fieldItems;
	}

	public String getSelectedField() {
		return selectedField;
	}

	public void setSelectedField(String selectedField) {
		this.selectedField = selectedField;
	}

	public List<SelectItem> getOperandItems(){

		if(selectedField.equals(defaultSelectValue)){
			operandItems = setOperatorValues(defaultSelectValue);
		}else{
		    //String fieldType = DataFieldTypeMap.getDataFieldType(selectedField);
		   // System.out.println("getOperandItems fieldType: " + fieldType);
            operandItems =  setOperatorValues(selectedField);
		}
		return operandItems;
	}

	public String getSelectedOperand() {
		return selectedOperand;
	}

	public void setSelectedOperand(String selectedOperand) {
		this.selectedOperand = selectedOperand;
	}

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public void dataGroupChanged(ValueChangeEvent event) throws Exception
	{
		newValue = (String)event.getNewValue();
		if(newValue.equals(defaultSelectValue)){
			System.out.println("no changes need");
			fieldItems = new ArrayList<SelectItem>();
			fieldItems.add(0, defaultSelectItem);
			selectedField = fieldItems.get(0).getValue().toString();
			return;
		}

		preLoadFieldItems = dataFieldItems.get(newValue);
		fieldItems = new ArrayList<SelectItem>();
		fieldItems.add(0, defaultSelectItem);
		for(SourceItem item : preLoadFieldItems)
		{
			fieldItems.add(new SelectItem(item.getItemName(), item.getItemLabel()));
		}
		//preLoadFieldItems.add();
		selectedField = fieldItems.get(0).getValue().toString();
		System.out.println("dataGroupChanged: selectedField: " + selectedField);
		//selectedField = preLoadFieldItems.get(0).getItemName();
		hasPermissibleData = false;
		invalidDate = false;
		invalidInteger = false;
		invalidDouble = false;
	}
	public void fieldItemChanged(ValueChangeEvent event) throws Exception
	{
		String newFieldValue = (String)event.getNewValue();
		operandItems =  setOperatorValues(newFieldValue);
		selectedOperand = (String)operandItems.get(0).getValue();

		this.hasPermissibleData = checkPermissibleData(newFieldValue);
		if(hasPermissibleData)
		{
			if (newValue.equals(initialDataGroup) && newFieldValue.equals(dataGroup.getDataSource().get(0).getSourceItem().get(1).getItemName()))
			{
				loadProjectName();
			}
			else
			{
				loadPermissibleData(newFieldValue);
			}


		}
		else {
			this.permissibleDataValue = "";
		}
		inputValue="";
		invalidDate = false;
		invalidInteger = false;
		invalidDouble = false;
	}

	private void loadPermissibleData(String field) throws Exception
	{
		List<String> permissibleDataItems = null;

		QueryHandler qh = (QueryHandler)SpringApplicationContext.getBean("queryHandler");
		qh.setStudyNumberMap(ApplicationFactory.getInstance().getStudyNumberMap());
		String actualSource = "";
		actualSource = itemActualSource.get(field);
		if (actualSource != null && actualSource.trim().length() > 1)
		{
			permissibleDataItems = qh.getPermissibleData(
					DataFieldParser.getPackageNames().get(newValue),
					actualSource,
					field);
		}
		else
		{
			permissibleDataItems = qh.getPermissibleData(
					DataFieldParser.getPackageNames().get(newValue),
					newValue,
					field);
		}
		permissibleData = new ArrayList<SelectItem>();
		permissibleData.add(new SelectItem(defaultSelectValue, defaultSelectLabel));
		//special case for permissible data
		if (field.equalsIgnoreCase("usMultiModality"))
		{
			List<String> m_modality_list = parseMultiModality(permissibleDataItems);
			for (String st : m_modality_list){
				permissibleData.add(new SelectItem(st, Ultrasound_Util.getTextByGivenImageTypeCode(st)));
			}
			
		}else{
			for (String st : permissibleDataItems)
			{
				permissibleData.add(new SelectItem(st==null ? notPopulated : st, st==null ? notPopulated : st));
			}
		}
		this.permissibleDataValue = defaultSelectValue;
	}

	private List<String> parseMultiModality(List<String> list){
		List<String> myList = new ArrayList<String>();
		for (String s : list){
			if (s == null){continue;}
			String[] item = s.split(",");
				for(String s2 : item){
					if (!myList.contains(s2)){
						myList.add(s2);
					}
				}
		}
		Collections.sort(myList);
		
		return myList;
	}
	
	private void loadProjectName() throws Exception
	{
		SecurityBean sb = BeanManager.getSecurityBean();
		AuthorizationManager man = sb.getAuthorizationManager();
		List<String> authorizedCollections = man.getAuthorizedCollections();

		LookupManager lookupManager = LookupManagerFactory.createLookupManager(authorizedCollections);
		List<String> collections = lookupManager.getSearchCollection();
		permissibleData = new ArrayList<SelectItem>();
		permissibleData.add(new SelectItem(defaultSelectValue, defaultSelectLabel));
		for (String st : collections)
		{
			permissibleData.add(new SelectItem(st==null ? notPopulated : st, st==null ? notPopulated : st));
		}

		Comparator<SelectItem> dssic = new SelectItemLabelComparator();
		Collections.sort(permissibleData, dssic);
	}

	private boolean checkPermissibleData(String field)
	{
		boolean hasPermissibleData = false;

		if(defaultSelectValue.equals(field)){
			return hasPermissibleData;
		}

		System.out.println("checkPermissibleData field " + field  );
		if (dataFieldSourceItems.get(field).getItemPrimaryValue() != null
				  && dataFieldSourceItems.get(field).getItemPrimaryValue().equalsIgnoreCase("true"))
		{
			hasPermissibleData = true;
		}

		return hasPermissibleData;
	}

	public void addCriteria() throws Exception
	{
		if( selectValidate()){
			return;
		}
		String operand = operands[Integer.parseInt(selectedOperand)];
		Operator op = new Operator();
		op.setValue(operand);
		DynamicSearchCriteria dsc = new DynamicSearchCriteria();
		String selectFieldType = DataFieldTypeMap.getDataFieldType(selectedField.trim());
		if(selectFieldType != null && validation(selectFieldType))
		{
			return;
		}
		dsc.setField(selectedField.trim());
		dsc.setOperator(op);
		if (inputValue == null)
		{
			inputValue="null";
		}
		dsc.setValue(inputValue.trim());
		if (permissibleDataValue != null && permissibleDataValue.length() > 0)
		{
			dsc.setValue(permissibleDataValue.trim());
		}
		if (itemActualSource.get(selectedField.trim()).length() > 1)
		{
			List<Element> elementTree = new TableRelationships().getRelationTree();
			newValue = elementTree.get(elementTree.size()-1).getSource();
		}
		if (newValue != null)
		{
			dsc.setDataGroup(newValue.trim());
		}
		else
		{
			dsc.setDataGroup(initialDataGroup.trim());
		}
		dsc.setLabel(itemLabelTable.get(selectedField.trim()));

		if (!isDuplicate(dsc))
		{
			criteria.add(dsc);
			hasDuplicate = false;
			defaultView();
		}else{
			hasDuplicate = true;
		}
		showCriteria = true;

	}

	private boolean isDuplicate(DynamicSearchCriteria newOne)
	{
		boolean duplicate = false;
		for (DynamicSearchCriteria item : criteria)
		{
			if (newOne.getField().equalsIgnoreCase(item.getField())
					&& newOne.getOperator().getValue().equals(item.getOperator().getValue())
					&& newOne.getValue().equalsIgnoreCase(item.getValue()))
			{
				duplicate = true;
				break;
			}
		}

		if (duplicate)
		{
			errorMessage = false;
		}
		return duplicate;
	}
	public List<DynamicSearchCriteria> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<DynamicSearchCriteria> criteria) {
		this.criteria = criteria;
	}

	public boolean isShowCriteria() {
		return showCriteria;
	}

	public void setShowCriteria(boolean showCriteria) {
		this.showCriteria = showCriteria;
	}

	public boolean isHasDuplicate() {
		return hasDuplicate;
	}

	public void setHasDuplicate(boolean hasDuplicate) {
		this.hasDuplicate = hasDuplicate;
	}

	public UIData getTable() {
		return table;
	}

	public void setTable(UIData table) {
		this.table = table;
	}

	public void removeItem (ActionEvent event)
	{
		DynamicSearchCriteria tmpBean = null;

	    UIComponent tmpComponent = event.getComponent();

	    while (null != tmpComponent && !(tmpComponent instanceof UIData)) {
	      tmpComponent = tmpComponent.getParent();
	    }

	    if (tmpComponent != null && (tmpComponent instanceof UIData))
	    {
	      Object tmpRowData = ((UIData) tmpComponent).getRowData();
	      if (tmpRowData instanceof DynamicSearchCriteria)
	      {
	        tmpBean = (DynamicSearchCriteria) tmpRowData;
	        criteria.remove(tmpBean);
	      }
	    }
	    if (criteria.size() == 0)
	    {
	    	showCriteria = false;
	    }
	    hasDuplicate = false;
	    defaultView();
	}


	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public void resetAction()
	{
		preLoadFieldItems = dataGroup.getDataSource().get(0).getSourceItem();
		showCriteria = false;
		hasDuplicate = false;
		errorMessage = false;
		selectedField = defaultSelectValue;
		selectedDataGroup = defaultSelectValue;
		selectedOperand = defaultSelectValue;
		inputValue = "";
		relation = "AND";
		criteria = new ArrayList<DynamicSearchCriteria>();
		defaultView();
		this.errorMessage = false;
		this.setHasDuplicate(false);
		invalidDate = false;
		invalidInteger = false;
		invalidDouble = false;
	}

	public String submitSearch() throws Exception
	{
		String returnValue = "submitSearch";

		QueryHandler qh = (QueryHandler)SpringApplicationContext.getBean("queryHandler");
		qh.setStudyNumberMap(ApplicationFactory.getInstance().getStudyNumberMap());
		qh.setQueryCriteria(criteria, relation, authorizedSiteData, seriesSecurityGroups);
		qh.query();
		List<PatientSearchResult> patients = qh.getPatients();

		populateSearchResults(patients);

		return returnValue;
	}

	private void populateSearchResults(List<PatientSearchResult> patients) throws Exception {
		SearchResultBean srb = BeanManager.getSearchResultBean();
        srb.setPatientResults(patients);
    	srb.setResultsPerPage(new Integer(selectedResultPerPage));
	}


	public String getSelectedResultPerPage() {
		return selectedResultPerPage;
	}

	public void setSelectedResultPerPage(String selectedResultPerPage) {
		this.selectedResultPerPage = selectedResultPerPage;
	}

	public boolean isHasPermissibleData() {
		return hasPermissibleData;
	}

	public void setHasPermissibleData(boolean hasPermissibleData) {
		this.hasPermissibleData = hasPermissibleData;
	}

	public List<SelectItem> getPermissibleData() {
		return permissibleData;
	}

	public void setPermissibleData(List<SelectItem> permissibleData) {
		this.permissibleData = permissibleData;
	}

	public boolean selectValidate(){
	 	errorMessage=false;
	 	if(hasPermissibleData)
	 	{
	 	 	if(this.permissibleDataValue != null &&
	 	 	   this.permissibleDataValue.equals(this.defaultSelectValue)){
			 	errorMessage=true;
			}
	 	}
	 	else
	 	{
	 		if(StringUtil.isEmptyTrim(inputValue)){
	 			errorMessage=true;
	 		}
	 	}
		if(selectedOperand.equals(this.defaultSelectValue)){
			errorMessage=true;
		}
		if(selectedField.equals(defaultSelectValue)){
		 	errorMessage= true;
		}
		if (errorMessage)
		{
			this.setHasDuplicate(false);
			invalidDate = false;
			invalidInteger = false;
			invalidDouble = false;

		}
		System.out.println("inputValue: " + inputValue + " errorMessage: " + errorMessage);
		return errorMessage;
	}

	public boolean validation(String type)
	{
		boolean returnBoolean = false;
		invalidDate = false;
		invalidInteger = false;
		invalidDouble = false;
		if (type.equalsIgnoreCase("java.util.Date"))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

			try
			{
				sdf.setLenient(false);
				sdf.parse(inputValue);
			}
			catch (Exception e)
			{
				invalidDate = true;
				returnBoolean = invalidDate;
			}
		}
		else if (type.equalsIgnoreCase("java.lang.Integer"))
		{
			try
			{
				new Integer(inputValue);
			}catch (Exception e)
			{
				invalidInteger = true;
				returnBoolean = invalidInteger;
			}
		}
		else if (type.equalsIgnoreCase("java.lang.Double"))
		{
			try
			{
				new Double(inputValue);
			}catch(Exception e)
			{
				invalidDouble = true;
				returnBoolean = invalidDouble;
			}
		}

		if (returnBoolean )
		{
			this.setHasDuplicate(false);
		}

		return returnBoolean;
	}

	public boolean isInvalidDate()
	{
		return invalidDate;
	}

	public boolean isInvalidInteger()
	{
		return invalidInteger;
	}

	public boolean isInvalidDouble()
	{
		return invalidDouble;
	}

	public boolean isErrorMessage(){
		return errorMessage;
	}

	public int getCriteriaLength()
	{
		return criteria.size();
	}

	private void defaultView(){
		dataGroupItems = new ArrayList<SelectItem>();
		List<DataSource> dataSourceList = dataGroup.getDataSource();

		dataGroupItems.add(0, defaultSelectItem);
		for(DataSource ds : dataSourceList){
			dataGroupItems.add(new SelectItem(ds.getSourceName(), ds.getSourceLabel()));
		}
		inputValue="";
		fieldItems = new ArrayList<SelectItem>();
		fieldItems.add(0, defaultSelectItem);
		selectedField = defaultSelectValue;
		selectedDataGroup = defaultSelectValue;
		hasPermissibleData=false;
		errorMessage=false;
		invalidDate = false;
		invalidInteger = false;
		invalidDouble = false;
		permissibleDataValue="";

	}
	private List<SelectItem> setOperatorValues(String selectedValue){
		List<SelectItem> operatorList = new ArrayList<SelectItem>();
		if(selectedValue.equals(defaultSelectValue)){
			operatorList.add(0, defaultSelectItem);
			return operatorList;
		}
		String fieldType = DataFieldTypeMap.getDataFieldType(selectedValue);
		//System.out.println("setOperatorValues: fieldType: " + fieldType);
		if(fieldType.equalsIgnoreCase("java.lang.String")){
			if(hasPermissibleData)
			{
				if (selectedValue.equalsIgnoreCase("usMultiModality"))
				{
					operatorList.add(new SelectItem(""+stringOperandValues[2], stringOperands[2]));
				}else{
					operatorList.add(new SelectItem(""+stringOperandValues[3], stringOperands[3]));
				}
			}
			else
			{
				for(int i=0; i< stringOperands.length; i++){
					operatorList.add(new SelectItem(""+stringOperandValues[i], stringOperands[i]));
				}
			}
		}else{
			for(int i=0; i< otherOperands.length; i++){
				operatorList.add(new SelectItem(""+otherOperandValues[i], otherOperands[i]));
			}
		}
		operatorList.add(0, defaultSelectItem);
		return operatorList;
	}
	private boolean errorMessage=false;
	private boolean invalidDate = false;
	private boolean invalidInteger = false;
	private boolean invalidDouble = false;
}
