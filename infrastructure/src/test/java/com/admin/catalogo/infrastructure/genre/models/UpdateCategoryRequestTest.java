package com.admin.catalogo.infrastructure.genre.models;

import java.io.IOException;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import com.admin.catalogo.JacksonTest;

@JacksonTest
public class UpdateCategoryRequestTest {


    @Autowired
    private JacksonTester<UpdateGenreRequest> json;

    @Test
    public void testMarshall() throws IOException {

        final var expectedName = "Filmes";
        final var expectedCategories = List.of("123");
        final var expectedActive = true;

        final var response = new UpdateGenreRequest(
            expectedName,
            expectedCategories,
            expectedActive);

        final var actualJson = json.write(response);

        Assertions.assertThat(actualJson)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.categories_id", expectedCategories)
            .hasJsonPathValue("$.is_active", expectedActive);

    }
    
    @Test
    public void testUnMarshall() throws IOException {

        final var expectedName = "Filmes";
        final var expectedCategory = "123";
        final var expectedActive = true;
        
        final var jsonString = """
            {
                "name":"%s",
                "categories_id":["%s"],
                "is_active": %s
            }
            """
            .formatted(expectedName, expectedCategory, expectedActive);


         final var actualJson = json.parse(jsonString); 

        Assertions.assertThat(actualJson)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory))
            .hasFieldOrPropertyWithValue("active", expectedActive);
    }
}
