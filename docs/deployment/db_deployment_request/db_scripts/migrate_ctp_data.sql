/*
select concat('/data/dataset_p01/',substring(dicom_file_uri from 34))
from general_image
where dicom_file_uri like '%CTP_data%'  
*/

update general_image
set dicom_file_uri = concat('/data/dataset_p01/',substring(dicom_file_uri from 34))
where dicom_file_uri like '%CTP_data%';  

update annotation
set file_path = concat('/data/dataset_p01/',substring(file_path from 34))
where file_path like '%CTP_data%'; 