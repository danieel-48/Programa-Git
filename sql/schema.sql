CREATE SCHEMA IF NOT EXISTS `bookstore`;

USE `bookstore`;

CREATE TABLE IF NOT EXISTS `users` (
  `id_user`   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`      VARCHAR(150)    NOT NULL,
  `email`     VARCHAR(100)    NOT NULL,
  `password`  VARCHAR(255)    NOT NULL,
  `role`      ENUM('ADMIN','EMPLOYEE') NOT NULL DEFAULT 'EMPLOYEE',
  `is_active` TINYINT(1)      NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `users_email_unique` (`email`)
);

CREATE TABLE IF NOT EXISTS `books` (
  `id_book`   BIGINT UNSIGNED   NOT NULL AUTO_INCREMENT,
  `title`     VARCHAR(250)      NOT NULL,
  `author`    VARCHAR(100)      NULL DEFAULT NULL,
  `category`  VARCHAR(100)      NULL DEFAULT NULL,
  `language`  VARCHAR(80)       NULL DEFAULT NULL,
  `price`     DECIMAL(18, 6)    NOT NULL,
  `stock`     INT               NOT NULL,
  PRIMARY KEY (`id_book`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET @language_column_exists = (
  SELECT COUNT(*)
  FROM information_schema.columns
  WHERE table_schema = DATABASE()
    AND table_name = 'books'
    AND column_name = 'language'
);

SET @add_language_sql = IF(
  @language_column_exists = 0,
  'ALTER TABLE `books` ADD COLUMN `language` VARCHAR(80) NULL DEFAULT NULL AFTER `category`',
  'SELECT 1'
);

PREPARE add_language_statement FROM @add_language_sql;
EXECUTE add_language_statement;
DEALLOCATE PREPARE add_language_statement;