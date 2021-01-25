DELETE FROM role_permission;

DELETE FROM permission;
DELETE FROM role;

INSERT INTO "permission" ("id", "symbol", "info") VALUES(1, 'ACCOUNT_PANEL', 'Account Panel');
INSERT INTO "permission" ("id", "symbol", "info") VALUES(2, 'ADMIN_PANEL', 'Admin Panel');
INSERT INTO "permission" ("id", "symbol", "info") VALUES(4, 'PRODUCT_MANAGE_PANEL', 'Product Manage Panel');
INSERT INTO "permission" ("id", "symbol", "info") VALUES(3, 'GUEST_AREA', 'Guest Area');
