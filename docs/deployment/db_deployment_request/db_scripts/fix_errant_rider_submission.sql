/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

update study
set age_group = '050Y'
where study_instance_uid = '1.3.6.1.4.1.9328.50.1.2';



update general_series
set age_group = '050Y'
where series_instance_uid = '1.3.6.1.4.1.9328.50.1.3';

update general_series
set visibility = 1
where series_instance_uid = '1.3.6.1.4.1.9328.50.1.3';

update general_image
set dicom_file_uri = '/usr/local/tomcat-4.1.30/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.1.2/1.3.6.1.4.1.9328.50.1.1.dcm'
where sop_instance_uid = '1.3.6.1.4.1.9328.50.1.1';

update general_image
set submission_date = '2005-09-28' 
where sop_instance_uid = '1.3.6.1.4.1.9328.50.1.1';

update general_image
set dicom_size = 1303520.000000
where sop_instance_uid = '1.3.6.1.4.1.9328.50.1.1';

update general_image
set visibility = 1
where sop_instance_uid = '1.3.6.1.4.1.9328.50.1.1';


update general_image
set dicom_file_uri = '/usr/local/tomcat-4.1.30/webapps/NCICBIMAGE/documents/1.3.6.1.4.1.9328.50.1.2/1.3.6.1.4.1.9328.50.1.5.dcm'
where sop_instance_uid = '1.3.6.1.4.1.9328.50.1.5';

update general_image
set submission_date = '2005-09-28' 
where sop_instance_uid = '1.3.6.1.4.1.9328.50.1.5';

update general_image
set dicom_size = 1303518.000000
where sop_instance_uid = '1.3.6.1.4.1.9328.50.1.5';

update general_image
set visibility = 1
where sop_instance_uid = '1.3.6.1.4.1.9328.50.1.5';

