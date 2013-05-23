/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

select trial_dp_pk_id, image_pk_id, patient_id, study_instance_uid, series_instance_uid,
       general_series_pk_id, sop_instance_uid, submission_date, visibility,
       dicom_file_uri
from general_image gi 
where sop_instance_uid in
(
  select sop_instance_uid
  from general_image gi1 
  where sop_instance_uid in
  (
    select sop_instance_uid from general_image gi2
    where trial_dp_pk_id in ( select distinct trial_dp_pk_id from TRIAL_DATA_PROVENANCE )
    group by sop_instance_uid having count(sop_instance_uid) > 1
  )
)
group by trial_dp_pk_id, sop_instance_uid, patient_id, study_instance_uid, series_instance_uid,
         submission_date, general_series_pk_id, dicom_file_uri, visibility, image_pk_id
order by trial_dp_pk_id, sop_instance_uid, submission_date desc, patient_id, study_instance_uid,
         series_instance_uid