package net.kuzukawa.api.artist.repository;

import net.kuzukawa.api.artist.entity.ArtistsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistsRepository extends JpaRepository<ArtistsEntity, String> {
    @NonNull
    Optional<ArtistsEntity> findById(@NonNull final String username);
}
