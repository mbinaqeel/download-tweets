/*
SQLyog Enterprise - MySQL GUI v7.12 
MySQL - 5.7.11-log : Database - twitterwali
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`twitterwali` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `twitterwali`;

/*Table structure for table `lahore_tweets` */

DROP TABLE IF EXISTS `lahore_tweets`;

CREATE TABLE `lahore_tweets` (
  `text` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `favorited` tinyint(4) DEFAULT NULL,
  `favoriteCount` double DEFAULT NULL,
  `replyToSN` text COLLATE utf8mb4_unicode_ci,
  `created` text COLLATE utf8mb4_unicode_ci,
  `truncated` tinyint(4) DEFAULT NULL,
  `replyToSID` text COLLATE utf8mb4_unicode_ci,
  `id` text COLLATE utf8mb4_unicode_ci,
  `replyToUID` text COLLATE utf8mb4_unicode_ci,
  `statusSource` text COLLATE utf8mb4_unicode_ci,
  `screenName` text COLLATE utf8mb4_unicode_ci,
  `retweetCount` double DEFAULT NULL,
  `isRetweet` tinyint(4) DEFAULT NULL,
  `retweeted` tinyint(4) DEFAULT NULL,
  `longitude` text COLLATE utf8mb4_unicode_ci,
  `latitude` text COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
