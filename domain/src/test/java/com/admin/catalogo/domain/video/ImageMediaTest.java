package com.admin.catalogo.domain.video;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ImageMediaTest {

    @Test
    public void givenValidParams_whenCallsNewImage_ShouldReturnInstance() {
        // given
        final var expectedChecksum = "äbc";
        final var expectedName = "viedo.mp3";
        final var expectedLocation = "/imagens/ac";

        // when

        final var actualImage = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        // then

        assertNotNull(actualImage);

        assertEquals(expectedChecksum, actualImage.checksum());
        assertEquals(expectedName, actualImage.name());
        assertEquals(expectedLocation, actualImage.location());

    }

    @Test
    public void givenTwoImagesWithSameChacksumAndLocation_whenCallsEquals_ShouldReturnTrue() {
        // given
        final var expectedChecksum = "äbc";
        final var expectedLocation = "/imagens/ac";

        // when
        final var img1 = ImageMedia.with(expectedChecksum, "Random", expectedLocation);

        final var img2 = ImageMedia.with(expectedChecksum, "Simple", expectedLocation);

        // then

        assertEquals(img1, img2);
        assertFalse(img1 == img2);

    }

    @Test
    public void givenInvalidParams_whenCallsWith_ShouldReturnError() {
        assertThrows(NullPointerException.class, () -> ImageMedia.with(null, "Simple", "/imagens"));
        assertThrows(NullPointerException.class, () -> ImageMedia.with("qwe", null, "/imagens"));
        assertThrows(NullPointerException.class, () -> ImageMedia.with("123", "Simple", null));

    }
    

}
