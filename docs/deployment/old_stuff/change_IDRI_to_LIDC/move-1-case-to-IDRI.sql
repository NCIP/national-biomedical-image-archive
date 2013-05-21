-- move this case back to idri per data owner request
update patient set trial_dp_pk_id=8 where  patient_id='1.3.6.1.4.1.9328.50.3.0496';

update general_image set trial_dp_pk_id=8, project='IDRI' where  patient_id='1.3.6.1.4.1.9328.50.3.0496';