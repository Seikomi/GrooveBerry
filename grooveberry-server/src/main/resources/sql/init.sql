-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: grooveberry-database
-- ------------------------------------------------------
-- Server version	5.7.21-log

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
-- Table structure for table `playlist`
--

DROP TABLE IF EXISTS `playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `playlist` (
  `playlist_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`playlist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlist`
--

LOCK TABLES `playlist` WRITE;
/*!40000 ALTER TABLE `playlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `playlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `playlist_songs`
--

DROP TABLE IF EXISTS `playlist_songs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `playlist_songs` (
  `playlist_playlist_id` bigint(20) NOT NULL,
  `songs_song_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_lrsgr2dlemht0r2mk7d819rgr` (`songs_song_id`),
  KEY `FK8318cakf6hjujs4mqw1219jkg` (`playlist_playlist_id`),
  CONSTRAINT `FK6o7g64232ylhk6o9kb2hihgqr` FOREIGN KEY (`songs_song_id`) REFERENCES `song` (`song_id`),
  CONSTRAINT `FK8318cakf6hjujs4mqw1219jkg` FOREIGN KEY (`playlist_playlist_id`) REFERENCES `playlist` (`playlist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlist_songs`
--

LOCK TABLES `playlist_songs` WRITE;
/*!40000 ALTER TABLE `playlist_songs` DISABLE KEYS */;
/*!40000 ALTER TABLE `playlist_songs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reading_queue`
--

DROP TABLE IF EXISTS `reading_queue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reading_queue` (
  `reading_queue_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `current_track_song_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`reading_queue_id`),
  KEY `FKpbqo4ju73yfjo7hu42co8yl3v` (`current_track_song_id`),
  CONSTRAINT `FKpbqo4ju73yfjo7hu42co8yl3v` FOREIGN KEY (`current_track_song_id`) REFERENCES `song` (`song_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO reading_queue(current_track_song_id) SELECT NULL WHERE NOT EXISTS (SELECT * FROM reading_queue);

--
-- Dumping data for table `reading_queue`
--

LOCK TABLES `reading_queue` WRITE;
/*!40000 ALTER TABLE `reading_queue` DISABLE KEYS */;
/*!40000 ALTER TABLE `reading_queue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reading_queue_list`
--

DROP TABLE IF EXISTS `reading_queue_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reading_queue_list` (
  `reading_queue_reading_queue_id` bigint(20) NOT NULL,
  `list_song_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_72e6advt46yhis3b6eedfn4cm` (`list_song_id`),
  KEY `FK1kblkk7ajv554s4cfhw3t65qo` (`reading_queue_reading_queue_id`),
  CONSTRAINT `FK1kblkk7ajv554s4cfhw3t65qo` FOREIGN KEY (`reading_queue_reading_queue_id`) REFERENCES `reading_queue` (`reading_queue_id`),
  CONSTRAINT `FKbgbl8mw91e5sgsmf4wxprjwh` FOREIGN KEY (`list_song_id`) REFERENCES `song` (`song_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reading_queue_list`
--

LOCK TABLES `reading_queue_list` WRITE;
/*!40000 ALTER TABLE `reading_queue_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `reading_queue_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `song`
--

DROP TABLE IF EXISTS `song`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `song` (
  `song_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `path` varchar(255) DEFAULT NULL,
  `song_tag_song_tag_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`song_id`),
  KEY `FKa2lxcfhf0xw9j8j8l3m7bgbnc` (`song_tag_song_tag_id`),
  CONSTRAINT `FKa2lxcfhf0xw9j8j8l3m7bgbnc` FOREIGN KEY (`song_tag_song_tag_id`) REFERENCES `song_tag` (`song_tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `song`
--

LOCK TABLES `song` WRITE;
/*!40000 ALTER TABLE `song` DISABLE KEYS */;
/*!40000 ALTER TABLE `song` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `song_tag`
--

DROP TABLE IF EXISTS `song_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `song_tag` (
  `song_tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `album_name` varchar(255) DEFAULT NULL,
  `artist_name` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `genre` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `track_number` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  PRIMARY KEY (`song_tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `song_tag`
--

LOCK TABLES `song_tag` WRITE;
/*!40000 ALTER TABLE `song_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `song_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_playlists`
--

DROP TABLE IF EXISTS `user_playlists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_playlists` (
  `user_user_id` bigint(20) NOT NULL,
  `playlists_playlist_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_8ukj5h8y3l6pum273utms7teq` (`playlists_playlist_id`),
  KEY `FKt8odlorqjukvg59r6xuexr481` (`user_user_id`),
  CONSTRAINT `FKmq78dd9ox1lptino4s23p326j` FOREIGN KEY (`playlists_playlist_id`) REFERENCES `playlist` (`playlist_id`),
  CONSTRAINT `FKt8odlorqjukvg59r6xuexr481` FOREIGN KEY (`user_user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_playlists`
--

LOCK TABLES `user_playlists` WRITE;
/*!40000 ALTER TABLE `user_playlists` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_playlists` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-02 14:18:47
