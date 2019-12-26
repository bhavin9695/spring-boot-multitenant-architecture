create schema altimetrik;

CREATE TABLE `altimetrik`.`user` (
  `id` BIGINT(20) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `tenant` VARCHAR(255) NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC));

ALTER TABLE `altimetrik`.`user`
CHANGE COLUMN `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT ;


create schema google;

CREATE TABLE `google`.`customer` (
  `id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `google`.`customer`
CHANGE COLUMN `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT ;

create schema apple;

CREATE TABLE `apple`.`customer` (
  `id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `apple`.`customer`
CHANGE COLUMN `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT ;
