DELETE FROM role_permission;
DELETE FROM user_account_role;
DELETE FROM permission;
DELETE FROM role;

INSERT INTO "permission" ("id", "name", "description") VALUES(1, 'READ_LAB1', 'Read');
INSERT INTO "permission" ("id", "name", "description") VALUES(2, 'READ_LAB2', 'Read');
INSERT INTO "permission" ("id", "name", "description") VALUES(3, 'WRITE_LAB2', 'Read');
