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
  `price`     DECIMAL(18, 6)    NOT NULL,
  `stock`     INT               NOT NULL,
  PRIMARY KEY (`id_book`)
) 