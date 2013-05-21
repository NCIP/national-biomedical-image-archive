-- Delete non-exist user data
DELETE
  FROM query_history_attribute
 WHERE query_history_pk_id IN (SELECT query_history_pk_id
                               FROM query_history
                              WHERE user_id NOT IN (SELECT user_id
                                                      FROM csm_user));
													  
DELETE FROM query_history
      WHERE user_id NOT IN (SELECT user_id
                              FROM csm_user);

DELETE
  FROM saved_query_attribute
 WHERE saved_query_pk_id IN (SELECT saved_query_pk_id
                               FROM saved_query
                              WHERE user_id NOT IN (SELECT user_id
                                                      FROM csm_user));

DELETE FROM saved_query
      WHERE user_id NOT IN (SELECT user_id
                              FROM csm_user);