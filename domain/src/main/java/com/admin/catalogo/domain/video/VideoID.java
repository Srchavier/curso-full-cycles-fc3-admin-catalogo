package com.admin.catalogo.domain.video;

import java.util.Objects;
import java.util.UUID;

import com.admin.catalogo.domain.Identifier;

public class VideoID extends Identifier {
    
    private final String value;

    private VideoID(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static VideoID from(final String anId) {
        return new VideoID(anId.toLowerCase());
    }

    public static VideoID from(final UUID anId) {
        return VideoID.from(anId.toString());
    }

    public static VideoID unique() {
        return VideoID.from(UUID.randomUUID());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof VideoID)) {
            return false;
        }
        final VideoID VideoID = (VideoID) o;
        return Objects.equals(value, VideoID.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

}
