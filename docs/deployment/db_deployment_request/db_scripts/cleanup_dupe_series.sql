/*L
   Copyright SAIC, Ellumen and RSNA (CTP)


   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
L*/

update general_image
set general_series_pk_id = 343638016
where general_series_pk_id = 343670784;

delete from general_series
where general_series_pk_id = 343670784;






update general_image
set general_series_pk_id = 359104517
where general_series_pk_id = 359137283;

delete from general_series
where general_series_pk_id = 359137283;




delete from general_series
where general_series_pk_id = 371982338;


delete from general_series
where general_series_pk_id = 380567552;


delete from general_series
where general_series_pk_id = 372637696;



update general_image
set general_series_pk_id = 373751811
where general_series_pk_id = 373686275;

delete from general_series
where general_series_pk_id = 373686275;





delete from general_series
where general_series_pk_id = 404848642;


delete from general_series
where general_series_pk_id = 404848644;



update general_image
set general_series_pk_id = 407371779
where general_series_pk_id = 407339009;

delete from general_series
where general_series_pk_id = 407339009;



create table image_temp as select image_pk_id from general_image where general_series_pk_id = 742096896;

delete from ct_image
where image_pk_id in (select image_pk_id from image_temp);

delete from qa_status_history
where general_image_pk_id in (select image_pk_id from image_temp);

delete from group9_dicom_tags
where image_pk_id in (select image_pk_id from image_temp);

delete from general_image
where general_series_pk_id = 742096896;

delete from general_series
where general_series_pk_id = 742096896;

drop table image_temp;




create table image_temp as select image_pk_id from general_image where general_series_pk_id = 881786880;

delete from ct_image
where image_pk_id in (select image_pk_id from image_temp);

delete from qa_status_history
where general_image_pk_id in (select image_pk_id from image_temp);

delete from group9_dicom_tags
where image_pk_id in (select image_pk_id from image_temp);

delete from general_image
where general_series_pk_id = 881786880;

delete from general_series
where general_series_pk_id = 881786880;

drop table image_temp;




delete from general_series
where general_series_pk_id = 353140740;




update general_image
set general_series_pk_id = 372047875
where general_series_pk_id = 371982337;

delete from general_series
where general_series_pk_id = 371982337;





delete from general_series
where general_series_pk_id = 372637697;





update general_image
set general_series_pk_id = 387448835
where general_series_pk_id = 387416070;

delete from general_series
where general_series_pk_id = 387416070;






delete from general_series
where general_series_pk_id = 404815875;





update general_image
set general_series_pk_id = 407339014
where general_series_pk_id = 407371780;

delete from general_series
where general_series_pk_id = 407371780;





delete from general_series
where general_series_pk_id = 1252753412;
