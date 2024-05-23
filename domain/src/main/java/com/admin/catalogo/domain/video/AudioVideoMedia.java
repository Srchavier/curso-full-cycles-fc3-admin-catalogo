package com.admin.catalogo.domain.video;

import java.util.Objects;

import com.admin.catalogo.domain.ValueObject;

public class AudioVideoMedia extends ValueObject {

    private final String checksum;
    private final String name;
    private final String rawLocation;
    private final String encodedLocation;
    private final MediaStatus status;

    private AudioVideoMedia(
            final String checksum,
            final String name,
            final String rawLocation,
            final String encodedLocation,
            final MediaStatus status) {

        this.checksum = Objects.requireNonNull(checksum);
        this.name = Objects.requireNonNull(name);
        this.rawLocation = Objects.requireNonNull(rawLocation);
        this.encodedLocation = Objects.requireNonNull(encodedLocation);
        this.status = Objects.requireNonNull(status);
    }

    public static AudioVideoMedia with(
            final String checksum,
            final String name,
            final String rawLocation,
            final String encodedLocation,
            final MediaStatus status) {
        return new AudioVideoMedia(checksum, name, rawLocation, encodedLocation, status);
    }

    public String checksum() {
        return this.checksum;
    }

    public String name() {
        return this.name;
    }

    public String rawLocation() {
        return this.rawLocation;
    }

    public String encodedLocation() {
        return this.encodedLocation;
    }

    public MediaStatus status() {
        return this.status;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AudioVideoMedia)) {
            return false;
        }
        final AudioVideoMedia audioImageMedia = (AudioVideoMedia) o;
        return Objects.equals(checksum, audioImageMedia.checksum)
                && Objects.equals(rawLocation, audioImageMedia.rawLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, rawLocation);
    }

}
