package com.admin.catalogo.domain.category;

import java.util.Optional;

import com.admin.catalogo.domain.pagination.Pagination;

public interface CategoryGateway {

    Category create(Category aCategory);

    void deleteById(CategoryID anId);

    Optional<Category> finById(Category aCategory);

    Category update(Category aCategory);

    Pagination<Category> findAll(CategorySearchQuery aQuery);
    
}
