package net.kuzukawa.api.artist.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.kuzukawa.api.artist.api.ArtistsApi;
import net.kuzukawa.api.artist.entity.ArtistsEntity;
import net.kuzukawa.api.artist.model.Artist;
import net.kuzukawa.api.artist.model.GetArtistByUsername200Response;
import net.kuzukawa.api.artist.service.ArtistsApiService;

@RestController
public class ArtistApiController implements ArtistsApi {
  private final ArtistsApiService artistsApiService;

  public ArtistApiController(@NonNull final ArtistsApiService artistsApiService) {
    this.artistsApiService = artistsApiService;
  }

  @Override
  public ResponseEntity<GetArtistByUsername200Response> getArtistByUsername(String username) {
    Optional<ArtistsEntity> oArtistsEntity = artistsApiService.getById(username);
    if(oArtistsEntity.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    final ArtistsEntity artistsEntity = oArtistsEntity.get();
    GetArtistByUsername200Response response = new GetArtistByUsername200Response()
      .artistName(artistsEntity.getArtistName())
      .artistGenre(artistsEntity.getArtistGenre())
      .albumsRecorded(artistsEntity.getAlbumsRecorded());

    return new ResponseEntity<>(
            response,
            HttpStatus.OK);
  }

  @Override
  public ResponseEntity<List<Artist>> getArtists(@Valid Integer limit, @Valid Integer offset) {
    List<Artist> response = new ArrayList<Artist>();
    response.add(new Artist()
      .artistName("List Musician 1")
      .artistGenre("rock")
      .albumsRecorded(1)
      .username("List Musician 1"));

      response.add(new Artist()
      .artistName("List Musician 2")
      .artistGenre("jazz")
      .albumsRecorded(2)
      .username("List Musician 2"));

      response.add(new Artist()
      .artistName("List Musician 3")
      .artistGenre("classic")
      .albumsRecorded(3)
      .username("List Musician 3"));

      return new ResponseEntity<>(
        response,
        HttpStatus.OK
      );
    }

  @Override
  public ResponseEntity<Void> postArtist(@Valid Artist artist) {
    //insert logic
    return new ResponseEntity<>(
      null,
      HttpStatus.OK
    );
  }
}
