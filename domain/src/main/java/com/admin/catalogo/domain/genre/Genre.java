package com.admin.catalogo.domain.genre;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.admin.catalogo.domain.AggregateRoot;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotificationException;
import com.admin.catalogo.domain.utils.InstantUtils;
import com.admin.catalogo.domain.validation.ValidationHandler;
import com.admin.catalogo.domain.validation.handler.Notification;

public class Genre extends AggregateRoot<GenreID> {

    private String name;

    private boolean active;

    private List<CategoryID> categories;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Genre(GenreID anId) {
        super(anId);
    }

    protected Genre(final GenreID anId, final String aName, final boolean isActive, final List<CategoryID> categories, final Instant aCreatedAt, final Instant aUpdatedAt, final Instant aDeletedAt) {
        super(anId);
        this.name = aName;
        this.active = isActive;
        this.categories = categories;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
        this.deletedAt = aDeletedAt;

        selfValidate();
    }

    public static Genre newGenre(final String aName, final boolean isActive) {
        final var anId = GenreID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;
        return new Genre(anId, aName, isActive, new ArrayList<>(), now, now, deletedAt);

    }


    public static Genre with(final GenreID anId, final String aName, final boolean isActive, final List<CategoryID> categories, final Instant aCreatedAt, final Instant aUpdatedAt, final Instant aDeletedAt) {
        return new Genre(anId, aName, isActive, categories, aCreatedAt, aUpdatedAt, aDeletedAt);
    }

    public static Genre with(final Genre anGenre) {
        return new Genre(anGenre.id, anGenre.name, anGenre.active,new ArrayList<>(anGenre.categories), anGenre.createdAt, anGenre.updatedAt, anGenre.deletedAt);
    }

    @Override
    public void validate(ValidationHandler handler) {
        new GenreValidator(this, handler).validate();;
        
    }

    public Genre addCategory(CategoryID anCategoryID) {
        if(anCategoryID == null) {
            return this;
        }

        this.categories.add(anCategoryID);
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Genre removeCategory(CategoryID anCategoryID) {
        if(anCategoryID == null) {
            return this;
        }

        this.categories.remove(anCategoryID);
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Genre update(final String aName,final boolean isActive, final List<CategoryID> categories) {
        if(isActive){
            activate();
        } else {
            deactivate();
        }

        this.name = aName;
        this.categories = new ArrayList<>(categories != null ? categories: Collections.emptyList());
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if(notification.hasError()) {
            throw new NotificationException("Failed to create a Aggregate Genre", notification);
        }
    }

    public Genre activate() {
        this.deletedAt = null;
        this.updatedAt = InstantUtils.now();
        this.active = true;

        return this;
    }

    public Genre deactivate() {

        if(getDeletedAt() == null) {
            this.deletedAt = InstantUtils.now();
        }
        
        this.updatedAt = InstantUtils.now();
        this.active = false;

        return this;
    }

    public String getName() {
        return this.name;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean getActive() {
        return this.active;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

}
