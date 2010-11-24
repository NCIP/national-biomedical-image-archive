update general_image 
set general_series_pk_id = 403111936
where general_series_pk_id = 403144704;

update general_image 
set study_pk_id = 403046400
where study_pk_id = 403079168;

delete from general_series 
where general_series_pk_id = 403144704;

delete from study 
where study_pk_id = 403079168;