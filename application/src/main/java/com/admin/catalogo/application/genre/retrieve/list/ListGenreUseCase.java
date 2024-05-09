package com.admin.catalogo.application.genre.retrieve.list;

import com.admin.catalogo.application.UseCase;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {

}
