package com.admin.catalogo.infrastructure.api.controllers;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.validation.handler.Notification;
import com.admin.catalogo.infrastructure.api.CategoryAPI;
import com.admin.catalogo.infrastructure.category.models.CategoryListResponse;
import com.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;

    private final GetCategoryByIdUseCase byIdUseCase;

    private final UpdateCategoryUseCase updateCategoryUseCase;

    private final DeleteCategoryUseCase deleteCategoryUseCase;

    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
            CreateCategoryUseCase createCategoryUseCase,
            GetCategoryByIdUseCase byIdUseCase,
            UpdateCategoryUseCase updateCategoryUseCase,
            DeleteCategoryUseCase deleteCategoryUseCase,
            ListCategoriesUseCase listCategoriesUseCase
            ) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.byIdUseCase = Objects.requireNonNull(byIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull( listCategoriesUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(@Valid CreateCategoryRequest input) {

        CreateCategoryCommand aCommand = CreateCategoryCommand.with(
            input.name(), 
            input.description(),
            input.active() != null ? input.active() : true);
        

        final Function<Notification, ResponseEntity<?>> orError = 
                ResponseEntity.unprocessableEntity()::body;


         final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand)
                .fold(orError, onSuccess);
    }

    @Override
    public Pagination<CategoryListResponse> listCategory(
        String search, 
        int page, 
        int perpage, 
        String sort, 
        String direction) {
            return listCategoriesUseCase
                .execute(new CategorySearchQuery(page, perpage, search, sort, direction))
                .map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryResponse getById(String id) {
       return CategoryApiPresenter.present
                .compose(byIdUseCase::execute)
                .apply(id);
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryRequest input) {
        var aCommand = UpdateCategoryCommand.with(
            id,
            input.name(), 
            input.description(),
            input.active() != null ? input.active() : true);
        

        final Function<Notification, ResponseEntity<?>> orError = 
                ResponseEntity.unprocessableEntity()::body;


         final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.ok().body(output);

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(orError, onSuccess);
    }

    @Override
    public void deleteById(final String id) {
       deleteCategoryUseCase.execute(id);
    }

}
