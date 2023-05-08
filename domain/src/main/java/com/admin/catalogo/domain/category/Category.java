/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.admin.catalogo.domain.category;

import java.time.Instant;

import com.admin.catalogo.domain.AggregateRoot;
import com.admin.catalogo.domain.validation.ValidationHandler;

public class Category extends AggregateRoot<CategoryID>{

    
    private String name;
    private String description;
    private Boolean active;
    private Instant createAt;
    private Instant updateAt;
    private Instant deleteAt;

    public Category(CategoryID anId, final String aName, final String aDescription, final Boolean isActive,
            final Instant aCreationDate, final Instant aUpdateDate, final Instant aDeleteDate) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createAt = aCreationDate;
        this.updateAt = aUpdateDate;
        this.deleteAt = aDeleteDate;
    }

    public static Category newCategory(final String aName, final String aDescription, final Boolean isActive) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deleteAt = isActive ? null: now; 
        return new Category(id, aName, aDescription, isActive, now, now, deleteAt);
    }


      /**
     * @return String return the name
     */
    @Override
    public CategoryID getId() {
        return id;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Boolean return the Active
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * @param Active the Active to set
     */
    public void setActive(Boolean Active) {
        this.active = Active;
    }

    /**
     * @return LocalDate return the createAt
     */
    public Instant getCreateAt() {
        return createAt;
    }

    /**
     * @param createAt the createAt to set
     */
    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    /**
     * @return LocalDate return the updateAt
     */
    public Instant getUpdateAt() {
        return updateAt;
    }

    /**
     * @param updateAt the updateAt to set
     */
    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * @return LocalDate return the deleteAt
     */
    public Instant getDeleteAt() {
        return deleteAt;
    }

    /**
     * @param deleteAt the deleteAt to set
     */
    public void setDeleteAt(Instant deleteAt) {
        this.deleteAt = deleteAt;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category deactivate() {
        if(getDeleteAt() == null) {
            this.deleteAt = Instant.now();
        }

        this.active = false;
        this.updateAt = Instant.now();
        return this;
    }

    public Category activate() {
        this.deleteAt = null;
        this.active = true;
        this.updateAt = Instant.now();
        return this;
    }

    public Category update(final String aName, final String aDescription, final boolean isActive) {
        if(isActive) {
            activate();
        } else {
            deactivate();
        }

        this.name = aName;
        this.description = aDescription;
        this.updateAt = Instant.now();
        
        return this;
    }

}
