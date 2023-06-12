package com.admin.catalogo.infrastructure.category.persistence;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;

@Entity
@Table(name = "category")
public class CategoryJpaEntity {

    @Id
    private  String id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;


    public CategoryJpaEntity() {

    }

    private CategoryJpaEntity(
            final String id,  
            final String name, 
            final String description,
            final Boolean active, 
            final Instant createdAt, 
            final Instant updatedAt, 
            final Instant deletedAt
            ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static CategoryJpaEntity from(final Category aCategory) {
        return new CategoryJpaEntity(
                aCategory.getId().getValue(), 
                aCategory.getName(), 
                aCategory.getDescription(), 
                aCategory.isActive(), 
                aCategory.getCreatedAt(), 
                aCategory.getUpdatedAt(), 
                aCategory.getDeletedAt()
            );

    }

    public Category toAggregate() {
        return Category.with(
            CategoryID.from(getId()),
            getName(), 
            getDescription(), 
            getActive(), 
            getCreatedAt(), 
            getUpdatedAt(), 
            getDeletedAt()
        );

    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isActive() {
        return this.active;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    
}
