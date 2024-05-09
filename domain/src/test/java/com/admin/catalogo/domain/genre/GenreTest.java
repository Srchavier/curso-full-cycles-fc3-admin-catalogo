package com.admin.catalogo.domain.genre;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.domain.exceptions.NotificationException;
import com.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallNewGenre_shouldInstantiateAGenre() {

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
  

    }

    @Test
    public void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveError() {

        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMassage = "'name' should not be null";

        final var actualException = assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMassage, actualException.getErrors().get(0).getMessage());

    }

    @Test
    public void givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveError() {

        final String expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMassage = "'name' should not be black";

        final var actualException = assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMassage, actualException.getErrors().get(0).getMessage());

    }

    @Test
    public void givenInvalidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveError() {

        final String expectedName = """
            	Lorem ipsum platea urna enim mattis, nisl platea etiam at sociosqu porttitor, 
                sollicitudin interdum nullam accumsan. pulvinar varius viverra ultrices in quam varius eu,
                est ut primis dictumst cras tristique, ut nunc sem adipiscing nam feugiat.
                taciti porttitor commodo laoreet lacus faucibus vivamus aptent lorem vitae 
                diam aliquet enim vestibulum, sodales est pharetra etiam mollis mattis magna 
                faucibus platea amet nunc lacus. sollicitudin conubia quam mauris ipsum 
                pellentesque lacus, mattis vestibulum praesent curabitur vulputate hendrerit donec, 
                tincidunt ultricies volutpat dictumst aptent. posuere sagittis et aenean ipsum dolor elit netus placerat, 
                arcu orci malesuada hac a felis tempor, nulla dolor ut sollicitudin dictum non in. 
        """;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMassage = "'name' must be between 1 and 255 character";


        final var actualException = assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMassage, actualException.getErrors().get(0).getMessage());

    }

    @Test
    public void givenAnActiveGenre_whenCallDeactivate_shouldReceiveOK() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, true);

        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());
        assertNotNull(actualGenre.getCreatedAt());

        final var actualCreated = actualGenre.getCreatedAt();
        final var actualUpdated = actualGenre.getUpdatedAt();
        
        Thread.sleep(1L);
        actualGenre.deactivate();

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertEquals(actualCreated, actualGenre.getCreatedAt());
        assertTrue(actualUpdated.isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());

    }

    @Test    
    public void givenAnInactiveGenre_whenCallActivate_shouldReceiveOK() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, false);

        assertFalse(actualGenre.isActive());
        assertNotNull(actualGenre.getDeletedAt());
        assertNotNull(actualGenre.getCreatedAt());

        final var actualCreated = actualGenre.getCreatedAt();
        final var actualUpdated = actualGenre.getUpdatedAt();

        Thread.sleep(1L);

        actualGenre.activate();


        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertEquals(actualCreated, actualGenre.getCreatedAt());
        assertTrue(actualUpdated.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());

    }

    @Test    
    public void givenAnValidInactiveGenre_whenCallUpdateWithActive_shouldReceiveGenreUpdate() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("acao", false);

        assertFalse(actualGenre.isActive());
        assertNotNull(actualGenre.getDeletedAt());
        assertNotNull(actualGenre.getCreatedAt());

        final var actualCreated = actualGenre.getCreatedAt();
        final var actualUpdated = actualGenre.getUpdatedAt();

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);


        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreated, actualGenre.getCreatedAt());
        assertTrue(actualUpdated.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());

    }

    @Test    
    public void givenAnValidActiveGenre_whenCallUpdateWithInactivate_shouldReceiveGenreUpdate() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories =  List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("acao", true);

        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());
        assertNotNull(actualGenre.getCreatedAt());

        final var actualCreated = actualGenre.getCreatedAt();
        final var actualUpdated = actualGenre.getUpdatedAt();

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);


        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreated, actualGenre.getCreatedAt());
        assertTrue(actualUpdated.isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());

    }

    @Test    
    public void givenAnValidGenre_whenCallUpdateWithEmptyName_shouldReceiveNotificationException() throws InterruptedException {
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var actualGenre = Genre.newGenre("acao", false);
        final var expectedErrorCount = 1;
        final var expectedErrorMassage = "'name' should not be null";

        final var actualException = assertThrows(NotificationException.class, () -> {
            actualGenre.update(null, expectedIsActive, expectedCategories);
        });

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMassage, actualException.getErrors().get(0).getMessage());

    }

    @Test    
    public void givenAnValidGenre_whenCallUpdateWithNullName_shouldReceiveNotificationException() throws InterruptedException {
        final var expectedName = "  ";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var actualGenre = Genre.newGenre("acao", false);
        final var expectedErrorCount = 1;
        final var expectedErrorMassage = "'name' should not be black";

        final var actualException = assertThrows(NotificationException.class, () -> {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        });

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMassage, actualException.getErrors().get(0).getMessage());

    }

    @Test    
    public void givenAnValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOK() throws InterruptedException {
        final var expectedName = "acao";
        final var expectedIsActive = true;
        final List<CategoryID> expectedCategories = new ArrayList<>();
        final var actualGenre = Genre.newGenre("acao", true);

        final var actualCreated = actualGenre.getCreatedAt();
        final var actualUpdated = actualGenre.getUpdatedAt();

        assertDoesNotThrow(() -> {
            actualGenre.update(expectedName, expectedIsActive, null);
        });

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreated, actualGenre.getCreatedAt());
        assertTrue(actualUpdated.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());

    }

    @Test    
    public void givenAnValidEmptyCategoriesGenre_whenCallAddCategory_shouldReceiveOK() throws InterruptedException {
        final var expectedName = "acao";
        final var expectedIsActive = true;

        final var serialID = CategoryID.from("123");
        final var moviesID = CategoryID.from("1234");
        final var expectedCategories = List.of(serialID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var actualCreated = actualGenre.getCreatedAt();
        final var actualUpdated = actualGenre.getUpdatedAt();

        assertEquals(0, actualGenre.getCategories().size());

        actualGenre.addCategory(serialID);
        actualGenre.addCategory(moviesID);


        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreated, actualGenre.getCreatedAt());
        assertTrue(actualUpdated.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());

    }

    @Test    
    public void givenAnValidEmptyCategoriesGenre_whenCallAddCategories_shouldReceiveOK() throws InterruptedException {
        final var expectedName = "acao";
        final var expectedIsActive = true;

        final var serialID = CategoryID.from("123");
        final var moviesID = CategoryID.from("1234");
        final var expectedCategories = List.of(serialID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var actualCreated = actualGenre.getCreatedAt();
        final var actualUpdated = actualGenre.getUpdatedAt();

        assertEquals(0, actualGenre.getCategories().size());

        actualGenre.addCategories(expectedCategories);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreated, actualGenre.getCreatedAt());
        assertTrue(actualUpdated.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());

    }

    @Test    
    public void givenAnValidGenreWithTwoCategories_whenCallRemoveCategories_shouldReceiveOK() {
        final var expectedName = "acao";
        final var expectedIsActive = true;

        final var serialID = CategoryID.from("123");
        final var moviesID = CategoryID.from("1234");
        final var expectedCategories = List.of(moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, List.of(serialID, moviesID));

        final var actualCreated = actualGenre.getCreatedAt();
        final var actualUpdated = actualGenre.getUpdatedAt();

        assertEquals(2, actualGenre.getCategories().size());
        
        actualGenre.removeCategory(serialID);

        assertEquals(1, actualGenre.getCategories().size());

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreated, actualGenre.getCreatedAt());
        assertTrue(actualUpdated.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());

    }

    @Test    
    public void givenAnInvalidNullAsCategoryID_whenCallAddCategories_shouldReceiveOK() {
        final var expectedName = "acao";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        final var actualCreated = actualGenre.getCreatedAt();
        final var actualUpdated = actualGenre.getUpdatedAt();

        assertEquals(0, actualGenre.getCategories().size());
        
        actualGenre.addCategory(null);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreated, actualGenre.getCreatedAt());
        assertEquals(actualUpdated,actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());

    }

    
    @Test    
    public void givenAnInvalidNullAsCategoryID_whenCallRemoveCategories_shouldReceiveOK() {
        final var expectedName = "acao";
        final var expectedIsActive = true;

        final var serialID = CategoryID.from("123");
        final var moviesID = CategoryID.from("1234");
        final var expectedCategories = List.of(serialID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        final var actualCreated = actualGenre.getCreatedAt();
        final var actualUpdated = actualGenre.getUpdatedAt();

        assertEquals(2, actualGenre.getCategories().size());
        
        actualGenre.removeCategory(null);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreated, actualGenre.getCreatedAt());
        assertEquals(actualUpdated,actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());

    }

    @Test    
    public void givenAnValidEmptyCategoriesGenre_whenCallAddCategoriesWitEmptyList_shouldReceiveOK() throws InterruptedException {
        final var expectedName = "acao";
        final var expectedIsActive = true;

        final var expectedCategories = List.<CategoryID>of();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var actualCreated = actualGenre.getCreatedAt();
        final var actualUpdated = actualGenre.getUpdatedAt();

        assertEquals(0, actualGenre.getCategories().size());

        actualGenre.addCategories(expectedCategories);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreated, actualGenre.getCreatedAt());
        assertEquals(actualUpdated, actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());

    }

    @Test    
    public void givenAnValidEmptyCategoriesGenre_whenCallAddCategoriesWitNullList_shouldReceiveOK() throws InterruptedException {
        final var expectedName = "acao";
        final var expectedIsActive = true;

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var actualCreated = actualGenre.getCreatedAt();
        final var actualUpdated = actualGenre.getUpdatedAt();

        assertEquals(0, actualGenre.getCategories().size());

        actualGenre.addCategories(null);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(List.<CategoryID>of(), actualGenre.getCategories());
        assertEquals(actualCreated, actualGenre.getCreatedAt());
        assertEquals(actualUpdated, actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());

    }
}
