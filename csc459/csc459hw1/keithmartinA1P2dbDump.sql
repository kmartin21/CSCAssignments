-- MySQL dump 10.13  Distrib 5.5.47, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: keithmartinA1P2db
-- ------------------------------------------------------
-- Server version	5.5.47-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `allowed_rating`
--

DROP TABLE IF EXISTS `allowed_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `allowed_rating` (
  `star_rating` int(11) NOT NULL,
  PRIMARY KEY (`star_rating`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `allowed_rating`
--

LOCK TABLES `allowed_rating` WRITE;
/*!40000 ALTER TABLE `allowed_rating` DISABLE KEYS */;
INSERT INTO `allowed_rating` VALUES (1),(2),(3),(4),(5);
/*!40000 ALTER TABLE `allowed_rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Action'),(2,'Comedy'),(3,'Drama'),(4,'Horror'),(5,'Sport');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie`
--

DROP TABLE IF EXISTS `movie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movie` (
  `movie_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  `length` varchar(25) NOT NULL,
  PRIMARY KEY (`movie_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `movie_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie`
--

LOCK TABLES `movie` WRITE;
/*!40000 ALTER TABLE `movie` DISABLE KEYS */;
INSERT INTO `movie` VALUES (1,1,'Mad Max','2 hr 00 min'),(2,3,'Selma','2 hr 07 min'),(3,2,'Inside out','1 hr 34 min'),(4,3,'Brooklyn','1 hr 51 min'),(5,3,'Spotlight','2 hr 07 min'),(6,1,'Star Wars: Episode VII - The Force Awakens','2 hr 16 min'),(7,4,'It follows','1 hr 34 min'),(8,2,'Shaun the sheep movie','1 hr 24 min'),(9,3,'Room','1 hr 53 min'),(10,1,'Mission: Impossible Rogue Nation','2 hr 11 min'),(11,2,'Paddington','1 hr 36 min'),(12,1,'The Martian','2 hr 14 min'),(13,3,'Timbuktu','1 hr 37 min'),(14,5,'Creed','2 hr 12 min'),(15,3,'Gett: The trail of Viviane Amsalem','1 hr 55 min'),(16,3,'Amy','2 hr 08 min'),(17,3,'Phoenix','1 hr 38 min'),(18,2,'What we do in the shadows','1 hr 26 min'),(19,2,'Spy','1 hr 57 min'),(20,1,'Sicario','2 hr 01 min'),(21,3,'45 years','1 hr 33 min'),(22,5,'Red Army','1 hr 25 min'),(23,2,'Tangerine','1 hr 28 min'),(24,2,'Wild Tales','2 hr 02 min'),(25,2,'Appropriate behavior','1 hr 30 min'),(26,4,'The Shining','2 hr 26 min'),(27,4,'Halloween','1 hr 31 min'),(28,4,'The Thing','1 hr 49 min'),(29,5,'Cinderella Man','2 hr 24 min'),(30,5,'Any Given Sunday','2 hr 42 min');
/*!40000 ALTER TABLE `movie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preference`
--

DROP TABLE IF EXISTS `preference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preference` (
  `user_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`category_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `preference_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `preference_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preference`
--

LOCK TABLES `preference` WRITE;
/*!40000 ALTER TABLE `preference` DISABLE KEYS */;
INSERT INTO `preference` VALUES (8,1),(9,1),(10,1),(16,1),(21,1),(24,1),(25,1),(8,2),(9,2),(10,2),(16,2),(19,2),(20,2),(24,2),(25,2),(1,3),(2,3),(5,3),(19,3),(25,3),(1,5),(4,5),(5,5),(10,5),(19,5),(23,5);
/*!40000 ALTER TABLE `preference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating`
--

DROP TABLE IF EXISTS `rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rating` (
  `user_id` int(11) NOT NULL,
  `star_rating` int(11) NOT NULL,
  `movie_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`movie_id`),
  KEY `star_rating` (`star_rating`),
  KEY `movie_id` (`movie_id`),
  CONSTRAINT `rating_ibfk_4` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rating_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rating_ibfk_3` FOREIGN KEY (`star_rating`) REFERENCES `allowed_rating` (`star_rating`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating`
--

LOCK TABLES `rating` WRITE;
/*!40000 ALTER TABLE `rating` DISABLE KEYS */;
INSERT INTO `rating` VALUES (1,1,7),(3,1,19),(4,1,18),(4,1,19),(8,1,22),(10,1,15),(10,1,16),(10,1,17),(17,1,10),(18,1,19),(22,1,15),(23,1,1),(23,1,20),(25,1,22),(2,2,16),(5,2,5),(5,2,9),(11,2,13),(11,2,21),(12,2,9),(15,2,6),(25,2,7),(2,3,13),(7,3,8),(9,3,8),(9,3,11),(13,3,19),(14,3,5),(20,3,11),(21,3,1),(21,3,12),(25,3,21),(2,4,10),(4,4,14),(4,4,22),(8,4,3),(9,4,1),(16,4,24),(25,4,1),(1,5,2),(1,5,4),(6,5,25),(8,5,1),(8,5,24),(9,5,6),(16,5,23),(19,5,3),(19,5,21),(19,5,24),(23,5,14),(23,5,22),(24,5,19),(25,5,24);
/*!40000 ALTER TABLE `rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Christina','Walker'),(2,'Richard','Adams'),(3,'Jessica','Campbell'),(4,'Sean','Washington'),(5,'Shawn','Bell'),(6,'Stephen','Wilson'),(7,'Stephanie','Wood'),(8,'Cheryl','Young'),(9,'John','Howard'),(10,'Susan','Simmons'),(11,'Aaron','Bailey'),(12,'Margaret','Sanders'),(13,'Maria','Martinez'),(14,'Eric','Gonzalez'),(15,'Bruce','James'),(16,'Doris','Ross'),(17,'Earl','Collins'),(18,'Paul','Johnson'),(19,'Lillian','Hall'),(20,'Jane','Brown'),(21,'Emily','Williams'),(22,'Gregory','Lopez'),(23,'Evelyn','Price'),(24,'Keith','Lee'),(25,'Dennis','Brooks');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `watched_movie`
--

DROP TABLE IF EXISTS `watched_movie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `watched_movie` (
  `user_id` int(11) NOT NULL,
  `movie_id` int(11) NOT NULL,
  `position` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`,`movie_id`),
  KEY `movie_id` (`movie_id`),
  CONSTRAINT `watched_movie_ibfk_2` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `watched_movie_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `watched_movie`
--

LOCK TABLES `watched_movie` WRITE;
/*!40000 ALTER TABLE `watched_movie` DISABLE KEYS */;
INSERT INTO `watched_movie` VALUES (1,2,'Full'),(1,4,'Full'),(1,7,'Partial'),(2,10,'Full'),(2,13,'Full'),(2,16,'Full'),(3,19,'Partial'),(4,14,'Full'),(4,18,'Partial'),(4,19,'Partial'),(4,22,'Full'),(5,5,'Partial'),(5,9,'Partial'),(6,25,'Full'),(7,8,'Partial'),(8,1,'Partial'),(8,3,'Full'),(8,22,'Partial'),(8,24,'Full'),(9,1,'Full'),(9,6,'Full'),(9,8,'Full'),(9,11,'Full'),(10,15,'Partial'),(10,16,'Partial'),(10,17,'Partial'),(11,13,'Full'),(11,21,'Full'),(12,9,'Full'),(13,19,'Full'),(14,5,'Partial'),(15,6,'Partial'),(16,23,'Full'),(16,24,'Full'),(17,10,'Partial'),(18,19,'Full'),(19,3,'Full'),(19,21,'Partial'),(19,24,'Full'),(20,11,'Full'),(21,1,'Full'),(21,12,'Full'),(22,15,'Partial'),(23,1,'Partial'),(23,14,'Full'),(23,20,'Partial'),(23,22,'Full'),(24,19,'Full'),(25,1,'Partial'),(25,7,'Partial'),(25,21,'Partial'),(25,22,'Partial'),(25,24,'Full');
/*!40000 ALTER TABLE `watched_movie` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-22  2:17:47
