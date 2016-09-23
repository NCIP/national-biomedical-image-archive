/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

// Displays the image in new window 
function showInNewWindow(imagename)
{
	var windowId = imagename.substring(imagename.lastIndexOf('=') + 1);
	popup = window.open (imagename, 
	                     windowId, 
	                     config='height=500,width=500, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, directories=no, status=no');
	popup.focus();
}
