package com.seikomi.grooveberry.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seikomi.grooveberry.entity.ReadingQueue;
import com.seikomi.grooveberry.entity.Song;
import com.seikomi.grooveberry.repository.ReadingQueueRepository;

@RestController
@RequestMapping({ "/api" })
public class ReadingQueueController {
	
	@Autowired
	private ReadingQueueRepository readingQueueRepository;
	
	@GetMapping("/readingQueue/currentTrack")
	public Song getCurrentTrack() {
		List<ReadingQueue> readingQueues = readingQueueRepository.findAll();
		return readingQueues.size() == 1 ? readingQueues.get(0).getCurrentTrack() : null;
	}

}
