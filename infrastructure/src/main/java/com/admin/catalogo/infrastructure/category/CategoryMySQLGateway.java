package com.admin.catalogo.infrastructure.category;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.pagination.SearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.admin.catalogo.infrastructure.utils.SpecificationUtils;

@Component
public class CategoryMySQLGateway implements CategoryGateway{

    private final CategoryRepository categoryRepository;
    

    public CategoryMySQLGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(final Category aCategory) {
        return this.categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

    @Override
    public void deleteById(final CategoryID anId) {

       final var anIdValue = anId.getValue();

        if(this.categoryRepository.existsById(anIdValue)){
            this.categoryRepository.deleteById(anIdValue);
        }
        
    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return this.categoryRepository.findById(anId.getValue()).map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return this.categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

    @Override
    public Pagination<Category> findAll(final SearchQuery aQuery) {
            // Paginação
            final var page = PageRequest.of(
            aQuery.page(),
            aQuery.perPage(),
            Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        // Busca dinamica pelo criterio terms (name ou description)
        final var specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult =
                this.categoryRepository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
            );
    }

    private Specification<CategoryJpaEntity> assembleSpecification(String str) {
        return  SpecificationUtils.<CategoryJpaEntity>like("name", str)
                    .or(SpecificationUtils.<CategoryJpaEntity>like("description", str));
    }

    @Override
    public List<CategoryID> existsByIds(Iterable<CategoryID> aCategoriesds) {
        final var ids = StreamSupport.stream(aCategoriesds.spliterator(), false)
            .map(CategoryID::getValue)
            .toList();
        return this.categoryRepository.existsByIds(ids).stream()
                    .map(CategoryID::from)
                    .toList();
    }
    
}
