package com.admin.catalogo.application.category.retrieve.list;

import com.admin.catalogo.application.UseCase;
import com.admin.catalogo.domain.pagination.SearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
    
}
