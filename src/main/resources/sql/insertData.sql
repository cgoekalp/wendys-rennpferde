-- insert initial test data
-- the id is hardcode to enable references between further test data
INSERT INTO horse (ID, NAME, BREED, MIN_SPEED, MAX_SPEED, CREATED, UPDATED )
VALUES (1, 'Joe', 'Cob', 40.1, 50.0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
  (2, 'Lisa', 'Arab', 40.5, 50.7, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
  (3, 'Jim', 'Andalusian', 40.0, 60.0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

  INSERT INTO jockey (ID, NAME, SKILL, CREATED, UPDATED )
   VALUES (1, 'JAES', 4.1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
     (2, 'Lisa', 3.5, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
     (3, 'Jim', 6.0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());