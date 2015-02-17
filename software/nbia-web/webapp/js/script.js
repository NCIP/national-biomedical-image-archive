/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

function changeMenuStyle(obj, new_style) {
    obj.className = new_style;
}


function showCursor() {
    document.body.style.cursor = "hand";
}


function hideCursor() {
    document.body.style.cursor = "default";
}


function confirmDelete() {
    if (confirm("Are you sure you want to delete?")) {
    	Effect.Appear('notification',{duration: 0.25, queue: 'end'});
        return true;
    } else {
        return false;
    }
}

function checkAllInContainingTable(containedCheckbox) {
    var containerTable = findContainingTable(containedCheckbox);
    if(containerTable==null) {
        return;
    }
    
    var inputElements = containerTable.getElementsByTagName('INPUT');
    for(var z = 0; z<inputElements.length; z++) {
        if (inputElements[z].type == "checkbox") {
            inputElements[z].checked = containedCheckbox.checked;
        }
    }


}

function findContainingTable(element) {
    var elementIter = element;
    while(elementIter != null) {
        if(elementIter.nodeName == "table" || elementIter.nodeName == "TABLE" ) {
            return elementIter;
        }
        else {
            elementIter = elementIter.parentNode;
        }
    }
    return null;
}

function resetBooleanCheckbox(){
	
	document.getElementById('MAINbody:qcToolForm:data:checkAllBox').checked = false;
	//document.getElementById("checkAllBox").checked = false;
	//document.getElementById('checkAllBox').reset();
		
}

function toggleBooleanCheckAll(){

	if(document.getElementById('MAINbody:qcToolForm:data:checkAllBox').checked){
		document.getElementById('MAINbody:qcToolForm:data:checkAllBox').checked = true;
	}
		else{
			document.getElementById('MAINbody:qcToolForm:data:checkAllBox').checked = true;
	}
}


function toggleBooleanUnCheckAll(){

	if(!document.getElementById('MAINbody:qcToolForm:data:checkAllBox').checked){
		document.getElementById('MAINbody:qcToolForm:data:checkAllBox').checked = false;
	}
		else{
			document.getElementById('MAINbody:qcToolForm:data:checkAllBox').checked = false;
	}
}

function checkUncheckAll(theElement) {
	//documentation for this script at http://www.shawnolson.net/a/693/
    var theForm = theElement.form, z = 0;
    while (theForm[z] != null && theForm[z].name != "checkall") {
        if (theForm[z].type == "checkbox") {
            theForm[z].checked = theElement.checked;
        }
        z++;
    }
   // document.getElementById('checkAllBox').checked = !document.getElementById('checkAllBox').checked;
}

// Check all of the series for a given study
function checkAllSeries(theElement) {
    var table = document.getElementById("MAINbody:dataForm:studyTable");
    var studyTables = table.getElementsByTagName("table");
    for (var i = 0; i < studyTables.length; i++) {
        if (studyTables[i].className == "contentPageII") {
            var shouldCheck = false;
            var inputs = studyTables[i].getElementsByTagName("input");
            for (var j = 0; j < inputs.length; j++) {
                if (inputs[j].type == "checkbox") {
                    if (inputs[j] === theElement) {
                        shouldCheck = true;
                    }
                    if (shouldCheck) {
                        inputs[j].checked = theElement.checked;
                    }
                }
            }
        }
    }
}


// Method to select the radio button when the new query text box is selected
function selectRadio() {
    var inputs = document.getElementsByTagName("INPUT");
    var num = 0;
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].type == "radio") {
            num++;
            if (num == 2) {
                inputs[i].checked = true;
            }
        }
    }
}


function doDicomPopup() {
	var popup = window.open('/ncia/showDicom.jsf',
			    "dicom_window",
			    "height=800,width=700,scrollbars=yes,resizable=yes");
	popup.focus();
}

function doDicomTagPopup(seriesId) {
	var popup = window.open('/ncia/showDicom.jsf?seriesId='+seriesId, 
			    "dicom_window",
			    "height=800,width=700,scrollbars=yes,resizable=yes");
	popup.focus();
}
function doViewSeriesPopup(seriesId,location,url) {
	var popup = window.open('/ncia/viewSeriesPopup.jsf?seriesId='+seriesId+'&location='+location +'&url='+url, 
                            "view_series", 
                            "height=800,width=600,scrollbars=yes,resizable=yes");
    popup.focus();
}

function doViewThumbnailPopup(seriesId) {
	var popup = window.open('/ncia/viewThumbnailsPopup.jsf?seriesId='+seriesId, 
                            "view_series", 
                            "height=800,width=600,scrollbars=yes,resizable=yes");
    popup.focus();
}

function doDownloadPopup() {
    var popup = window.open('/ncia/download.jsp', 
                            "download_popup", 
                            "height=300,width=300,scrollbars=no,resizable=no");
    popup.focus();
}

function doAvailableSearchTermsPopup() {
    var popup = window.open('/ncia/availableSearchTerms.jsf', 
                            "avaiable_search_terms", 
                            "height=600,width=600,scrollbars=yes,resizable=yes,location=no");
    popup.focus();
}


function setChanged(inputField,message) {
	inputField.value=message;
	//document.write(inputField.value);
}

function checkCriteriaAndShowProgress(criteriaSize)
{
	if(criteriaSize == 0)
    {
    	if (!confirm('No search criteria has been selected, do you want to continue?'))
        {		
        	 return false;
        }
    }
   	Effect.Appear('notification',{duration: 0.25, queue: 'end'});
   	return true;	
}
/************************* BEGIN CEDARA RELATED JAVASCRIPT *****************************/

cedaraNotFound = false;

function showCedaraNotFoundPage(divToShow, goBtn, img) {
    divToShow.style.display = "block";
	goBtn.style.display = "none";
	img.style.display = "inline";
	cedaraNotFound = true;
}


function showWrongCedaraVersionPage(divToShow, goBtn, img){
    if(!cedaraNotFound) {
        divToShow.style.display = "block";
	    goBtn.style.display = "none";
	    img.style.display = "inline";
	}    
}

function showStartingCedaraPage(divToShow, btn)
{
    divToShow.style.display = "block";
    btn.style.display = "inline";
}

function sendReqToIRW(user, host, achiveValue, uid)
{
   var hidden = document.getElementById("hidden");
    //alert("seriesUids="+uid)
    var winName = window.open("", "popUp");
    hidden.uid.value = uid;
    hidden.usr.value = user;
    hidden.host.value = host ;
    //alert("host: " + host);
    hidden.archive.value=achiveValue;
    //alert("archive: " + archiveValue);
    hidden.target  = 'popUp';
    document.forms["hidden"].submit();  
    winName.close();
}



function countUserSelection(form)
{
	var count = 0; // keep track of number of series
    var studyTable = document.getElementById("MAINbody:dataForm:studyTable");
    var studyTableCheckboxes = studyTable.getElementsByTagName("input");     

    for (var i = 0; i < studyTableCheckboxes.length; i++) {
  
        //example: MAINbody:dataForm:studyTable:0:seriesTable:1:seriesSelectionCheckbox
        if (studyTableCheckboxes[i].id.match(/.*seriesSelectionCheckbox$/)) {    
	      if( studyTableCheckboxes[i].checked) { 
	          count=count + 1;
          }
        }
	}
    
    if(count == 0 ){
    	alert("Please add series to basket for visualization.");
    	return false;
    }
    else {
       form.submit();
       return true; 
    }
}


function slctVis(element) {
	var eleName = element.name;
	var checkBoxNamePrefix= eleName.substring(0,eleName.lastIndexOf(":"));
	var studyTable = document.getElementById("MAINbody:dataForm:studyTable");
	var checkBoxName = checkBoxNamePrefix+":seriesSelectionCheckbox";
	var studyTableCheckboxes = studyTable.getElementsByTagName("input");     
	    for (var i = 0; i < studyTableCheckboxes.length; i++) {
	        if (studyTableCheckboxes[i].id == checkBoxName) {
		   studyTableCheckboxes[i].checked = true;
		   alert(studyTableCheckboxes[i].checked);
		   break;
	        }
	}
	
	return true;

}


function countDataBasketSelection(form)
{
	var count = 0; // keep track of number of series
	var table = document.getElementById("MAINbody:basketForm:dataBasketDataTable");
	var inputs = table.getElementsByTagName("input");

    for (var i = 0; i < inputs.length; i++) {
       	if( inputs[i].checked) { 
	       	count=count + 1;
	    }
	}
    if(count == 0 ){
    	alert("Please select a series for visualization.");
    	return false;
    }
    else {
	    return true; 
    }
}
/************************* END CEDARA RELATED JAVASCRIPT *****************************/
function selectAllInContainingTable(containerTableName) {
    var containerTable = document.getElementById(containerTableName);
    if(containerTable==null) {
    	this.disabled="disabled"
    }
    var inputElements = containerTable.getElementsByTagName('input');
    for(var z = 0; z<inputElements.length; z++) {
        if (inputElements[z].type == "checkbox") {
            inputElements[z].checked = "yes";
        }
    }


}

/**************************Editing Workflow Item ***************************************/
function editWorkflow(val){
	
	var val;
	
	document.getElementById("hiddenName").value=val;

}

/**************************Deleting Workflow Item ***************************************/
function delWorkflow(delVal, delName, event){
	var delVal;
	var delName;
	
    if (confirm("Are you sure you want to delete workflow '"+ delName +"' ?")) {
    	document.getElementById("hiddenDel").value=delVal;
        return true;
    } else {
        return event.preventDefault();
    }
	

}

/***************************WORKFLOW RELATED JAVASCRIPT*****************************/
function cancelValidation()
{
	document.getElementById("MAINbody:createWorkFlowForm:name").value=" ";
	document.getElementById("MAINbody:createWorkFlowForm:hyperlinkWorkFlow").value=" ";
}
