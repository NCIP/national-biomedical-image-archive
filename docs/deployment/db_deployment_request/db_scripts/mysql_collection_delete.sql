/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

create table patient_temp as select patient_pk_id from patient where trial_dp_pk_id = 1300725760;

create table study_temp as select study_pk_id from study where patient_pk_id in (select * from patient_temp);

create table series_temp as select general_series_pk_id from general_series where study_pk_id in (select * from study_temp);

create table image_temp as select image_pk_id from general_image where general_series_pk_id in (select * from series_temp);


delete from ct_image
where image_pk_id in (select image_pk_id from image_temp);

delete from qa_status_history
where general_image_pk_id in (select image_pk_id from image_temp);

delete from group9_dicom_tags
where image_pk_id in (select image_pk_id from image_temp);

delete from general_image  
where general_series_pk_id in (select general_series_pk_id from series_temp);

delete from annotation  
where general_series_pk_id in (select general_series_pk_id from series_temp);

delete from general_series  
where study_pk_id in (select study_pk_id from study_temp); 

delete from study 
where study_pk_id in (select study_pk_id from study_temp);

delete from patient 
where patient_pk_id in (select patient_pk_id from patient_temp);

delete from trial_data_provenance where trial_dp_pk_id = 1300725760;

drop table study_temp;
drop table series_temp;
drop table image_temp;
drop table patient_temp;