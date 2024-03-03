package net.kuzukawa.api.artist.service;

import net.kuzukawa.api.artist.entity.ArtistsEntity;
import net.kuzukawa.api.artist.repository.ArtistsRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArtistsApiService {
  private final ArtistsRepository artistsRepository;

  public ArtistsApiService(@NonNull final ArtistsRepository artistsRepository) {
      this.artistsRepository = artistsRepository;
  }

  @NonNull
  public Optional<ArtistsEntity> getById(@NonNull final String username) {
      return artistsRepository.findById(username);
  }
}
