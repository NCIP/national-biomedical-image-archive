/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

delete from ct_image
where ct_image_pk_id in 
   (select c.ct_image_pk_id 
    from ct_image c, general_image g 
    where (c.image_pk_id = g.image_pk_id) and g.patient_pk_id in (select patient_pk_id from patient where trial_dp_pk_id = 579764224));
  
delete from qa_status_history
where qa_status_history_pk_id in 
   (select q.qa_status_history_pk_id
    from qa_status_history q, general_image g 
    where (q.general_image_pk_id = g.image_pk_id) and g.patient_pk_id in (select patient_pk_id from patient where trial_dp_pk_id = 579764224));
  
delete from general_image gi where gi.patient_pk_id in (select patient_pk_id from patient where trial_dp_pk_id = 579764224);
  
delete from general_series gs where gs.patient_pk_id in (select patient_pk_id from patient where trial_dp_pk_id = 579764224);  
  
delete from study s where s.patient_pk_id in (select patient_pk_id from patient where trial_dp_pk_id = 579764224); 

delete from patient p where p.patient_pk_id in (select patient_pk_id from patient where trial_dp_pk_id = 579764224);
   
  
delete from trial_data_provenance where trial_dp_pk_id = 579764224;