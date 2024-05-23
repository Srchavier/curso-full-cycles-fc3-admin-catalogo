package com.admin.catalogo.domain.video;

import java.util.Objects;

import com.admin.catalogo.domain.ValueObject;

public class ImageMedia extends ValueObject {

    private final String checksum;
    private final String name;
    private final String location;

    private ImageMedia(final String checksum, final String name, final String location) {
        this.checksum =  Objects.requireNonNull(checksum);;
        this.name =  Objects.requireNonNull(name);;
        this.location =  Objects.requireNonNull(location);;
    }

    public static ImageMedia with(final String checksum, final String name, final String location) {
        return new ImageMedia(checksum, name, location);
    }

    public String checksum() {
        return this.checksum;
    }

    public String name() {
        return this.name;
    }

    public String location() {
        return this.location;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ImageMedia)) {
            return false;
        }
        final ImageMedia imageMedia = (ImageMedia) o;
        return Objects.equals(checksum, imageMedia.checksum) && Objects.equals(location, imageMedia.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, location);
    }

}
