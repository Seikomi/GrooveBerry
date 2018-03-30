package com.seikomi.grooveberry.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.seikomi.grooveberry.bo.Genre;
import com.seikomi.grooveberry.bo.Song;
import com.seikomi.grooveberry.bo.SongTag;
import com.seikomi.grooveberry.database.ConnectionH2Database;

public class SongDAOTest {
	private SongDAO songDAO;
	
	Connection connection;
	
	@Rule
	public ExternalResource ressource = new ExternalResource() {

		@Override
		protected void before() throws Throwable {
			connection = ConnectionH2Database.getConnection("jdbc:h2:mem:", "sa", "");
			try (Statement statement = connection.createStatement()) {
				ClassLoader classLoader = getClass().getClassLoader();
				File initDatabaseFile = new File(classLoader.getResource("sql/init.sql").getFile());
				statement.executeUpdate(String.format("RUNSCRIPT FROM '%s'", initDatabaseFile));
			} catch (SQLException e) {
				fail(e.getMessage());
			}
		}
		
		protected void after() {
			try (Statement statement = connection.createStatement()) {
				ClassLoader classLoader = getClass().getClassLoader();
				File initDatabaseFile = new File(classLoader.getResource("sql/dropTables.sql").getFile());
				statement.executeUpdate(String.format("RUNSCRIPT FROM '%s'", initDatabaseFile));
			} catch (SQLException e) {
				fail(e.getMessage());
			}
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
	};

	@Before
	public void setUp() throws Exception {
		songDAO = new SongDAO();
	}

	@After
	public void tearDown() throws Exception {
		songDAO = null;
	}

	@Test
	public void testCreateSongWithoutTag() {
		Song song = new Song();
		Song songCreated = createSong(song, "testPath");

		assertEquals(songCreated.getPath(), song.getPath());
		assertEquals(songCreated.getSongTag(), song.getSongTag());
	}

	@Test
	public void testCreateSongWithTag() {
		SongTag songTag = new SongTag();
		createSongTag(songTag, "testAlbum", "testArtist", "testComment", Genre.ALTERNATIVE, "testTitle", 2018);

		Song song = new Song();
		song.setSongTag(songTag);

		Song songCreated = createSong(song, "testPath");

		assertEquals(songCreated.getPath(), song.getPath());

		assertEquals(songCreated.getSongTag().getAlbumName(), songTag.getAlbumName());
		assertEquals(songCreated.getSongTag().getArtistName(), songTag.getArtistName());
		assertEquals(songCreated.getSongTag().getComment(), songTag.getComment());
		assertEquals(songCreated.getSongTag().getGenre(), songTag.getGenre());
		assertEquals(songCreated.getSongTag().getTitle(), songTag.getTitle());
		assertEquals(songCreated.getSongTag().getYear(), songTag.getYear());
	}
	
	@Test
	@Ignore //TODO
	public void testCreateMultipleSongs() {		
		Song firstSong = new Song();
		Song firstSongCreated = createSong(firstSong, "testPath");
		
		SongTag songTag = new SongTag();
		createSongTag(songTag, "testAlbum", "testArtist", "testComment", Genre.ALTERNATIVE, "testTitle", 2018);

		Song secondSong = new Song();
		secondSong.setSongTag(songTag);

		Song secondSongCreated = createSong(secondSong, "testPath2");
		
		List<Song> songs = songDAO.findAll();
		
		assertEquals(2, songs.size());
		assertEquals(firstSongCreated.getPath(), songs.get(0).getPath());
		assertEquals(secondSongCreated.getPath(), songs.get(1).getPath());
	}

	@Test
	public void testUpdateSongWithoutTag() {
		Song songInDatabase = createSong(new Song(), "testPath");

		Song song = songDAO.find(songInDatabase.getSongId());
		song.setPath("testUpdatePath");

		Song songUpdated = songDAO.update(song);

		assertEquals(song.getPath(), songUpdated.getPath());
		assertEquals(song.getSongTag(), songUpdated.getSongTag());
	}

	@Test
	public void testUpdateSongWithTag() {
		SongTag songTagToCreate = new SongTag();
		createSongTag(songTagToCreate, "testAlbum", "testArtist", "testComment", Genre.ALTERNATIVE, "testTitle", 2018);

		Song songToCreate = new Song();
		songToCreate.setSongTag(songTagToCreate);

		Song songInDatabase = createSong(songToCreate, "testPath");

		Song song = songDAO.find(songInDatabase.getSongId());
		song.setPath("testUpdatePath");

		SongTag songTag = song.getSongTag();
		songTag.setAlbumName("testUpdateAlbum");
		songTag.setArtistName("testUpdateArtist");
		songTag.setComment("testUpdateComment");
		songTag.setGenre(Genre.FUNK);
		songTag.setTitle("testUpdateTitle");
		songTag.setYear(2016);

		Song songUpdated = songDAO.update(song);

		assertEquals(song.getPath(), songUpdated.getPath());

		assertEquals(song.getSongTag().getAlbumName(), songUpdated.getSongTag().getAlbumName());
		assertEquals(song.getSongTag().getArtistName(), songUpdated.getSongTag().getArtistName());
		assertEquals(song.getSongTag().getComment(), songUpdated.getSongTag().getComment());
		assertEquals(song.getSongTag().getGenre(), songUpdated.getSongTag().getGenre());
		assertEquals(song.getSongTag().getTitle(), songUpdated.getSongTag().getTitle());
		assertEquals(song.getSongTag().getYear(), songUpdated.getSongTag().getYear());
	}
	
	@Test
	public void testDeleteSong() {
		Song songToCreate = new Song();
		Song songInDatabase = createSong(songToCreate, "testPath");
		
		songDAO.delete(songInDatabase);
		
		assertNull(songDAO.find(songInDatabase.getSongId()));
	}

	private Song createSong(Song song, String path) {
		song.setPath(path);
		return songDAO.create(song);
	}

	private void createSongTag(SongTag songTag, String albumName, String artistName, String comment, Genre genre,
			String title, int year) {
		songTag.setAlbumName(albumName);
		songTag.setArtistName(artistName);
		songTag.setComment(comment);
		songTag.setGenre(genre);
		songTag.setTitle(title);
		songTag.setYear(year);
	}

}
