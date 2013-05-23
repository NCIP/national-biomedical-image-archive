/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

ALTER TABLE GENERAL_IMAGE ADD CONSTRAINT SOP_INSTANCE_UID_UNIQUE
UNIQUE(SOP_INSTANCE_UID);
COMMIT;