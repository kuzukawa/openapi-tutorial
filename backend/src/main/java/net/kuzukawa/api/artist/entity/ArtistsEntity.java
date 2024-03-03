package net.kuzukawa.api.artist.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@ToString
@Table(name = "artists")
public class ArtistsEntity {
    @Id
    @Column(name = "username", nullable = false, updatable = true)
    private String username;

    @Column(name = "artist_name", nullable = false, length = 255)
    private String artistName;

    @Column(name = "artist_genre", nullable = false, length = 255)
    private String artistGenre;

    @Column(name = "albums_recorded", nullable = false)
    private int albumsRecorded;
}
