package com.admin.catalogo.infrastructure.genre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.admin.catalogo.MySQLGatewayTest;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.genre.GenreID;
import com.admin.catalogo.domain.pagination.SearchQuery;
import com.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.admin.catalogo.infrastructure.genre.persistence.GenreCategoryJpaEntity;
import com.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private GenreMySQLGateway gatewayGenreMySQLGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testeDependenciesInjected() {
        assertNotNull(categoryMySQLGateway);
        assertNotNull(gatewayGenreMySQLGateway);
        assertNotNull(genreRepository);

    }

    @Test
    public void givenAValidGenre_whenCallsCreateGenre_shouldPersistGenre() {
        final var filmes = categoryMySQLGateway.create(Category.newCategory("Filmes", null, true));

        final var expectedName = "acao";
        final var expectedIsAtive = true;
        final var expectedCategories = List.of(filmes.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsAtive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        final var actualGenre = gatewayGenreMySQLGateway.create(aGenre);

        assertEquals(1, genreRepository.count());

        assertEquals(aGenre.getId(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsAtive, actualGenre.getActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());

        final var aGenreSave = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, aGenreSave.getName());
        assertEquals(expectedIsAtive, aGenreSave.isActive());
        assertEquals(expectedCategories, aGenreSave.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), aGenreSave.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), aGenreSave.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), aGenreSave.getDeletedAt());
        assertNull(aGenreSave.getDeletedAt());

    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersistGenre() {
        final var expectedName = "acao";
        final var expectedIsAtive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, expectedIsAtive);

        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        final var actualGenre = gatewayGenreMySQLGateway.create(aGenre);

        assertEquals(1, genreRepository.count());

        assertEquals(aGenre.getId(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsAtive, actualGenre.getActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());

        final var aGenreSave = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, aGenreSave.getName());
        assertEquals(expectedIsAtive, aGenreSave.isActive());
        assertEquals(expectedCategories, aGenreSave.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), aGenreSave.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), aGenreSave.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), aGenreSave.getDeletedAt());
        assertNull(aGenreSave.getDeletedAt());

    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre() {

        final var filmes = categoryMySQLGateway.create(Category.newCategory("Filmes", null, true));

        final var series = categoryMySQLGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "acao";
        final var expectedIsAtive = true;
        final var expectedCategories = List.<CategoryID>of(filmes.getId(), series.getId());

        final var aGenre = Genre.newGenre("acd", expectedIsAtive);

        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        assertEquals("acd", aGenre.getName());
        assertEquals(0, aGenre.getCategories().size());

        assertEquals(1, genreRepository.count());

        final var newGenre = Genre.with(aGenre).update(expectedName, expectedIsAtive, expectedCategories);
        final var actualGenre = gatewayGenreMySQLGateway.update(newGenre);

        assertEquals(1, genreRepository.count());

        assertEquals(aGenre.getId(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsAtive, actualGenre.getActive());
        assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var aGenreSave = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, aGenreSave.getName());
        assertEquals(expectedIsAtive, aGenreSave.isActive());
        assertEquals(sorted(expectedCategories), sorted(aGenreSave.getCategoryIDs()));
        assertEquals(aGenre.getCreatedAt(), aGenreSave.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), aGenreSave.getDeletedAt());
        assertNull(aGenreSave.getDeletedAt());

    }

    @Test
    public void givenAValidGenreWithCategories_whenCallsUpdateGenreCleaningCategories_shouldPersistGenre() {

        final var filmes = categoryMySQLGateway.create(Category.newCategory("Filmes", null, true));

        final var series = categoryMySQLGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "acao";
        final var expectedIsAtive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre("acd", expectedIsAtive);
        aGenre.addCategories(List.of(filmes.getId(), series.getId()));

        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        assertEquals("acd", aGenre.getName());
        assertEquals(2, aGenre.getCategories().size());

        assertEquals(1, genreRepository.count());

        final var newGenre = Genre.with(aGenre).update(expectedName, expectedIsAtive, expectedCategories);
        final var actualGenre = gatewayGenreMySQLGateway.update(newGenre);

        assertEquals(1, genreRepository.count());

        assertEquals(aGenre.getId(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsAtive, actualGenre.getActive());
        assertIterableEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var aGenreSave = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, aGenreSave.getName());
        assertEquals(expectedIsAtive, aGenreSave.isActive());
        assertEquals(expectedCategories, aGenreSave.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), aGenreSave.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), aGenreSave.getDeletedAt());
        assertNull(aGenreSave.getDeletedAt());

    }

    private List<CategoryID> sorted(List<CategoryID> categoryIDs) {
        return categoryIDs.stream().sorted(Comparator.comparing(CategoryID::getValue)).toList();
    }

    @Test
    public void givenAValidGenreInactive_whenCallsUpdateGenreActivating_shouldPersistGenre() {

        final var expectedName = "acao";
        final var expectedIsAtive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, false);

        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        assertEquals(false, aGenre.isActive());
        assertNotNull(aGenre.getDeletedAt());
        assertEquals(1, genreRepository.count());

        final var newGenre = Genre.with(aGenre).update(expectedName, expectedIsAtive, expectedCategories);
        final var actualGenre = gatewayGenreMySQLGateway.update(newGenre);

        assertEquals(1, genreRepository.count());

        assertEquals(aGenre.getId(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsAtive, actualGenre.getActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());

        final var aGenreSave = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, aGenreSave.getName());
        assertEquals(expectedIsAtive, aGenreSave.isActive());
        assertEquals(expectedCategories, aGenreSave.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), aGenreSave.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(aGenreSave.getDeletedAt());

    }

    @Test
    public void givenAValidGenreActive_whenCallsUpdateGenreInactivating_shouldPersistGenre() {

        final var expectedName = "acao";
        final var expectedIsAtive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, true);

        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        assertEquals(true, aGenre.isActive());
        assertNull(aGenre.getDeletedAt());
        assertEquals(1, genreRepository.count());

        final var newGenre = Genre.with(aGenre).update(expectedName, expectedIsAtive, expectedCategories);
        final var actualGenre = gatewayGenreMySQLGateway.update(newGenre);

        assertEquals(1, genreRepository.count());

        assertEquals(aGenre.getId(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsAtive, actualGenre.getActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());

        final var aGenreSave = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, aGenreSave.getName());
        assertEquals(expectedIsAtive, aGenreSave.isActive());
        assertEquals(expectedCategories, aGenreSave.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), aGenreSave.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(aGenreSave.getDeletedAt());

    }

    @Test
    public void givenAPrePersistedGenre_whenCallsDeleteById_shouldDeleteGenre() {

        final var aGenre = Genre.newGenre("acao", true);
        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        assertEquals(1, genreRepository.count());

        gatewayGenreMySQLGateway.deleteById(expectedId);

        assertEquals(0, genreRepository.count());

    }

    @Test
    public void givenAPrePersistedGenre_whenCallsFindByIs_shouldReturnGenre() {
        final var filmes = categoryMySQLGateway.create(Category.newCategory("Filmes", null, true));
        final var series = categoryMySQLGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "acao";
        final var expectedIsAtive = true;
        final var expectedCategories = List.<CategoryID>of(filmes.getId(), series.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsAtive);
        aGenre.addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        assertEquals(1, genreRepository.count());

        final var actualGenre = gatewayGenreMySQLGateway.findById(expectedId).get();

        assertEquals(aGenre.getId(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsAtive, actualGenre.getActive());
        assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidGenreId_whenCallsFindByIs_shouldReturnEmpty() {

        final var expectedId = GenreID.from("234");

        final var actualGenre = gatewayGenreMySQLGateway.findById(expectedId);

        assertTrue(actualGenre.isEmpty());
    }

    @Test
    public void givenAnInvalidGenre_whenCallsDeleteById_shouldReturnOk() {

        assertEquals(0, genreRepository.count());

        gatewayGenreMySQLGateway.deleteById(GenreID.from("123"));

        assertEquals(0, genreRepository.count());

    }

    @Test
    public void givenEmptyGenres_whenCallFinfAll_shouldReturnEmptyList() {

        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection);

        final var actualPage = gatewayGenreMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.items().size());
        assertEquals(expectedTotal, actualPage.total());

    }

    @ParameterizedTest
    @CsvSource({
            "aç,0,10,1,1,Ação",
            "dr,0,10,1,1,Drama",
            "com,0,10,1,1,Comedia romatica",
            "cie,0,10,1,1,Ficção cientifica",
            "ter,0,10,1,1,Terror"
    })
    public void givenAValidTerm_whenCallFinfAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final long expectedCountItems,
            final long expectedTotal,
            final String expectedGenreName) {

        mockGenres();

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, "name", "asc");

        final var actualPage = gatewayGenreMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedCountItems, actualPage.items().size());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedGenreName, actualPage.items().get(0).getName());

    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Ação",
            "name,desc,0,10,5,5,Terror",
            "createdAt,asc,0,10,5,5,Comedia romatica",
            "createdAt,desc,0,10,5,5,Ficção cientifica"
    })
    public void givenAValidSortAndDirection_whenCallFinfAll_shouldReturnFiltered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final long expectedCountItems,
            final long expectedTotal,
            final String expectedGenreName) {

        mockGenres();

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, "", expectedSort, expectedDirection);

        final var actualPage = gatewayGenreMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedCountItems, actualPage.items().size());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedGenreName, actualPage.items().get(0).getName());

    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Ação;Comedia romatica",
            "1,2,2,5,Drama;Ficção cientifica",
            "2,2,1,5,Terror"
    })
    public void givenAValidSortAndDirection_whenCallFinfAll_shouldReturnFiltered(
            final int expectedPage,
            final int expectedPerPage,
            final long expectedCountItems,
            final long expectedTotal,
            final String expectedGenres) {

        mockGenres();

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, "", "name", "asc");

        final var actualPage = gatewayGenreMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedCountItems, actualPage.items().size());
        assertEquals(expectedTotal, actualPage.total());

        int index = 0;
        for (String expectedGenreName : expectedGenres.split(";")) {
            assertEquals(expectedGenreName, actualPage.items().get(index).getName());
            index++;
        }

    }

    private void mockGenres() {
        genreRepository.saveAllAndFlush(List.of(
                GenreJpaEntity.from(Genre.newGenre("Comedia romatica", true)),
                GenreJpaEntity.from(Genre.newGenre("Ação", true)),
                GenreJpaEntity.from(Genre.newGenre("Drama", true)),
                GenreJpaEntity.from(Genre.newGenre("Terror", true)),
                GenreJpaEntity.from(Genre.newGenre("Ficção cientifica", true))));
    }

}
