package com.kube.training.songservice.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    @Id
    @GeneratedValue
    private Integer songId;

    @Column
    private String name;

    @Column(length = 20)
    private String artist;

    @Column(length = 20)
    private String album;

    @Column(length = 20)
    private String length;

    private Integer resourceId;

    private String year;
}
