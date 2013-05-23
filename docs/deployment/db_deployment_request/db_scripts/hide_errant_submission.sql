/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

update general_image
set visibility = 0
where series_instance_uid = '1.3.6.1.4.1.9328.50.1.3';

update general_series
set visibility = 0
where series_instance_uid = '1.3.6.1.4.1.9328.50.1.3';