-- set two images from RIDER PET CT to visible
update general_image
set visibility = '1', curation_timestamp=CURRENT_TIMESTAMP()
where  image_pk_id in (1332303191,1332303210, 1332838400);


