package com.seikomi.grooveberry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seikomi.grooveberry.entity.ReadingQueue;

@Repository
public interface ReadingQueueRepository extends JpaRepository<ReadingQueue, Long> {

}
