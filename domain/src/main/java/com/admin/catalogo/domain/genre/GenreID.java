package com.admin.catalogo.domain.genre;

import java.util.Objects;
import java.util.UUID;

import com.admin.catalogo.domain.Identifier;

public class GenreID extends Identifier {

    private final String value;

    private GenreID(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static GenreID unique() {
        return GenreID.from(UUID.randomUUID());
    }

    public static GenreID from(final String anId) {
        return new GenreID(anId);
    }

    public static GenreID from(final UUID anId) {
        return new GenreID(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof GenreID)) {
            return false;
        }
        final GenreID genreID = (GenreID) o;
        return Objects.equals(value, genreID.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
