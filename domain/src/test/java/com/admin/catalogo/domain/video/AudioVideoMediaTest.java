package com.admin.catalogo.domain.video;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class AudioVideoMediaTest {

    
    @Test
    public void givenValidParams_whenCallsNewImage_ShouldReturnInstance() {
        // given
        final var expectedChecksum = "äbc";
        final var expectedName = "viedo.mp3";
        final var expectedRawLocation = "/imagens/ac";
        final var expectedEncoded = "/imagens/enco";
        final var expectedStatus = MediaStatus.COMPLETED;
        // when

        final var actualImage = AudioVideoMedia.with(expectedChecksum, expectedName, expectedRawLocation, expectedEncoded, expectedStatus);

        // then

        assertNotNull(actualImage);

        assertEquals(expectedChecksum, actualImage.checksum());
        assertEquals(expectedName, actualImage.name());
        assertEquals(expectedRawLocation, actualImage.rawLocation());
        assertEquals(expectedEncoded, actualImage.encodedLocation());
        assertEquals(expectedStatus, actualImage.status());

    }

    @Test
    public void givenTwoImagesWithSameChacksumAndLocation_whenCallsEquals_ShouldReturnTrue() {
        // given
        final var expectedChecksum = "äbc";
        final var expectedLocation = "/imagens/ac";

        // when
        final var img1 = AudioVideoMedia.with(expectedChecksum, "Random", expectedLocation, "/imagens/enco", MediaStatus.COMPLETED);

        final var img2 = AudioVideoMedia.with(expectedChecksum, "Simple", expectedLocation, "/imagens/enco", MediaStatus.COMPLETED);

        // then

        assertEquals(img1, img2);
        assertFalse(img1 == img2);

    }

    @Test
    public void givenInvalidParams_whenCallsWith_ShouldReturnError() {
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with(null, "Simple", "/imagens", "/imagens/enco", MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("qwe", null, "/imagens", "/imagens/enco", MediaStatus.PROCESSING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("123", "Simple", null, "/imagens/enco", MediaStatus.COMPLETED));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("123", "Simple", "/imagens", null, MediaStatus.COMPLETED));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("123", "Simple", "/imagens", "/imagens/enco", null));
    }
    

}

