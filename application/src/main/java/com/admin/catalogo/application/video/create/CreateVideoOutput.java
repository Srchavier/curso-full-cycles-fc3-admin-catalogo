package com.admin.catalogo.application.video.create;

import com.admin.catalogo.domain.video.Video;

public record CreateVideoOutput(String id) {

    public static CreateVideoOutput with(final Video aVideo) {
        return new CreateVideoOutput(aVideo.getId().getValue());
    }
}
