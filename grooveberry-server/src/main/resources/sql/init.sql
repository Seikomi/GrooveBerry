DROP TABLE IF EXISTS `playlist`;
CREATE TABLE `playlist` (
  `playlist_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL
);

DROP TABLE IF EXISTS `playlist_songs`;
CREATE TABLE `playlist_songs` (
  `playlist_playlist_id` bigint(20) NOT NULL,
  `songs_song_id` bigint(20) NOT NULL
);

DROP TABLE IF EXISTS `reading_queue`;
CREATE TABLE `reading_queue` (
  `reading_queue_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `current_track_song_id` bigint(20) DEFAULT NULL
);

INSERT INTO reading_queue(current_track_song_id) SELECT NULL WHERE NOT EXISTS (SELECT * FROM reading_queue);

DROP TABLE IF EXISTS `reading_queue_list`;
CREATE TABLE `reading_queue_list` (
  `reading_queue_reading_queue_id` bigint(20) NOT NULL,
  `list_song_id` bigint(20) NOT NULL
);

DROP TABLE IF EXISTS `song`;
CREATE TABLE `song` (
  `song_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `path` varchar(255) DEFAULT NULL,
  `song_tag_song_tag_id` bigint(20) DEFAULT NULL
);

DROP TABLE IF EXISTS `song_tag`;
CREATE TABLE `song_tag` (
  `song_tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `album_name` varchar(255) DEFAULT NULL,
  `artist_name` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `genre` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `track_number` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL
);

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL
);

DROP TABLE IF EXISTS `user_playlists`;
CREATE TABLE `user_playlists` (
  `user_user_id` bigint(20) NOT NULL,
  `playlists_playlist_id` bigint(20) NOT NULL
);
