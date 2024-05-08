package com.admin.catalogo.infrastructure.api;

import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.category.models.CategoryListResponse;
import com.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(value = "categories")
@Tag(name = "Categories")
public interface CategoryAPI {

        @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseStatus(code = HttpStatus.CREATED)
        @Operation(summary = "Create a new category")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Created successfully"),
                        @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
                        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        })
        ResponseEntity<?> createCategory(@RequestBody @Valid CreateCategoryRequest apiInput);

        @GetMapping
        @ResponseStatus(code = HttpStatus.OK)
        @Operation(summary = "List all categories paginated")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Listed successfully"),
                        @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
                        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        })
        Pagination<CategoryListResponse> listCategory(
                        @RequestParam(name = "search", required = false, defaultValue = "") String search,
                        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                        @RequestParam(name = "perPage", required = false, defaultValue = "10") int perpage,
                        @RequestParam(name = "sort", required = false, defaultValue = "name") String sort,
                        @RequestParam(name = "dir", required = false, defaultValue = "asc") String direction);

        @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE )
        @ResponseStatus(code = HttpStatus.OK)
        @Operation(summary = "Get a category by it`s identifier")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Category retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "Category not found"),
                        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        })
        CategoryResponse getById(@PathVariable String id);


        @PutMapping(value = "{id}")
        @ResponseStatus(code = HttpStatus.OK)
        @Operation(summary = "Update a category by it`s identifier")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Category update successfully"),
                        @ApiResponse(responseCode = "404", description = "Category not found"),
                        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        })
        ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UpdateCategoryRequest input);

        @DeleteMapping(value = "{id}")
        @ResponseStatus(code = HttpStatus.NO_CONTENT)
        @Operation(summary = "Delete a category by it`s identifier")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Category delete successfully"),
                        @ApiResponse(responseCode = "404", description = "Category not found"),
                        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        })
        void deleteById(@PathVariable String id);

}
