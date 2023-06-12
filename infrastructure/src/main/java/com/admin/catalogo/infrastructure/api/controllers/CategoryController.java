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
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.validation.handler.Notification;
import com.admin.catalogo.infrastructure.api.CategoryAPI;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(@Valid CreateCategoryApiInput input) {

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
    public Pagination<?> listCategory(String search, int page, int perpage, String sort, String direction) {
        return null;
    }

}
