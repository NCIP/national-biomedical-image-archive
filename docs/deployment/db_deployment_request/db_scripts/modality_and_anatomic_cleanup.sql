/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

update general_series
set modality = 'HISTOPATHOLOGY'
where modality = 'OT';

update general_series
set modality = 'HISTOPATHOLOGY'
where modality = 'Histopathology';

update general_series
set body_part_examined = 'BRAIN'
where body_part_examined = 'BRAIN WITH _WITH';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'CHEST - PA AND L';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'CHEST 1B';

update general_series
set body_part_examined =  'CHEST'
where body_part_examined ='CHEST ABDOMEN PE';

update general_series
set body_part_examined = 'HEAD'
where body_part_examined = 'CT CEREBRUM';

update general_series
set body_part_examined = 'HEAD'
where body_part_examined = 'CT CERERBRUM';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'CT CHEST';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'CT, CHEST, W CON';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'CT_ CHEST_ W CON';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'CT_ CHEST_ W_O C';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'CT_CHEST_W CONTR';

update general_series
set body_part_examined = 'BRAIN'
where body_part_examined = 'E_1 MRI BRAIN';

update general_series
set body_part_examined = 'BRAIN'
where body_part_examined = 'IP DYNAMIC ENHAN';

update general_series
set body_part_examined = 'BRAIN'
where body_part_examined = 'IP PERFUSION';

update general_series
set body_part_examined = 'BRAIN'
where body_part_examined = 'MRI BRAIN_PERFUS';

update general_series
set body_part_examined = 'BRAIN'
where body_part_examined = 'MRI BRAIN _IP_';

update general_series
set body_part_examined = 'BRAIN'
where body_part_examined = 'MRI BRAIN PERFUS';


update general_series
set body_part_examined = 'BRAIN'
where body_part_examined = 'MRI BRAIN';

update general_series
set body_part_examined = 'BRAIN'
where body_part_examined = 'MRA BRAIN';

update general_series
set body_part_examined = 'RECTUM'
where body_part_examined = 'NONE';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'RIB';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'TSPINE';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'TRT PLANNING CHE';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'THORAX_2_CAP _AD';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'THORAX_1_ROUTINE';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'THORAX^6_ROUTINE';

update general_series
set body_part_examined = 'CHEST'
where body_part_examined = 'THORAX^1_ROUTINE';

update general_series
set body_part_examined = 'BRAIN'
where body_part_examined = 'UNDEFINED';

update general_series
set body_part_examined = 'NOT SPECIFIED'
where body_part_examined = 'UNKNOWN';