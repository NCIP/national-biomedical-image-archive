/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

-- clean up data submitted by ctp and has no connection with general series or images
delete from annotation where general_series_pk_id is null and annotation_type='xml';

-- remove annotation files for VC - WRAMC 
delete  from annotation where general_series_pk_id is null and annotation_type='zip';

-- move to idri, these cases were moved to lidc by mistake
update patient set trial_dp_pk_id=8 where  patient_id in (
'1.3.6.1.4.1.9328.50.3.0441',
'1.3.6.1.4.1.9328.50.3.0483',
'1.3.6.1.4.1.9328.50.3.0484',
'1.3.6.1.4.1.9328.50.3.0651');

update general_image set trial_dp_pk_id=8, project='IDRI' where  patient_id in (
'1.3.6.1.4.1.9328.50.3.0441',
'1.3.6.1.4.1.9328.50.3.0483',
'1.3.6.1.4.1.9328.50.3.0484',
'1.3.6.1.4.1.9328.50.3.0651');

update general_series set security_group=null where  patient_id in (
'1.3.6.1.4.1.9328.50.3.0441',
'1.3.6.1.4.1.9328.50.3.0483',
'1.3.6.1.4.1.9328.50.3.0484',
'1.3.6.1.4.1.9328.50.3.0651');


-- move to lidc, these two cases were left out by mistake
update patient set trial_dp_pk_id=6 where  patient_id in (
'1.3.6.1.4.1.9328.50.3.0495');

update general_image set trial_dp_pk_id=6, project='LIDC' where  patient_id in (
'1.3.6.1.4.1.9328.50.3.0495');

update general_series set security_group=null where  patient_id in (
'1.3.6.1.4.1.9328.50.3.0495');
