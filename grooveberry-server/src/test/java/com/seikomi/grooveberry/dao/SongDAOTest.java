package com.seikomi.grooveberry.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.seikomi.grooveberry.bo.Genre;
import com.seikomi.grooveberry.bo.Song;
import com.seikomi.grooveberry.bo.SongTag;
import com.seikomi.grooveberry.database.ConnectionH2Database;

public class SongDAOTest {
	private SongDAO songDAO;
	private Connection connection;
	
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Before
	public void setUp() throws Exception {
		Path fileDatabasePath = Paths.get(temporaryFolder.getRoot().getPath() + "dbSongDAOTest");
		connection = ConnectionH2Database.getConnection("jdbc:h2:file:" + fileDatabasePath, "sa", "");
		try (Statement statement = connection.createStatement()){
			ClassLoader classLoader = getClass().getClassLoader();
			File initDatabaseFile = new File(classLoader.getResource("sql/init.sql").getFile());
			statement.executeUpdate(String.format("RUNSCRIPT FROM '%s'", initDatabaseFile));
		} catch (SQLException e) {
			fail(e.getMessage());
		}
		
		songDAO = new SongDAO();
	}

	@After
	public void tearDown() throws Exception {
		try (Statement statement = connection.createStatement()){
			ClassLoader classLoader = getClass().getClassLoader();
			File initDatabaseFile = new File(classLoader.getResource("sql/dropTables.sql").getFile());
			statement.executeUpdate(String.format("RUNSCRIPT FROM '%s'", initDatabaseFile));
		} catch (SQLException e) {
			fail(e.getMessage());
		}
		
		songDAO = null;
	}

	@Test
	public void testCreateSongWithoutTag() {
		Song song = new Song();
		song.setPath("testPath");
		
		Song songCreated = songDAO.create(song);
		
		assertEquals(songCreated.getPath(), song.getPath());
		assertEquals(songCreated.getSongTag(), song.getSongTag());
	}
	
	@Test
	public void testCreateSongWithTag() {
		SongTag songTag = new SongTag();
		songTag.setAlbumName("testAlbum");
		songTag.setArtistName("testArtist");
		songTag.setComment("testComment");
		songTag.setGenre(Genre.ALTERNATIVE);
		songTag.setTitle("testTitle");
		songTag.setYear(2018);
		
		Song song = new Song();
		song.setPath("testPath");
		song.setSongTag(songTag);
		
		Song songCreated = songDAO.create(song);
		
		assertEquals(songCreated.getPath(), song.getPath());
		
		assertEquals(songCreated.getSongTag().getAlbumName(), songTag.getAlbumName());
		assertEquals(songCreated.getSongTag().getArtistName(), songTag.getArtistName());
		assertEquals(songCreated.getSongTag().getComment(), songTag.getComment());
		assertEquals(songCreated.getSongTag().getGenre(), songTag.getGenre());
		assertEquals(songCreated.getSongTag().getTitle(), songTag.getTitle());
		assertEquals(songCreated.getSongTag().getYear(), songTag.getYear());
	}

}
