package com.admin.catalogo.application.video.create;

import java.util.Set;

import com.admin.catalogo.domain.video.Resource;

public record CreateVideoCommand(
        String title,
        String description,
        int launchedAt,
        double duration,
        boolean opened,
        boolean published,
        String rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> members,
        Resource video,
        Resource trailer,
        Resource banner,
        Resource thumbnail,
        Resource thumbnailHalf) {

    public static CreateVideoCommand with(String title,
            String description,
            int launchedAt,
            double duration,
            boolean opened,
            boolean published,
            String rating,
            Set<String> categories,
            Set<String> genres,
            Set<String> members,
            Resource video,
            Resource trailer,
            Resource banner,
            Resource thumbnail,
            Resource thumbnailHalf) {
        return new CreateVideoCommand(
                title,
                description,
                launchedAt,
                duration,
                opened,
                published,
                rating,
                categories,
                genres,
                members,
                video,
                trailer,
                banner,
                thumbnail,
                thumbnailHalf);
    }

}
