package com.admin.catalogo.domain.category;

import java.util.List;
import java.util.Optional;

import com.admin.catalogo.domain.pagination.SearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;

public interface CategoryGateway {

    Category create(final Category aCategory);

    void deleteById(final CategoryID anId);

    Optional<Category> findById(final CategoryID anId);

    Category update(final Category aCategory);

    Pagination<Category> findAll(final SearchQuery aQuery);

    List<CategoryID> existsByIds(Iterable<CategoryID> ids);
    
}
