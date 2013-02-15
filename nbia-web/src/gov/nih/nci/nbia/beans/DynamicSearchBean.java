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
	protected DataGroup dataGroup;
	protected Map<String, String> itemLabelTable;
	protected Map<String, String> itemActualSource;
	protected String initialDataGroup;
	protected Map<String, List<SourceItem>> dataFieldItems;
	protected Map<String, SourceItem> dataFieldSourceItems;
	//protected String initialDataFieldItem;

	protected String[] operands = {">","<",">=","<=","=","!=","starts with","ends with","contains","equals"};
	protected String[] otherOperands =  {">","<",">=","<=","=","!="};
	protected int[] stringOperandValues={6,7,8,9};
	protected int[] otherOperandValues={0,1,2,3,4,5};
	protected String[] stringOperands={"starts with","ends with","contains","equals"};
	protected int[] resultPerPage = {10,25,50,100};
	protected List<SourceItem> preLoadFieldItems;
	protected String selectedDataGroup;
	protected List<SelectItem> dataGroupItems;
	protected List<SelectItem> fieldItems;
	protected List<SelectItem> modalityFieldItems;
	protected List<SelectItem> operandItems;
	protected List<SelectItem> resultPerPageItems;
	protected String selectedOperand;
	protected String inputValue;
	protected String newValue;
	protected List<DynamicSearchCriteria> criteria = new ArrayList<DynamicSearchCriteria>();
	protected boolean showCriteria = false;
	protected boolean hasDuplicate = false;
	protected UIData table;
	protected String relation = "AND";
	protected String selectedResultPerPage="10";
	protected boolean hasPermissibleData = false;
	protected boolean isMr=false;
	protected List<SelectItem> permissibleData;

	protected String defaultSelectValue="please select";
	protected String defaultSelectLabel="--Please Select--";
	protected SelectItem defaultSelectItem = new SelectItem(defaultSelectValue, defaultSelectLabel);
	protected String selectedField=defaultSelectItem.getValue().toString();
	protected String modalitySelectedField=defaultSelectItem.getValue().toString();
	protected AuthorizationManager man;
	protected List<SiteData> authorizedSiteData;
	protected List<String> seriesSecurityGroups;
	protected static final String notPopulated = "Not Populated (NULL)";
	protected String permissibleDataValue = "";

	protected SourceItem defaultSourceItem = new SourceItem();

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
//		modalityFieldItems = new ArrayList<SelectItem>();
//		modalityFieldItems.add(0, defaultSelectItem);
//		modalityFieldItems.add(1, new SelectItem("Image Type", "Image Type" ));
//		modalityFieldItems.add(2, new SelectItem("Scanning Sequence", "Scanning Sequence" ));
//		modalityFieldItems.add(3, new SelectItem("Sequence Variant", "Sequence Variant" ));
//		modalityFieldItems.add(4, new SelectItem("Repetition Time", "Repetition Time" ));
//		modalityFieldItems.add(5, new SelectItem("Echo Time", "Echo Time" ));
//		modalityFieldItems.add(6, new SelectItem("Inversion Time", "Inversion Time" ));
//		modalityFieldItems.add(7, new SelectItem("Sequence Name", "Sequence Name" ));
//		modalityFieldItems.add(8, new SelectItem("maged Nucleus", "maged Nucleus" ));
//		modalityFieldItems.add(9, new SelectItem("Magnetic Field Strength", "Magnetic Field Strength" ));
//		modalityFieldItems.add(10, new SelectItem("SAR", "SAR" ));
//		modalityFieldItems.add(11, new SelectItem("dB/dt", "dB/dt" ));
//		modalityFieldItems.add(12, new SelectItem("Trigger Time", "Trigger Time" ));
//		modalityFieldItems.add(13, new SelectItem("Angio Flag", "Angio Flag" ));
	}

	public String getSelectedDataGroup() {
		return selectedDataGroup;
	}

	public void setSelectedDataGroup(String selectedDataGroup) {
		this.selectedDataGroup = selectedDataGroup;
	}

	public List<SelectItem> getDataGroupItems() {
		System.out.println("!!!!called getDataGroupItems");
		dataGroupItems = new ArrayList<SelectItem>();
		List<DataSource> dataSourceList = dataGroup.getDataSource();
		dataGroupItems.add(0, defaultSelectItem);
		for(DataSource ds : dataSourceList)
		{
			dataGroupItems.add(new SelectItem(ds.getSourceName(), ds.getSourceLabel()));
		}
		
		if (isMr) {
			dataGroupItems.add(new SelectItem("MR", "MR"));
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
	
	public List<SelectItem> getModalityFieldItems()
	{
		return modalityFieldItems;
	}

	public String getSelectedField() {
		return selectedField;
	}

	public void setSelectedField(String selectedField) {
		this.selectedField = selectedField;
	}
	
	public String getModalitySelectedField() {
		return modalitySelectedField;
	}

	public void setModalitySelectedField(String modalitySelectedField) {
		this.modalitySelectedField = modalitySelectedField;
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
		System.out.println("!!!called dataGroupChanged");
		newValue = (String)event.getNewValue();
		if (!newValue.equals("MR")) {
			isMr = false;
		}
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
			if (newValue.equals(initialDataGroup) && newFieldValue.equals(dataGroup.getDataSource().get(0).getSourceItem().get(0).getItemName()))
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
	
	public void modalityFieldItemChanged(ValueChangeEvent event) throws Exception
	{
		String newFieldValue = (String)event.getNewValue();
		//For demo purpose
		
		for(int i=0; i< stringOperands.length; i++){
			operandItems.add(new SelectItem(""+stringOperandValues[i], stringOperands[i]));
		}
		
		operandItems.add(0, defaultSelectItem);
		selectedOperand = (String)operandItems.get(0).getValue();
//		newFieldValue = "Image Type";
//		operandItems =  setOperatorValues(newFieldValue);
//		selectedOperand = (String)operandItems.get(0).getValue();
//
//		this.hasPermissibleData = checkPermissibleData(newFieldValue);
//		if(hasPermissibleData)
//		{
//			if (newValue.equals(initialDataGroup) && newFieldValue.equals(dataGroup.getDataSource().get(0).getSourceItem().get(1).getItemName()))
//			{
//				loadProjectName();
//			}
//			else
//			{
//				loadPermissibleData(newFieldValue);
//			}
//
//
//		}
//		else {
//			this.permissibleDataValue = "";
//		}
		permissibleDataValue = "";
		inputValue="";
		invalidDate = false;
		invalidInteger = false;
		invalidDouble = false;
	}


	protected void loadPermissibleData(String field) throws Exception
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

	protected List<String> parseMultiModality(List<String> list){
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

	protected void loadProjectName() throws Exception
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

	protected boolean checkPermissibleData(String field)
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
			if (permissibleDataValue.equals("MR")) {
				isMr=true;
			}
			else isMr=false;
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
		System.out.println("get selected field="+ selectedField.trim());		
		if (!isMr) {		
		dsc.setLabel(itemLabelTable.get(selectedField.trim()));
		}
		else { 
			
			if (permissibleDataValue.equals("MR")) {
				dsc.setLabel(itemLabelTable.get(selectedField.trim()));
			}
			else
			//temp code for demo needs to put into itemLabelTable
			dsc.setLabel(modalitySelectedField.trim());
		}
		
		if (!isDuplicate(dsc))
		{
			criteria.add(dsc);
			hasDuplicate = false;
			if (!isMr)
			{			
			defaultView();
			}
			else {
				modalityView();
			}
		}else{
			hasDuplicate = true;
		}
		showCriteria = true;

	}

	protected boolean isDuplicate(DynamicSearchCriteria newOne)
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

	protected void populateSearchResults(List<PatientSearchResult> patients) throws Exception {
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
	
	public boolean isModMR() {
		return isMr;
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

	protected void defaultView(){
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
	
	protected void modalityView(){
System.out.println("!!!!!!MR Modality view");
		dataGroupItems = new ArrayList<SelectItem>();
		List<DataSource> dataSourceList = dataGroup.getDataSource();

		dataGroupItems.add(0, defaultSelectItem);
		for(DataSource ds : dataSourceList){
			dataGroupItems.add(new SelectItem(ds.getSourceName(), ds.getSourceLabel()));
		}
		
		dataGroupItems.add(new SelectItem("MR", "MR"));
		inputValue="";
		modalityFieldItems = new ArrayList<SelectItem>();
		modalityFieldItems.add(0, defaultSelectItem);
		modalityFieldItems.add(1, new SelectItem("Image Type", "Image Type" ));
		modalityFieldItems.add(2, new SelectItem("Scanning Sequence", "Scanning Sequence" ));
		modalityFieldItems.add(3, new SelectItem("Sequence Variant", "Sequence Variant" ));
		modalityFieldItems.add(4, new SelectItem("Repetition Time", "Repetition Time" ));
		modalityFieldItems.add(5, new SelectItem("Echo Time", "Echo Time" ));
		modalityFieldItems.add(6, new SelectItem("Inversion Time", "Inversion Time" ));
		modalityFieldItems.add(7, new SelectItem("Sequence Name", "Sequence Name" ));
		modalityFieldItems.add(8, new SelectItem("maged Nucleus", "maged Nucleus" ));
		modalityFieldItems.add(9, new SelectItem("Magnetic Field Strength", "Magnetic Field Strength" ));
		modalityFieldItems.add(10, new SelectItem("SAR", "SAR" ));
		modalityFieldItems.add(11, new SelectItem("dB/dt", "dB/dt" ));
		modalityFieldItems.add(12, new SelectItem("Trigger Time", "Trigger Time" ));
		modalityFieldItems.add(13, new SelectItem("Angio Flag", "Angio Flag" ));
		modalitySelectedField = defaultSelectValue;
		selectedDataGroup = "MR";
		hasPermissibleData=false;
		errorMessage=false;
		invalidDate = false;
		invalidInteger = false;
		invalidDouble = false;
		isMr = true;
		permissibleDataValue="";

	}
	protected List<SelectItem> setOperatorValues(String selectedValue){
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

	protected boolean errorMessage=false;
	protected boolean invalidDate = false;
	protected boolean invalidInteger = false;
	protected boolean invalidDouble = false;
}
