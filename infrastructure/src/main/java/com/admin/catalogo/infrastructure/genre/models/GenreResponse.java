package com.admin.catalogo.infrastructure.genre.models;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenreResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("categories_id") List<String> categories,
    @JsonProperty("is_active") Boolean active,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt,
    @JsonProperty("deleted_at") Instant deletedAt
) {

}
