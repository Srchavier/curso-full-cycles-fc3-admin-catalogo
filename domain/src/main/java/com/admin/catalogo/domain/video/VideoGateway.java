package com.admin.catalogo.domain.video;

import java.util.List;
import java.util.Optional;

import com.admin.catalogo.domain.pagination.Pagination;

public interface VideoGateway {

    Video create(final Video aVideo);

    void deleteById(final VideoID anId);

    Optional<Video> findById(final VideoID anId);

    Video update(final Video aVideo);

    Pagination<Video> findAll(final VideoSearchQuery aQuery);

    List<VideoID> existsByIds(Iterable<VideoID> ids);
}
