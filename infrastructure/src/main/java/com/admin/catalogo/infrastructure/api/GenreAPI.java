package com.admin.catalogo.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(value = "genres")
@Tag(name = "Genre")
public interface GenreAPI {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> create(@RequestBody CreateGenreRequest input);

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "List all genres paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Pagination<GenreListResponse> list(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") int perpage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String direction);

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Get a genre by it`s identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Genre was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    GenreResponse getById(@PathVariable String id);

    @PutMapping(value = "{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Update a genre by it`s identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre update successfully"),
            @ApiResponse(responseCode = "404", description = "Genre was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UpdateGenreRequest input);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a genre by it`s identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Genre delete successfully"),
            @ApiResponse(responseCode = "404", description = "Genre was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    void deleteById(@PathVariable String id);

}
