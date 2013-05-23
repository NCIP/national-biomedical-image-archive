/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

create table series_temp as select general_series_pk_id from general_series where study_pk_id = 407502851;

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
where study_pk_id = 407502851; 

delete from study 
where study_pk_id = 407502851;

drop table series_temp;
drop table image_temp;