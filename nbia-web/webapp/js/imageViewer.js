// Displays the image in new window 
function showInNewWindow(imagename)
{
	var windowId = imagename.substring(imagename.lastIndexOf('=') + 1);
	popup = window.open (imagename, 
	                     windowId, 
	                     config='height=500,width=500, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, directories=no, status=no');
	popup.focus();
}

function setHiddenValue(imageLabel, slideshowtemplate){
	document.getElementById('MAINbody:imageForm:imageIdx').value=imageLabel; 
	document.getElementById('MAINbody:imageForm').submit();   
	window.open(slideshowtemplate, 'slideshow', 'resizable=no,scrollbars=no,width=602,height=602');
}
