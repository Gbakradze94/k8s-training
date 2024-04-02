package com.kube.training.songservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.kube.training.songservice.domain.Song;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {
}
