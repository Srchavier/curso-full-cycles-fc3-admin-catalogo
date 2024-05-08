package com.admin.catalogo.application.category.retrieve.list;

import java.util.Objects;

import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.pagination.SearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {

    private final CategoryGateway categoryGateway;
    

    public DefaultListCategoriesUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }


    @Override
    public Pagination<CategoryListOutput> execute(SearchQuery aQuery) {
        return this.categoryGateway.findAll(aQuery)
                .map(CategoryListOutput::from);
    }
    
}
