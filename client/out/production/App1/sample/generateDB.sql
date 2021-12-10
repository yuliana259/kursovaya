-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema hrmanagerdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema hrmanagerdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `hrmanagerdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `hrmanagerdb` ;

-- -----------------------------------------------------
-- Table `hrmanagerdb`.`hrmanagers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hrmanagerdb`.`hrmanagers` (
  `Табельный_номер` INT NOT NULL AUTO_INCREMENT,
  `Роль` VARCHAR(45) NOT NULL,
  `Имя_пользователя` VARCHAR(45) NOT NULL,
  `Пароль` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Табельный_номер`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `hrmanagerdb`.`hrmanagers`
(`Роль`,
`Имя_пользователя`,
`Пароль`)
VALUES
('Администратор',
'viktoria',
'1111'),
('Администратор',
    'yuli',
    '1111');

-- -----------------------------------------------------
-- Table `hrmanagerdb`.`вид_образования`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hrmanagerdb`.`вид_образования` (
  `КодВидаОбразования` INT NOT NULL AUTO_INCREMENT,
  `ВидОбразования` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`КодВидаОбразования`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `hrmanagerdb`.`вид_образования`
(`ВидОбразования`)
VALUES
('Очное'),
('Заочное');
-- -----------------------------------------------------
-- Table `hrmanagerdb`.`гражданство`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hrmanagerdb`.`гражданство` (
  `КодГражданства` INT NOT NULL AUTO_INCREMENT,
  `Гражданство` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`КодГражданства`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `hrmanagerdb`.`гражданство`
(`Гражданство`)
VALUES
('Республика Беларусь'),
('Российская Федерация'),
('Украина'),
('Польша'),
('Латвия'),
('Литва');
-- -----------------------------------------------------
-- Table `hrmanagerdb`.`должности`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hrmanagerdb`.`должности` (
  `КодДолжности` INT NOT NULL AUTO_INCREMENT,
  `Должность` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`КодДолжности`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


INSERT INTO `hrmanagerdb`.`должности`
(`Должность`)
VALUES
('Директор по персоналу'),
('Специалист по кадрам'),
('Экономист'),
('Финансовый директор'),
('Продукт-менеджер'),
('Менеджер по проектам'),
('Java разработчик'),
('C# разработчик'),
('Python разработчик');

-- -----------------------------------------------------
-- Table `hrmanagerdb`.`форма_обучения`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hrmanagerdb`.`форма_обучения` (
  `КодФормыОбучения` INT NOT NULL AUTO_INCREMENT,
  `ФормаОбучения` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`КодФормыОбучения`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `hrmanagerdb`.`форма_обучения`
(`ФормаОбучения`)
VALUES
('Платная'),
('Бюджетная');
-- -----------------------------------------------------
-- Table `hrmanagerdb`.`образование`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hrmanagerdb`.`образование` (
  `КодОбразования` INT NOT NULL AUTO_INCREMENT,
  `ДатаПоступления` DATETIME NULL DEFAULT NULL,
  `ДатаОкончания` DATETIME NULL DEFAULT NULL,
  `Специальность` VARCHAR(45) NULL DEFAULT NULL,
  `КодФормыОбучения` INT NULL DEFAULT NULL,
  `КодВидаОбразования` INT NULL DEFAULT NULL,
  PRIMARY KEY (`КодОбразования`),
  INDEX `кодФормыОбучения_INDEX` (`КодФормыОбучения` ASC) VISIBLE,
  INDEX `кодВидаОбразования_INDEX` (`КодВидаОбразования` ASC) VISIBLE,
  CONSTRAINT `ВидОбразованияFK`
    FOREIGN KEY (`КодВидаОбразования`)
    REFERENCES `hrmanagerdb`.`вид_образования` (`КодВидаОбразования`),
  CONSTRAINT `ФормаОбученияFK`
    FOREIGN KEY (`КодФормыОбучения`)
    REFERENCES `hrmanagerdb`.`форма_обучения` (`КодФормыОбучения`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hrmanagerdb`.`подразделение`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hrmanagerdb`.`подразделение` (
  `КодПодразделения` INT NOT NULL AUTO_INCREMENT,
  `Подразделение` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`КодПодразделения`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `hrmanagerdb`.`подразделение`
(`Подразделение`)
VALUES
('Отдел кадров'),
('Экономический отдел'),
('Финансовый отдел'),
('Отдел управления продуктом'),
('Отдел управления проектом'),
('Отдел разработки');

-- -----------------------------------------------------
-- Table `hrmanagerdb`.`семейное_положение`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hrmanagerdb`.`семейное_положение` (
  `КодСемейногоПоложения` INT NOT NULL AUTO_INCREMENT,
  `СемейныйСтатус` VARCHAR(45) NULL DEFAULT NULL,
  `КоличествоДетей` INT NULL DEFAULT NULL,
  PRIMARY KEY (`КодСемейногоПоложения`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `hrmanagerdb`.`семейное_положение`
(`СемейныйСтатус`,
`КоличествоДетей`)
VALUES
('Есть супруг(а)', 1),
('Есть супруг(а)', 2),
('Есть супруг(а)', 3),
('Есть супруг(а)', 4),
('Есть супруг(а)', 5),
('Есть супруг(а)', 6),
('Нет супруг(а)', 1),
('Нет супруг(а)', 2),
('Нет супруг(а)', 3),
('Нет супруг(а)', 4),
('Нет супруг(а)', 5),
('Нет супруг(а)', 6);
-- -----------------------------------------------------
-- Table `hrmanagerdb`.`работники`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hrmanagerdb`.`работники` (
  `Табельный_номер` INT NOT NULL AUTO_INCREMENT,
  `Фамилия` VARCHAR(45) NULL DEFAULT NULL,
  `Имя` VARCHAR(45) NULL DEFAULT NULL,
  `Отчество` VARCHAR(45) NULL DEFAULT NULL,
  `КодГражданства` INT NULL DEFAULT NULL,
  `КодДолжности` INT NULL DEFAULT NULL,
  `КодСемейногоПоложения` INT NULL DEFAULT NULL,
  `КодПодразделения` INT NULL DEFAULT NULL,
  `ДатаПриема` DATETIME NULL DEFAULT NULL,
  `ДатаУвольнения` DATETIME NULL DEFAULT NULL,
  `Пол` VARCHAR(45) NULL DEFAULT NULL,
  `ДатаРождения` DATETIME NULL DEFAULT NULL,
  `Фото` VARCHAR(45) NULL DEFAULT NULL,
  `НомерСоциальногоСтрахования` INT NULL DEFAULT NULL,
  `КодОбразования` INT NULL DEFAULT NULL,
  PRIMARY KEY (`Табельный_номер`),
  INDEX `КодОбразования_INDEX` (`КодОбразования` ASC) VISIBLE,
  INDEX `КодПодразделения_INDEX` (`КодПодразделения` ASC) VISIBLE,
  INDEX `КодСемейногоПоложения_INDEX` (`КодСемейногоПоложения` ASC) VISIBLE,
  INDEX `КодДолжности_INDEX` (`КодДолжности` ASC) VISIBLE,
  INDEX `КодГражданства_INDEX` (`КодГражданства` ASC) VISIBLE,
  CONSTRAINT `hrmanagersFK`
    FOREIGN KEY (`Табельный_номер`)
    REFERENCES `hrmanagerdb`.`hrmanagers` (`Табельный_номер`),
  CONSTRAINT `ГражданствоFK`
    FOREIGN KEY (`КодГражданства`)
    REFERENCES `hrmanagerdb`.`гражданство` (`КодГражданства`),
  CONSTRAINT `ДолжностиFK`
    FOREIGN KEY (`КодДолжности`)
    REFERENCES `hrmanagerdb`.`должности` (`КодДолжности`),
  CONSTRAINT `ОбразованиеFK`
    FOREIGN KEY (`КодОбразования`)
    REFERENCES `hrmanagerdb`.`образование` (`КодОбразования`),
  CONSTRAINT `ПодразделениеFK`
    FOREIGN KEY (`КодПодразделения`)
    REFERENCES `hrmanagerdb`.`подразделение` (`КодПодразделения`),
  CONSTRAINT `СемейноеПоложениеFK`
    FOREIGN KEY (`КодСемейногоПоложения`)
    REFERENCES `hrmanagerdb`.`семейное_положение` (`КодСемейногоПоложения`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
