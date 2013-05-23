/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

-- creat two new record for "Pilot RIDER//MSKCC" and "Pilot RIDER//MDACC"
INSERT INTO trial_data_provenance 
(TRIAL_DP_PK_ID, DP_SITE_NAME, DP_SITE_ID, PROJECT) 
VALUES (21, 'MSKCC', '6', 'RIDER Pilot');

INSERT INTO trial_data_provenance 
(TRIAL_DP_PK_ID, DP_SITE_NAME, DP_SITE_ID, PROJECT) 
VALUES (22, 'MDACC', '4', 'RIDER Pilot');

INSERT INTO trial_data_provenance 
(TRIAL_DP_PK_ID, DP_SITE_NAME, DP_SITE_ID, PROJECT) 
VALUES (23, 'Novartis', '21', 'RIDER Pilot');

-- Update PATIENT table change RIDER//MSKCC(1) to "Pilot RIDER//MSKCC" (11) (total 130 patients)
update patient set trial_dp_pk_id=21 where trial_dp_pk_id=1;

-- Update PATIENT table change RIDER//MDACC(2) to "Pilot RIDER//MDACC" (12) (total 243 patients)
update patient set trial_dp_pk_id=22 where trial_dp_pk_id=2;

-- Update PATIENT table change RIDER//Novartis() to "Pilot RIDER//Novartis" (684982272) (total 16 patients)
update patient set trial_dp_pk_id=23 where trial_dp_pk_id=684982272;


-- Update GENERAL_IMAGE table change RIDER//MSKCC(1) to "Pilot RIDER//MSKCC" (11) (total 67796 images)
update general_image set project='RIDER Pilot' where trial_dp_pk_id=1;
update general_image set trial_dp_pk_id=21 where trial_dp_pk_id=1;

-- Update PATIENT table change RIDER//MDACC(2) to "Pilot RIDER//MDACC" (12) (total 376830 images)
update general_image set project='RIDER Pilot' where trial_dp_pk_id=2;
update general_image set trial_dp_pk_id=22 where trial_dp_pk_id=2;

-- Update PATIENT table change RIDER//Novartis() to "Pilot RIDER//Novartis" (684982272) (total 36966 images)
update general_image set project='RIDER Pilot' where trial_dp_pk_id=684982272;
update general_image set trial_dp_pk_id=23 where trial_dp_pk_id=684982272;
