package com.seikomi.grooveberry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seikomi.grooveberry.entity.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Long>{

}
