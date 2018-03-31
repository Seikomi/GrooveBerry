package com.seikomi.grooveberry.rest.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seikomi.grooveberry.entity.Song;
import com.seikomi.grooveberry.exception.ResourceNotFoundException;
import com.seikomi.grooveberry.repository.SongRepository;

@RestController
@RequestMapping({ "/api" })
public class SongController {

	@Autowired
	private SongRepository songRepository;

	@GetMapping("/songs")
	public List<Song> getAllSong() {
		return songRepository.findAll();
	}

	@GetMapping("/songs/{id}")
	public Song getSong(@PathVariable("id") Long songId) {
		return songRepository.findById(songId).orElseThrow(() -> new ResourceNotFoundException("song", "id", songId));
	}

	@PostMapping("/songs")
	public Song createSong(@Valid @RequestBody Song song) {
		return songRepository.save(song);
	}

	@PutMapping("/songs/{id}")
	public Song updateSong(@PathVariable("id") Long songId, @Valid @RequestBody Song songDetails) {
		Song song = songRepository.findById(songId)
				.orElseThrow(() -> new ResourceNotFoundException("song", "id", songId));
		song.setPath(songDetails.getPath());
		song.setSongTag(song.getSongTag());

		return songRepository.save(song);
	}

	@DeleteMapping("/songs/{id}")
	public ResponseEntity<?> deleteSong(@PathVariable("id") Long songId) {
		Song song = songRepository.findById(songId)
				.orElseThrow(() -> new ResourceNotFoundException("song", "id", songId));

		songRepository.delete(song);

		return ResponseEntity.ok().build();
	}

}
