package com.admin.catalogo.domain.video;

public record VideoSearchQuery(
    int page,
    int perPage,
    String terms,
    String sort,
    String direction
) {

}
