/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

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