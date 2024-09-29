package com.admin.catalogo.domain.video;

import java.util.Objects;

import com.admin.catalogo.domain.ValueObject;

public class Resource extends ValueObject {
    private final byte[] content;
    private final String contentType;
    private final String name;
    private final Type type;


    public enum Type {
        VIDEO, 
        TRAILER,
        BANNER,
        THUMBNAIL,
        THUMBNAILHALF
    }

    private Resource(final byte[] content, final String contentType, final String name, final Type type) {
        this.content = Objects.requireNonNull(content);
        this.contentType = Objects.requireNonNull(contentType);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }


    public static Resource with(final byte[] content, final String contentType, final String name, final Type type) {
        return new Resource(content, contentType, name, type);
    }

    public byte[] content() {
        return this.content;
    }


    public String contentType() {
        return this.contentType;
    }


    public String name() {
        return this.name;
    }


    public Type type() {
        return this.type;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Resource)) {
            return false;
        }
        Resource resource = (Resource) o;
        return Objects.equals(content, resource.content) && Objects.equals(contentType, resource.contentType) && Objects.equals(name, resource.name) && Objects.equals(type, resource.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, contentType, name, type);
    }


    @Override
    public String toString() {
        return "{" +
            " content='" + content + "'" +
            ", contentType='" + contentType + "'" +
            ", name='" + name + "'" +
            ", type='" + type + "'" +
            "}";
    }


}
