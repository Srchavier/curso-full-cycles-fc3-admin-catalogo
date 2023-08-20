package com.admin.catalogo.domain.validation.handler;

import java.util.ArrayList;
import java.util.List;

import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.ValidationHandler;

public class Notification implements ValidationHandler {

    private final List<Error> errors;

    public Notification(List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Throwable anError) {

        return create(new Error(anError.getMessage()));
    }

    public static Notification create(final Error anError) {
        return new Notification(new ArrayList<>()).append(anError);
    }

    @Override
    public Notification append(Error anError) {
        this.errors.add(anError);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler anHandler) {
        this.errors.addAll(anHandler.getErrors());
        return this;
    }

    @Override
    public Notification validate(final Validation aValidation) {
        try {
            aValidation.validate();
        } catch (DomainException e) {
            this.errors.addAll(e.getErrors());
        } catch (final Throwable e) {
            this.errors.add(new Error(e.getMessage()));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }

}
