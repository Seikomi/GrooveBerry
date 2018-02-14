CREATE TABLE Library (
  libraryId int GENERATED BY DEFAULT AS IDENTITY, 
  PRIMARY KEY (libraryId)
);
 
CREATE TABLE Playlist (
  playlistId int GENERATED BY DEFAULT AS IDENTITY,
  name    varchar(255), 
  PRIMARY KEY (playlistId)
);

CREATE TABLE PlaylistSong (
  playlistId int NOT NULL, 
  songId     int NOT NULL, 
  PRIMARY KEY (playlistId, songId)
);

CREATE TABLE Song (
  songId    int GENERATED BY DEFAULT AS IDENTITY, 
  path      varchar(255), 
  fileName  int, 
  libraryId int, 
  songTagId int, 
  PRIMARY KEY (songId)
);

CREATE TABLE SongTag (
  songTagId   int GENERATED BY DEFAULT AS IDENTITY, 
  title       varchar(255), 
  artistName  varchar(255), 
  albumName   varchar(255), 
  year        int, 
  comment     varchar(255), 
  trackNumber int, 
  genreId     int, 
  PRIMARY KEY (songTagId)
);

CREATE TABLE "User" (
  userId    int GENERATED BY DEFAULT AS IDENTITY, 
  name      varchar(255), 
  libraryId int, 
  PRIMARY KEY (userId)
);

ALTER TABLE "User"
  ADD CONSTRAINT FK_Library_User
  FOREIGN KEY (libraryId)
  REFERENCES Library (libraryId);

ALTER TABLE PlaylistSong
  ADD CONSTRAINT FK_Playlist_PlaylistSong
  FOREIGN KEY (playlistId)
  REFERENCES Playlist (playlistId);

ALTER TABLE Song
  ADD CONSTRAINT FK_Song_Library
  FOREIGN KEY (libraryId)
  REFERENCES Library (libraryId);

ALTER TABLE PlaylistSong
  ADD CONSTRAINT FK_Song_PlaylistSong
  FOREIGN KEY (songId)
  REFERENCES Song (songId);

ALTER TABLE Song
  ADD CONSTRAINT FK_Song_SongTag
  FOREIGN KEY (songTagId)
  REFERENCES SongTag (songTagId);
  
