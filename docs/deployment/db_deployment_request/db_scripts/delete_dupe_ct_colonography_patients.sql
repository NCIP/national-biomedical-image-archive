/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

delete from ct_image
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 548);

delete from qa_status_history
where general_image_pk_id in (select image_pk_id from general_image where patient_pk_id = 548);

delete from group9_dicom_tags
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 548);

delete from general_image  
where patient_pk_id = 548;

delete from annotation  
where general_series_pk_id in (select general_series_pk_id from general_series where patient_pk_id = 548);

delete from general_series  
where patient_pk_id = 548;

delete from study 
where patient_pk_id = 548;

delete from patient 
where patient_pk_id = 548;










delete from ct_image
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 553);

delete from qa_status_history
where general_image_pk_id in (select image_pk_id from general_image where patient_pk_id = 553);

delete from group9_dicom_tags
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 553);

delete from general_image  
where patient_pk_id = 553;

delete from annotation  
where general_series_pk_id in (select general_series_pk_id from general_series where patient_pk_id = 553);

delete from general_series  
where patient_pk_id = 553;

delete from study 
where patient_pk_id = 553;

delete from patient 
where patient_pk_id = 553;












delete from ct_image
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 579);

delete from qa_status_history
where general_image_pk_id in (select image_pk_id from general_image where patient_pk_id = 579);

delete from group9_dicom_tags
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 579);

delete from general_image  
where patient_pk_id = 579;

delete from annotation  
where general_series_pk_id in (select general_series_pk_id from general_series where patient_pk_id = 579);

delete from general_series  
where patient_pk_id = 579;

delete from study 
where patient_pk_id = 579;

delete from patient 
where patient_pk_id = 579;







delete from ct_image
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 687);

delete from qa_status_history
where general_image_pk_id in (select image_pk_id from general_image where patient_pk_id = 687);

delete from group9_dicom_tags
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 687);

delete from general_image  
where patient_pk_id = 687;

delete from annotation  
where general_series_pk_id in (select general_series_pk_id from general_series where patient_pk_id = 687);

delete from general_series  
where patient_pk_id = 687;

delete from study 
where patient_pk_id = 687;

delete from patient 
where patient_pk_id = 687;











delete from ct_image
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 705);

delete from qa_status_history
where general_image_pk_id in (select image_pk_id from general_image where patient_pk_id = 705);

delete from group9_dicom_tags
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 705);

delete from general_image  
where patient_pk_id = 705;

delete from annotation  
where general_series_pk_id in (select general_series_pk_id from general_series where patient_pk_id = 705);

delete from general_series  
where patient_pk_id = 705;

delete from study 
where patient_pk_id = 705;

delete from patient 
where patient_pk_id = 705;