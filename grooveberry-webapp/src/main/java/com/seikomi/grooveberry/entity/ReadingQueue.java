package com.seikomi.grooveberry.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "reading_queue")
public class ReadingQueue {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long readingQueueId;
	
	@OneToOne
	private Song currentTrack;
	
	@OneToMany
	private List<Song> list;

	public long getReadingQueueId() {
		return readingQueueId;
	}

	public void setReadingQueueId(long readingQueueId) {
		this.readingQueueId = readingQueueId;
	}

	public Song getCurrentTrack() {
		return currentTrack;
	}

	public void setCurrentTrack(Song currentTrack) {
		this.currentTrack = currentTrack;
	}

	public List<Song> getList() {
		return list;
	}

	public void setList(List<Song> list) {
		this.list = list;
	}
	
	

}
