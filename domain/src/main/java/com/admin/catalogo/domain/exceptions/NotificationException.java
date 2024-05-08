package com.admin.catalogo.domain.exceptions;

import com.admin.catalogo.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(String aMessage, Notification notification ) {
        super(aMessage, notification.getErrors());
    }

}
